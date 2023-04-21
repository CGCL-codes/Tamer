package org.openscience.nmrshiftdb.modules.actions;

import java.util.Vector;
import org.apache.turbine.modules.Action;
import org.apache.turbine.services.security.TurbineSecurity;
import org.apache.turbine.util.RunData;
import org.apache.turbine.util.db.Criteria;
import org.apache.turbine.util.mail.MailMessage;
import org.openscience.nmrshiftdb.om.NmrshiftdbUser;
import org.openscience.nmrshiftdb.om.NmrshiftdbUserPeer;
import org.openscience.nmrshiftdb.util.GeneralUtils;

/**
 *This action sets a new password for a user and sends it by email.
 *
 * @author     shk3
 * @created    April 29, 2003
 */
public class RememberPassword extends Action {

    /**
	 *  Performs the action.
	 *
	 * @param  data           The RunData object.
	 * @exception  Exception  Description of Exception
	 */
    public void doPerform(RunData data) throws Exception {
        String username = data.getParameters().getString("username", "");
        String email = data.getParameters().getString("email", "");
        Criteria crit = new Criteria();
        crit.add(NmrshiftdbUserPeer.USERNAME, username);
        crit.add(NmrshiftdbUserPeer.EMAIL, email);
        Vector v = NmrshiftdbUserPeer.doSelect(crit);
        if (v.size() == 0) {
            data.setMessage("There is no such combination of username/email");
            data.setScreenTemplate("RememberPassword");
            return;
        }
        String password = "" + "" + new Double("" + Math.random() * 100000).intValue();
        NmrshiftdbUser user = (NmrshiftdbUser) v.get(0);
        user.setPassword(TurbineSecurity.encryptPassword(password));
        user.save();
        MailMessage emailmess = new org.apache.turbine.util.mail.MailMessage(GeneralUtils.getSmtpServer(data), email, GeneralUtils.getAdminEmail(data), "New NMRShiftDB password", "You requested a new password for NMRShiftDB. The new passwod is " + password + ". You can change this as soon as you are logged in!" + GeneralUtils.getEmailSignature(data));
        if (emailmess.send() == false) {
            GeneralUtils.sendEmailToEventReceivers("Sending new password failed", "User " + username + " requested a new password, but sending the email with the password failed", data, 2);
        }
        data.setMessage("The new password has been emailed to your address!");
        data.setScreenTemplate("ShowMessage");
    }
}
