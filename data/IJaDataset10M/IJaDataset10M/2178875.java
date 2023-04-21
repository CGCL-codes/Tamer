package com.tabuto.xmlMVC;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.io.*;
import javax.swing.JPanel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class XMLModel implements XMLUpdateObserver, SelectionObserver {

    public static final String INPUT = "input";

    public static final String PROTOCOL = "protocol";

    private Document document;

    private Document outputDocument;

    private Tree tree;

    private Tree importTree;

    private File currentFile;

    private boolean currentFileEdited;

    private ArrayList<XMLUpdateObserver> xmlObservers;

    private ArrayList<SelectionObserver> selectionObservers = new ArrayList<SelectionObserver>();

    ;

    public XMLModel(String path) {
        currentFile = new File(path);
        xmlObservers = new ArrayList<XMLUpdateObserver>();
        initialiseBlankDOMdoc();
        tree = new Tree(this, this);
        new XMLView(this);
    }

    public void openXMLFile(File xmlFile) {
        currentFile = xmlFile;
        readXMLtoDOM(xmlFile);
        tree = new Tree(document, this, this);
        document = null;
        notifyXMLObservers();
    }

    public void notifyXMLObservers() {
        for (XMLUpdateObserver xmlObserver : xmlObservers) {
            xmlObserver.xmlUpdated();
        }
    }

    public void xmlUpdated() {
        currentFileEdited = true;
        notifyXMLObservers();
    }

    public void selectionChanged() {
        notifySelectionObservers();
    }

    public void notifySelectionObservers() {
        for (SelectionObserver selectionObserver : selectionObservers) {
            selectionObserver.selectionChanged();
        }
    }

    public void addXMLObserver(XMLUpdateObserver newXMLObserver) {
        xmlObservers.add(newXMLObserver);
    }

    public void addSelectionObserver(SelectionObserver newSelectionObserver) {
        selectionObservers.add(newSelectionObserver);
    }

    public void initialiseBlankDOMdoc() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        ;
    }

    public void readXMLtoDOM(File xmlFile) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new org.xml.sax.ErrorHandler() {

                public void fatalError(SAXParseException exception) throws SAXException {
                }

                public void error(SAXParseException e) throws SAXParseException {
                    throw e;
                }

                public void warning(SAXParseException err) throws SAXParseException {
                    System.out.println("** Warning" + ", line " + err.getLineNumber() + ", uri " + err.getSystemId());
                    System.out.println("   " + err.getMessage());
                }
            });
            document = builder.parse(xmlFile);
        } catch (SAXException sxe) {
            Exception x = sxe;
            if (sxe.getException() != null) x = sxe.getException();
            x.printStackTrace();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public Tree getTreeFromNewFile(File xmlFile) {
        readXMLtoDOM(xmlFile);
        Tree tree = new Tree(document);
        document = null;
        return tree;
    }

    public void setImportTree(Tree tree) {
        importTree = tree;
        if (tree == null) return;
    }

    public void importElementsFromImportTree() {
        if (importTree.getHighlightedFields().size() > 0) {
            tree.copyAndInsertElements(importTree.getHighlightedFields());
        } else {
            tree.copyAndInsertElement(importTree.getRootNode());
        }
        notifyXMLObservers();
    }

    public void writeTreeToDOM() {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            outputDocument = db.newDocument();
            Element protocol = outputDocument.createElement(PROTOCOL);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        tree.buildDOMfromTree(outputDocument);
    }

    public void transformXmlToHtml() {
        File outputXmlFile = new File("file");
        saveTreeToXmlFile(outputXmlFile);
        XmlTransform.transformXMLtoHTML(outputXmlFile);
    }

    public void saveTreeToXmlFile(File outputFile) {
        writeTreeToDOM();
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            Source source = new DOMSource(outputDocument);
            Result output = new StreamResult(outputFile);
            transformer.transform(source, output);
            setCurrentFile(outputFile);
            currentFileEdited = false;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void printDOM(Document docToPrint) {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            Source source = new DOMSource(docToPrint);
            Result output = new StreamResult(System.out);
            transformer.transform(source, output);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("");
    }

    public void openBlankXmlFile() {
        tree.openBlankXmlFile();
        setCurrentFile(new File(""));
        notifyXMLObservers();
    }

    public void addDataField() {
        tree.addElement();
        notifyXMLObservers();
    }

    public void duplicateDataFields() {
        tree.duplicateAndInsertElements();
        notifyXMLObservers();
    }

    public void deleteDataFields(boolean saveChildren) {
        tree.deleteElements(saveChildren);
        notifyXMLObservers();
    }

    public void demoteDataFields() {
        tree.demoteDataFields();
        notifyXMLObservers();
    }

    public void promoteDataFields() {
        tree.promoteDataFields();
        notifyXMLObservers();
    }

    public void moveFieldsUp() {
        tree.moveFieldsUp();
        notifyXMLObservers();
    }

    public void moveFieldsDown() {
        tree.moveFieldsDown();
        notifyXMLObservers();
    }

    public void setXmlEdited(boolean xmlEdited) {
        currentFileEdited = xmlEdited;
    }

    public boolean isCurrentFileEdited() {
        return currentFileEdited;
    }

    public void setCurrentFile(File file) {
        currentFile = file;
    }

    public File getCurrentFile() {
        return currentFile;
    }

    public XmlNode getRootNode() {
        return tree.getRootNode();
    }

    public JPanel getAttributeEditorToDisplay() {
        return tree.getAttributeEditorToDisplay();
    }
}
