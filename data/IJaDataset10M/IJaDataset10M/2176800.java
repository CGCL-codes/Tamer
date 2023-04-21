package osa.ora.server.beans;

import java.io.Serializable;

/**
 *
 * @author ooransa
 */
public class ResultBean implements Serializable {

    private boolean success;

    private int action;

    private String message;

    /** Creates a new instance of ResultBean */
    public ResultBean() {
    }

    public ResultBean(boolean result, int action, String message) {
        this.setSuccess(result);
        this.action = action;
        this.message = message;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
