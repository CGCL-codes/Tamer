package org.tagewerk.bookinfo.action;

import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.tagewerk.bookinfo.model.Book;
import org.tagewerk.bookinfo.service.BookManager;
import org.tagewerk.bookinfo.util.GoogleBookSearch;
import com.google.gdata.util.parser.Action;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Validateable;
import com.opensymphony.xwork2.ValidationAwareSupport;

public class BookAction extends ValidationAwareSupport implements ModelDriven<Object>, Validateable {

    @Autowired
    private BookManager manager;

    protected Book model = new Book();

    protected Long id;

    protected String query;

    protected Collection<Book> list;

    public String execute() {
        return "success";
    }

    public String search() {
        this.list = GoogleBookSearch.findByQuery(query);
        return "success";
    }

    public void validate() {
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        if (id != null) {
            this.model = manager.get(id);
        }
        this.id = id;
    }

    public Object getModel() {
        return (list != null ? list : model);
    }

    public Collection<Book> getList() {
        return list;
    }

    public void setList(Collection<Book> list) {
        this.list = list;
    }
}
