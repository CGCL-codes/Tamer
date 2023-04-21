package com.saic.ship.fsml;

import java.io.*;
import java.util.*;
import junit.framework.*;
import java.net.*;
import com.saic.ship.data.*;

/**
  * @author	  Eric Wood
  * @classname	  ClassName
  * @see          Closely associated classes, especially within a package.
  * @since	  mm-dd-yy
  */
public class FsmlProcessorTest extends TestCase {

    private static final String FSML_URL = "tests/work/LAMP.fsml";

    private static final File INPUT_FILE = new File("tests/work/original.in");

    private static final File OUTPUT_FILE = new File("tests/work/new.in");

    private static final String FSML_URL_LETHEVAL = "tests/work/LethEvalFsml_dat.xml";

    private static final File INPUT_FILE_LETHEVAL = new File("tests/work/letheval.dat");

    private static final File OUTPUT_FILE_LETHEVAL = new File("tests/work/letheval.out");

    private static final String FSML_URL_MIT5D = "tests/work/MIT5DInp.fsml";

    private static final File INPUT_FILE_MIT5D = new File("tests/work/MIT5D_original.inp");

    private static final File OUTPUT_FILE_MIT5D = new File("tests/work/MIT5D_new.inp");

    private static final double[] OMEGA = { .25, .325, .35, .375, .425, .45, .475, .525, .55, .575, .625, .65, .725, .75, .85, .95 };

    private static final String FSML_URL_LAMP = "tests/work/LAMP.fsml";

    private static final File INPUT_FILE_LAMP = new File("tests/work/LAMP_original.in");

    private static final File OUTPUT_FILE_LAMP = new File("tests/work/LAMP_new.in");

    private static HashMap parameters = new HashMap();

    private FsmlDocument fsmlDoc;

    private DefaultFsmlProcessor processor = new DefaultFsmlProcessor();

    /**
  * Constructor description
*/
    public FsmlProcessorTest(String testName) {
        super(testName);
    }

    public void setUp() {
        try {
            URL url = new File(FSML_URL).toURL();
            fsmlDoc = new FsmlDocument(url.toExternalForm());
            fsmlDoc.init();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to parse or validate FSML document");
        }
    }

    public void tearDown() {
    }

    public void testInitRead() {
        try {
            URL url = new File("tests/work/init.xml").toURL();
            FsmlDocument doc = new FsmlDocument(url.toExternalForm());
            doc.init();
            Hashtable initParams = new Hashtable();
            processor.setParameters(initParams);
            processor.readFile(doc, new File("tests/work/init_template.dat"));
            Assert.assertTrue(Double.parseDouble(initParams.get("NumSegments").toString()) == 4);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to read file correctly");
        }
    }

    public void testReadLetheval() {
        try {
            URL url = new File(FSML_URL_LETHEVAL).toURL();
            FsmlDocument doc = new FsmlDocument(url.toExternalForm());
            doc.init();
            Hashtable initParams = new Hashtable();
            processor.setParameters(initParams);
            processor.readFile(doc, INPUT_FILE_LETHEVAL);
            Assert.assertTrue(Double.parseDouble(initParams.get("Torpedo_Diameter").toString()) == 0.3048);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to read file correctly");
        }
    }

    public void testReadMIT5D() {
        try {
            URL url = new File(FSML_URL_MIT5D).toURL();
            FsmlDocument doc = new FsmlDocument(url.toExternalForm());
            doc.init();
            Hashtable initParams = new Hashtable();
            processor.setParameters(initParams);
            processor.readFile(doc, INPUT_FILE_MIT5D);
            Object key = initParams.get("NBK");
            if (key instanceof ArrayImpl && ((ArrayImpl) key).getBaseType() == ArrayBaseTypes.FLOAT) {
                ArrayImpl value = (ArrayImpl) key;
                int[] dims = value.getDims();
                Assert.assertTrue(dims.length == 1);
                Vector data = value.getData();
                for (int i = 0; i < dims[0]; i++) {
                    Assert.assertTrue(((Double) data.get(i)).doubleValue() == OMEGA[i]);
                }
            } else {
                Assert.assertTrue(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to read file correctly");
        }
    }

    public void testWriteMIT5D() {
        try {
            URL url = new File(FSML_URL_MIT5D).toURL();
            FsmlDocument fsmlDoc = new FsmlDocument(url.toExternalForm());
            fsmlDoc.init();
            Hashtable initParams = new Hashtable();
            processor.setParameters(initParams);
            processor.readFile(fsmlDoc, INPUT_FILE_MIT5D);
            processor.writeFile(fsmlDoc, OUTPUT_FILE_MIT5D);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to write file correctly");
        }
    }

    public void testReadLAMP() {
        try {
            URL url = new File(FSML_URL_LAMP).toURL();
            FsmlDocument doc = new FsmlDocument(url.toExternalForm());
            doc.init();
            Hashtable initParams = new Hashtable();
            processor.setParameters(initParams);
            processor.readFile(doc, INPUT_FILE_LAMP);
            Assert.assertTrue(Integer.parseInt(initParams.get("NBK").toString()) == 0);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to read file correctly");
            Assert.assertTrue(false);
        }
    }

    public void testWriteLAMP() {
        try {
            URL url = new File(FSML_URL_LAMP).toURL();
            FsmlDocument fsmlDoc = new FsmlDocument(url.toExternalForm());
            fsmlDoc.init();
            Hashtable initParams = new Hashtable();
            processor.setParameters(initParams);
            processor.readFile(fsmlDoc, INPUT_FILE_LAMP);
            processor.writeFile(fsmlDoc, OUTPUT_FILE_LAMP);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Failed to write file correctly");
            Assert.assertTrue(false);
        }
    }
}
