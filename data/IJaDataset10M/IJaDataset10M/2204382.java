package org.fudaa.dodico.rubar;

import java.io.File;
import java.io.IOException;
import org.fudaa.ctulu.CtuluIOOperationSynthese;
import org.fudaa.dodico.common.TestIO;
import org.fudaa.dodico.h2d.rubar.H2dRubarDTRResult;
import org.fudaa.dodico.rubar.io.RubarDTRReader;
import org.fudaa.dodico.rubar.io.RubarDTRWriter;

/**
 * @author Fred Deniger
 * @version $Id: TestJDTR.java,v 1.1 2007-01-19 13:07:16 deniger Exp $
 */
public class TestJDTR extends TestIO {

    public TestJDTR() {
        super("sect24.dtr");
    }

    protected void testInterface(final H2dRubarDTRResult res) {
        assertDoubleEquals(10, res.getTimeStep());
        assertEquals(41, res.getNbPoint());
        int idx = 19;
        assertDoubleEquals(811338.1, res.getX(idx));
        assertDoubleEquals(284371.4, res.getY(idx));
        idx = 40;
        assertDoubleEquals(811094.2, res.getX(idx));
        assertDoubleEquals(284567.1, res.getY(idx));
    }

    public void testLecture() {
        final RubarDTRReader r = new RubarDTRReader();
        r.setFile(fic_);
        final CtuluIOOperationSynthese s = r.read();
        if (s.containsMessages()) {
            s.printAnalyze();
        }
        assertFalse(s.containsMessages());
        testInterface((H2dRubarDTRResult) s.getSource());
    }

    public void testEcriture() {
        RubarDTRReader r = new RubarDTRReader();
        r.setFile(fic_);
        CtuluIOOperationSynthese s = r.read();
        final RubarDTRWriter w = new RubarDTRWriter();
        File f = null;
        try {
            f = File.createTempFile("rubar", ".dtr");
            w.setFile(f);
            w.write(s.getSource());
            r = new RubarDTRReader();
            r.setFile(f);
            s = r.read();
            if (s.containsMessages()) {
                s.printAnalyze();
            }
            assertFalse(s.containsMessages());
            testInterface((H2dRubarDTRResult) s.getSource());
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            if (f != null) {
                f.delete();
            }
        }
    }
}
