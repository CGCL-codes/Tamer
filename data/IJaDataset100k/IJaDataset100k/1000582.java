package org.personalsmartspace.ex.dpi_data_gen.impl;

import java.util.Hashtable;
import org.personalsmartspace.cm.model.api.pss3p.CtxAttributeTypes;

/**
 * @author Elizabeth
 *
 */
public final class DefaultPSS {

    public static final String NAME = "John Doe";

    public static final SymLocValues SYMBOLIC_LOCATION = SymLocValues.HOME;

    public static final StatusValues STATUS = StatusValues.FREE;

    public static final ActivityValues ACTIVITY = ActivityValues.COOKING;

    public static final Hashtable<String, String> getHashtable() {
        Hashtable<String, String> table = new Hashtable<String, String>();
        table.put(CtxAttributeTypes.SYMBOLIC_LOCATION, SYMBOLIC_LOCATION.toString());
        table.put(CtxAttributeTypes.ACTIVITY, ACTIVITY.toString());
        table.put(CtxAttributeTypes.STATUS, STATUS.toString());
        table.put(CtxAttributeTypes.NAME, NAME);
        return table;
    }
}
