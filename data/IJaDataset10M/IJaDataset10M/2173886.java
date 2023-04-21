package org.atlantal.utils;

import java.util.Iterator;
import java.util.Map;
import gnu.trove.THashMap;

/**
 * @author f.masurel
 */
public class AtlantalMap extends THashMap {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    public AtlantalMap() {
        super();
    }

    /**
     * @param map map
     */
    public AtlantalMap(Map map) {
        super(map);
    }

    /**
     * @return string
     */
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("AtlantalMap : ");
        try {
            Iterator it = this.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                str.append(entry.getKey()).append(":").append(entry.getValue());
                if (it.hasNext()) {
                    str.append(", ");
                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return str.toString();
    }
}
