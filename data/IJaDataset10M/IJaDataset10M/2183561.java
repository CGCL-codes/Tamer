package org.scidac.sam.eln.serverproxy;

import eln.server.Administration;
import eln.server.ELNPrincipal;
import eln.server.NotebookServerException;
import eln.server.NotebookAccessDeniedException;
import eln.server.NotebookServerProxy;
import eln.server.event.AdminActionEvent;
import eln.server.event.AdminActionListener;
import eln.util.AddUserDialog;
import eln.util.InterestDialog;
import eln.util.UserPasswordChangeDialog;
import eln.client.ELNClient;
import eln.util.UserSelectionDialog;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLDecoder;
import java.util.Vector;
import javax.swing.*;
import javax.swing.JFileChooser;

/**
 *  Description of the Class
 *
 * @author     d3h252
 * @created    June 11, 2003
 */
public class SAMAdministration extends Administration {

    /**
   *  Constructor for the SAMAdministration object
   *
   * @param  theNSP  Description of Parameter
   */
    public SAMAdministration(NotebookServerProxy theNSP) {
        super(theNSP);
    }

    /**
   *  Adds a feature to the NewUser attribute of the SAMAdministration object
   */
    public void addNewUser() {
        String newUserName = null;
        try {
            Frame dummyFrame = new Frame();
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            SAMAddUserDialog aud = new SAMAddUserDialog(dummyFrame, "Grant Access to Notebook User");
            aud.setLocation((d.width - aud.getSize().width) / 2, (d.height - aud.getSize().height) / 2);
            aud.show();
            if (aud.userClickedAdd()) {
                newUserName = aud.getSelectedUser();
                boolean addAsAdmin = aud.getAddAsAdmin();
                ;
                if (newUserName != null) {
                    try {
                        mServer.addNewUser(new ELNPrincipal(newUserName, null), null, addAsAdmin);
                        JOptionPane.showMessageDialog(new JFrame(), "User: " + newUserName + " successfully granted access", "Access Granted", JOptionPane.INFORMATION_MESSAGE);
                    } catch (NotebookAccessDeniedException nade) {
                        ELNClient.showReauthDialog(nade);
                    } catch (NotebookServerException nse) {
                        JOptionPane.showMessageDialog(new JFrame(), "Error granting access: " + newUserName + " : " + nse.getMessage(), "ELN Error:Cancelled Grant Access", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(new JFrame(), "No User Selected!", "ELN Error:Cancelled Grant Access", JOptionPane.ERROR_MESSAGE);
                }
            }
            aud.dispose();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    /**
   *  Description of the Method
   */
    public void changeCredential() {
        try {
            String[] userlist;
            boolean isAdmin = mServer.getUserRole() == NotebookServerProxy.ADMIN_ROLE;
            SAMCredentials currentCreds = (SAMCredentials) mServer.getCredentialStructure();
            String currentUser = URLDecoder.decode(currentCreds.getNBUserName(), "UTF-8");
            if (isAdmin) {
                userlist = mServer.getUserList();
            } else {
                userlist = new String[] { currentUser };
            }
            String user = null;
            Frame dummyFrame = new Frame();
            Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
            UserPasswordChangeDialog upcd = new UserPasswordChangeDialog(dummyFrame, userlist, "User Password Change", (!isAdmin));
            upcd.setLocation((d.width - upcd.getSize().width) / 2, (d.height - upcd.getSize().height) / 2);
            upcd.show();
            user = upcd.getSelectedUser();
            char[] oldPassword = upcd.getCurrentPassword();
            char[] newPassword = upcd.getNewPassword();
            upcd.dispose();
            if (user != null) {
                SAMCredentials newCreds = new SAMCredentials();
                newCreds.setNBUserName(user);
                newCreds.setNBPassword(newPassword);
                SAMCredentials submitterCreds = new SAMCredentials();
                if (isAdmin) {
                    submitterCreds.setNBUserName(currentCreds.getNBUserName());
                } else {
                    submitterCreds.setNBUserName(user);
                }
                submitterCreds.setNBPassword(oldPassword);
                submitterCreds.setGrpUserName(submitterCreds.getNBUserName());
                submitterCreds.setGrpPassword(submitterCreds.getNBPassword());
                try {
                    if (user.equals(currentUser)) {
                        if (JOptionPane.showConfirmDialog(null, "Changing your password will require restarting your browser. Continue?", "Change Password Warning", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.CANCEL_OPTION) {
                            throw new NotebookServerException("Change Cancelled by User");
                        }
                    }
                    mServer.resetUserCredentials(submitterCreds, newCreds);
                    if (user.equals(currentUser)) {
                        JOptionPane.showMessageDialog(new JFrame(), "Your password has been updated. You must restart your browser and return to the launch page for this notebook to continue.", "Password Changed - Restart Browser", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(new JFrame(), "User: " + user + "'s password successfully updated", "Password Changed", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (NotebookAccessDeniedException nade) {
                    ELNClient.showReauthDialog(nade);
                } catch (NotebookServerException nse) {
                    JOptionPane.showMessageDialog(new JFrame(), "Error changing password for user: " + user + " : " + nse.getMessage(), "ELN Error:Cancelled Password Change", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NotebookAccessDeniedException nade) {
            ELNClient.showReauthDialog(nade);
        } catch (NotebookServerException nse) {
            JOptionPane.showMessageDialog(new JFrame(), "Error reading list of users: " + nse.getMessage(), "ELN Error:Cancelled Remove User", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
