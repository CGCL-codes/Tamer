package org.formproc;

/**
 * A name/value pair for a single form field.
 *
 * @author Anthony Eden
 */
public class FormData {

    private String name;

    private Object value;

    /**
     * Construct a new FormData object for the given field name with the given value.  The value may be null if the
     * field data was not submitted.
     *
     * @param name The field name
     * @param value The value
     */
    public FormData(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Get the field name.
     *
     * @return The field name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the value.
     *
     * @return The value
     */
    public Object getValue() {
        return value;
    }
}
