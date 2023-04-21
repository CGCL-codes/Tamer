package net.sf.dewdrop.sql.dialect;

import net.sf.dewdrop.sqlml.SqlDataType;
import net.sf.dewdrop.util.StringUtils;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * This class maps a type to names. Associations may be marked with a capacity.
 * Calling the get() method with a type and actual size n will return the
 * associated name with smallest capacity >= n, if available and an unmarked
 * default type otherwise. Eg, setting
 * <pre>
 * 	names.put(type,        "TEXT" );
 * 	names.put(type,   255, "VARCHAR($l)" );
 * 	names.put(type, 65534, "LONGVARCHAR($l)" );
 * </pre>
 * will give you back the following:
 * <pre>
 *  names.get(type)         // --> "TEXT" (default)
 *  names.get(type,    100) // --> "VARCHAR(100)" (100 is in [0:255])
 *  names.get(type,   1000) // --> "LONGVARCHAR(1000)" (1000 is in [256:65534])
 *  names.get(type, 100000) // --> "TEXT" (default)
 * </pre>
 * On the other hand, simply putting
 * <pre>
 * 	names.put(type, "VARCHAR($l)" );
 * </pre>
 * would result in
 * <pre>
 *  names.get(type)        // --> "VARCHAR($l)" (will cause trouble)
 *  names.get(type, 100)   // --> "VARCHAR(100)"
 *  names.get(type, 10000) // --> "VARCHAR(10000)"
 * </pre>
 *
 * @author Christoph Beck
 * @author Les Hazlewood
 * @since 6 January 2005
 */
public class TypeMap {

    private static final String LENGTH_TOKEN = "$l";

    private static final String PRECISION_TOKEN = "$p";

    private HashMap weighted = new HashMap();

    private HashMap defaults = new HashMap();

    /**
     * Retrieve the default type string for the specified {@link SqlDataType}.
     *
     * @param type the type key
     *
     * @return the default type string associated with the specified key, or
     *         <code>null</code> if there is no default Dialect type string for
     *         the specified key.
     */
    public String get(SqlDataType type) {
        return (String) defaults.get(type);
    }

    /**
     * get type name for specified type and size
     *
     * @param type the type key
     * @param size the (maximum) type size/length
     *
     * @return the associated name with smallest capacity >= size, if available
     *         and the default type string otherwise ( or <code>null</code> if
     *         there is no default type string ).
     */
    public String get(SqlDataType type, int size, int precision) {
        Map map = (Map) weighted.get(type);
        if (map != null && !map.isEmpty()) {
            Iterator entries = map.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry entry = (Map.Entry) entries.next();
                if (size <= ((Integer) entry.getKey()).intValue()) {
                    return replace((String) entry.getValue(), size, precision);
                }
            }
        }
        String typeString = get(type);
        if (typeString != null) {
            return replace(get(type), size, precision);
        } else {
            return null;
        }
    }

    private static String replace(String type, int length, int precision) {
        String l = Integer.toString(length);
        String p = Integer.toString(precision);
        type = StringUtils.replaceFirst(type, LENGTH_TOKEN, l);
        return StringUtils.replaceFirst(type, PRECISION_TOKEN, p);
    }

    /**
     * set a type name for specified type key and capacity
     *
     * @param type the type key
     */
    public void put(SqlDataType type, int capacity, String value) {
        TreeMap map = (TreeMap) weighted.get(type);
        if (map == null) {
            map = new TreeMap();
            weighted.put(type, map);
        }
        map.put(new Integer(capacity), value);
    }

    /**
     * set a default type name for specified type key
     *
     * @param type the type key
     */
    public void put(SqlDataType type, String value) {
        defaults.put(type, value);
    }
}
