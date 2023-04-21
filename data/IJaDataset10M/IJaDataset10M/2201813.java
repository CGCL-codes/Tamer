package com.aptech.fpt.c0810g.newspj.beans;

import com.aptech.fpt.c0810g.newspj.entity.Manager;
import com.aptech.fpt.c0810g.newspj.model.checkvalidate.CheckValidate;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author ZANG26
 */
@ManagedBean(name = "login")
@RequestScoped
public class LoginBean {

    private String userName;

    private String password;

    private String Result;

    public String getResult() {
        return Result;
    }

    public void setResult(String Result) {
        this.Result = Result;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public LoginBean() {
    }

    public void doLogin() {
        String acction = "QuanLy.aspx";
        FacesContext context = FacesContext.getCurrentInstance();
        CheckValidate check = new CheckValidate();
        boolean userNameIsEmpty = check.isEmpty(this.getUserName());
        if (!userNameIsEmpty) {
            boolean passwordIsEmpty = check.isEmpty(this.getPassword());
            if (!passwordIsEmpty) {
                boolean checkPasswordValid = check.checkLeght(6, this.getPassword(), 5);
                if (checkPasswordValid) {
                    Manager userObj = new Manager();
                    userObj.setUserName(this.getUserName());
                    userObj.setPassWord(this.getPassword());
                    boolean result = userObj.login();
                    if (result) {
                        ShareBean bean = (ShareBean) context.getApplication().evaluateExpressionGet(context, "#{shareBean}", ShareBean.class);
                        bean.doPutElement("user-info", userObj);
                        try {
                            context.getExternalContext().redirect(acction);
                        } catch (IOException ex) {
                            Logger.getLogger(LoginBean.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        context.addMessage("login-mesage", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Message", "Error : Login faile !"));
                        context.renderResponse();
                    }
                } else {
                    context.addMessage("login-mesage", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Password", "Password : Value is must less than 6 charactor."));
                    context.renderResponse();
                }
            } else {
                context.addMessage("login-mesage", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Password", "Password : Value is required."));
                context.renderResponse();
            }
        } else {
            context.addMessage("login-mesage", new FacesMessage(FacesMessage.SEVERITY_ERROR, "User Name", "User Name : Value is required."));
            context.renderResponse();
        }
    }
}
