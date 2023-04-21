package nl.headspring.photoz.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Created by IntelliJ IDEA.
 * User: eelco
 * Date: Oct 5, 2010
 * Time: 8:41:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class MaskingResourceBundle extends ResourceBundle {

    private static final Log LOG = LogFactory.getLog(MaskingResourceBundle.class);

    private final ResourceBundle delegate;

    public MaskingResourceBundle(ResourceBundle delegate) {
        this.delegate = delegate;
    }

    protected Object handleGetObject(String key) {
        try {
            String s = delegate.getString(key);
            if (s == null) {
                s = "#" + key + "#";
            }
            return s;
        } catch (MissingResourceException e) {
            LOG.warn("Missing resource: " + e.getKey());
            return "@" + key + "@";
        }
    }

    public Enumeration<String> getKeys() {
        return delegate.getKeys();
    }
}
