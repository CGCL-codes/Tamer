package solidbase.test.ant;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildFileTest;
import org.apache.tools.ant.BuildListener;
import org.apache.tools.ant.Project;
import org.testng.Assert;
import org.testng.annotations.Test;
import solidbase.core.TestUtil;

public class SqlTaskTests extends BuildFileTest {

    protected StringBuilder logBuffer;

    @Override
    public void configureProject(String filename, int logLevel) {
        super.configureProject(filename, logLevel);
        int count = 0;
        Iterator<BuildListener> iterator = this.project.getBuildListeners().iterator();
        while (iterator.hasNext()) {
            BuildListener listener = iterator.next();
            if (listener.getClass().getName().equals("org.apache.tools.ant.BuildFileTest$AntTestListener")) {
                iterator.remove();
                count++;
            }
        }
        Assert.assertEquals(count, 1);
        this.logBuffer = new StringBuilder();
        this.project.addBuildListener(new MyAntTestListener(logLevel));
    }

    @Override
    public String getFullLog() {
        return this.logBuffer.toString();
    }

    @Override
    public String getLog() {
        return this.logBuffer.toString();
    }

    @Test
    public void testSqlTask() {
        configureProject("test-sqltask.xml");
        this.project.setBaseDir(new File("."));
        executeTarget("ant-test");
        String log = TestUtil.generalizeOutput(getLog());
        Assert.assertEquals(log, "SolidBase v1.5.x (http://solidbase.org)\n" + "\n" + "Opening file 'X:/.../testsql1.sql'\n" + "    Encoding is 'ISO-8859-1'\n" + "Connecting to database...\n" + "Creating table USERS...\n" + "Inserting admin user...\n" + "Inserting 3 users...\n" + "Inserting 3 users...\n" + "Inserting 3 users...\n" + "Inserting 3 users...\n" + "Opening file 'X:/.../testsql2.sql'\n" + "    Encoding is 'ISO-8859-1'\n" + "Inserting 3 users...\n" + "Execution complete.\n" + "\n");
    }

    @Test
    public void testSqlFileDoesNotExist() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        String log = TestUtil.captureAnt(new Runnable() {

            public void run() {
                new AntMain().startAnt(new String[] { "-f", "test-sqltask.xml", "ant-test-filenotfound" }, null, null);
            }
        });
        log = TestUtil.generalizeOutput(log);
        Assert.assertEquals(log, "Buildfile: test-sqltask.xml\n" + "\n" + "ant-test-filenotfound:\n" + "   [sb-sql] SolidBase v1.5.x (http://solidbase.org)\n" + "   [sb-sql] \n" + "   [sb-sql] Opening file 'X:/.../doesnotexist.sql'\n" + "   [sb-sql] Execution aborted.\n" + "\n" + "BUILD FAILED\n" + "X:/.../test-sqltask.xml:47: java.io.FileNotFoundException: X:/.../doesnotexist.sql (The system cannot find the file specified)\n" + "\n" + "Total time: 0 seconds\n");
    }

    protected class MyAntTestListener implements BuildListener {

        public MyAntTestListener(int logLevel) {
        }

        public void buildStarted(BuildEvent event) {
        }

        public void buildFinished(BuildEvent event) {
        }

        public void targetStarted(BuildEvent event) {
        }

        public void targetFinished(BuildEvent event) {
        }

        public void taskStarted(BuildEvent event) {
        }

        public void taskFinished(BuildEvent event) {
        }

        public void messageLogged(BuildEvent event) {
            if (event.getPriority() == Project.MSG_INFO || event.getPriority() == Project.MSG_WARN || event.getPriority() == Project.MSG_ERR) {
                SqlTaskTests.this.logBuffer.append(event.getMessage());
                SqlTaskTests.this.logBuffer.append('\n');
            }
        }
    }
}
