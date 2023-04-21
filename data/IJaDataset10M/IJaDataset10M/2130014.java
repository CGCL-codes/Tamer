package com.genia.toolbox.model.exemple.impl;

import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import com.genia.toolbox.model.enumeration.Statut;
import com.genia.toolbox.model.enumeration.impl.StatutDelegate;
import com.genia.toolbox.model.exemple.impl.AImpl;
import com.genia.toolbox.model.exemple.impl.EImpl;

/**
 * this class is use for testing delegate from generating class.
 */
public class DelegateTest {

    /**
   * the object AImpl being test.
   */
    AImpl a = null;

    /**
   * delegate object.
   */
    ADelegate aDelegate = null;

    /**
   * the object EImpl being test.
   */
    EImpl e = null;

    /**
   * delegate object.
   */
    EDelegate eDelegate = null;

    /**
   * the object BImpl being test.
   */
    BImpl b = null;

    /**
   * delegate object.
   */
    BDelegate bDelegate = null;

    /**
   * the object BChildImpl being test.
   */
    BChildImpl bChild = null;

    /**
   * delegate object.
   */
    BChildDelegate bChildDelegate = null;

    /**
   * delegate object.
   */
    StatutDelegate statutDelegate = null;

    /**
   * initialization of the object.
   */
    @Before
    public void initObject() {
        a = new AImpl();
        e = new EImpl();
        b = new BImpl();
        bChild = new BChildImpl();
        aDelegate = new ADelegate(a);
        eDelegate = new EDelegate(e);
        bDelegate = new BDelegate(b);
        bChildDelegate = new BChildDelegate(bChild);
        statutDelegate = new StatutDelegate(Statut.Married);
    }

    /**
   * test methods from enum.
   */
    @Test
    public void testMethodsFromEnum() {
        Assert.assertEquals(Statut.Single.getWife(null, "testVar", 2), statutDelegate.getWife(null, "testVar", 2));
        Assert.assertEquals(Statut.Married.getWife(null, "testVar", 2), statutDelegate.getWife(null, "testVar", 2));
    }

    /**
   * test methods from <code>AImpl</code>.
   */
    @Test
    public void testMethodsFromAImpl() {
        Assert.assertNull(a.getFoo());
        a.getParam();
        Assert.assertEquals(new Integer(2), a.getFoo());
        Assert.assertNotSame(new Integer(0), a.getFoo());
        Assert.assertEquals(aDelegate.getFoo3(1, 1), a.getFoo3(1, 1));
        Assert.assertEquals(aDelegate.getFoo4(), a.getFoo4());
        Assert.assertEquals(aDelegate.getVal("a Value"), a.getVal("a Value"));
    }

    /**
   * test methods from <code>EImpl</code>.
   */
    @Test
    public void testMethodsFromEImpl() {
        Assert.assertNull(e.getFoo());
        e.getParam();
        Assert.assertEquals(new Integer(2), e.getFoo());
        Assert.assertNotSame(new Integer(2), a.getFoo());
        Assert.assertEquals(eDelegate.getNameFoo("string test"), e.getNameFoo("string test"));
        Assert.assertEquals(eDelegate.getFoo3(0, 2), e.getFoo3(0, 2));
        Assert.assertEquals(eDelegate.getFoo4(), e.getFoo4());
        Assert.assertEquals(eDelegate.getVal("testVar"), e.getVal("testVar"));
        Assert.assertEquals(eDelegate.getFoo4(), aDelegate.getFoo4());
        Assert.assertEquals(eDelegate.getFoo3(2, 6), aDelegate.getFoo3(2, 6));
        Assert.assertEquals(eDelegate.getVal("v"), aDelegate.getVal("v"));
    }

    /**
   * test methods from <code>BImpl</code>.
   */
    @Test
    public void testMethodsFromBImpl() {
        Assert.assertNull(b.getFoo());
        b.getParam();
        Assert.assertEquals(new Integer(2), b.getFoo());
        List<String> testListString = new ArrayList<String>();
        testListString.add("test1");
        testListString.add("test2");
        Assert.assertEquals(bDelegate.getAnyList(testListString), b.getAnyList(testListString));
        Assert.assertEquals(bDelegate.getBar("testBar", new Long(0)), b.getBar("testBar", new Long(0)));
        Assert.assertEquals(bDelegate.getFoo3(2, 2), b.getFoo3(2, 2));
        Assert.assertEquals(bDelegate.getFoo4(), b.getFoo4());
        Assert.assertNull(b.getBar());
        b.getFoo2("bar", "foo");
        Assert.assertEquals("barfoo", b.getBar());
        b.setBar(null);
        Assert.assertEquals(bDelegate.getNameFoo("surName"), b.getNameFoo("surName"));
        Assert.assertEquals(bDelegate.getSomeText("valt1"), b.getSomeText("valt1"));
        Assert.assertNull(b.getBar());
        b.doFoo5();
        Assert.assertEquals("doFoo5 from BDelegate", b.getBar());
        Assert.assertEquals(eDelegate.getFoo4(), bDelegate.getFoo4());
        Assert.assertEquals(eDelegate.getFoo3(2, 6), bDelegate.getFoo3(2, 6));
        Assert.assertEquals(eDelegate.getVal("v"), bDelegate.getVal("v"));
        Assert.assertEquals(eDelegate.getNameFoo("s"), bDelegate.getNameFoo("s"));
        Assert.assertEquals(aDelegate.getFoo3(1, 1), bDelegate.getFoo3(1, 1));
        Assert.assertEquals(aDelegate.getFoo4(), bDelegate.getFoo4());
        Assert.assertEquals(aDelegate.getVal("a Value"), bDelegate.getVal("a Value"));
    }

    /**
   * test methods from <code>BChildImpl</code>.
   */
    @Test
    public void testMethodsFromBChildImpl() {
        Assert.assertNull(bChild.getFoo());
        bChild.getParam();
        Assert.assertEquals(new Integer(2), bChild.getFoo());
        List<String> otherListString = new ArrayList<String>();
        otherListString.add("test3");
        otherListString.add("test4");
        Assert.assertEquals(bChildDelegate.getAnyList(otherListString), bChild.getAnyList(otherListString));
        Assert.assertEquals(bChildDelegate.getBar("otherTest", new Long(0)), bChild.getBar("otherTest", new Long(0)));
        Assert.assertEquals(bChildDelegate.getFoo3(3, 3), bChild.getFoo3(3, 3));
        Assert.assertEquals(bChildDelegate.getFoo4(), bChild.getFoo4());
        Assert.assertEquals(bChildDelegate.getNameFoo("name"), bChild.getNameFoo("name"));
        Assert.assertEquals(bChildDelegate.getSomeText("value1"), bChild.getSomeText("value1"));
        Assert.assertNull(bChild.getOtherBar());
        bChildDelegate.getSomeFoo();
        Assert.assertEquals("getSomeFoo from BChildDelegate", bChild.getOtherBar());
        List<String> testListString = new ArrayList<String>();
        testListString.add("test1");
        testListString.add("test2");
        Assert.assertEquals(bDelegate.getAnyList(testListString), bChildDelegate.getAnyList(testListString));
        Assert.assertEquals(bDelegate.getBar("testBar", new Long(0)), bChildDelegate.getBar("testBar", new Long(0)));
        Assert.assertEquals(bDelegate.getSomeText("valt1"), bChildDelegate.getSomeText("valt1"));
        Assert.assertEquals(bDelegate.getFoo4(), bChildDelegate.getFoo4());
        Assert.assertEquals(bDelegate.getFoo3(2, 6), bChildDelegate.getFoo3(2, 6));
        Assert.assertEquals(bDelegate.getVal("v"), bChildDelegate.getVal("v"));
        Assert.assertEquals(bDelegate.getNameFoo("s"), bChildDelegate.getNameFoo("s"));
        Assert.assertEquals(eDelegate.getFoo4(), bChildDelegate.getFoo4());
        Assert.assertEquals(eDelegate.getFoo3(2, 6), bChildDelegate.getFoo3(2, 6));
        Assert.assertEquals(eDelegate.getVal("v"), bChildDelegate.getVal("v"));
        Assert.assertEquals(eDelegate.getNameFoo("s"), bChildDelegate.getNameFoo("s"));
        Assert.assertEquals(aDelegate.getFoo3(1, 1), bChildDelegate.getFoo3(1, 1));
        Assert.assertEquals(aDelegate.getFoo4(), bChildDelegate.getFoo4());
        Assert.assertEquals(aDelegate.getVal("a Value"), bChildDelegate.getVal("a Value"));
    }
}
