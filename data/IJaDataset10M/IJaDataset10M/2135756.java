package dbdependency;

import java.io.*;
import org.apache.xerces.dom.*;
import org.apache.xerces.parsers.*;
import org.apache.xml.serialize.*;
import org.w3c.dom.*;
import org.xml.sax.*;

/**
 * An implementation of DBConnectionConfigurator that reads and writes
 * the configuration from an XML file.
 *
 * $Id: XMLDBConnectionConfigurator.java,v 1.5 2000/04/10 15:54:31 john Exp $
 * 
 * @author  Jesus M. Salvo Jr.
 */
public class XMLDBConnectionConfigurator implements DBConnectionConfigurator {

    String xmlfile;

    org.w3c.dom.Document document;

    DBConnection connections[];

    /**
   * Creates an XMLDBConnectionConfigurator object, indicating the XML
   * file to read the configuration from.
   *
   * @param xmlconfigfile   Filename storing the connection configurations
   */
    public XMLDBConnectionConfigurator(String xmlconfigfile) {
        this.xmlfile = xmlconfigfile;
        org.w3c.dom.Document document;
        DOMParser parser = new DOMParser();
        try {
            parser.parse(xmlconfigfile);
        } catch (org.xml.sax.SAXException e) {
            System.out.println("Unable to parse configuration file.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Unable to open configuration file.");
            e.printStackTrace();
            return;
        }
        this.document = parser.getDocument();
    }

    /**
   * Reads the XML file specified in the constructor and returns an array of DBConnection objects
   * identifying the predefined connections to databases.
   *
   * @return  Array of DBConnection objects.
   */
    public DBConnection[] getPredefinedConnections() {
        NodeList connectionNodes = document.getElementsByTagName("connection");
        connections = new DBConnection[connectionNodes.getLength()];
        int i, length = connectionNodes.getLength();
        Node connectionNode;
        NodeList childnodes;
        String name, jdbcdriver = null, url = null, username = null;
        for (i = 0; i < length; i++) {
            connectionNode = connectionNodes.item(i);
            NamedNodeMap attributes = connectionNode.getAttributes();
            connections[i] = new DBConnection(attributes.getNamedItem("id").getNodeValue(), attributes.getNamedItem("jdbcdriver").getNodeValue(), attributes.getNamedItem("url").getNodeValue(), attributes.getNamedItem("username").getNodeValue(), attributes.getNamedItem("resolver").getNodeValue());
        }
        return connections;
    }

    /**
   * Writes new conneciton configurations to the XML file specified in the constructor
   *
   * @param configuration Array of DBConnection objects to be saved into configuration repository.
   */
    public void savePredefinedConnections(DBConnection[] dbconnections) {
        this.document = new DOMImplementationImpl().createDocument(null, "connections", null);
        Node root = this.document.getFirstChild();
        int i, size = dbconnections.length;
        for (i = 0; i < size; i++) {
            ElementImpl connectionnode = (ElementImpl) document.createElement("connection");
            DBConnection dbconnection = dbconnections[i];
            connectionnode.setAttribute("id", dbconnection.getName());
            connectionnode.setAttribute("jdbcdriver", dbconnection.getJdbcDriver());
            connectionnode.setAttribute("url", dbconnection.getURL());
            connectionnode.setAttribute("username", dbconnection.getUsername());
            connectionnode.setAttribute("resolver", dbconnection.getResolverString());
            root.appendChild(connectionnode);
        }
        try {
            XMLSerializer serializer = new XMLSerializer(new FileWriter("config.xml"), new OutputFormat("xml", "utf-8", true));
            serializer.serialize(this.document);
        } catch (IOException e) {
            System.out.println("Unable to serialize the configuration file.");
            e.printStackTrace();
        }
    }
}
