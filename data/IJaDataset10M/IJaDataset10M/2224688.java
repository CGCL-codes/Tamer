package org.in4ama.editor.project;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.TransformerFactory;
import org.in4ama.datasourcemanager.util.DocumentHelper;
import org.in4ama.editor.exception.EditorException;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ProjectReferenceManager {

    private Document referenceDoc;

    private Element referencesEle;

    public static final String REFERENCE_FILENAME = "references.xml";

    String projectPath;

    public void projectOpened(String path) throws EditorException {
        try {
            projectPath = path;
            File file = new File(path + File.separatorChar + REFERENCE_FILENAME);
            DocumentHelper docHelper = new DocumentHelper();
            if (file.exists()) {
                TransformerFactory tFactory = TransformerFactory.newInstance();
                DocumentBuilder dBuilder = docHelper.getDocumentBuilder(tFactory);
                referenceDoc = docHelper.getDocument(dBuilder, file.getAbsolutePath());
                referencesEle = referenceDoc.getDocumentElement();
            } else {
                referenceDoc = docHelper.createDocument();
                referencesEle = referenceDoc.createElement("References");
                referenceDoc.appendChild(referencesEle);
            }
        } catch (Exception ex) {
            String msg = "Error while reading the project configuration file.";
            throw new EditorException(msg, ex);
        }
    }

    public void save() throws EditorException {
        try {
            DocumentHelper docHelper = new DocumentHelper();
            TransformerFactory tFactory = TransformerFactory.newInstance();
            DocumentBuilder dBuilder = docHelper.getDocumentBuilder(tFactory);
            docHelper.saveDocument(referenceDoc, projectPath + File.separatorChar + REFERENCE_FILENAME);
        } catch (Exception ex) {
            String msg = "Error while saving the project configuration file.";
            throw new EditorException(msg, ex);
        }
    }

    public String[] getReferenceNames() {
        String[] ret = new String[referencesEle.getChildNodes().getLength()];
        NodeList list = referencesEle.getChildNodes();
        for (int i = 0; i < ret.length; i++) {
            Element childEle = (Element) list.item(i);
            ret[i] = childEle.getAttribute("name");
        }
        return ret;
    }

    public void addReference(String referenceName, String expressionText) {
        Element referenceEle = referenceDoc.createElement("reference");
        referencesEle.appendChild(referenceEle);
        CDATASection expressionEle = referenceDoc.createCDATASection(expressionText);
        referenceEle.setAttribute("name", referenceName);
        referenceEle.appendChild(expressionEle);
    }

    public String findReference(String name) {
        NodeList list = referencesEle.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Element child = (Element) list.item(i);
            String childName = child.getAttribute("name");
            if (childName.equals(name)) {
                CDATASection expressionEle = (CDATASection) child.getChildNodes().item(0);
                return (expressionEle.getTextContent());
            }
        }
        return null;
    }

    public void updateReference(String name, String expression) {
        NodeList list = referencesEle.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Element child = (Element) list.item(i);
            String childName = child.getAttribute("name");
            if (childName.equals(name)) {
                CDATASection expressionEle = (CDATASection) child.getChildNodes().item(0);
                expressionEle.setTextContent(expression);
            }
        }
    }

    public void removeReference(String name) {
        NodeList list = referencesEle.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Element child = (Element) list.item(i);
            String childName = child.getAttribute("name");
            if (childName.equals(name)) {
                referencesEle.removeChild(child);
            }
        }
    }

    public Document getReferenceDoc() {
        return referenceDoc;
    }
}
