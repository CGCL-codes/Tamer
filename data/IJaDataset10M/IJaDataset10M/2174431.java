package toxTree.test.tree.rules;

import junit.framework.TestCase;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.templates.MoleculeFactory;
import toxTree.exceptions.DecisionMethodException;
import toxTree.tree.rules.RuleElements;

/**
 * TODO add description
 * @author Vedina
 * <b>Modified</b> 2005-8-19
 */
public class RuleElementsTest extends TestCase {

    public static void main(String[] args) {
        junit.textui.TestRunner.run(RuleElementsTest.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
	 * Constructor for RuleElementsTest.
	 * @param arg0
	 */
    public RuleElementsTest(String arg0) {
        super(arg0);
    }

    public void testRule() {
        RuleElements rule = new RuleElements();
        rule.setComparisonMode(RuleElements.modeOnlySpecifiedElements);
        rule.addElement("C");
        IMolecule mol = MoleculeFactory.makeAlkane(20);
        try {
            assertTrue(rule.verifyRule(mol));
        } catch (DecisionMethodException x) {
            fail();
        }
        SmilesParser sp = new SmilesParser(SilentChemObjectBuilder.getInstance());
        try {
            mol = sp.parseSmiles("CC=O");
        } catch (InvalidSmilesException x) {
            fail();
        }
        try {
            assertFalse(rule.verifyRule(mol));
            rule.setComparisonMode(RuleElements.modeAnySpecifiedElements);
            assertTrue(rule.verifyRule(mol));
            rule.addElement("O");
            assertTrue(rule.verifyRule(mol));
            rule.setComparisonMode(RuleElements.modeOnlySpecifiedElements);
            assertTrue(rule.verifyRule(mol));
        } catch (DecisionMethodException x) {
            fail();
        }
    }
}
