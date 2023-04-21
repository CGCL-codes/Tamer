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
public class DistanceToAtomTest extends AbstractDescriptorTestCase {

    /**
	 * Instance of the tested processor 
	 */
    private DistanceToAtom processor;

    /**
	 * Constructor which instantiate the testing processor 
	 */
    public DistanceToAtomTest() {
        processor = new DistanceToAtom();
        processorInputNames = processor.inputNames();
        processorOutputNames = processor.outputNames();
        processorName = processor.getClass().getSimpleName();
        testDataWith3DCoordinates = true;
    }

    /**
	 * Method which returns a test suit with the name of this class
	 * @return TestSuite
	 */
    public static Test suite() {
        return new TestSuite(DistanceToAtomTest.class);
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
