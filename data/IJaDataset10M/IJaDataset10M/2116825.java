package net.javaseminar.shopping.domain;

import java.math.BigDecimal;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the TBL_BOOK database table.
 * 
 */
@Entity
@Table(name = "TBL_BOOK")
@DiscriminatorValue("1")
@NamedQueries({ @NamedQuery(name = Book.FIND_BY_TITLE, query = "SELECT b FROM Book b WHERE LOWER(b.title) LIKE :title"), @NamedQuery(name = Book.FIND_ALL, query = "SELECT b FROM Book b ORDER BY LOWER(b.title)") })
public class Book extends CartItem {

    private static final long serialVersionUID = 1L;

    public static final String FIND_BY_TITLE = "Book.findByTitle";

    public static final String FIND_ALL = "Book.findAll";

    private String author;

    private String title;

    public Book() {
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public BigDecimal getAgio() {
        if (this.hasHypeTitle()) return new BigDecimal(10); else return new BigDecimal(0);
    }

    private boolean hasHypeTitle() {
        return title.contains("Spring");
    }
}
