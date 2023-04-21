package validate;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * This class is used to forgive error 
 * don't want to stop processing when we encount an error
 */
public class ForgivingErrorHandler implements ErrorHandler {

    public String errors = "";

    public void warning(SAXParseException ex) {
        errors = errors + "Wraning : ";
        for (int i = 1; i < ex.getMessage().split(":").length; i++) errors = errors + ex.getMessage().split(":")[i];
        errors = errors + "\n";
        System.err.println(ex.getMessage());
    }

    public void error(SAXParseException ex) {
        errors = errors + "* ";
        for (int i = 1; i < ex.getMessage().split(":").length; i++) errors = errors + ex.getMessage().split(":")[i];
        errors = errors + "\n";
        System.err.println(ex.getMessage());
        System.err.println(ex.getClass());
    }

    public void fatalError(SAXParseException ex) throws SAXException {
        errors = errors + "Fatal Error : ";
        for (int i = 1; i < ex.getMessage().split(":").length; i++) errors = errors + ex.getMessage().split(":")[i];
        errors = errors + "\n";
        System.err.println(ex.getMessage());
        System.err.println(ex.getClass());
    }
}
