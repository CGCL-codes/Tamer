package model;

/**
 * Nota: value siempre debe != de null porque de �l se desprende el tipo de propiedad
 * @author sgx
 *
 */
public class Property {

    public String id;

    String description = "no description";

    Object defaultValue;

    public Property(String id, String description, Object value) {
        this(id, value);
        this.description = description;
    }

    public Property(String id, Object value) {
        this.id = id;
        if (value == null) throw new RuntimeException("the value of a property can't be null. insert a sample object");
        this.defaultValue = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String toString() {
        return id + " - " + defaultValue + " - " + defaultValue.getClass();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(Object value) {
        this.defaultValue = value;
    }
}
