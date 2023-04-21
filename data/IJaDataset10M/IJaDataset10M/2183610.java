package de.t5book.services.impl;

import org.apache.tapestry5.ValueEncoder;
import de.t5book.entities.Book;
import de.t5book.services.BookService;

public class BookEncoder implements ValueEncoder<Book> {

    private BookService bookService;

    public BookEncoder(BookService bookService) {
        this.bookService = bookService;
    }

    public String toClient(Book book) {
        return String.valueOf(book.getId());
    }

    public Book toValue(String clientValue) {
        Long id = Long.valueOf(clientValue);
        return bookService.findBookById(id);
    }
}
