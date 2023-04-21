package net.sf.tdsl.tests;

import static org.junit.Assert.*;
import org.junit.*;
import net.sf.tdsl.numeric.*;

public class NumericTest {

    BandwidthReduction bandwidthReduction = new BandwidthReduction();

    BigArithmetic bigArithmetic = new BigArithmetic();

    ConstrainedOptimization constrainedOptimization = new ConstrainedOptimization();

    Determinant determinant = new Determinant();

    DiscreteFourier discreteFourier = new DiscreteFourier();

    Factoring factoring = new Factoring();

    KnapsackProblem knapsackProblem = new KnapsackProblem();

    LinearEquations linearEquations = new LinearEquations();

    LinearProgramming linearProgramming = new LinearProgramming();

    MatrixMultiplication matrixMultiplication = new MatrixMultiplication();

    Permanent permanent = new Permanent();

    PrimalityTesting primalityTesting = new PrimalityTesting();

    RandomNumbers randomNumbers = new RandomNumbers();

    UnconstrainedOptimization unconstrainedOptimization = new UnconstrainedOptimization();

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testSorting() {
        assertTrue(true);
    }
}
