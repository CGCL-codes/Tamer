package jasel.actor;

import jasel.engine.Renderer;
import java.util.EnumSet;
import java.util.List;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * 
 */
public class HitManagerUT extends TestCase {

    public static void main(String[] args) {
        (new HitManagerUT()).testAddSomeObjects();
    }

    public static class TestObject extends JaselObject {

        public TestObject(HitArea hitArea) {
            super(hitArea, EnumSet.of(HitType.Enemy), Type.Player);
        }

        public void draw(long millis, Renderer r) {
        }
    }

    public static Test suite() {
        return new TestSuite(HitManagerUT.class);
    }

    public void testAddSomeObjects() {
        HitManager.clearList();
        HitManager.setSorting(HitManager.X_SORTING);
        JaselObject[] testObjs = { new TestObject(HitArea.createPoint(0, 0)), new TestObject(HitArea.createPoint(20, 10)), new TestObject(HitArea.createPoint(2, 4)), new TestObject(HitArea.createPoint(18, 1)) };
        for (JaselObject jobject : testObjs) {
            HitManager.addJaselObject(jobject);
        }
        System.out.println(HitManager.string());
        @SuppressWarnings("unused") List list = HitManager.getList(Type.Player);
        assertListSortedX();
        HitManager.setSorting(HitManager.Y_SORTING);
        System.out.println(HitManager.string());
        list = HitManager.getList(Type.Player);
        assertListSortedY();
    }

    public void testObjectsStaySorted() {
        System.out.println("testObjectsStaySorted()");
        HitManager.clearList();
        HitManager.setSorting(HitManager.X_SORTING);
        JaselObject[] testObjs = { new TestObject(HitArea.createPoint(0, 0)), new TestObject(HitArea.createPoint(20, 10)), new TestObject(HitArea.createPoint(2, 4)), new TestObject(HitArea.createPoint(18, 1)) };
        for (int i = 0; i < testObjs.length; ++i) {
            HitManager.addJaselObject(testObjs[i]);
        }
        System.out.println(HitManager.string());
        HitManager.getList(Type.Player);
        assertListSortedX();
        testObjs[2].setLocation(200, 200);
        HitManager.detectHits();
        assertListSortedX();
    }

    private void assertListSortedX() {
        List list = HitManager.getList(Type.Player);
        for (int i = 0; i < list.size() - 1; ++i) {
            JaselObject jo1 = (JaselObject) list.get(i);
            JaselObject jo2 = (JaselObject) list.get(i + 1);
            assertTrue(jo1.getLocation().getX() <= jo2.getLocation().getX());
        }
    }

    private void assertListSortedY() {
        List list = HitManager.getList(Type.Player);
        for (int i = 0; i < list.size() - 1; ++i) {
            JaselObject jo1 = (JaselObject) list.get(i);
            JaselObject jo2 = (JaselObject) list.get(i + 1);
            assertTrue(jo1.getLocation().getY() <= jo2.getLocation().getY());
        }
    }
}
