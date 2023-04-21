package org.riverock.generic.system;

import java.io.FileInputStream;
import java.sql.SQLException;
import org.riverock.generic.db.DatabaseAdapter;
import org.riverock.generic.db.DatabaseManager;
import org.riverock.generic.db.DatabaseStructureManager;
import org.riverock.generic.schema.db.structure.DbSchemaType;
import org.riverock.generic.schema.db.structure.DbSequenceType;
import org.riverock.generic.schema.db.structure.DbTableType;
import org.riverock.generic.schema.db.structure.DbViewType;
import org.riverock.generic.startup.StartupApplication;
import org.exolab.castor.xml.Unmarshaller;
import org.xml.sax.InputSource;

/**
 * Author: mill
 * Date: Nov 28, 2002
 * Time: 3:10:19 PM
 *
 * $Id: DbStructureCreateTable.java,v 1.3 2006/06/05 19:18:34 serg_main Exp $
 *
 * import data from XML file to DB
 */
public class DbStructureCreateTable {

    public static void main(String args[]) throws Exception {
        if (args.length < 3) {
            System.out.println("Command line format: <DB_ALIAS> <IMPORT_FILE> <TABLE_NAME>");
            return;
        }
        StartupApplication.init();
        final String dbAlias = args[0];
        final String fileName = args[1];
        final String tableName = args[2];
        DbStructureCreateTable.importStructure(fileName, true, dbAlias, tableName);
    }

    public static void importStructure(String fileName, boolean isData, String dbAlias, String tableName) throws Exception {
        DatabaseAdapter db_ = null;
        try {
            db_ = DatabaseAdapter.getInstance(dbAlias);
            System.out.println("db connect - " + db_.getClass().getName());
            int i = 0;
            System.out.println("Unmarshal data from file " + fileName);
            InputSource inSrc = new InputSource(new FileInputStream(fileName));
            DbSchemaType millSchema = (DbSchemaType) Unmarshaller.unmarshal(DbSchemaType.class, inSrc);
            for (i = 0; i < millSchema.getTablesCount(); i++) {
                DbTableType table = millSchema.getTables(i);
                if (!tableName.equalsIgnoreCase(table.getName())) {
                    System.out.println("skip table: " + table.getName());
                    continue;
                }
                try {
                    System.out.println("create table " + table.getName());
                    db_.createTable(table);
                } catch (SQLException e) {
                    if (db_.testExceptionTableExists(e)) {
                        System.out.println("table " + table.getName() + " already exists");
                        System.out.println("drop table " + table.getName());
                        db_.dropTable(table);
                        System.out.println("create table " + table.getName());
                        db_.createTable(table);
                    } else throw e;
                }
                if (isData) DatabaseStructureManager.setDataTable(db_, table);
                break;
            }
        } finally {
            db_.commit();
            DatabaseAdapter.close(db_);
            db_ = null;
        }
    }
}
