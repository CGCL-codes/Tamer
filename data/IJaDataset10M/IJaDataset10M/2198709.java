package org.gloidy.stenella.core.hibernate.beans;

/**
 * Created on Mar 28, 2009
 * @author javajox
 */
public class Source {

    public Source() {
    }

    private long id;

    private String pointer;

    private Book book;

    private Short type;

    public void setId(long value) {
        this.id = value;
    }

    public long getId() {
        return id;
    }

    public void setPointer(String value) {
        this.pointer = value;
    }

    public String getPointer() {
        return pointer;
    }

    public void setType(short value) {
        setType(new Short(value));
    }

    public void setType(Short value) {
        this.type = value;
    }

    public Short getType() {
        return type;
    }

    public void setBook(Book value) {
        this.book = value;
    }

    public Book getBook() {
        return book;
    }
}
