package com.hp.hpl.jena.graph.compose.test;

import com.hp.hpl.jena.graph.*;
import com.hp.hpl.jena.graph.compose.Difference;
import com.hp.hpl.jena.graph.test.*;
import junit.framework.*;

/**
	@author kers
*/
public class TestDifference extends GraphTestBase {

    public TestDifference(String name) {
        super(name);
    }

    public static TestSuite suite() {
        return new TestSuite(TestDifference.class);
    }

    public void testDifference() {
        Graph g1 = graphWith("x R y; p R q");
        Graph g2 = graphWith("r A s; x R y");
        Difference d = new Difference(g1, g2);
        assertOmits("Difference", d, "x R y");
        assertContains("Difference", "p R q", d);
        assertOmits("Difference", d, "r A s");
        if (d.size() != 1) fail("oops: size of difference is not 1");
        d.add(triple("cats eat cheese"));
        assertContains("Difference.L", "cats eat cheese", g1);
        assertOmits("Difference.R", g2, "cats eat cheese");
    }
}
