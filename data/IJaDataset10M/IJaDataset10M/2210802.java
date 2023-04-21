package com.aimluck.eip.cayenne.om.portlet;

import org.apache.cayenne.ObjectId;
import com.aimluck.eip.cayenne.om.portlet.auto._EipMAddressbookCompany;

public class EipMAddressbookCompany extends _EipMAddressbookCompany {

    public static final String COMPANY_NAME_COLUMN = "COMPANY_NAME";

    public Integer getCompanyId() {
        if (getObjectId() != null && !getObjectId().isTemporary()) {
            Object obj = getObjectId().getIdSnapshot().get(COMPANY_ID_PK_COLUMN);
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

    public void setCompanyId(String id) {
        setObjectId(new ObjectId("EipMAddressbookCompany", COMPANY_ID_PK_COLUMN, Integer.valueOf(id)));
    }
}
