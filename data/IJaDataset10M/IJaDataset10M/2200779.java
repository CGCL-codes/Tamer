package org.objectstyle.cayenne.conf;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.objectstyle.cayenne.conn.DataSourceInfo;
import org.objectstyle.cayenne.project.Project;
import org.objectstyle.cayenne.unit.CayenneTestCase;

/**
 * Test cases for DomainHelper class.
 * 
 * @author Andrei Adamchik
 */
public class ConfigSaverTst extends CayenneTestCase {

    protected ConfigSaver saver;

    public void testStoreFullDataNode() throws Exception {
        DataSourceInfo info = new DataSourceInfo();
        info.setDataSourceUrl("s1");
        info.setJdbcDriver("s2");
        info.setPassword("s3");
        info.setUserName("s4");
        info.setMaxConnections(35);
        info.setMinConnections(22);
        assertSaved(info);
    }

    public void testStoreDataNodeNoUserName() throws Exception {
        DataSourceInfo info = new DataSourceInfo();
        info.setDataSourceUrl("s1");
        info.setJdbcDriver("s2");
        info.setPassword("s3");
        info.setMaxConnections(35);
        info.setMinConnections(22);
        assertSaved(info);
    }

    public void testStoreDataNodeNoPassword() throws Exception {
        DataSourceInfo info = new DataSourceInfo();
        info.setDataSourceUrl("s1");
        info.setJdbcDriver("s2");
        info.setUserName("s4");
        info.setMaxConnections(35);
        info.setMinConnections(22);
        assertSaved(info);
    }

    protected void assertSaved(DataSourceInfo info) throws Exception {
        StringWriter str = new StringWriter();
        PrintWriter out = new PrintWriter(str);
        saver.storeDataNode(out, info);
        out.close();
        str.close();
        StringBuffer buf = str.getBuffer();
        if (info.getDataSourceUrl() != null) {
            assertTrue("URL not saved: " + info.getDataSourceUrl(), buf.toString().indexOf("<url value=\"" + info.getDataSourceUrl() + "\"/>") >= 0);
        }
        if (info.getJdbcDriver() != null) {
            assertTrue("Driver not saved: " + info.getJdbcDriver(), buf.toString().indexOf("<driver project-version=\"" + Project.CURRENT_PROJECT_VERSION + "\" class=\"" + info.getJdbcDriver() + "\">") >= 0);
        }
        if (info.getUserName() != null) {
            assertTrue("User name not saved: " + info.getUserName(), buf.toString().indexOf("userName=\"" + info.getUserName() + "\"") >= 0);
        }
        if (info.getPassword() != null) {
            assertTrue("Password not saved: " + info.getPassword(), buf.toString().indexOf("password=\"" + info.getPassword() + "\"") >= 0);
        }
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        saver = new ConfigSaver();
    }
}
