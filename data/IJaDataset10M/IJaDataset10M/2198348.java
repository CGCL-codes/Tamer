package org.dctmvfs.vfs.provider.dctm.client.attrs.impl;

import com.documentum.fc.client.IDfSysObject;
import com.documentum.fc.common.DfException;
import com.documentum.fc.common.DfTime;

public class TypedAttrSetter extends DefaultAttrSetter {

    protected void setSingleAttribute(IDfSysObject sysObject, String name, int type, Object value) throws DfException {
        if (value instanceof String) {
            sysObject.setString(name, (String) value);
        } else if (value instanceof Boolean) {
            sysObject.setBoolean(name, ((Boolean) value).booleanValue());
        } else if (value instanceof Integer) {
            sysObject.setInt(name, ((Integer) value).intValue());
        } else if (value instanceof java.util.Date) {
            DfTime dfTime = new DfTime((java.util.Date) value);
            sysObject.setTime(name, dfTime);
        } else {
            sysObject.setString(name, value.toString());
        }
    }
}
