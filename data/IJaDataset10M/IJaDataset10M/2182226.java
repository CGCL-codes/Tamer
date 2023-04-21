package genj.gedcom;

/**
 * A property that doesn't have a value
 */
public class PropertyNoValue extends PropertySimpleValue {

    /**
   * @see genj.gedcom.Property#getValue()
   */
    public String getValue() {
        return EMPTY_STRING;
    }

    /**
   * @see genj.gedcom.Property#setValue(java.lang.String)
   */
    public void setValue(String newValue) {
    }

    /**
   * @see genj.gedcom.Property#getProxy()
   */
    public String getProxy() {
        return "Empty";
    }
}
