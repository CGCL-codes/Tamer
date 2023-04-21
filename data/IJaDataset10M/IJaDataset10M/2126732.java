package goose.web.utils;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ActionForwardParameters {

    private Map params = new HashMap();

    private boolean redirect = false;

    private ActionMapping mapping;

    public ActionForwardParameters(ActionMapping mapping) {
        this.mapping = mapping;
    }

    public ActionForwardParameters add(Hashtable parametersValues) {
        for (Iterator i = parametersValues.keySet().iterator(); i.hasNext(); ) {
            String key = (String) i.next();
            params.put(key, (String) parametersValues.get(key));
        }
        return this;
    }

    public ActionForwardParameters add(String key, String value) {
        params.put(key, value);
        return this;
    }

    public ActionForwardParameters add(String key, Object value) {
        return add(key, value.toString());
    }

    public ActionForwardParameters add(String key, int value) {
        return add(key, Integer.toString(value));
    }

    public ActionForwardParameters add(String key, long value) {
        return add(key, Long.toString(value));
    }

    public ActionForwardParameters add(String key, double value) {
        return add(key, Double.toString(value));
    }

    public ActionForwardParameters add(String key, float value) {
        return add(key, Float.toString(value));
    }

    public ActionForwardParameters add(String key, boolean value) {
        return add(key, Boolean.toString(value));
    }

    public void setRedirect(boolean redirect) {
        this.redirect = redirect;
    }

    public ActionForward forward(String name) {
        ActionForward forward = mapping.findForward(name);
        StringBuffer path = new StringBuffer(forward.getPath());
        boolean hasParam = true;
        if (path.indexOf("?") == -1) hasParam = false;
        Iterator iter = params.entrySet().iterator();
        if (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            if (hasParam) path.append("&"); else path.append("?");
            path.append(entry.getKey() + "=" + entry.getValue());
            while (iter.hasNext()) {
                entry = (Map.Entry) iter.next();
                path.append("&" + entry.getKey() + "=" + entry.getValue());
            }
        }
        ActionForward newForward = new ActionForward(path.toString());
        newForward.setRedirect(redirect);
        return newForward;
    }
}
