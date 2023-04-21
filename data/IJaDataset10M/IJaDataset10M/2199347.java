package org.openfrag.OpenCDS.core.project;

import org.openfrag.OpenCDS.core.logging.*;
import java.io.*;
import java.util.Enumeration;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

/**
 * This class reads My Gear entry's from a XML file.
 *
 * @author  Lars 'Levia' Wesselius
*/
public class GearXMLParser {

    private GearManager m_Manager;

    /**
     * The GearXMLParser constructor.
    */
    public GearXMLParser(GearManager manager) {
        m_Manager = manager;
    }

    /**
     * Parse a XML file, and add the entry's to the system.
     *
     * @param   file    The file to parse.
     * @param   append  True to append, false to recreate.
     * \return  True if successful, false if not.
    */
    public boolean parseFile(File file, boolean append) {
        if (!file.exists() && !file.getName().endsWith(".xml")) {
            return false;
        }
        if (!append) {
            m_Manager.removeAll();
        }
        BrowserManager browserMan = m_Manager.getBrowserManager();
        Logger logger = Logger.getInstance();
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(file);
            NodeList nodes = doc.getElementsByTagName("Project");
            for (int i = 0; i < nodes.getLength(); i++) {
                Element element = (Element) nodes.item(i);
                NodeList childNodes = element.getElementsByTagName("Name");
                Element childElement = (Element) childNodes.item(0);
                if (childElement == null) {
                    continue;
                }
                String name = childElement.getAttribute("value");
                logger.log("Parsing project: " + name);
                Project project = browserMan.getProject(name);
                if (project == null) {
                    logger.log(Logger.LOG_WARNING, "Project with the name: " + name + " could not be found in the system, discarding project.");
                    continue;
                }
                logger.log("Project with name: " + name + " has successfully been added to the system.");
                m_Manager.addProject(project);
            }
        } catch (Exception e) {
            logger.log(Logger.LOG_ERROR, "Parsing of file: " + file.getAbsoluteFile() + " failed.");
            logger.logException(e);
            return false;
        }
        return true;
    }

    /**
     * Parse a XML file, and add the entry's to the system.
     *
     * @param   file    The file to parse.
     * @param   append  True to append, false to recreate.
     * \return  True if successful, false if not.
    */
    public boolean parseFile(String file, boolean append) {
        return parseFile(new File(file), append);
    }

    /**
     * Write the XML file to disk.
     *
     * @param   fileName    The filename to write to.
     * \return  True if successful, false if not.
    */
    public boolean writeFile(String fileName) {
        File file = new File(fileName);
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            DOMImplementation impl = builder.getDOMImplementation();
            Document doc = impl.createDocument(null, null, null);
            Element root = doc.createElement("MyGear");
            doc.appendChild(root);
            for (Enumeration entries = m_Manager.getProjects(); entries.hasMoreElements(); ) {
                Project project = (Project) entries.nextElement();
                Element projectNode = doc.createElement("Project");
                root.appendChild(projectNode);
                Element nameNode = doc.createElement("Name");
                nameNode.setAttribute("value", project.getName());
                projectNode.appendChild(nameNode);
            }
            FileOutputStream fos;
            Transformer transformer;
            fos = new FileOutputStream(file);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(fos);
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
