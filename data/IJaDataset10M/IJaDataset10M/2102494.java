package com.sitescape.team.context.request;

import java.util.Map;
import java.util.HashMap;
import javax.servlet.http.HttpSession;
import com.sitescape.team.ObjectKeys;

/**
 * Saves properties for life of session.  Only accessed if use is shared, otherwise use database
 * @author Janet
 *
 */
public class HttpSessionContext extends BaseSessionContext {

    private HttpSession session;

    public HttpSessionContext(HttpSession session) {
        this.session = session;
    }

    protected Map getProperties() {
        Map props = (Map) session.getAttribute(ObjectKeys.SESSION_USERPROPERTIES);
        if (props == null) {
            props = new HashMap();
            session.setAttribute(ObjectKeys.SESSION_USERPROPERTIES, props);
        }
        return props;
    }
}
