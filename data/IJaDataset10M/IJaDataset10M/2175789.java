package com.asl.library.controllers.upload;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import com.asl.library.document.services.BookService;

/**
 * @author asl
 * 
 */
public class UploadBookController extends SimpleFormController {

    private BookService bookService = null;

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        ModelAndView modelAndView = new ModelAndView(getSuccessView());
        UploadFormBean formBean = (UploadFormBean) command;
        bookService.storeBook(formBean.getName(), formBean.getCategory(), formBean.getTags(), formBean.getFile().getInputStream(), formBean.getFile().getOriginalFilename());
        return modelAndView;
    }

    @Override
    protected ModelAndView processFormSubmission(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        return super.processFormSubmission(request, response, command, errors);
    }

    @Override
    protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response, BindException errors, Map controlModel) throws Exception {
        return new ModelAndView(getFormView());
    }

    /**
	 * @return the bookService
	 */
    public BookService getBookService() {
        return bookService;
    }

    /**
	 * @param bookService
	 *            the bookService to set
	 */
    public void setBookService(BookService bookService) {
        this.bookService = bookService;
    }
}
