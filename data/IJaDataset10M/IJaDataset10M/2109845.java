package uk.org.ogsadai.exception;

import uk.org.ogsadai.common.msgs.DAIUniqueID;

/**
 * Interface for the information provided by OGSA-DAI exceptions.
 * 
 * @author The OGSA-DAI Project Team
 */
public interface DAIExceptionInformation {

    /**
     * Gets the error ID.
     * 
     * @return the error ID.
     */
    public ErrorID getErrorID();

    /**
     * Gets the parameters associated with the error.
     * 
     * @return the parameters accociated with the error.  If there are no 
     * parameters associated with the error then the array length
     * will be 0.
     */
    public Object[] getParameters();

    /**
     * Gets the unique ID associated with this exception.
     * 
     * @return the unique ID associated with the exception.
     */
    public DAIUniqueID getExceptionID();

    /**
     * Gets whether there is a child exception.
     * 
     * @return <code>true</code> if there is a child exception, 
     * <code>false</code> otherwise.
     */
    public boolean hasChild();

    /**
     * Gets a message describing the error.
     * 
     * @return message.
     */
    public String getMessage();

    /**
     * Gets a localized message describing the error.
     * 
     * @return message.
     */
    public String getLocalizedMessage();
}
