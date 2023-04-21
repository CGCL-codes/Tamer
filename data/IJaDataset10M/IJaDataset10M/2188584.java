package uk.co.kimble.cobra;

import java.util.Hashtable;
import java.util.Vector;
import java.util.Enumeration;
import java.sql.*;

/**
 *	Initialise class dictionary from XML file
 *
 *  @author		David B George
 *  @version	$\Revision$
 */
class XMLClassDictionary extends ClassDictionary {

    void setClass(String s) {
        dictionary = s;
    }

    void setTable(String s) {
        table = s;
    }

    public void setDatabase(String s) {
        database = s;
    }

    public void setMapping(String attribute, String column, String primary) {
        if (primary.equalsIgnoreCase("true")) {
            class_map.put(attribute, new Attribute(column, true));
            primary_keys.addElement(attribute);
        } else {
            class_map.put(attribute, new Attribute(column, false));
        }
        reverse_class_map.put(column, attribute);
    }
}
