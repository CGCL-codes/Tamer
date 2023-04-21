package net.todd.games.boardgame;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Bounds;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Node;
import javax.vecmath.Point3d;
import net.todd.common.uitools.IListener;
import org.junit.Before;
import org.junit.Test;
import com.sun.j3d.utils.universe.Viewer;
import com.sun.j3d.utils.universe.ViewingPlatform;

public class GameEngineTest {

    private int lightSceneCallCount;

    private int createGameGridCallCount;

    private int createBackgroundCallCount;

    private int createPiecesCallCount;

    private int callCount;

    private CameraGeneratorStub cameraGenerator;

    private PieceGeneratorStub pieceGenerator;

    private SceneGeneratorStub sceneGenerator;

    @Before
    public void setUp() {
        cameraGenerator = new CameraGeneratorStub();
        pieceGenerator = new PieceGeneratorStub();
        sceneGenerator = new SceneGeneratorStub();
    }

    @Test
    public void testCreateSceneCallsAllTheRightMethodsInCorrectOrder() {
        GameEngine gameEngine = new GameEngine(sceneGenerator, pieceGenerator, cameraGenerator);
        gameEngine.createScene(null);
        assertEquals(1, lightSceneCallCount);
        assertEquals(2, createGameGridCallCount);
        assertEquals(3, createBackgroundCallCount);
        assertEquals(4, createPiecesCallCount);
    }

    @Test
    public void testCreateCameraCallsRightMethodWithGivenArguments() {
        GameEngine gameEngine = new GameEngine(null, null, cameraGenerator);
        IUniverse universe = new UniverseStub();
        assertNull(cameraGenerator.su);
        assertNull(cameraGenerator.bounds);
        gameEngine.createCamera(universe);
        assertSame(universe, cameraGenerator.su);
        assertEquals(100d, cameraGenerator.bounds.getRadius());
        Point3d boundingSphereCenter = new Point3d();
        cameraGenerator.bounds.getCenter(boundingSphereCenter);
        assertEquals(new Point3d(0, 0, 0), boundingSphereCenter);
    }

    @Test
    public void testPickerIsPassedOnToSceneAndPieceGenerators() {
        GameEngine gameEngine = new GameEngine(sceneGenerator, pieceGenerator, null);
        IPicker picker = new PickerStub();
        gameEngine.createScene(picker);
        assertSame(picker, sceneGenerator.picker);
        assertSame(picker, pieceGenerator.picker);
    }

    @Test
    public void testBoundsGivenToAllGenerators() {
        GameEngine gameEngine = new GameEngine(sceneGenerator, pieceGenerator, cameraGenerator);
        assertNull(sceneGenerator.backgroundBounds);
        assertNull(sceneGenerator.lightingBounds);
        assertNull(cameraGenerator.bounds);
        gameEngine.createScene(null);
        gameEngine.createCamera(null);
        assertSame(sceneGenerator.backgroundBounds, sceneGenerator.lightingBounds);
        assertSame(sceneGenerator.lightingBounds, cameraGenerator.bounds);
    }

    private class CameraGeneratorStub implements ICameraGenerator {

        BoundingSphere bounds;

        IUniverse su;

        public void createCamera(IUniverse su, Bounds bounds) {
            this.su = su;
            this.bounds = (BoundingSphere) bounds;
        }
    }

    private class PieceGeneratorStub implements IPieceGenerator {

        IPicker picker;

        public void createPieces(IPicker picker, IUserPiecesFactory userPiecesFactory) {
            this.picker = picker;
            createPiecesCallCount = ++callCount;
        }
    }

    private class SceneGeneratorStub implements ISceneGenerator {

        IPicker picker;

        Bounds backgroundBounds;

        Bounds lightingBounds;

        public void createBackground(Bounds bounds) {
            this.backgroundBounds = bounds;
            createBackgroundCallCount = ++callCount;
        }

        public void createGameGrid(IPicker picker, IGameGridFactory gameGridFactory) {
            this.picker = picker;
            createGameGridCallCount = ++callCount;
        }

        public void lightScene(Bounds bounds) {
            this.lightingBounds = bounds;
            lightSceneCallCount = ++callCount;
        }
    }

    private static class UniverseStub implements IUniverse {

        public void addBranchGraph(IBranchGroup branchGroup) {
            throw new UnsupportedOperationException();
        }

        public Canvas3D getCanvas() {
            throw new UnsupportedOperationException();
        }

        public Viewer getViewer() {
            throw new UnsupportedOperationException();
        }

        public ViewingPlatform getViewingPlatform() {
            throw new UnsupportedOperationException();
        }
    }

    private static class PickerStub implements IPicker {

        public void addListener(IListener listener) {
            throw new UnsupportedOperationException();
        }

        public Node getSelectedNode() {
            throw new UnsupportedOperationException();
        }

        public void removeListener(IListener listener) {
            throw new UnsupportedOperationException();
        }
    }
}
