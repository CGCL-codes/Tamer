package name.huliqing.qblog.backup.converter;

import name.huliqing.qblog.backup.TypeConverter;
import name.huliqing.qblog.enums.Group;

/**
 *
 * @author huliqing
 */
public class GroupConverter implements TypeConverter {

    public Object getAsObject(String strValue) {
        if (strValue == null || "".equals(strValue)) return null;
        Group result = null;
        try {
            result = Group.valueOf(strValue);
        } catch (Exception e) {
            return null;
        }
        return result;
    }

    public String getAsString(Object objValue) {
        if (objValue == null) return "";
        return ((Group) objValue).name();
    }
}
