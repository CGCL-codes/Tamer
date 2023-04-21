package net.sourceforge.webflowtemplate.appuser.bean;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sourceforge.webflowtemplate.appuser.bean.AppUser;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.binding.validation.ValidationContext;

public class AppUserValidator implements Serializable {

    private static final long serialVersionUID = 1;

    public AppUserValidator() {
    }

    private void validatePasswordLength(String pNewPassword, MessageContext pMessageContext) {
        final int MINIMUM_PASSWORD_LENGTH = 5;
        final int USERNAME_LENGTH = pNewPassword == null ? -1 : pNewPassword.length();
        if (USERNAME_LENGTH < MINIMUM_PASSWORD_LENGTH) {
            pMessageContext.addMessage(new MessageBuilder().error().code("appUser.username.length").arg(MINIMUM_PASSWORD_LENGTH).build());
        }
    }

    private void validateNewPasswordConfirmation(String pNewPassword, String pNewPasswordConfirmation, MessageContext pMessageContext) {
        if (pNewPassword != null && pNewPasswordConfirmation != null) {
            if (!pNewPassword.equals(pNewPasswordConfirmation)) {
                pMessageContext.addMessage(new MessageBuilder().error().code("appUser.changePassword.nonMatching").build());
            }
        }
    }

    private void validatePasswordContent(String pNewPassword, MessageContext pMessageContext) {
        Pattern p = Pattern.compile("\\p{Punct}");
        Matcher m = p.matcher(pNewPassword);
        if (!m.find()) {
            pMessageContext.addMessage(new MessageBuilder().error().code("appUser.password.content.punctuation").build());
        }
        p = Pattern.compile("\\p{Digit}");
        m = p.matcher(pNewPassword);
        if (!m.find()) {
            pMessageContext.addMessage(new MessageBuilder().error().code("appUser.password.content.digit").build());
        }
        p = Pattern.compile("\\p{Alpha}");
        m = p.matcher(pNewPassword);
        if (!m.find()) {
            pMessageContext.addMessage(new MessageBuilder().error().code("appUser.password.content.character").build());
        }
    }

    /**
   * Used by Register flow
   * 
   * @param pAppUser
   * @param pValidationContext
   */
    public void validateRegister(AppUser pAppUser, ValidationContext pValidationContext) {
        validatePasswordLength(pAppUser.getNewPassword(), pValidationContext.getMessageContext());
    }

    public void validateChangePassword(AppUser pAppUser, ValidationContext pValidationContext) {
        if (pValidationContext.getUserEvent().equals("changePassword")) {
            final String NEW_PASSWORD = pAppUser.getNewPassword();
            final MessageContext messages = pValidationContext.getMessageContext();
            validateNewPasswordConfirmation(NEW_PASSWORD, pAppUser.getNewPasswordConfirmation(), messages);
            validatePasswordLength(NEW_PASSWORD, messages);
            validatePasswordContent(NEW_PASSWORD, messages);
        }
    }
}
