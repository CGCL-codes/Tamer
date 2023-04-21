package coyousoft.javaee.bookstore.dao;

import java.util.List;
import coyousoft.javaee.bookstore.cart.ShoppingCart;
import coyousoft.javaee.bookstore.jdo.Books;

public interface BooksDao {

    public Books getBook(Long bookId) throws Exception;

    public List<Books> getBookList() throws Exception;

    public void buyBook(Long bookId, int quantity) throws Exception;

    public void buyBooks(ShoppingCart cart) throws Exception;
}
