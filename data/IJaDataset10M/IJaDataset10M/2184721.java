package uk.ac.ebi.mydas.model.writeback;

public class DasCommonAttributes {

    private String base;

    private String lang;

    private String id;

    private String space;

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSpace() {
        return space;
    }

    public void setSpace(String space) {
        this.space = space;
    }

    public String toString() {
        return "Base: " + base + ", Id : " + id + ", Lang : " + lang + ", Space : " + space;
    }
}
