package simple.xml.load;

/**
 * The <code>TextException</code> is used to represent conditions 
 * when an XML element text value is in an invalid state. Typically 
 * this is used when text cannot be serialized or deserialized. Also
 * this may be thrown if the <code>Text</code> annotation is used 
 * to label a field that is not a primitive type.
 * 
 * @author Niall Gallagher
 */
public class TextException extends PersistenceException {

    /**
    * Constructor for the <code>TextException</code> object. This
    * constructor takes a format string an a variable number of object
    * arguments, which can be inserted into the format string. 
    * 
    * @param text a format string used to present the error message
    * @param list a list of arguments to insert into the string
    */
    public TextException(String text, Object... list) {
        super(text, list);
    }

    /**
    * Constructor for the <code>TextException</code> object. This
    * constructor takes a format string an a variable number of object
    * arguments, which can be inserted into the format string. 
    * 
    * @param cause the source exception this is used to represent
    * @param text a format string used to present the error message
    * @param list a list of arguments to insert into the string 
    */
    public TextException(Throwable cause, String text, Object... list) {
        super(cause, text, list);
    }
}
