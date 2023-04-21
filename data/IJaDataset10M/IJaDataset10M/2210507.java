package com.thyrsus.project.object;

import java.io.Serializable;
import java.util.Date;
import com.thyrsus.project.dao.user.entity.User;
import com.thyrsus.project.form.user.UserForm;

/**
 * 
 * @author maximiliense
 *
 */
public class UserContext implements Serializable {

    private static final long serialVersionUID = -872003628584696222L;

    private Long id;

    private String login;

    private String password;

    private String firstName;

    private String lastName;

    private String mail;

    private Date birthDate;

    private String phone;

    private String locale;

    private boolean guest;

    private String ajaxResult;

    public UserContext() {
    }

    public UserContext(UserForm user) {
        this.login = user.getLogin();
        this.password = user.getPassword();
        this.firstName = user.getFirstName().toUpperCase();
        this.lastName = user.getLastName().toUpperCase();
        this.mail = user.getMail();
        this.birthDate = user.getBirthDate();
        this.phone = user.getPhone();
        this.locale = user.getLocale();
    }

    public UserContext(User user) {
        this.id = user.getId().getId();
        this.login = user.getLogin();
        this.password = user.getPassword();
        this.firstName = user.getFirstName().toUpperCase();
        this.lastName = user.getLastName().toUpperCase();
        this.mail = user.getMail();
        this.birthDate = user.getBirthDate();
        this.phone = user.getPhone();
        this.locale = user.getLocale();
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName.toUpperCase();
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName.toUpperCase();
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAjaxResult() {
        return ajaxResult;
    }

    public void setAjaxResult(String ajaxResult) {
        this.ajaxResult = ajaxResult;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isGuest() {
        return guest;
    }

    public void setGuest(boolean guest) {
        this.guest = guest;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
