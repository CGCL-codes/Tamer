package redrocket.pathfinding.meshscanner.test;

import redrocket.pathfinding.meshscanner.MeshScanner;
import redrocket.pathfinding.test.TestHelper;
import huf.data.IContainer;
import huf.data.LinkedList;
import huf.misc.tester.Tester;
import xmage.math.Triangle3d;
import xmage.turbine.Node;
import java.io.IOException;

public class MeshScannerTestMultiMeshEdge {

    public MeshScannerTestMultiMeshEdge(Tester t) throws IOException {
        test(t);
    }

    /** Enable visual test feedback. */
    private static boolean visual = false;

    private IContainer<Triangle3d> getWantedTris() {
        return new LinkedList<Triangle3d>(new Triangle3d[] { new Triangle3d(15.0, 0.0, 1.0, -6.0, 0.0, -1.0, -6.0, 0.0, 1.0), new Triangle3d(6.0, 0.0, 3.0, 8.0, 0.0, 3.0, 8.0, 0.0, 1.0), new Triangle3d(6.0, 0.0, 3.0, 8.0, 0.0, 1.0, 6.0, 0.0, 1.0), new Triangle3d(-3.0, 0.0, 2.0, -1.0, 0.0, 2.0, -2.0, 0.0, 1.0), new Triangle3d(-3.0, 0.0, 2.0, -2.0, 0.0, 3.0, -1.0, 0.0, 2.0), new Triangle3d(9.0, 0.0, 3.0999999046325684, 11.0, 0.0, 3.0999999046325684, 11.0, 0.0, 1.100000023841858), new Triangle3d(9.0, 0.0, 3.0999999046325684, 11.0, 0.0, 1.100000023841858, 9.0, 0.0, 1.100000023841858), new Triangle3d(9.0, 0.0, 5.199999809265137, 11.0, 0.0, 5.199999809265137, 11.0, 0.0, 3.200000047683716), new Triangle3d(9.0, 0.0, 5.199999809265137, 11.0, 0.0, 3.200000047683716, 9.0, 0.0, 3.200000047683716), new Triangle3d(-0.0, 0.0, 2.0999999046325684, 2.0, 0.0, 2.0999999046325684, 1.0, 0.0, 1.100000023841858), new Triangle3d(-0.0, 0.0, 2.0999999046325684, 1.0, 0.0, 3.0999999046325684, 2.0, 0.0, 2.0999999046325684), new Triangle3d(15.0, 0.0, 1.0, 15.0, 0.0, -1.0, -6.0, 0.0, -1.0), new Triangle3d(4.0, 0.0, -1.100000023841858, 6.0, 0.0, -1.100000023841858, 6.0, 0.0, -3.0999999046325684), new Triangle3d(4.0, 0.0, -1.100000023841858, 6.0, 0.0, -3.0999999046325684, 4.0, 0.0, -3.0999999046325684), new Triangle3d(1.0, -0.10000000149011612, -1.100000023841858, 3.0, -0.10000000149011612, -1.100000023841858, 3.0, -0.10000000149011612, -3.0999999046325684), new Triangle3d(1.0, -0.10000000149011612, -1.100000023841858, 3.0, -0.10000000149011612, -3.0999999046325684, 1.0, -0.10000000149011612, -3.0999999046325684), new Triangle3d(7.0, 0.10000000149011612, -1.100000023841858, 9.0, 0.10000000149011612, -1.100000023841858, 9.0, 0.10000000149011612, -3.0999999046325684), new Triangle3d(7.0, 0.10000000149011612, -1.100000023841858, 9.0, 0.10000000149011612, -3.0999999046325684, 7.0, 0.10000000149011612, -3.0999999046325684), new Triangle3d(-3.0, 0.0, 5.099999904632568, -1.0, 0.0, 3.0999999046325684, -3.0, 0.0, 3.0999999046325684), new Triangle3d(-3.0, 0.0, 5.099999904632568, -1.0, 0.0, 5.099999904632568, -1.0, 0.0, 3.0999999046325684), new Triangle3d(-6.0, 0.0, 2.0, -4.0, 0.0, 2.0, -5.0, 0.0, 1.0), new Triangle3d(-6.0, 0.0, 2.0, -5.0, 0.0, 3.0, -4.0, 0.0, 2.0), new Triangle3d(-6.0, 0.0, 5.0, -4.0, 0.0, 3.0, -6.0, 0.0, 3.0), new Triangle3d(-6.0, 0.0, 5.0, -4.0, 0.0, 5.0, -4.0, 0.0, 3.0) });
    }

    private void test(Tester t) throws IOException {
        Node scene = TestHelper.loadScene(this);
        new MeshScanner().findWalkMesh(scene);
        MeshScannerTestHelper.checkPFData(t, getClass(), getWantedTris(), scene);
        if (visual) {
            TestHelper.show(scene);
        }
    }

    public static void main(String[] args) throws IOException {
        visual = true;
        Tester t = new Tester();
        new MeshScannerTestMultiMeshEdge(t);
        t.totals();
    }
}
