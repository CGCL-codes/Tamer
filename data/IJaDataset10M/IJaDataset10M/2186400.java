package org.aigebi.rbac;

/**
 * @author Ligong Xu
 * @version $Id: AuthorContext.java 1 2007-09-22 18:10:03Z ligongx $
 */
public interface AuthorContext extends SecurityContext {

    public AuthorRequest getAuthorRequest();

    public void setAuthorRequest(AuthorRequest pAuthorRequest);

    public AuthorResponse getAuthorResponse();

    public void setAuthorResponse(AuthorResponse pAuthorResponse);

    /** return operationName in authorization request */
    public String getRequestOpName();

    /** return username in authorization request */
    public String getRequestUsername();

    /** return role name in authorization request */
    public String getRequestRoleName();
}
