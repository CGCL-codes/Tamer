package annone.client.web;

import javax.servlet.http.HttpSession;

public class Session {

    public static String getLanguage(HttpSession session) {
        return "it";
    }

    public static String getTheme(HttpSession session) {
        return "themes/default/theme.css";
    }
}
