package net.ontopia.topicmaps.xml;

import java.io.*;
import net.ontopia.utils.FileUtils;
import net.ontopia.utils.TestFileUtils;
import net.ontopia.infoset.impl.basic.URILocator;
import net.ontopia.topicmaps.core.*;
import net.ontopia.topicmaps.impl.basic.*;
import net.ontopia.topicmaps.xml.CanonicalXTMWriter;
import org.junit.Assert;
import org.junit.Test;

/**
 * INTERNAL. The purpose of this test class is basically to verify
 * that all entry points in the API work as advertised.
 */
public class CanonicalXTMWriterTest extends AbstractXMLTestCase {

    private TopicMapIF topicmap;

    private static final String testdataDirectory = "cxtm";

    public void setUp() throws IOException {
        topicmap = makeEmptyTopicMap();
        String root = TestFileUtils.getTestdataOutputDirectory();
        TestFileUtils.verifyDirectory(root, testdataDirectory, "out");
    }

    @Test
    public void testOutputStream() throws IOException {
        String baseline = TestFileUtils.getTestInputFile(testdataDirectory, "baseline", "outputstream.cxtm");
        File out = TestFileUtils.getTestOutputFile(testdataDirectory, "out", "outputstream.cxtm");
        FileOutputStream outs = new FileOutputStream(out);
        new CanonicalXTMWriter(outs).write(topicmap);
        outs.close();
        Assert.assertTrue("OutputStream export gives incorrect output", FileUtils.compareFileToResource(out, baseline));
    }

    @Test
    public void testWriter() throws IOException {
        String baseline = TestFileUtils.getTestInputFile(testdataDirectory, "baseline", "writer.cxtm");
        File out = TestFileUtils.getTestOutputFile(testdataDirectory, "out", "writer.cxtm");
        Writer outw = new OutputStreamWriter(new FileOutputStream(out), "utf-8");
        new CanonicalXTMWriter(outw).write(topicmap);
        outw.close();
        Assert.assertTrue("OutputStream export gives incorrect output", FileUtils.compareFileToResource(out, baseline));
    }

    private TopicMapIF makeEmptyTopicMap() throws IOException {
        InMemoryTopicMapStore store = new InMemoryTopicMapStore();
        store.setBaseAddress(new URILocator("http://www.ontopia.net"));
        return store.getTopicMap();
    }
}
