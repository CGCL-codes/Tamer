package org.slasoi.infrastructure.servicemanager;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.slasoi.infrastructure.servicemanager.exceptions.DescriptorException;
import org.slasoi.infrastructure.servicemanager.exceptions.ProvisionException;
import org.slasoi.infrastructure.servicemanager.exceptions.UnknownIdException;
import org.slasoi.infrastructure.servicemanager.types.EndPoint;
import org.slasoi.infrastructure.servicemanager.types.Images;
import org.slasoi.infrastructure.servicemanager.types.Locations;
import org.slasoi.infrastructure.servicemanager.types.ProvisionRequestType;
import org.slasoi.infrastructure.servicemanager.types.ProvisionResponseType;
import org.slasoi.infrastructure.servicemanager.types.ReservationResponseType;
import org.slasoi.infrastructure.servicemanager.types.Slas;
import eu.slasoi.infrastructure.model.Category;
import eu.slasoi.infrastructure.model.infrastructure.Compute;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/org/slasoi/infrastructure/servicemanager/context-TestInfrastructureMockup.xml" })
public class MultipleProvisionMultiHeaderTest {

    private static String notificationURI = "adjustment@testbed.sla-at-soi.eu";

    Logger logger = Logger.getLogger(MultipleProvisionMultiHeaderTest.class.getName());

    @Autowired
    org.slasoi.infrastructure.servicemanager.impl.InfrastructureImpl infrastructure;

    private static String id = "e0cb4eb02f47";

    private static String metricName = "cpu";

    private static int minutes = 2;

    private String hostname = "it";

    private String monitoringRequest = "http://testbed.sla-at-soi.eu/monitoring/request.xml";

    private Random rnd = new Random(System.currentTimeMillis());

    private static List<ProvisionResponseType> infrastructures = new ArrayList<ProvisionResponseType>();

    private int numOfVMs = 2;

    @Test
    public void testMultipleComputeProvision() {
        Set<Compute> computeConfigurations = new HashSet<Compute>();
        for (int i = 0; i < numOfVMs; i++) {
            Compute vmConfiguration = createComputeConfiguraton1();
            computeConfigurations.add(vmConfiguration);
            logger.info(vmConfiguration);
        }
        try {
            ProvisionRequestType provisionRequestType = infrastructure.createProvisionRequestType(computeConfigurations, notificationURI);
            logger.info(provisionRequestType);
            ProvisionResponseType provisionResponseType = infrastructure.provision(provisionRequestType);
            assertNotNull(provisionResponseType);
            assertNotNull(provisionResponseType.getInfrastructureID());
            logger.info(provisionResponseType);
            if (provisionResponseType.getEndPoints().size() == 0) {
                fail();
            }
            logger.info("provision - infrastructureID - " + provisionResponseType.getInfrastructureID());
            List<EndPoint> endPoints = provisionResponseType.getEndPoints();
            assertNotNull(endPoints);
            for (Iterator<EndPoint> iterator = endPoints.iterator(); iterator.hasNext(); ) {
                EndPoint endPoint = iterator.next();
                logger.info("	EndPoint - getHostName - " + endPoint.getHostName());
                logger.info("	EndPoint - getResourceUrl - " + endPoint.getResourceUrl());
            }
            infrastructures.add(provisionResponseType);
        } catch (DescriptorException e) {
            e.printStackTrace();
            fail();
        } catch (ProvisionException e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testPredictionQuery() {
        assertNotNull(infrastructure);
        String result = null;
        try {
            result = infrastructure.predictionQuery(id, metricName, minutes);
            logger.info(result);
        } catch (UnknownIdException e) {
            e.printStackTrace();
            logger.error(e);
            fail();
        }
        assertNotNull(result);
    }

    @Test
    public void testStartResource() {
        assertNotNull(infrastructure);
        if (infrastructures.size() == 0) {
        }
        {
            for (Iterator<ProvisionResponseType> iterator = infrastructures.iterator(); iterator.hasNext(); ) {
                ProvisionResponseType type = iterator.next();
                logger.info(type.getInfrastructureID());
                List<EndPoint> endPoints = type.getEndPoints();
                for (Iterator<EndPoint> iterator2 = endPoints.iterator(); iterator2.hasNext(); ) {
                    EndPoint endPoint = iterator2.next();
                    try {
                        infrastructure.startResource(endPoint.getResourceUrl().getPath());
                    } catch (UnknownIdException e) {
                        e.printStackTrace();
                        logger.info("infrastructure - " + infrastructure);
                        logger.error(e);
                    }
                }
            }
        }
    }

    @Test
    public void testStopResource() {
        assertNotNull(infrastructure);
        if (infrastructures.size() == 0) {
        }
        {
            for (Iterator<ProvisionResponseType> iterator = infrastructures.iterator(); iterator.hasNext(); ) {
                ProvisionResponseType type = iterator.next();
                logger.info(type.getInfrastructureID());
                List<EndPoint> endPoints = type.getEndPoints();
                for (Iterator<EndPoint> iterator2 = endPoints.iterator(); iterator2.hasNext(); ) {
                    EndPoint endPoint = iterator2.next();
                    try {
                        infrastructure.stopResource(endPoint.getResourceUrl().getPath());
                    } catch (UnknownIdException e) {
                        e.printStackTrace();
                        logger.error(e);
                    }
                }
            }
        }
    }

    private Compute createComputeConfiguraton1() {
        return infrastructure.createComputeConfiguration(Images.Ubuntu_9_10_32, Slas.GOLD, Locations.IE, 2, 1.0f, 512, monitoringRequest, hostname + Integer.toString(rnd.nextInt(100000)), notificationURI);
    }
}
