package org.maven.ide.eclipse.ext.support;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Information about the module related with Maven2 features</p>
 * 
 * @author Francesc Xavier Magdaleno
 * @version 1.0
 * @since 1.0
 */
public interface IJetty6Configuration {

    String getDriver();

    String getUser();

    String getPassword();

    String getUrl();

    String getHttpPort();

    String getScanInterval();

    String getStopPort();
}
