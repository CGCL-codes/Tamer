package mymogo.model.helper;

import java.util.Collection;
import java.util.Vector;
import mymogo.model.Identifiable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author wusel
 */
public class IDGenerator {

    private static final Log log = LogFactory.getLog(IDGenerator.class);

    public static long generateID() {
        return System.currentTimeMillis() * (long) (Math.random() * 10000);
    }

    public static long createUniqueIDForCollection(Collection base) {
        long erg = generateID();
        for (Object identifiable : base) {
            if (identifiable instanceof Identifiable) {
                if (((Identifiable) identifiable).getId() == erg) {
                    erg = createUniqueIDForCollection(base);
                    break;
                }
            } else {
                throw new IllegalArgumentException("Non identifiable object found in collection [" + identifiable + "]");
            }
        }
        return erg;
    }
}
