package org.az.paccman.services;

import org.az.model.User;
import org.az.tb.common.vo.client.ContactVo;

/**
 * User-related service for server-side usage.
 * @author bububu
 *
 */
public interface UserAccountService {

    /**
     * Generates almost unique session id, creates a new PersistentSession object with this id.
     * The new session id is stored in UserSession.
     * WARNING: This method relies on User object stored in UserSession bean.
     * @param isPersistent true if the new PersistentSession object must be saved in database.
     * @return new session id.
     */
    public String createAndInitSession(boolean isPersistent);

    /**
     * Puts the person object to the UserSession and initializes its fields and session id.
     * @param person User object to put to the session.
     * @param isSessionPersistent if the user session must be saved to the database or not.
     * @return user profile vo without secret data.
     */
    public ContactVo initLoggedInUser(User person, boolean isSessionPersistent);
}
