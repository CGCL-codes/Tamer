package org.jcuefile.config;

public class Constants {

    private static final String PROJECT_NAME = "JCueFile Creator B�ta";

    private static final double PROJECT_VERSION = 1.0;

    private static final int PROJECT_BUILD = 1;

    public static final String getProjectName() {
        return PROJECT_NAME;
    }

    public static final String getProjectVersion() {
        return PROJECT_VERSION + "." + PROJECT_BUILD;
    }
}
