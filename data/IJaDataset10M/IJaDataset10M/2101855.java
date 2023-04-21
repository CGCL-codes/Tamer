package com.liferay.portal.kernel.configuration;

import java.util.Properties;

/**
 * <a href="Configuration.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public interface Configuration {

    public void addProperties(Properties properties);

    public boolean contains(String key);

    public String get(String key);

    public String get(String key, Filter filter);

    public String[] getArray(String key);

    public String[] getArray(String key, Filter filter);

    public Properties getProperties();

    public void removeProperties(Properties properties);

    public void set(String key, String value);
}
