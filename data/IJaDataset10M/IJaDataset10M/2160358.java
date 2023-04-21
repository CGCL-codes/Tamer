package actiontest;

import com.opensymphony.xwork2.ActionSupport;

/**
 * @author hrmzone.cn
 *
 */
@SuppressWarnings("serial")
public class SimpleAction extends ActionSupport {

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String execute() {
        if (username.equals("")) {
            return ERROR;
        } else {
            return SUCCESS;
        }
    }
}
