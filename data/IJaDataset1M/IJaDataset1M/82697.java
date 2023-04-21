package bookstore.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;

/**
 * The persistent class for the INVENTORY database table.
 * 
 */
@Entity
@Table(name = "INVENTORY")
public class Inventory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "BOOK_ID", unique = true, nullable = false, precision = 22)
    private long bookId;

    @Column(name = "BOOK_QUANTITY", nullable = false, precision = 5)
    private int bookQuantity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOOK_ID", nullable = false, insertable = false, updatable = false)
    private Book book;

    public Inventory() {
    }

    public long getBookId() {
        return this.bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public int getBookQuantity() {
        return this.bookQuantity;
    }

    public void setBookQuantity(int bookQuantity) {
        this.bookQuantity = bookQuantity;
    }

    public Book getBook() {
        return this.book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
