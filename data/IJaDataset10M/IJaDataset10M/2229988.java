package org.objectstyle.cayenne.unit.jira;

import org.objectstyle.cayenne.testdo.inherit.Manager;

/**
 * @author Andrei Adamchik
 */
public class CAY_207Manager1 extends Manager {

    public void setClientContactType(CAY_207String1 clientContactType) {
        writeProperty("clientContactType", clientContactType);
    }

    public CAY_207String1 getClientContactType() {
        return (CAY_207String1) readProperty("clientContactType");
    }
}
