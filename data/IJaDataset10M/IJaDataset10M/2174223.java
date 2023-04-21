package ar.edu.fesf.dtos;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.xml.bind.annotation.XmlRootElement;
import ar.edu.fesf.model.Author;
import ar.edu.fesf.model.Book;
import ar.edu.fesf.model.Category;

@XmlRootElement(name = "editBookDTO")
public class EditBookDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;

    protected String title;

    protected String isbn;

    protected String publisher;

    protected String description;

    protected Set<String> categories = new HashSet<String>();

    protected Set<String> authors = new HashSet<String>();

    protected boolean available;

    protected String imageLink;

    public EditBookDTO() {
        super();
    }

    public EditBookDTO(final Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.isbn = book.getIsbn().getValue();
        this.publisher = book.getPublisher().getName();
        this.description = book.getDescription();
        this.available = book.getAvailable();
        for (Author author : book.getAuthors()) {
            this.authors.add(author.getName());
        }
        for (Category category : book.getCategories()) {
            this.categories.add(category.getName());
        }
    }

    public void addCategory(final String category) {
        if (this.getCategories() == null) {
            this.categories = new HashSet<String>();
        }
        if (category != null && !category.isEmpty()) {
            this.getCategories().add(category);
        }
    }

    public void addAuthor(final String author) {
        if (this.getAuthors() == null) {
            this.authors = new HashSet<String>();
        }
        if (author != null && !author.isEmpty()) {
            this.getAuthors().add(author);
        }
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getIsbn() {
        return this.isbn;
    }

    public void setIsbn(final String isbn) {
        this.isbn = isbn;
    }

    public String getPublisher() {
        return this.publisher;
    }

    public void setPublisher(final String publisher) {
        this.publisher = publisher;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public boolean getAvailable() {
        return this.available;
    }

    public void setAvailable(final boolean available) {
        this.available = available;
    }

    public Set<String> getCategories() {
        return this.categories;
    }

    public void setCategories(final Set<String> categories) {
        this.categories = categories;
    }

    public Set<String> getAuthors() {
        return this.authors;
    }

    public void setAuthors(final Set<String> authors) {
        this.authors = authors;
    }

    public String getImageLink() {
        return this.imageLink != null & this.imageLink != "" ? this.imageLink : "http://t3.gstatic.com/images?q=tbn:ANd9GcRnOSqXTlETDlOV7TRVsDz9YYV2hmIJRMedBLFkfSH7aGFqlSLpDQ";
    }

    public void setImageLink(final String imageLink) {
        this.imageLink = imageLink;
    }
}
