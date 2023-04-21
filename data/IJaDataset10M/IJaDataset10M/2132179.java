package net.sf.etl.tests.term_parser.ej;

import net.sf.etl.tests.term_parser.TermStructureTestCase;

/**
 * The test that reads samples with basic event parser
 * 
 * @author const
 * 
 */
public class EJEventTests extends TermStructureTestCase {

    /**
	 * a test that reads entire contents of corresponding resource
	 */
    public void testAsyncTest() {
        readAllEJSample("AsyncTest.aej.etl");
    }

    /**
	 * a test that reads entire contents of corresponding resource
	 */
    public void testClassifiersClasses() {
        readAllEJSample("ClassifiersClasses.ej.etl");
    }

    /**
	 * a test that reads entire contents of corresponding resource
	 */
    public void testClassifiersEnums() {
        readAllEJSample("ClassifiersEnums.ej.etl");
    }

    /**
	 * a test that reads entire contents of corresponding resource
	 */
    public void testClassifiersInterfaces() {
        readAllEJSample("ClassifiersInterfaces.ej.etl");
    }

    /**
	 * a test that reads entire contents of corresponding resource
	 */
    public void testExpressionArithmeticOperators() {
        readAllEJSample("ExpressionArithmeticOperators.ej.etl");
    }

    /**
	 * a test that reads entire contents of corresponding resource
	 */
    public void testExpressionAssigmentOperators() {
        readAllEJSample("ExpressionAssigmentOperators.ej.etl");
    }

    /**
	 * a test that reads entire contents of corresponding resource
	 */
    public void testExpressionBiwiseAndLogicalOperators() {
        readAllEJSample("ExpressionBiwiseAndLogicalOperators.ej.etl");
    }

    /**
	 * a test that reads entire contents of corresponding resource
	 */
    public void testExpressionConditionalOperator() {
        readAllEJSample("ExpressionConditionalOperator.ej.etl");
    }

    /**
	 * a test that reads entire contents of corresponding resource
	 */
    public void testExpressionIncrementDecrement() {
        readAllEJSample("ExpressionIncrementDecrement.ej.etl");
    }

    /**
	 * a test that reads entire contents of corresponding resource
	 */
    public void testExpressionLiterals() {
        readAllEJSample("ExpressionLiterals.ej.etl");
    }

    /**
	 * a test that reads entire contents of corresponding resource
	 */
    public void testExpressionOtherPrimiaryAndAccess() {
        readAllEJSample("ExpressionOtherPrimiaryAndAccess.ej.etl");
    }

    /**
	 * a test that reads entire contents of corresponding resource
	 */
    public void testExpressionRelationalOperators() {
        readAllEJSample("ExpressionRelationalOperators.ej.etl");
    }

    /**
	 * a test that reads entire contents of corresponding resource
	 */
    public void testExpressionTypeOperators() {
        readAllEJSample("ExpressionTypeOperators.ej.etl");
    }

    /**
	 * a test that reads entire contents of corresponding resource
	 */
    public void testExpressionValueConstructors() {
        readAllEJSample("ExpressionValueConstructors.ej.etl");
    }

    /**
	 * a test that reads entire contents of corresponding resource
	 */
    public void testHelloWorld() {
        readAllEJSample("HelloWorld.ej.etl");
    }

    /**
	 * a test that reads entire contents of corresponding resource
	 */
    public void testMethodContentJumps() {
        readAllEJSample("MethodContentJumps.ej.etl");
    }

    /**
	 * a test that reads entire contents of corresponding resource
	 */
    public void testMethodContentLoops() {
        readAllEJSample("MethodContentLoops.ej.etl");
    }

    /**
	 * a test that reads entire contents of corresponding resource
	 */
    public void testMethodContentScopes() {
        readAllEJSample("MethodContentScopes.ej.etl");
    }

    /**
	 * a test that reads entire contents of corresponding resource
	 */
    public void testMethodContentSelectors() {
        readAllEJSample("MethodContentSelectors.ej.etl");
    }

    /**
	 * a test that reads entire contents of corresponding resource
	 */
    public void testNames() {
        readAllEJSample("Names.ej.etl");
    }

    /**
	 * a test that reads entire contents of corresponding resource
	 */
    public void testPackageNames() {
        readAllEJSample("PackageNames.ej.etl");
    }

    /**
	 * a test that reads entire contents of corresponding resource
	 */
    public void testTopLevelStatements() {
        readAllEJSample("TopLevelStatements.ej.etl");
    }

    /**
	 * a test that reads entire contents of corresponding resource
	 */
    public void testTypeNames() {
        readAllEJSample("TypeNames.ej.etl");
    }

    /**
	 * Read all tokens from resource with specified name
	 * 
	 * @param name
	 *            a name of resource
	 */
    private void readAllEJSample(String name) {
        final String resource = "/net/sf/etl/samples/ej/samples/" + name;
        readAllWithoutErrors(resource);
    }
}
