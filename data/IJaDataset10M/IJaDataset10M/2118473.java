package com.vitria.test.ejb.util;

import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JndiUtil {

    private static String COMPONENT_ENV_PREFIX = "java:comp/env";

    public static Object lookupWithLogicalName(String name) throws NamingException {
        InitialContext ctx = null;
        try {
            ctx = new InitialContext();
            return ctx.lookup(adaptLogicalName(name));
        } finally {
            if (ctx != null) {
                ctx.close();
            }
        }
    }

    private static String adaptLogicalName(String name) {
        if (name.startsWith("/")) {
            return COMPONENT_ENV_PREFIX + name;
        } else {
            return COMPONENT_ENV_PREFIX + "/" + name;
        }
    }
}
