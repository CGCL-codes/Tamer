package org.zamia.instgraph;

import org.zamia.SourceLocation;
import org.zamia.zdb.ZDB;

/**
 * 
 * @author Guenter Bartsch
 *
 */
@SuppressWarnings("serial")
public abstract class IGConcurrentStatement extends IGItem {

    private String fLabel;

    public IGConcurrentStatement(String aLabel, SourceLocation aLocation, ZDB aZDB) {
        super(aLocation, aZDB);
        fLabel = aLabel;
    }

    public String getLabel() {
        return fLabel;
    }

    public abstract IGItem findChild(String aLabel);
}
