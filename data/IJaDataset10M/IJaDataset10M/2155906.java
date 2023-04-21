package ar.edu.fesf.builders;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import ar.edu.fesf.model.Author;
import ar.edu.fesf.model.Book;
import ar.edu.fesf.model.Category;
import ar.edu.fesf.model.ISBN;
import ar.edu.fesf.model.Publisher;

public class BookBuilder {

    private String title;

    private ISBN isbn;

    private Publisher publisher;

    private String imagepath = "http://t3.gstatic.com/images?q=tbn:ANd9GcRnOSqXTlETDlOV7TRVsDz9YYV2hmIJRMedBLFkfSH7aGFqlSLpDQ";

    private String description = "";

    private List<Author> authors = new ArrayList<Author>();

    private Set<Category> categories = new HashSet<Category>();

    private int countOfCopies;

    private Boolean available;

    public Book build() {
        if (this.available == null) {
            this.available = true;
        }
        return new Book(this.title, this.isbn, this.publisher, this.imagepath, this.description, this.authors, this.categories, this.countOfCopies, this.available);
    }

    public BookBuilder withCountOfCopies(final int count) {
        this.setCountOfCopies(count);
        return this;
    }

    public BookBuilder withTitle(final String aTitle) {
        this.title = aTitle;
        return this;
    }

    public BookBuilder withIsbn(final ISBN aIsbn) {
        this.isbn = aIsbn;
        return this;
    }

    public BookBuilder withPublisher(final Publisher apublisher) {
        this.publisher = apublisher;
        return this;
    }

    public BookBuilder withImagepath(final String aimagepath) {
        this.imagepath = aimagepath;
        return this;
    }

    public BookBuilder withDescription(final String adescription) {
        this.description = adescription;
        return this;
    }

    public BookBuilder withAuthor(final Author author) {
        this.authors.add(author);
        return this;
    }

    public BookBuilder withAuthors(final List<Author> aauthors) {
        this.authors = aauthors;
        return this;
    }

    public BookBuilder withCategories(final Set<Category> acategories) {
        this.categories = acategories;
        return this;
    }

    public BookBuilder withCategory(final Category category) {
        this.categories.add(category);
        return this;
    }

    public BookBuilder withAvailable(final boolean aBoolean) {
        this.available = aBoolean;
        return this;
    }

    /*** ACCESSORS ***/
    public void setTitle(final String title) {
        this.title = title;
    }

    public void setIsbn(final ISBN isbn) {
        this.isbn = isbn;
    }

    public void setPublisher(final Publisher publisher) {
        this.publisher = publisher;
    }

    public void setImagepath(final String imagepath) {
        this.imagepath = imagepath;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setAuthors(final List<Author> authors) {
        this.authors = authors;
    }

    public void setCategories(final Set<Category> categories) {
        this.categories = categories;
    }

    public String getTitle() {
        return this.title;
    }

    public ISBN getIsbn() {
        return this.isbn;
    }

    public Publisher getPublisher() {
        return this.publisher;
    }

    public String getImagepath() {
        return this.imagepath;
    }

    public String getDescription() {
        return this.description;
    }

    public List<Author> getAuthors() {
        return this.authors;
    }

    public Set<Category> getCategories() {
        return this.categories;
    }

    public void setCountOfCopies(final int countOfCopies) {
        this.countOfCopies = countOfCopies;
    }

    public int getCountOfCopies() {
        return this.countOfCopies;
    }

    public boolean isAvailable() {
        return this.available;
    }

    public void setAvailable(final boolean available) {
        this.available = available;
    }
}
