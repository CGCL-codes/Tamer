package org.fudaa.dodico.telemac;

import java.io.File;
import java.io.IOException;
import org.fudaa.ctulu.CtuluIOOperationSynthese;
import org.fudaa.dodico.common.TestIO;
import org.fudaa.dodico.h2d.telemac.H2dTelemacSeuilInterface;
import org.fudaa.dodico.telemac.io.TelemacSeuilSiphonContainer;
import org.fudaa.dodico.telemac.io.TelemacSeuilSiphonReader;
import org.fudaa.dodico.telemac.io.TelemacSeuilSiphonWriter;
import org.fudaa.dodico.telemac.io.TelemacSiphonItem;

/**
 * @author Fred Deniger
 * @version $Id: TestJSiphon.java,v 1.1 2007-01-19 13:07:17 deniger Exp $
 */
public class TestJSiphon extends TestIO {

    /**
   * Ouvre le fichier 'seuil-siphon.txt'.
   */
    public TestJSiphon() {
        super("seuil-siphon.txt");
    }

    /**
   * Test de la lecture.
   */
    public void testReader() {
        final TelemacSeuilSiphonReader r = new TelemacSeuilSiphonReader();
        r.setFile(fic_);
        r.setContainsSeuil(true);
        final CtuluIOOperationSynthese s = r.read();
        if (s.containsMessages()) {
            s.printAnalyze();
        }
        assertFalse(s.containsMessages());
        internTestInterface((TelemacSeuilSiphonContainer) s.getSource());
    }

    /**
   * Teste l'ecriture du fichier.
   */
    public void testWriter() {
        TelemacSeuilSiphonReader r = new TelemacSeuilSiphonReader();
        r.setFile(fic_);
        r.setContainsSeuil(true);
        CtuluIOOperationSynthese s = r.read();
        if (s.containsMessages()) {
            s.printAnalyze();
        }
        assertFalse(s.containsMessages());
        final TelemacSeuilSiphonWriter w = new TelemacSeuilSiphonWriter();
        try {
            final File f = File.createTempFile("fudaaSiphon", ".txt");
            f.deleteOnExit();
            w.setFile(f);
            s = w.write(s.getSource());
            if (s.containsMessages()) {
                s.printAnalyze();
            }
            assertFalse(s.containsMessages());
            r = new TelemacSeuilSiphonReader();
            r.setFile(f);
            r.setContainsSeuil(true);
            s = r.read();
            if (s.containsMessages()) {
                s.printAnalyze();
            }
            assertFalse(s.containsMessages());
            internTestInterface((TelemacSeuilSiphonContainer) s.getSource());
            f.delete();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    protected void internTestInterface(final TelemacSeuilSiphonContainer _c) {
        assertNotNull(_c);
        assertEquals(2, _c.getNbSeuil());
        H2dTelemacSeuilInterface it = _c.getSeuil(0);
        assertEquals(37, it.getNbPoint());
        int[] c1 = new int[] { 555, 556, 557, 558, 559, 560, 561, 562, 563, 564, 565, 566, 567, 568, 569, 570, 571, 572, 573, 574, 575, 576, 577, 578, 579, 580, 581, 582, 583, 584, 585, 586, 587, 588, 589, 590, 591 };
        int[] c2 = new int[] { 552, 551, 550, 549, 548, 547, 546, 545, 544, 543, 542, 541, 540, 539, 538, 537, 536, 535, 534, 533, 532, 531, 530, 529, 528, 527, 526, 525, 524, 523, 522, 521, 520, 519, 518, 517, 516 };
        for (int i = it.getNbPoint() - 1; i >= 0; i--) {
            assertEquals(c1[i] - 1, it.getPtIdx1(i));
            assertEquals(c2[i] - 1, it.getPtIdx2(i));
            assertDoubleEquals(3.6, it.getCote(i));
            assertDoubleEquals(0.4, it.getCoefDebit(i));
        }
        c1 = new int[] { 444, 445, 446, 447, 448, 449, 450, 451, 452, 453, 454, 455, 456, 457, 458, 459, 460, 461, 462, 463, 464, 465, 466, 467, 468, 469, 470, 471, 472, 473, 474, 475, 476, 477, 478, 479, 480 };
        c2 = new int[] { 503, 502, 501, 500, 499, 498, 497, 496, 495, 531, 530, 529, 528, 527, 526, 525, 524, 523, 522, 521, 520, 519, 518, 517, 516, 515, 514, 513, 512, 511, 510, 509, 508, 507, 506, 505, 504 };
        it = _c.getSeuil(1);
        for (int i = it.getNbPoint() - 1; i >= 0; i--) {
            assertEquals(c1[i] - 1, it.getPtIdx1(i));
            assertEquals(c2[i] - 1, it.getPtIdx2(i));
            assertDoubleEquals(3.6, it.getCote(i));
            assertDoubleEquals(0.4, it.getCoefDebit(i));
        }
        assertEquals(12, _c.getNbSiphon());
        assertDoubleEquals(0.2, _c.getRelaxation());
        c1 = new int[] { 19, 21, 23, 25, 27, 29, 31, 33, 35, 37, 39, 41 };
        c2 = new int[c1.length];
        for (int i = c2.length - 1; i >= 0; i--) {
            c2[i] = c1[i] + 1;
        }
        final double[] s12 = new double[] { 4.0, 0.9, 0.72, 0.78, 0.78, 0.78, 0.20, 0.20, 0.78, 0.01, 0.01, 0.01 };
        final double[] z1 = new double[] { 8.01, 6.35, 15.51, 6.16, 6.02, 3.20, 7.31, 3.68, 3.15, 4.57, 2.33, 1.93 };
        final double[] z2 = new double[] { 7.71, 0, 8.61, 0, 4.68, 0, 4.51, 3.62, 3.1, 0, 0, 0 };
        for (int i = 0; i < _c.getNbSiphon(); i++) {
            final TelemacSiphonItem si = _c.getSiphonTest(i);
            assertEquals(c1[i], si.getI1() + 1);
            assertEquals(c1[i], si.getSource1() + 1);
            assertEquals(c2[i], si.getI2() + 1);
            assertEquals(c2[i], si.getSource2() + 1);
            int idx = 0;
            assertDoubleEquals(0, si.getD1());
            assertDoubleEquals(0, si.getValue(idx++));
            assertDoubleEquals(0, si.getD2());
            assertDoubleEquals(0, si.getValue(idx++));
            assertDoubleEquals(0.5, si.getCe1());
            assertDoubleEquals(0.5, si.getValue(idx++));
            assertDoubleEquals(0.5, si.getCe2());
            assertDoubleEquals(0.5, si.getValue(idx++));
            assertDoubleEquals(1, si.getCs1());
            assertDoubleEquals(1, si.getValue(idx++));
            assertDoubleEquals(1, si.getCs2());
            assertDoubleEquals(1, si.getValue(idx++));
            assertDoubleEquals(s12[i], si.getS12());
            assertDoubleEquals(s12[i], si.getValue(idx++));
            assertDoubleEquals(0, si.getL12());
            assertDoubleEquals(0, si.getValue(idx++));
            assertDoubleEquals(z1[i], si.getZ1());
            assertDoubleEquals(z1[i], si.getValue(idx++));
            assertDoubleEquals(z2[i], si.getZ2());
            assertDoubleEquals(z2[i], si.getValue(idx++));
            assertDoubleEquals(0, si.getA1());
            assertDoubleEquals(0, si.getValue(idx++));
            assertDoubleEquals(0, si.getA2());
            assertDoubleEquals(0, si.getValue(idx++));
        }
    }
}
