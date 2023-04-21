package dbobjects;

import dbreflection.tablemanagement.DOTable;
import dbreflection.tablemanagement.DOField;
import dbreflection.tablemanagement.DatabaseObject;

/**
 * Created by IntelliJ IDEA.
 * User: [J0k3r]
 * Date: 26.11.2008
 * Time: 14:51:08
 */
@DOTable(table = "db_object_param_value_string", pk = "id")
public class DBObjectParamValueString extends DatabaseObject {

    @DOField(column = "id")
    private Integer id;

    @DOField(column = "value")
    private String value;

    @DOField(column = "db_object_param_id")
    private Integer paramId;

    @DOField(column = "db_object_id")
    private Integer objectId;

    public DBObjectParamValueString() {
        this.id = UNASSIGNEDINT;
        this.value = UNASSIGNEDSTRING;
        this.paramId = UNASSIGNEDINT;
        this.objectId = UNASSIGNEDINT;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getParamId() {
        return paramId;
    }

    public void setParamId(Integer paramId) {
        this.paramId = paramId;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }
}
