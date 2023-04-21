package org.deft.extension.persistence;

import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Representation of a modification tag in the XML format.
 * 
 * @author Martin Heinzerling
 * 
 */
public class Modification extends Item {

    public Modification(Invoker invoker) {
        super(invoker);
    }

    public Modification(Invoker invoker, Map<String, String> params) {
        super(invoker, params);
    }

    public Modification(Node n) {
        super(n);
    }

    @Override
    protected Element createNode(Document doc) {
        Element root = doc.createElement("modification");
        return root;
    }
}
