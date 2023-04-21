package com.xith3d.spatial.octree;

import com.xith3d.spatial.*;
import com.xith.utility.logs.*;
import com.xith.utility.profile.*;
import java.util.*;
import javax.vecmath.*;

/**
 * <p> </p>
 * <p> </p>
 * <p>Copyright: Copyright (c) 2000-2002</p>
 * <p>Company: Teseract Software, LLP</p>
 * @author David Yazel
 *
 */
public class OcTest {

    OcTree tree;

    public OcTest(OcTree tree) {
        this.tree = tree;
    }

    /**
     * Inserts a node into the octree and then attempts to find it again
     * @param x
     * @param y
     * @param z
     * @return
     */
    public TestCallback test(float x, float y, float z, float radius, float cx, float cy, float cz, float cradius) {
        TestObject o = new TestObject();
        OcNode node = (OcNode) tree.insert(x, y, z, radius, o);
        TestCallback callback = new TestCallback(o);
        tree.findWithinSphere(new Point3f(cx, cy, cz), cradius, callback);
        return callback;
    }

    /**
     * Testing the octree
     * @param args
     */
    public static void main(String[] args) {
        Log.log.registerLog(new ConsoleLog(LogType.PROFILE | LogType.ALL));
        Log.log.registerLog(new FileLog(LogType.PROFILE | LogType.ALL, "octree.log"));
        OcTree tree = new OcTree(16000, 6);
        OcTest test = new OcTest(tree);
        Random r = new Random(34535);
        int ok = 0;
        long totalHits = 0;
        Log.print("Inserting and checking 100000 nodes...");
        final int total = 100000;
        for (int i = 0; i < total; i++) {
            float x = r.nextFloat() * 8000;
            float y = r.nextFloat() * 8000;
            float z = r.nextFloat() * 8000;
            float radius = 1f + (r.nextFloat() * 3);
            float cradius = radius * 3f;
            float tradius = radius * 2.3f;
            float cx = x + (r.nextBoolean() ? (-tradius) : tradius);
            float cy = y + (r.nextBoolean() ? (-tradius) : tradius);
            float cz = z + (r.nextBoolean() ? (-tradius) : tradius);
            TestCallback tcb = test.test(x, y, z, radius, cx, cy, cz, cradius);
            if (tcb.found) {
                ok++;
                totalHits += tcb.hits;
            } else {
                Log.print("--------------------------------");
                Log.print("------------ ERROR -------------");
                Log.print("--------------------------------");
                Log.print("");
                Log.print("object inserted to " + x + "," + y + "," + z + " radius " + radius);
                Log.print("culler checked : " + cx + "," + cy + "," + cz + " radius " + cradius);
                OcTree.debug = true;
                test.test(x, y, z, radius, cx, cy, cz, cradius);
                Log.print("--------------------------------");
                Log.print("------- RERUN ON EMPTY TREE ----");
                Log.print("--------------------------------");
                tree = new OcTree(16000, 6);
                test = new OcTest(tree);
                test.test(x, y, z, radius, cx, cy, cz, cradius);
                break;
            }
        }
        Log.print("We performed " + ok + " successful and " + (100000 - ok) + " unsuccessful tests");
        Log.print("Average number of hits per check was " + (totalHits / total));
        ProfileTimer.printLogs();
    }

    /**
     * simple callback to see if we can find a specific node
     * <p> </p>
     * <p> </p>
     * <p>Copyright: Copyright (c) 2000-2002</p>
     * <p>Company: Teseract Software, LLP</p>
     * @author David Yazel
     *
     */
    class TestCallback implements SpatialCallback {

        TestObject o;

        int hits = 0;

        boolean found = false;

        public TestCallback(TestObject object) {
            this.o = object;
            found = false;
        }

        public void hit(SpatialHandle handle) {
            if (handle.getObject() == o) {
                found = true;
            }
            hits++;
        }
    }

    class TestObject {
    }
}
