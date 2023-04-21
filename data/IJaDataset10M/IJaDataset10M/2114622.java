package com.feyaSoft.plugin.util;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

/**
 * @author fzhuang Created at Jun 21, 2006
 * @Updated at Jun 21, 2006
 */
public class MsgResource {

    public static String getMessage(String key) {
        Locale english = Locale.ENGLISH;
        String result = AppContext.getMsgRseAppContext().getMessage(key, null, english);
        return result;
    }

    public static String getMessage(HttpServletRequest request, String key) {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        Locale language = slr.resolveLocale(request);
        String result = AppContext.getMsgRseAppContext().getMessage(key, null, language);
        return result;
    }
}
