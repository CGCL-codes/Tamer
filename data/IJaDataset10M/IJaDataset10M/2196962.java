package org.aigebi.rbac;

/**
 * @author Ligong Xu
 * @version $Id: BaseRequest.java 1 2007-09-22 18:10:03Z ligongx $
 */
public class BaseRequest implements Request {

    private String mUsername;

    public BaseRequest() {
    }

    public BaseRequest(String pUsername) {
        mUsername = pUsername;
    }

    /** 
	 * @see org.aigebi.rbac.Request#getUsername()
	 */
    public String getUsername() {
        return mUsername;
    }

    /** 
	 * @see org.aigebi.rbac.Request#setUsername(java.lang.String)
	 */
    public void setUsername(String pUsername) {
        mUsername = pUsername;
    }
}
