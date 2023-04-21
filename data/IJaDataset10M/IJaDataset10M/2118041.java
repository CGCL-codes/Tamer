package org.omegat.filters;

import java.util.List;
import org.omegat.filters3.xml.docbook.DocBookFilter;

public class DocBookFilterTest extends TestFilterBase {

    public void testParse() throws Exception {
        List<String> lines = parse(new DocBookFilter(), "test/data/filters/docBook/file-DocBookFilter.xml");
        boolean c = lines.contains("My String");
        assertTrue("'My String' not defined'", c);
    }

    public void testTranslate() throws Exception {
        translateText(new DocBookFilter(), "test/data/filters/docBook/file-DocBookFilter.xml");
    }

    public void testParseIntroLinux() throws Exception {
        List<String> lines = parse(new DocBookFilter(), "test/data/filters/docBook/Intro-Linux/abook.xml");
        assertTrue("Message not exist, i.e. entities not loaded", lines.contains("Why should I use an editor?"));
    }
}
