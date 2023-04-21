package org.archive.processors.extractor;

import java.util.Collection;
import java.util.Collections;
import org.archive.net.UURI;
import org.archive.net.UURIFactory;
import org.archive.processors.DefaultProcessorURI;
import org.archive.util.Recorder;
import static org.archive.processors.extractor.LinkContext.EMBED_MISC;
import static org.archive.processors.extractor.LinkContext.NAVLINK_MISC;

/**
 * Unit test for ExtractorCSS.
 * 
 * @author pjack
 */
public class ExtractorCSSTest extends StringExtractorTestBase {

    /**
     * Test data. a[n] is sample CSS input, a[n + 1] is expected extracted URI
     */
    public static final String[] VALID_TEST_DATA = new String[] { "@import url(http://www.archive.org)", "http://www.archive.org", "@import url('http://www.archive.org')", "http://www.archive.org", "@import url(    \"  http://www.archive.org  \"   )", "http://www.archive.org", "table { border: solid black 1px}\n@import url(style.css)", "http://www.archive.org/start/style.css" };

    @Override
    protected Class getModuleClass() {
        return ExtractorCSS.class;
    }

    @Override
    protected Extractor makeExtractor() {
        return new ExtractorCSS();
    }

    @Override
    protected Collection<TestData> makeData(String content, String uri) throws Exception {
        UURI src = UURIFactory.getInstance("http://www.archive.org/start/");
        DefaultProcessorURI euri = new DefaultProcessorURI(src, NAVLINK_MISC);
        Recorder recorder = createRecorder(content);
        euri.setContentType("text/css");
        euri.setRecorder(recorder);
        euri.setContentLength(content.length());
        UURI dest = UURIFactory.getInstance(uri);
        Link link = new Link(src, dest, EMBED_MISC, Hop.EMBED);
        TestData td = new TestData(euri, link);
        return Collections.singleton(td);
    }

    @Override
    public String[] getValidTestData() {
        return VALID_TEST_DATA;
    }
}
