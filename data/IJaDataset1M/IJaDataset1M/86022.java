package cn.myapps.core.fieldextends.ejb;

import java.lang.reflect.Method;
import cn.myapps.base.dao.ValueObject;
import cn.myapps.core.department.ejb.DepartmentVO;
import cn.myapps.core.user.ejb.UserVO;

/**
 * 用户扩展表影射类
 * 
 * @author think
 */
@SuppressWarnings("serial")
public class FieldExtendsVO extends ValueObject {

    public static final String TABLE_USER = "tableUser";

    public static final String TABLE_DEPT = "tableDept";

    public static final String TYPE_STRING = "string";

    public static final String TYPE_DATE = "date";

    public static final String TYPE_NUMBER = "number";

    public static final String TYPE_CLOB = "clob";

    private String fid;

    private String forTable;

    private String name;

    private String label;

    private String type;

    private Boolean isNull;

    private Boolean enabel;

    private Integer sortNumber;

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getForTable() {
        return forTable;
    }

    public void setForTable(String forTable) {
        this.forTable = forTable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getIsNull() {
        return isNull;
    }

    public void setIsNull(Boolean isNull) {
        this.isNull = isNull;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Boolean getEnabel() {
        return enabel;
    }

    public void setEnabel(Boolean enabel) {
        this.enabel = enabel;
    }

    public Integer getSortNumber() {
        return sortNumber;
    }

    public void setSortNumber(Integer sortNumber) {
        this.sortNumber = sortNumber;
    }

    /**
	 * 
	 * @param object DepartmentVO or UserVO object
	 * @return
	 */
    public String getValue(ValueObject object) {
        String fieldName = this.getName();
        fieldName = fieldName.replaceFirst("f", "F");
        String value = "";
        try {
            if (object instanceof DepartmentVO) {
                Method method = DepartmentVO.class.getMethod("get" + fieldName);
                Object result = method.invoke(object);
                if (result != null) {
                    value = result.toString();
                }
            } else if (object instanceof UserVO) {
                Method method = UserVO.class.getMethod("get" + fieldName);
                Object result = method.invoke(object);
                if (result != null) {
                    value = result.toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }
}
