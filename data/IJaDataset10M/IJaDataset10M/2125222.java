package org.nomicron.suber.form;

import org.nomicron.suber.enums.PageStatus;
import org.nomicron.suber.model.bean.AlterationType;
import org.nomicron.suber.model.object.Book;
import org.nomicron.suber.model.object.Page;
import com.dreamlizard.miles.model.Selectable;

public class PageEditForm extends ElementListForm {

    private Page page;

    private Book book;

    private Page alterationPage;

    private String submitUrl;

    private PageStatus pageStatus;

    private AlterationType alterationType;

    private Boolean newVersion;

    private Boolean publicPage;

    private Selectable turnAdopted;

    private Selectable turnEffected;

    private Selectable turnEnded;

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Page getAlterationPage() {
        return alterationPage;
    }

    public void setAlterationPage(Page alterationPage) {
        this.alterationPage = alterationPage;
    }

    public String getSubmitUrl() {
        return submitUrl;
    }

    public void setSubmitUrl(String submitUrl) {
        this.submitUrl = submitUrl;
    }

    public PageStatus getPageStatus() {
        return pageStatus;
    }

    public void setPageStatus(PageStatus pageStatus) {
        this.pageStatus = pageStatus;
    }

    public AlterationType getAlterationType() {
        return alterationType;
    }

    public void setAlterationType(AlterationType alterationType) {
        this.alterationType = alterationType;
    }

    public Boolean getNewVersion() {
        return newVersion;
    }

    public void setNewVersion(Boolean newVersion) {
        this.newVersion = newVersion;
    }

    public Boolean getPublicPage() {
        return publicPage;
    }

    public void setPublicPage(Boolean publicPage) {
        this.publicPage = publicPage;
    }

    public Selectable getTurnAdopted() {
        return turnAdopted;
    }

    public void setTurnAdopted(Selectable turnAdopted) {
        this.turnAdopted = turnAdopted;
    }

    public Selectable getTurnEffected() {
        return turnEffected;
    }

    public void setTurnEffected(Selectable turnEffected) {
        this.turnEffected = turnEffected;
    }

    public Selectable getTurnEnded() {
        return turnEnded;
    }

    public void setTurnEnded(Selectable turnEnded) {
        this.turnEnded = turnEnded;
    }
}
