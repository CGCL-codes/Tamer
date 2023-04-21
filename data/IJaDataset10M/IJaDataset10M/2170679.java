package cn.jsprun.domain;

public class Typeoptions implements java.io.Serializable {

    private static final long serialVersionUID = 2707874118083150326L;

    private Short optionid;

    private Short classid;

    private Short displayorder;

    private String title;

    private String description;

    private String identifier;

    private String type;

    private String rules;

    public Typeoptions() {
    }

    public Typeoptions(Short classid, Short displayorder, String title, String description, String identifier, String type, String rules) {
        this.classid = classid;
        this.displayorder = displayorder;
        this.title = title;
        this.description = description;
        this.identifier = identifier;
        this.type = type;
        this.rules = rules;
    }

    public Short getOptionid() {
        return this.optionid;
    }

    public void setOptionid(Short optionid) {
        this.optionid = optionid;
    }

    public Short getClassid() {
        return this.classid;
    }

    public void setClassid(Short classid) {
        this.classid = classid;
    }

    public Short getDisplayorder() {
        return this.displayorder;
    }

    public void setDisplayorder(Short displayorder) {
        this.displayorder = displayorder;
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

    public String getIdentifier() {
        return this.identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRules() {
        return this.rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }
}
