package org.argouml.uml.diagram.state.ui;

import junit.framework.TestCase;
import org.argouml.model.InitializeModel;
import org.argouml.profile.init.InitProfileSubsystem;

/**
 * Tests whether Figs in state.ui are clonable,
 * apart from FigStateVertex which is abstract.
 */
public class TestFigClonable extends TestCase {

    /**
     * The constructor.
     *
     * @param name the test name
     */
    public TestFigClonable(String name) {
        super(name);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        InitializeModel.initializeDefault();
        new InitProfileSubsystem().init();
    }

    /**
     * Try to clone {@link FigBranchState}.
     */
    public void testBranchStateClonable() {
        FigBranchState fig = new FigBranchState();
        FigBranchState figClone = (FigBranchState) fig.clone();
        assertNotNull(figClone);
    }

    /**
     * Try to clone {@link FigCompositeState}.
     */
    public void testCompositeStateClonable() {
        FigCompositeState fig = new FigCompositeState();
        FigCompositeState figClone = (FigCompositeState) fig.clone();
        assertNotNull(figClone);
    }

    /**
     * Try to clone {@link FigDeepHistoryState}.
     */
    public void testDeepHistoryStateClonable() {
        FigDeepHistoryState fig = new FigDeepHistoryState();
        FigDeepHistoryState figClone = (FigDeepHistoryState) fig.clone();
        assertNotNull(figClone);
    }

    /**
     * Try to clone {@link FigFinalState}.
     */
    public void testFinalStateClonable() {
        FigFinalState fig = new FigFinalState();
        FigFinalState figClone = (FigFinalState) fig.clone();
        assertNotNull(figClone);
    }

    /**
     * Try to clone {@link FigForkState}.
     */
    public void testForkStateClonable() {
        FigForkState fig = new FigForkState();
        FigForkState figClone = (FigForkState) fig.clone();
        assertNotNull(figClone);
    }

    /**
     * Try to clone {@link FigInitialState}.
     */
    public void testInitialStateClonable() {
        FigInitialState fig = new FigInitialState();
        FigInitialState figClone = (FigInitialState) fig.clone();
        assertNotNull(figClone);
    }

    /**
     * Try to clone {@link FigJoinState}.
     */
    public void testJoinStateClonable() {
        FigJoinState fig = new FigJoinState();
        FigJoinState figClone = (FigJoinState) fig.clone();
        assertNotNull(figClone);
    }

    /**
     * Try to clone {@link FigShallowHistoryState}.
     */
    public void testShallowHistoryStateClonable() {
        FigShallowHistoryState fig = new FigShallowHistoryState();
        FigShallowHistoryState figClone = (FigShallowHistoryState) fig.clone();
        assertNotNull(figClone);
    }

    /**
     * Try to clone {@linkFigState}.
     */
    public void testSimpleStateClonable() {
        FigSimpleState fig = new FigSimpleState();
        FigSimpleState figClone = (FigSimpleState) fig.clone();
        assertNotNull(figClone);
    }

    /**
     * Try to clone {@link FigTransition}.
     */
    public void testTransitionClonable() {
        FigTransition fig = new FigTransition();
        FigTransition figClone = (FigTransition) fig.clone();
        assertNotNull(figClone);
    }
}
