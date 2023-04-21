package oext.test.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.FileOutputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import oext.gui.MainFrame;
import oext.model.rules.AllChildEquality;
import oext.model.rules.RuleMap;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import junit.framework.TestCase;

public class ShowMainFrameTest extends TestCase {

    public static final String fileName1 = "./data/file1.xml";

    public static final String fileName2 = "./data/file2.xml";

    public ShowMainFrameTest() {
        super();
    }

    protected void setUp() throws Exception {
        super.setUp();
        Node node1 = null;
        Node node2 = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            node1 = document.createElement("test-node");
            node2 = document.createElement("test-node");
            addChilds(document, node1, node2, true);
            FileOutputStream fos1 = new FileOutputStream(fileName1);
            FileOutputStream fos2 = new FileOutputStream(fileName2);
            StreamResult t = new StreamResult(fos1);
            DOMSource source = new DOMSource(node1);
            Transformer trans = TransformerFactory.newInstance().newTransformer();
            trans.transform(source, t);
            t = new StreamResult(fos2);
            source = new DOMSource(node2);
            trans = TransformerFactory.newInstance().newTransformer();
            trans.transform(source, t);
            fos1.close();
            fos2.close();
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    protected void addChilds(Document document, Node node1, Node node2, boolean firstLevel) {
        String childName = "add";
        NamedNodeMap map1 = null;
        NamedNodeMap map2 = null;
        Node att = null;
        int count = (int) (10 * Math.random());
        if (firstLevel) {
            count = 10;
        }
        for (int i = 0; i < count; i++) {
            String pcData = "PCDATA_ " + count;
            Node add1 = document.createElement(childName + i);
            add1.setTextContent(pcData);
            node1.appendChild(add1);
            map1 = add1.getAttributes();
            Node add2 = document.createElement(childName + i);
            add2.setTextContent(pcData);
            node2.appendChild(add2);
            map2 = add2.getAttributes();
            RuleMap.setRule(add2.getNodeName(), new AllChildEquality());
            int attCount = (int) (10 * Math.random());
            for (int j = 0; j < attCount; j++) {
                att = add1.getOwnerDocument().createAttribute("att" + j);
                att.setNodeValue("value" + j);
                map1.setNamedItem(att);
                att = add2.getOwnerDocument().createAttribute("att" + j);
                att.setNodeValue("value" + j);
                map2.setNamedItem(att);
            }
            if ((100 * Math.random()) < 20) {
                addChilds(document, add1, add2, false);
            }
        }
    }

    protected void showFrame() {
        MainFrame frame = new MainFrame();
        frame.setFileName1(fileName1);
        frame.setFileName2(fileName2);
        frame.construct();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = new Dimension(800, 600);
        if (frameSize.height > screenSize.height) frameSize.height = screenSize.height;
        if (frameSize.width > screenSize.width) frameSize.width = screenSize.width;
        frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        frame.setSize(frameSize);
        frame.setVisible(true);
        try {
            Thread.sleep(20000);
        } catch (Exception exp) {
        }
        frame.setVisible(false);
        frame.saveActionPerformed(null);
    }

    public void testGui() {
        for (int i = 0; i < 2; i++) {
            showFrame();
        }
    }
}
