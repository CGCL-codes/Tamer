package org.shell.loaders;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.shell.loaders.exceptions.LoaderGeneralException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class XMLParser implements Loader {

    private static DocumentBuilder db;

    private Boolean override = false;

    public XMLParser() throws ParserConfigurationException {
        if (db == null) {
            db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        }
    }

    public void load(String path, Boolean override) throws LoaderGeneralException {
        this.override = override;
        try {
            Document document = db.parse(path);
            Element shell = document.getDocumentElement();
            if (shell.getNodeName().equals("shell")) {
                for (Node childNode = shell.getFirstChild(); childNode != null; childNode = childNode.getNextSibling()) {
                    if (childNode instanceof Element) {
                        Element childElement = (Element) childNode;
                        if (childElement.getNodeName().equals("jars")) {
                            parseJars(childElement);
                        } else {
                            throw new LoaderGeneralException("node not recognized - " + childElement.getNodeName() + "(should be 'jars' or 'commands')");
                        }
                    }
                }
            } else {
                throw new LoaderGeneralException("root element not found");
            }
        } catch (Exception ex) {
            throw new LoaderGeneralException(ex.getMessage());
        }
    }

    private void parseJars(Element jars) throws LoaderGeneralException {
        for (Node childNode = jars.getFirstChild(); childNode != null; childNode = childNode.getNextSibling()) {
            if (childNode instanceof Element) {
                Element childElement = (Element) childNode;
                if (childElement.getNodeName().equals("file")) {
                    parseFile(childElement);
                } else {
                    throw new LoaderGeneralException("node not recognized - " + childElement.getNodeName() + "(should be 'file')");
                }
            }
        }
    }

    private void parseFile(Element file) throws LoaderGeneralException {
        try {
            Loader jarLoader = new JarLoader();
            jarLoader.load(file.getTextContent().trim(), override);
        } catch (Exception ex) {
            throw new LoaderGeneralException(ex.getLocalizedMessage());
        }
    }
}
