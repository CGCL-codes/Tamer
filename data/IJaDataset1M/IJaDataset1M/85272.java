package org.wings;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wings.session.Session;
import org.wings.session.SessionManager;

/**
 * TODO: documentation
 *
 * @author <a href="mailto:haaf@mercatis.de">Armin Haaf</a>
 * @author <a href="mailto:H.Zeller@acm.org">Henner Zeller</a>
 * @version $Revision: 1759 $
 */
public abstract class Resource implements Serializable, URLResource, Renderable {

    private static final Log logger = LogFactory.getLog("org.wings");

    /**
     *
     */
    protected String id;

    /**
     * TODO: documentation
     */
    protected String extension;

    /**
     * TODO: documentation
     */
    protected String mimeType;

    protected Collection headers;

    protected Resource(String extension, String mimeType) {
        this.extension = extension;
        this.mimeType = mimeType;
    }

    protected Resource() {
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public int getLength() {
        return -1;
    }

    /**
     * TODO: documentation
     *
     * @return
     */
    public String getExtension() {
        return extension;
    }

    /**
     * Returns the mime type of this resource.
     *
     * @return
     */
    public String getMimeType() {
        return mimeType;
    }

    public void setHeaders(Collection headers) {
        this.headers = headers;
    }

    public Collection getHeaders() {
        return headers;
    }

    /**
     *
     */
    public String getId() {
        return id;
    }

    public abstract SimpleURL getURL();

    /**
     * TODO: documentation
     *
     * @return
     */
    public String toString() {
        return getId();
    }

    public Session getSession() {
        return SessionManager.getSession();
    }

    public static final class HeaderEntry implements Map.Entry {

        final Object key;

        Object value;

        /**
         * Create new entry.
         */
        public HeaderEntry(Object k, Object v) {
            key = k;
            value = v;
        }

        public Object getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }

        public Object setValue(Object newValue) {
            Object oldValue = value;
            value = newValue;
            return oldValue;
        }

        public boolean equals(Object o) {
            if (!(o instanceof Map.Entry)) return false;
            Map.Entry e = (Map.Entry) o;
            Object k1 = getKey();
            Object k2 = e.getKey();
            if (k1 == k2 || (k1 != null && k1.equals(k2))) {
                Object v1 = getValue();
                Object v2 = e.getValue();
                if (v1 == v2 || (v1 != null && v1.equals(v2))) return true;
            }
            return false;
        }

        public int hashCode() {
            return (key == null ? 0 : key.hashCode()) ^ (value == null ? 0 : value.hashCode());
        }

        public String toString() {
            return getKey() + "=" + getValue();
        }
    }
}
