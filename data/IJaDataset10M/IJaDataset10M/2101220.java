package excel.report.test;

import java.io.File;
import java.util.List;
import java.util.Map;
import net.excel.report.base.element.Variable;
import net.excel.report.config.DataSourceConfig;
import net.excel.report.config.ReportConfigManager;
import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * Description:
 * @author juny
 */
public class ConfigTest extends TestCase {

    /**
     * @param x
     */
    public ConfigTest(String x) {
        super(x);
    }

    public static Test suite() {
        return new TestSuite(ConfigTest.class);
    }

    public static void main(String[] args) throws Exception {
        TestRunner.run(suite());
    }

    public ReportConfigManager getRConfigManager() {
        if (null == rConfigManager) {
            String configFile = this.getTempletFilePath() + File.separator + "ReportConfigTest.xml";
            System.out.println(configFile);
            rConfigManager = new ReportConfigManager(configFile);
            assertNotNull(rConfigManager);
        }
        return rConfigManager;
    }

    private ReportConfigManager rConfigManager = null;

    public void testDataSourceConfig() {
        ReportConfigManager rcm = getRConfigManager();
        DataSourceConfig dsBillPartsConfig = rcm.getDataSource("dsBillParts");
        DataSourceConfig dsPartsConfig = rcm.getDataSource("partsSource");
        DataSourceConfig dsStaticConfigConfig = rcm.getDataSource("DS_STATIC");
        assertNotNull(dsBillPartsConfig);
        assertNotNull(dsPartsConfig);
        assertNotNull(dsStaticConfigConfig);
        assertTrue(dsBillPartsConfig.getDataSourceType().equals(DataSourceConfig.DS_TYPE_DATABASE));
        assertTrue(dsPartsConfig.getDataSourceType().equals(DataSourceConfig.DS_TYPE_DATABASE));
        assertTrue(dsStaticConfigConfig.getDataSourceType().equals(DataSourceConfig.DS_TYPE_STATIC));
        Map fields = dsBillPartsConfig.getFields();
        assertNotNull(fields);
        String fieldType = (String) fields.get("VPARTSCODE");
        assertTrue(fieldType.equals(Variable.LONG));
    }

    public void testReportConfig(ReportConfigManager rcm) {
    }
}
