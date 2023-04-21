package com.fddtool.ui.view.login;

import java.io.IOException;
import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import com.fddtool.exception.NotAuthenticatedException;
import com.fddtool.pd.account.Person;
import com.fddtool.resource.MessageKey;
import com.fddtool.ui.faces.bean.RefreshableManagedBean;
import com.fddtool.ui.view.FddpmaView;
import com.fddtool.ui.view.navigation.NavigationBean;
import com.fddtool.ui.view.navigation.NavigationResults;
import com.fddtool.util.J2EETransaction;

/**
 * The JSF-managed bean that keeps properties of the logged in user.
 * 
 * <strong>Warning:</strong> JSP pages refer to the methods and properties of
 * this bean. Make sure to update the JSPs whenever you rename properties or
 * methods.
 */
public class UserBean extends RefreshableManagedBean {

    /**
     * Initial serialization ID
     */
    private static final long serialVersionUID = -740983439322524311L;

    /**
     * Person's old password. Used when user changes his password.
     */
    private String oldPassword;

    /**
     * Person's new password. Used when user changes his password.
     */
    private String newPassword;

    /**
     * Confirmation of person's new password. Used when user changes his
     * password.
     */
    private String confirmNewPassword;

    /**
     * Returns name of logged in user.
     * 
     * @return String name of the user.
     */
    public String getUsername() {
        Person current = null;
        try {
            current = Person.findCurrent();
            return current.getUserName();
        } catch (NotAuthenticatedException ex) {
            current = null;
        }
        return null;
    }

    /**
     * Indicates if the user successfully logged in.
     * 
     * @return boolean value which is <code>true</code> if user is logged in
     *         and <code>false</code> otherwise.
     */
    public boolean isLoggedIn() {
        Person current = null;
        try {
            current = Person.findCurrent();
        } catch (NotAuthenticatedException ex) {
            current = null;
        }
        return current != null;
    }

    /**
     * Indicates if the logged user has administrative permissions.
     * 
     * @return boolean <code>true</code> if a user is logged in and has
     *         administrative permissions.
     */
    public boolean isInAdminRole() {
        if (isLoggedIn()) {
            Person current = Person.findCurrent();
            return current.isAdmin();
        }
        return false;
    }

    /**
     * Performs logout action. This method is called by JSF framework when user
     * clicks logout menu item.
     * 
     * @return String with value
     *         {@link com.fddtool.ui.view.NavigationResults#LOGOUT NavigationResults.LOGOUT}}
     */
    public String logoutAction() {
        try {
            forwardToDefaultView();
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            if (session != null) {
                session.invalidate();
            }
            ;
        } catch (Exception ex) {
            String message = messageProvider.getMessage(MessageKey.ERROR_LOGIN_ERROR);
            getLogger().error(message, ex);
        }
        return null;
    }

    /**
     * Forwards to the default view of application.
     */
    private void forwardToDefaultView() {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        String redirectPath = FddpmaView.DEFAULT_VIEW.getViewId();
        try {
            externalContext.redirect(externalContext.encodeActionURL(redirectPath));
        } catch (IOException e) {
            throw new FacesException(e.getMessage(), e);
        }
        context.responseComplete();
    }

    /**
     * Returns the old password that a user types in when changing his password.
     * 
     * @return String with old password or <code>null</code> if the user is
     *         not changing his/her password.
     */
    public String getOldPassword() {
        return oldPassword;
    }

    /**
     * Sets the old password that a user types in when changing his password.
     * JSF framework calls this method in response to user's input.
     * 
     * @param oldPassword
     *            String containing old password.
     */
    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    /**
     * Returns the new password that a user types in when changing his password.
     * 
     * @return String with new password or <code>null</code> if the user is
     *         not changing his/her password.
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * Sets the new password that a user types in when changing his password.
     * JSF framework calls this method in response to user's input.
     * 
     * @param newPassword
     *            String containing new password.
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    /**
     * Returns the confirmation of a new password that a user types in when
     * changing his password.
     * 
     * @return String with a confirmation of a new password or <code>null</code>
     *         if the user is not changing his/her password.
     */
    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    /**
     * Sets the confirmation of a new password that a user types in when
     * changing his password. JSF framework calls this method in response to
     * user's input.
     * 
     * @param confirmNewPassword
     *            String containing confirmation of a new password.
     */
    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }

    /**
     * Changes user's password. JSF framework calls this method when user clicks
     * on "Change password" button.
     * 
     * @return String with a result of the operation. The result may be
     *         {@link com.fddtool.ui.view.NavigationResults.SUCCESS NavigationResults.SUCCESS}}
     *         if the password was change, or
     *         {@link com.fddtool.ui.view.NavigationResults#RETRY NavigationResults.RETRY}}
     *         is error happened.
     */
    public String changePasswordAction() {
        J2EETransaction tx = null;
        try {
            tx = new J2EETransaction();
            tx.begin();
            Person.findCurrent().changePassword(getOldPassword(), getNewPassword(), getConfirmNewPassword());
            tx.commit();
            NavigationBean.redirectRequest(FddpmaView.VIEW_PROJECT_GROUP_WORKPLACE);
            return NavigationResults.NONE;
        } catch (Exception ex) {
            handleException(ex, tx);
            return NavigationResults.RETRY;
        }
    }

    /**
     * Handles the request to cancel password change. JSF framework calls this
     * method when user clicks on "Cancel" button in "Force Change Password"
     * view. This action logs out the user.
     * 
     * @return String with a result of the operation. JSF framework uses this
     *         result to determine which view should be displayed next.
     * @see com.fddtool.ui.view.NavigationResults#CANCEL
     */
    public String cancelChangePasswordAction() {
        return logoutAction();
    }
}
