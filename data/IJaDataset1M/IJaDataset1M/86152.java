package org.fedoracommons.unapi;

/**
 * 
 *
 * @author Edwin Shin
 * @since 0.1
 * @version $Id: ObjectResolver.java 2 2008-10-10 05:49:28Z pangloss $
 */
public interface ObjectResolver {

    /**
     * Provide the object formats which should be supported for all objects 
     * available through the unAPI service.
     * 
     * @return UnapiFormats representing the object formats supported for all
     *         objects.
     * @throws UnapiException
     */
    public UnapiFormats getFormats() throws UnapiException;

    /**
     * Provide a list of object formats available from the unAPI service for the 
     * object identified by <code>id</code>. 
     * It is similar to the {@link #getFormats() getFormats} response, but the
     * returned UnapiFormats object must have the requested <code>id</code> set.
     * 
     * @param id
     * @return UnapiFormats representing the object formats supported for the
     *         requested object.
     * @throws UnapiException
     */
    public UnapiFormats getFormats(String id) throws UnapiException;

    /**
     * <p>Return an {@link UnapiObject UnapiObject} representing the object 
     * specified by <code>id</code> in the format specified by 
     * <code>format</code>.</p>
     * 
     * <p>Implementations should throw an {@link IdentifierException 
     * IdentifierException} for requests for an identifier that is not available 
     * on the server.
     * Implementations should throw a {@link FormatException FormatException} 
     * for requests for an identifier that is available on the server in a 
     * format that is not available for that identifier.</p>
     * 
     * @param id
     * @param format
     * @return UnapiObject
     * @throws UnapiException
     */
    public UnapiObject getObject(String id, String format) throws UnapiException;
}
