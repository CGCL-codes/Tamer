package meta.library.model.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import meta.library.model.bean.Book;
import meta.library.model.dao.BookDao;
import meta.library.model.service.BookManager;
import org.hibernate.Hibernate;

/**
 * @author Biao Zhang
 *
 */
public class BookManagerImpl extends BaseManagerImpl<BookDao, Book> implements BookManager {

    @Override
    public Book addBook(String title, String author, String press, String isbn, String catalog, String description, int copy) {
        Book book = new Book(title, author, press, isbn, catalog, description, copy);
        dao.save(book);
        return book;
    }

    @Override
    public void addBookCover(int bookId, InputStream bookCover) {
        Book book = dao.findById(bookId);
        try {
            book.setCover(Hibernate.createBlob(bookCover));
            dao.save(book);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @Override
    public InputStream getBookCover(int bookId) {
        Book book = dao.findById(bookId);
        InputStream bookCover;
        if (book != null && book.getCover() != null) {
            try {
                bookCover = book.getCover().getBinaryStream();
            } catch (SQLException sqle) {
                bookCover = null;
                sqle.printStackTrace();
            }
        } else {
            bookCover = null;
        }
        return bookCover;
    }

    @Override
    public Book addBook(String title, String author, String press, String isbn, String catalog, String description, int copy, InputStream cover) {
        Book book = new Book(title, author, press, isbn, catalog, description, copy);
        if (cover != null) {
            try {
                book.setCover(Hibernate.createBlob(cover));
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        dao.save(book);
        return book;
    }

    @Override
    public Book getByTitle(String title) {
        Book book = dao.getByTitle(title);
        return book;
    }

    @Override
    public List<Book> getByAuthor(String author) {
        List<Book> books = dao.getByAuthor(author);
        return books;
    }

    @Override
    public List<Book> getByPress(String press) {
        List<Book> books = dao.getByPress(press);
        return books;
    }

    @Override
    public List<Book> searchByTitle(String title) {
        List<Book> books = dao.searchByTitle(title);
        return books;
    }

    @Override
    public List<Book> search(String keywords) {
        String[] words = keywords.split(" ");
        List<Book> result = new LinkedList<Book>();
        for (String keyword : words) {
            List<Book> books = dao.searchByTitle(keyword);
            result.addAll(books);
            books = dao.searchByAuthor(keyword);
            result.addAll(books);
            books = dao.searchByPress(keyword);
            result.addAll(books);
        }
        return result;
    }

    @Override
    public List<Book> searchByAuthor(String author) {
        List<Book> books = dao.searchByAuthor(author);
        return books;
    }

    @Override
    public List<Book> searchByPress(String press) {
        List<Book> books = dao.searchByPress(press);
        return books;
    }
}
