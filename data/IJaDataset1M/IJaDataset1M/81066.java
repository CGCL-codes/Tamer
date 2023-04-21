package org.dasein.cloud.jclouds.gogrid;

import junit.framework.Test;
import org.dasein.cloud.test.ComprehensiveTestSuite;
import org.dasein.cloud.test.TestConfigurationException;

public class GoGridTestSuite {

    public static Test suite() throws TestConfigurationException {
        return new ComprehensiveTestSuite(GoGrid.class);
    }
}
