package redrocket.pathfinding.pathfinder.test;

import redrocket.pathfinding.Path;
import redrocket.pathfinding.meshscanner.MeshScanner;
import redrocket.pathfinding.meshscanner.test.MeshScannerTestHelper;
import redrocket.pathfinding.pathfinder.PathFinder;
import redrocket.pathfinding.test.TestHelper;
import huf.data.IContainer;
import huf.data.IMap;
import huf.data.LinkedList;
import huf.data.Map;
import huf.misc.tester.Tester;
import xmage.math.Triangle3d;
import xmage.turbine.Node;
import xutils.geomview.BaseGeomViewer.CameraMode;
import java.io.IOException;

public class PathFinderTestObjects03 {

    public PathFinderTestObjects03(Tester t) throws IOException {
        test(t);
    }

    /** Enable visual test feedback. */
    private static boolean visual = false;

    private IMap<String, IContainer<Triangle3d>> getWantedTris() {
        IMap<String, IContainer<Triangle3d>> map = new Map<String, IContainer<Triangle3d>>();
        map.put("Plane", new LinkedList<Triangle3d>(new Triangle3d[] { new Triangle3d(-1.0, 0.0, -1.0, 1.0, 0.0, -3.0, -1.0, 0.0, -3.0) }));
        map.put("Plane.001", new LinkedList<Triangle3d>(new Triangle3d[] { new Triangle3d(1.0, 0.0, -1.0, -1.0, 0.0, 1.0, 1.0, 0.0, 1.0) }));
        map.put("Plane.002", new LinkedList<Triangle3d>(new Triangle3d[] { new Triangle3d(1.0, 0.0, -1.0, -1.0, 0.0, -1.0, -1.0, 0.0, 1.0) }));
        map.put("Plane.003", new LinkedList<Triangle3d>(new Triangle3d[] { new Triangle3d(1.0, 0.0, 1.0, 3.0, 0.0, 1.0, 1.0, 0.0, -1.0) }));
        map.put("Plane.004", new LinkedList<Triangle3d>(new Triangle3d[] { new Triangle3d(1.0, 0.0, -1.0, 3.0, 0.0, 1.0, 3.0, 0.0, -1.0) }));
        map.put("Plane.005", new LinkedList<Triangle3d>(new Triangle3d[] { new Triangle3d(3.0, 0.0, -1.0, 3.0, 0.0, -3.0, 1.0, 0.0, -1.0) }));
        map.put("Plane.006", new LinkedList<Triangle3d>(new Triangle3d[] { new Triangle3d(1.0, 0.0, -1.0, 3.0, 0.0, -3.0, 1.0, 0.0, -3.0) }));
        map.put("Plane.007", new LinkedList<Triangle3d>(new Triangle3d[] { new Triangle3d(1.0, 0.0, -1.0, 1.0, 0.0, -3.0, -1.0, 0.0, -1.0) }));
        return map;
    }

    private IContainer<Triangle3d> getWantedTris1to2() {
        return new LinkedList<Triangle3d>(new Triangle3d[] { new Triangle3d(1.0, 0.0, -1.0, -1.0, 0.0, 1.0, 1.0, 0.0, 1.0), new Triangle3d(1.0, 0.0, -1.0, -1.0, 0.0, -1.0, -1.0, 0.0, 1.0), new Triangle3d(1.0, 0.0, -1.0, 1.0, 0.0, -3.0, -1.0, 0.0, -1.0), new Triangle3d(1.0, 0.0, -1.0, 3.0, 0.0, -3.0, 1.0, 0.0, -3.0) });
    }

    private void test(Tester t) throws IOException {
        Node scene = TestHelper.loadScene(this);
        new MeshScanner().findWalkMesh(scene);
        MeshScannerTestHelper.checkPFData(t, getClass(), getWantedTris(), scene);
        Path path = PathFinder.findPath(scene, "item1", "item2");
        PathFinderTestHelper.checkPath(t, getClass(), getWantedTris1to2(), path);
        if (visual) {
            TestHelper.show(scene, path, CameraMode.TOP);
        }
    }

    public static void main(String[] args) throws IOException {
        visual = true;
        Tester t = new Tester();
        new PathFinderTestObjects03(t);
        t.totals();
    }
}
