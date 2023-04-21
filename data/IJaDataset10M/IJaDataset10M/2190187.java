package modelet.entity;

import java.util.List;

public interface Entity {

    /**
   * Modelet supposes each entity must at lease have 'id' field as primary key or unique key to identify a record.
   * 
   * Be default, Modelet think the sequence number of id is generated within database, Modelet will retrieve the given it and 
   * set it back to entity after successfully insert; but if you prefer to generate id by your framework self, please just implements
   * SysemIncrementEntity, then Model won't try to retrieve id from database.
   * 
   * If your entity does not need this id field, please add it to getExclusiveFields() fields.
   */
    public Object getId();

    public void setId(Object id);

    /**
	 * Modelet converts your entity content into INSERT, UPDATE, DELETE sql statement depending on this fields setting.
	 * 
	 * Once successful insert, Modelet will convert entity's txnModel into UPDATE state. If an entity is retrieved from database, its txnMode
	 * will also be set to UPDATE automatically.
	 * 
	 * Just new created Entity should be at INSERT state.
	 * 
	 * If you want to delete a entity, please set txnMode to DELETE.
	 */
    public TxnMode getTxnMode();

    public void setTxnMode(TxnMode txnMode);

    /**
	 * Modelet maps your entity to database table by this method return. If your table has schema prefix, please return like this: schemaName.TableName
	 * @return a database table name.
	 */
    public String getTableName();

    /**
	 * This method is used in UPDATE and DELETE action. Modelet generates "where" criteria by parsing your key definition.
	 * If your entity extends modelet.entity.AbstractEntity, the "id" field is default used as key field.
	 * @return
	 */
    public List<String> getKeyNames();

    /**
	 * Sometimes you will have more complicated SQL, like 'join' statement, and convert its results into entity. You should exclude these extra joined
	 * fields if you want to persist your entity back to database again. This method is used to tell Modelet which fields not to be included into SQL statements.
	 */
    public List<String> getExclusiveFields();

    /**
	 * Before this entity is converted to SQL statement, this method will be called.
	 */
    public void beforeSave();

    /**
	 * After this entity is update to database, this method will be called. 
	 */
    public void afterSave();

    /**
	 * Tell framework to append field with null value to sql statement
	 */
    public boolean isAllowNullValue();
}
