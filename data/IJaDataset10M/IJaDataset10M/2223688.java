package de.t5book.pages;

import java.util.Date;
import java.util.List;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.hibernate.HibernateGridDataSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import de.t5book.entities.Book;
import de.t5book.services.BookService;

public class GridDemo {

    @Inject
    private BookService bookService;

    @Property
    private Book currentBook;

    public List<Book> getBooks() {
        return bookService.findAllBooks();
    }
}
