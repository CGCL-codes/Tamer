package siouxsie.mvc.xwork;

import com.opensymphony.xwork2.Action;

public class ViewBookAction implements Action {

    Book book;

    String id;

    private String test;

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public String execute() throws Exception {
        book = new Book();
        if (book != null) return "success";
        return "notFound";
    }

    public Book getBook() {
        return this.book;
    }

    public void setId(String id) {
        this.id = id;
    }
}
