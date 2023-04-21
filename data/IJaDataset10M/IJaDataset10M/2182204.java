package org.xaware.functoids;

import org.xaware.testing.util.BaseBdpTestCase;

/**
 * Class to test functoid execution for nested functoids and escaping the
 * functoid specific characters : $ ( , ) .
 * @author satish
 *
 */
public class TestXAFunctoids extends BaseBdpTestCase {

    /**Constructor with the name parameter.*/
    public TestXAFunctoids(String name) {
        super(name);
    }

    /**returns the data folder containing the test files. */
    protected String getDataFolder() {
        return "data/org/xaware/functoids/";
    }

    /**
	  * Test the Add 2 numbers functoid with parameters as two Add2Numbers functoids.
	  * 
	  */
    public void testAdd2NumNestedFunctoid() {
        clearInputParams();
        setInputXmlFileName(null);
        setBizDocFileName("test_add_2_num_nested_functoid.xbd");
        setExpectedOutputFileName("test_expected_add_2_num_nested_functoid.xml");
        getTestHelper().setTestMethodName("testAdd2NumNestedFunctoid");
        evaluateBizDoc();
    }

    /**
	  * Test the Functoid execution with the data containing '$' encoded as '$'
	  */
    public void testFunctoidWithEncodedData() {
        clearInputParams();
        setInputXmlFileName(null);
        setBizDocFileName("test_append_strings_data_encoded_functoid.xbd");
        setExpectedOutputFileName("test_expected_append_strings_data_encoded_functoid.xml");
        getTestHelper().setTestMethodName("testFunctoidWithEncodedData");
        evaluateBizDoc();
    }

    /**
	  * Test case for testing multiple arithmetic operations using nested functoid execution.
	  * 
	  */
    public void testNestedFunctoidWithMoreOperations() {
        clearInputParams();
        setInputXmlFileName(null);
        setBizDocFileName("test_nested_functoid_with_more_operations.xbd");
        setExpectedOutputFileName("test_nested_functoid_with_more_operations_expected.xml");
        getTestHelper().setTestMethodName("testNestedFunctoidWithMoreOperations");
        evaluateBizDoc();
    }
}
