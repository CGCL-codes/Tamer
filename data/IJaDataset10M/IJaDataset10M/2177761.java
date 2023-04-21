package net.sf.brightside.xlibrary.service.addBookToLibrary;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import net.sf.brightside.xlibrary.metamodel.Genre;
import net.sf.brightside.xlibrary.metamodel.Publisher;
import net.sf.brightside.xlibrary.metamodel.XBook;

public class AddBookCommandImpl extends HibernateDaoSupport implements AddBookCommand<XBook> {

    private AddBookToLibrary addBook;

    private XBook book;

    private Genre gender;

    private Publisher publisher;

    public Genre getGender() {
        return gender;
    }

    public void setGender(Genre gender) {
        this.gender = gender;
    }

    public AddBookToLibrary getAddBook() {
        return addBook;
    }

    public void setAddBook(AddBookToLibrary addBook) {
        this.addBook = addBook;
    }

    @Override
    public XBook execute() {
        return addBook.saveBookToLibrary(book, gender, publisher);
    }

    public void setBook(XBook book) {
        this.book = book;
    }

    public XBook getBook() {
        return book;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }
}
