package org.exist.config;

import java.util.List;

/**
 * @author <a href="mailto:shabanovd@gmail.com">Dmitriy Shabanov</a>
 *
 */
public interface ConfigElement {

    public ConfigElement getConfiguration(String name);

    public List<ConfigElement> getConfigurations(String name);

    public List<String> getProperties();

    public String getProperty(String name);

    public Integer getPropertyInteger(String name);

    public Boolean getPropertyBoolean(String name);
}
