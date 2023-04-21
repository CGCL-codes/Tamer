package net.sourceforge.ords.core.util;

import java.util.Date;

public class BookDto {

    private String name;

    private Double price;

    private String author;

    private String press;

    private Date pressDate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPress() {
        return press;
    }

    public void setPress(String press) {
        this.press = press;
    }

    public Date getPressDate() {
        return pressDate;
    }

    public void setPressDate(Date pressDate) {
        this.pressDate = pressDate;
    }
}
