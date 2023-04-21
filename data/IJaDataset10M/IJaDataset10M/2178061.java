package radius.log;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogConfigurationException;
import org.apache.commons.logging.LogFactory;

/**
 * @author <a href="mailto:zzzhc0508@hotmail.com">zzzhc</a>
 * 
 */
public class RadiusLogFactory extends LogFactory {

    public Object getAttribute(String name) {
        return null;
    }

    public String[] getAttributeNames() {
        return null;
    }

    public Log getInstance(Class clazz) throws LogConfigurationException {
        return null;
    }

    public Log getInstance(String name) throws LogConfigurationException {
        return null;
    }

    public void release() {
    }

    public void removeAttribute(String name) {
    }

    public void setAttribute(String name, Object value) {
    }
}
