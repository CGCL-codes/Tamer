package cn.jsprun.domain;

public class Forumlinks implements java.io.Serializable {

    private static final long serialVersionUID = -4178195197402292593L;

    private Short id;

    private Short displayorder;

    private String name;

    private String url;

    private String description;

    private String logo;

    public Forumlinks() {
    }

    public Forumlinks(Short displayorder, String name, String url, String description, String logo) {
        this.displayorder = displayorder;
        this.name = name;
        this.url = url;
        this.description = description;
        this.logo = logo;
    }

    public Short getId() {
        return this.id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public Short getDisplayorder() {
        return this.displayorder;
    }

    public void setDisplayorder(Short displayorder) {
        this.displayorder = displayorder;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogo() {
        return this.logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
