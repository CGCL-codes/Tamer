package net.sf.iqser.plugin.web.base;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import net.sf.iqser.plugin.web.test.MockContentProviderFacade;
import net.sf.iqser.plugin.web.test.MockCrawlerContentProvider;
import net.sf.iqser.plugin.web.test.TestServiceLocator;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import com.iqser.core.config.Configuration;
import com.iqser.core.exception.IQserException;
import com.iqser.core.model.Content;
import com.iqser.core.plugin.ContentProviderFacade;
import junit.framework.TestCase;

public class McKinleyCrawlerTest extends TestCase {

    /** The provider to test */
    MockCrawlerContentProvider provider = null;

    /** The database connection to cache */
    Connection conn = null;

    /** Logger */
    private static Logger logger = Logger.getLogger(McKinleyCrawlerTest.class);

    protected void setUp() throws Exception {
        PropertyConfigurator.configure(System.getProperty("user.dir") + "/base-plugin/src/test/res/log4j.properties");
        logger.debug("setup() called");
        super.setUp();
        provider = new MockCrawlerContentProvider();
        Properties initParams = new Properties();
        initParams.setProperty("database", "localhost/crawler");
        initParams.setProperty("username", "root");
        initParams.setProperty("password", "master");
        initParams.setProperty("start-server", "http://www.morganmckinley.ie");
        initParams.setProperty("start-path", "/search/jobs/accountant?");
        initParams.setProperty("server-filter", "http://www.morganmckinley.ie");
        initParams.setProperty("maxdepth-filter", "1");
        initParams.setProperty("link-filter", "(\\S*accountant?\\S*)||(\\S+\\/job\\/\\d{6}\\S+)");
        initParams.setProperty("item-filter", "\\S+\\/job\\/\\d{6}\\S+");
        provider.setInitParams(initParams);
        provider.setType("Job");
        provider.setId("ie.morganmckinley.offerings");
        provider.init();
        Configuration.configure(new File(System.getProperty("user.dir") + "/base-plugin/src/test/res/iqser-config.xml"));
        TestServiceLocator sl = (TestServiceLocator) Configuration.getConfiguration().getServiceLocator();
        sl.setContentProviderFacade(new MockContentProviderFacade());
        try {
            conn = DriverManager.getConnection(provider.getInitParams().getProperty("protocol", "jdbc:mysql:") + "//" + provider.getInitParams().getProperty("database") + "?user=" + provider.getInitParams().getProperty("username") + "&password=" + provider.getInitParams().getProperty("password"));
        } catch (SQLException e) {
            fail("Could not establish database connection - " + e.getMessage());
        }
    }

    protected void tearDown() throws Exception {
        logger.debug("tearDown() called");
        provider.destroy();
        super.tearDown();
    }

    public void testInit() {
        logger.debug("testInit() called");
        try {
            Statement stmt = conn.createStatement();
            ;
            stmt.execute("DELETE FROM documents");
            ResultSet rs = stmt.executeQuery("SELECT COUNT(url) FROM documents");
            assertTrue(rs.first());
            assertEquals(0, rs.getInt("Count(url)"));
        } catch (SQLException e) {
            fail("Could not establish database connection - " + e.getMessage());
        }
    }

    public void testDoSynchonization() {
        logger.debug("testDoSynchronization() called");
        provider.doSynchonization();
        try {
            Statement stmt = conn.createStatement();
            ;
            ResultSet rs = stmt.executeQuery("SELECT COUNT(url) FROM documents");
            assertTrue(rs.first());
            assertTrue(rs.getInt("Count(url)") > 0);
        } catch (SQLException e) {
            fail("Could not establish database connection - " + e.getMessage());
        }
    }

    public void testDoHousekeeping() {
        logger.debug("tstDoHousekeeping() called");
        ContentProviderFacade cpf = Configuration.getConfiguration().getServiceLocator().getContentProviderFacade();
        try {
            Statement stmt = conn.createStatement();
            ;
            ResultSet rs = stmt.executeQuery("SELECT url FROM documents");
            while (rs.next()) {
                Content c = new Content();
                c.setContentUrl(rs.getString("url"));
                c.setProvider(provider.getId());
                c.setType(provider.getType());
                cpf.addContent(c);
            }
        } catch (SQLException e) {
            fail("Could not establish database connection - " + e.getMessage());
        } catch (IQserException iqe) {
            fail("Could not add content to repository - " + iqe.getMessage());
        }
        Properties initParams = provider.getInitParams();
        initParams.setProperty("start-path", "/Seiten/impressum.html");
        provider.setInitParams(initParams);
        provider.doSynchonization();
        provider.doHousekeeping();
        try {
            Statement stmt = conn.createStatement();
            ;
            ResultSet rs = stmt.executeQuery("SELECT COUNT(url) FROM documents");
            assertTrue(rs.first());
            assertTrue(rs.getInt("Count(url)") > 0);
        } catch (SQLException e) {
            fail("Could not establish database connection - " + e.getMessage());
        }
    }

    public void testGetContentUrls() {
        logger.debug("testGetContentUrls() called");
    }

    public void testParse() {
        logger.debug("testParse() called");
    }
}
