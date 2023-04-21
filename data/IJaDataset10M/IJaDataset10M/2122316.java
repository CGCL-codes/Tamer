package com.cineplex.rolemanagement;

import java.sql.Date;
import java.util.Map;
import org.apache.struts2.interceptor.SessionAware;
import com.cineplex.role.User;
import com.opensymphony.xwork2.ActionSupport;
import com.cineplex.dao.*;

@SuppressWarnings("serial")
public class RegisterAction extends ActionSupport implements SessionAware {

    private Map session;

    private User user;

    private String password1;

    private String password2;

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public String execute() {
        if (password1.equals(password2)) {
            UserDao dao = UserDao.getInstance();
            User u = dao.find("userid", user.getUserid());
            if (u != null) {
                this.addActionError("���û����Ѿ���ռ�����");
                return INPUT;
            }
            user.setPassword(password1);
            user.setAccount(0);
            user.setCreateDate(new Date((new java.util.Date()).getTime()));
            UserDao.getInstance().save(user);
            session.put("user", user);
            return SUCCESS;
        }
        this.addFieldError(password1, "���벻һ��");
        return INPUT;
    }

    public void setPassword1(String password1) {
        this.password1 = password1;
    }

    public String getPassword1() {
        return password1;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getPassword2() {
        return password2;
    }

    @Override
    public void setSession(Map session) {
        this.session = session;
    }
}
