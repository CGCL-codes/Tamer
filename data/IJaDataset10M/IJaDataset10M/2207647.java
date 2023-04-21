package cache;

import net.jadoth.sqlengine.dbms.DbmsConnectionProvider;
import net.jadoth.sqlengine.dbms.intersyscache2009.Cache2009Dbms;
import net.jadoth.util.jdbc.ResultTable;
import sqlcsv.SqlCsv;
import test.Tbl;
import test.Test;

/**
 * @author Thomas Muenz
 *
 */
public class MainTestCache2009 {

    public static final String DB_HOST = "localhost";

    public static final int DB_PORT = 1972;

    public static final String DB_USER = "_SYSTEM";

    public static final String DB_PASSWORD = "SYS";

    public static final String DB_NAMESPACE = "USER";

    static {
        final DbmsConnectionProvider<Cache2009Dbms> con = Cache2009Dbms.singleConnection(DB_HOST, DB_PORT, DB_USER, DB_PASSWORD, DB_NAMESPACE);
        con.getDbmsAdaptor().getDdlMapper().setDictionaryCreationEnabled(false);
        Test.initializeDbConnection(con);
    }

    /**
	 * @param args
	 */
    public static void main(final String[] args) throws Exception {
        testExportImport();
    }

    static void testExportImport() {
        System.out.println("Killing table...");
        Tbl.MyTable1.db().killTable();
        System.out.println("Initilizing table...");
        Tbl.MyTable1.db().initialize();
        new ResultTable(Tbl.MyTable1.sql().SELECT().execute()).print();
        System.out.println("Exporting...");
        SqlCsv.SqlCsvExportTable(Tbl.MyTable1.toString(), Test.DIRECTORY_SQLCSV);
        System.out.println("Truncating...");
        Tbl.MyTable1.db().truncate();
        new ResultTable(Tbl.MyTable1.sql().SELECT().execute()).print();
        System.out.println("Importing...");
        SqlCsv.SqlCsvImportTableData(Test.DIRECTORY_SQLCSV + Tbl.MyTable1.toString() + ".sqlcsv");
        new ResultTable(Tbl.MyTable1.sql().SELECT().execute()).print();
        System.out.println("Done.");
    }

    static void testExportSchema() {
        SqlCsv.SqlCsvExportSchema("My%", Test.DIRECTORY_SQLCSV);
    }
}
