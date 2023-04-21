package org.blueoxygen.lotion.invoice.actions;

import org.blueoxygen.cimande.function.Check;
import org.blueoxygen.cimande.security.SessionCredentials;
import org.blueoxygen.cimande.security.SessionCredentialsAware;

/**
 * @author intercitra
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class AddInvoice extends InVoiceProductForm implements SessionCredentialsAware {

    private SessionCredentials sess;

    private String id;

    private Check check = new Check();

    public String execute() {
        if (getInvoice().getInVoiceNo().equalsIgnoreCase("")) {
            addActionError("please input Opportunity name");
            return INPUT;
        }
        return SUCCESS;
    }

    public void setSessionCredentials(SessionCredentials sessionCredentials) {
        this.sess = sessionCredentials;
    }
}
