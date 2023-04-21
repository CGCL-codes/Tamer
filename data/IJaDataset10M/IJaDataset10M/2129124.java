package boogle.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author martin
 */
@Entity
@Table(name = "rating", catalog = "boogle", schema = "", uniqueConstraints = { @UniqueConstraint(columnNames = { "book", "user" }) })
@NamedQueries({ @NamedQuery(name = "Rating.findAll", query = "SELECT r FROM Rating r"), @NamedQuery(name = "Rating.findById", query = "SELECT r FROM Rating r WHERE r.id = :id"), @NamedQuery(name = "Rating.findByRating", query = "SELECT r FROM Rating r WHERE r.rating = :rating"), @NamedQuery(name = "Rating.findByUser", query = "SELECT r FROM Rating r WHERE r.user = :user"), @NamedQuery(name = "Rating.findByBook", query = "SELECT r FROM Rating r WHERE r.book = :book"), @NamedQuery(name = "Rating.findByTime", query = "SELECT r FROM Rating r WHERE r.time = :time") })
public class Rating implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Basic(optional = false)
    @Column(name = "rating", nullable = false)
    private int rating;

    @Basic(optional = false)
    @Column(name = "time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;

    @JoinColumn(name = "user", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private User user;

    @JoinColumn(name = "book", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Book book;

    public Rating() {
    }

    public Rating(Integer id) {
        this.id = id;
    }

    public Rating(Integer id, int rating, Date time) {
        this.id = id;
        this.rating = rating;
        this.time = time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Rating)) {
            return false;
        }
        Rating other = (Rating) object;
        if (this.book.equals(other.getBook()) && this.user.equals(other.getUser())) {
            return true;
        }
        return true;
    }

    @Override
    public String toString() {
        return "boogle.entities.Rating[id=" + id + "]";
    }
}
