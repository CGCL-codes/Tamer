package org.apache.harmony.security.tests.support;

import java.security.BasicPermission;

public class MyBasicPermission extends BasicPermission {

    private static final long serialVersionUID = -4220730623258019258L;

    public MyBasicPermission(String name) {
        super(name);
    }
}
