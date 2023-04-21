package gov.sns.tools.math.tests;

import java.util.EnumSet;
import gov.sns.tools.math.r3.R3x3;
import gov.sns.tools.math.r3.R3x3.Position;
import org.junit.Before;
import org.junit.Test;
import junit.framework.JUnit4TestAdapter;

/**
 * Class for performing JUnit 4.x test on the <code>R3x3</code> class
 * in the <code>gov.sns.tools.math</code> package.
 * 
 * @author Christopher K. Allen
 *
 */
public class TestR3x3 {

    /** solution precision */
    public static final double ERROR_TOLERANCE = 1.0e-0;

    /** Schema attributes */
    public static final String STR_TEST_MATRIX_1 = "{{1,2,3},{4,5,6},{7,8,9}}";

    /** test matrix */
    private R3x3 matTest;

    /**
     * Return a JUnit 3.x version <code>Test</code> instance that encapsulates this
     * test suite.  This is a convenience method for attaching to old JUnit testing
     * frameworks, for example, using Eclipse.
     * 
     * @return  a JUnit 3.8 type test object adaptor
     */
    public static junit.framework.Test getJUnitTest() {
        return new JUnit4TestAdapter(TestR3x3.class);
    }

    /**
     *  Create a new <code>TestR3x3JacobiDecomposition</code> class for 
     *  JUnit 4.x testing of the <code>TableSchema</code> class. 
     */
    public TestR3x3() {
        super();
    }

    /**
     * Setup the test fixture by creating a the test matrices.
     */
    @Before
    public void setup() {
        this.matTest = R3x3.parse(STR_TEST_MATRIX_1);
    }

    /**
     * Print out the <code>Index</code> enumeration.
     */
    @Test
    public void printPositions() {
        System.out.println("Index Enumeration");
        System.out.println("All Elements");
        for (Index pos : Index.values()) {
            System.out.println("Index " + pos + "(row,col) = (" + pos.row() + "," + pos.col() + ")");
        }
        System.out.print("Diagonal Elements");
        this.outputPositions(Index.getDiagonal());
        System.out.println();
        System.out.print("Upper Triangular Elements");
        this.outputPositions(Index.getUpperTriangle());
        System.out.println();
        System.out.print("Lower Triangular Elements");
        this.outputPositions(Index.getLowerTriangle());
        System.out.println();
        System.out.print("All Off-Diagonal Elements");
        this.outputPositions(Index.getOffDiagonal());
        System.out.println();
    }

    /**
     * Print out the matrix elements using the <code>Index</code>
     * enumeration.
     */
    @Test
    public void printMatrixByPosition() {
        System.out.println("Matrix Elements By Index");
        for (Index pos : Index.values()) {
            System.out.println("Matrix element " + pos + " = " + this.matTest.getElem(pos));
        }
    }

    /**
     * Print out the matrix using the <code>toString</code> method of the 
     * matrix.
     */
    @Test
    public void printMatrix() {
        System.out.println(this.matTest.toString());
    }

    /**
     * Prints out the <code>Index</code> enumeration value in the
     * given enumeration set.
     * 
     * @param   setPos      set of positions to print to console
     */
    private void outputPositions(EnumSet<Index> setPos) {
        System.out.print("{");
        for (Index pos : setPos) {
            System.out.print("," + pos);
        }
        System.out.print("}");
    }
}
