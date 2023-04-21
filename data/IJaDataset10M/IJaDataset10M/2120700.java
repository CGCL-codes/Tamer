package de.moonflower.jfritz.utils.reverselookup;

import java.util.Vector;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import de.moonflower.jfritz.struct.ReverseLookupSite;
import de.moonflower.jfritz.utils.Debug;

/**
 *	This class is responsible for parsing the reverseloop.xml file
 * 	Important to note is that both url and name must be filled out!!!
 * 
 * @see de.moonflower.jfritz.struct.ReverseLookupSite
 * 
 *  created 06.02.07
 * 
 * @author brian jensen
 *
 */
public class ReverseLookupXMLHandler extends DefaultHandler {

    String chars, url, name, prefix, country_code, pname, pstreet, pcity, pzipcode, pfirst, plast, firstOccurance;

    Vector<ReverseLookupSite> rls_list;

    int rls_count, ac_length;

    public ReverseLookupXMLHandler() {
        super();
    }

    public void startDocument() throws SAXException {
    }

    public void endDocument() throws SAXException {
    }

    public void startElement(String namespaceURI, String lName, String qName, Attributes attrs) throws SAXException {
        String eName = lName;
        if ("".equals(eName)) eName = qName;
        chars = "";
        if (eName.equals("country")) {
            rls_count = 0;
            rls_list = new Vector<ReverseLookupSite>(2);
        } else if (eName.equals("website")) {
            url = "";
            name = "";
            prefix = "";
            ac_length = 0;
            firstOccurance = "name";
        } else if (eName.equals("entry")) {
            pname = "";
            pstreet = "";
            pcity = "";
            pzipcode = "";
            pfirst = "";
            plast = "";
        }
        if (attrs != null) {
            for (int i = 0; i < attrs.getLength(); i++) {
                String aName = attrs.getLocalName(i);
                if ("".equals(aName)) aName = attrs.getQName(i);
                if (eName.equals("reverselookup") && aName.equals("version")) Debug.info("Loading reverselookup.xml version " + attrs.getValue(i));
                if (eName.equals("country") && aName.equals("code")) {
                    country_code = attrs.getValue(i);
                } else if (eName.equals("website")) {
                    if (aName.equals("url")) url = attrs.getValue(i); else if (aName.equals("name")) name = attrs.getValue(i); else if (aName.equals("prefix")) prefix = attrs.getValue(i); else if (aName.equals("areacode")) ac_length = Integer.parseInt(attrs.getValue(i));
                    if (i == attrs.getLength() - 1) {
                        rls_list.add(new ReverseLookupSite(url, name, prefix, ac_length));
                    }
                } else if (eName.equals("entry")) {
                    if (aName.equals("firstOccurance")) {
                        firstOccurance = attrs.getValue(i);
                    }
                }
            }
        }
    }

    public void endElement(String namespaceURI, String sName, String qName) throws SAXException {
        if (qName.equals("name")) {
            pname = chars;
        } else if (qName.equals("street")) {
            pstreet = chars;
        } else if (qName.equals("city")) {
            pcity = chars;
        } else if (qName.equals("zipcode")) {
            pzipcode = chars;
        } else if (qName.equals("firstname")) {
            pfirst = chars;
        } else if (qName.equals("lastname")) {
            plast = chars;
        } else if (qName.equals("entry")) {
            rls_list.get(rls_count).addEntry(firstOccurance, pname, pstreet, pcity, pzipcode, pfirst, plast);
        } else if (qName.equals("website")) {
            rls_count++;
        } else if (qName.equals("country")) {
            if (!country_code.equals("")) {
                ReverseLookup.addReverseLookupSites(country_code, rls_list);
            }
        }
    }

    public void characters(char buf[], int offset, int len) throws SAXException {
        chars += new String(buf, offset, len);
    }
}
