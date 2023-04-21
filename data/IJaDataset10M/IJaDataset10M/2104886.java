package edu.upmc.opi.caBIG.caTIES.client.vr.desktop.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import edu.upmc.opi.caBIG.caTIES.client.config.ClientConfiguration;
import edu.upmc.opi.caBIG.caTIES.client.dispatcher.emailer.CaTIES_EmailerClient;
import edu.upmc.opi.caBIG.caTIES.client.vr.admin.dialogs.CaTIES_NewUserDialog;
import edu.upmc.opi.caBIG.caTIES.client.vr.desktop.CaTIES_Desktop;
import edu.upmc.opi.caBIG.caTIES.client.vr.desktop.CaTIES_Messages;
import edu.upmc.opi.caBIG.caTIES.client.vr.utils.UIUtilities;
import edu.upmc.opi.caBIG.caTIES.middletier.CaTIES_UserImpl;

public class UserNewButtonAction extends AbstractAction {

    public void actionPerformed(ActionEvent arg0) {
        CaTIES_Desktop desktop = CaTIES_Desktop.getInstance();
        CaTIES_NewUserDialog d = new CaTIES_NewUserDialog(desktop);
        d.setModal(true);
        UIUtilities.centerDialog(d);
        d.setVisible(true);
        if (!d.isCancelled()) {
            if (d.isSuccessful()) {
                d.dispose();
                if (ClientConfiguration.isUsingEmailer()) {
                    int ret = JOptionPane.showConfirmDialog(desktop, "Do you want to send a notification email to the user?", CaTIES_Messages.NEW_USER_DIALOG_TITLE, JOptionPane.YES_NO_OPTION);
                    if (ret == JOptionPane.YES_OPTION) {
                        CaTIES_EmailerClient emailer = CaTIES_Desktop.getEmailerClient();
                        CaTIES_UserImpl cu = CaTIES_Desktop.getCurrentUser();
                        CaTIES_UserImpl user = d.getUser();
                        try {
                            if (emailer.sendUserUpdateEmail(user.obj, cu.getCurrentUserOrganization().obj.getOrganization(), true)) {
                                JOptionPane.showMessageDialog(CaTIES_Desktop.getInstance(), "An email notification was sent to " + user.obj.getAddress().getEmailAddress() + ". You were CC'd on the email.");
                            } else {
                                JOptionPane.showMessageDialog(CaTIES_Desktop.getInstance(), "The email failed.", "Email Failed", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            JOptionPane.showMessageDialog(CaTIES_Desktop.getInstance(), "The email failed.\n Reason:" + e.getMessage(), "Email Failed", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
                if (desktop.getAdminTab().getExplorer().getBrowser().addEntity(d.getUser())) desktop.getAdminTab().getExplorer().getBrowser().changeSelectionTo(d.getUser()); else JOptionPane.showMessageDialog(desktop, "An error occured while displaying the user. Please restart the client to view this user.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
