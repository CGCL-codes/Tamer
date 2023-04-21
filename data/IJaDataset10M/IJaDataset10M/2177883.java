package com.release.bean;

/**
 * @author sonia
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class UserBean {

    String id;

    String username;

    String status;

    /**
	 * @return Returns the status.
	 */
    public String getStatus() {
        return status;
    }

    /**
	 * @param status The status to set.
	 */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
	 * @return Returns the username.
	 */
    public String getUsername() {
        return username;
    }

    /**
	 * @param username The username to set.
	 */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
	 * @return Returns the id.
	 */
    public String getId() {
        return id;
    }

    /**
	 * @param id The id to set.
	 */
    public void setId(String id) {
        this.id = id;
    }
}
