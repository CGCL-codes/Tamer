package de.dNb.conversion.converters;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Properties;
import junit.extensions.RepeatedTest;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import de.ddb.conversion.CharstreamConverter;

/**
 * @author kleinm, German National Library
 */
public class TestConverter extends TestCase {

    private static final Log logger = LogFactory.getLog(TestConverter.class);

    /**
	 * Properties-Dateien für Testdaten: Zähler
	 */
    protected int propFileCounter = 0;

    /**
	 * Der zu testende XML-Konverter
	 */
    protected CharstreamConverter converter = null;

    /**
	 * Zu konvertierende Eingabedaten
	 */
    protected Reader inReader = null;

    /**
	 * Konvertierten Ausgabedaten
	 */
    protected Writer outWriter = null;

    /**
	 * Properties-Dateien für Testdaten: Property "Name der Eingabedatei"
	 */
    public static final String FILE_NAME_IN = "fileNameIn";

    /**
	 * Properties-Dateien für Testdaten: Property "Name der Ausgabedatei"
	 */
    public static final String FILE_NAME_OUT = "fileNameOut";

    /**
	 * @see #getTestProperties(String)
	 * @author t_kleinm, German National Library
	 * @param propFileName
	 * @return Properties-Objekt
	 * @deprecated
	 */
    @Deprecated
    protected Properties getInputOutputFiles(String propFileName) {
        return getTestProperties(propFileName);
    }

    /**
	 * <p>
	 * Lädt die Properties-Datei für die Konversionstests.
	 * </p>
	 * 
	 * @param propFileName
	 *            Name der Properties-Datei
	 * @return Properties-Objekt
	 * @author t_kleinm, German National Library
	 */
    protected Properties getTestProperties(String propFileName) {
        logger.info("getTestProperties() - start");
        Properties props = new Properties();
        try {
            logger.info("getTestProperties() - loading " + propFileName);
            props = TestUtil.getPropertiesFromClasspath(propFileName);
            String fileNameIn = props.getProperty(FILE_NAME_IN);
            if (fileNameIn == null || fileNameIn.length() == 0) {
                fail("Error: Can not get property " + FILE_NAME_IN + " in properties file " + propFileName);
            } else {
                logger.info("getTestProperties() - " + FILE_NAME_IN + "= " + fileNameIn);
            }
            String fileNameOut = props.getProperty(FILE_NAME_OUT);
            if (fileNameOut == null || fileNameOut.length() == 0) {
                fail("Error: Can not get property " + FILE_NAME_OUT + " in properties file " + propFileName);
            } else {
                logger.info("getTestProperties() - " + FILE_NAME_OUT + "= " + fileNameOut);
            }
        } catch (Exception e) {
            logger.error("getTestProperties() - e= ", e);
            fail("Error: Can not get properties file " + propFileName + " in classpath");
        }
        logger.info("getTestProperties() - end");
        return props;
    }

    /**
	 * <p>
	 * Die JUnit-Tests sollen mit ganz verschiedenen Testdaten möglich sein.
	 * Deshalb muss für jedes zu testende Datenkontingent eine eigene
	 * Properties-Datei angelegt werden, welche den Namen der Ein- und
	 * Ausgabe-Datei enthält.
	 * </p>
	 * <p>
	 * Hinweis: Jede Testfall-Klasse definiert implizit eine eigene
	 * suite-Methode, in der alle Testfall-Methoden eingebunden werden, die in
	 * der betreffenden Klasse definiert wurden. JUnit erledigt diesen Teil
	 * automatisch mittels Reflection (siehe <a
	 * href="http://www.frankwestphal.de/UnitTestingmitJUnit.html"
	 * >http://www.frankwestphal.de/UnitTestingmitJUnit.html</a>.
	 * </p>
	 * 
	 * @param pathToPropFiles
	 *            Properties-Dateien für Testdaten: Pfandangabe
	 * @param prefix
	 *            Properties-Dateien für Testdaten: Dateinamenspräfix (Wert bis
	 *            zur Zählung)
	 * @param propFileSuffix
	 *            Properties-Dateien für Testdaten: Dateinamenssuffix (Wert nach
	 *            der Zählung)
	 * @param suitName
	 *            Name der Test-Suite
	 * @param testClass
	 *            Test-Klasse
	 * @return RepeatedTest-Objekt
	 * @author t_kleinm, German National Library
	 */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    protected static Test suite(String pathToPropFiles, String prefix, String propFileSuffix, String suitName, Class testClass) {
        int i = 0;
        while (true) {
            try {
                Properties prop = new Properties();
                String propFileName = pathToPropFiles + prefix + (i + 1) + propFileSuffix;
                prop.load(ClassLoader.getSystemClassLoader().getResourceAsStream(propFileName));
                logger.info("suite() - propFileName= " + propFileName);
                ++i;
            } catch (Exception e) {
                break;
            }
        }
        logger.info("suite() - " + i + " properties file(s) are available");
        TestSuite testSuite = new TestSuite(suitName);
        testSuite.addTestSuite(testClass);
        logger.info("suite() - end");
        return new RepeatedTest(testSuite, (i < 1) ? 1 : i);
    }

    /**
	 * @see de.ddb.conversion.GenericConverter#convert(Reader, Writer)
	 * @param propFileName
	 *            Name der Properties-Datei für Testdaten
	 * @throws Exception
	 */
    protected void testConvert(String propFileName) throws Exception {
        logger.info("testConvert() - start");
        logger.info("testConvert() - propFileName= " + propFileName);
        Properties props = getTestProperties(propFileName);
        inReader = new FileReader(props.getProperty(FILE_NAME_IN));
        outWriter = new FileWriter(props.getProperty(FILE_NAME_OUT));
        converter.convert(inReader, outWriter);
        logger.info("testConvert() - end");
    }

    /**
	 * @see junit.framework.TestCase#tearDown()
	 * @author t_kleinm, German National Library
	 */
    @Override
    protected void tearDown() throws Exception {
        logger.info("tearDown() - start");
        logger.info("tearDown() - setting converter, readers and writers to null");
        converter = null;
        if (inReader != null) inReader.close();
        if (outWriter != null) outWriter.close();
        inReader = null;
        outWriter = null;
        logger.info("tearDown() - end");
    }

    /**
	 * <p>
	 * Erzeugt den Namen der Properties-Datei für Testdaten.
	 * </p>
	 * 
	 * @param pathToPropFiles
	 *            Properties-Dateien für Testdaten: Pfadangabe
	 * @param propFilePrefix
	 *            Properties-Dateien für Testdaten: Dateinamenspräfix (Wert bis
	 *            zur Zählung)
	 * @param propFileSuffix
	 *            Properties-Dateien für Testdaten: Dateinamenssuffix (Wert nach
	 *            der Zählung)
	 * @return Name der Properties-Datei für Testdaten
	 * @author t_kleinm, German National Library
	 */
    protected String getPropFileName(String pathToPropFiles, String propFilePrefix, String propFileSuffix) {
        return pathToPropFiles + propFilePrefix + ++propFileCounter + propFileSuffix;
    }
}
