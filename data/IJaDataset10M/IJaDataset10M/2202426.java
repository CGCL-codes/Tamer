package com.jeantessier.dependency;

import java.io.*;
import java.util.*;
import junit.framework.*;
import com.jeantessier.classreader.*;

public class TestCodeDependencyCollectorWithFiltering extends TestCase {

    public static final String TEST_CLASS = "test";

    public static final String TEST_FILENAME = "classes" + File.separator + "test.class";

    private NodeFactory factory;

    protected void setUp() throws Exception {
        factory = new NodeFactory();
        RegularExpressionSelectionCriteria filterCriteria = new RegularExpressionSelectionCriteria("//");
        filterCriteria.setGlobalExcludes("/java.lang/");
        ClassfileLoader loader = new AggregatingClassfileLoader();
        loader.addLoadListener(new LoadListenerVisitorAdapter(new CodeDependencyCollector(factory, filterCriteria)));
        loader.load(Collections.singleton(TEST_FILENAME));
    }

    public void testPackages() {
        assertEquals("nb packages", 3, factory.getPackages().size());
        Node node;
        node = factory.getPackages().get("");
        assertNotNull("default package missing", node);
        assertTrue("default package not concrete", node.isConfirmed());
        node = factory.getPackages().get("java.io");
        assertNotNull("package java.io missing", node);
        assertFalse("package java.io is concrete", node.isConfirmed());
        node = factory.getPackages().get("java.util");
        assertNotNull("package java.util missing", node);
        assertFalse("package java.util is concrete", node.isConfirmed());
    }

    public void testClasses() {
        assertEquals("nb classes", 3, factory.getClasses().size());
        Node node;
        node = factory.getClasses().get("test");
        assertNotNull("class test missing", node);
        assertTrue("class test not concrete", node.isConfirmed());
        node = factory.getClasses().get("java.io.PrintStream");
        assertNotNull("class java.io.PrintStream missing", node);
        assertFalse("class java.io.PrintStream is concrete", node.isConfirmed());
        node = factory.getClasses().get("java.util.Set");
        assertNotNull("class java.util.Set missing", node);
        assertFalse("class java.util.Set is concrete", node.isConfirmed());
    }

    public void testFeatures() {
        assertEquals("nb features", 2, factory.getFeatures().size());
        Node node;
        node = factory.getFeatures().get("test.main(java.lang.String[])");
        assertNotNull("feature test.main(java.lang.String[]) missing", node);
        assertTrue("feature test.main(java.lang.String[]) not concrete", node.isConfirmed());
        node = factory.getFeatures().get("test.test()");
        assertNotNull("feature test.test() missing", node);
        assertTrue("feature test.test() not concrete", node.isConfirmed());
    }
}
