package org.openscience.cdk.applications.taverna.qsar.descriptors.bond;

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
public class BondPartialTChargeTest extends AbstractDescriptorTestCase {

    /**
	 * Instance of the tested processor
	 */
    private BondPartialTCharge processor;

    /**
	 * Constructor which instantiate the testing processor
	 */
    public BondPartialTChargeTest() {
        processor = new BondPartialTCharge();
        processorInputNames = processor.inputNames();
        processorOutputNames = processor.outputNames();
        processorName = processor.getClass().getSimpleName();
    }

    /**
	 * Method which returns a test suit with the name of this class
	 * 
	 * @return TestSuite
	 */
    public static Test suite() {
        return new TestSuite(BondPartialSigmaChargeTest.class);
    }

    /**
	 * Method which executes the test
	 * 
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
