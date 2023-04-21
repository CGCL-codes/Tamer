package test.org.mandarax.reference;

import org.mandarax.kernel.InferenceEngine;
import org.mandarax.kernel.KnowledgeBase;
import org.mandarax.kernel.Query;

/**
 * Simple NAF test.
 * @author <A href="http://www-ist.massey.ac.nz/JBDietrich" target="_top">Jens Dietrich</A>
 * @version 3.4 <7 March 05>
 * @since 2.0
 */
public class TestInferenceEngineNAF3 extends TestInferenceEngineNAF {

    /**
     * Constructor.
     * @param aKnowledgeBase a new, uninitialized knowledge base that will be used
     * @param anInferenceEngine the inference engine that will be tested
     */
    public TestInferenceEngineNAF3(KnowledgeBase aKnowledgeBase, InferenceEngine anInferenceEngine) {
        super(aKnowledgeBase, anInferenceEngine);
    }

    /**
     * Add facts and rules to the knowledge base.
     * @param knowledge org.mandarax.kernel.KnowledgeBase
     */
    public void feedKnowledgeBase(KnowledgeBase knowledge) {
        knowledge.removeAll();
        knowledge.add(lfs.rule(lfs.prereq(Q, lfs.variable("x"), false), lfs.prereq(P, lfs.variable("x"), true), lfs.fact(R, lfs.variable("x"))));
        knowledge.add(lfs.fact(Q, "a"));
        knowledge.add(lfs.fact(P, "a"));
    }

    /**
     * Get a description of this test case.
     * This is used by the <code>org.mandarax.demo</code>
     * package to display the test cases.
     * @return a brief description of the test case
     */
    public String getDescription() {
        return "Test for NAF";
    }

    /**
     * Get the expected result.
     * @return a string
     */
    public String getExpected() {
        return null;
    }

    /**
     * Get the query.
     * @return a query
     */
    public Query getQuery() {
        return lfs.query(lfs.fact(R, lfs.variable(QUERY_VARIABLE)), "?R(x)");
    }
}
