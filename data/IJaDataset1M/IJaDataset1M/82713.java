package org.jbjf.plugin.util;

import java.util.HashMap;
import java.util.Iterator;
import org.jbjf.plugin.IJBJFPlugin;
import org.jbjf.util.APILog4j;

public interface PluginService {

    public Iterator<IJBJFPlugin> getPlugins();

    public void initPluginService(APILog4j batchLog);

    public void setLog(APILog4j batchLog);

    public APILog4j getLog();
}
