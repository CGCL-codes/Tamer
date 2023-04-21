package ru.ti.util;

/**
 *
 * @author msa
 */
public interface AppContext {

    public static final String WEB_APP_HOME = "web.app.home";

    public static final String TOMCAT_HOME = "tomcat.home";

    public static final String PROJECTS_HOME = "projects.home";

    public String getProperty(String name) throws Exception;

    public void init() throws Exception;

    public void init(String appHome) throws Exception;
}
