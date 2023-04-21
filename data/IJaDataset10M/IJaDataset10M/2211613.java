package cn.jsprun.domain;

public class Pluginhooks implements java.io.Serializable {

    private static final long serialVersionUID = 1064523646545110698L;

    private Integer pluginhookid;

    private Short pluginid;

    private Byte available;

    private String title;

    private String description;

    private String code;

    public Pluginhooks() {
    }

    public Pluginhooks(Short pluginid, Byte available, String title, String description, String code) {
        this.pluginid = pluginid;
        this.available = available;
        this.title = title;
        this.description = description;
        this.code = code;
    }

    public Integer getPluginhookid() {
        return this.pluginhookid;
    }

    public void setPluginhookid(Integer pluginhookid) {
        this.pluginhookid = pluginhookid;
    }

    public Short getPluginid() {
        return this.pluginid;
    }

    public void setPluginid(Short pluginid) {
        this.pluginid = pluginid;
    }

    public Byte getAvailable() {
        return this.available;
    }

    public void setAvailable(Byte available) {
        this.available = available;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
