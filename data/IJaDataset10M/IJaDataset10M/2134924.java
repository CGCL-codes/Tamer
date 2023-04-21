package com.fddtool.ui.view.login;

import com.fddtool.exception.EmailException;
import com.fddtool.pd.account.Person;
import com.fddtool.resource.MessageKey;
import com.fddtool.services.email.EmailProcessor;
import com.fddtool.ui.faces.FacesUtils;
import com.fddtool.ui.faces.bean.RefreshableManagedBean;
import com.fddtool.ui.view.navigation.NavigationResults;
import com.fddtool.util.J2EETransaction;

/**
 * This is a JSF-managed bean that supports the "Forgot My Password view".
 * The password may be sent to a user if he provided the email address.
 *
 * @author Serguei Khramtchenko
 */
public class ForgotPasswordBean extends RefreshableManagedBean {

    /**
     * Initial serialization ID.
     */
    private static final long serialVersionUID = 8534590012807611127L;

    /**
     * Email to send forgotten password to.
     */
    private String email;

    /**
     * Sends email to the user with the account information.
     * JSF framework calls this method when
     * a user submits the information.
     *
     * @return String indicating success or failure of the operation.
     *
     * @see com.fddtool.ui.view.NavigationResults#SUCCESS
     * @see com.fddtool.ui.view.NavigationResults#RETRY
     */
    public String submitAction() {
        String result = NavigationResults.RETRY;
        String newPassword = null;
        try {
            Person person = Person.findByEmail(getEmail());
            if (person == null) {
                FacesUtils.addErrorMessage(messageProvider.getMessage(MessageKey.ERROR_UNKNOWN_EMAIL));
            } else {
                J2EETransaction tx = null;
                try {
                    tx = new J2EETransaction();
                    tx.begin();
                    newPassword = person.resetPassword();
                    tx.commit();
                } catch (Exception ex) {
                    handleException(ex, tx);
                }
                try {
                    EmailProcessor.sendForgottenPassword(person, newPassword);
                    result = NavigationResults.SUCCESS;
                    String msg = messageProvider.getMessage(MessageKey.INFO_FORGOT_PASSWORD_EMAIL_SENT);
                    FacesUtils.addInfoMessage(msg);
                    setEmail(null);
                } catch (EmailException ex) {
                    String msg = messageProvider.getMessage(MessageKey.ERROR_CANNOT_SEND_EMAIL);
                    getLogger().error(msg, ex);
                    FacesUtils.addErrorMessage(msg);
                }
            }
        } catch (Exception ex) {
            handleException(ex, null);
        }
        return result;
    }

    /**
     * Returns email address to send notification to.
     * It may be empty if the user did not provide it yet.
     *
     * @return String containg email address as user typed it in.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email address to send notification to. JSF framework
     * calls this method when user submits the view.
     *
     * @param email String containg email address as user typed it in.
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
