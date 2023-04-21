package org.fudaa.dodico.crue.config;

import org.fudaa.ctulu.CtuluLog;
import org.fudaa.dodico.crue.common.AbstractTestCase;
import org.fudaa.dodico.crue.io.common.CrueIOResu;
import org.fudaa.dodico.crue.projet.coeur.TestCoeurConfig;
import org.joda.time.Duration;

/**
 * @author deniger Test de CrueNatureLoader.
 */
public class TestCrueConfigMetierLoader extends AbstractTestCase {

    public void testXsd() {
        CrueConfigMetierReaderXML loader = new CrueConfigMetierReaderXML(TestCoeurConfig.INSTANCE);
        CtuluLog log = new CtuluLog();
        boolean valide = loader.isValide(TestCrueConfigMetierLoaderDefault.CONFIGMETIER_DEFAULT_FILE, log);
        if (log.containsErrorOrFatalError()) {
            log.printResume();
        }
        assertTrue(valide);
    }

    public void testXsd1_1_1() {
        CrueConfigMetierReaderXML loader = new CrueConfigMetierReaderXML(TestCoeurConfig.INSTANCE_1_1_1);
        CtuluLog log = new CtuluLog();
        boolean valide = loader.isValide(TestCrueConfigMetierLoaderDefault.CONFIGMETIER_FILENAME_V_1_1_1, log);
        if (log.containsErrorOrFatalError()) {
            log.printResume();
        }
        assertTrue(valide);
    }

    public void testPdtPerm() {
        CrueConfigMetier props = TestCrueConfigMetierLoaderDefault.DEFAULT;
        int val = (int) props.getDefaultDoubleValue("pdtPerm");
        assertEquals(3600, val);
        Duration defaultDurationValue = props.getDefaultDurationValue("pdtPerm");
        assertEquals(3600, defaultDurationValue.getStandardSeconds());
    }

    /**
   * TODO completer le fichiers CrueConfigMetier.xml avec le contenu de crue-variable.csv et crue-nature.csv.
   * 
   * @see CrueConfigMetierReaderXML
   * @return
   */
    public void testDefaultFile() {
        CrueConfigMetierLoader loader = new CrueConfigMetierLoader();
        CrueIOResu<CrueConfigMetier> load = loader.load(TestCrueConfigMetierLoaderDefault.CONFIGMETIER_DEFAULT_FILE, TestCoeurConfig.INSTANCE);
        assertNotNull(load);
        testAnalyser(load.getAnalyse());
    }

    public void testFile1_1_1() {
        CrueConfigMetierLoader loader = new CrueConfigMetierLoader();
        CrueIOResu<CrueConfigMetier> load = loader.load(TestCrueConfigMetierLoaderDefault.CONFIGMETIER_FILENAME_V_1_1_1, TestCoeurConfig.INSTANCE_1_1_1);
        assertNotNull(load);
        testAnalyser(load.getAnalyse());
    }
}
