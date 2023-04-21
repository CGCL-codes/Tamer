package org.slasoi.infrastructure.servicemanager.exceptions;

import static org.junit.Assert.fail;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Exception thrown when there is a problem with resources.
 * 
 * @author Patrick Cheevers
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/org/slasoi/infrastructure/servicemanager/context-TestInfrastructureMockup.xml" })
public class ResourceNotAvailableExceptionTest {

    private static final String RESOURCE_EXCEPTION_TEST = "RESOURCE Exception Test";

    private static final Logger LOGGER = Logger.getLogger(ResourceNotAvailableExceptionTest.class.getName());

    @Test
    public void testthrow() {
        try {
            ResourceNotAvailableException exception = new ResourceNotAvailableException();
            throw exception;
        } catch (Exception e) {
            if (!(e instanceof ResourceNotAvailableException)) {
                LOGGER.error("Exception is not Instance of RESOURCEException");
                fail();
            }
        }
    }
}
