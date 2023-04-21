package javax.faces.el;

/**
 * see Javadoc of <a href="http://java.sun.com/javaee/javaserverfaces/1.2/docs/api/index.html">JSF Specification</a>
 * 
 * @author Thomas Spiegl (latest modification by $Author: lu4242 $)
 * @version $Revision: 882395 $ $Date: 2009-11-19 22:15:53 -0500 (Thu, 19 Nov 2009) $
 * @deprecated
 */
public class PropertyNotFoundException extends EvaluationException {

    private static final long serialVersionUID = -7271529989175141594L;

    /**
     * @deprecated
     */
    public PropertyNotFoundException() {
        super();
    }

    /**
     * @deprecated
     */
    public PropertyNotFoundException(String message) {
        super(message);
    }

    /**
     * @deprecated
     */
    public PropertyNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @deprecated
     */
    public PropertyNotFoundException(Throwable cause) {
        super(cause);
    }
}
