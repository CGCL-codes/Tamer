package jopt.csp.test.search;

import jopt.csp.search.SearchAction;
import jopt.csp.spi.SolverImpl;
import jopt.csp.spi.search.actions.GenerateDoubleAction;
import jopt.csp.spi.search.technique.BreadthFirstSearch;
import jopt.csp.spi.search.technique.DepthFirstSearch;
import jopt.csp.spi.search.technique.TreeSearch;
import jopt.csp.spi.search.tree.BasicSearchNode;
import jopt.csp.spi.search.tree.CrawlingSearchTree;
import jopt.csp.spi.search.tree.DeltaStateManager;
import jopt.csp.spi.search.tree.JumpingSearchTree;
import jopt.csp.spi.search.tree.ProblemStateManager;
import jopt.csp.spi.search.tree.SearchTree;
import jopt.csp.spi.solver.ConstraintStore;
import jopt.csp.variable.CspDoubleVariable;
import jopt.csp.variable.CspVariableFactory;
import jopt.csp.variable.PropagationFailureException;
import junit.framework.TestCase;

/**
 * Basic test of depth first searching
 */
public class SearchRealTest extends TestCase {

    private ConstraintStore store;

    private CspVariableFactory varFactory;

    private CspDoubleVariable x;

    private CspDoubleVariable y;

    private CspDoubleVariable z;

    public SearchRealTest(String testName) {
        super(testName);
    }

    private int doTestSearch(TreeSearch search) {
        boolean solFound = search.nextSolution();
        int solCount = 0;
        while (solFound) {
            solCount++;
            solFound = search.nextSolution();
        }
        return solCount;
    }

    public int doDFSRecalculatingTree(double precision) {
        SearchAction genVars = new GenerateDoubleAction(new CspDoubleVariable[] { x, y, z }, precision);
        TreeSearch search = new TreeSearch(store, genVars, new DepthFirstSearch());
        return doTestSearch(search);
    }

    public int doDFSProblemStateTree(double precision) {
        SearchAction genVars = new GenerateDoubleAction(new CspDoubleVariable[] { x, y, z }, precision);
        SearchTree tree = new CrawlingSearchTree(new BasicSearchNode(genVars), new ProblemStateManager(store));
        TreeSearch search = new TreeSearch(tree, new DepthFirstSearch());
        return doTestSearch(search);
    }

    public int doDFSDeltaStateTree(double precision) {
        SearchAction genVars = new GenerateDoubleAction(new CspDoubleVariable[] { x, y, z }, precision);
        SearchTree tree = new CrawlingSearchTree(new BasicSearchNode(genVars), new DeltaStateManager(store));
        TreeSearch search = new TreeSearch(tree, new DepthFirstSearch());
        return doTestSearch(search);
    }

    public int doDFSJumpingTree(double precision) {
        SearchAction genVars = new GenerateDoubleAction(new CspDoubleVariable[] { x, y, z }, precision);
        SearchTree tree = new JumpingSearchTree(new BasicSearchNode(genVars), new ProblemStateManager(store));
        TreeSearch search = new TreeSearch(tree, new DepthFirstSearch());
        return doTestSearch(search);
    }

    public int doBFSRecalculatingTree(double precision) {
        SearchAction genVars = new GenerateDoubleAction(new CspDoubleVariable[] { x, y, z }, precision);
        TreeSearch search = new TreeSearch(store, genVars, new BreadthFirstSearch());
        return doTestSearch(search);
    }

    public int doBFSProblemStateTree(double precision) {
        SearchAction genVars = new GenerateDoubleAction(new CspDoubleVariable[] { x, y, z }, precision);
        SearchTree tree = new CrawlingSearchTree(new BasicSearchNode(genVars), new ProblemStateManager(store));
        TreeSearch search = new TreeSearch(tree, new BreadthFirstSearch());
        return doTestSearch(search);
    }

    public int doBFSDeltaStateTree(double precision) {
        SearchAction genVars = new GenerateDoubleAction(new CspDoubleVariable[] { x, y, z }, precision);
        SearchTree tree = new CrawlingSearchTree(new BasicSearchNode(genVars), new DeltaStateManager(store));
        TreeSearch search = new TreeSearch(tree, new BreadthFirstSearch());
        return doTestSearch(search);
    }

    public int doBFSJumpingTree(double precision) {
        SearchAction genVars = new GenerateDoubleAction(new CspDoubleVariable[] { x, y, z }, precision);
        SearchTree tree = new JumpingSearchTree(new BasicSearchNode(genVars), new ProblemStateManager(store));
        TreeSearch search = new TreeSearch(tree, new BreadthFirstSearch());
        return doTestSearch(search);
    }

    public void testDFSRecalculatingTree10() {
        int solCount = doDFSRecalculatingTree(1d);
        assertEquals("num solutions", 11, solCount);
    }

    public void testDFSProblemStateTree10() {
        int solCount = doDFSProblemStateTree(1d);
        assertEquals("num solutions", 11, solCount);
    }

    public void testDFSDeltaStateTree10() {
        int solCount = doDFSDeltaStateTree(1d);
        assertEquals("num solutions", 11, solCount);
    }

    public void testDFSJumpingTree10() {
        int solCount = doDFSJumpingTree(1d);
        assertEquals("num solutions", 11, solCount);
    }

    public void testDFSRecalculatingTree075() {
        int solCount = doDFSRecalculatingTree(0.75);
        assertEquals("num solutions", 17, solCount);
    }

    public void testDFSProblemStateTree075() {
        int solCount = doDFSProblemStateTree(0.75);
        assertEquals("num solutions", 17, solCount);
    }

    public void testDFSDeltaStateTree075() {
        int solCount = doDFSDeltaStateTree(0.75);
        assertEquals("num solutions", 17, solCount);
    }

    public void testDFSJumpingTree075() {
        int solCount = doDFSJumpingTree(0.75);
        assertEquals("num solutions", 17, solCount);
    }

    public void testDFSRecalculatingTree05() {
        int solCount = doDFSRecalculatingTree(0.5);
        assertEquals("num solutions", 44, solCount);
    }

    public void testDFSProblemStateTree05() {
        int solCount = doDFSProblemStateTree(0.5);
        assertEquals("num solutions", 44, solCount);
    }

    public void testDFSDeltaStateTree05() {
        int solCount = doDFSDeltaStateTree(0.5);
        assertEquals("num solutions", 44, solCount);
    }

    public void testDFSJumpingTree05() {
        int solCount = doDFSJumpingTree(0.5);
        assertEquals("num solutions", 44, solCount);
    }

    public void testDFSRecalculatingTree25() {
        int solCount = doDFSRecalculatingTree(0.25);
        assertEquals("num solutions", 164, solCount);
    }

    public void testDFSProblemStateTree25() {
        int solCount = doDFSProblemStateTree(0.25);
        assertEquals("num solutions", 164, solCount);
    }

    public void testDFSDeltaStateTree25() {
        int solCount = doDFSDeltaStateTree(0.25);
        assertEquals("num solutions", 164, solCount);
    }

    public void testDFSJumpingTree25() {
        int solCount = doDFSJumpingTree(0.25);
        assertEquals("num solutions", 164, solCount);
    }

    public void tesBFSRecalculatingTree10() {
        int solCount = doBFSRecalculatingTree(1d);
        assertEquals("num solutions", 11, solCount);
    }

    public void testBFSProblemStateTree10() {
        int solCount = doBFSProblemStateTree(1d);
        assertEquals("num solutions", 11, solCount);
    }

    public void testBFSDeltaStateTree10() {
        int solCount = doBFSDeltaStateTree(1d);
        assertEquals("num solutions", 11, solCount);
    }

    public void testBFSJumpingTree10() {
        int solCount = doBFSJumpingTree(1d);
        assertEquals("num solutions", 11, solCount);
    }

    public void testBFSRecalculatingTree075() {
        int solCount = doBFSRecalculatingTree(0.75);
        assertEquals("num solutions", 17, solCount);
    }

    public void testBFSProblemStateTree075() {
        int solCount = doBFSProblemStateTree(0.75);
        assertEquals("num solutions", 17, solCount);
    }

    public void testBFSDeltaStateTree075() {
        int solCount = doBFSDeltaStateTree(0.75);
        assertEquals("num solutions", 17, solCount);
    }

    public void testBFSJumpingTree075() {
        int solCount = doBFSJumpingTree(0.75);
        assertEquals("num solutions", 17, solCount);
    }

    public void testBFSRecalculatingTree05() {
        int solCount = doBFSRecalculatingTree(0.5);
        assertEquals("num solutions", 44, solCount);
    }

    public void testBFSProblemStateTree05() {
        int solCount = doBFSProblemStateTree(0.5);
        assertEquals("num solutions", 44, solCount);
    }

    public void testBFSDeltaStateTree05() {
        int solCount = doBFSDeltaStateTree(0.5);
        assertEquals("num solutions", 44, solCount);
    }

    public void testBFSJumpingTree05() {
        int solCount = doBFSJumpingTree(0.5);
        assertEquals("num solutions", 44, solCount);
    }

    public void testBFSRecalculatingTree25() {
        int solCount = doBFSRecalculatingTree(0.25);
        assertEquals("num solutions", 164, solCount);
    }

    public void testBFSProblemStateTree25() {
        int solCount = doBFSProblemStateTree(0.25);
        assertEquals("num solutions", 164, solCount);
    }

    public void testBFSDeltaStateTree25() {
        int solCount = doBFSDeltaStateTree(0.25);
        assertEquals("num solutions", 164, solCount);
    }

    public void testBFSJumpingTree25() {
        int solCount = doBFSJumpingTree(0.25);
        assertEquals("num solutions", 164, solCount);
    }

    public void setUp() {
        try {
            store = new ConstraintStore(SolverImpl.createDefaultAlgorithm());
            varFactory = store.getConstraintAlg().getVarFactory();
            x = varFactory.doubleVar("x", 0, 3);
            y = varFactory.doubleVar("y", 0, 3);
            z = varFactory.doubleVar("z", 0, 3);
            store.addConstraint(x.add(y).eq(z));
        } catch (PropagationFailureException failx) {
            fail("failed adding constraint to store");
        }
    }

    public void tearDown() {
        store = null;
        varFactory = null;
        x = null;
        y = null;
        z = null;
    }
}
