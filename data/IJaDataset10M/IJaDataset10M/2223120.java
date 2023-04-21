package cn.jsprun.domain;

public class Projects implements java.io.Serializable {

    private static final long serialVersionUID = 27919924742083789L;

    private Short id;

    private String name;

    private String type;

    private String description;

    private String value;

    public Projects() {
    }

    public Projects(String name, String type, String description, String value) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.value = value;
    }

    public Short getId() {
        return this.id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
