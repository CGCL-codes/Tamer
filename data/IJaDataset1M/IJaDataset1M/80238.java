package test.org.mandarax.reference;

import org.mandarax.kernel.InferenceEngine;
import org.mandarax.kernel.InferenceException;
import org.mandarax.kernel.KnowledgeBase;
import org.mandarax.kernel.ResultSet;

/**
 * Test case for testing what happens when we attempt to fetch results and there 
 * are no more results available.
 * @author <A href="http://www-ist.massey.ac.nz/JBDietrich" target="_top">Jens Dietrich</A>
 * @version 3.4 <7 March 05>
 * @since 1.7
 */
public class TestResultSet7 extends TestResultSet {

    /**
	 * Constructor.
	 * @param kb a new, uninitialized knowledge base that will be used
	 * @param ie the inference engine that will be tested
	 */
    public TestResultSet7(KnowledgeBase kb, InferenceEngine ie) {
        super(kb, ie);
    }

    /**
	 * Get a description of this test case.
	 * This is used by the <code>org.mandarax.demo</code>
	 * package to display the test cases.
	 * @return a brief description of the test case
	 */
    public String getDescription() {
        return "Test ResultSet";
    }

    /**
	 * Test the result set. Note that the result set is uninitialized (nothing has been fetched).
	 * @param rs the result set
	 * @return false if the test fails and true otherwise
	 */
    public boolean testResultSet(ResultSet rs) {
        try {
            rs.next();
            rs.next();
            rs.getResult(Person.class, "x");
        } catch (InferenceException x) {
            return true;
        }
        return false;
    }
}
