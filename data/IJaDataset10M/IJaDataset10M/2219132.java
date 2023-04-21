package com.dbxml.db.common.security;

import com.dbxml.db.core.Database;
import com.dbxml.db.core.FaultCodes;
import com.dbxml.db.core.security.SecurityException;
import com.dbxml.db.core.security.SecurityManager;
import com.dbxml.db.core.security.User;
import com.dbxml.db.core.security.UserStack;
import com.dbxml.util.SimpleConfigurable;

/**
 * SecurityManagerBase
 */
public abstract class SecurityManagerBase extends SimpleConfigurable implements SecurityManager {

    protected Database database;

    protected UserStack userStack;

    protected User magicUser;

    public void setDatabase(Database database) {
        this.database = database;
    }

    public void setMagicUser(User magicUser) throws SecurityException {
        if (this.magicUser == null) this.magicUser = magicUser; else throw new SecurityException(FaultCodes.SEC_INVALID_ACCESS, "Can't overwrite Magic User");
    }

    public void setUserStack(UserStack userStack) throws SecurityException {
        if (this.userStack == null) this.userStack = userStack; else throw new SecurityException(FaultCodes.SEC_INVALID_ACCESS, "Can't overwrite User Stack");
    }

    public String getCurrentUserID() {
        User user = userStack.getCurrentUser();
        if (user != null) return user.getId(); else return null;
    }
}
