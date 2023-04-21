package com.cateshop.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.connection.ConnectionProvider;
import org.hibernate.connection.ConnectionProviderFactory;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.Mapping;
import org.hibernate.mapping.ForeignKey;
import org.hibernate.mapping.Table;
import org.hibernate.pretty.DDLFormatter;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.util.JDBCExceptionReporter;

/**
 * ������ݿⶨ��.
 */
public class SchemaExporter implements Serializable {

    private static final long serialVersionUID = -7763649712696133710L;

    private static final Log log = LogFactory.getLog(SchemaExporter.class);

    static String sqlDelimiter = null;

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            if ((args == null) || (args.length == 0)) {
                throw new IllegalArgumentException();
            }
            String cmd = args[0];
            if ("create".equalsIgnoreCase(cmd)) {
                Configuration configuration = createConfiguration(args, 1);
                new SchemaExporter(configuration).createSchema();
            } else if ("drop".equalsIgnoreCase(cmd)) {
                Configuration configuration = createConfiguration(args, 1);
                new SchemaExporter(configuration).dropSchema();
            } else if ("clean".equalsIgnoreCase(cmd)) {
                Configuration configuration = createConfiguration(args, 1);
                new SchemaExporter(configuration).cleanSchema();
            } else if ("scripts".equalsIgnoreCase(cmd)) {
                Configuration configuration = createConfiguration(args, 3);
                new SchemaExporter(configuration).saveSqlScripts(args[1], args[2]);
            }
        } catch (IllegalArgumentException e) {
            System.err.println("syntax: SchemaExporter create [<hibernate.cfg.xml> [<hibernate.properties>]]");
            System.err.println("syntax: SchemaExporter drop [<hibernate.cfg.xml> [<hibernate.properties>]]");
            System.err.println("syntax: SchemaExporter clean [<hibernate.cfg.xml> [<hibernate.properties>]]");
            System.err.println("syntax: SchemaExporter scripts <dir> <prefix> [<hibernate.cfg.xml> [<hibernate.properties>]]");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExportException(e);
        }
    }

    static Configuration createConfiguration(String[] args, int index) {
        String hibernateCfgXml = (args.length > index ? args[index] : "hibernate.cfg.xml");
        String hibernateProperties = (args.length > (index + 1) ? args[index + 1] : null);
        Configuration configuration = new Configuration();
        configuration.configure(new File(hibernateCfgXml));
        if (hibernateProperties != null) {
            try {
                Properties properties = new Properties();
                InputStream inputStream = new FileInputStream(hibernateProperties);
                properties.load(inputStream);
                configuration.setProperties(properties);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ExportException("couldn't load hibernate configuration", e);
            }
        }
        return configuration;
    }

    private String[] cleanSql = null;

    private Configuration configuration = null;

    private transient Connection connection = null;

    private transient ConnectionProvider connectionProvider = null;

    private String[] createSql = null;

    private transient Dialect dialect = null;

    private String[] dropSql = null;

    private transient Mapping mapping = null;

    private Properties properties = null;

    private transient Statement statement = null;

    /**
     * @param configuration
     */
    public SchemaExporter(Configuration configuration) {
        this.configuration = configuration;
        this.properties = configuration.getProperties();
        this.dialect = Dialect.getDialect(properties);
        try {
            Field mappingField = Configuration.class.getDeclaredField("mapping");
            mappingField.setAccessible(true);
            this.mapping = (Mapping) mappingField.get(configuration);
        } catch (Exception e) {
            throw new ExportException("couldn't get the hibernate mapping", e);
        }
    }

    /**
     * 
     */
    public void cleanSchema() {
        execute(getCleanSql());
    }

    /**
     * 
     */
    public void createSchema() {
        execute(getCreateSql());
    }

    /**
     * 
     */
    public void dropSchema() {
        execute(getDropSql());
    }

    /**
     * @param sqls
     */
    public void execute(String[] sqls) {
        String sql = null;
        String showSqlText = properties.getProperty("hibernate.show_sql");
        boolean showSql = ("true".equalsIgnoreCase(showSqlText));
        try {
            createConnection();
            statement = connection.createStatement();
            for (int i = 0; i < sqls.length; i++) {
                sql = sqls[i];
                if (showSql) log.debug(sql);
                statement.executeUpdate(sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ExportException("couldn't execute sql '" + sql + "'", e);
        } finally {
            closeConnection();
        }
    }

    /**
     * @return String[]
     */
    public String[] getCleanSql() {
        if (cleanSql == null) {
            List<String> dropForeignKeysSql = new ArrayList<String>();
            List<String> createForeignKeysSql = new ArrayList<String>();
            Iterator iter = configuration.getTableMappings();
            while (iter.hasNext()) {
                Table table = (Table) iter.next();
                if (table.isPhysicalTable()) {
                    Iterator subIter = table.getForeignKeyIterator();
                    while (subIter.hasNext()) {
                        ForeignKey fk = (ForeignKey) subIter.next();
                        if (fk.isPhysicalConstraint()) {
                            dropForeignKeysSql.add(fk.sqlDropString(dialect, properties.getProperty(Environment.DEFAULT_CATALOG), properties.getProperty(Environment.DEFAULT_SCHEMA)));
                            createForeignKeysSql.add(fk.sqlCreateString(dialect, mapping, properties.getProperty(Environment.DEFAULT_CATALOG), properties.getProperty(Environment.DEFAULT_SCHEMA)));
                        }
                    }
                }
            }
            List<String> deleteSql = new ArrayList<String>();
            iter = configuration.getTableMappings();
            while (iter.hasNext()) {
                Table table = (Table) iter.next();
                deleteSql.add("delete from " + table.getName());
            }
            List<String> cleanSqlList = new ArrayList<String>();
            cleanSqlList.addAll(dropForeignKeysSql);
            cleanSqlList.addAll(deleteSql);
            cleanSqlList.addAll(createForeignKeysSql);
            cleanSql = cleanSqlList.toArray(new String[cleanSqlList.size()]);
        }
        return cleanSql.clone();
    }

    /**
     * @return String[]
     */
    public String[] getCreateSql() {
        if (createSql == null) {
            createSql = configuration.generateSchemaCreationScript(dialect);
        }
        return createSql.clone();
    }

    /**
     * @return String[]
     */
    public String[] getDropSql() {
        if (dropSql == null) {
            dropSql = configuration.generateDropSchemaScript(dialect);
        }
        return dropSql.clone();
    }

    /**
     * @return Properties
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     * @param dir
     * @param prefix
     */
    public void saveSqlScripts(String dir, String prefix) {
        try {
            new File(dir).mkdirs();
            saveSqlScript(dir + "/" + prefix + ".drop.sql", getDropSql());
            saveSqlScript(dir + "/" + prefix + ".create.sql", getCreateSql());
            saveSqlScript(dir + "/" + prefix + ".clean.sql", getCleanSql());
            new SchemaExport(configuration).setDelimiter(getSqlDelimiter()).setOutputFile(dir + "/" + prefix + ".drop.create.sql").create(true, false);
        } catch (Exception e) {
            throw new ExportException("couldn't generate scripts", e);
        }
    }

    private void closeConnection() {
        try {
            if (statement != null) statement.close();
            if (connection != null) {
                JDBCExceptionReporter.logWarnings(connection.getWarnings());
                connection.clearWarnings();
                connectionProvider.closeConnection(connection);
                connectionProvider.close();
            }
        } catch (Exception e) {
            System.err.println("Could not close connection");
            e.printStackTrace();
        }
    }

    private void createConnection() throws SQLException {
        connectionProvider = ConnectionProviderFactory.newConnectionProvider(properties);
        connection = connectionProvider.getConnection();
        if (!connection.getAutoCommit()) {
            connection.commit();
            connection.setAutoCommit(true);
        }
    }

    private synchronized String getSqlDelimiter() {
        if (sqlDelimiter == null) {
            sqlDelimiter = properties.getProperty("jbpm.sql.delimiter", ";");
        }
        return sqlDelimiter;
    }

    private void saveSqlScript(String fileName, String[] sql) throws FileNotFoundException {
        PrintStream printStream = new PrintStream(new FileOutputStream(fileName));
        try {
            for (int i = 0; i < sql.length; i++) {
                printStream.print(new DDLFormatter(sql[i]).format());
            }
        } finally {
            printStream.close();
        }
    }
}
