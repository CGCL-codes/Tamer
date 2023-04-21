package pl.model.domain;

import java.util.Collection;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

@Entity
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "name", "surname" }), @UniqueConstraint(columnNames = { "birthday", "surname" }) })
@Indexed
public class Author extends AbstractDBObject {

    @Field(index = Index.UN_TOKENIZED, store = Store.YES)
    protected String name;

    @Field(index = Index.UN_TOKENIZED, store = Store.NO)
    protected String surname;

    protected Date birthday;

    @OneToMany(mappedBy = "author")
    @OrderBy("title")
    protected Collection<Book> books;

    @ManyToMany
    protected Collection<Book> cobooks;

    public Author(String name, String surname) {
        super();
        this.name = name;
        this.surname = surname;
    }

    @Override
    public String toString() {
        return super.toString() + " " + name + " " + surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Collection<Book> getBooks() {
        return books;
    }

    public void setBooks(Collection<Book> books) {
        this.books = books;
    }

    public Collection<Book> getCobooks() {
        return cobooks;
    }

    public void setCobooks(Collection<Book> cobooks) {
        this.cobooks = cobooks;
    }

    public Author(String name, String surname, Date birthday, Collection<Book> books, Collection<Book> cobooks) {
        super();
        this.name = name;
        this.surname = surname;
        this.birthday = birthday;
        this.books = books;
        this.cobooks = cobooks;
    }

    public Author() {
        super();
    }
}
