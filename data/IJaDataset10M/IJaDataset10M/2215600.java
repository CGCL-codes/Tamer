package org.dasein.cloud.openstack.swift;

import junit.framework.Test;
import org.dasein.cloud.test.ComprehensiveTestSuite;
import org.dasein.cloud.test.TestConfigurationException;

public class SwiftTestSuite {

    public static Test suite() throws TestConfigurationException {
        return new ComprehensiveTestSuite(Swift.class);
    }
}
