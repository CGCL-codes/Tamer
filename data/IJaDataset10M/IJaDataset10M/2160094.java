package org.iqual.infodrome;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: zslajchrt
 * Date: May 11, 2010
 * Time: 9:22:15 AM
 * To change this template use File | Settings | File Templates.
 */
public class Book extends Product {

    String author = null;

    String isbn = null;

    String publisher = null;

    Book brochure;

    public Book() {
    }

    public Book(String name, String desc, double price, String author, String isbn, String publisher, Product byProduct, Book brochure) {
        super(name, desc, price, byProduct);
        this.author = author;
        this.isbn = isbn;
        this.publisher = publisher;
        this.brochure = brochure;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Book getBrochure() {
        return brochure;
    }

    public void setBrochure(Book brochure) {
        this.brochure = brochure;
    }

    public int doSomeThing() {
        return 0;
    }

    @Override
    public Collection<Class> getTargetTypes() {
        return null;
    }
}
