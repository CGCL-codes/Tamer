package net.sourceforge.smarthomephone.action;

import java.util.Map;
import net.sourceforge.smarthomephone.dao.UserDao;
import net.sourceforge.smarthomephone.entities.User;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class LoginAction extends ActionSupport {

    private static final long serialVersionUID = -8871164893777575638L;

    private String userid;

    private String password;

    @SuppressWarnings("unchecked")
    public String login() {
        if (userid == null || userid.length() == 0) {
            return "error";
        }
        UserDao dao = new UserDao();
        User user = dao.getUser(userid);
        if (user != null && password.equals(user.getWebpassword())) {
            Map session = ActionContext.getContext().getSession();
            session.put("user", userid);
            session.put("WW_TRANS_I18N_LOCALE", user.getLang());
            return "success";
        } else {
            addActionError(getText("InvalidLogin"));
            return "error";
        }
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
