package com.aimluck.eip.cayenne.om.portlet;

import org.apache.cayenne.ObjectId;
import com.aimluck.eip.cayenne.om.portlet.auto._EipTReport;

public class EipTReport extends _EipTReport {

    public static final String CREATE_DATE_COLUMN = "CREATE_DATE";

    public static final String REPORT_NAME_COLUMN = "REPORT_NAME";

    public static final String USER_ID_COLUMN = "USER_ID";

    public Integer getReportId() {
        if (getObjectId() != null && !getObjectId().isTemporary()) {
            Object obj = getObjectId().getIdSnapshot().get(REPORT_ID_PK_COLUMN);
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

    public void setReportId(String id) {
        setObjectId(new ObjectId("EipTReport", REPORT_ID_PK_COLUMN, Integer.valueOf(id)));
    }
}
