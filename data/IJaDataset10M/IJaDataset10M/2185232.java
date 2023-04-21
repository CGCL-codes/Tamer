package org.modss.facilitator.ui.result;

import java.awt.*;
import java.util.*;
import org.modss.facilitator.shared.resource.*;

/**
 * Methods which should be in resources but aren't.
 *
 * @author John Farrell
 * @version $Revision: 1.1 $
 */
public class ResourceUtils {

    private static Map created = new HashMap();

    /** You don't need one of these. */
    private ResourceUtils() {
    }

    static synchronized Font getFont(ResourceProvider resources, String key) {
        if (!created.containsKey(key)) {
            String name = resources.getProperty(key + ".font.name", "sansserif");
            int size = resources.getIntProperty(key + ".font.size", 12);
            created.put(key, new Font(name, Font.PLAIN, size));
        }
        return (Font) created.get(key);
    }
}
