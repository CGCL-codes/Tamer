package net.sf.brightside.moljac.metamodel.beans;

import java.util.ArrayList;
import net.sf.brightside.moljac.metamodel.Book;
import net.sf.brightside.moljac.metamodel.BookSeller;

public class BookSellerBean implements BookSeller {

    private ArrayList<Book> soldBooks;

    private String name;

    private double id;

    public ArrayList<Book> getBooks() {
        return this.soldBooks;
    }

    public void sell(Book book) {
        book.setSold(true);
        soldBooks.add(book);
    }

    public String getName() {
        return this.name;
    }

    public double getId() {
        return this.id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBooks(ArrayList<Book> books) {
        this.soldBooks = books;
    }
}
