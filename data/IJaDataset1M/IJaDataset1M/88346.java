package net.sf.jguard.jee.taglib;

import net.sf.jguard.core.authentication.credentials.JGuardCredential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.security.auth.Subject;
import java.util.Set;

/**
 * display the Private Credential of the Subject.
 *
 * @author <a href="mailto:diabolo512@users.sourceforge.net">Charles Gay</a>
 */
public class PrivCredential extends JGuardTagCredential {

    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(PrivCredential.class);

    /**
     * serial version id.
     */
    private static final long serialVersionUID = 3546084670207767349L;

    protected Set<JGuardCredential> getCredentials(Subject subject) {
        return subject.getPrivateCredentials(JGuardCredential.class);
    }

    protected boolean isPrivate() {
        return true;
    }

    protected String getTagName() {
        return "PrivCredential";
    }
}
