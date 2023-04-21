package com.ksevindik.acegi.portlet.samples.libraryservice;

import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.acegisecurity.AcegiSecurityException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BookAction {

    private LibraryService libraryService;

    private String bookName;

    private String bookAuthor;

    private Log logger = LogFactory.getLog(getClass());

    public String insertBook() {
        try {
            Book book = new Book(bookName, bookAuthor);
            libraryService.insertBook(book);
        } catch (AcegiSecurityException ex) {
            logger.error(ex);
            FacesMessage msg = new FacesMessage("Operation not allowed", "Sorry, you are not permitted to add a new public book!");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } finally {
            bookName = "";
            bookAuthor = "";
        }
        return "";
    }

    public String insertPrivateBook() {
        try {
            Book book = new Book(bookName, bookAuthor);
            libraryService.insertPrivateBook(book);
        } catch (AcegiSecurityException ex) {
            logger.error(ex);
            FacesMessage msg = new FacesMessage("Operation not allowed", "Sorry, you are not permitted to add a new private book!");
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } finally {
            bookName = "";
            bookAuthor = "";
        }
        return "";
    }

    public void setLibraryService(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }
}
