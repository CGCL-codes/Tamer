package org.xaware.ide.xadev.datamodel;

import org.xaware.server.engine.exceptions.XAwareConfigurationException;

/**
 * @author tferguson
 *
 */
public interface IBizDriverInfo {

    public void testConnectivity() throws XAwareConfigurationException;
}
