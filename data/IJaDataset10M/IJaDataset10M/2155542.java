package org.piuframework.config.xml.test;

import org.piuframework.config.xml.XMLConfigModule;

/**
 * TODO
 * 
 * @author Dirk Mascher, Accelsis Technologies GmbH
 */
public class DefaultTestXMLConfigModule extends XMLConfigModule {

    private static final long serialVersionUID = -7493027232922527709L;

    public static final String CONFIG_MODULE_NAME = "test-config-testcase";

    public static final String CONFIG_MODULE_PATH = "/org/piuframework/config/xml/test-config-testcase.cfg.xml";

    public static final String CONFIG_MODULE_PARSER = "org.piuframework.config.xml.test.XMLConfigModuleParser";

    public static final DefaultTestXMLConfigModule instance = new DefaultTestXMLConfigModule();

    private DefaultTestXMLConfigModule() {
        setName(CONFIG_MODULE_NAME);
        setPath(CONFIG_MODULE_PATH);
        setParserClassName(CONFIG_MODULE_PARSER);
        setModifiableFlag("true");
    }

    public static DefaultTestXMLConfigModule getInstance() {
        return instance;
    }
}
