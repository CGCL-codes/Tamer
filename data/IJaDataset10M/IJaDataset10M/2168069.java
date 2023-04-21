package org.jdeluxe.testing.tree;

import java.io.*;
import java.util.ArrayList;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.jface.window.*;
import org.eclipse.swt.widgets.*;

/**
 * The Class XMLTreeDemo.
 */
public class XMLTreeDemo extends ApplicationWindow {

    /** The Constant DEFAULT_XML_FILE. */
    private static final String DEFAULT_XML_FILE = "resources/jdeluxe.jdc";

    /** The doc. */
    private static Document doc;

    /**
   * Instantiates a new XML tree demo.
   */
    public XMLTreeDemo() {
        super(null);
    }

    protected Control createContents(Composite parent) {
        getShell().setSize(350, 550);
        getShell().setText("JFace XML Tree");
        TreeViewer tv = new TreeViewer(parent);
        tv.setContentProvider(new ITreeContentProvider() {

            public Object[] getChildren(Object element) {
                ArrayList ch = new ArrayList();
                NamedNodeMap atrs = ((Node) element).getAttributes();
                if (atrs != null) for (int i = 0; i < atrs.getLength(); i++) ch.add(atrs.item(i));
                NodeList nl = ((Node) element).getChildNodes();
                for (int i = 0; i < nl.getLength(); i++) if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) ch.add(nl.item(i));
                return ch.toArray();
            }

            public Object getParent(Object element) {
                return ((Node) element).getParentNode();
            }

            public Object[] getElements(Object element) {
                return getChildren(element);
            }

            public boolean hasChildren(Object element) {
                return getChildren(element).length > 0;
            }

            public void dispose() {
            }

            public void inputChanged(Viewer viewer, Object old_input, Object new_input) {
            }
        });
        tv.setLabelProvider(new LabelProvider() {

            public String getText(Object element) {
                if (element instanceof Attr) return "@" + ((Attr) element).getNodeName(); else return ((Node) element).getNodeName();
            }
        });
        tv.setInput(doc);
        return tv.getControl();
    }

    /**
   * The main method.
   * 
   * @param args the arguments
   * 
   * @throws Exception the exception
   */
    public static void main(String[] args) throws Exception {
        File in = new File(args.length > 0 ? args[0] : DEFAULT_XML_FILE);
        doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
        XMLTreeDemo w = new XMLTreeDemo();
        w.setBlockOnOpen(true);
        w.open();
        Display.getCurrent().dispose();
    }
}
