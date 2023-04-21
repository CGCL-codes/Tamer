package org.zamia.instgraph;

import org.zamia.SourceLocation;
import org.zamia.zdb.ZDB;

/**
 * 
 * @author Guenter Bartsch
 *
 */
@SuppressWarnings("serial")
public class IGAttribute extends IGContainerItem {

    public IGAttribute(IGType aType, String aId, SourceLocation aSrc, ZDB aZDB) {
        super(aId, aSrc, aZDB);
    }

    @Override
    public int getNumChildren() {
        return 0;
    }

    @Override
    public IGItem getChild(int aIdx) {
        return null;
    }
}
