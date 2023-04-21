package com.liferay.portal.tools.sql;

import com.liferay.portal.kernel.util.StringMaker;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.spring.hibernate.HibernateUtil;
import com.liferay.portal.velocity.VelocityUtil;
import com.liferay.util.FileUtil;
import com.liferay.util.SimpleCounter;
import com.liferay.util.dao.DataAccess;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.dialect.DB2Dialect;
import org.hibernate.dialect.DerbyDialect;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.FirebirdDialect;
import org.hibernate.dialect.HSQLDialect;
import org.hibernate.dialect.InformixDialect;
import org.hibernate.dialect.InterbaseDialect;
import org.hibernate.dialect.JDataStoreDialect;
import org.hibernate.dialect.MySQLDialect;
import org.hibernate.dialect.Oracle10gDialect;
import org.hibernate.dialect.Oracle8iDialect;
import org.hibernate.dialect.Oracle9Dialect;
import org.hibernate.dialect.Oracle9iDialect;
import org.hibernate.dialect.OracleDialect;
import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.dialect.SAPDBDialect;
import org.hibernate.dialect.SQLServerDialect;
import org.hibernate.dialect.SybaseDialect;

/**
 * <a href="DBUtil.java.html"><b><i>View Source</i></b></a>
 *
 * @author Alexander Chow
 *
 */
public abstract class DBUtil {

    public static final int DB_TYPE_DB2 = 1;

    public static final int DB_TYPE_DERBY = 2;

    public static final int DB_TYPE_FIREBIRD = 3;

    public static final int DB_TYPE_HYPERSONIC = 4;

    public static final int DB_TYPE_INFORMIX = 5;

    public static final int DB_TYPE_INTERBASE = 6;

    public static final int DB_TYPE_JDATASTORE = 7;

    public static final int DB_TYPE_MYSQL = 8;

    public static final int DB_TYPE_ORACLE = 9;

    public static final int DB_TYPE_POSTGRESQL = 10;

    public static final int DB_TYPE_SAP = 11;

    public static final int DB_TYPE_SQLSERVER = 12;

    public static final int DB_TYPE_SYBASE = 13;

    public static DBUtil getInstance() {
        if (_dbUtil != null) {
            return _dbUtil;
        }
        Dialect dialect = HibernateUtil.getWrappedDialect();
        if (dialect instanceof DB2Dialect) {
            if (dialect instanceof DerbyDialect) {
                _dbUtil = DerbyUtil.getInstance();
            } else {
                _dbUtil = DB2Util.getInstance();
            }
        } else if (dialect instanceof HSQLDialect) {
            _dbUtil = HypersonicUtil.getInstance();
        } else if (dialect instanceof InformixDialect) {
            _dbUtil = InformixUtil.getInstance();
        } else if (dialect instanceof InterbaseDialect) {
            if (dialect instanceof FirebirdDialect) {
                _dbUtil = FirebirdUtil.getInstance();
            } else {
                _dbUtil = InterBaseUtil.getInstance();
            }
        } else if (dialect instanceof JDataStoreDialect) {
            _dbUtil = JDataStoreUtil.getInstance();
        } else if (dialect instanceof MySQLDialect) {
            _dbUtil = MySQLUtil.getInstance();
        } else if (dialect instanceof OracleDialect || dialect instanceof Oracle8iDialect || dialect instanceof Oracle9Dialect || dialect instanceof Oracle9iDialect || dialect instanceof Oracle10gDialect) {
            _dbUtil = OracleUtil.getInstance();
        } else if (dialect instanceof PostgreSQLDialect) {
            _dbUtil = PostgreSQLUtil.getInstance();
        } else if (dialect instanceof SAPDBDialect) {
            _dbUtil = SAPUtil.getInstance();
        } else if (dialect instanceof SybaseDialect) {
            if (dialect instanceof SQLServerDialect) {
                _dbUtil = SQLServerUtil.getInstance();
            } else {
                _dbUtil = SybaseUtil.getInstance();
            }
        }
        return _dbUtil;
    }

    public static DBUtil getInstance(int dbType) {
        DBUtil dbUtil = null;
        if (dbType == DB_TYPE_DB2) {
            dbUtil = DB2Util.getInstance();
        } else if (dbType == DB_TYPE_DERBY) {
            dbUtil = DerbyUtil.getInstance();
        } else if (dbType == DB_TYPE_FIREBIRD) {
            dbUtil = FirebirdUtil.getInstance();
        } else if (dbType == DB_TYPE_HYPERSONIC) {
            dbUtil = HypersonicUtil.getInstance();
        } else if (dbType == DB_TYPE_INFORMIX) {
            dbUtil = InformixUtil.getInstance();
        } else if (dbType == DB_TYPE_INTERBASE) {
            dbUtil = InterBaseUtil.getInstance();
        } else if (dbType == DB_TYPE_JDATASTORE) {
            dbUtil = JDataStoreUtil.getInstance();
        } else if (dbType == DB_TYPE_MYSQL) {
            dbUtil = MySQLUtil.getInstance();
        } else if (dbType == DB_TYPE_ORACLE) {
            dbUtil = OracleUtil.getInstance();
        } else if (dbType == DB_TYPE_POSTGRESQL) {
            dbUtil = PostgreSQLUtil.getInstance();
        } else if (dbType == DB_TYPE_SAP) {
            dbUtil = SAPUtil.getInstance();
        } else if (dbType == DB_TYPE_SQLSERVER) {
            dbUtil = SQLServerUtil.getInstance();
        } else if (dbType == DB_TYPE_SYBASE) {
            dbUtil = SybaseUtil.getInstance();
        }
        return dbUtil;
    }

    public void buildCreateFile(String databaseName) throws IOException {
        buildCreateFile(databaseName, true);
        buildCreateFile(databaseName, false);
    }

    public abstract String buildSQL(String template) throws IOException;

    public void buildSQLFile(String fileName) throws IOException {
        String template = buildTemplate(fileName);
        template = buildSQL(template);
        FileUtil.write("../sql/" + fileName + "/" + fileName + "-" + getServerName() + ".sql", template);
    }

    public void runSQL(String sql) throws IOException, SQLException {
        runSQL(new String[] { sql });
    }

    public void runSQL(String[] sqls) throws IOException, SQLException {
        Connection con = null;
        Statement stmt = null;
        try {
            con = HibernateUtil.getConnection();
            stmt = con.createStatement();
            for (int i = 0; i < sqls.length; i++) {
                String sql = buildSQL(sqls[i]);
                sql = sql.trim();
                if (sql.endsWith(";")) {
                    sql = sql.substring(0, sql.length() - 1);
                }
                if (sql.endsWith("go")) {
                    sql = sql.substring(0, sql.length() - 2);
                }
                if (_log.isDebugEnabled()) {
                    _log.debug(sql);
                }
                try {
                    stmt.executeUpdate(sql);
                } catch (SQLException sqle) {
                    throw sqle;
                }
            }
        } finally {
            DataAccess.cleanUp(con, stmt);
        }
    }

    public void runSQLTemplate(String path) throws IOException, SQLException {
        runSQLTemplate(path, true);
    }

    public void runSQLTemplate(String path, boolean failOnError) throws IOException, SQLException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream("com/liferay/portal/tools/sql/dependencies/" + path);
        if (is == null) {
            is = classLoader.getResourceAsStream(path);
        }
        String template = StringUtil.read(is);
        is.close();
        boolean evaluate = path.endsWith(".vm");
        runSQLTemplateString(template, evaluate, failOnError);
    }

    public void runSQLTemplateString(String template, boolean evaluate, boolean failOnError) throws IOException, SQLException {
        if (evaluate) {
            try {
                template = evaluateVM(template);
            } catch (Exception e) {
                _log.error(e, e);
            }
        }
        StringMaker sm = new StringMaker();
        BufferedReader br = new BufferedReader(new StringReader(template));
        String line = null;
        while ((line = br.readLine()) != null) {
            if (!line.startsWith("##")) {
                sm.append(line);
                if (line.endsWith(";")) {
                    String sql = sm.toString();
                    sm = new StringMaker();
                    try {
                        if (!sql.equals("COMMIT_TRANSACTION;")) {
                            runSQL(sql);
                        } else {
                            if (_log.isDebugEnabled()) {
                                _log.debug("Skip commit sql");
                            }
                        }
                    } catch (IOException ioe) {
                        if (failOnError) {
                            throw ioe;
                        } else if (_log.isWarnEnabled()) {
                            _log.warn(ioe.getMessage());
                        }
                    } catch (SQLException sqle) {
                        if (failOnError) {
                            throw sqle;
                        } else if (_log.isWarnEnabled()) {
                            _log.warn(sqle.getMessage());
                        }
                    }
                }
            }
        }
        br.close();
    }

    protected abstract void buildCreateFile(String databaseName, boolean minimal) throws IOException;

    protected String[] buildColumnNameTokens(String line) {
        String[] words = StringUtil.split(line, " ");
        if (words.length == 7) {
            words[5] = "not null;";
        }
        String[] template = { words[1], words[2], words[3], words[4], words[5] };
        return template;
    }

    protected String[] buildColumnTypeTokens(String line) {
        String[] words = StringUtil.split(line, " ");
        String nullable = "";
        if (words.length == 6) {
            nullable = "not null;";
        } else if (words.length == 5) {
            nullable = words[4];
        } else if (words.length == 4) {
            nullable = "not null;";
            if (words[3].endsWith(";")) {
                words[3] = words[3].substring(0, words[3].length() - 1);
            }
        }
        String[] template = { words[1], words[2], "", words[3], nullable };
        return template;
    }

    protected String buildTemplate(String fileName) throws IOException {
        File file = new File("../sql/" + fileName + ".sql");
        String template = FileUtil.read(file);
        if (fileName.equals("portal") || fileName.equals("portal-minimal") || fileName.equals("update-3.6.0-4.0.0")) {
            BufferedReader br = new BufferedReader(new StringReader(template));
            StringMaker sm = new StringMaker();
            String line = null;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("@include ")) {
                    int pos = line.indexOf(" ");
                    String includeFileName = line.substring(pos + 1, line.length());
                    File includeFile = new File("../sql/" + includeFileName);
                    if (!includeFile.exists()) {
                        continue;
                    }
                    String include = FileUtil.read(includeFile);
                    if (includeFileName.endsWith(".vm")) {
                        try {
                            include = evaluateVM(include);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    include = convertTimestamp(include);
                    include = StringUtil.replace(include, TEMPLATE, getTemplate());
                    sm.append(include);
                    sm.append("\n\n");
                } else {
                    sm.append(line);
                    sm.append("\n");
                }
            }
            br.close();
            template = sm.toString();
        }
        if (fileName.equals("indexes") && (this instanceof SybaseUtil)) {
            template = removeBooleanIndexes(template);
        }
        return template;
    }

    protected String convertTimestamp(String data) {
        String s = null;
        if (this instanceof MySQLUtil) {
            s = StringUtil.replace(data, "SPECIFIC_TIMESTAMP_", "");
        } else {
            s = data.replaceAll("SPECIFIC_TIMESTAMP_" + "\\d+", "CURRENT_TIMESTAMP");
        }
        return s;
    }

    protected String evaluateVM(String template) throws Exception {
        Map variables = new HashMap();
        variables.put("counter", new SimpleCounter());
        template = VelocityUtil.evaluate(template, variables);
        BufferedReader br = new BufferedReader(new StringReader(template));
        StringMaker sm = new StringMaker();
        String line = null;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            sm.append(line);
            sm.append("\n");
        }
        br.close();
        template = sm.toString();
        template = StringUtil.replace(template, "\n\n\n", "\n\n");
        return template;
    }

    protected String getMinimalSuffix(boolean minimal) {
        if (minimal) {
            return "-minimal";
        } else {
            return StringPool.BLANK;
        }
    }

    protected abstract String getServerName();

    protected abstract String[] getTemplate();

    protected String readSQL(String fileName, String comments, String eol) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
        StringMaker sm = new StringMaker();
        String line = null;
        while ((line = br.readLine()) != null) {
            if (!line.startsWith(comments)) {
                line = StringUtil.replace(line, new String[] { "\n", "\t" }, new String[] { "", "" });
                if (line.endsWith(";")) {
                    sm.append(line.substring(0, line.length() - 1));
                    sm.append(eol);
                } else {
                    sm.append(line);
                }
            }
        }
        br.close();
        return sm.toString();
    }

    protected String removeBooleanIndexes(String data) throws IOException {
        String portalData = FileUtil.read("../sql/portal-tables.sql");
        BufferedReader br = new BufferedReader(new StringReader(data));
        StringMaker sm = new StringMaker();
        String line = null;
        while ((line = br.readLine()) != null) {
            boolean append = true;
            int x = line.indexOf(" on ");
            if (x != -1) {
                int y = line.indexOf(" (", x);
                String table = line.substring(x + 4, y);
                x = y + 2;
                y = line.indexOf(")", x);
                String[] columns = StringUtil.split(line.substring(x, y));
                x = portalData.indexOf("create table " + table + " (");
                y = portalData.indexOf(");", x);
                String portalTableData = portalData.substring(x, y);
                for (int i = 0; i < columns.length; i++) {
                    if (portalTableData.indexOf(columns[i].trim() + " BOOLEAN") != -1) {
                        append = false;
                        break;
                    }
                }
            }
            if (append) {
                sm.append(line);
                sm.append("\n");
            }
        }
        br.close();
        return sm.toString();
    }

    protected String removeInserts(String data) throws IOException {
        BufferedReader br = new BufferedReader(new StringReader(data));
        StringMaker sm = new StringMaker();
        String line = null;
        while ((line = br.readLine()) != null) {
            if (!line.startsWith("insert into ") && !line.startsWith("update ")) {
                sm.append(line);
                sm.append("\n");
            }
        }
        br.close();
        return sm.toString();
    }

    protected String removeLongInserts(String data) throws IOException {
        BufferedReader br = new BufferedReader(new StringReader(data));
        StringMaker sm = new StringMaker();
        String line = null;
        while ((line = br.readLine()) != null) {
            if (!line.startsWith("insert into Image (") && !line.startsWith("insert into JournalArticle (") && !line.startsWith("insert into JournalStructure (") && !line.startsWith("insert into JournalTemplate (")) {
                sm.append(line);
                sm.append("\n");
            }
        }
        br.close();
        return sm.toString();
    }

    protected String removeNull(String content) {
        content = StringUtil.replace(content, " not null", " not_null");
        content = StringUtil.replace(content, " null", "");
        content = StringUtil.replace(content, " not_null", " not null");
        return content;
    }

    protected abstract String reword(String data) throws IOException;

    protected static String ALTER_COLUMN_TYPE = "alter_column_type ";

    protected static String ALTER_COLUMN_NAME = "alter_column_name ";

    protected static String DROP_PRIMARY_KEY = "drop primary key";

    protected static String[] REWORD_TEMPLATE = { "@table@", "@old-column@", "@new-column@", "@type@", "@nullable@" };

    protected static String[] TEMPLATE = { "##", "TRUE", "FALSE", "'01/01/1970'", "CURRENT_TIMESTAMP", " BOOLEAN", " DATE", " DOUBLE", " INTEGER", " LONG", " STRING", " TEXT", " VARCHAR", " IDENTITY", "COMMIT_TRANSACTION" };

    private static Log _log = LogFactory.getLog(DBUtil.class);

    private static DBUtil _dbUtil;
}
