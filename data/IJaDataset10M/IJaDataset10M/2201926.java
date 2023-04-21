package sssvmtoolbox.dataprovider;

public class Param {

    private String key;

    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(final String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public Param(final String key, final String value) {
        super();
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return "Key: " + key + ", Value: " + value;
    }
}
