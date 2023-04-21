package com.sodad.weka.gui.beans;

/**
 * Marker interface for components that can share their configuration.
 * 
 * @author Mark Hall (mhall{[at]}pentaho{[dot]}org)
 * @version $Revision $
 */
public interface ConfigurationProducer {

    /**
   * We don't have to keep track of configuration listeners (see the
   * documentation for ConfigurationListener/ConfigurationEvent).
   * 
   * @param cl a ConfigurationListener.
   */
    void addConfigurationListener(ConfigurationListener cl);

    /**
   * We don't have to keep track of configuration listeners (see the
   * documentation for ConfigurationListener/ConfigurationEvent).
   * 
   * @param cl a ConfigurationListener.
   */
    void removeConfigurationListener(ConfigurationListener cl);
}
