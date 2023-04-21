package org.tagbox.engine.http;

import javax.servlet.http.HttpServletRequest;
import org.tagbox.engine.http.HttpTagEnvironment;

public class HttpTools extends org.tagbox.util.http.HttpTools {

    public static String makeAbsoluteURL(String href, HttpTagEnvironment env) {
        return makeAbsoluteURL(href, env.getServletRequest());
    }
}
