package com.manydesigns.portofino.database;

import com.manydesigns.portofino.base.ConnectionProvider;
import com.manydesigns.portofino.base.MDConfig;
import com.manydesigns.portofino.base.MDObject;
import com.manydesigns.portofino.base.Transaction;
import com.manydesigns.portofino.util.Defs;
import com.manydesigns.portofino.util.Escape;
import com.manydesigns.portofino.util.Testing;
import com.manydesigns.portofino.util.Util;
import java.io.*;
import java.sql.*;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Paolo Predonzani - paolo.predonzani@manydesigns.com
 * @author Angelo Lupo      - angelo.lupo@manydesigns.com
 */
public class DB2DatabaseAbstraction extends CommonDatabaseAbstraction {

    public static final String copyright = "Copyright (c) 2005-2009, ManyDesigns srl";

    private static final int MAXTABLENAMELENGTHDB2 = 30;

    public static final String TABLESPACE_NAME = "tempspacedb2";

    public DB2DatabaseAbstraction(ConnectionProvider connectionProvider) throws SQLException {
        super(connectionProvider);
    }

    @Override
    public String getClassForName() {
        return "com.ibm.db2.jcc.DB2Driver";
    }

    public Connection getConnection(String host, int port, String dbName, String login, String password) throws SQLException {
        return DriverManager.getConnection("jdbc:db2://" + host + ":" + port + "/" + dbName + "", login, password);
    }

    @Override
    public String createDataBase(String escapedName) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void createRemoteDb(String host, int port, String initialDb, String name, String login, String password, Locale locale) throws Exception {
    }

    @Override
    public void setSchemaAfterLoadMDTemplate(Connection conn) throws Exception {
    }

    @Override
    public void constraintsOff(Connection conn, String schema) throws Exception {
        ResultSet rsTable = null;
        try {
            String[] tabletypes = { "TABLE" };
            rsTable = conn.getMetaData().getTables(null, schema, "%", tabletypes);
            while (rsTable.next()) {
                String tableName = rsTable.getString("TABLE_NAME");
                ResultSet rsConstraint = null;
                try {
                    rsConstraint = conn.getMetaData().getExportedKeys(null, schema, tableName);
                    while (rsConstraint.next()) {
                        String escapedConstraintName = Escape.dbSchemaEscape(rsConstraint.getString("FK_NAME"));
                        String escapedTabName = Escape.dbSchemaEscape(rsConstraint.getString("FKTABLE_NAME"));
                        PreparedStatement stDropConstr = null;
                        try {
                            String query = MessageFormat.format("ALTER TABLE \"{0}\".\"{1}\"" + " ALTER FOREIGN KEY \"{2}\" NOT ENFORCED", schema, escapedTabName, escapedConstraintName);
                            stDropConstr = conn.prepareStatement(query);
                            stDropConstr.clearParameters();
                            stDropConstr.execute();
                        } finally {
                            if (stDropConstr != null) {
                                try {
                                    stDropConstr.close();
                                } catch (Exception ignore) {
                                }
                            }
                        }
                    }
                } finally {
                    if (rsConstraint != null) {
                        try {
                            rsConstraint.close();
                        } catch (Exception ignore) {
                        }
                    }
                }
            }
        } finally {
            if (rsTable != null) {
                try {
                    rsTable.close();
                } catch (Exception ignore) {
                }
            }
        }
    }

    @Override
    public void constraintsOn(Connection conn, String schema) throws Exception {
        ResultSet rsTable = null;
        try {
            String[] tabletypes = { "TABLE" };
            rsTable = conn.getMetaData().getTables(null, schema, "%", tabletypes);
            while (rsTable.next()) {
                String tableName = rsTable.getString("TABLE_NAME");
                ResultSet rsConstraint = null;
                try {
                    rsConstraint = conn.getMetaData().getExportedKeys(null, schema, tableName);
                    while (rsConstraint.next()) {
                        String escapedConstraintName = Escape.dbSchemaEscape(rsConstraint.getString("FK_NAME"));
                        String escapedTabName = Escape.dbSchemaEscape(rsConstraint.getString("FKTABLE_NAME"));
                        PreparedStatement stDropConstr = null;
                        try {
                            String query = MessageFormat.format("ALTER TABLE \"{0}\".\"{1}\"" + " ALTER FOREIGN KEY \"{2}\" ENFORCED", schema, escapedTabName, escapedConstraintName);
                            stDropConstr = conn.prepareStatement(query);
                            stDropConstr.clearParameters();
                            stDropConstr.execute();
                        } finally {
                            if (stDropConstr != null) {
                                try {
                                    stDropConstr.close();
                                } catch (Exception ignore) {
                                }
                            }
                        }
                    }
                } finally {
                    if (rsConstraint != null) {
                        try {
                            rsConstraint.close();
                        } catch (Exception ignore) {
                        }
                    }
                }
            }
        } finally {
            if (rsTable != null) {
                try {
                    rsTable.close();
                } catch (Exception ignore) {
                }
            }
        }
    }

    @Override
    public void renameColumn(Collection<String> query, String schema, String escapedTableName, String escapedOldName, String newName, String escapedNewName, String attrType, boolean required) {
        query.add(addColumn(schema, escapedTableName, escapedNewName, attrType));
        if (required) {
            query.add(setColumnNotNull(schema, escapedTableName, escapedNewName, null));
        }
        query.add(MessageFormat.format("UPDATE \"{0}\".\"{1}\" SET \"{2}\"=\"{3}\"", schema, escapedTableName, escapedNewName, escapedOldName));
        query.add(dropColumn(schema, escapedTableName, escapedOldName));
    }

    @Override
    public String getDecimalType() {
        return "DECIMAL(20,2)";
    }

    @Override
    public String dropPk(String schema, String escapedTableName) {
        return dropConstraint(schema, escapedTableName, escapedTableName + "_pkey");
    }

    @Override
    public void changeTextAttributeLength(Collection<String> queries, String schema, String escapedTableName, String oldTableName, String escapedColumnName, int newLength, int oldLength, boolean oldRequired, Locale locale) {
        if (newLength > oldLength) {
            queries.add(MessageFormat.format("ALTER TABLE \"{0}\".\"{1}\" " + "ALTER COLUMN \"{2}\" SET DATA TYPE VARCHAR({3})", schema, escapedTableName, escapedColumnName, Integer.toString(newLength)));
        } else {
            String colBackup = "";
            for (int i = 0; i < MAXATTRIBUTENAMELENGTH; i++) {
                int rand = (int) Math.random() * 9;
                colBackup += rand;
            }
            String queryStr = addColumn(schema, escapedTableName, colBackup, "VARCHAR(" + newLength + ")");
            queries.add(queryStr);
            queryStr = MessageFormat.format("UPDATE \"{0}\".\"{1}\" SET \"{2}\"=\"{3}\"", schema, escapedTableName, colBackup, escapedColumnName);
            queries.add(queryStr);
            queryStr = dropColumn(schema, escapedTableName, escapedColumnName);
            queries.add(queryStr);
            queryStr = addColumn(schema, escapedTableName, escapedColumnName, "VARCHAR(" + newLength + ")");
            queries.add(queryStr);
            queryStr = MessageFormat.format("UPDATE \"{0}\".\"{1}\" SET \"{2}\"=\"{3}\"", schema, escapedTableName, escapedColumnName, colBackup);
            queries.add(queryStr);
            queryStr = dropColumn(schema, escapedTableName, colBackup);
            queries.add(queryStr);
            if (oldRequired) {
                queryStr = setColumnNotNull(schema, escapedTableName, escapedColumnName, null);
                queries.add(queryStr);
            }
        }
    }

    @Override
    public String getMaxTextLength(String schema, String escapedTableName, String escapedColumnName) {
        return MessageFormat.format("SELECT MAX(LENGTH(CAST(\"{0}\" AS VARCHAR({1}))))" + " AS maxlength FROM \"{2}\".\"{3}\"", escapedColumnName, Integer.toString(VARCHARDIM), schema, escapedTableName);
    }

    @Override
    public String addConstraintDeleteCascade(String schema, String escapedTableName, String escapedConstraintName, String escapedTableNameRef) {
        return MessageFormat.format("ALTER TABLE \"{0}\".\"{1}\" ADD CONSTRAINT" + " \"{2}_id\" FOREIGN KEY(\"id\") REFERENCES \"{0}\".\"{3}\"" + " (\"id\") ON DELETE CASCADE ", schema, escapedTableName, escapedConstraintName, escapedTableNameRef);
    }

    @Override
    public String renameTableName(String schema, String oldTableName, String newTableName, String escapedNewTableName) {
        return "RENAME TABLE \"" + schema + "\".\"" + oldTableName + "\"" + " TO \"" + escapedNewTableName + "\"";
    }

    @Override
    public String addConstraintOnRelation(String schema, String relationName, String escapedConstraintName, String nameForeign, String ref, String campoRef) {
        StringBuffer query = new StringBuffer("ALTER TABLE \"");
        query.append(schema);
        query.append("\".\"");
        query.append(relationName);
        query.append("\" ADD CONSTRAINT \"");
        query.append(escapedConstraintName);
        query.append("\" FOREIGN KEY (\"");
        query.append(nameForeign);
        query.append("\") REFERENCES \"");
        query.append(schema);
        query.append("\".\"");
        query.append(ref);
        query.append("\" (\"");
        query.append(campoRef);
        query.append("\")");
        query.append(" ON UPDATE NO ACTION ON DELETE NO ACTION ");
        return query.toString();
    }

    public String addContrainstUnique(String schema, String tableName, String escapedConstraintName, String nameForeign) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String addIndex(String schema, String escapedTableName, String escapedColumnName, String escapedIndexName) {
        return "CREATE INDEX \"" + schema + "\".\"" + escapedIndexName + "\"" + " ON \"" + schema + "\".\"" + escapedTableName + "\"(\"" + escapedColumnName + "\")";
    }

    @Override
    public String addUniqueIndex(String schema, String escapedTableName, String escapedColumnName, String escapedIndexName) {
        return "CREATE UNIQUE INDEX \"" + schema + "\".\"" + escapedIndexName + "\"" + " ON \"" + schema + "\".\"" + escapedTableName + "\"(\"" + escapedColumnName + "\")";
    }

    @Override
    public int getStringLength(Connection conn, String escapedAttributeName) throws Exception {
        String query = "SELECT LENGTH(CAST(? AS VARCHAR(" + VARCHARDIM + "))) AS strLength FROM SYSIBM.SYSDUMMY1";
        PreparedStatement st = null;
        ResultSet rs = null;
        int lengtAttributeName = 0;
        try {
            st = conn.prepareStatement(query);
            st.clearParameters();
            st.setString(1, escapedAttributeName);
            rs = st.executeQuery();
            if (!rs.next()) throw new Exception();
            lengtAttributeName = rs.getInt("strLength");
        } finally {
            if (rs != null) try {
                rs.close();
            } catch (Exception ignore) {
            }
            if (st != null) try {
                st.close();
            } catch (Exception ignore) {
            }
        }
        return lengtAttributeName;
    }

    @Override
    public String getDbDataEscape(String s1) {
        if (s1 == null) return null;
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < s1.length(); i++) {
            char ch = s1.charAt(i);
            if (ch == '\\') {
                buf.append("\\");
            } else if (ch == '\'') {
                buf.append("''");
            } else {
                buf.append(ch);
            }
        }
        return buf.toString();
    }

    @Override
    public String countAttributeInhe(String schema, String inhe, String attrCls) {
        return MessageFormat.format("SELECT count(attr.\"name\") AS \"totale\"" + " FROM \"{0}\".\"{1}\" inhe" + " JOIN \"{0}\".\"{2}\" attr ON attr.\"class\" = inhe.\"id\"" + " WHERE inhe.\"refid\" = ?  AND UPPER(attr.\"name\") =" + " UPPER(CAST(? AS VARCHAR(4000)))", schema, inhe, attrCls);
    }

    @Override
    public StringBuffer caseCastIntegerToString(StringBuffer sb, String escapedAttributeName) {
        sb.append("(CASE WHEN \"");
        sb.append(escapedAttributeName);
        sb.append("\" IS NULL THEN '' ELSE ");
        sb.append("TRIM(CHAR(\"");
        sb.append(escapedAttributeName);
        sb.append("\")) ||'' END)");
        return sb;
    }

    @Override
    public StringBuffer caseCastDecimalToString(StringBuffer sb, String escapedAttributeName) {
        sb.append("(CASE WHEN \"");
        sb.append(escapedAttributeName);
        sb.append("\" IS NULL THEN '' ELSE ");
        sb.append("TRIM(STRIP(CHAR(DECIMAL(\"");
        sb.append(escapedAttributeName);
        sb.append("\",19,2)),L,'0')) ||'' END)");
        return sb;
    }

    @Override
    public StringBuffer caseCastDateToString(StringBuffer sb, String escapedAttributeName, String format) throws Exception {
        sb.append("(CASE WHEN \"");
        sb.append(escapedAttributeName);
        sb.append("\" IS NULL");
        sb.append(" THEN '' ELSE ");
        String sql = formatDate(format, escapedAttributeName);
        sb.append(sql);
        sb.append(" END)");
        return sb;
    }

    private String formatDate(String format, String param) throws Exception {
        String countY = "";
        String countM = "";
        String countD = "";
        for (int i = 0; i < format.length(); i++) {
            if (format.charAt(i) == 'Y') countY = countY + "Y"; else if (format.charAt(i) == 'M') countM = countM + "M"; else if (format.charAt(i) == 'D') countD = countD + "D";
        }
        boolean charFounded = true;
        if (format.contains("/")) {
            format = format.replace("/", " ||'/'|| ");
        } else if (format.contains("-")) {
            format = format.replace("-", " ||'-'|| ");
        } else {
            charFounded = false;
        }
        format = format.replace(countY, "STRIP(DIGITS(YEAR(\"" + param + "\")),L,'0')");
        format = format.replace(countM, "SUBSTR(DIGITS(MONTH(\"" + param + "\")),LENGTH(DIGITS(MONTH(\"" + param + "\")))-1)");
        format = format.replace(countD, "SUBSTR(DIGITS(DAY(\"" + param + "\")),LENGTH(DIGITS(DAY(\"" + param + "\")))-1)");
        if (!charFounded) {
            format = format.replace(")))-1)", ")))-1)" + " ||''|| ");
            format = format.substring(0, format.length() - 8);
        }
        return format;
    }

    @Override
    public void createTableTemp(Collection<String> queries, String schema, String escapedTableNameTemp, String escapedTableName) {
        queries.add("CREATE USER TEMPORARY TABLESPACE " + TABLESPACE_NAME);
        queries.add(MessageFormat.format("DECLARE GLOBAL TEMPORARY TABLE \"{0}\"" + " LIKE \"{1}\".\"{2}\"" + " ON COMMIT PRESERVE ROWS IN {3}", escapedTableNameTemp, schema, escapedTableName, TABLESPACE_NAME));
        queries.add(MessageFormat.format("INSERT INTO session.\"{0}\"" + " (SELECT * FROM  \"{1}\".\"{2}\")", escapedTableNameTemp, schema, escapedTableName));
    }

    @Override
    public void dropSchema(Connection conn, String schema) throws Exception {
        dropAllViews(conn, schema);
        dropAllConstraints(conn, schema);
        dropAllTables(conn, schema);
        PreparedStatement st = null;
        try {
            String query = "DROP SCHEMA \"" + schema + "\" RESTRICT";
            st = conn.prepareStatement(query);
            st.clearParameters();
            st.execute();
        } finally {
            if (st != null) try {
                st.close();
            } catch (Exception ignore) {
            }
        }
    }

    @Override
    public void loadMDTemplate(Connection conn, File mdtemplate) throws Exception {
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            fis = new FileInputStream(mdtemplate);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            String line = "";
            String pp;
            while ((pp = br.readLine()) != null) {
                line += pp;
                if (!line.startsWith("CREATE DATABASE") && !line.startsWith("--") && !line.equals("")) {
                    line = line.trim();
                    if (line.endsWith(";")) {
                        PreparedStatement st = null;
                        try {
                            st = conn.prepareStatement(line.substring(0, line.length() - 1));
                            st.execute();
                        } catch (Exception e) {
                            String msg = "Error has occurred while loading the database" + mdtemplate.getPath();
                            throw new Exception(msg + ". " + e.getMessage(), e);
                        } finally {
                            if (st != null) try {
                                st.close();
                            } catch (Exception e2) {
                            }
                        }
                        line = "";
                    }
                } else line = "";
            }
        } finally {
            if (br != null) try {
                br.close();
            } catch (Exception ignore) {
            }
            if (isr != null) try {
                isr.close();
            } catch (Exception ignore) {
            }
            if (fis != null) try {
                fis.close();
            } catch (Exception ignore) {
            }
        }
    }

    public void logDML(String file, String schema, MDConfig config) throws Exception {
        OutputStreamWriter log = null;
        Transaction tx = config.getCurrentTransaction();
        try {
            String path = config.getMDCvsWorkingDirectory() + File.separatorChar + config.getMDCvsModule() + File.separatorChar;
            log = new OutputStreamWriter(new FileOutputStream(path + file), Defs.ENCODING_FILE);
            DatabaseMetaData dbmd = tx.getConnection().getMetaData();
            ResultSet rs = null;
            try {
                rs = getTables(schema, dbmd);
                while (rs.next()) {
                    log.write("DELETE FROM \"" + schema + "\".\"" + Escape.dbSchemaEscape(rs.getString("TABLE_NAME")) + "\";\n");
                }
            } finally {
                if (rs != null) {
                    try {
                        rs.close();
                        rs.getStatement().close();
                    } catch (Exception ignore) {
                    }
                }
            }
            try {
                String regExpr = "ra[0-9]+_unicol";
                Pattern pat = Pattern.compile(regExpr);
                rs = getTables(schema, dbmd);
                while (rs.next()) {
                    String tableName = rs.getString("TABLE_NAME");
                    String quotedTableName = Escape.dbSchemaEscape(tableName);
                    StringBuffer nameColum = new StringBuffer();
                    ResultSet rsCol = null;
                    String query = "";
                    String queryInsert;
                    int columnNumber = 0;
                    boolean first = true;
                    boolean addComma = false;
                    try {
                        rsCol = getColumns(schema, tableName, dbmd);
                        while (rsCol.next()) {
                            String nomeColonna = rsCol.getString("COLUMN_NAME");
                            String quotedNomeColonna = Escape.dbSchemaEscape(nomeColonna);
                            Matcher matcher = pat.matcher(nomeColonna);
                            if (matcher.matches()) {
                                continue;
                            }
                            if (first) {
                                query = "SELECT ";
                                first = false;
                            }
                            if (columnNumber > 0) {
                                query += ",";
                            }
                            query += "\"" + quotedNomeColonna + "\"";
                            columnNumber++;
                            if (addComma) {
                                nameColum.append(",");
                            }
                            nameColum.append("\"" + quotedNomeColonna + "\"");
                            addComma = true;
                        }
                    } finally {
                        if (rsCol != null) {
                            try {
                                rsCol.close();
                                rsCol.getStatement().close();
                            } catch (Exception ignore) {
                            }
                        }
                    }
                    if (first) {
                        continue;
                    }
                    query += " FROM \"" + schema + "\".\"" + Escape.dbSchemaEscape(tableName) + "\"";
                    PreparedStatement stInsert = null;
                    ResultSet rsInsert = null;
                    try {
                        stInsert = tx.getConnection().prepareStatement(query);
                        rsInsert = stInsert.executeQuery();
                        while (rsInsert.next()) {
                            queryInsert = "INSERT INTO \"" + schema + "\".\"" + quotedTableName + "\" (" + nameColum + ") VALUES(";
                            for (int j = 1; j <= columnNumber; j++) {
                                Object dbObj = rsInsert.getObject(j);
                                if (dbObj == null) {
                                    queryInsert += "NULL";
                                } else if (dbObj.getClass() == java.sql.Date.class || dbObj.getClass() == java.sql.Time.class || dbObj.getClass() == java.sql.Timestamp.class) {
                                    queryInsert += "'";
                                    queryInsert += rsInsert.getString(j).substring(0, 10);
                                    queryInsert += "'";
                                } else if (rsInsert.getMetaData().getColumnType(j) == java.sql.Types.VARCHAR) {
                                    queryInsert += "'" + getDbDataEscape(rsInsert.getString(j)) + "'";
                                } else {
                                    queryInsert += "" + getDbDataEscape(rsInsert.getString(j)) + "";
                                }
                                if (j <= (columnNumber - 1)) {
                                    queryInsert += ",";
                                }
                            }
                            queryInsert += ");\n";
                            log.write(queryInsert);
                        }
                    } finally {
                        if (rsInsert != null) {
                            try {
                                rsInsert.close();
                            } catch (Exception ignore) {
                            }
                        }
                        if (stInsert != null) {
                            try {
                                stInsert.close();
                            } catch (Exception ignore) {
                            }
                        }
                    }
                }
            } finally {
                if (rs != null) {
                    try {
                        rs.close();
                        rs.getStatement().close();
                    } catch (Exception ignore) {
                    }
                }
            }
        } finally {
            if (log != null) {
                try {
                    log.close();
                } catch (Exception ignore) {
                }
            }
        }
    }

    @Override
    public int getJavaDecimalSqlType() {
        return java.sql.Types.DECIMAL;
    }

    @Override
    public int getExpectedDeferrability() {
        return DatabaseMetaData.importedKeyNotDeferrable;
    }

    @Override
    public int getExpectedUpdateRule() {
        return DatabaseMetaData.importedKeyNoAction;
    }

    @Override
    public int getExpectedDeleteRule() {
        return DatabaseMetaData.importedKeyNoAction;
    }

    @Override
    public boolean getExpectedUnique(boolean oneToOne) {
        return oneToOne;
    }

    @Override
    public void dropTableTemp(Collection<String> queries, String schema, String escapedTableName) {
        queries.add("DROP TABLE session.\"" + escapedTableName + "\"");
        queries.add("DROP TABLESPACE " + TABLESPACE_NAME);
    }

    @Override
    public void copyDataFromtTableToOtherTable(Connection conn, String schema, String escapedFromTableName, String escapedToTableName) throws SQLException {
        String query = MessageFormat.format("INSERT INTO \"{0}\".\"{1}\" " + "SELECT * FROM session.\"{2}\"", schema, escapedToTableName, escapedFromTableName);
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement(query);
            st.clearParameters();
            st.execute();
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (Exception ignore) {
                }
            }
        }
    }

    @Override
    public void dropTempInstance(Connection conn) throws SQLException {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("DROP TABLE session.\"" + Defs.INSTANCE_CLS_TEMP + "\"");
            st.execute();
        } finally {
            if (st != null) try {
                st.close();
            } catch (Exception ignore) {
            }
        }
        conn.commit();
        st = null;
        try {
            st = conn.prepareStatement("DROP TABLESPACE " + TABLESPACE_NAME);
            st.execute();
        } finally {
            if (st != null) try {
                st.close();
            } catch (Exception ignore) {
            }
        }
    }

    @Override
    public StringBuffer dbUpper(String escapedAttribute, boolean like) {
        StringBuffer query = new StringBuffer();
        query.append("UPPER(\"");
        query.append(escapedAttribute);
        query.append("\") " + (like ? "LIKE" : "=") + " UPPER(CAST(? AS VARCHAR(4000)))");
        if (!like) return query;
        query.append(" ESCAPE '\\'");
        return query;
    }

    @Override
    public void addNotInNameAttribute(StringBuffer sb, String escapedAttributeName) {
        sb.append("'");
        sb.append(escapedAttributeName);
        sb.append(":' || TRIM(CHAR(\"id\"))");
    }

    @Override
    public void addRelAttributeInName(StringBuffer sb, String escapedAttributeName, String oppositeEscapedAttributeName) {
        sb.append("('");
        sb.append(oppositeEscapedAttributeName);
        sb.append(":' || TRIM(CHAR(\"");
        sb.append(escapedAttributeName);
        sb.append("\")) ||'')");
    }

    @Override
    public int getMaxTableNameLength() {
        return MAXTABLENAMELENGTHDB2;
    }

    @Override
    public void createOneToOneRelationship(Collection<String> queries1, Collection<String> queries2, String schema, String escapedTableName, String escapedColumnName, int id) throws Exception {
        String query = createUniqueColumn(schema, escapedTableName, escapedColumnName, id);
        queries1.add(query);
        query = createUniqueIndex(schema, escapedTableName, escapedColumnName, id);
        queries1.add(query);
        query = addIndex(schema, escapedTableName, escapedColumnName, "ra" + id + "_index");
        queries1.add(query);
    }

    private String createUniqueColumn(String schema, String escapedTableName, String escapedColumnName, int id) {
        return MessageFormat.format("ALTER TABLE \"{0}\".\"{1}\" ADD COLUMN \"ra{2}_unicol\" " + "GENERATED ALWAYS AS (CASE WHEN \"" + escapedColumnName + "\" IS NULL THEN \"id\" ELSE 0 END)", schema, escapedTableName, id);
    }

    private String createUniqueIndex(String schema, String escapedTableName, String escapedColumnName, int id) {
        return MessageFormat.format("CREATE UNIQUE INDEX \"{0}\".\"ra{1}_uniindex\"" + " ON \"{0}\".\"{2}\"(\"{3}\", \"ra{1}_unicol\")", schema, id, escapedTableName, escapedColumnName);
    }

    @Override
    public void changeRelToOneToOneRel(Collection<String> queries1, Collection<String> queries2, Collection<String> queries3, String schema, String escapedTableName, int id, String escapedNewName, String escapedOldTableName) throws Exception {
        String query = createUniqueColumn(schema, escapedTableName, escapedNewName, id);
        queries1.add(query);
        query = createUniqueIndex(schema, escapedTableName, escapedNewName, id);
        queries1.add(query);
    }

    @Override
    public void changeOneToOneRelToRel(Collection<String> queries1, Collection<String> queries2, Collection<String> queries3, String schema, String escapedOldTableName, int id, String escapedNewName, String escapedTableName) throws Exception {
        String query = dropIndex(schema, escapedOldTableName, "ra" + id + "_uniindex");
        queries2.add(query);
        query = dropUniqueColumn(schema, escapedOldTableName, id);
        queries2.add(query);
    }

    private String dropUniqueColumn(String schema, String escapedCName, int id) {
        return MessageFormat.format("ALTER TABLE \"{0}\".\"{1}\" DROP COLUMN \"ra{2}_unicol\"", schema, escapedCName, id);
    }

    @Override
    public void changeNameOneToOneRel(Collection<String> queries1, Collection<String> queries2, Collection<String> queries3, Collection<String> queries4, String schema, String escapedTableName, String escapedNewTableName, String escapedOldTableName, int id) throws Exception {
        String query = dropIndex(schema, escapedOldTableName, "ra" + id + "_index");
        queries3.add(query);
        query = addIndex(schema, escapedTableName, escapedNewTableName, "ra" + id + "_index");
        queries4.add(query);
        query = dropIndex(schema, escapedOldTableName, "ra" + id + "_uniindex");
        queries3.add(query);
        query = dropUniqueColumn(schema, escapedOldTableName, id);
        queries3.add(query);
        query = createUniqueColumn(schema, escapedTableName, escapedNewTableName, id);
        queries4.add(query);
        query = createUniqueIndex(schema, escapedTableName, escapedNewTableName, id);
        queries4.add(query);
    }

    @Override
    public void deleteEnds(Collection<String> queries1, Collection<String> queries2, String schema, String escapedTableName, int id) throws Exception {
        String query = dropIndex(schema, escapedTableName, "ra" + id + "_uniindex");
        queries2.add(query);
        query = dropUniqueColumn(schema, escapedTableName, id);
        queries2.add(query);
        query = dropIndex(schema, escapedTableName, "ra" + id + "_index");
        queries2.add(query);
    }

    @Override
    public void checkIndex(Connection conn, String schema, String fullName, DatabaseMetaData dbmd, String expectedTableName, String expectedColumnName, int expectedIdxCounter, Locale locale, Collection<String> errors, boolean isOneToOne, int attrId) {
        boolean expectedUnique = getExpectedUnique(isOneToOne);
        String expectedIdxName;
        if (expectedUnique) expectedIdxName = "ra" + attrId + "_uniindex"; else expectedIdxName = "ra" + attrId + "_index";
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            rs = getIndexInfo(schema, expectedTableName, dbmd);
            int idxCounter = 0;
            while (rs.next()) {
                String tableSchema = rs.getString(getTableSchema());
                String tableName = rs.getString("TABLE_NAME");
                String idxName = rs.getString("INDEX_NAME");
                boolean unique = !rs.getBoolean("NON_UNIQUE");
                if (!schema.equals(tableSchema)) {
                    continue;
                }
                if (!expectedTableName.equals(tableName)) {
                    continue;
                }
                if (!expectedIdxName.equals(idxName)) {
                    continue;
                }
                Testing.assertEquals(Util.getLocalizedString(Defs.MDLIBI18N, locale, "Index_uniqueness"), expectedUnique, unique, locale);
                idxCounter++;
            }
            String msg = MessageFormat.format(Util.getLocalizedString(Defs.MDLIBI18N, locale, "Wrong_number_of_indexes"), expectedIdxName, fullName);
        } catch (Exception e) {
            errors.add(e.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e2) {
                }
            }
            if (st != null) {
                try {
                    st.close();
                } catch (Exception e2) {
                }
            }
        }
    }

    @Override
    public void addOperationForCreateOneToOneRelationship(Collection<String> queries, String schema, int id, String escapedAttrName, String escapedTableName) {
        String query = createUniqueColumn(schema, escapedTableName, escapedAttrName, id);
        queries.add(query);
        query = createUniqueIndex(schema, escapedTableName, escapedAttrName, id);
        queries.add(query);
    }

    @Override
    public void addOperationForDropOneToOneRelationship(Collection<String> queries, String schema, String escapedTableName, int id) {
        String query = dropIndex(schema, escapedTableName, "ra" + id + "_uniindex");
        queries.add(query);
        query = dropUniqueColumn(schema, escapedTableName, id);
        queries.add(query);
    }

    @Override
    public void handleUniqueAndIndexForUpdate(Collection<String> vincoliDaDroppare, Collection<String> vincoliDaCreare, Collection<String> indiciDaDroppare, Collection<String> indiciDaCreare, String schema3, MDObject relAttrObj, String escapedNewName, String escapedClsName, String escapedOldClsName, boolean newOto, boolean oldOto) throws Exception {
        if (newOto && oldOto) {
            changeNameOneToOneRel(vincoliDaDroppare, vincoliDaCreare, indiciDaDroppare, indiciDaCreare, schema3, escapedClsName, escapedNewName, escapedOldClsName, relAttrObj.getId());
        } else {
            String query = dropIndex(schema3, escapedOldClsName, "ra" + relAttrObj.getId() + "_index");
            indiciDaDroppare.add(query);
            query = addIndex(schema3, escapedClsName, escapedNewName, "ra" + relAttrObj.getId() + "_index");
            indiciDaCreare.add(query);
        }
    }

    @Override
    public void dropConstraintForClassNameChange(String schema, int raId, Collection<String> vincoliDaDroppare, Collection<String> indiciDaDroppare, String escapedOwnerCls, boolean oto) {
        String query = dropConstraint(schema, escapedOwnerCls, "ra" + raId + "_ref");
        vincoliDaDroppare.add(query);
        if (oto) {
            addOperationForDropOneToOneRelationship(indiciDaDroppare, schema, escapedOwnerCls, raId);
        }
    }

    @Override
    public void createConstraintForClassNameChange(String escapedOwnerCls, String schema, int raId, String escapedAttrName, Collection<String> vincoliDaCreare, Collection<String> indiciDaCreare, String escapedOppositeCls, boolean oto) {
        String query = addConstraintOnRelation(schema, escapedOwnerCls, "ra" + raId + "_ref", escapedAttrName, escapedOppositeCls, "id");
        vincoliDaCreare.add(query);
        if (oto) {
            addOperationForCreateOneToOneRelationship(indiciDaCreare, schema, raId, escapedAttrName, escapedOwnerCls);
        }
    }
}
