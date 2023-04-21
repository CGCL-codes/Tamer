package ru.nnstu.medialib.service;

import java.util.List;
import ru.nnstu.medialib.dao.BookDAO;
import ru.nnstu.medialib.dao.BookDAOImpl;
import ru.nnstu.medialib.domain.Book;

public class BookServiceImpl implements BookService {

    BookDAO bookDAO;

    public BookServiceImpl() {
        super();
        bookDAO = new BookDAOImpl();
    }

    @Override
    public void addBook(Book book) {
        bookDAO.saveBook(book);
    }

    @Override
    public List<Book> listBook() {
        return bookDAO.listBook();
    }

    @Override
    public void removeBook(Long id) {
        bookDAO.removeBook(id);
    }
}
