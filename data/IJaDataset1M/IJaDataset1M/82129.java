package pl.kwiecienm.cvms.action;

import java.util.List;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Factory;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.datamodel.DataModel;
import org.jboss.seam.annotations.datamodel.DataModelSelection;
import org.jboss.seam.annotations.security.Restrict;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.log.Log;
import pl.kwiecienm.cvms.event.EventType;
import pl.kwiecienm.cvms.i18n.Messages;
import pl.kwiecienm.cvms.model.User;
import pl.kwiecienm.cvms.model.dao.UserDao;
import pl.kwiecienm.util.Enc;
import pl.kwiecienm.util.Passwd;

/**
 * CRUD actions for User objects.
 * 
 * @author kwiecienm
 */
@Scope(ScopeType.CONVERSATION)
@Name("userActions")
public class UserActions extends AbstractActions implements CRUDActions {

    /** */
    @Logger
    private Log log;

    /** */
    @In(create = true)
    private UserDao userDao;

    /** */
    @In("loggedUser")
    private User loggedUser;

    /** */
    @RequestParameter
    private Integer userId;

    /** */
    @DataModelSelection
    private User user;

    /** */
    @DataModel
    private List<User> users;

    /** */
    private String newPassword;

    /** */
    private String confirmNewPassword;

    /** */
    @Factory("users")
    public void initDataModel() {
        users = userDao.findAll();
    }

    /**
	 * @return the user
	 */
    public User getUser() {
        return user;
    }

    /**
	 * @param userRef
	 *            the user to set
	 */
    public void setUser(User userRef) {
        this.user = userRef;
    }

    /**
	 * @return the userId
	 */
    public Integer getUserId() {
        return userId;
    }

    /**
	 * @param userIdInt
	 *            the userId to set
	 */
    public void setUserId(Integer userIdInt) {
        this.userId = userIdInt;
    }

    /**
	 * @return the newPassword
	 */
    public String getNewPassword() {
        return newPassword;
    }

    /**
	 * @param newPasswordStr
	 *            the newPassword to set
	 */
    public void setNewPassword(String newPasswordStr) {
        this.newPassword = newPasswordStr;
    }

    /**
	 * @return the confirmNewPassword
	 */
    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    /**
	 * @param confNewPasswdStr
	 *            the confirmNewPassword to set
	 */
    public void setConfirmNewPassword(String confNewPasswdStr) {
        this.confirmNewPassword = confNewPasswdStr;
    }

    /**
	 */
    @Restrict("#{s:hasRole('ADMIN')}")
    public void create() {
        captureCurrentRequest();
        log.debug("creating new user");
        user = new User();
    }

    /**
	 */
    @Restrict("#{s:hasRole('ADMIN')}")
    public void edit() {
        captureCurrentRequest();
        log.debug("editing existing user");
        user = userDao.findById(userId);
        if (user == null) {
            addErrorMessage(Messages.getMessage("message.user_not_found"));
        }
    }

    /**
	 */
    public void settings() {
        captureCurrentRequest();
        user = userDao.findById(loggedUser.getId());
    }

    /**
	 */
    public void saveSettings() {
        userDao.update(user);
        addMessage(Messages.getMessage("message.settings_saved"));
    }

    /**
	 * Only user may change his password.
	 */
    public void selfChangePassword() {
        log.debug("changing user's password");
        if (newPassword != null && confirmNewPassword != null && newPassword.equals(confirmNewPassword)) {
            String encPasswd = Enc.enc(newPassword);
            user.setPassword(encPasswd);
            addMessage(Messages.getMessage("message.password_saved"));
            userDao.update(user);
        } else {
            addErrorMessage(Messages.getMessage("message.passwords_donot_match"));
        }
    }

    /**
	 * Only user may change his password.
	 */
    @Restrict("#{s:hasRole('ADMIN')}")
    public void resetPassword() {
        log.debug("resetting user's password");
        user = userDao.findById(userId);
        if (user != null) {
            String randomPassword = Passwd.randomPassword();
            String encPasswd = Enc.enc(randomPassword);
            user.setPassword(encPasswd);
            raiseEvent(EventType.USER_PASSWORD_RESET, user, randomPassword);
            userDao.update(user);
        } else {
            addErrorMessage(Messages.getMessage("message.user_not_found"));
        }
    }

    /**
	 */
    @Restrict("#{s:hasRole('ADMIN')}")
    public void save() {
        log.debug("saving new user");
        if (user.getId() > 0) {
            userDao.update(user);
            returnToCapturedRequest();
        } else {
            if (!users.contains(user)) {
                String randomPassword = Passwd.randomPassword();
                String encPasswd = Enc.enc(randomPassword);
                user.setPassword(encPasswd);
                raiseEvent(EventType.USER_CREATED, user, randomPassword);
                userDao.save(user);
                users.add(user);
                returnToCapturedRequest();
            } else {
                addControlMessage("userLogin", Messages.getMessage("message.user_login_already_taken"));
            }
        }
    }

    /**
	 */
    @Restrict("#{s:hasRole('ADMIN')}")
    public void delete() {
        log.debug("deleting existing user");
        user = userDao.findById(userId);
        if (user != null) {
            users.remove(user);
            userDao.delete(user);
        } else {
            addErrorMessage(Messages.getMessage("message.user_not_found"));
        }
    }

    /**
	 */
    public void cancel() {
        log.debug("canceling user related action");
        user = null;
        returnToCapturedRequest(false);
    }
}
