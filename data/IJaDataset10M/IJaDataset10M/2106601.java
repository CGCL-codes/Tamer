package edu.ba.library.management.lending;

import java.util.Date;
import edu.ba.library.management.article.Article;
import edu.ba.library.management.article.Copy;
import edu.ba.library.management.user.Customer;

public class Process {

    protected int id;

    protected Date processTimestamp;

    protected Customer customer;

    protected Article article;

    protected Copy copy;

    protected ProcessStatus status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Date getProcessTimestamp() {
        return processTimestamp;
    }

    public void setProcessTimestamp(Date processTimestamp) {
        this.processTimestamp = processTimestamp;
    }

    public Copy getCopy() {
        return copy;
    }

    public void setCopy(Copy copy) {
        this.copy = copy;
    }

    public ProcessStatus getStatus() {
        return status;
    }

    public void setStatus(ProcessStatus status) {
        this.status = status;
    }
}
