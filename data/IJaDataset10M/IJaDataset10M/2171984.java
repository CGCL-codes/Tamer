package org.macchiato.classifier;

import java.util.List;
import java.util.Vector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.macchiato.log.MacchiatoLogger;

/**
 * @author fdietz
 */
public class CombineProbabilitiesTest {

    @Test
    public void test() {
        List list = new Vector();
        list.add(new Float("0.6"));
        list.add(new Float("0.72"));
        float result = CombineProbabilities.combine(list);
        Assert.assertEquals(0.794f, result, 0.01f);
    }

    @Test
    public void test2() {
        List list = new Vector();
        list.add(new Float("0.9"));
        list.add(new Float("0.9"));
        list.add(new Float("0.9"));
        list.add(new Float("0.9"));
        list.add(new Float("0.9"));
        list.add(new Float("0.9"));
        list.add(new Float("0.9"));
        list.add(new Float("0.9"));
        float result = CombineProbabilities.combine(list);
        System.out.println("result=" + result);
        Assert.assertEquals(1.0f, result, 0.01f);
    }

    @Test
    public void test3() {
        List list = new Vector();
        list.add(new Float("0.01"));
        list.add(new Float("0.01"));
        list.add(new Float("0.01"));
        list.add(new Float("0.01"));
        list.add(new Float("0.01"));
        list.add(new Float("0.01"));
        list.add(new Float("0.01"));
        list.add(new Float("0.01"));
        list.add(new Float("0.01"));
        list.add(new Float("0.01"));
        list.add(new Float("0.01"));
        list.add(new Float("0.01"));
        float result = CombineProbabilities.combine(list);
        System.out.println("result=" + result);
        Assert.assertEquals(0.0f, result, 0.01f);
    }

    /**
     * @see junit.framework.TestCase#setUp()
     */
    @Before
    public void setUp() throws Exception {
        new MacchiatoLogger();
    }
}
