package com.bebo.platform.lib.api.users;

import com.bebo.platform.lib.api.BeboMethod;

public class SetStatus extends BeboMethod {

    public SetStatus(String sessionKey, String status) {
        this(sessionKey, status, false);
    }

    public SetStatus(String sessionKey, String status, boolean clear) {
        super(sessionKey);
        addParameter("status", status);
        addParameter("clear", Boolean.toString(clear));
    }

    @Override
    public String getMethodName() {
        return "users.setStatus";
    }
}
