package nl.chess.it.util.config;

/**
 * Thrown when a property could not be parsed as the expected type.
 *
 * @author Guus Bosman (Chess iT)
 * @version $Revision: 1.1.1.1 $
 */
public class InvalidPropertyException extends ConfigurationException {

    /** 
     * Describes what type of variable we though the property should be.
     */
    protected String expectedtype;

    /** 
     * Name of the property (key in Properties).
     */
    protected String propertyname;

    /** 
     * Actual value read.
     */
    protected String value;

    /**
     * Constructs a new InvalidPropertyException where no fields have been filled in (yet).
     */
    public InvalidPropertyException() {
    }

    /**
     * Constructs a new InvalidPropertyException where some fields are not filled in (yet).
     *
     * @param propertyname Name of the property that couldn't be parsed.
     */
    public InvalidPropertyException(String propertyname) {
        this.propertyname = propertyname;
    }

    /**
     * Constructs a new InvalidPropertyException.
     *
     * @param propertyname Name of the property that couldn't be parsed.
     * @param value Value that caused the problem.
     * @param expectedType String description of the expected type.
     */
    public InvalidPropertyException(String propertyname, String value, String expectedType) {
        this.propertyname = propertyname;
        this.value = value;
        this.expectedtype = expectedType;
    }

    public String getPropertyname() {
        return propertyname;
    }

    public String getValue() {
        return value;
    }

    public String getExpectedtype() {
        return expectedtype;
    }

    public String toString() {
        return this.getClass().getName() + ": " + propertyname + ", " + value + ", " + expectedtype;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setExpectedtype(String expectedtype) {
        this.expectedtype = expectedtype;
    }

    public void setPropertyname(String propertyname) {
        this.propertyname = propertyname;
    }
}
