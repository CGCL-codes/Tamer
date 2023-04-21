package net.charabia.ac.spedia;

import net.charabia.ac.*;
import java.io.LineNumberReader;
import java.io.IOException;
import net.charabia.ac.*;
import java.util.*;
import javax.xml.parsers.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import java.text.ParseException;

public class XMLPlace extends DefaultHandler {

    private Database m_toFill;

    private Entry m_current;

    private int m_state = 0;

    private StringBuffer m_buffer = new StringBuffer();

    private final int NAME = 1, ID = 2, TYPE = 3, LOCATION = 4, EXITLOCATION = 5, ADDEDDATE = 6, MODIFIED = 7, NOTES = 8, LEVELRESTRICTION = 9;

    public XMLPlace(Database fillit) {
        m_toFill = fillit;
    }

    public void startElement(String namespaceURI, String localName, String rawName, Attributes atts) throws SAXException {
        String key = rawName;
        if (key.equalsIgnoreCase("place")) {
            m_current = new Entry();
            m_buffer.setLength(0);
        } else if (key.equalsIgnoreCase("name")) {
            m_state = NAME;
            m_buffer.setLength(0);
        } else if (key.equalsIgnoreCase("id")) {
            m_state = ID;
            m_buffer.setLength(0);
        } else if (key.equalsIgnoreCase("type")) {
            m_state = TYPE;
            m_buffer.setLength(0);
        } else if (key.equalsIgnoreCase("location")) {
            m_state = LOCATION;
            m_buffer.setLength(0);
        } else if (key.equalsIgnoreCase("exitlocation")) {
            m_state = EXITLOCATION;
            m_buffer.setLength(0);
        } else if (key.equalsIgnoreCase("added")) {
            m_state = ADDEDDATE;
            m_buffer.setLength(0);
        } else if (key.equalsIgnoreCase("modified")) {
            m_state = MODIFIED;
            m_buffer.setLength(0);
        } else if (key.equalsIgnoreCase("notes")) {
            m_state = NOTES;
            m_buffer.setLength(0);
        } else if (key.equalsIgnoreCase("levelrestriction")) {
            m_state = LEVELRESTRICTION;
            m_buffer.setLength(0);
        } else if (key.equalsIgnoreCase("br")) {
            m_buffer.append("<p>");
        } else {
            m_state = 0;
        }
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        m_buffer.append(ch, start, length);
    }

    public void endElement(java.lang.String namespaceURI, java.lang.String localName, java.lang.String qName) throws SAXException {
        String key = qName;
        if (key.equalsIgnoreCase("place")) {
            m_toFill.addEntry(m_current);
        } else if (key.equalsIgnoreCase("name")) {
            m_current.Name = m_buffer.toString();
            System.out.println("Name: " + m_buffer.toString());
        } else if (key.equalsIgnoreCase("id")) {
            m_current.Id = Integer.valueOf(m_buffer.toString());
        } else if (key.equalsIgnoreCase("type")) {
            m_current.Type = m_buffer.toString().trim().intern();
            m_current.NormalType = m_current.Type.toLowerCase().intern();
        } else if (key.equalsIgnoreCase("location")) {
            try {
                m_current.Loc = Location.parse(m_buffer.toString());
            } catch (ParseException pe) {
                pe.printStackTrace();
            }
        } else if (key.equalsIgnoreCase("exitlocation")) {
            try {
                if (m_buffer.length() != 0) m_current.ExitLocation = Location.parse(m_buffer.toString());
            } catch (ParseException pe) {
                pe.printStackTrace();
            }
        } else if (key.equalsIgnoreCase("added")) {
            m_current.Added = m_buffer.toString();
        } else if (key.equalsIgnoreCase("modified")) {
        } else if (key.equalsIgnoreCase("notes")) {
            m_current.Notes = m_buffer.toString();
        } else if (key.equalsIgnoreCase("levelrestriction")) {
            m_current.LevelRestriction = m_buffer.toString().intern();
        }
    }
}
