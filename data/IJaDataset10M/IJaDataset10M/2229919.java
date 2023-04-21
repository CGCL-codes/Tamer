package edu.bsu.monopoly.jtests;

import org.xml.sax.*;
import junit.framework.TestCase;

public class EstateAttrTest implements Attributes {

    String[] attrs;

    int len;

    public EstateAttrTest() {
        attrs = new String[] { "estateid", "10", "color", "maroon", "bgcolor", "maroon", "owner", "10", "houseprice", "10", "group", "10", "can_be_owned", "1", "can_toggle_mortgage", "1", "can_sell_houses", "1", "price", "10", "rent0", "10", "rent1", "10", "rent2", "10", "rent3", "10", "rent4", "10", "rent5", "10", "name", "property", "houses", "5", "money", "10", "mortgageprice", "10", "unmortgageprice", "10", "sellhouseprice", "10", "mortgaged", "0", "tax", "10", "taxpercentage", "10" };
        len = 25;
    }

    public int getLength() {
        return len;
    }

    public String getQName(int arg0) {
        return attrs[2 * arg0];
    }

    public String getType(int arg0) {
        return null;
    }

    public String getType(String arg0) {
        return null;
    }

    public String getValue(int arg0) {
        return attrs[(2 * arg0) + 1];
    }

    public String getValue(String arg0, String arg1) {
        return null;
    }

    public int getLocalName(String arg0) {
        return 0;
    }

    public String getValue(String arg0) {
        return null;
    }

    public int getIndex(String arg0) {
        return 0;
    }

    public int getIndex(String arg0, String arg1) {
        return 0;
    }

    public String getLocalName(int arg0) {
        return null;
    }

    public String getType(String arg0, String arg1) {
        return null;
    }

    public String getURI(int arg0) {
        return null;
    }
}
