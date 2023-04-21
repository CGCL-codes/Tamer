package com.myapp.jsf.bibliothek;

import javax.faces.component.UIParameter;
import javax.faces.event.ActionEvent;

public class Book {

    private long id;

    private String title;

    private String author;

    private boolean available;

    public Book() {
    }

    public Book(long id, String title, String author, boolean available) {
        super();
        this.author = author;
        this.available = available;
        this.id = id;
        this.title = title;
    }

    @SuppressWarnings("unused")
    public void initBook(ActionEvent e) {
        setBook(new Book());
    }

    public void selectBook(ActionEvent e) {
        UIParameter component = (UIParameter) e.getComponent().findComponent("editId");
        long idl = Long.parseLong(component.getValue().toString());
        setBook(DB.getInstance().getBookById(idl));
    }

    @SuppressWarnings("unused")
    public void saveBook(ActionEvent e) {
        DB.getInstance().saveBook(this);
    }

    public void deleteBook(ActionEvent e) {
        UIParameter component = (UIParameter) e.getComponent().findComponent("deleteId");
        long idl = Long.parseLong(component.getValue().toString());
        DB.getInstance().deleteBookById(idl);
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void setBook(Book book) {
        this.setId(book.getId());
        this.setTitle(book.getTitle());
        this.setAuthor(book.getAuthor());
        this.setAvailable(book.isAvailable());
    }
}
