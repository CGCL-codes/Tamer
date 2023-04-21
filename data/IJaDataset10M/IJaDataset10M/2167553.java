package ch.eivd.jct.utils;

import ch.eivd.jct.gui.ChangePasswordDialog;
import ch.eivd.jct.gui.PasswordDialog;
import ch.eivd.jct.model.MapModel;
import java.awt.*;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.*;

/**
 * This class contains the current user level applied for this project.
 * <p>
 * Possible user levels are for now�:
 * <ul>
 * <li> 0 - can't edit anything
 * <li> 1 - can edit everything
 * </ul>
 *
 * @author dlifschitz
 * @author nseriot
 */
public class UserLevel {

    private static final ResourceBundle resbundle = ResourceBundle.getBundle("jctstrings", Locale.getDefault());

    private static final String _ERROR = resbundle.getString("global.error");

    private static final String _ALLOW_CHANGES = resbundle.getString("userLevel.allowChanges");

    private static final String _ALLOW_CHANGES_TITLE = resbundle.getString("userLevel.allowChangesTitle");

    private static final String _CANT_CHANGE_LEVEL1 = resbundle.getString("userLevel.cantChangeLevel1");

    private static final String _CANT_CHANGE_LEVEL2 = resbundle.getString("userLevel.cantChangeLevel2");

    private int userLevel = 0;

    private int defaultPassword = new String("jctei4b").hashCode();

    private int hashedPassword = defaultPassword;

    private MapModel mapModel;

    /**
     * Constructor
     * @param mapModel the MapModel instance
     * @param hashedPassword the password that allow the user to change the level
     */
    public UserLevel(MapModel mapModel, int hashedPassword) {
        this.mapModel = mapModel;
        setPassword(hashedPassword);
    }

    /**
     * Return the actually user level.
     *
     * @return  0 = simple user, cannot make any change
     *          1 = administrator, can make changes
     */
    public int getLevel() {
        return userLevel;
    }

    /**
     * Check if the given password let the user change the level
     *
     * @param password to check
     * @return true if this password let the user change the level, false otherwise
     */
    public boolean checkPassword(String password) {
        return checkPassword(password.hashCode());
    }

    /**
     * Check if the given hash code let the user change te level
     *
     * @param hashedPassword hash code to check
     * @return true if this hash code let the user change the level, false otherwise
     */
    public boolean checkPassword(int hashedPassword) {
        return ((this.hashedPassword == hashedPassword) || (defaultPassword == hashedPassword));
    }

    /**
     * Sets the current level to 1.
     */
    public void setSimpleUser() {
        userLevel = 0;
    }

    /**
     * Return true if the current level is set to 2.
     *
     * @return true if the user is allowed to edit the info, false otherwise
     */
    public boolean canEditInfo() {
        return (userLevel > 0);
    }

    /**
     * Return true if the current level is set to 1.
     *
     * @return true if the user is allowed to edit the data, false otherwise
     */
    public boolean canEditData() {
        return (userLevel > 0);
    }

    /**
     * Return true if the current level is set to 1.
     *
     * @return true if the user is allowed to change the password, false otherwise
     */
    public boolean canChangePasswd() {
        return (userLevel > 0);
    }

    /**
     * Set the password.
     *
     * @param hashCode - the hash code from the password.
     */
    public void setPassword(int hashCode) {
        if (hashCode == 0) {
            hashedPassword = defaultPassword;
        } else {
            hashedPassword = hashCode;
        }
    }

    /**
     * promps the user to change the password
     *
     * @param parent the Frame from which the dialog is displayed
     */
    public void changePassword(Frame parent) {
        ChangePasswordDialog changePasswd = new ChangePasswordDialog(parent, this);
        changePassword(changePasswd);
    }

    /**
     * prompts the user to change the password
     *
     * @param parent the Dialog from which the dialog is displayed
     */
    public void changePassword(Dialog parent) {
        ChangePasswordDialog changePasswd = new ChangePasswordDialog(parent, this);
        changePassword(changePasswd);
    }

    /**
     * called by the public ChangePassword methods
     *
     * @param d the ChangePasswordDialog instance
     */
    private void changePassword(ChangePasswordDialog d) {
        if (d.showDialog()) {
            setPassword(d.getNewPassword().hashCode());
            mapModel.setPasswordHash(hashedPassword);
        }
    }

    /**
     * promps the user to change the level
     *
     * @param parent the Frame from which the dialog is displayed
     * @return true if the user level has chagend
     */
    public boolean changeLevel(Frame parent) {
        PasswordDialog passwordDialog = new PasswordDialog(parent, this);
        return changeLevel(passwordDialog);
    }

    /**
     * promps the user to change the level
     *
     * @param parent the Dialog from which the dialog is displayed
     * @return true if the user level has changed
     */
    public boolean changeLevel(Dialog parent) {
        PasswordDialog passwordDialog = new PasswordDialog(parent, this);
        return changeLevel(passwordDialog);
    }

    /**
     * called by the public changeLevel methods
     * @param d the PasswordDialog instance
     * @return true if the user level has changed
     */
    private boolean changeLevel(PasswordDialog d) {
        if (userLevel == 1) {
            if (JOptionPane.showConfirmDialog(d.getParent(), _ALLOW_CHANGES, _ALLOW_CHANGES_TITLE, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION) {
                userLevel = 0;
                return true;
            } else {
                return false;
            }
        }
        if (!mapModel.canWrite()) {
            JOptionPane.showMessageDialog(d.getParent(), _CANT_CHANGE_LEVEL1 + "\n" + _CANT_CHANGE_LEVEL2, _ERROR, JOptionPane.ERROR_MESSAGE);
            return false;
        } else if (d.showDialog()) {
            userLevel = 1;
            return true;
        }
        return false;
    }
}
