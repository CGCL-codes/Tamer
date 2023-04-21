package com.mycila.jms;

import java.util.Collection;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface JMSMetaData {

    String getJMSVersion();

    String getJMSMinorVersion();

    String getJMSMajorVersion();

    String getJMSProviderName();

    String getProviderVersion();

    String getProviderMajorVersion();

    String getProviderMinorVersion();

    Collection<String> getJMSXPropertyNames();
}
