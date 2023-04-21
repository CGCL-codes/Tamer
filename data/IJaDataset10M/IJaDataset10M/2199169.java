package com.azaleait.asterion.action;

import java.util.Map;
import org.apache.log4j.Logger;

public class SecuredCommand {

    private static Logger logger = Logger.getLogger(SecuredCommand.class);

    private String name;

    private String url;

    private Map parameters;

    public SecuredCommand(final String newName, final String newUrl, final Map newParameters) {
        super();
        logger.debug("name=" + name);
        this.name = newName;
        this.url = newUrl;
        this.parameters = newParameters;
    }

    public String getName() {
        return name;
    }

    public Object getParameter(final String paramName) {
        return this.parameters.get(paramName);
    }

    public Map getParameters() {
        return parameters;
    }

    public String getUrl() {
        return url;
    }
}
