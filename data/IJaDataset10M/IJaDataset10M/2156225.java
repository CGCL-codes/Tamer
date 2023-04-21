package org.jrest.dao.test.entities;

public class BookModel {

    private String id;

    private String title;

    private float price;

    public BookModel(Book book) {
        id = book.getId();
        title = book.getTitle();
        price = book.getPrice();
    }

    public BookModel(String id, String title, Float price) {
        this.id = id;
        this.title = title;
        this.price = price != null ? price.floatValue() : 0f;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
