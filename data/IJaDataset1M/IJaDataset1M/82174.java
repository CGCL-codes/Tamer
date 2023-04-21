package com.smb.framework.web.action;

import java.io.FileInputStream;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ReadActionConfigFile {

    private static Log logger = LogFactory.getLog(ReadActionConfigFile.class);

    private static String controllerConfigFile = ReadActionConfigFile.class.getResource("/ActionsMapping.properties").getFile();

    public static Properties getControllerConfigFile() {
        Properties prop = new Properties();
        try {
            if (logger.isDebugEnabled()) {
                logger.info("getControllerConfigFile ............ ");
            }
            prop.load(new FileInputStream(controllerConfigFile));
        } catch (Exception e) {
            logger.error(e);
        }
        return prop;
    }
}
