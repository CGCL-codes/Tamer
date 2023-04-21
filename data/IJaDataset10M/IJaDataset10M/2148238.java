package com.manning.gwtip.bookstore.server;

import com.manning.gwtip.bookstore.AbstractTestDAO;
import com.manning.gwtip.bookstore.client.model.Author;
import com.manning.gwtip.bookstore.client.model.Book;
import com.manning.gwtip.bookstore.client.model.Category;
import com.manning.gwtip.bookstore.client.model.Review;
import com.manning.gwtip.bookstore.service.BookstoreService;
import com.manning.gwtip.bookstore.service.BookstoreServiceImpl;
import com.manning.gwtip.bookstore.test.Util;
import com.totsp.gwt.beans.server.BeanMapping;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author cooper
 */
public class BookstoreServiceServletTest extends AbstractTestDAO {

    public BookstoreServiceServletTest(String testName) {
        super(testName);
    }

    public void xtestFindBooksByCategory() throws Exception {
        System.out.println("findBooksByCategory");
        String categoryName = "";
        BookstoreServiceServlet instance = new BookstoreServiceServlet();
        List expResult = null;
        List result = instance.findBooksByCategory(categoryName);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    public void xtestStoreAuthor() throws Exception {
        System.out.println("storeAuthor");
        Author author = null;
        BookstoreServiceServlet instance = new BookstoreServiceServlet();
        Author expResult = null;
        Author result = instance.storeAuthor(author);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    public void xtestFindBookById() throws Exception {
        System.out.println("findBookById");
        int bookId = 0;
        BookstoreServiceServlet instance = new BookstoreServiceServlet();
        Book expResult = null;
        Book result = instance.findBookById(bookId);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    public void xtestFindBooksByAuthor() throws Exception {
        System.out.println("findBooksByAuthor");
        int authorId = 0;
        BookstoreServiceServlet instance = new BookstoreServiceServlet();
        List expResult = null;
        List result = instance.findBooksByAuthor(authorId);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    public void xtestFindAuthorsByName() throws Exception {
        System.out.println("findAuthorsByName");
        String firstName = "";
        String lastName = "";
        BookstoreServiceServlet instance = new BookstoreServiceServlet();
        List expResult = null;
        List result = instance.findAuthorsByName(firstName, lastName);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    public void xtestCreateReview() throws Exception {
        System.out.println("createReview");
        int bookId = 0;
        Review review = null;
        BookstoreServiceServlet instance = new BookstoreServiceServlet();
        Review expResult = null;
        Review result = instance.createReview(bookId, review);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    public void xtestFindAllCategories() throws Exception {
        System.out.println("findAllCategories");
        BookstoreServiceServlet instance = new BookstoreServiceServlet();
        List expResult = null;
        List result = instance.findAllCategories();
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    public void xtestSetService() {
        System.out.println("setService");
        BookstoreService service = null;
        BookstoreServiceServlet instance = new BookstoreServiceServlet();
        instance.setService(service);
        fail("The test case is a prototype.");
    }
}
