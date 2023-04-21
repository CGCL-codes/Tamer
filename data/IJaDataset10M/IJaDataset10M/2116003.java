package org.fudaa.dodico.h2d.rubar;

import java.io.File;
import java.io.IOException;
import org.fudaa.ctulu.CtuluIOOperationSynthese;
import org.fudaa.dodico.all.TestIO;
import org.fudaa.dodico.h2d.type.H2dRubarOuvrageType;
import org.fudaa.dodico.rubar.io.RubarOUVReader;
import org.fudaa.dodico.rubar.io.RubarOUVWriter;
import org.fudaa.dodico.rubar.io.RubarOuvrageElementaireApportDebit;

/**
 * @author Fred Deniger
 * @version $Id: TestJOUV.java,v 1.4 2006-10-19 14:12:29 deniger Exp $
 */
public class TestJOUV extends TestIO {

    /**
   * Ouvre le fichier "OYOAME.OUV" 
   */
    public TestJOUV() {
        super("OYOAME.OUV");
        ;
    }

    protected H2dRubarOuvrageContainer readFic() {
        final RubarOUVReader r = new RubarOUVReader();
        r.setFile(fic_);
        final CtuluIOOperationSynthese s = r.read();
        if (s.containsMessages()) {
            s.printAnalyze();
        }
        assertFalse(s.containsMessages());
        return (H2dRubarOuvrageContainer) s.getSource();
    }

    protected void testInterface(final H2dRubarOuvrageContainer _c) {
        assertNotNull(_c);
        assertEquals(36, _c.getNbOuvrage());
        final int[] ouvrageInterne = new int[] { 0, 2, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 1, 1, 1 };
        final H2dRubarOuvrageType[] t = new H2dRubarOuvrageType[36];
        for (int i = 0; i < 36; i++) {
            if (i == 1) {
                t[1] = H2dRubarOuvrageType.DEVERSOIR;
            } else if (i >= 33) {
                t[i] = H2dRubarOuvrageType.APPORT_DEBIT;
            } else {
                t[i] = H2dRubarOuvrageType.TRANSFERT_DEBIT;
            }
        }
        for (int i = 35; i >= 0; i--) {
            assertEquals(-2, _c.getOuvrage(i).getRubarRef());
            assertEquals(0, _c.getOuvrage(i).getNbMailleIntern());
            assertEquals(ouvrageInterne[i], _c.getOuvrage(i).getNbOuvrageElementaires());
            for (int j = _c.getOuvrage(i).getNbOuvrageElementaires() - 1; j >= 0; j--) {
                assertEquals(t[i], _c.getOuvrage(i).getOuvrageElementaire(j).getType());
            }
        }
        final H2dRubarOuvrageI o = _c.getOuvrage(35);
        final H2dRubarOuvrageElementaireInterface iele = o.getOuvrageElementaire(o.getNbOuvrageElementaires() - 1);
        assertTrue(iele instanceof RubarOuvrageElementaireApportDebit);
        final H2dRubarOuvrageElementaireTransfertDebitI d = (H2dRubarOuvrageElementaireTransfertDebitI) iele;
        final double[] tq = new double[] { 9000.000, 54000.000, 66420.000, 87192.000, 136692.000 };
        final double[] q = new double[] { 4.800, 27.710, 19.300, 15.180, 11.040 };
        assertEquals(tq.length, d.getEvol().getNbValues());
        for (int i = tq.length - 1; i >= 0; i--) {
            assertDoubleEquals(tq[i], d.getEvol().getXAt(i));
            assertDoubleEquals(q[i], d.getEvol().getYAt(i));
        }
    }

    /**
   * Test l'ecriture
   */
    public void testEcriture() {
        File f = null;
        try {
            f = File.createTempFile("fudaa", ".ouv");
            assertNotNull(f);
            final H2dRubarOuvrageContainer c = readFic();
            assertNotNull(c);
            final RubarOUVWriter w = new RubarOUVWriter();
            w.setFile(f);
            CtuluIOOperationSynthese s = w.write(c);
            if (s.containsMessages()) {
                s.printAnalyze();
            }
            assertFalse(s.containsMessages());
            final RubarOUVReader r = new RubarOUVReader();
            r.setFile(f);
            s = r.read();
            if (s.containsMessages()) {
                s.printAnalyze();
            }
            assertFalse(s.containsMessages());
            testInterface((H2dRubarOuvrageContainer) s.getSource());
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            if (f != null) {
                f.delete();
            }
        }
    }

    /**
   * Teste la lecture d'un fichier.
   */
    public void testLecture() {
        testInterface(readFic());
    }
}
