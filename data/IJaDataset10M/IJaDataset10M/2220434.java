package com.byterefinery.rmbench.external;

import com.byterefinery.rmbench.external.model.IColumn;
import com.byterefinery.rmbench.external.model.IModel;
import com.byterefinery.rmbench.external.model.IPrimaryKey;
import com.byterefinery.rmbench.external.model.ISchema;
import com.byterefinery.rmbench.external.model.ITable;

/**
 * Specification of a generator for database object names. This interface may be 
 * implemented by external parties that use the nameGenerator extension point
 * 
 * @author cse
 */
public interface INameGenerator {

    /**
     * generate a name for a table
     * @param model the owning model
     * @param schema the owning schema
     * @return the new name
     */
    public String generateTableName(IModel model, ISchema schema);

    /**
     * generate a foreign key constraint name
     * @param model the owning model
     * @param table the owning table
     * @return the new constraint name
     */
    public String generateForeignKeyName(IModel model, ITable table);

    /**
     * generate a primary key constraint name
     * @param model the owning model
     * @param table the owning table
     * @return the new constraint name
     */
    public String generatePrimaryKeyName(IModel model, ITable table);

    /**
     * generate a check constraint name
     * @param model the owning model
     * @param table the owning table
     * @return the new constraint name
     */
    public String generateCheckConstraintName(IModel model, ITable table);

    /**
     * generate a unique key constraint name
     * @param model the owning model
     * @param table the owning table
     * @return the new constraint name
     */
    public String generateUniqueConstraintName(IModel model, ITable table);

    /**
     * generate a column name
     * @param model the owning model
     * @param table the owning table
     * @return the new column name
     */
    public String generateColumnName(IModel model, ITable table);

    /**
     * generate a name for a column generated as part of a foreign key
     * @param model the owning model
     * @param sourceTable the owning table
     * @param targetKey the target key
     * @param column the referenced target key column
     * @return the new name
     */
    public String generateForeignKeyColumnName(IModel model, ITable sourceTable, IPrimaryKey targetKey, IColumn column);

    /**
     * generate a valid schema name based on a name previously rejected by a validate 
     * method of the database info
     * 
     * @param model the owning model
     * @param previousName the previous name which was rejected
     * @return the new schema name
     */
    public String generateNewSchemaName(IModel model, String previousName);

    /**
     * generate a valid table name based on a name previously rejected by a validate 
     * method of the database info
     * 
     * @param model the owning model
     * @param schema the owning schema
     * @param previousName the previous name which was rejected
     * @return the new table name
     */
    public String generateNewTableName(IModel model, ISchema schema, String previousName);

    /**
     * generate a valid column name based on a name previously rejected by a validate 
     * method of the database info
     * 
     * @param model the owning model
     * @param table the owning table
     * @param previousName the previous name which was rejected
     * @return the new column name
     */
    public String generateNewColumnName(IModel model, ITable table, String previousName);
}
