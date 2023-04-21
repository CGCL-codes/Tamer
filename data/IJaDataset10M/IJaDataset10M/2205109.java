package openep.junit;

import openep.logger.EpLogger;
import openep.logger.EpLoggerFactory;
import openep.utils.EpRuntimeUtils;
import openep.utils.IPrefixProperties;
import org.junit.Before;
import org.junit.Test;
import com.groiss.component.ServiceManager;
import junit.framework.TestCase;

public class EpTestCase extends TestCase {

    public static final String AVWCONFFILE_SYSPROPERTY = "avwconffile";

    EpLogger LOGGER = EpLoggerFactory.getLogger(EpTestCase.class);

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Before
    public void setupEp() throws Exception {
        log(" start   ep ");
        if (EpRuntimeUtils.isEpRunning() == false) {
            String propertyPrefix = System.getProperties().getProperty(IPrefixProperties.PROPERTYPREFIX);
            if (propertyPrefix == null) {
                propertyPrefix = "";
            } else {
                propertyPrefix = "." + propertyPrefix;
            }
            String avwConf = "./conf/avw.conf.local";
            System.out.println("props" + System.getProperties());
            String avwConfVal = System.getProperties().getProperty(AVWCONFFILE_SYSPROPERTY);
            log("value of " + AVWCONFFILE_SYSPROPERTY + ":" + avwConfVal);
            if (avwConfVal != null) {
                avwConf = avwConfVal;
            } else {
                log("no value of avw-conf found, taking default. Use launcher or -Davwconffile=xxx  to set the conf file.");
                avwConf = "c:/srv/ep70/conf/avw.conf.local";
            }
            log("taking conf file to start enterprise : " + avwConf);
            ServiceManager.main(avwConf, true);
            log(" ");
            log(" ------------------->   ENTERPRISE STARTED");
            log(" ");
        }
    }

    public void log(String message) {
        System.out.println(message);
        LOGGER.debug(message);
    }
}
