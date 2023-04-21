package org.apache.tools.ant.taskdefs.optional.net;

interface FTPTaskConfig {

    void log(String msg, int level);

    String getSystemTypeKey();

    String getDefaultDateFormatConfig();

    String getRecentDateFormatConfig();

    String getServerLanguageCodeConfig();

    String getServerTimeZoneConfig();

    String getShortMonthNamesConfig();
}
