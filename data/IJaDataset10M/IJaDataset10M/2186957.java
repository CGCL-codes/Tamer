package org.vesuf.tools.xmi;

import org.vesuf.model.presentation.*;
import org.vesuf.model.uml.modelmanagement.*;
import org.vesuf.model.uml.foundation.core.*;
import org.vesuf.util.*;
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.w3c.dom.*;
import java.io.*;
import java.util.*;

/**
 *  The XMI-Reader reads an xmi file and
 *  creates the specified model elements.
 *  XMIReader implementation is not guaranteed
 *  to be thread-safe, therefore you should not
 *  use the same xmi reader from different threads.
 */
public class XMIReader implements IParseContext {

    /** The document builder (if already created). */
    protected DocumentBuilder builder;

    /** The root elements. */
    protected Vector roots;

    /** The elements. */
    protected Hashtable elements;

    /** The document. */
    protected Document document;

    /** The parsers (by xmi element name). */
    protected Hashtable parsers;

    /** The parsers (by generated element class). Used for checking. */
    protected Hashtable parsers2;

    /** The parser name -> class names. */
    protected Properties parsernames;

    /** The stereotypes. Hack !!! */
    protected Hashtable stereotypes;

    /**
	 *  Create an xmi reader object.
	 */
    public XMIReader() {
        this.builder = null;
        this.parsers = new Hashtable();
        this.parsernames = new Properties();
        ResourceManager resman = ResourceManager.getManager(XMIReader.class);
        try {
            parsernames.load(resman.getStream("parser.properties"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 *  Read in an xmi file and create the model.
	 *  @param xmi	The xmi input stream.
	 *  @return The loaded model.
	 */
    public IModel readXMI(InputStream xmi) throws ParserConfigurationException, IOException, SAXException {
        document = getBuilder().parse(xmi);
        this.roots = new Vector();
        this.elements = new Hashtable();
        this.parsers2 = new Hashtable();
        this.stereotypes = new Hashtable();
        preprocessStereotypes(document);
        processNode(document, true);
        checkElements();
        if (roots.size() != 1) {
            throw new RuntimeException("Only one root element supported");
        }
        return (IModel) roots.elementAt(0);
    }

    /**
	 *  Get a model element by node.
	 *  Extracts the xmi.id or xmi.idref.
	 *  @param node	The node.
	 *  @return	The element.
	 */
    public IModelElement getElement(Node node) {
        String xmiid = null;
        Node idrefnode = node.getAttributes().getNamedItem("xmi.idref");
        Node idnode = node.getAttributes().getNamedItem("xmi.id");
        if (idrefnode != null) {
            xmiid = idrefnode.getNodeValue();
        } else if (idnode != null) {
            xmiid = idnode.getNodeValue();
        }
        if (xmiid == null) {
            throw new RuntimeException("Not a model element in node " + node);
        }
        IModelElement el = (IModelElement) elements.get(xmiid);
        if (el == null) {
            if (idrefnode != null) {
                el = createElement(document, xmiid);
            } else {
                el = createElement(node);
            }
        }
        return el;
    }

    /**
	 *  Hack !!! Get the stereotype for a model element node.
	 */
    public IModelElement getStereotype(Node node) {
        String xmiid = null;
        Node idrefnode = node.getAttributes().getNamedItem("xmi.idref");
        Node idnode = node.getAttributes().getNamedItem("xmi.id");
        if (idrefnode != null) {
            xmiid = idrefnode.getNodeValue();
        } else if (idnode != null) {
            xmiid = idnode.getNodeValue();
        }
        if (xmiid == null) {
            throw new RuntimeException("Not a model element in node " + node);
        }
        return (IModelElement) stereotypes.get(xmiid);
    }

    /**
	 *  Parse the dom until the element with idref was created.
	 *  @param node The node.
	 *  @param idref The idref.
	 *  @return The created modelelement.
	 */
    protected IModelElement createElement(Node node, String idref) {
        IModelElement me = null;
        NamedNodeMap nnm = node.getAttributes();
        if (nnm != null) {
            Node attr = nnm.getNamedItem("xmi.id");
            if (attr != null && attr.getNodeValue().equals(idref)) {
                me = createElement(node);
            }
        }
        if (me == null) {
            NodeList nl = node.getChildNodes();
            for (int i = 0; i < nl.getLength() && me == null; i++) {
                me = createElement(nl.item(i), idref);
            }
        }
        return me;
    }

    /**
	 *  Create a model element for a node.
	 *  @param node The node.
	 *  @return The created modelelement.
	 */
    protected IModelElement createElement(Node node) {
        IModelElementParser p = getParser(node.getNodeName());
        IModelElement me = p.parseElement(node, this);
        parsers2.put(me.getClass(), p);
        if (elements.put(node.getAttributes().getNamedItem("xmi.id").getNodeValue(), me) != null) {
            throw new RuntimeException("Added element twice: " + node.getAttributes().getNamedItem("xmi.id").getNodeValue());
        }
        return me;
    }

    /**
	 *  Get a parser for a uml modelelement. 
	 *  @param name The name.
	 *  @return The parser.
	 */
    protected IModelElementParser getParser(String name) {
        IModelElementParser p = (IModelElementParser) parsers.get(name);
        if (p == null) {
            try {
                Class pc = Class.forName(parsernames.getProperty(name));
                p = (IModelElementParser) pc.newInstance();
            } catch (Exception e) {
                throw new RuntimeException("No parser for " + name + ": " + e);
            }
        }
        return p;
    }

    /**
	 *  Get the document builder.
	 *  @return	The document builder.
	 */
    protected DocumentBuilder getBuilder() throws ParserConfigurationException {
        if (builder == null) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            this.builder = factory.newDocumentBuilder();
        }
        return builder;
    }

    /**
	 *  Recursivley process an xmi node.
	 *  @param node The xmi-node.
	 *  @param top Flag, to indicate processing at top-level
	 *    (= no parent element).
	 */
    protected void processNode(Node node, boolean top) {
        NamedNodeMap nnm = node.getAttributes();
        if (nnm != null) {
            Node attr = nnm.getNamedItem("xmi.id");
            if (attr != null) {
                String xmiid = attr.getNodeValue();
                if (!elements.containsKey(xmiid)) {
                    createElement(node);
                }
                if (top) {
                    top = false;
                    roots.addElement(elements.get(xmiid));
                }
            }
        }
        NodeList nl = node.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            processNode(nl.item(i), top);
        }
    }

    /**
	 *  Hack !!! Preparse stereotypes.
	 *  @param node The xmi-node.
	 */
    protected void preprocessStereotypes(Node node) {
        NamedNodeMap nnm = node.getAttributes();
        if (nnm != null && node.getNodeName().equals("Foundation.Extension_Mechanisms.Stereotype")) {
            Node attr = nnm.getNamedItem("xmi.id");
            if (attr != null) {
                String xmiid = attr.getNodeValue();
                if (!elements.containsKey(xmiid)) {
                    IModelElement elm = createElement(node);
                    Element exel = (Element) ((Element) node).getElementsByTagName("Foundation.Extension_Mechanisms.Stereotype.extendedElement").item(0);
                    if (exel != null) {
                        String xmiref = ((Element) exel.getElementsByTagName("Foundation.Core.ModelElement").item(0)).getAttributeNode("xmi.idref").getNodeValue();
                        stereotypes.put(xmiref, elm);
                    }
                }
            }
        }
        NodeList nl = node.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            preprocessStereotypes(nl.item(i));
        }
    }

    /**
	 *  Check the generated elements.
	 */
    protected void checkElements() {
        for (Enumeration e = elements.elements(); e.hasMoreElements(); ) {
            IModelElement element = (IModelElement) e.nextElement();
            ((IModelElementParser) parsers2.get(element.getClass())).checkElement(element);
        }
    }

    /** Quiet flag. */
    protected static boolean quiet = false;

    /**
	 *  Invoke the reader with xmi files
	 *  and serialize the loaded model.
	 *  @param args	The xmi files.
	 */
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            printUsage();
            return;
        }
        args = scanArgs(args);
        XMIReader reader = new XMIReader();
        for (int i = 0; i < args.length; i++) {
            String xmi = args[i];
            try {
                if (!quiet) System.out.println("Parsing " + xmi);
                FileInputStream fis = new FileInputStream(xmi);
                IModel model = reader.readXMI(fis);
                if (!quiet) System.out.println("Saving " + SUtil.removeExtension(xmi) + ".ser");
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SUtil.removeExtension(xmi) + ".ser"));
                oos.writeObject(model);
                oos.close();
            } catch (Exception e) {
                System.out.println("Exception while processing " + xmi);
                throw e;
            }
        }
    }

    /**
	 *  Print usage information.
	 */
    protected static void printUsage() {
        System.out.println("Usage:");
        System.out.println("java org.vesuf.tools.xmi.XMIReader" + " [-q] <file1> [<file2> ...]");
        System.out.println("Read an xmi file and writes serialized model.");
        System.out.println("Options:");
        System.out.println("\t-q\tQuiet operation.");
    }

    /**
	 *  Scan command line args for supported options.
	 *  @param args	The command line args.
	 *  @return The command line args without options.
	 */
    protected static String[] scanArgs(String[] args) {
        int i;
        for (i = 0; i < args.length && args[i].startsWith("-"); i++) {
            if (args[i].equals("-q")) {
                quiet = true;
            }
        }
        String[] newargs = new String[args.length - i];
        for (int j = 0; j < newargs.length; j++) {
            newargs[j] = args[j + i];
        }
        return newargs;
    }
}
