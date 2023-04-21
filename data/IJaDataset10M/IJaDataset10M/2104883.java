package net.sf.rcpforms.experimenting.java.base;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Hashtable;
import org.apache.log4j.Logger;
import org.eclipse.osgi.util.NLS;

/**
 * 
 * @deprecated nicht in Benutzung. Erlaubt, initialisiert NLS Strings auch 
 *             via dynamischen Key zu lesen....
 */
@Deprecated
public class NLS2 extends NLS {

    private static Hashtable<String, String> s_map = null;

    private static Class<?> s_class = null;

    @SuppressWarnings("all")
    private static final Logger log = Logger.getLogger(NLS2.class);

    public static void initializeMessages(final String bundleName, final Class clazz) {
        NLS.initializeMessages(bundleName, clazz);
        s_class = clazz;
        initMap();
    }

    protected static String proGetLabel(final Class<?> clazz, final String key) {
        if (s_map == null) {
            s_class = clazz;
            initMap();
        }
        return s_map.get(key);
    }

    private static void initMap() {
        s_map = new Hashtable<String, String>();
        final Field[] fields = s_class.getFields();
        for (final Field field : fields) {
            final int modifiers = field.getModifiers();
            if (Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers) && field.getType() == String.class) {
                try {
                    if (!field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    final String value = (String) field.get(null);
                    s_map.put(field.getName(), value);
                } catch (final Throwable e) {
                    log.warn("failed to read NLS entry '" + field.getName() + "' : " + e.getMessage(), e);
                }
            }
        }
    }
}
