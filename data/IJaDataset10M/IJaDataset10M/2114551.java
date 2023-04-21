package test.com.gestioni.adoc;

import test.com.agiletec.ConfigTestUtils;

public class AdocRepositoryConfigTestUtils extends ConfigTestUtils {

    protected String[] getSpringConfigFilePaths() {
        String[] filePaths = new String[6];
        filePaths[0] = "admin/test/testSystemConfig.xml";
        filePaths[1] = "WebContent/WEB-INF/conf/managers/**/**.xml";
        filePaths[2] = "WebContent/WEB-INF/adoc/conf/**/**/**.xml";
        filePaths[3] = "WebContent/WEB-INF/plugins/jptwitter/conf/**/**.xml";
        filePaths[4] = "WebContent/WEB-INF/plugins/jpopenoffice/conf/**/**.xml";
        filePaths[5] = "admin/test/adoctestRepositorySystemConfig.xml";
        return filePaths;
    }
}
