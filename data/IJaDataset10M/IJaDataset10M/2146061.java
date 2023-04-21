package net.sf.serialex.beans;

/**
 * Serialex cannot unmarshal fields that do not have setters.
 */
public class BeanWithNoSetter {

    private String s;

    public String getS() {
        return s;
    }
}
