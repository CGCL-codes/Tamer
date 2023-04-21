package treeView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.LinkedList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import connector.MaseSystem;

public class TreeViewDocument {

    private LinkedList<String> roots = new LinkedList<String>();

    public boolean Refresh(Tree tree) {
        MaseSystem mase = MaseSystem.getMaseSystem();
        if (mase == null) {
            tree.setEnabled(false);
            return false;
        }
        String xmlDoc = mase.getFitPageTree();
        InputStream xmlStream = new ByteArrayInputStream(xmlDoc.getBytes());
        System.out.println(xmlDoc);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document dom = null;
            dom = db.parse(xmlStream);
            Node node = dom.getFirstChild().getFirstChild();
            tree.removeAll();
            while (node != null) {
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    TreeItem item = new TreeItem(tree, SWT.NULL);
                    item.setText(element.getAttribute("name"));
                    doChildren(element, item);
                }
                node = node.getNextSibling();
            }
            tree.setEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    private void doChildren(Element element, TreeItem tree) {
        Node child = element.getFirstChild();
        while (child != null) {
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                Element childElement = (Element) child;
                TreeItem item = new TreeItem(tree, SWT.NULL);
                item.setText(childElement.getAttribute("name"));
                doChildren(childElement, item);
            }
            child = child.getNextSibling();
        }
    }
}
