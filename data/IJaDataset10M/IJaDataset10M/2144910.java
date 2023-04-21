package uk.icat3.sessionbeans.user;

import javax.ejb.Local;
import javax.ejb.Remote;
import uk.icat3.exceptions.SessionException;
import uk.icat3.user.User;

/**
 * This is the business interface for UserSession enterprise bean.
 */
@Local
public interface UserSessionLocal extends User {
}
