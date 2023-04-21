package ec.display;

/**
 * @author spaus
 */
public class ParameterValue {

    final String value;

    public ParameterValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String toString() {
        return "*" + value + "*";
    }
}
