package net.sf.buildbox.buildozer;

import org.junit.Test;
import org.junit.Assert;

public class DependencyGraphTest {

    @Test(expected = IllegalArgumentException.class)
    public void testCircleDetection() {
        final DependencyGraph<String> g = new DependencyGraph<String>();
        g.node("A").addDependency("B");
        g.node("A").addDependency("C");
        Assert.assertEquals(g.node("A").getDependencies().size(), 2);
        Assert.assertEquals(g.node("B").getDependants().size(), 1);
        Assert.assertEquals(g.node("C").getDependants().size(), 1);
        g.node("A").removeDependency("C");
        Assert.assertEquals(g.node("A").getDependencies().size(), 1);
        Assert.assertEquals(g.node("C").getDependants().size(), 0);
        g.node("B").addDependency("C");
        g.node("C").addDependency("D");
        System.out.println("g = " + g);
        g.node("D").addDependency("A");
        g.assertNoCircles();
    }
}
