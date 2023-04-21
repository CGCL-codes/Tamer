package org.monet.modelling.ide.builders;

public interface IGlobalData {

    public static final String PROJECT = "PROJECT";

    public static final String PROJECT_NAME = "PROJECT_NAME";

    public static final String PROJECT_PATH = "PROJECT_PATH";

    public static final String PROJECT_OUTPUT_PATH = "PROJECT_OUTPUT_PATH";

    public static final String PROJECT_DEFINITIONS_PATH = "PROJECT_DEFINITIONS_PATH";

    public static final String PROJECT_IMAGES_PATH = "PROJECT_IMAGES_PATH";

    public static final String PROJECT_BEHAVIOUR_PATH = "PROJECT_BEHAVIOUR_PATH";

    public static final String PROJECT_PROXIES_PATH = "PROJECT_PROXIES_PATH";

    public static final String PROJECT_CLIENT_SCRIPT_TEMPLATE = "PROJECT_CLIENT_SCRIPT_TEMPLATE";

    public static final String PROJECT_PROXIES_TEMPLATE = "PROJECT_PROXIES_TEMPLATE";

    public static final String PROJECT_BEHAVIOUR_TEMPLATE = "PROJECT_BEHAVIOUR_TEMPLATE";

    public static final String THEME_TEMPLATE = "SCRIPT_LIST_TEMPLATE";

    public static final String THEME_PATH = "THEME_PATH";

    public static final String HELP_PATH = "HELP_PATH";

    public static final String XSD_PATH = "XSD_PATH";

    public static final String DEFINITIONS_CHANDED = "DEFINITIONS_NO_CHANGED";

    <T> T getData(String key);

    void setData(String key, Object data);
}
