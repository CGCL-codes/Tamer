package org.springframework.webflow.context.portlet;

import java.util.Iterator;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import org.springframework.binding.collection.SharedMap;
import org.springframework.binding.collection.StringKeyedMapAdapter;
import org.springframework.web.util.WebUtils;
import org.springframework.webflow.context.web.HttpSessionMapBindingListener;
import org.springframework.webflow.core.collection.AttributeMapBindingListener;
import org.springframework.webflow.core.collection.CollectionUtils;

/**
 * A Shared Map backed by the Portlet session, for accessing session scoped attributes.
 * 
 * @author Keith Donald
 * @author Scott Andrews
 */
public class PortletSessionMap extends StringKeyedMapAdapter implements SharedMap {

    /**
	 * The wrapped portlet request, providing access to the session.
	 */
    private PortletRequest request;

    /**
	 * Create a map wrapping the session of given request.
	 */
    public PortletSessionMap(PortletRequest request) {
        this.request = request;
    }

    /**
	 * Internal helper to get the portlet session associated with the wrapped request, or null if there is no such
	 * session.
	 * <p>
	 * Note that this method will not force session creation.
	 */
    private PortletSession getSession() {
        return request.getPortletSession(false);
    }

    protected Object getAttribute(String key) {
        PortletSession session = getSession();
        if (session == null) {
            return null;
        }
        Object value = session.getAttribute(key);
        if (value instanceof HttpSessionMapBindingListener) {
            return ((HttpSessionMapBindingListener) value).getListener();
        } else {
            return value;
        }
    }

    protected void setAttribute(String key, Object value) {
        PortletSession session = request.getPortletSession(true);
        if (value instanceof AttributeMapBindingListener) {
            session.setAttribute(key, new HttpSessionMapBindingListener((AttributeMapBindingListener) value, this));
        } else {
            session.setAttribute(key, value);
        }
    }

    protected void removeAttribute(String key) {
        PortletSession session = getSession();
        if (session != null) {
            session.removeAttribute(key);
        }
    }

    protected Iterator getAttributeNames() {
        PortletSession session = getSession();
        return session == null ? CollectionUtils.EMPTY_ITERATOR : CollectionUtils.toIterator(session.getAttributeNames());
    }

    public Object getMutex() {
        PortletSession session = request.getPortletSession(true);
        Object mutex = session.getAttribute(WebUtils.SESSION_MUTEX_ATTRIBUTE);
        return mutex != null ? mutex : session;
    }
}
