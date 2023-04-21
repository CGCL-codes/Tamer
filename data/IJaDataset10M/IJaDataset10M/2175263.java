package ontool.ext;

public class StringHolder {

    private String v;

    public StringHolder(String v) {
        this.v = v;
    }

    public StringHolder(StringHolder v) {
        this.v = v.v;
    }

    public String getValue() {
        return v;
    }

    public void setValue(String v) {
        this.v = v;
    }

    public String toString() {
        return v;
    }
}
