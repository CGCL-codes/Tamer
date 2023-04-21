package file2xliff4j;

/**
 * Exception thrown when unable to decode encrypted PDF file because of
 * missing/incorrect password.
 * @author Weldon Whipple &lt;weldon@lingotek.com&gt;
 */
public class PdfPasswordException extends ConversionException {

    /**
     * No-argument version of PdfPasswordException constructor
     */
    public PdfPasswordException() {
    }

    /**
     * PdfPasswordException constructor that takes a message
     * string as an argument
     * 
     * @param msg Informational message about the exception.
     */
    public PdfPasswordException(String msg) {
        super(msg);
    }
}
