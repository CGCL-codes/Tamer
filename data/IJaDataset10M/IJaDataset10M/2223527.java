package org.fb4j.permission;

import org.fb4j.FacebookObject;

/**
 * @author Mino Togna
 * 
 */
public interface Permissions extends FacebookObject {

    long getUser();

    boolean statusUpdate();

    boolean photoUpload();

    boolean sms();

    boolean createListing();

    boolean offlineAccess();

    boolean email();

    boolean createEvent();

    boolean rsvpEvent();
}
