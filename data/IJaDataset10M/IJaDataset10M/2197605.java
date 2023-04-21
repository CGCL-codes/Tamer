package org.vrspace.server;

import java.net.URL;
import java.util.HashSet;
import org.vrspace.util.*;

/**
This class encapsulates authentication information:<br>
- login name<br>
- password<br>
- client ID (class and it within the class)<br>
- classpath used for commands<br>
- objects owned by this client<br>
<b>NOTE:</b><br>
All fields are public to enable database storage. Take care not to pass
AuthInfo to any objects! <br>
What if an object creates an AuthInfo and stores it?
*/
public class AuthInfo extends OwnedDBObject {

    /** login name */
    public String login;

    /** password */
    public String password;

    /** client's class name */
    public String className;

    /** client's id within it's class */
    public long id;

    /** Ability to login. A bot that can travel to other host must have valid
  Authinfo, but no client can login using this info. Strictly local bots don't
  need Authinfo at all. Note that a bot must have Authinfo in order to
  execute commands.
  */
    public boolean canLogin = true;

    /** maximum number of sessions a client can open */
    public int maxSessions = 1;

    /** list of owned objects */
    public String[] ownedObjects = new String[0];

    HashSet owned;

    public AuthInfo() {
    }

    /**
  creates new AuthInfo with <b>login</b> and <b>password</b>
  */
    public AuthInfo(String login, String password) {
        this.login = login;
        this.password = password;
    }

    /**
  Does this user own <b>obj</b>?
  */
    protected boolean isOwned(VRObject obj) {
        if (owned == null) {
            loadOwned();
        }
        return owned.contains(obj.getClass().getName() + " " + obj.db_id);
    }

    /** 
  Loads the owned list.  To be used, for example, if the database
  updates ownedObjects.
  */
    public void loadOwned() {
        owned = new HashSet();
        for (int i = 0; i < ownedObjects.length; i++) {
            owned.add(ownedObjects[i]);
        }
    }

    /**
  Own <b>obj</b>
  */
    protected void addOwned(VRObject obj) {
        if (!isOwned(obj)) {
            owned.add(obj.getClass().getName() + " " + obj.db_id);
            ownedObjects = (String[]) owned.toArray(ownedObjects);
            put();
        }
    }

    /**
  Do not own <b>obj</b> any longer
  */
    protected void removeOwned(VRObject obj) {
        if (isOwned(obj)) {
            owned.remove(obj.getClass().getName() + " " + obj.db_id);
            ownedObjects = (String[]) owned.toArray(ownedObjects);
            put();
        }
    }
}
