package org.equanda.tapestry.model;

import org.equanda.util.xml.tree.ParentNode;
import org.equanda.util.xml.tree.XMLTreeException;
import javolution.xml.sax.Attributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * representation of a &lt;database&gt; node
 *
 * @author Florin
 */
public class GMDatabase extends ParentNode {

    private Map<String, ArrayList<String>> categories = new HashMap<String, ArrayList<String>>();

    public String getTagName() {
        return "database";
    }

    public void setAttribute(CharSequence name, CharSequence value) throws XMLTreeException {
        throw new XMLTreeException(XMLTreeException.NOT_SUPPORTED_ERR, "attribute " + name + " value " + value + " not allowed");
    }

    public boolean canAppendChild(CharSequence tag, Attributes attr) {
        if (tag.equals("table")) {
            String name = attr.getValue("name").toString();
            String category = attr.getValue("category").toString();
            if (categories.keySet().contains(category)) {
                categories.get(category).add(name);
            } else {
                ArrayList<String> tables = new ArrayList<String>();
                tables.add(name);
                categories.put(category, tables);
            }
        } else {
            return super.canAppendChild(tag, attr);
        }
        return false;
    }

    public ArrayList<String> getTables() {
        ArrayList<String> tables = new ArrayList();
        for (String category : categories.keySet()) {
            tables.addAll(categories.get(category));
        }
        return tables;
    }

    public Map<String, ArrayList<String>> getCategories() {
        return categories;
    }
}
