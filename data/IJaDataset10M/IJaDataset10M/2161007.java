package org.dbreplicator.replication.xml;

import java.sql.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import org.dbreplicator.replication.*;

public class ServerHandler extends DefaultHandler {

    XMLElement currentElement;

    Connection subConnection;

    Statement statement;

    public ServerHandler(Connection connection) {
        try {
            currentElement = new XMLElement("root");
            subConnection = connection;
            statement = subConnection.createStatement();
        } catch (SQLException ex) {
            RepConstants.writeERROR_FILE(ex);
        }
    }

    public void startElement(String namespace, String localname, String qname, Attributes atts) throws SAXException {
        XMLElement childElement = new XMLElement(qname);
        currentElement.addChild(childElement);
        childElement.setParentElement(currentElement);
        currentElement = childElement;
    }

    public void endElement(String namespace, String localname, String qname) throws SAXException {
        if (qname.equalsIgnoreCase("row")) {
            createQuery();
        }
        if (qname.equalsIgnoreCase("tablename")) {
            currentElement.elementList.clear();
        }
        XMLElement parentElement = currentElement.getParentElement();
        currentElement = parentElement;
    }

    public void characters(char[] ch, int start, int len) {
        String elementValue = new String(ch, start, len);
        if (elementValue.equalsIgnoreCase("") || elementValue.equalsIgnoreCase("\n")) {
            return;
        }
        currentElement.setElementValue(elementValue);
    }

    /** @todo
     *  Implement it */
    public void createQuery() {
    }
}
