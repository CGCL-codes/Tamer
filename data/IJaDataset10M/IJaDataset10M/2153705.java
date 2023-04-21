package hermes.browser.dialog;

import hermes.selector.JAMSELMessageSelectorFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * @author colincrist@hermesjms.com
 * @version $Id: SelectorImpl.java,v 1.2 2006/07/13 07:35:32 colincrist Exp $
 */
public class SelectorImpl {

    private static Map<String, SelectorImpl> impls = new HashMap<String, SelectorImpl>();

    public static SelectorImpl JAMSEL = new SelectorImpl("JamSel", JAMSELMessageSelectorFactory.class);

    private String displayName;

    private Class clazz;

    private SelectorImpl(String displayName, Class clazz) {
        super();
        this.displayName = displayName;
        this.clazz = clazz;
        impls.put(clazz.getName(), this);
    }

    public String toString() {
        return getDisplayName();
    }

    public Class getClazz() {
        return clazz;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static SelectorImpl getWithClassName(String className) {
        return impls.get(className);
    }
}
