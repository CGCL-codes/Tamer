package org.dasein.cloud.jclouds.terremark;

import junit.framework.Test;
import org.dasein.cloud.test.ComprehensiveTestSuite;
import org.dasein.cloud.test.TestConfigurationException;

public class TerremarkTestSuite {

    public static Test suite() throws TestConfigurationException {
        return new ComprehensiveTestSuite(Terremark.class);
    }
}
