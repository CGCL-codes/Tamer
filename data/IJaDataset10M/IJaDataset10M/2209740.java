package com.icesoft.jasper.compiler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.text.MessageFormat;
import java.util.MissingResourceException;

/**
 * Class responsible for converting error codes to corresponding localized error
 * messages.
 *
 * @author Jan Luehe
 */
public class Localizer {

    private static final Log log = LogFactory.getLog(Localizer.class);

    public static String getMessage(String errCode) {
        String errMsg = errCode;
        return errMsg;
    }

    public static String getMessage(String errCode, String arg) {
        return getMessage(errCode, new Object[] { arg });
    }

    public static String getMessage(String errCode, String arg1, String arg2) {
        return getMessage(errCode, new Object[] { arg1, arg2 });
    }

    public static String getMessage(String errCode, String arg1, String arg2, String arg3) {
        return getMessage(errCode, new Object[] { arg1, arg2, arg3 });
    }

    public static String getMessage(String errCode, String arg1, String arg2, String arg3, String arg4) {
        return getMessage(errCode, new Object[] { arg1, arg2, arg3, arg4 });
    }

    public static String getMessage(String errCode, Object[] args) {
        String errMsg = errCode;
        for (int i = 0; i < args.length; i++) {
            errMsg = errMsg + " " + String.valueOf(args[i]);
        }
        return errMsg;
    }
}
