package com.yugte.ums;

import javax.annotation.ManagedBean;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.support.WebApplicationContextUtils;
import com.yugte.ums.service.UserService;
import com.yugte.ums.user.User;

/**
 * @author Imran pariyani
 * 
 * {@link http://pariyani.com} pariyani
 */
@ManagedBean("loginBean")
@Scope("request")
public class LoginBean {

    private final AuthenticationManager am;

    private String nameError;

    private PreferencesBean preferencesBean;

    private UserService userService;

    /**
     * XXX Returns <code>nameError</code>.
     * 
     * @return Returns the {@link #nameError}.
     */
    public String getNameError() {
        return nameError;
    }

    /**
     * XXX Set <code>nameError</code>.
     * 
     * @param nameError The {@link #nameError} to set.
     */
    public void setNameError(String nameError) {
        this.nameError = nameError;
    }

    @Inject
    public LoginBean(@Named("authenticationManager") AuthenticationManager am, final PreferencesBean preferencesBean, final UserService service) {
        this.am = am;
        this.preferencesBean = preferencesBean;
        this.userService = service;
    }

    public String login() {
        try {
            if (this.preferencesBean.isAuthenticated()) {
                return "/loggedin.html";
            }
            Authentication request = new UsernamePasswordAuthenticationToken(this.getUserName(), getPassword());
            Authentication result = am.authenticate(request);
            result.getAuthorities();
            SecurityContextHolder.getContext().setAuthentication(result);
            this.preferencesBean.setAuthData(result);
            ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            ApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
            User user = this.userService.getUser(1);
            this.preferencesBean.setUser(user);
            return "/loggedin.html?faces-redirect=true";
        } catch (AuthenticationException e) {
            FacesUtils.addErrorString(null, "login.failed");
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new FacesException(e);
        }
    }

    private String userName;

    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void validateName(ValueChangeEvent e) {
        String name = (String) e.getNewValue();
        if (name.contains("_")) nameError = "Name cannot contain underscores"; else if (name.equals("")) nameError = "Name cannot be blank"; else nameError = "";
    }

    public void validate(ComponentSystemEvent e) {
        UIComponent form = e.getComponent();
        FacesContext fc = FacesContext.getCurrentInstance();
        UIInput newPass = (UIInput) form.findComponent("newPass");
        if (true) fc.renderResponse();
    }
}
