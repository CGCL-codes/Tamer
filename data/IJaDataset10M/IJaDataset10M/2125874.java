package org.primordion.xholon.service.db;

import java.util.List;
import java.util.Map;

/**
 * Provides information about the database as a whole,
 * including table names, column names, and other information
 * available through java.sql.DatabaseMetaData .
 * @author <a href="mailto:ken@primordion.com">Ken Webb</a>
 * @see <a href="http://www.primordion.com/Xholon">Xholon Project website</a>
 * @since 0.8.1 (Created on October 25, 2009)
 */
public interface IXholonDatabaseMetaData extends IXholonDatabase {

    /**
	 * Get a list of user (non-system) table names from the database.
	 * @return A list of capitalized table names, or null.
	 */
    public abstract List getTableNames();

    /**
	 * Show a list of user (non-system) table names from the database.
	 */
    public abstract void showTableNames();

    /**
	 * Get a list of column names for a database table.
	 * @param tableName The name of a user table in the database.
	 * @return A list of column names, or null.
	 */
    public abstract List getColumnNames(String tableName);

    /**
	 * Show a list of column names for a database table.
	 * @param tableName The name of a user table in the database.
	 */
    public abstract void showColumnNames(String tableName);

    /**
	 * Get a list of primary keys for a database table.
	 * @param tableName The name of a user table in the database.
	 * @return A list of primary keys.
	 */
    public abstract List getPrimaryKeys(String tableName);

    /**
	 * Show a list of primary keys for a database table.
	 * @param tableName The name of a user table in the database.
	 */
    public abstract void showPrimaryKeys(String tableName);

    /**
	 * Get a list of foreign keys for a database table.
	 * @param tableName The name of a user table in the database.
	 * @return
	 */
    public Map getForeignKeys(String tableName);

    /**
	 * Get the Java source code for a bean that represents a database table.
	 * @param tableName The name of the database table.
	 * @param appName The name of the application that this class is a part of.
	 * @return The complete source code for a Java class.
	 */
    public abstract String getJavaSourceCode(String tableName, String appName);

    /**
	 * Create a mapping between Java objects in this application and a relational database or other datastore.
	 * This will be in the form of a Hibernate .hbm.xml file.
	 * @param tableName 
	 * @param appName 
	 */
    public abstract void createHibernateMapping(String tableName, String appName);

    /**
	 * Transform a SQL table name into a Java class name.
	 * <p>ex: "Buildings" --> "Building"</p>
	 * <p>ex: "Buses" --> "Bus"</p>
	 * <p>[ex: "Faculty" --> "FacultyMember" (not yet implemented; should be "FacultyMembers")]</p>
	 * @param sqlTableName The name of a database table.
	 * @return A transformed Java class name.
	 */
    public abstract String sqlTableName2JavaClassName(String sqlTableName);

    /**
	 * Transform a SQL column name into a Java attribute name.
	 * ex: "number_of_floors" --> "numberOfFloors"
	 * @param sqlColumnName The name of a column in a database table.
	 * @return A transformed Java attribute name.
	 */
    public abstract String sqlColumnName2JavaAttributeName(String sqlColumnName);

    /**
	 * Transform a list of SQL column names into a list of Java attribute names.
	 * @param sqlColumnNames The names of columns in a database table.
	 * @return A list of transformed Java attribute names.
	 */
    public abstract List sqlColumnName2JavaAttributeName(List sqlColumnNames);

    /**
	 * Transform a SQL data type into a Java data type.
	 * ex: java.sql.Types.INTEGER --> "int"
	 * @param sqlDataType A SQL data type.
	 * @return A Java data type.
	 */
    public abstract String sqlDataType2JavaDataType(String sqlDataTypeStr);

    /**
	 * Capitalize a String.
	 * The first letter of the String is set to upper case.
	 * ex: mydatabasetable --> Mydatabasetable
	 * @param str A String
	 * @return A capitalized version of the input String.
	 */
    public abstract String capitalize(String str);
}
