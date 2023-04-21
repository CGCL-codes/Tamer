package org.jdbf.engine.xml;

import org.jdbf.engine.JDBFException;
import org.xml.sax.SAXParseException;

/**
 * <code>MarshallExcpetion </code> is thrown if the marshalling process fails.
 *
 *
 * @author Giovanni Martone<br> 
 * @version $Revision: 1.2 $<br>
 * last changed by $Author: gmartone $
 *
 * 
 */
public class MarshallException extends JDBFException {

    /** Exception object */
    private Exception except;

    /**
     * Creates MarshallException with a message
     *
     * @param message
     *
     */
    public MarshallException(String message) {
        super(message);
    }

    /**
     * Creates MarshallException matching a message with
     * an integer argument
     *
     * @param message
     * @param type
     *
     */
    public MarshallException(String message, int type) {
        super(message, new Integer(type));
    }

    /**
     * Creates MarshallException matching a message with
     * an object argument
     *
     * @param message
     * @param type
     *
     */
    public MarshallException(String message, Object type) {
        super(message, type);
    }

    /**
     * Creates MarshallException matching a message with
     * two string arguments
     *
     * @param message
     * @param type1
     * @param type2
     *
     */
    public MarshallException(String message, String type1, String type2) {
        super(message, new Object[] { type1, type2 });
    }

    /**
     * Creates MarshallException from a SAXParseException
     *
     * @param sx SAXParseException
     *
     */
    public MarshallException(SAXParseException sx) {
        super("mapping.SAXParseError", new Object[] { sx.getSystemId(), sx.getPublicId(), new Integer(sx.getLineNumber()), new Integer(sx.getColumnNumber()), sx.getMessage() });
        this.except = sx;
    }

    /**
     * Creates MarshallException from a Exception
     *
     * @param except Exception
     *
     */
    public MarshallException(Exception except) {
        this("mapping.nested", except.toString());
        this.except = except;
    }

    /**
     * Return exception
     *
     * @return Exception
     *
     */
    public Exception getException() {
        return except;
    }
}
