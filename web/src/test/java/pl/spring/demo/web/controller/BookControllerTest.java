package pl.spring.demo.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import pl.spring.demo.service.BookService;
import pl.spring.demo.to.BookTo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
@WebAppConfiguration
public class BookControllerTest {

	@Autowired
	BookService bookService;
    @Autowired
    private WebApplicationContext wac;
    
	private MockMvc mockMvc;

	@Before
	public void setup() {
		 this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	public void testDeleteBook() throws Exception {
		// given
		final Long id = 1L;
		final String title = "Title";
		BookTo bookTo = new BookTo(1L, title, "Author");
		Mockito.when(bookService.findBookById(id)).thenReturn(bookTo);
		// when
		ResultActions response = this.mockMvc.perform(get("/book-by-id?id=" + id)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON));
		// then
		Mockito.verify(bookService).deleteBook(id);
		response.andExpect(status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.redirectedUrl("/confirmation"))
				.andExpect(MockMvcResultMatchers.flash().attributeExists("title"))
	            .andExpect(MockMvcResultMatchers.flash().attribute("title", title));
	}

}
