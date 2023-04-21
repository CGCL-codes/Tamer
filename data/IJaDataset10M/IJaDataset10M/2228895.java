package bgu.nlp.seg.prefix;

import bgu.nlp.seg.prefix.vo.PrefixSet;
import bgu.nlp.utils.FileUtils;
import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;
import junit.framework.TestCase;

/**
 * @author Guz
 * 
 */
public class PrefixXstreamReaderWriterTest extends TestCase {

    public static void main(String[] args) throws MalformedURLException, IOException {
        final PrefixXstreamReaderWriterTest test = new PrefixXstreamReaderWriterTest();
        test.testRead();
    }

    public void testRead() throws MalformedURLException, IOException {
        final PrefixXstreamReaderWriter prefixXstream = new PrefixXstreamReaderWriter();
        final Reader reader = FileUtils.buildReader("F:\\source_code\\DUCK\\duck-trunk-sf\\duck\\etc\\he\\ALL_PREFIXES.xml");
        final PrefixSet prefixSet = prefixXstream.readPrefixSet(reader);
        reader.close();
        final String actualText = prefixSet.iterator().next().getText();
        final String expectedText = "לדיגג";
        assertEquals(expectedText, actualText);
    }
}
