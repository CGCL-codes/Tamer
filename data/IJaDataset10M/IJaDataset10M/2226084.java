package com.justin.foundation.util;

import java.util.Hashtable;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JndiUtil {

    @SuppressWarnings("unchecked")
    public static final Object lookup(String jndiName, Hashtable hashtable) {
        try {
            InitialContext context = null;
            if (hashtable == null) {
                context = new InitialContext();
            } else {
                context = new InitialContext(hashtable);
            }
            return context.lookup(jndiName);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }
}
