package com.closeme.util.message;

import java.io.Serializable;

public class ResponseMessage implements Serializable {

    /**
	 * Generate serial version UID.
	 */
    private static final long serialVersionUID = -1433431578915403590L;

    private boolean flag;

    private String message;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
