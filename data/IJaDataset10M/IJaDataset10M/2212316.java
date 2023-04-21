package com.aimluck.eip.cayenne.om.portlet;

import org.apache.cayenne.ObjectId;
import com.aimluck.eip.cayenne.om.portlet.auto._EipTWorkflowRoute;

public class EipTWorkflowRoute extends _EipTWorkflowRoute {

    public Integer getRouteId() {
        if (getObjectId() != null && !getObjectId().isTemporary()) {
            Object obj = getObjectId().getIdSnapshot().get(ROUTE_ID_PK_COLUMN);
            if (obj instanceof Long) {
                Long value = (Long) obj;
                return Integer.valueOf(value.intValue());
            } else {
                return (Integer) obj;
            }
        } else {
            return null;
        }
    }

    public void setRouteId(String id) {
        setObjectId(new ObjectId("EipTWorkflowRoute", ROUTE_ID_PK_COLUMN, Integer.valueOf(id)));
    }
}
