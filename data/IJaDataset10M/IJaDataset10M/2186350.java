package test.de.offis.semanticmm4u.compositors.basics;

import junit.framework.TestCase;
import de.offis.semanticmm4u.compositors.variables.AbstractVariable;
import de.offis.semanticmm4u.compositors.variables.VariableList;
import de.offis.semanticmm4u.compositors.variables.media.Text;
import de.offis.semanticmm4u.compositors.variables.operators.basics.temporal.Delay;

/**
 * JUnit TestCase.
 * @testfamily JUnit
 * @testkind testcase
 * @testsetup Default TestCase
 * @testedclass de.offis.semanticmm4u.compositors.basics.VariableList
 */
public class TestVariableList extends TestCase {

    VariableList variables = null;

    /** Constructs a test case with the given name. */
    public TestVariableList(String name) {
        super(name);
    }

    /**
     * Sets up the fixture, for example, open a network connection.
     * This method is called before a test is executed.
     */
    @Override
    protected void setUp() {
        variables = new VariableList();
        variables.add(new Text("bla", 300, 20));
        variables.add(new Text("bla", 50, 20));
        assertEquals(variables.size(), 2);
    }

    /**
     * Tears down the fixture, for example, close a network connection.
     * This method is called after a test is executed.
     */
    @Override
    protected void tearDown() {
        variables = null;
    }

    public void testVariableList() {
        Delay var1 = new Delay(50);
        Delay var2 = new Delay(100);
        Delay var3 = new Delay(150);
        VariableList tempVariableList = new VariableList(new AbstractVariable[] { var1, var2, var3 });
        assertEquals(tempVariableList.size(), 3);
        Delay tempVar3 = (Delay) tempVariableList.elementAt(2);
        assertEquals(tempVar3.getDelay(), 150);
    }

    public void testAddVariable() {
        assertEquals(variables.size(), 2);
        variables.add(new Text("fasel", 100, 10));
        assertEquals(variables.size(), 3);
    }

    public void testAddAllVariables() {
        assertEquals(variables.size(), 2);
        VariableList tempVariableList = new VariableList();
        tempVariableList.add(new Text("kekse", 50, 10));
        tempVariableList.add(new Text("fabrik", 50, 10));
        tempVariableList.add(new Text("kaufen", 50, 10));
        variables.addAll(tempVariableList);
        assertEquals(variables.size(), 5);
    }
}
