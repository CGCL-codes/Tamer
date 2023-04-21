package org.tagbox.engine.action.http;

import org.tagbox.engine.http.HttpTagEnvironment;
import org.tagbox.engine.TagBoxException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Cookie;
import org.w3c.dom.Element;
import org.tagbox.util.Log;

public class GetCookieAction extends HttpAction {

    public void process(Element e, HttpTagEnvironment env) throws TagBoxException {
        String name = e.getAttribute("name");
        name = evaluate(name, env, e);
        HttpServletRequest req = env.getServletRequest();
        String result = null;
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (int i = 0; result == null && i < cookies.length; ++i) {
                if (cookies[i].getName().equals(name)) result = cookies[i].getValue();
            }
        }
        if (result == null) e.getParentNode().removeChild(e); else e.getParentNode().replaceChild(e.getOwnerDocument().createTextNode(result), e);
    }
}
