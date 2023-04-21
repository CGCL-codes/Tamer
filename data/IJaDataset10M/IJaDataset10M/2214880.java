package com.ivis.xprocess.web.properties;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginMsg {

    private static final String BUNDLE_NAME = "com.ivis.xprocess.web.properties.login";

    public static String ACCOUNT_ALREADY_LOGGED_IN;

    public static String APACHE_AUTHENTICATION_FAILED;

    public static String ACCOUNT_DOES_NOT_EXIST_IN_DATASOURCE;

    public static String FAILED_TO_CONNECT_TO_SERVER;

    public static String NOT_A_VALID_WEB_USER;

    public static String OTHER;

    static {
        final Logger logger = Logger.getLogger(LoginMsg.class.getName());
        ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME, Locale.getDefault());
        for (Field field : LoginMsg.class.getDeclaredFields()) {
            if (!Modifier.isFinal(field.getModifiers())) {
                if (bundle != null) {
                    try {
                        field.set(null, bundle.getString(field.getName()));
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (MissingResourceException e) {
                        try {
                            String missingValueMessage = "Missing message for " + field.getName() + " in " + BUNDLE_NAME;
                            logger.log(Level.SEVERE, missingValueMessage);
                            field.set(null, missingValueMessage);
                        } catch (IllegalArgumentException e1) {
                            e1.printStackTrace();
                        } catch (IllegalAccessException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
