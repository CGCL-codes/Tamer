package com.sptci.rwt;

import java.util.List;
import static junit.framework.Assert.*;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.sptci.util.CloseJDBCResources;

/**
 * Unit test setup class for creating test database objects to use in
 * the unit test suite.  This also functions as a rudimentary test for
 * the {@link BatchQueryExecutor} class.
 * 
 * <p>&copy; Copyright 2007 Sans Pareil Technologies, Inc.</p>
 * @author Rakesh Vidyadharan 2007-09-25
 * @version $Id: CreateTestObjects.java 2 2007-10-19 21:06:36Z rakesh.vidyadharan $
 */
public class CreateTestObjects extends TestCase {

    static ConnectionParameters parameters;

    static ConnectionManager manager;

    static final String tableName = "test" + System.currentTimeMillis();

    static final String refTableName = "testref" + System.currentTimeMillis();

    static final String viewName = "testView" + System.currentTimeMillis();

    static final String functionName = "testFunction" + System.currentTimeMillis();

    static final String triggerName = "testTrigger" + System.currentTimeMillis();

    static final String sequenceName = "testSequence" + System.currentTimeMillis();

    static final String indexName = "testIndex" + System.currentTimeMillis();

    public static Test suite() {
        return new TestSuite(CreateTestObjects.class);
    }

    /**
   * Test creating tables using {@link BatchQueryExecutor}.
   */
    public void testCreate() throws Exception {
        parameters = getParameters();
        assertNotNull("Checking initialised ConnectionParameters", parameters);
        manager = new ConnectionManager(parameters);
        createTable();
    }

    /**
   * Fetch the connection parameters from the build properties file
   */
    protected ConnectionParameters getParameters() {
        final String type = System.getProperty("database.type");
        assertNotNull("Checking database.type", type);
        final String driver = System.getProperty("jdbc.driver");
        assertNotNull("Checking JDBC driver", driver);
        final String url = System.getProperty("url.pattern");
        assertNotNull("Checking url.pattern", url);
        final String host = System.getProperty("database.host");
        assertNotNull("Checking database host", host);
        final String name = System.getProperty("database.name");
        assertNotNull("Checking databsae name", name);
        final String port = System.getProperty("database.port");
        assertNotNull("Checking port", port);
        final String user = System.getProperty("database.user");
        assertNotNull("Checking databsae user", user);
        final String password = System.getProperty("database.password");
        assertNotNull("Checking password", password);
        return new ConnectionParameters(user, password, host, Integer.parseInt(port), name, type, url, driver);
    }

    /**
   * Create a test table to ensure that there is at least one table for
   * testing analysers.
   */
    protected void createTable() throws Exception {
        String ddl = "create table $TABLENAME$ ( \n" + "name character varying(100) not null, \n" + "description character varying(1000) null, \n" + "constraint pk_$TABLENAME$ primary key (name) ); \n" + "comment on table $TABLENAME$ is 'A unit test table'; \n" + "create table $REFTABLENAME$ ( \n" + "id numeric(9) not null, \n" + "name character varying(100) not null, \n" + "constraint pk_$REFTABLENAME$ primary key (id,name), \n" + "constraint fk_$TABLENAME$_$REFTABLENAME$ " + "foreign key (name) references $TABLENAME$ " + "(name) on delete cascade ); \n" + "create index idx_name_$REFTABLENAME$ on $REFTABLENAME$ (name); \n" + "comment on table $REFTABLENAME$ is 'A unit test reference table'; \n" + "create view $VIEWNAME$ as \n" + "select b.id, a.name, a.description \n" + "from $TABLENAME$ a, $REFTABLENAME$ b \n" + "where a.name = b.name \n" + "order by a.name; \n" + "comment on view $VIEWNAME$ is 'A unit test schema'; \n" + "create function $FUNCTIONNAME$() returns trigger as $func_body$\n" + "begin select old.*; return old; end; \n" + "$func_body$ language plpgsql; \n" + "create trigger $TRIGGERNAME$ after insert or update or delete on $REFTABLENAME$ \n" + "  for each row execute procedure $FUNCTIONNAME$();\n" + "create sequence $SEQUENCENAME$;\n" + "create index $INDEXNAME$ on $REFTABLENAME$ (id,name);\n";
        ddl = ddl.replaceAll("\\$TABLENAME\\$", tableName);
        ddl = ddl.replaceAll("\\$REFTABLENAME\\$", refTableName);
        ddl = ddl.replaceAll("\\$VIEWNAME\\$", viewName);
        ddl = ddl.replaceAll("\\$FUNCTIONNAME\\$", functionName);
        ddl = ddl.replaceAll("\\$TRIGGERNAME\\$", triggerName);
        ddl = ddl.replaceAll("\\$SEQUENCENAME\\$", sequenceName);
        ddl = ddl.replaceAll("\\$INDEXNAME\\$", indexName);
        final int ddlCount = 11;
        BatchQueryExecutor executor = new BatchQueryExecutor(manager);
        List<Rows> rows = executor.execute(ddl);
        System.out.format("Created test tables: %s, %s and view: %s%n", tableName, refTableName, viewName);
        assertEquals("Ensuring ddlCount results returned", rows.size(), ddlCount);
        assertEquals("Ensuring single row results returned", rows.get(0).getRows().size(), 1);
        assertEquals("Ensuring only one column in row results", rows.get(0).getRows().get(0).getColumns().size(), 1);
    }
}
