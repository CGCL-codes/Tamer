package org.myjerry.evenstar.openid;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import com.dyuproject.openid.OpenIdUser;

public class OpenID {

    private static final String OPEN_ID_SESSION_ATTRIBUTE = "evenstar.open.id.session.attribute";

    public static void setUser(HttpServletRequest request, OpenIdUser user) {
        HttpSession session = request.getSession(true);
        Object previousUser = session.getAttribute(OPEN_ID_SESSION_ATTRIBUTE);
        if (previousUser != null) {
            session.removeAttribute(OPEN_ID_SESSION_ATTRIBUTE);
        }
        session.setAttribute(OPEN_ID_SESSION_ATTRIBUTE, user);
    }

    public static OpenIdUser getUser(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        Object user = session.getAttribute(OPEN_ID_SESSION_ATTRIBUTE);
        if (user != null) {
            if (user instanceof OpenIdUser) {
                return (OpenIdUser) user;
            }
        }
        return null;
    }

    public static String getUserEmail(HttpServletRequest request) {
        OpenIdUser user = getUser(request);
        return getUserEmail(user);
    }

    @SuppressWarnings("unchecked")
    public static String getUserEmail(OpenIdUser user) {
        if (user != null) {
            Object attribute = user.getAttribute("axschema");
            if (attribute != null && attribute instanceof Map) {
                Map<String, String> map = (Map<String, String>) attribute;
                if (map != null) {
                    return map.get("email");
                }
            }
        }
        return null;
    }

    public static String getName(OpenIdUser user) {
        return null;
    }

    public static void removeUser(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        session.removeAttribute(OPEN_ID_SESSION_ATTRIBUTE);
    }
}
