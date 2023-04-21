package ces.coffice.joblog.ui.form;

import org.apache.struts.action.ActionForm;

public class AuthLstOfRoleForm extends ActionForm {

    private String checkUser;

    private String checkId;

    private String user_name;

    private String user_cid;

    public void setCheckUser(String checkUser) {
        this.checkUser = checkUser;
    }

    public String getCheckUser() {
        return this.checkUser;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }

    public String getCheckId() {
        return this.checkId;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_name() {
        return this.user_name;
    }

    public void setUser_cid(String user_cid) {
        this.user_cid = user_cid;
    }

    public String getUser_cid() {
        return this.user_cid;
    }
}
