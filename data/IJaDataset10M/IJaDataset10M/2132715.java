package junit.util;

import java.lang.reflect.Array;
import java.util.Calendar;
import org.junit.*;
import org.junit.Assert.*;
import junit.framework.JUnit4TestAdapter;
import dbaccess.util2.*;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

/** 
 * JUnit TestCase for DBProperties class 
 */
public class DBPropertiesTest {

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(DBPropertiesTest.class);
    }

    /** 
    * Test all constructors of DBProperties class.
    */
    @Test
    public void constructors() {
        DBProperties prop;
        prop = new DBProperties();
        Assert.assertEquals("guest", prop.get("dbLogin"));
        prop = new DBProperties("iono");
        Assert.assertEquals("iono", prop.get("propPrefix"));
        String progname = "iiwgload";
        String progdesc = "Load IIWG Format Data";
        prop = new DBProperties(progname, progdesc);
        Assert.assertEquals(progname, prop.get("progName"));
        Assert.assertEquals(progdesc, prop.get("progDesc"));
        String uid = "testuser";
        String pwd = "testpwd";
        prop = new DBProperties(progname, progdesc, uid);
        Assert.assertEquals(uid, prop.get("dbLogin"));
        prop = new DBProperties(progname, progdesc, uid, pwd);
        Assert.assertEquals(uid, prop.get("dbLogin"));
        Assert.assertEquals(pwd, prop.get("dbPassword"));
        String args[] = new String[4];
        args[0] = "--login=testuser";
        args[1] = "--password=testpwd";
    }

    /**
    * Test flag options
    */
    @Test
    public void testFlagOpt() {
        String[] a = new String[2];
        a[0] = "--verbose";
        a[1] = "--local";
        DBProperties prop = new DBProperties();
        prop.setArgs(a);
        prop.getOptions();
        Assert.assertTrue(prop.verbose());
        Assert.assertEquals("local", prop.get("host"));
    }

    /**
    * Test JDBC options
    */
    @Test
    public void testjdbcOpt() {
        String[] a = new String[9];
        String uid = "testuser";
        String pwd = "testpwd";
        String db = "ionodb";
        String dbms = "mysql";
        String host = "esg3";
        String port = "3306";
        String protocol = "mysql";
        String driver = "mm";
        String url = "jdbc:mysql://localhost.localdomain/ionodb?autoReconnect=true";
        a[0] = "--login=" + uid;
        a[1] = "--password=" + pwd;
        a[2] = "--database=" + db;
        a[3] = "--dbms=" + dbms;
        a[4] = "--host=" + host;
        a[5] = "--port=" + port;
        a[6] = "--protocol=" + protocol;
        a[7] = "--driver=" + driver;
        a[8] = "--url=" + url;
        DBProperties prop = new DBProperties();
        prop.setArgs(a);
        prop.getOptions();
        Assert.assertEquals(uid, prop.get("dbLogin"));
        Assert.assertEquals(pwd, prop.get("dbPassword"));
        Assert.assertEquals(db, prop.get("database"));
        Assert.assertEquals(dbms, prop.get("dbms"));
        Assert.assertEquals(host, prop.get("host"));
        Assert.assertEquals(port, prop.get("port"));
        Assert.assertEquals(protocol, prop.get("protocol"));
        Assert.assertEquals(driver, prop.get("dbDriver"));
        Assert.assertEquals(url, prop.get("dbUrl"));
    }

    /**
    * Test JDBC shortcut options
    */
    @Test
    public void testJdbcOpt2() {
        String[] a = new String[4];
        String uid = "testuser";
        String pwd = "testpwd";
        a[0] = "-l";
        a[1] = uid;
        a[2] = "-p";
        a[3] = pwd;
        DBProperties prop = new DBProperties();
        prop.setArgs(a);
        prop.getOptions();
        Assert.assertEquals(uid, prop.get("dbLogin"));
        Assert.assertEquals(pwd, prop.get("dbPassword"));
    }

    /**
    * Tests file options
    */
    @Test
    public void testFileOpt() {
        String[] a = new String[3];
        String uid = "testuser";
        String pwd = "testpwd";
        String file = "testfile";
        a[0] = "--login=" + uid;
        a[1] = "--password=" + pwd;
        a[2] = "--file=" + file;
        DBProperties prop = new DBProperties();
        prop.setArgs(a);
        prop.getOptions();
        Assert.assertEquals(file, prop.get("file"));
    }

    /**
    * Tests file shortcut options
    */
    @Test
    public void testFileOpt2() {
        String[] a = new String[4];
        String uid = "testuser";
        String pwd = "testpwd";
        String file = "testfile";
        a[0] = "--login=" + uid;
        a[1] = "--password=" + pwd;
        a[2] = "-f";
        a[3] = file;
        DBProperties prop = new DBProperties();
        prop.setArgs(a);
        prop.getOptions();
        Assert.assertEquals(file, prop.get("file"));
    }

    /**
    * Tests logging options
    */
    @Test
    public void testLogOpt() {
        String[] a = new String[6];
        String uid = "testuser";
        String pwd = "testpwd";
        String cfg = "testconfigfile";
        String lvl = "testlevel";
        String pat = "testpattern";
        String file = "testlogfile";
        a[0] = "--login=" + uid;
        a[1] = "--password=" + pwd;
        a[2] = "--logcfg=" + cfg;
        a[3] = "--loglevel=" + lvl;
        a[4] = "--logpattern=" + pat;
        a[5] = "--logfile=" + file;
        DBProperties prop = new DBProperties();
        prop.setArgs(a);
        prop.getOptions();
        Assert.assertEquals(cfg, prop.get("logcfg"));
        Assert.assertEquals(lvl, prop.get("loglevel"));
        Assert.assertEquals(pat, prop.get("logpattern"));
        Assert.assertEquals(file, prop.get("logfile"));
    }

    /**
    * Tests logging shortcut options
    */
    @Test
    public void testLogOpt2() {
        String[] a = new String[10];
        String uid = "testuser";
        String pwd = "testpwd";
        String cfg = "testconfigfile";
        String lvl = "testlevel";
        String pat = "testpattern";
        String file = "testlogfile";
        a[0] = "--login=" + uid;
        a[1] = "--password=" + pwd;
        a[2] = "-C";
        a[3] = cfg;
        a[4] = "-L";
        a[5] = lvl;
        a[6] = "-P";
        a[7] = pat;
        a[8] = "-F";
        a[9] = file;
        DBProperties prop = new DBProperties();
        prop.setArgs(a);
        prop.getOptions();
        Assert.assertEquals(cfg, prop.get("logcfg"));
        Assert.assertEquals(lvl, prop.get("loglevel"));
        Assert.assertEquals(pat, prop.get("logpattern"));
        Assert.assertEquals(file, prop.get("logfile"));
    }

    /**
    * Test extra arguments
    */
    @Test
    public void testExtra() {
        String[] a = new String[5];
        String uid = "testuser";
        String pwd = "testpwd";
        String arg1 = "arg1";
        String arg2 = "arg2";
        String arg3 = "arg3";
        a[0] = "--login=" + uid;
        a[1] = "--password=" + pwd;
        a[2] = arg1;
        a[3] = arg2;
        a[4] = arg3;
        DBProperties prop = new DBProperties();
        prop.setArgs(a);
        prop.getOptions();
        Assert.assertTrue(prop.isExtraArgs());
        String[] extraArgs = prop.getExtraArgs();
        Assert.assertEquals(arg1, extraArgs[0]);
        Assert.assertEquals(arg2, extraArgs[1]);
        Assert.assertEquals(arg3, extraArgs[2]);
    }
}
