package com.manning.gwtip.bookstore.service;

import com.manning.gwtip.bookstore.model.Author;
import com.manning.gwtip.bookstore.model.Book;
import com.manning.gwtip.bookstore.model.Category;
import com.manning.gwtip.bookstore.model.Review;
import java.util.List;

public interface BookstoreService {

    public Author storeAuthor(Author author) throws BookstoreServiceException;

    public Book storeBook(Book book) throws BookstoreServiceException;

    public Review createReview(int bookId, Review review) throws BookstoreServiceException;

    public List<Category> findAllCategories() throws BookstoreServiceException;

    public List<Author> findAuthorsByName(String firstName, String lastName) throws BookstoreServiceException;

    public List<Author> findAllAuthors() throws BookstoreServiceException;

    public Book findBookById(int bookId) throws BookstoreServiceException;

    public List<Book> findBooksByAuthor(int authorId) throws BookstoreServiceException;

    public List<Book> findAllBooks() throws BookstoreServiceException;

    public void deleteBook(Book book) throws BookstoreServiceException;

    public List findBooksByCategory(String categoryName) throws BookstoreServiceException;
}
