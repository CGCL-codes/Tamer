package openvend.portlet;

import java.util.HashMap;
import java.util.Map;
import openvend.main.I_OvDestroyableObject;

/**
 * A data container to pass objects from the action to the render phase within the lifecycle of a portlet request.<p/>
 * 
 * @author Thomas Weckert
 * @version $Revision: 1.5 $
 * @since 1.0
 */
public class OvPortletLifecycleData implements I_OvDestroyableObject {

    private Exception exception;

    private Map attributes;

    public OvPortletLifecycleData() {
        super();
        this.exception = null;
        this.attributes = new HashMap();
    }

    /**
	 * @see openvend.main.I_OvDestroyableObject#destroy()
	 */
    public void destroy() throws Exception {
        try {
            if (attributes != null) {
                attributes.clear();
            }
        } finally {
            attributes = null;
            exception = null;
        }
    }

    public boolean hasAttribute(Object key) {
        return attributes.containsKey(key);
    }

    public Object setAttribute(Object key, Object value) {
        return attributes.put(key, value);
    }

    public Object getAttribute(Object key) {
        return attributes.get(key);
    }

    public boolean hasException() {
        return (exception != null);
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}
