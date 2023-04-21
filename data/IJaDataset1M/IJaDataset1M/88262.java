package ch.sahits.model.db;

import java.util.List;
import ch.sahits.model.IBuilder;

public interface IBasicDataTableBuilder extends IBuilder<IBasicDataBaseTable>, IDBTable2Builder {

    /**
	 * Initialize the database products name
	 * @param val product name
	 * @return this
	 */
    public IBasicDataTableBuilder dbProductName(String val);

    /**
	 * Initialize the host
	 * @param val host
	 * @return this
	 */
    public IBasicDataTableBuilder host(String val);

    /**
	 * Initialize the database name
	 * @param val database name
	 * @return this
	 */
    public IBasicDataTableBuilder dbName(String val);

    /**
	 * Initialize the database fields
	 * @param val database fields
	 * @return this
	 */
    public IBasicDataTableBuilder fields(List<DataBaseTableField> val);

    /**
	 * Add a database fields
	 * If the field list is not yet initialized it is done
	 * Any subsequent call of {@link #fields(List)} overrides the list
	 * @param val database field
	 * @return this
	 */
    public IBasicDataTableBuilder addField(DataBaseTableField val);

    /**
	 * Initialize the database indexes
	 * @param val database indexes
	 * @return this
	 */
    public IBasicDataTableBuilder indexes(List<DataBaseTableIndex> val);

    /**
	 * Add a database index
	 * If the index list is not yet initialized it is done
	 * Any subsequent call of {@link #indexes(List)} overrides the list
	 * @param val database field
	 * @return this
	 */
    public IBasicDataTableBuilder addIndex(DataBaseTableIndex val);

    /**
	 * Initialize the database password
	 * @param val database password
	 * @return this
	 */
    public IBasicDataTableBuilder password(String val);

    /**
	 * Initialize the database port
	 * @param val database port
	 * @return this
	 */
    public IBasicDataTableBuilder port(String val);

    /**
	 * Initialize the database schema
	 * @param val database schema
	 * @return this
	 */
    public IBasicDataTableBuilder schema(String val);

    /**
	 * Initialize the database table name
	 * @param val database table name
	 * @return this
	 */
    public IBasicDataTableBuilder tableName(String val);

    /**
	 * Initialize the database user name
	 * @param val database user name
	 * @return this
	 */
    public IBasicDataTableBuilder userName(String val);

    /**
	 * Find the field with the name <code>fieldName</code>
	 * @param fieldName name of the field
	 * @return {@link DataBaseTableField} or null if the field is not found
	 */
    public DataBaseTableField findField(String fieldName);

    /**
	 * Retrieve the currently set table name
	 * @return table name
	 */
    public String getTableName();
}
