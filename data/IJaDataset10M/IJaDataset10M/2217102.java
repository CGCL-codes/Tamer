package bookstore.domain;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the BOOK_IMAGE database table.
 * 
 */
@Entity
@Table(name = "BOOK_IMAGE")
public class BookImage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "BOOK_IMAGE_ID", unique = true, nullable = false, precision = 22)
    private long bookImageId;

    @Column(nullable = false, length = 255)
    private String filename;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOOK_ID", nullable = false)
    private Book book;

    public BookImage() {
    }

    public long getBookImageId() {
        return this.bookImageId;
    }

    public void setBookImageId(long bookImageId) {
        this.bookImageId = bookImageId;
    }

    public String getFilename() {
        return this.filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Book getBook() {
        return this.book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
