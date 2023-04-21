package org.omegat.gui.glossary;

import java.io.File;
import java.util.List;
import junit.framework.TestCase;

/**
 * @author Alex Buloichik <alex73mail@gmail.com>
 */
public class GlossaryReaderTSVTest extends TestCase {

    public void testRead() throws Exception {
        List<GlossaryEntry> g = GlossaryReaderTSV.read(new File("test/data/glossaries/test.tab"));
        assertEquals(2, g.size());
        assertEquals(g.get(0).getSrcText(), "kde");
        assertEquals(g.get(0).getLocText(), "koo moo");
        assertEquals(g.get(1).getSrcText(), "question");
        assertEquals(g.get(1).getLocText(), "qqqqq");
    }
}
