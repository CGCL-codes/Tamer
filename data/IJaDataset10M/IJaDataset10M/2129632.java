package edu.uwlax.cs.oayonlinestore.client.utils;

import edu.uwlax.cs.oayonlinestore.exceptions.UserVisibleException;

public class ExceptionTool {

    public static String extractUserMessage(Throwable t) {
        Throwable x = t;
        while ((x != null) && !(x instanceof UserVisibleException)) x = x.getCause();
        if (x == null) x = t;
        return x.getMessage();
    }

    public static String fullDetails(Throwable t) {
        StringBuffer b = new StringBuffer();
        while (t != null) {
            b.append(t + ":" + t.getMessage() + "\n\n");
            t = t.getCause();
        }
        return b.toString();
    }
}
