package org.dmd.dms.generated.dmw;

import java.util.Iterator;
import org.dmd.dmw.DmwMVIterator;
import org.dmd.dmc.types.DotName;

/**
 * The DotNameIterableDMW wraps an Iterator for a particular type and makes 
 * it Iterable.
 * <P>
 * This code was auto-generated and shouldn't be altered manually!
 * Generated from: org.dmd.dms.util.GenUtility.dumpIterable(GenUtility.java:1618)
 *    Called from: org.dmd.dms.meta.MetaGenerator.dumpTypeIterables(MetaGenerator.java:285)
 */
public class DotNameIterableDMW extends DmwMVIterator<DotName> {

    public static final DotNameIterableDMW emptyList = new DotNameIterableDMW();

    protected DotNameIterableDMW() {
        super();
    }

    public DotNameIterableDMW(Iterator<DotName> it) {
        super(it);
    }
}
