package net.sf.brightside.moljac.pages;

import net.sf.brightside.moljac.core.spring.ApplicationContextProviderSinglton;
import net.sf.brightside.moljac.core.tapestry.SpringBean;
import net.sf.brightside.moljac.metamodel.Author;
import net.sf.brightside.moljac.service.BookRegistering;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.springframework.context.ApplicationContext;

public class AddAuthors {

    private Author author;

    @Inject
    @SpringBean("BookRegistering")
    private BookRegistering registerBook;

    @InjectPage
    private AddAuthors addAnotherAuthor;

    @InjectPage
    private Status status;

    private ApplicationContext applicationContext() {
        return new ApplicationContextProviderSinglton().getContext();
    }

    private Author createAuthor() {
        return (Author) applicationContext().getBean(Author.class.getName());
    }

    @OnEvent(value = "submit", component = "addingAuthors")
    public Object onFormSubmit() {
        registerBook.getBookForRegistering().getAuthors().add(author);
        return addAnotherAuthor;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Author getAuthor() {
        if (author == null) {
            author = createAuthor();
        }
        return author;
    }

    @OnEvent(value = "submit", component = "finalBookRegistering")
    Object onSubmitButton() {
        registerBook.execute();
        status.setStatusMessage("Your book is registered");
        return status;
    }
}
