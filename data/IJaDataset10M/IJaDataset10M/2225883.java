package org.gvsig.fmap.drivers.gpe.utils;

import com.hardcode.gdbms.engine.values.Value;
import com.hardcode.gdbms.engine.values.ValueFactory;

/**
 * @author Jorge Piera LLodr� (jorge.piera@iver.es)
 */
public class GPETypesConversion {

    /**
	 * Convert types from java to gvSIG
	 * @param obj
	 * @return
	 */
    public static Value fromJavaTogvSIG(Object obj) {
        if (obj instanceof String) {
            return ValueFactory.createValue((String) obj);
        }
        if (obj instanceof Integer) {
            return ValueFactory.createValue(((Integer) obj).intValue());
        }
        if (obj instanceof Double) {
            return ValueFactory.createValue(((Double) obj).doubleValue());
        }
        if (obj instanceof Float) {
            return ValueFactory.createValue(((Float) obj).floatValue());
        }
        if (obj instanceof Long) {
            return ValueFactory.createValue(((Long) obj).longValue());
        }
        if (obj instanceof Boolean) {
            return ValueFactory.createValue(((Boolean) obj).booleanValue());
        }
        return ValueFactory.createValue("");
    }
}
