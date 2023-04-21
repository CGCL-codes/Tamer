package com.oracle2hsqldb.ant;

import junit.framework.TestCase;
import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.BuildLogger;
import org.apache.tools.ant.Project;
import org.easymock.MockControl;
import org.easymock.classextension.MockClassControl;
import com.oracle2hsqldb.Column;
import com.oracle2hsqldb.Schema;
import com.oracle2hsqldb.Table;
import java.io.IOException;
import java.io.PrintStream;
import java.io.File;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;

/**
 * @author Moses Hohman
 */
public class SchemaCopyTaskTest extends TestCase {

    private Project project = new Project();

    private SchemaCopyTask task = new SchemaCopyTask();

    protected void setUp() throws Exception {
        project.addBuildListener(new ConsoleLogger());
        task.setProject(project);
    }

    public void testExecuteThrowsBuildExceptionIfThereAreNoFroms() throws IOException, URISyntaxException {
        task.addConfiguredTo(createHsqlSchemaParams());
        try {
            task.execute();
            fail("Should have thrown BuildException");
        } catch (BuildException expected) {
            assertEquals("No from subelements to copy", expected.getMessage());
        }
    }

    public void testExecuteThrowsBuildExceptionIfToNotAdded() throws IOException, URISyntaxException {
        task.addConfiguredFrom(createSchemaParams());
        try {
            task.execute();
            fail("Should have thrown BuildException");
        } catch (BuildException expected) {
            assertEquals("to subelement is required", expected.getMessage());
        }
    }

    public void testAddingToTwiceThrowsBuildException() throws IOException, URISyntaxException {
        task.addConfiguredTo(createHsqlSchemaParams());
        try {
            task.addConfiguredTo(createHsqlSchemaParams());
            fail("Should have thrown BuildException");
        } catch (BuildException expected) {
            assertEquals("only one to subelement is allowed", expected.getMessage());
        }
    }

    public void testAddingInvalidToThrowsBuildException() throws IOException, URISyntaxException {
        SchemaParams to = createHsqlSchemaParams();
        to.setUsername(null);
        try {
            task.addConfiguredTo(to);
            fail("Should have thrown BuildException");
        } catch (BuildException expected) {
            assertEquals("username argument is required", expected.getMessage());
        }
    }

    public void testAddingToWithTablesThrowsBuildException() throws IOException, URISyntaxException {
        SchemaParams to = createHsqlSchemaParams();
        TableParams table = new TableParams();
        table.setName("T_BLAH");
        to.addConfiguredTable(table);
        try {
            task.addConfiguredTo(to);
            fail("Should have thrown BuildException");
        } catch (BuildException expected) {
            assertEquals("to subelement does not support table subelements", expected.getMessage());
        }
    }

    public void testExecuteThrowsBuildExceptionIfBatchsizeLessThanOne() throws IOException, URISyntaxException {
        task.addConfiguredTo(createHsqlSchemaParams());
        task.setBatchSize(0);
        task.addConfiguredFrom(createSchemaParams());
        try {
            task.execute();
            fail("Should have thrown BuildException");
        } catch (BuildException expected) {
            assertEquals("batchsize argument must be >= 1", expected.getMessage());
        }
    }

    public void testAddConfiguredSchemaThrowsBuildExceptionIfInvalidFromIsAdded() throws IOException, URISyntaxException {
        try {
            task.addConfiguredFrom(createSchemaParamsWithProjectOnly());
            fail("Should have thrown BuildException");
        } catch (BuildException expected) {
            assertEquals("uri argument is required", expected.getMessage());
        }
    }

    public void testReadsAndWritesSchemas() throws IOException, URISyntaxException, SQLException {
        SchemaParams to = createHsqlFileSchemaParams();
        SchemaParams from1 = createHsqlSchemaParams("from1");
        SchemaParams from2 = createHsqlSchemaParams("from2");
        task.setProject(project);
        task.addConfiguredTo(to);
        task.addConfiguredFrom(from1);
        task.addConfiguredFrom(from2);
        executeOnSchema(from1, "CREATE TABLE t_blah (id INTEGER)");
        executeOnSchema(from2, "CREATE TABLE t_hooey (name VARCHAR)");
        task.execute();
        verify(to);
    }

    public void testExecuteTearsdownTo() throws IOException, URISyntaxException, SQLException {
        MockControl control = MockClassControl.createStrictControl(SchemaParams.class);
        control.setDefaultMatcher(MockControl.ALWAYS_MATCHER);
        SchemaParams mockTo = (SchemaParams) control.getMock();
        SchemaParams from = createHsqlSchemaParams();
        mockTo.setTask(null);
        mockTo.validate();
        control.expectAndReturn(mockTo.getIncludedTables(), new HashSet());
        control.expectAndReturn(mockTo.getConnection(), createHsqlSchemaParams().getConnection());
        mockTo.writeSchemas(null, null);
        mockTo.teardown(null);
        control.replay();
        task.setProject(project);
        task.addConfiguredTo(mockTo);
        task.addConfiguredFrom(from);
        task.execute();
        control.verify();
    }

    private void verify(SchemaParams to) throws SQLException {
        to.setProject(project);
        Schema schema = to.readSchema();
        assertEquals("no tables", 2, schema.tables().size());
        Table blah = schema.findTable("T_BLAH");
        assertNotNull("blah table not found", blah);
        assertEquals("no blah columns", 1, blah.columns().size());
        Column id = blah.findColumn("ID");
        assertNotNull("blah.id column not found", id);
        Table hooey = schema.findTable("T_HOOEY");
        assertNotNull("hooey table not found", hooey);
        assertEquals("no hooey columns", 1, hooey.columns().size());
        Column name = hooey.findColumn("NAME");
        assertNotNull("hooey.name column not found", name);
    }

    private void executeOnSchema(SchemaParams params, String sql) throws SQLException {
        Connection create = params.getConnection();
        Statement statement = null;
        try {
            statement = create.createStatement();
            statement.executeUpdate(sql);
        } finally {
            if (statement != null) statement.close();
            create.close();
        }
    }

    private SchemaParams createHsqlSchemaParams() throws URISyntaxException {
        return createHsqlSchemaParams("whatever");
    }

    private static String getTempHsqlFileUri() throws IOException {
        File tempFile = File.createTempFile("hsqltest", "");
        return new StringBuffer("jdbc:hsqldb:file:").append(tempFile.getAbsolutePath()).toString();
    }

    private SchemaParams createHsqlSchemaParams(String alias) throws URISyntaxException {
        SchemaParams result = createSchemaParams(alias);
        result.setSchema(null);
        return result;
    }

    private SchemaParams createSchemaParams(String alias) throws URISyntaxException {
        SchemaParams params = createSchemaParamsWithProjectOnly();
        params.setUri("jdbc:hsqldb:mem:" + alias);
        params.setUsername("sa");
        params.setPassword("");
        return params;
    }

    private SchemaParams createSchemaParams() throws URISyntaxException {
        return createSchemaParams("whatever");
    }

    private SchemaParams createSchemaParamsWithProjectOnly() {
        SchemaParams params = new SchemaParams();
        params.setProject(project);
        params.setTask(task);
        return params;
    }

    private SchemaParams createHsqlFileSchemaParams() throws IOException, URISyntaxException {
        SchemaParams result = createHsqlSchemaParams();
        result.setUri(getTempHsqlFileUri());
        return result;
    }

    private static class ConsoleLogger implements BuildLogger {

        public void setMessageOutputLevel(int i) {
        }

        public void setOutputPrintStream(PrintStream printStream) {
        }

        public void setEmacsMode(boolean b) {
        }

        public void setErrorPrintStream(PrintStream printStream) {
        }

        public void buildStarted(BuildEvent event) {
            messageLogged(event);
        }

        public void buildFinished(BuildEvent event) {
            messageLogged(event);
        }

        public void targetStarted(BuildEvent event) {
            messageLogged(event);
        }

        public void targetFinished(BuildEvent event) {
            messageLogged(event);
        }

        public void taskStarted(BuildEvent event) {
            messageLogged(event);
        }

        public void taskFinished(BuildEvent event) {
            messageLogged(event);
        }

        public void messageLogged(BuildEvent event) {
            System.out.println(event.getMessage());
        }
    }
}
