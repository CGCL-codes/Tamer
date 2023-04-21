package jfun.yan.xml.nuts;

import java.util.List;
import java.util.Properties;
import jfun.yan.Component;
import jfun.yan.Components;
import jfun.yan.SimpleComponent;

/**
 * The &lt;props&gt; tag that creates Properties object.
 * <p>
 * @author Ben Yu
 * Nov 17, 2005 1:03:30 AM
 */
public class PropertiesNut extends EntriesNut {

    public PropertiesNut() {
        super.setKey_type(String.class);
        super.setOf(String.class);
    }

    /**
   * Disable "key-type" by throwing exception.
   */
    public void setKey_type(Class key_type) {
        throw raise("attribute \"key-type\" not supported.");
    }

    /**
   * Disable "of" by throwing exception.
   */
    public void setOf(Class of) {
        throw raise("attribute \"of\" not supported.");
    }

    public Component eval() {
        final List keys = getKeys();
        final Component[] vals = getEntryComponents();
        final Component step1 = new SimpleComponent(Properties.class) {

            public Object create() {
                return new Properties();
            }
        };
        return Components.storeMap(step1, keys.toArray(), vals);
    }
}
