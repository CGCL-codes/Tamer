package net.nothinginteresting.datamappers.base;

import java.util.ArrayList;
import java.util.List;
import net.nothinginteresting.datamappers.base.sqlelements.FieldMeta;
import net.nothinginteresting.datamappers.base.sqlelements.LeftJoin;

/**
 * @author Dmitri Gorbenko
 * 
 */
public abstract class Table {

    public abstract List<FieldMeta> getFields();

    public abstract Table getParentTable() throws DatamappersException;

    public abstract boolean hasParent();

    private final String schemaName;

    private final String tableName;

    public Table(String schemaName, String tableName) {
        this.schemaName = schemaName;
        this.tableName = tableName;
    }

    public String getFullName() {
        return schemaName + "." + tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public String getSchemaName() {
        return schemaName;
    }

    /**
	 * @return
	 * @throws DatamappersException
	 */
    public List<LeftJoin> getParentJoins() throws DatamappersException {
        List<LeftJoin> result = new ArrayList<LeftJoin>();
        Table current = this;
        while (current.hasParent()) {
            result.add(createJoin(current));
            current = current.getParentTable();
        }
        return result;
    }

    private LeftJoin createJoin(Table current) throws DatamappersException {
        return null;
    }

    /**
	 * @param table
	 * @return
	 * @throws DatamappersException
	 */
    public FieldMeta findPrimaryField() throws DatamappersException {
        for (FieldMeta field : getFields()) {
            if (field.isPrimary()) return field;
        }
        throw new DatamappersException("Can not find primary field in table " + getFullName());
    }

    /**
	 * @return
	 * @throws DatamappersException
	 */
    public List<FieldMeta> getParentFields() throws DatamappersException {
        List<FieldMeta> result = new ArrayList<FieldMeta>();
        if (hasParent()) {
            result.addAll(getParentTable().getFields());
            result.addAll(getParentTable().getParentFields());
        }
        return result;
    }

    /**
	 * @return
	 * @throws DatamappersException
	 */
    public List<FieldMeta> getAllFields() throws DatamappersException {
        List<FieldMeta> result = new ArrayList<FieldMeta>();
        result.addAll(getFields());
        result.addAll(getParentFields());
        return result;
    }

    @Override
    public String toString() {
        return "Table[schemaName=" + schemaName + ",tableName=" + tableName + "]";
    }
}
