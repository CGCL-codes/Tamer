package org.systemsbiology.apps.gui;

import junit.framework.TestCase;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.systemsbiology.apps.gui.server.WebappConstants;
import org.systemsbiology.apps.gui.server.job.IJob;
import org.systemsbiology.apps.gui.server.job.PBSJobManager;

public class BaseTestCase extends TestCase {

    protected static Logger log = Logger.getLogger(BaseTestCase.class.getName());

    static {
        BasicConfigurator.configure();
        WebappConstants.SRM_WEBAPP_PROPS = "test_resources/conf/srm_webapp.properties";
    }

    public BaseTestCase() {
    }

    protected PBSJobManagerWrapper manager = new PBSJobManagerWrapper();

    protected static class PBSJobManagerWrapper extends PBSJobManager {

        public String getJobScriptContents(IJob j) {
            return (super.getJobScriptContents(j));
        }
    }
}
