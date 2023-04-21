package org.slasoi.isslam.pac;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Beatriz Fuentes (TID)
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/org/slasoi/isslam/pac/context-TestNetworkMonitoring.xml" })
public class NetworkMonitoringTest {

    private static final Logger logger = Logger.getLogger(NetworkMonitoringTest.class.getName());

    private static int MAX_STEPS = 10;

    @Autowired
    org.slasoi.infrastructure.servicemanager.impl.InfrastructureImpl iServiceManager;

    @Autowired
    org.slasoi.isslam.pac.InfrastructureProvisioningAdjustment pac;

    @Autowired
    org.slasoi.isslam.pac.mockups.ContextMockup context;

    @Test
    public void execute() {
        int steps = 0;
        while (steps < MAX_STEPS) {
            try {
                Thread.sleep(1000);
                steps++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @After
    public void stop() {
        pac.stop();
    }
}
