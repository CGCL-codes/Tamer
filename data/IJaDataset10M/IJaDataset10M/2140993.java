package org.dctmvfs.vfs.provider.dctm.client.attrs;

import java.util.ArrayList;
import java.util.List;
import com.documentum.fc.client.IDfTypedObject;

public class AttrUtil {

    public static Object getAttr(AttrSpec attr, IDfTypedObject object) throws Exception {
        Object result = null;
        if (!object.hasAttr(attr.getName())) {
            return result;
        }
        if (attr.getType().equals(AttrSpec.BOOLEAN)) {
            result = new Boolean(object.getBoolean(attr.getName()));
        } else if (attr.getType().equals(AttrSpec.DOUBLE)) {
            result = new Double(object.getDouble(attr.getName()));
        } else if (attr.getType().equals(AttrSpec.ID)) {
            result = object.getId(attr.getName());
        } else if (attr.getType().equals(AttrSpec.INTEGER)) {
            result = new Integer(object.getInt(attr.getName()));
        } else if (attr.getType().equals(AttrSpec.STRING)) {
            result = object.getString(attr.getName());
        } else if (attr.getType().equals(AttrSpec.TIME)) {
            result = object.getTime(attr.getName());
        } else {
            result = object.getString(attr.getName());
        }
        return result;
    }

    public static List getRepeatingAttr(AttrSpec attr, IDfTypedObject object) throws Exception {
        List results = null;
        if (!object.hasAttr(attr.getName())) {
            return new ArrayList();
        }
        int attrCount = object.getValueCount(attr.getName());
        results = new ArrayList(attrCount);
        for (int i = 0; i < attrCount; i++) {
            Object result;
            if (attr.getType().equals(AttrSpec.BOOLEAN)) {
                result = new Boolean(object.getRepeatingBoolean(attr.getName(), i));
            } else if (attr.getType().equals(AttrSpec.DOUBLE)) {
                result = new Double(object.getRepeatingDouble(attr.getName(), i));
            } else if (attr.getType().equals(AttrSpec.ID)) {
                result = object.getRepeatingId(attr.getName(), i);
            } else if (attr.getType().equals(AttrSpec.INTEGER)) {
                result = new Integer(object.getRepeatingInt(attr.getName(), i));
            } else if (attr.getType().equals(AttrSpec.STRING)) {
                result = object.getRepeatingString(attr.getName(), i);
            } else if (attr.getType().equals(AttrSpec.TIME)) {
                result = object.getTime(attr.getName());
            } else {
                result = object.getString(attr.getName());
            }
            results.add(result);
        }
        return results;
    }
}
