package ua.eshop.web.action;

import java.io.IOException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.eshop.data.User;
import ua.eshop.service.MailSender;
import ua.eshop.service.UserService;
import com.sun.faces.util.MessageFactory;

@Service
public class RestorePasswordAction {

    @Autowired
    private UserService userService;

    @Autowired
    private MailSender mailSender;

    private String username;

    private String email;

    private String newPassword;

    private String confirmNewPassword;

    private User user;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void validateUsername(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        try {
            UserDetails user = userService.loadUserByUsername((String) value);
            if (user != null) {
                return;
            }
        } catch (UsernameNotFoundException ex) {
        }
        throw new ValidatorException(MessageFactory.getMessage(context, "restorePassword.userNotFound", new Object[] { value }));
    }

    public void validateEmail(FacesContext context, UIComponent validate, Object value) throws ValidatorException {
        if (!((String) value).matches(NewUserAction.EMAIL_PATTERN)) {
            throw new ValidatorException(MessageFactory.getMessage(context, "addNewUser.invalidEmail", new Object[] { value }));
        }
        User user = userService.getUserByEmail((String) value);
        if (user == null) {
            throw new ValidatorException(MessageFactory.getMessage(context, "restorePassword.emailNotFound", new Object[] { value }));
        }
    }

    public void restorePassword(ActionEvent event) {
        User user = null;
        try {
            if ((username != null && username.length() > 0 && (user = (User) userService.loadUserByUsername(username)) != null) || (email != null && (user = userService.getUserByEmail(email)) != null)) {
                sendRestorePasswordMessage(user);
                FacesContext facesContext = FacesContext.getCurrentInstance();
                FacesMessage message = MessageFactory.getMessage(facesContext, "restorePassword.emailSent");
                message.setSeverity(FacesMessage.SEVERITY_INFO);
                facesContext.addMessage(event.getComponent().getClientId(facesContext), message);
                return;
            }
        } catch (UsernameNotFoundException ex) {
        }
        FacesContext facesContext = FacesContext.getCurrentInstance();
        FacesMessage message = MessageFactory.getMessage(facesContext, "restorePassword.wrongUsernameOrEmail");
        facesContext.addMessage(event.getComponent().getClientId(facesContext), message);
    }

    private void sendRestorePasswordMessage(User user) {
        String securityToken = DigestUtils.md5Hex(username + user.getPassword());
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            mailSender.sendMessage(user.getEmail(), MessageFactory.getMessage(facesContext, "restorePassword.mailSubject").getSummary(), MessageFactory.getMessage(facesContext, "restorePassword.mailText", new Object[] { user.getFirstName(), user.getLastName(), facesContext.getExternalContext().getRequestContextPath(), securityToken }).getSummary());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void validateConfirmNewPassword(FacesContext context, UIComponent validate, Object value) throws ValidatorException {
        if (newPassword != null && !newPassword.equals((String) value)) {
            throw new ValidatorException(MessageFactory.getMessage(context, "addNewUser.invalidConfirm"));
        }
    }

    public void changePassword(ActionEvent event) throws IOException {
        user.setPassword(DigestUtils.md5Hex(newPassword));
        userService.updateUser(user);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        FacesMessage message = MessageFactory.getMessage(facesContext, "editUserProfile.passwordChanged");
        message.setSeverity(FacesMessage.SEVERITY_INFO);
        facesContext.addMessage(event.getComponent().getClientId(facesContext), message);
    }
}
