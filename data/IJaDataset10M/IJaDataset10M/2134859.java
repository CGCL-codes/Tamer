package com.negatlov.serviceImpl;

import com.negatlov.Dao.BookDao;
import com.negatlov.DaoImpl.BookDaoImpl;
import com.negatlov.pojo.Book;
import com.negatlov.service.BookService;

public class BookServiceImpl implements BookService {

    BookDao bookDao;

    public void add(Book book) {
        bookDao.addBook(book);
    }

    public void delete(Book book) {
    }

    public void update(Book book) {
    }
}
