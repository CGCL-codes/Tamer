package org.proteored.miapeapi.interfaces;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.proteored.miapeapi.exceptions.MiapeDatabaseException;
import org.proteored.miapeapi.exceptions.MiapeSecurityException;
import org.proteored.miapeapi.interfaces.ge.MiapeGEDocument;
import org.proteored.miapeapi.interfaces.gi.MiapeGIDocument;
import org.proteored.miapeapi.interfaces.ms.MiapeMSDocument;
import org.proteored.miapeapi.interfaces.msi.MiapeMSIDocument;
import org.proteored.miapeapi.interfaces.persistence.MiapeFile;
import org.proteored.miapeapi.validation.ValidationReport;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class MiapeHeaderImpl implements MiapeDocument {

    private MiapeDate date = null;

    private int id = -1;

    private Date modificationDate = null;

    private String name = null;

    private String prideURI = null;

    private Boolean template = null;

    private String version = null;

    private MiapeFile miapeFile;

    private String projectName = null;

    private int idProject = -1;

    public MiapeHeaderImpl(MiapeGEDocument miape) {
        date = miape.getDate();
        id = miape.getId();
        modificationDate = miape.getModificationDate();
        name = miape.getName();
        prideURI = miape.getAttachedFileLocation();
        template = miape.getTemplate();
        version = miape.getVersion();
        Project project = miape.getProject();
        if (project != null) {
            idProject = project.getId();
            projectName = project.getName();
        }
    }

    public MiapeHeaderImpl(MiapeGIDocument miape) {
        date = miape.getDate();
        id = miape.getId();
        modificationDate = miape.getModificationDate();
        name = miape.getName();
        prideURI = miape.getAttachedFileLocation();
        template = miape.getTemplate();
        version = miape.getVersion();
        Project project = miape.getProject();
        if (project != null) {
            idProject = project.getId();
            projectName = project.getName();
        }
    }

    public MiapeHeaderImpl(MiapeMSDocument miape) {
        date = miape.getDate();
        id = miape.getId();
        modificationDate = miape.getModificationDate();
        name = miape.getName();
        prideURI = miape.getAttachedFileLocation();
        template = miape.getTemplate();
        version = miape.getVersion();
        Project project = miape.getProject();
        if (project != null) {
            idProject = project.getId();
            projectName = project.getName();
        }
    }

    public MiapeHeaderImpl(MiapeMSIDocument miape) {
        date = miape.getDate();
        id = miape.getId();
        modificationDate = miape.getModificationDate();
        name = miape.getName();
        prideURI = miape.getAttachedFileLocation();
        template = miape.getTemplate();
        version = miape.getVersion();
        Project project = miape.getProject();
        if (project != null) {
            idProject = project.getId();
            projectName = project.getName();
        }
    }

    /**
	 * 
	 * @param bytes
	 *            byte array as: <?xml version="1.0" encoding="UTF-8"
	 *            standalone="no"?> <MiapeHeader> <ID value="514"/> <Name
	 *            value="ProteoRed (DIGE) (CNB)"/> <Template value="false"/>
	 *            <Date value="2008-10-01"/> <ModificationDate
	 *            value="2009-09-28 12:56:27.71"/> <Version value="1"/>
	 *            <PRIDEURI value=""/> <ProjectName value="proyecto x"/>
	 *            <ProjectID value="123"/> </MiapeHeader>
	 */
    public MiapeHeaderImpl(byte[] bytes) {
        try {
            miapeFile = new MiapeFile(bytes);
            File file = miapeFile.toFile();
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = builder.parse(file);
            file.delete();
            readDocument(document);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }

    public MiapeHeaderImpl(Document document) {
        readDocument(document);
    }

    public void deleteFile() {
        if (miapeFile != null) miapeFile.delete();
    }

    private void readDocument(Document document) {
        NodeList childNodes = document.getChildNodes();
        Node rootElement = childNodes.item(0);
        for (int i = 0; i < rootElement.getChildNodes().getLength(); i++) {
            Node childNode = rootElement.getChildNodes().item(i);
            if (childNode.getNodeType() == Element.ELEMENT_NODE) {
                if (childNode.getNodeName().equals(MiapeHeader.NAME)) {
                    NamedNodeMap attr = childNode.getAttributes();
                    for (int j = 0; j < attr.getLength(); j++) {
                        if (attr.item(j).getNodeName().equals(MiapeHeader.VALUE)) {
                            name = attr.item(j).getNodeValue();
                        }
                    }
                }
                if (childNode.getNodeName().equals(MiapeHeader.DATE)) {
                    NamedNodeMap attr = childNode.getAttributes();
                    for (int j = 0; j < attr.getLength(); j++) {
                        if (attr.item(j).getNodeName().equals(MiapeHeader.VALUE)) {
                            String nodeValue = attr.item(j).getNodeValue();
                            if (nodeValue != null && !nodeValue.equals("")) date = new MiapeDate(nodeValue);
                        }
                    }
                }
                if (childNode.getNodeName().equals(MiapeHeader.ID)) {
                    NamedNodeMap attr = childNode.getAttributes();
                    for (int j = 0; j < attr.getLength(); j++) {
                        if (attr.item(j).getNodeName().equals(MiapeHeader.VALUE)) {
                            String nodeValue = attr.item(j).getNodeValue();
                            if (nodeValue != null && !nodeValue.equals("")) id = Integer.valueOf(nodeValue);
                        }
                    }
                }
                if (childNode.getNodeName().equals(MiapeHeader.MODIFICATION_DATE)) {
                    NamedNodeMap attr = childNode.getAttributes();
                    for (int j = 0; j < attr.getLength(); j++) {
                        if (attr.item(j).getNodeName().equals(MiapeHeader.VALUE)) {
                            String nodeValue = attr.item(j).getNodeValue();
                            if (nodeValue != null && !nodeValue.equals("")) modificationDate = new MiapeDate(nodeValue).toDate();
                        }
                    }
                }
                if (childNode.getNodeName().equals(MiapeHeader.PRIDEURI)) {
                    NamedNodeMap attr = childNode.getAttributes();
                    for (int j = 0; j < attr.getLength(); j++) {
                        if (attr.item(j).getNodeName().equals(MiapeHeader.VALUE)) {
                            String nodeValue = attr.item(j).getNodeValue();
                            if (nodeValue != null && !nodeValue.equals("")) prideURI = nodeValue;
                        }
                    }
                }
                if (childNode.getNodeName().equals(MiapeHeader.TEMPLATE)) {
                    NamedNodeMap attr = childNode.getAttributes();
                    for (int j = 0; j < attr.getLength(); j++) {
                        if (attr.item(j).getNodeName().equals(MiapeHeader.VALUE)) {
                            String nodeValue = attr.item(j).getNodeValue();
                            if (nodeValue != null && !nodeValue.equals("")) template = Boolean.valueOf(nodeValue);
                        }
                    }
                }
                if (childNode.getNodeName().equals(MiapeHeader.VERSION)) {
                    NamedNodeMap attr = childNode.getAttributes();
                    for (int j = 0; j < attr.getLength(); j++) {
                        if (attr.item(j).getNodeName().equals(MiapeHeader.VALUE)) {
                            String nodeValue = attr.item(j).getNodeValue();
                            if (nodeValue != null && !nodeValue.equals("")) version = nodeValue;
                        }
                    }
                }
                if (childNode.getNodeName().equals(MiapeHeader.PROJECTID)) {
                    NamedNodeMap attr = childNode.getAttributes();
                    for (int j = 0; j < attr.getLength(); j++) {
                        if (attr.item(j).getNodeName().equals(MiapeHeader.VALUE)) {
                            String nodeValue = attr.item(j).getNodeValue();
                            if (nodeValue != null && !nodeValue.equals("")) idProject = Integer.valueOf(nodeValue);
                        }
                    }
                }
                if (childNode.getNodeName().equals(MiapeHeader.PROJECTNAME)) {
                    NamedNodeMap attr = childNode.getAttributes();
                    for (int j = 0; j < attr.getLength(); j++) {
                        if (attr.item(j).getNodeName().equals(MiapeHeader.VALUE)) {
                            String nodeValue = attr.item(j).getNodeValue();
                            if (nodeValue != null && !nodeValue.equals("")) projectName = nodeValue;
                        }
                    }
                }
            }
        }
    }

    @Override
    public Contact getContact() {
        return null;
    }

    @Override
    public MiapeDate getDate() {
        return date;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public Date getModificationDate() {
        return modificationDate;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public User getOwner() {
        return null;
    }

    @Override
    public String getAttachedFileLocation() {
        return prideURI;
    }

    @Override
    public Project getProject() {
        return new ProjectHeaderImpl(idProject, projectName);
    }

    @Override
    public Boolean getTemplate() {
        return template;
    }

    @Override
    public ValidationReport getValidationReport() {
        return null;
    }

    @Override
    public String getVersion() {
        return version;
    }

    @Override
    public void delete(String user, String password) throws MiapeDatabaseException, MiapeSecurityException {
    }

    @Override
    public int store() throws MiapeDatabaseException, MiapeSecurityException {
        return -1;
    }
}
