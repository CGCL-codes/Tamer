package cc.w3d.jawos.xloginManager;

import static cc.w3d.jawos.xjawosdata.XJawosDataContainer.XJawosData;
import static cc.w3d.jawos.xlogin.XLoginContainer.XLogin;
import static cc.w3d.jawos.xloginManager.XLoginManagerContainer.XLoginManager;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import cc.w3d.enviromentConfiguration.Configuration;
import cc.w3d.jawos.xjawosdata.engines.xeMysqlJawosData.configurationManager.XEMysqlJawosDataConfigurator;
import cc.w3d.jawos.xlogin.engines.xeDataLogin.configurationManager.XEDataLoginConfigurator;
import cc.w3d.jawos.xloginManager.engines.xeDataLoginManager.configurationManager.XEDataLoginManagerConfigurator;

public class XLoginManagerTest extends TestCase {

    public XLoginManagerTest() {
        super();
    }

    public static Test suite() {
        return new TestSuite(XLoginManagerTest.class);
    }

    public static void configureDB() {
        XEMysqlJawosDataConfigurator cfg = (XEMysqlJawosDataConfigurator) XJawosData.getEngineConfigurationManager();
        cfg.login(Configuration.getMySqlUser(), Configuration.getMySqlPassword());
        cfg.setLocation(Configuration.getMySqlLocation());
        cfg.setDatabase(Configuration.getMySqlTestDatabaseName());
    }

    public static void initializeMySqlDbConfig() {
        configureDB();
        XEDataLoginManagerConfigurator lmcfg = (XEDataLoginManagerConfigurator) XLoginManager.getEngineConfigurationManager();
        XEDataLoginConfigurator lcfg = (XEDataLoginConfigurator) XLogin.getEngineConfigurationManager();
        lmcfg.format();
        lmcfg.setReference("XLOGIN");
        lcfg.setReference("XLOGIN");
        XJawosData.setReference("XLOGIN");
    }

    public void test1() {
        initializeMySqlDbConfig();
        XLoginManager.put("foo", "foopassword");
        assertTrue(XLoginManager.exists("foo"));
        assertTrue(XLogin.autenticate(XLoginManager.codeToAuthenticationString("foo", "foopassword")));
        XLoginManager.put("foo", "badpassword");
        assertTrue(XLoginManager.exists("foo"));
        assertTrue(!XLogin.autenticate(XLoginManager.codeToAuthenticationString("foo", "foopassword")));
        XLoginManager.delete("foo");
        assertTrue(!XLoginManager.exists("foo"));
        assertTrue(!XLogin.autenticate(XLoginManager.codeToAuthenticationString("foo", "foopassword")));
    }
}
