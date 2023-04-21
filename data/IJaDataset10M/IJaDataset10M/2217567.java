package com.jeantessier.dependencyfinder.cli;

import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import junit.framework.*;
import org.xml.sax.*;
import com.jeantessier.classreader.*;
import com.jeantessier.dependency.*;
import com.jeantessier.metrics.*;

public class TestVerboseListener extends TestCase {

    public static final String TEST_CLASS = "test";

    public static final String TEST_FILENAME = "classes" + File.separator + "test.class";

    private StringWriter writer;

    private VerboseListener listener;

    protected void setUp() throws Exception {
        super.setUp();
        writer = new StringWriter();
        listener = new VerboseListener();
        listener.setWriter(writer);
    }

    public void testLoadListener() {
        AggregatingClassfileLoader loader = new AggregatingClassfileLoader();
        loader.addLoadListener(listener);
        loader.load(Collections.singleton(TEST_FILENAME));
        assertTrue("Wrote nothing", writer.toString().length() > 0);
    }

    public void testDependencyListener() {
        AggregatingClassfileLoader loader = new AggregatingClassfileLoader();
        loader.load(Collections.singleton(TEST_FILENAME));
        CodeDependencyCollector collector = new CodeDependencyCollector();
        collector.addDependencyListener(listener);
        loader.getClassfile(TEST_CLASS).accept(collector);
        assertTrue("Wrote nothing", writer.toString().length() > 0);
    }

    public void testMetricsListener() throws IOException, SAXException, ParserConfigurationException {
        AggregatingClassfileLoader loader = new AggregatingClassfileLoader();
        loader.load(Collections.singleton(TEST_FILENAME));
        MetricsFactory factory = new MetricsFactory("test", new MetricsConfigurationLoader(Boolean.getBoolean("DEPENDENCYFINDER_TESTS_VALIDATE")).load("etc" + File.separator + "MetricsConfig.xml"));
        com.jeantessier.metrics.MetricsGatherer gatherer = new com.jeantessier.metrics.MetricsGatherer(factory);
        gatherer.addMetricsListener(listener);
        loader.getClassfile(TEST_CLASS).accept(gatherer);
        assertTrue("Wrote nothing", writer.toString().length() > 0);
    }

    public void testPrintWriter() {
        String testText = "foobar";
        listener.print(testText);
        assertEquals(testText + System.getProperty("line.separator"), writer.toString());
    }
}
