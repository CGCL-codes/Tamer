package com.google.code.openperfmon.web.admin;

import org.apache.commons.lang.StringUtils;
import com.google.code.openperfmon.domain.AppUser;
import com.google.code.openperfmon.service.NonUniqueFieldException;
import com.google.code.openperfmon.service.ServiceFactory;
import com.opensymphony.xwork2.ActionSupport;

@SuppressWarnings("serial")
public class AddEditUserAction extends ActionSupport {

    private Long id;

    private AppUser user;

    private String password;

    private String passwordConfirm;

    public String input() throws Exception {
        if (id == null) {
            user = new AppUser();
        } else {
            user = ServiceFactory.getUserService().getById(id);
        }
        return super.input();
    }

    public String save() throws Exception {
        boolean passEmpty = StringUtils.isBlank(password);
        if (passEmpty && user.getId() == null) {
            addFieldError("password", "Please enter password and password confirm");
            return INPUT;
        }
        if (!passEmpty) user.setPassword(password);
        try {
            ServiceFactory.getUserService().saveUser(user, passEmpty ? null : password);
        } catch (NonUniqueFieldException e) {
            addActionError(e.getMessage());
            return INPUT;
        }
        return SUCCESS;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
