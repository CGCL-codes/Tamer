package bean;

public class StringBean extends SimpleBean {

    public static final String VALUE = "Value";

    private String value;

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
