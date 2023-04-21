package au.edu.diasb.annotation.danno;

import javax.servlet.http.HttpServletRequest;
import au.edu.diasb.annotation.danno.model.AnnoteaObject;
import au.edu.diasb.annotation.danno.model.RDFContainer;
import au.edu.diasb.annotation.danno.model.RDFObject;
import au.edu.diasb.chico.mvc.RequestFailureException;

/**
 * A DannoAccessPolicy object implements fine-grained access control
 * rules on Annotea (and related) requests.  A typical implementation
 * will decide on the basis of the users' identity, authorities and
 * other attributes, together with details of the target object(s).
 * 
 * @author scrawley
 */
public interface DannoAccessPolicy {

    /**
     * Called for GET requests after fetching the object(s) from the triple 
     * store and preparing the result container.  An implementation may
     * throw an unchecked exception or modify the contents of the
     * container.
     * 
     * @param res the result container.
     */
    void checkRead(HttpServletRequest request, RDFContainer res) throws RequestFailureException;

    /**
     * Called for PUT and POST requests after preparing the result container 
     * and prior to committing.  An implementation may throw an unchecked exception.  
     * (Modifying the result container is neither necessary or advisable.  It 
     * will only change the result, not the triples committed to the triple store.)
     * 
     * @param res the result container containing the triples to be 
     *     returned in the response.
     * @throws RequestFailureException 
     */
    void checkCreate(HttpServletRequest request, RDFObject res) throws RequestFailureException;

    /**
     * Called for PUT requests to check that the requestor may update an object. 
     * An implementation may throw an unchecked exception.
     * 
     * @param obj the original version of the object as fetched from 
     *     the triple store.
     * @throws RequestFailureException 
     */
    void checkUpdate(HttpServletRequest request, AnnoteaObject obj) throws RequestFailureException;

    /**
     * Called for DELETE requests prior to committing.
     * An implementation may throw an unchecked exception.  
     * 
     * @param obj the original version of the object as fetched from 
     *     the triple store.
     * @throws RequestFailureException 
     */
    void checkDelete(HttpServletRequest request, AnnoteaObject obj) throws RequestFailureException;

    /**
     * Called for administrative requests to check that the request has
     * the rights required for a given admin request.
     * 
     * @param request the specific request.
     * @throws RequestFailureException 
     */
    void checkWebAdmin(HttpServletRequest request) throws RequestFailureException;

    /**
     * Check that the user is allowed to override the user identification(s) in an 
     * annotation or reply.
     * 
     * @param request the current request
     */
    boolean canChangeNames(HttpServletRequest request);
}
