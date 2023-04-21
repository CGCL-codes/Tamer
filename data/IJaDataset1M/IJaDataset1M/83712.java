package edu.asu.vogon.rdf.foxml.jdom;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import edu.asu.vogon.rdf.xml.NodeProperty;
import edu.asu.vogon.rdf.xml.XMLNode;

public class JDOMDocumentWrapper {

    private Document doc;

    private Map<String, Namespace> namespaces;

    public JDOMDocumentWrapper() {
        doc = new Document();
        namespaces = new HashMap<String, Namespace>();
    }

    public void setRoot(String rootName, String nsPre, String nsUri) {
        Namespace ns = Namespace.getNamespace(nsPre, nsUri);
        namespaces.put(nsPre, ns);
        Element root = new Element(rootName, ns);
        doc.setRootElement(root);
    }

    public void addNamespaceToRoot(String nsPre, String nsUri) {
        Namespace ns = Namespace.getNamespace(nsPre, nsUri);
        namespaces.put(nsPre, ns);
        Element root = doc.getRootElement();
        if (root != null) root.addNamespaceDeclaration(ns);
    }

    public void addSchemaLocation(String nsPre, String name, String location) {
        Element root = doc.getRootElement();
        if (root != null) {
            Namespace ns = namespaces.get(nsPre);
            Attribute schema = null;
            if (ns != null) schema = new Attribute(name, location, ns); else schema = new Attribute(name, location);
            root.setAttribute(schema);
        }
    }

    public void addNode(XMLNode node, String nsPre) {
        Namespace ns = namespaces.get(nsPre);
        Element root = doc.getRootElement();
        addNodeToNode(root, node, ns);
    }

    private void addNodeToNode(Element parent, XMLNode node, Namespace ns) {
        if (node == null) return;
        Element newNode = new Element(node.getName(), ns);
        List<Attribute> atts = new ArrayList<Attribute>();
        for (NodeProperty prop : node.getProperties()) {
            Attribute a = new Attribute(prop.getPropertyName(), prop.getPropertyValue());
            atts.add(a);
        }
        newNode.setAttributes(atts);
        parent.addContent(newNode);
        for (XMLNode n : node.getChildNodes()) {
            addNodeToNode(newNode, n, ns);
        }
    }

    public void print() {
        XMLOutputter out = new XMLOutputter();
        try {
            out.output(doc, System.out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean save(File file) {
        try {
            FileOutputStream stream = new FileOutputStream(file);
            XMLOutputter out = new XMLOutputter();
            out.setFormat(Format.getPrettyFormat());
            out.output(doc, stream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
