package drcube.test.views;

import j2meunit.framework.Test;
import j2meunit.framework.TestCase;
import j2meunit.framework.TestSuite;
import drcube.controller.CanvasCubeController;
import drcube.model.CubeGenerator;
import drcube.model.LoggedCube;
import drcube.model.Rotation;
import drcube.model.RubikCube;
import drcube.test.views.testclasses.GameView3DTest;
import drcube.views.Cursor3D;

public class TestGameView3D extends TestCase {

    /**
     * Vorbedingungen f�r Tests.
     * 
     * @see j2meunit.framework.TestCase#setUp()
     */
    public void setUp() {
    }

    /**
     * Nachbedingungen f�r Tests. Aufr�umarbeiten etc.
     * 
     * @see j2meunit.framework.TestCase#tearDown()
     */
    public void tearDown() {
    }

    /**
     * Liefert die TestSuite.
     * 
     * @see j2meunit.framework.TestCase#suite()
     */
    public Test suite() {
        return new TestSuite(new TestGameView3D().getClass(), new String[] { "testInit", "testRotateLayer", "testRotateCube", "testMapping" });
    }

    /***************************************************************************
     * Override to run the test and assert its state.
     * 
     * @exception Throwable
     *                if any exception is thrown
     */
    protected void runTest() throws java.lang.Throwable {
    }
}
