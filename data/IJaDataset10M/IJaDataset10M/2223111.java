package ishima.qa;

import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ishima.gui.MainForm;
import ishima.ini.ConfigFile;
import ishima.sql.SQLConnection;
import ishima.sql.SQLManager;

public class PMDTest {

    private String projectUNIXName;

    private String tempDir;

    private String pmdDir;

    public int rulesetsNo = 12;

    public int prioritiesNo = 4;

    public String[] rulesets;

    public Integer[] rulesetErrors;

    public Integer[] errorPriorities;

    public PMDTest(String UNIXName) {
        projectUNIXName = UNIXName;
        rulesets = new String[rulesetsNo];
        rulesetErrors = new Integer[rulesetsNo];
        for (int i = 0; i < rulesetsNo; i++) rulesetErrors[i] = new Integer(0);
        rulesets[0] = "Basic Rules";
        rulesets[1] = "Design Rules";
        rulesets[2] = "Unused Code Rules";
        rulesets[3] = "Coupling Rules";
        rulesets[4] = "Security Code Guidelines";
        rulesets[5] = "Optimization Rules";
        rulesets[6] = "Import Statement Rules";
        rulesets[7] = "Java Logging Rules";
        rulesets[8] = "Strict Exception Rules";
        rulesets[9] = "Naming Rules";
        rulesets[10] = "Clone Implementation Rules";
        rulesets[11] = "String and StringBuffer Rules";
        errorPriorities = new Integer[prioritiesNo + 1];
        for (int i = 0; i <= prioritiesNo; i++) errorPriorities[i] = 0;
    }

    private int getValue(String query) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        domFactory.setNamespaceAware(true);
        DocumentBuilder builder = domFactory.newDocumentBuilder();
        Document doc = builder.parse(pmdDir + "result.xml");
        XPath xpath = XPathFactory.newInstance().newXPath();
        XPathExpression expr = xpath.compile(query);
        Object result = expr.evaluate(doc, XPathConstants.NODESET);
        NodeList nodes = (NodeList) result;
        return nodes.getLength();
    }

    public void run() throws InterruptedException {
        SQLConnection con = new SQLConnection("settings.ini");
        SQLManager man = new SQLManager(con);
        System.out.println("PMD rozpoczal przeliczanie " + projectUNIXName);
        ConfigFile cf = new ConfigFile("settings.ini");
        pmdDir = cf.getPMD();
        pmdDir = pmdDir.replace("\"", " ").trim();
        tempDir = cf.getTempDir();
        tempDir = tempDir.replace("\"", " ").trim();
        String command = pmdDir + "pmd.bat ";
        String projectDir = tempDir + "\\" + projectUNIXName;
        String targetFile = pmdDir + "result.xml";
        String param = projectDir + " xml " + pmdDir + "ruleset.xml" + " >" + targetFile;
        String cmd = command + param;
        boolean testSucceed = ishima.core.RunCommand.run(cmd);
        if (testSucceed) {
            try {
                String query;
                for (int i = 0; i < rulesetsNo; i++) {
                    query = "/pmd/file/violation[@ruleset=\"" + rulesets[i] + "\"]";
                    rulesetErrors[i] = getValue(query);
                }
                for (int i = 1; i <= prioritiesNo; i++) {
                    query = "/pmd/file/violation[@priority=" + i + "]";
                    errorPriorities[i] = getValue(query);
                }
            } catch (XPathExpressionException e) {
                System.err.println("PMD XPathExpressionException: " + e.getMessage());
            } catch (ParserConfigurationException e) {
                System.err.println("PMD ParserConfigurationException: " + e.getMessage());
            } catch (SAXException e) {
                System.err.println("PMD SAXException: " + e.getMessage());
            } catch (IOException e) {
                System.err.println("PMD IOException: " + e.getMessage());
            } catch (OutOfMemoryError e) {
                System.err.println("PMD OutOfMemoryError: " + e.getMessage());
            }
        }
        man.addPMDResult(this);
        MainForm.add2Log("PMD zbada� " + projectUNIXName);
        System.out.println("PMD zbada� " + projectUNIXName);
        MainForm.updateResults();
    }

    public String getProjectUNIXName() {
        return projectUNIXName;
    }

    public void setProjectUNIXName(String projectUNIXName) {
        this.projectUNIXName = projectUNIXName;
    }
}
