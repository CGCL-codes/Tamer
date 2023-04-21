package org.piuframework.service.config.xml;

import org.piuframework.config.xml.XMLConfigModule;

/**
 * TODO
 * 
 * @author Dirk Mascher, Accelsis Technologies GmbH
 */
public class DefaultXMLConfigModule extends XMLConfigModule {

    private static final long serialVersionUID = 5505683632397322521L;

    public static final String CONFIG_MODULE_PATH = "/piu-service.cfg.xml";

    public static final String CONFIG_MODULE_PARSER = XMLConfigModuleParser.class.getName();

    public DefaultXMLConfigModule() {
        this("");
    }

    public DefaultXMLConfigModule(String pathPrefix) {
        setPath(pathPrefix + CONFIG_MODULE_PATH);
        setParserClassName(CONFIG_MODULE_PARSER);
        setModifiableFlag("true");
    }
}
