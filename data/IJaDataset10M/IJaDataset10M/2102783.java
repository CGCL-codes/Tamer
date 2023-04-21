package org.ncgr.cmtv.services;

import org.ncgr.isys.service.MapGroupingService;
import org.ncgr.isys.system.AbstractService;
import org.ncgr.isys.system.ServiceProvider;

public class IdentityMapGroupingService extends AbstractService implements MapGroupingService {

    public IdentityMapGroupingService(ServiceProvider sp) {
        super(sp, "Group identical maps only", "<html>Group <i>identical</i> maps only");
    }

    public boolean isGroupable(Object map1, Object map2) {
        return map1.equals(map2);
    }

    private static final Class[] CLASSES = new Class[] { Object.class };

    public Class[] getComparisonClasses() {
        return CLASSES;
    }
}
