package ch.sahits.model.db;

import java.util.List;
import ch.sahits.model.IBuildable;
import ch.sahits.model.IFieldStructure;

/**
 * Interface defining the basic database table
 * @author Andi Hotz, Sahits GmbH
 * @since 2.1.0
 */
public interface IBasicDataBaseTable extends IDBTable, IDataBaseTable, IBuildable, IFieldStructure {

    /**
	 * @return  the dbProductName
	 * @uml.property  name="dbProductName"
	 */
    public String getDbProductName();

    /**
	 * Retrieve the schema.
	 * @return  database schema
	 * @uml.property  name="schema"
	 */
    public String getSchema();

    /**
	 * Retrieve the table name
	 * @return  table name
	 * @uml.property  name="tableName"
	 */
    public String getTableName();

    /**
	 * Retrieve a list of the database fields.
	 * @return   {@link IDatabaseTableField}  s
	 * @uml.property  name="fields"
	 */
    public List<IDatabaseTableField> getFields();

    /**
	 * Retrieve a list of all table indexes
	 * @return   {@link IDatabaseTableIndex}  s
	 * @uml.property  name="indexes"
	 */
    public List<IDatabaseTableIndex> getIndexes();

    /**
	 * Find the field with the name <code>fieldName</code>
	 * @param fieldName name of the field
	 * @return {@link IDatabaseTableField} or null if the field is not found
	 */
    public IDatabaseTableField findField(String fieldName);

    /**
	 * Find the index with the name <code>indexName</code>
	 * @param indexName name of the index
	 * @return {@link IDatabaseTableIndex} or null if the index is not found
	 */
    public IDatabaseTableIndex findIndex(String indexName);

    /**
	 * This methods trys to find the index defined as primary index.
	 * For legathy reasons, if no such index is found,  the index 
	 * which's name start with PK_ is searched. If no such
	 * index is found the first found unique is returned.
	 * @return Primary index or null if none is found.
	 */
    public IDatabaseTableIndex findPrimaryIndex();

    /**
	 * @return  the host
	 * @uml.property  name="host"
	 */
    public String getHost();

    /**
	 * @return  the port
	 * @uml.property  name="port"
	 */
    public String getPort();

    /**
	 * @return  the userName
	 * @uml.property  name="userName"
	 */
    public String getUserName();

    /**
	 * @return  the password
	 * @uml.property  name="password"
	 */
    public String getPassword();

    /**
	 * @return  the dbName
	 * @uml.property  name="dbName"
	 */
    public String getDbName();

    /**
	 * Retrieve a list of all indexes that represent a forgin key
	 * @return List of indexes
	 */
    public List<IDatabaseTableIndex> forginKeys();

    /**
	 * Retrieve a list of all indexes that represent a unique key
	 * @return List of indexes
	 */
    public List<IDatabaseTableIndex> uniqueKeys();

    /**
	 * Retrieve a list of all indexes that represent an index that is not a primary key nor a forgin key nor a unique index
	 * @return List of indexes
	 */
    public List<IDatabaseTableIndex> otherKeys();
}
