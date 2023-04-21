package com.wolfbell.araas.login;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
import javax.net.ssl.*;

/**
 * @version $Revision: 1.1.1.1 $ $Name:  $
 * @author Andreas Klingler
 */
public class ssltest {

    public static void main(String[] args) throws Exception {
        java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        Class factoryclass = Class.forName("com.wolfbell.aaras.login.FooSocketFactory");
        Method factorymethod = factoryclass.getMethod("getDefault", new Class[0]);
        Object factory = factorymethod.invoke(null, new Object[0]);
        Method socketcreatemethod = factoryclass.getMethod("createSocket", new Class[] { String.class, Integer.TYPE });
        Socket socket = (Socket) socketcreatemethod.invoke(factory, new Object[] { "ldap", new Integer(636) });
    }
}
