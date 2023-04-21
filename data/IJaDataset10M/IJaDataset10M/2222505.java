package com.jxml.qare.qhome.register;

import com.jxml.quick.*;
import com.jxml.qare.qhome.*;

public final class RegisterApp extends RegisterAppDataBase {

    public boolean installed;

    public String name;

    public RegisterApp() {
    }

    public RegisterApp(String name) {
        this(name, true);
    }

    public RegisterApp(String name, boolean installed) {
        this.name = name;
        this.installed = installed;
    }
}
