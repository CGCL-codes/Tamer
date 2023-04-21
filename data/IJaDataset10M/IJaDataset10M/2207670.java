package com.bugfree4j.commons;

import com.bugfree4j.per.common.web.UserSessionInfo;

/**
 * @author bugfree4j
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class UserInfo implements UserSessionInfo {

    private String username;

    private String realname;

    private String id;

    private String mail;

    /**
	 * @return
	 */
    public String getRealname() {
        return realname;
    }

    /**
	 * @return
	 */
    public String getUsername() {
        return username;
    }

    /**
	 * @param string
	 */
    public void setRealname(String string) {
        realname = string;
    }

    /**
	 * @param string
	 */
    public void setUsername(String string) {
        username = string;
    }

    /**
	 * @return
	 */
    public String getId() {
        return id;
    }

    /**
	 * @return
	 */
    public String getMail() {
        return mail;
    }

    /**
	 * @param string
	 */
    public void setId(String string) {
        id = string;
    }

    /**
	 * @param string
	 */
    public void setMail(String string) {
        mail = string;
    }
}
