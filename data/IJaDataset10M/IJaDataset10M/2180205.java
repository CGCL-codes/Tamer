package org.iseriestoolkit.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/** <p>An unchecked exception class for use when an
 * unexpected exception is thrown.</p>
 *
 * <b>Some examples of the use of this class:</b>
 *
 * <p>Re-throwing an exception the easy way:<pre>
 *  try{
 *    Connection conn = getConn();
 *  }catch(SQLException se){
 *    throw new ApplicationException(se);
 *  }
 * </pre></p>
 *
 * <p>Re-throwing an exception with some added context:
 * <pre>
 *  try{
 *    Connection conn = getConn();
 *  }catch(SQLException se){
 *    throw new ApplicationException(se, "Some description of the problem here.");
 *  }
 * </pre></p>
 *
 * <p>Re-throwing an exception with even more context:
 * <pre>
 *  try{
 *    Connection conn = getConn();
 *  }catch(SQLException se){
 *    throw new ApplicationException(se, "Some description of the problem here.", this);
 *  }
 * </pre></p>
 *
 * <p>You can also create an instance without a source
 * exception by supplying a message:
 * <pre>
 *  throw new ApplicationException("We have a VERY SERIOUS problem.");
 * </pre></p>
 *
 * <p>You cannot create an instance without a source
 * exception or a message. This will not compile:
 * <pre>
 *  throw new ApplicationException();
 * </pre></p>
 */
public class ApplicationException extends java.lang.RuntimeException {

    /** <p>Original exception if this is being thrown as the result of another
     * exception.</p>
     */
    private Throwable originalException;

    /** <p>Additional information related to the exception.</p>
     * <p>The <code>toString()</code> method of this object will be logged.</p>
     */
    private Object relatedObject;

    /** <p>Constructor that creates an exception using the
     * exception, message and object passed.</p>
     * <p>In addition to logging the exception and the message,
     * the <code>toString()</code> method of this object will
     * also be logged.</p>
     * @param theException The exception to use
     * @param message The message to use for the exception
     * @param relatedObject The object to use for additional information
     */
    public ApplicationException(Throwable originalException, String message, Object relatedObject) {
        super(message);
        this.originalException = originalException;
        this.relatedObject = relatedObject;
        if (null != originalException) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            originalException.printStackTrace(pw);
        }
    }

    /** <p>Constructor that creates an exception using the
     * exception and message passed.</p>
     * @param theException The exception to use
     * @param message The message to use for the exception
     */
    public ApplicationException(Throwable theException, String message) {
        this(theException, message, null);
    }

    /** <p>Simple constructor that creates an exception using the
     * exception passed.</p>
     * @param theException The exception to use
     */
    public ApplicationException(Throwable theException) {
        this(theException, theException.getMessage(), null);
    }

    /** <p>Simple constructor that creates an exception using the
     * message passed.</p>
     * @param message The message to use for the exception
     */
    public ApplicationException(String message) {
        this(new Exception(message), message, null);
    }

    /** <p>Private default constructor - must be created with at least a
     * message or an exception.</p>
     */
    private ApplicationException() {
        super();
    }

    /** <p>Getter for the exception that was used to construct
     * this object (if any).</p>
     * @return The original exception
     */
    public Throwable getOriginalException() {
        return this.originalException;
    }

    /** <p>Getter for the object to use for additional information
     * to log for this object (if any).</p>
     * @return The object
     */
    public Object getRelatedObject() {
        return this.relatedObject;
    }
}
