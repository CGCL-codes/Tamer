package org.fudaa.dodico.crue.io;

import java.io.File;
import java.util.List;
import org.fudaa.ctulu.CtuluLog;
import org.fudaa.dodico.crue.config.TestCrueConfigMetierLoaderDefault;
import org.fudaa.dodico.crue.io.common.CrueFileType;
import org.fudaa.dodico.crue.metier.emh.DonFrt;
import org.fudaa.dodico.crue.metier.emh.DonFrtStrickler;
import org.fudaa.dodico.crue.projet.coeur.CoeurConfigContrat;
import org.fudaa.dodico.crue.projet.coeur.TestCoeurConfig;

/**
 * Test en lecture/ecriture de DFRT
 */
public class TestCrueDFRTFile extends AbstractIOTestCase {

    /**
   * 
   */
    protected static final String FICHIER_TEST_DFRT_XML = "/v1_2/M3-0_c10.dfrt.xml";

    protected static final String FICHIER_TEST_DFRT_XML_V_1_1_1 = "/v1_1_1/M3-0_c10.dfrt.xml";

    public TestCrueDFRTFile() {
        super(Crue10FileFormatFactory.getVersion(CrueFileType.DFRT, TestCoeurConfig.INSTANCE), FICHIER_TEST_DFRT_XML);
    }

    public void testValideVersion1p1() {
        CtuluLog log = new CtuluLog();
        boolean valide = Crue10FileFormatFactory.getInstance().getFileFormat(CrueFileType.DFRT, TestCoeurConfig.getInstance(Crue10FileFormatFactory.V_1_1_1)).isValide("/v1_1_1/M3-0_c10.dfrt.xml", log);
        if (log.containsErrorOrFatalError()) log.printResume();
        assertTrue(valide);
    }

    /**
 * 
 */
    public void testLecture() {
        testDonFrt(readModele());
    }

    /**
   * @param frts
   */
    public void testDonFrt(final List<DonFrt> frts) {
        assertNotNull(frts);
        for (final DonFrt donfrt : frts) {
            assertNotNull(donfrt);
        }
        DonFrtStrickler donFrtList = (DonFrtStrickler) frts.get(0);
        assertEquals("Fk_K0", donFrtList.getNom());
        assertDoubleEquals(0, donFrtList.getLoi().getEvolutionFF().getPtEvolutionFF().get(0).getOrdonnee());
        donFrtList = (DonFrtStrickler) frts.get(1);
        assertEquals(0.00, donFrtList.getLoi().getEvolutionFF().getPtEvolutionFF().get(0).getAbscisse());
        assertEquals(10.00, donFrtList.getLoi().getEvolutionFF().getPtEvolutionFF().get(1).getAbscisse());
        assertEquals(15.00, donFrtList.getLoi().getEvolutionFF().getPtEvolutionFF().get(0).getOrdonnee());
        assertEquals(15.00, donFrtList.getLoi().getEvolutionFF().getPtEvolutionFF().get(1).getOrdonnee());
        donFrtList = (DonFrtStrickler) frts.get(2);
        assertEquals(0.00, donFrtList.getLoi().getEvolutionFF().getPtEvolutionFF().get(0).getAbscisse());
        assertEquals(10.00, donFrtList.getLoi().getEvolutionFF().getPtEvolutionFF().get(1).getAbscisse());
        assertEquals(30.00, donFrtList.getLoi().getEvolutionFF().getPtEvolutionFF().get(0).getOrdonnee());
        assertEquals(30.00, donFrtList.getLoi().getEvolutionFF().getPtEvolutionFF().get(1).getOrdonnee());
    }

    public List<DonFrt> readModele() {
        final CtuluLog analyse = new CtuluLog();
        final List<DonFrt> jeuDonnees = (List<DonFrt>) format.read(FICHIER_TEST_DFRT_XML, analyse, createDefault()).getMetier();
        analyse.printResume();
        assertFalse(analyse.containsErrorOrFatalError());
        return jeuDonnees;
    }

    public static List<DonFrt> readModeleLastVersion() {
        final CtuluLog analyse = new CtuluLog();
        final List<DonFrt> jeuDonnees = (List<DonFrt>) Crue10FileFormatFactory.getVersion(CrueFileType.DFRT, TestCoeurConfig.INSTANCE).read(FICHIER_TEST_DFRT_XML, analyse, createDefault()).getMetier();
        analyse.printResume();
        assertFalse(analyse.containsErrorOrFatalError());
        return jeuDonnees;
    }

    /**
   * Methode appelee JUNIT.
   */
    public void testEcriture() {
        final List<DonFrt> in = readModele();
        assertNotNull(in);
        final File f = createTempFile();
        testWrite(in, f);
        final CtuluLog analyse = new CtuluLog();
        final List<DonFrt> jeuDonnees = (List<DonFrt>) format.read(f, analyse, createDefault()).getMetier();
        testAnalyser(analyse);
        testDonFrt(jeuDonnees);
    }

    private File testWrite(final List<DonFrt> in, final File f) {
        final CtuluLog analyse = new CtuluLog();
        final boolean res = format.writeMetierDirect(in, f, analyse, TestCrueConfigMetierLoaderDefault.DEFAULT);
        testAnalyser(analyse);
        assertTrue(res);
        boolean valide = format.isValide(f, analyse);
        testAnalyser(analyse);
        assertTrue(valide);
        return f;
    }
}
