package org.openscience.cdk.applications.taverna.qsar.descriptors.atomic;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.openscience.cdk.applications.taverna.qsar.AbstractDescriptorTestCase;
import org.openscience.cdk.exception.CDKException;

/**
 * Class with contains JUnit-Tests for the CDK-Taverna Project
 * 
 * @author Thomas Kuhn
 * 
 */
public class PartialPiChargeTest extends AbstractDescriptorTestCase {

    /**
	 * Instance of the tested processor 
	 */
    private PartialPiCharge processor;

    /**
	 * Constructor which instantiate the testing processor 
	 */
    public PartialPiChargeTest() {
        processor = new PartialPiCharge();
        processorInputNames = processor.inputNames();
        processorOutputNames = processor.outputNames();
        processorName = processor.getClass().getSimpleName();
    }

    /**
	 * Method which returns a test suit with the name of this class
	 * @return TestSuite
	 */
    public static Test suite() {
        return new TestSuite(PartialPiChargeTest.class);
    }

    /**
	 * Method which executes the test
	 * @throws CDKException
	 * @throws Exception
	 */
    public void test_LocalWorker() throws CDKException, Exception {
        executeTest();
    }

    protected void executeProcessor() throws Exception {
        resultMap = processor.execute(inputMap);
    }
}
