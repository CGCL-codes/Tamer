package weka.attributeSelection;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests BestFirst. Run from the command line with:<p/>
 * java weka.attributeSelection.WrapperSubsetEvalTest
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision: 1.3 $
 */
public class WrapperSubsetEvalTest extends AbstractEvaluatorTest {

    public WrapperSubsetEvalTest(String name) {
        super(name);
    }

    /** Creates a default BestFirst */
    public ASSearch getSearch() {
        return new GreedyStepwise();
    }

    /** Creates a WrapperSubsetEval with J48 */
    public ASEvaluation getEvaluator() {
        WrapperSubsetEval eval = new WrapperSubsetEval();
        eval.setClassifier(new weka.classifiers.trees.J48());
        return eval;
    }

    public static Test suite() {
        return new TestSuite(WrapperSubsetEvalTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
