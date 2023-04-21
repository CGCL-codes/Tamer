package com.g2inc.scap.library.domain;

import java.util.HashMap;
import java.util.Map;
import org.jdom.Namespace;

/**
 * This class accumulates a set of referenced namespaces.
 *
 * @author ssill2
 */
public class NamespaceReferenceCounter {

    private Map<Namespace, Integer> namespaceMap = new HashMap<Namespace, Integer>();

    public void addNamespace(Namespace ns) {
        if (namespaceMap.containsKey(ns)) {
            Integer i = namespaceMap.get(ns);
            namespaceMap.put(ns, new Integer(i.intValue() + 1));
        } else {
            namespaceMap.put(ns, new Integer(1));
        }
    }

    public Map<Namespace, Integer> getNamespaceMap() {
        return namespaceMap;
    }
}
