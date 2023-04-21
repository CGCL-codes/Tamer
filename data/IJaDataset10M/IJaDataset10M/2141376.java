package com.hs.framework.db.jdbc;

import java.io.File;
import com.hs.framework.common.util.LogUtil;
import com.hs.framework.common.util.config.DOM4JConfiguration;

final class DateOptions {

    private static final String OPTION_FILE_NAME = "jdbc.xml";

    int serverHourOffset = 0;

    DateOptions() {
        try {
            String strPathName = FileUtil.getServletClassesPath();
            String configFilename = strPathName + OPTION_FILE_NAME;
            DOM4JConfiguration conf = new DOM4JConfiguration(new File(configFilename));
            serverHourOffset = conf.getInt("dateoptions.server_hour_offset", serverHourOffset);
        } catch (Exception e) {
            String message = "net.myvietnam.mvncore.util.ParamOptions: Can't read the configuration file: '" + OPTION_FILE_NAME + "'. Make sure the file is in your CLASSPATH";
            LogUtil.getLogger().error(message, e);
        }
    }
}
