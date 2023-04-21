package au.edu.diasb.annotation.dannotate;

import au.edu.diasb.chico.mvc.RequestFailureException;

/**
 * This is the interface for Dannotate access control decision makers.
 * <p>
 * Note that access control decisions are also made independently at the 
 * Danno / Annotea level based on the user credentials that are seen by that
 * stack.
 * 
 * @author scrawley
 */
public interface DannoateAccessPolicy {

    /**
     * Check that the user can create an annotation
     */
    void checkCreateAnnotation() throws RequestFailureException;

    /**
     * Check that the user can create a reply
     */
    void checkCreateReply() throws RequestFailureException;

    /**
     * Check that the user can edit an annotation
     */
    void checkEditAnnotation() throws RequestFailureException;

    /**
     * Check that the user can edit a reply
     */
    void checkEditReply() throws RequestFailureException;

    /**
     * Check that the user can delete an annotation or reply
     */
    void checkDelete() throws RequestFailureException;

    /**
     * Check that the user is allowed to override the user identification(s) in an 
     * annotation or reply.
     */
    boolean canChangeNames() throws RequestFailureException;
}
