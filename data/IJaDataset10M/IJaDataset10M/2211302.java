package gate.creole.ml.maxent;

import junit.framework.*;
import gate.*;
import gate.corpora.*;
import java.io.File;
import java.net.*;
import gate.gui.MainFrame;
import gate.util.Files;
import gate.util.GateRuntimeException;

public class TestMaxentWrapper extends TestCase {

    private static final boolean DEBUG = false;

    public TestMaxentWrapper(String name) {
        super(name);
    }

    /**
   * Flag to ensure Gate.init is only called once.
   */
    private static boolean gateInited = false;

    private static synchronized void initGate() throws Exception {
        if (!gateInited) {
            File gateHome = new File(System.getProperty("gate.home.location"));
            Gate.setGateHome(gateHome);
            Gate.init();
            File anniePlugin = new File(System.getProperty("annie.plugin"));
            Gate.getCreoleRegister().registerDirectories(anniePlugin.toURI().toURL());
            File mlPlugin = new File(System.getProperty("machinelearning.plugin"));
            Gate.getCreoleRegister().registerDirectories(mlPlugin.toURI().toURL());
            gateInited = true;
        }
    }

    /** Fixture set up - init GATE*/
    public void setUp() throws Exception {
        initGate();
    }

    /** Fixture tear down - does nothing */
    public void tearDown() throws Exception {
    }

    /** Tests the MAXENT machine learning wrapper, by training it to identify
   * lookup annotations based on the precence of lookup annotations.
   */
    public void testMaxentWrapper() throws Exception {
        java.io.PrintStream normalOutputStream = System.out;
        if (DEBUG) {
            MainFrame mainFrame = new MainFrame();
            mainFrame.setVisible(true);
        } else {
            System.setOut(new java.io.PrintStream(new java.io.OutputStream() {

                public void write(int b) {
                }

                public void write(byte[] b, int off, int len) {
                }
            }));
        }
        Document doc = Factory.newDocument(new URL(TestDocument.getTestServerName() + "tests/doc0.html"));
        LanguageAnalyser tokeniser = (LanguageAnalyser) Factory.createResource("gate.creole.tokeniser.DefaultTokeniser");
        LanguageAnalyser gazetteerInst = (LanguageAnalyser) Factory.createResource("gate.creole.gazetteer.DefaultGazetteer");
        FeatureMap maxentParameters = Factory.newFeatureMap();
        maxentParameters.put("configFileURL", Files.getGateResource("/gate.ac.uk/tests/TestMaxentConfigFile.xml"));
        LanguageAnalyser maxentPR = (LanguageAnalyser) Factory.createResource("gate.creole.ml.MachineLearningPR", maxentParameters);
        tokeniser.setDocument(doc);
        tokeniser.execute();
        gazetteerInst.setDocument(doc);
        gazetteerInst.execute();
        maxentPR.setDocument(doc);
        maxentPR.execute();
        maxentPR.setParameterValue("training", Boolean.FALSE);
        maxentPR.execute();
        Factory.deleteResource(doc);
        Factory.deleteResource(tokeniser);
        Factory.deleteResource(maxentPR);
        Factory.deleteResource(gazetteerInst);
        System.setOut(normalOutputStream);
    }

    /** Test suite routine for the test runner */
    public static Test suite() {
        return new TestSuite(TestMaxentWrapper.class);
    }

    public static void main(String[] args) {
        try {
            Gate.init();
            TestMaxentWrapper testMax = new TestMaxentWrapper("");
            testMax.setUp();
            testMax.testMaxentWrapper();
            testMax.tearDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
