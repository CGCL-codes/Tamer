package net.sf.jbg.test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import java.util.ArrayList;
import net.sf.jbg.sample.Detail1;
import net.sf.jbg.sample.DetailN;
import net.sf.jbg.sample.Master;
import org.junit.Test;

public class RelationsTest {

    @Test
    public void setDetail1() {
        Master m = new Master();
        Detail1 d = new Detail1();
        assertFalse("Before set not same detail", m.getDetail1() == d);
        assertFalse("Before set not same master", d.getMaster() == m);
        m.setDetail1(d);
        assertTrue("After set same detail", m.getDetail1() == d);
        assertFalse("After set master not set", d.getMaster() == m);
    }

    @Test
    public void setDetail1_WithReverseSide() {
        Master m = new Master();
        Detail1 d = new Detail1();
        assertFalse("Before set not same detail", m.getDetail1() == d);
        assertFalse("Before set not same master", d.getMaster() == m);
        m.setDetail1_WithReverseSide(d);
        assertTrue("After set same detail", m.getDetail1() == d);
        assertTrue("After set same master", d.getMaster() == m);
    }

    @Test
    public void setMaster1() {
        Master m = new Master();
        Detail1 d = new Detail1();
        assertFalse("Before set not same detail", m.getDetail1() == d);
        assertFalse("Before set not same master", d.getMaster() == m);
        d.setMaster(m);
        assertTrue("After set same detail", m.getDetail1() == d);
        assertTrue("After set same master", d.getMaster() == m);
    }

    @Test
    public void setDetailNList() {
        Master m = new Master();
        DetailN d1 = new DetailN();
        DetailN d2 = new DetailN();
        ArrayList<DetailN> ds = new ArrayList<DetailN>();
        ds.add(d1);
        ds.add(d2);
        assertNull("Before set master null, 1", d1.getMyMaster());
        assertNull("Before set master null, 2", d2.getMyMaster());
        m.setDetailNList(ds);
        assertNull("After set master null, 1", d1.getMyMaster());
        assertNull("After set master null, 2", d2.getMyMaster());
    }

    @Test
    public void setDetailNList_WithReverseSide() {
        Master m = new Master();
        DetailN d1 = new DetailN();
        DetailN d2 = new DetailN();
        ArrayList<DetailN> ds = new ArrayList<DetailN>();
        ds.add(d1);
        ds.add(d2);
        assertFalse("Before set not same master, 1", d1.getMyMaster() == m);
        assertFalse("Before set not same master, 2", d2.getMyMaster() == m);
        m.setDetailNList_WithReverseSide(ds);
        assertTrue("After set same master, 1", d1.getMyMaster() == m);
        assertTrue("After set same master, 2", d2.getMyMaster() == m);
    }

    @Test
    public void addToDetailNList() {
        Master m = new Master(null, new ArrayList<DetailN>());
        DetailN d = new DetailN();
        assertFalse("Before add not same master, 1", d.getMyMaster() == m);
        m.addToDetailNList(d);
        assertTrue("After add same master, 1", d.getMyMaster() == m);
    }
}
