package com;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import model.UserDetails;

@ManagedBean
@SessionScoped
public class Registration {

    @ManagedProperty(value = "#{productManager}")
    private ProductManager productManager;

    @ManagedProperty(value = "#{userDetails}")
    private UserDetails userDetails;

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    public void setProductManager(ProductManager productManager) {
        this.productManager = productManager;
    }

    public void registerUser() {
        try {
            encodePassword();
            productManager.registerNewUser(userDetails);
            FacesContext.getCurrentInstance().addMessage("registration-form", new FacesMessage(FacesMessage.SEVERITY_INFO, "user" + userDetails.getUsername() + "registered successfully", ""));
            Util.sendMail(userDetails.getUsername(), userDetails.getMail(), userDetails.getConfirmationCode());
            userDetails.resetForm();
        } catch (DataIntegrityViolationException e) {
            FacesContext.getCurrentInstance().addMessage("registration-form", new FacesMessage(FacesMessage.SEVERITY_INFO, "user already exists", "user already exists"));
        }
    }

    private void encodePassword() {
        org.springframework.security.crypto.password.PasswordEncoder encoder = new BCryptPasswordEncoder();
        userDetails.setPassword(encoder.encode(userDetails.getPassword()));
    }
}
