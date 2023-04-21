package lv.ante.springapp.domain;

import java.util.List;

/**
 * Module is a collection of test items about a single topic or skill. Images
 * and other attachments are still stored in the CMS.
 * 
 * @author kap
 * 
 */
public class Module {

    private long id;

    protected User user;

    protected List<Item> items;

    protected String name;

    protected String title;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
