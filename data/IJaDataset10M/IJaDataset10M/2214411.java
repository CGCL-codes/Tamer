package org.iqual.chaplin.example.basic.autocollect;

import junit.framework.TestCase;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.ArrayList;
import org.iqual.chaplin.*;
import static org.iqual.chaplin.DynaCastUtils.*;

/**
 * @author Zbynek Slajchrt
 * @since 31.10.2009 17:00:15
 */
public class AutoCollectTest extends TestCase {

    public static class AList {

        @FromContext
        private List<B> blist;

        public B getB(int i) {
            for (B b : blist) {
                if (b.getI() == i) {
                    return b;
                }
            }
            return null;
        }
    }

    public static class ASet {

        @FromContext
        private Set<B> bset;

        public B getB(int i) {
            for (B b : bset) {
                if (b.getI() == i) {
                    return b;
                }
            }
            return null;
        }
    }

    public static class AMap {

        @FromContext
        private Map<String, B> bmap;

        public B getB(String name) {
            return bmap.get(name);
        }
    }

    public static class B {

        private int i;

        public B(int i) {
            this.i = i;
        }

        public int getI() {
            return i;
        }
    }

    public static class AListWithSelector {

        private int iForSelector = 2;

        public static class ASelector extends ElementSelector<B, AListWithSelector> {

            public boolean selected(B b, AListWithSelector source) {
                return b.i > source.iForSelector;
            }
        }

        @FromContext(transformer = ASelector.class)
        private List<B> blist;

        public B getB(int i) {
            for (B b : blist) {
                if (b.getI() == i) {
                    return b;
                }
            }
            return null;
        }
    }

    public void testListAutocollect() {
        B b1 = new B(1);
        B b2 = new B(2);
        AList a = $($role("blist", b1), $role("blist", b2));
        assertEquals(b1, a.getB(1));
        assertEquals(b2, a.getB(2));
    }

    public void testSetAutocollect() {
        B b1 = new B(1);
        B b2 = new B(2);
        ASet a = $($role("bset", b1), $role("bset", b2));
        assertEquals(b1, a.getB(1));
        assertEquals(b2, a.getB(2));
    }

    public void testMapAutocollect() {
        B b1 = new B(1);
        B b2 = new B(2);
        AMap a = $($role("bmap", $name("b1", b1)), $role("bmap", $name("b2", b2)));
        assertEquals(b1, a.getB("b1"));
        assertEquals(b2, a.getB("b2"));
    }

    public void testListAutocollectWithSelector() {
        B b1 = new B(1);
        B b2 = new B(2);
        B b3 = new B(3);
        B b4 = new B(4);
        AListWithSelector a = $($role("blist", b1), $role("blist", b2), $role("blist", b3), $role("blist", b4));
        assertNull(a.getB(1));
        assertNull(a.getB(2));
        assertEquals(b3, a.getB(3));
        assertEquals(b4, a.getB(4));
    }

    public static class MyArrayList extends ArrayList<B> {
    }

    public void testListNoAutocollectWithSelector() {
        B b1 = new B(1);
        B b2 = new B(2);
        B b3 = new B(3);
        B b4 = new B(4);
        List<B> blist = new MyArrayList();
        blist.add(b1);
        blist.add(b2);
        blist.add(b3);
        blist.add(b4);
        AListWithSelector a = $($role("blist", blist));
        assertNull(a.getB(1));
        assertNull(a.getB(2));
        assertEquals(b3, a.getB(3));
        assertEquals(b4, a.getB(4));
    }
}
