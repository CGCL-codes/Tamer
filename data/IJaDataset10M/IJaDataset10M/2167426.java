package org.aigebi.rbac;

/**
 * Authorization Reqest
 * @author Ligong Xu
 * @version $Id: AuthorRequest.java 1 2007-09-22 18:10:03Z ligongx $
 */
public interface AuthorRequest extends Request {

    /**return operation name for authorization check*/
    public String getOperationName();

    public void setOperationName(String pOperationName);

    /**return role name for authorization check*/
    public String getRoleName();

    public void setRoleName(String pRoleName);
}
