package com.abiquo.test.middleware;

import java.util.Iterator;
import java.util.List;
import org.custommonkey.xmlunit.XMLUnit;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.abiquo.framework.config.FrameworkConfiguration;
import com.abiquo.util.test.XMLHelper;

/**
 * Driver to launch message test configured through an XML file.
 * 
 * @author abiquo
 */
public class MessageTestDriver {

    /**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: <tests descriptor file> <tests location>");
            System.exit(1);
        }
        XMLUnit.setIgnoreWhitespace(true);
        MessageTestDriver testDriver = new MessageTestDriver();
        try {
            testDriver.loadTests(args[0], args[1]);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
	 * The framework configuration is needed to know the middleware nodes to test.
	 */
    private FrameworkConfiguration fwConfig;

    /**
	 * Default constructor.
	 */
    public MessageTestDriver() {
        fwConfig = new FrameworkConfiguration();
        fwConfig.loadProperties();
    }

    /**
	 * Loads the tests.
	 * 
	 * @param testFile
	 *            the XML test file with the test cases definition
	 * @param testDir
	 *            the test directory where the tests are stored
	 * 
	 * @throws Exception
	 *             the exception
	 */
    private void loadTests(String testFile, String testDir) throws Exception {
        Node root = XMLHelper.getInstance().getRootElement(testFile);
        NodeList children = root.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i).getNodeType() != Node.ELEMENT_NODE) continue;
            Element child = (Element) children.item(i);
            String childName = child.getNodeName();
            if (childName.equals("TEST")) {
                String testFilePath = child.getAttribute("URI");
                String outputPath = null;
                String isValidString = child.getAttribute("TYPE");
                boolean isValid;
                if ("valid".equals(isValidString)) {
                    isValid = true;
                    outputPath = child.getAttribute("OUTPUT");
                } else {
                    isValid = false;
                }
                String testID = child.getAttribute("ID");
                child.getAttribute("TYPE");
                String fileToTest = testDir + testFilePath;
                String fileExpected = testDir + outputPath;
                List<com.abiquo.framework.domain.Node> nodes = fwConfig.getNodes();
                Iterator<com.abiquo.framework.domain.Node> nodesIter = nodes.iterator();
                while (nodesIter.hasNext()) {
                    com.abiquo.framework.domain.Node node = nodesIter.next();
                    junit.textui.TestRunner.run(new MessageTest("testMessage", fileToTest, fileExpected, testID, isValid, node.getLocation(), node.getPort()));
                }
            }
        }
    }
}
