package com.jspx.txweb.config;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User:chenYuan (mail:cayurain@21cn.com)
 * Date: 2007-1-5
 * Time: 15:04:23
 * 
 */
public interface Configuration {

    void setFileName(String fileName);

    String getFileName();

    Map<String, Map<String, ActionConfigBean>> loadConfig() throws Exception;
}
