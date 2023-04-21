package model.entities;

public class Post implements java.io.Serializable {

    private Integer id;

    private java.util.Date date;

    private String title;

    private String link;

    private String content;

    private boolean status;

    private java.util.Date modified;

    private Integer parent;

    private Integer order;

    public Post() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public java.util.Date getDate() {
        return this.date;
    }

    public void setDate(java.util.Date date) {
        this.date = date;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return this.link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean getStatus() {
        return this.status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public java.util.Date getModified() {
        return this.modified;
    }

    public void setModified(java.util.Date modified) {
        this.modified = modified;
    }

    public Integer getParent() {
        return this.parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public Integer getOrder() {
        return this.order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
}
