package com.mea.beans.auth;

import java.io.Serializable;

public class LoginResultBean implements Serializable {

    /** 
	 * 
	 */
    private static final long serialVersionUID = -1985989082602213409L;

    private boolean success = false;

    private String[] domains;

    private int validCode;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String[] getDomains() {
        return domains;
    }

    public void setDomains(String[] domains) {
        this.domains = domains;
    }

    public int getValidCode() {
        return validCode;
    }

    public void setValidCode(int validCode) {
        this.validCode = validCode;
    }
}
