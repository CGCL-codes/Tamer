package auto_test.tables;

import java.sql.Connection;
import java.sql.Statement;
import jtq.helper.CloseHelper;
import org.junit.Test;
import auto_test.setup.Globals;

public class RunSqlScripts {

    public RunSqlScripts() {
    }

    @Test
    public void runScripts() throws Throwable {
        String[] sqlQueries = new String[] { "DROP TABLE main_test_table", "CREATE TABLE main_test_table ( testUUID CHAR(36), testText VARCHAR(255) NOT NULL, testInteger INTEGER NOT NULL PRIMARY KEY, testDecimal DECIMAL(18, 9), testBoolean SMALLINT NOT NULL, testTimeStamp TIMESTAMP NOT NULL)", "DROP TABLE all_fields_table", "CREATE TABLE all_fields_table (		 	string_column VARCHAR(255) not null,		 	integer_column INTEGER not null,		 	decimal_column DECIMAL(18,9) not null		)", "DROP TABLE string_table", "CREATE TABLE string_table (			teststring VARCHAR(255)		)", "DROP TABLE uuid_table", "CREATE TABLE uuid_table (			key_column CHAR(36) NOT NULL PRIMARY KEY		)", "DROP SEQUENCE auto_id_table_seq", "CREATE TABLE auto_id_table (			auto_id INTEGER generated always as identity NOT NULL PRIMARY KEY,			int_column VARCHAR(255) NOT NULL		)", "DROP TABLE account", "DROP TABLE customer", "CREATE TABLE customer (			cust_id INTEGER NOT NULL PRIMARY KEY,			cust_name VARCHAR(255) NOT NULL		)", "CREATE TABLE account (			acc_id INTEGER NOT NULL PRIMARY KEY,			acc_cust_id INTEGER NOT NULL REFERENCES customer(cust_id),			acc_value DECIMAL(18, 9) NOT NULL		)", "DROP TABLE binary_table", "CREATE TABLE binary_table (			binary_column VARCHAR(500) FOR BIT DATA NOT NULL		)" };
        Connection connection = null;
        Statement statement = null;
        try {
            connection = Globals.DATABASE.getNewConnection();
            connection.setAutoCommit(true);
            statement = connection.createStatement();
            for (String sql : sqlQueries) statement.executeUpdate(sql);
        } catch (Throwable e) {
            throw e;
        } finally {
            CloseHelper.close(statement);
        }
    }
}
