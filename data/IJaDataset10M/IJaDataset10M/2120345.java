package cn.jsprun.domain;

public class Statvars implements java.io.Serializable {

    private static final long serialVersionUID = -4992798696869994093L;

    private StatvarsId id;

    private String value;

    public Statvars() {
    }

    public Statvars(StatvarsId id, String value) {
        this.id = id;
        this.value = value;
    }

    public StatvarsId getId() {
        return this.id;
    }

    public void setId(StatvarsId id) {
        this.id = id;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
