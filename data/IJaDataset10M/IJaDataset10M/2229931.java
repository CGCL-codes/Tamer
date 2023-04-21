package com.pyrphoros.erddb.db.adapters.postgresql;

import com.pyrphoros.erddb.model.data.Column;
import com.pyrphoros.erddb.model.data.Constraint;
import com.pyrphoros.erddb.model.data.DataModel;
import com.pyrphoros.erddb.model.data.ForeignKey;
import com.pyrphoros.erddb.model.data.ForeignKey.ACTION;
import com.pyrphoros.erddb.model.data.Function;
import com.pyrphoros.erddb.model.data.Index;
import com.pyrphoros.erddb.model.data.Sequence;
import com.pyrphoros.erddb.model.data.Table;
import com.pyrphoros.erddb.model.data.View;
import java.io.BufferedWriter;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Tyler
 */
public class SqlWriter {

    public static void writeAll(BufferedWriter file, DataModel m) throws Exception {
        writeDropSequences(file, m);
        writeDropFunctions(file, m);
        writeDropViews(file, m);
        writeDropTables(file, m);
        writeTables(file, m);
        writeViews(file, m);
        writeFunctions(file, m);
        writeSequences(file, m);
    }

    public static void writeTables(BufferedWriter file, DataModel m) throws Exception {
        file.write("--Create Tables");
        file.newLine();
        for (Table t : m.getTablesInHierarchichalOrder()) {
            file.write("CREATE TABLE " + t.getName() + " (");
            file.newLine();
            Constraint pk = t.getPrimaryKey();
            for (Iterator<Column> it = t.getColumns().iterator(); it.hasNext(); ) {
                Column c = it.next();
                file.write("\"" + c.getName() + "\" " + c.getDatatype() + " " + (!c.isNullable() ? "NOT NULL" : "NULL"));
                if (it.hasNext() || pk != null || t.getForeignKeys().values().size() > 0 || t.getConstraints().size() > 0) {
                    file.write(",");
                }
                file.newLine();
            }
            if (pk != null) {
                file.write("CONSTRAINT " + pk.getName() + " PRIMARY KEY (");
                for (Iterator<String> it = pk.getColumns().iterator(); it.hasNext(); ) {
                    String colName = it.next();
                    file.write(colName);
                    if (it.hasNext()) {
                        file.write(", ");
                    }
                }
                file.write(")");
                if (t.getForeignKeys().values().size() > 0 || t.getConstraints().size() > 0) {
                    file.write(",");
                }
                file.newLine();
            }
            for (Iterator<ForeignKey> it = t.getForeignKeys().values().iterator(); it.hasNext(); ) {
                ForeignKey fk = it.next();
                file.write("CONSTRAINT " + fk.getName() + " FOREIGN KEY (");
                for (Iterator<String> innerIt = fk.getColumns().iterator(); innerIt.hasNext(); ) {
                    String colName = innerIt.next();
                    file.write(colName);
                    if (innerIt.hasNext()) {
                        file.write(", ");
                    }
                }
                file.write(") REFERENCES " + fk.getOriginalTable() + "(");
                for (Iterator<String> innerIt = fk.getOriginalColumns().iterator(); innerIt.hasNext(); ) {
                    String colName = innerIt.next();
                    file.write(colName);
                    if (innerIt.hasNext()) {
                        file.write(", ");
                    }
                }
                file.write(") MATCH SIMPLE");
                file.write(" ON UPDATE ");
                writeAction(file, fk.getOnUpdate());
                file.write(" ON DELETE ");
                writeAction(file, fk.getOnDelete());
                if (it.hasNext() || t.getConstraints().size() > 0) {
                    file.write(",");
                }
                file.newLine();
            }
            for (Iterator<Constraint> it = t.getConstraints().iterator(); it.hasNext(); ) {
                Constraint c = it.next();
                switch(c.getType()) {
                    case CHECK:
                        file.write("CONSTRAINT " + c.getName() + " CHECK (" + c.getSql() + ")");
                        break;
                    case UNIQUE:
                        file.write("CONSTRAINT " + c.getName() + " UNIQUE (");
                        for (Iterator<String> innerIt = c.getColumns().iterator(); innerIt.hasNext(); ) {
                            String colName = innerIt.next();
                            file.write(colName);
                            if (innerIt.hasNext()) {
                                file.write(", ");
                            }
                        }
                        file.write(")");
                        break;
                }
                if (it.hasNext()) {
                    file.write(",");
                }
                file.newLine();
            }
            file.write(");");
            file.newLine();
            file.newLine();
            file.newLine();
            for (Index idx : t.getIndices()) {
                file.write("CREATE INDEX " + idx.getName() + " ON " + t.getName() + " USING " + idx.getType() + "(");
                for (Iterator<String> it = idx.getColumns().iterator(); it.hasNext(); ) {
                    String colName = it.next();
                    file.write(colName);
                    if (it.hasNext()) {
                        file.write(", ");
                    }
                }
                file.write(");");
            }
        }
        file.newLine();
        file.newLine();
        file.newLine();
    }

    public static void writeDropTables(BufferedWriter file, DataModel m) throws Exception {
        file.write("--Drop Tables");
        file.newLine();
        List<Table> tables = m.getTablesInHierarchichalOrder();
        for (int i = tables.size() - 1; i > -1; i--) {
            Table t = tables.get(i);
            file.write("DROP TABLE " + t.getName() + ";");
            file.newLine();
        }
        file.newLine();
        file.newLine();
        file.newLine();
    }

    public static void writeViews(BufferedWriter file, DataModel m) throws Exception {
        file.write("--Create Views");
        file.newLine();
        for (View v : m.getViews()) {
            file.write("CREATE VIEW " + v.getName() + " AS " + v.getSql() + ";");
            file.newLine();
            file.newLine();
            file.newLine();
        }
        file.newLine();
        file.newLine();
        file.newLine();
    }

    public static void writeDropViews(BufferedWriter file, DataModel m) throws Exception {
        file.write("--Drop Views");
        file.newLine();
        for (View v : m.getViews()) {
            file.write("DROP VIEW " + v.getName() + ";");
            file.newLine();
        }
        file.newLine();
        file.newLine();
        file.newLine();
    }

    public static void writeFunctions(BufferedWriter file, DataModel m) throws Exception {
        file.write("--Create Functions");
        file.newLine();
        for (View v : m.getViews()) {
            file.write("CREATE VIEW " + v.getName() + " AS " + v.getSql() + ";");
            file.newLine();
            file.newLine();
            file.newLine();
        }
        file.newLine();
        file.newLine();
        file.newLine();
    }

    public static void writeDropFunctions(BufferedWriter file, DataModel m) throws Exception {
        file.write("--Drop Functions");
        file.newLine();
        for (Function fx : m.getFunctions()) {
            file.write("DROP FUNCTION " + fx.getName() + "();");
            file.newLine();
        }
        file.newLine();
        file.newLine();
        file.newLine();
    }

    public static void writeSequences(BufferedWriter file, DataModel m) throws Exception {
        file.write("--Create Sequences");
        file.newLine();
        for (Sequence seq : m.getSequences()) {
            file.write("CREATE SEQUENCE " + seq.getName() + " INCREMENT " + seq.getInc() + " MINVALUE " + seq.getMin() + " MAXVALUE " + seq.getMax() + " START " + seq.getStart() + ";");
            file.newLine();
        }
        file.newLine();
        file.newLine();
        file.newLine();
    }

    public static void writeDropSequences(BufferedWriter file, DataModel m) throws Exception {
        file.write("--Drop Sequences");
        file.newLine();
        for (Sequence seq : m.getSequences()) {
            file.write("DROP SEQUENCE " + seq.getName() + ";");
            file.newLine();
        }
        file.newLine();
        file.newLine();
        file.newLine();
    }

    private static void writeAction(BufferedWriter file, ACTION action) throws Exception {
        switch(action) {
            case CASCADE:
                file.write("CASCADE");
                break;
            case NOACTION:
                file.write("NO ACTION");
                break;
            case RESTRICT:
                file.write("RESTRICT");
                break;
            case SETDEFAULT:
                file.write("SET DEFAULT");
                break;
            case SETNULL:
                file.write("SET NULL");
                break;
        }
    }
}
