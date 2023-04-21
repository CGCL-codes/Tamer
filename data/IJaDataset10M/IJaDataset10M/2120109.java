package pedro.util;

public class Parameter {

    private String name;

    private String value;

    public Parameter(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Get the value of name.
     *
     * @return value of name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the value of value.
     *
     * @return value of value.
     */
    public String getValue() {
        return value;
    }

    public String toString() {
        return name + value;
    }

    /**
     * Set the value of name.
     *
     * @param name Value to assign to name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Set the value of value.
     *
     * @param value Value to assign to value.
     */
    public void setValue(String value) {
        this.value = value;
    }
}
