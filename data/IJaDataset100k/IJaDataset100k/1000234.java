package ispyb.client.util;

public class OptionValue {

    public OptionValue() {
    }

    private String value;

    private String label;

    public OptionValue(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
