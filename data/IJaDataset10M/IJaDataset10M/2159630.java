package com.deitel.advjhtp1.store;

import java.io.*;

public class CartItemBean implements Serializable {

    private BookBean book;

    private int quantity;

    public CartItemBean(BookBean bookToAdd, int number) {
        book = bookToAdd;
        quantity = number;
    }

    public BookBean getBook() {
        return book;
    }

    public void setQuantity(int number) {
        quantity = number;
    }

    public int getQuantity() {
        return quantity;
    }
}
