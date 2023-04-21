package net.suberic.pooka.gui.propedit;

import java.util.*;
import net.suberic.util.gui.propedit.*;
import net.suberic.util.VariableBundle;

/**
 * The controller class for the NewUserProfileWizard.
 */
public class NewUserProfileWizardController extends WizardController {

    /**
   * Creates a NewUserProfileWizardController.
   */
    public NewUserProfileWizardController(String sourceTemplate, WizardEditorPane wep) {
        super(sourceTemplate, wep);
    }

    /**
   * Checks the state transition to make sure that we can move from
   * state to state.
   */
    public void checkStateTransition(String oldState, String newState) throws PropertyValueVetoException {
        getEditorPane().validateProperty(oldState);
        if (newState.equals("userName")) {
            String userName = getManager().getCurrentProperty("UserProfile._newValueWizard.user.from", "");
            PropertyEditorUI userNameEditor = getManager().getPropertyEditor("UserProfile._newValueWizard.userName.userName");
            setUniqueProperty(userNameEditor, userName, "UserProfile._newValueWizard.userName.userName");
            String smtpServerName = "";
            PropertyEditorUI smtpServerNameEditor = getManager().getPropertyEditor("UserProfile._newValueWizard.userName.smtpServerName");
            smtpServerName = getManager().getCurrentProperty("NewStoreWizard.editors.smtp.outgoingServer", "__default");
            if (smtpServerName.equalsIgnoreCase("__new")) {
                smtpServerName = getManager().getCurrentProperty("NewStoreWizard.editors.smtp.server", "");
                setUniqueProperty(smtpServerNameEditor, smtpServerName, "UserProfile._newValueWizard.userName.smtpServerName");
            } else if (smtpServerName.equalsIgnoreCase("__default")) {
                smtpServerName = getManager().getProperty("NewStoreWizard.editors.smtp.outgoingServer.listMapping.__default.label", "< Global Default SMTP Server >");
                smtpServerNameEditor.setOriginalValue(smtpServerName);
                smtpServerNameEditor.resetDefaultValue();
            } else {
                smtpServerNameEditor.setOriginalValue("<" + smtpServerName + ">");
                smtpServerNameEditor.resetDefaultValue();
            }
        }
    }

    /**
   * Gets the next state.
   */
    public String getNextState(String currentState) {
        int current = mStateList.indexOf(mState);
        if (current > -1 && current < (mStateList.size() - 1)) {
            String newState = mStateList.get(current + 1);
            return newState;
        } else {
            return null;
        }
    }

    /**
   * Gets the state that should be displayed next from a back request.
   */
    public String getBackState(String currentState) {
        int current = mStateList.indexOf(currentState);
        if (current >= 1) {
            String newState = mStateList.get(current - 1);
            return newState;
        } else {
            return null;
        }
    }

    /**
   * Saves all of the properties for this wizard.
   */
    protected void saveProperties() throws PropertyValueVetoException {
        Properties userProperties = createUserProperties();
        Properties smtpProperties = createSmtpProperties();
        addAll(userProperties);
        addAll(smtpProperties);
        String accountName = getManager().getCurrentProperty("UserProfile._newValueWizard.userName.userName", "testUserProfile");
        MultiEditorPane mep = (MultiEditorPane) getManager().getPropertyEditor("UserProfile");
        if (mep != null) {
            mep.addNewValue(accountName);
        } else {
            appendProperty("UserProfile", accountName);
        }
        String defaultSmtpServer = getManager().getCurrentProperty("NewStoreWizard.editors.smtp.outgoingServer", "__default");
        if (defaultSmtpServer.equals("__new")) {
            String smtpServerName = getManager().getCurrentProperty("UserProfile._newValueWizard.userName.smtpServerName", "");
            mep = (MultiEditorPane) getManager().getPropertyEditor("OutgoingServer");
            if (mep != null) {
                mep.addNewValue(smtpServerName);
            } else {
                appendProperty("OutgoingServer", smtpServerName);
            }
        }
    }

    /**
   * Finsihes the wizard.
   */
    public void finishWizard() throws PropertyValueVetoException {
        saveProperties();
        getEditorPane().getWizardContainer().closeWizard();
    }

    /**
   * Creates the userProperties from the wizard values.
   */
    public Properties createUserProperties() {
        Properties returnValue = new Properties();
        String userName = getManager().getCurrentProperty("UserProfile._newValueWizard.userName.userName", "testUserProfile");
        String from = getManager().getCurrentProperty("UserProfile._newValueWizard.user.from", "test@example.com");
        String fromPersonal = getManager().getCurrentProperty("UserProfile._newValueWizard.user.fromPersonal", "");
        String replyTo = getManager().getCurrentProperty("UserProfile._newValueWizard.user.replyTo", "");
        String replyToPersonal = getManager().getCurrentProperty("UserProfile._newValueWizard.user.replyToPersonal", "");
        returnValue.setProperty("UserProfile." + userName + ".mailHeaders.From", from);
        returnValue.setProperty("UserProfile." + userName + ".mailHeaders.FromPersonal", fromPersonal);
        returnValue.setProperty("UserProfile." + userName + ".mailHeaders.ReplyTo", replyTo);
        returnValue.setProperty("UserProfile." + userName + ".mailHeaders.ReplyToPersonal", replyToPersonal);
        returnValue.setProperty("UserProfile." + userName + ".mailHeaders.ReplyToPersonal", replyToPersonal);
        return returnValue;
    }

    /**
   * Creates the smtpProperties from the wizard values.
   */
    public Properties createSmtpProperties() {
        Properties returnValue = new Properties();
        String userName = getManager().getCurrentProperty("UserProfile._newValueWizard.userName.userName", "testUserProfile");
        String defaultSmtpServer = getManager().getCurrentProperty("NewStoreWizard.editors.smtp.outgoingServer", "__default");
        if (defaultSmtpServer.equals("__new")) {
            String serverName = getManager().getCurrentProperty("UserProfile._newValueWizard.userName.smtpServerName", "");
            String server = getManager().getCurrentProperty("NewStoreWizard.editors.smtp.server", "");
            String port = getManager().getCurrentProperty("NewStoreWizard.editors.smtp.port", "");
            String authenticated = getManager().getCurrentProperty("NewStoreWizard.editors.smtp.authenticated", "");
            String user = getManager().getCurrentProperty("NewStoreWizard.editors.smtp.user", "");
            String password = getManager().getCurrentProperty("NewStoreWizard.editors.smtp.password", "");
            returnValue.setProperty("OutgoingServer." + serverName + ".server", server);
            returnValue.setProperty("OutgoingServer." + serverName + ".port", port);
            returnValue.setProperty("OutgoingServer." + serverName + ".authenticated", authenticated);
            if (authenticated.equalsIgnoreCase("true")) {
                returnValue.setProperty("OutgoingServer." + serverName + ".user", user);
                returnValue.setProperty("OutgoingServer." + serverName + ".password", password);
            }
            returnValue.setProperty("UserProfile." + userName + ".mailServer", serverName);
        } else {
            returnValue.setProperty("UserProfile." + userName + ".mailServer", defaultSmtpServer);
        }
        return returnValue;
    }

    /**
   * Adds all of the values from the given Properties to the
   * PropertyEditorManager.
   */
    void addAll(Properties props) {
        Set<String> names = props.stringPropertyNames();
        for (String name : names) {
            getManager().setProperty(name, props.getProperty(name));
        }
    }

    public void setUniqueProperty(PropertyEditorUI editor, String originalValue, String propertyName) {
        String value = originalValue;
        boolean success = false;
        for (int i = 0; !success && i < 10; i++) {
            if (i != 0) {
                value = originalValue + "_" + i;
            }
            try {
                editor.setOriginalValue(value);
                editor.resetDefaultValue();
                getManager().setTemporaryProperty(propertyName, value);
                success = true;
            } catch (PropertyValueVetoException pvve) {
            }
        }
    }

    /**
   * Appends the given value to the property.
   */
    public void appendProperty(String property, String value) {
        List<String> current = getManager().getPropertyAsList(property, "");
        current.add(value);
        getManager().setProperty(property, VariableBundle.convertToString(current));
    }
}
