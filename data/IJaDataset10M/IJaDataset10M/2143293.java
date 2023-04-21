package org.fudaa.dodico.rubar;

import java.io.File;
import java.io.IOException;
import org.fudaa.ctulu.CtuluIOOperationSynthese;
import org.fudaa.dodico.common.TestIO;
import org.fudaa.dodico.mesure.EvolutionReguliereInterface;
import org.fudaa.dodico.rubar.io.RubarAPPFileFormat;
import org.fudaa.dodico.rubar.io.RubarAPPResult;

/**
 * @author Fred Deniger
 * @version $Id: TestJAPP.java,v 1.2 2007-05-22 13:11:21 deniger Exp $
 */
public class TestJAPP extends TestIO {

    int nbElt_ = 10528;

    /**
   * Definition fichier nim07.app.
   */
    public TestJAPP() {
        super("nime07.app");
    }

    private void internTest(final CtuluIOOperationSynthese _op) {
        if (_op.containsMessages()) {
            _op.printAnalyze();
        }
        assertFalse(_op.containsFatalError());
        final RubarAPPResult m = (RubarAPPResult) _op.getSource();
        assertEquals(nbElt_, m.getNbElt());
        int idx = 0;
        assertEquals(1, m.getEvolIdx(idx++));
        assertEquals(1, m.getEvolIdx(idx++));
        assertEquals(1, m.getEvolIdx(idx++));
        assertEquals(0, m.getEvolIdx(idx++));
        assertEquals(0, m.getEvolIdx(idx++));
        assertEquals(0, m.getEvolIdx(idx++));
        assertEquals(0, m.getEvolIdx(idx++));
        assertEquals(1, m.getEvolIdx(idx++));
        assertEquals(1, m.getEvolIdx(idx++));
        assertEquals(1, m.getEvolIdx(idx++));
        idx = nbElt_;
        for (int i = 0; i < 14; i++) {
            assertEquals(1, m.getEvolIdx(--idx));
        }
        assertEquals(0, m.getEvolIdx(--idx));
        assertEquals(2, m.getNbEvol());
        EvolutionReguliereInterface evol = m.getEvol(0);
        assertNotNull(evol);
        assertEquals(2, evol.getNbValues());
        assertDoubleEquals(10800, evol.getX(0));
        assertDoubleEquals(14400, evol.getX(1));
        assertDoubleEquals(500, evol.getY(0));
        assertDoubleEquals(0, evol.getY(1));
        evol = m.getEvol(1);
        assertEquals(1, evol.getNbValues());
        assertDoubleEquals(0, evol.getX(0));
        assertDoubleEquals(0, evol.getY(0));
        assertNotNull(evol);
    }

    /**
   * test lecture.
   */
    public void testEcriture() {
        CtuluIOOperationSynthese op = new RubarAPPFileFormat().read(fic_, null, nbElt_);
        File f = null;
        try {
            f = File.createTempFile("fudaa", "app");
            assertNotNull(f);
            op = new RubarAPPFileFormat().write(f, op.getSource(), null);
            assertFalse(op.containsMessages());
            internTest(new RubarAPPFileFormat().read(f, null, nbElt_));
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            if (f != null) {
                f.delete();
            }
        }
    }

    /**
   * test lecture.
   */
    public void testLecture() {
        final CtuluIOOperationSynthese op = new RubarAPPFileFormat().read(fic_, null, nbElt_);
        internTest(op);
    }
}
