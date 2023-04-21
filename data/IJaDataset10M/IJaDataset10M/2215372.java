package org.hip.vif.admin.member.mail;

import java.io.IOException;
import org.hip.kernel.exc.VException;
import org.hip.vif.admin.member.Activator;
import org.hip.vif.core.bom.VIFMember;
import org.hip.vif.core.mail.AbstractMail;

/**
 * Mail sent to the user after resetting the password.
 * 
 * Created on 14.08.2003
 * @author Luthiger
 */
public class PasswordResetMail extends AbstractMail {

    private static final String KEY_RESET = "mail.passwort.reset";

    private String password;

    /**
	 * PasswordResetMail constructor.
	 * 
	 * @param inContext Context
	 * @param inMember VIFMember
	 * @param inBody StringBuffer
	 * @throws VException
	 * @throws IOException
	 */
    public PasswordResetMail(VIFMember inMember, String inPassword) throws VException, IOException {
        super(inMember);
        password = inPassword;
    }

    /**
	 * Hook for subclasses
	 * 
	 * @return StringBuilder
	 */
    protected StringBuilder getBody() {
        return new StringBuilder(getFormattedMessage(Activator.getMessages(), KEY_RESET, password));
    }

    protected StringBuilder getBodyHtml() {
        return getBody();
    }
}
