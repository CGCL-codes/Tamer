package dbobjects;

import dbreflection.tablemanagement.DOTable;
import dbreflection.tablemanagement.DatabaseObject;
import dbreflection.tablemanagement.DOField;
import java.sql.Timestamp;

/**
 * User: [J0k3r]
 * Date: 02.02.2009
 * Time: 11:46:14
 */
@DOTable(table = "db_object_param_value_date", pk = "id")
public class DBObjectParamValueDate extends DatabaseObject {

    @DOField(column = "id")
    private Integer id;

    @DOField(column = "value")
    private Timestamp value;

    @DOField(column = "db_object_param_id")
    private Integer paramId;

    @DOField(column = "db_object_id")
    private Integer objectId;

    public DBObjectParamValueDate() {
        this.id = UNASSIGNEDINT;
        this.value = UNASSIGNEDTIMESTAMP;
        this.paramId = UNASSIGNEDINT;
        this.objectId = UNASSIGNEDINT;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Timestamp getValue() {
        return value;
    }

    public void setValue(Timestamp value) {
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
