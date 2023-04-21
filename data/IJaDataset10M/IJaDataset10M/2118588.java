package org.marcont.portal.ontologyeditor.ontology;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author katar
 */
public class OntologyManager {

    private static Map<String, Ontology> ontologyMap = new HashMap<String, Ontology>();

    public static Ontology getOntology(String login) {
        if (!ontologyMap.containsKey(login)) {
            Ontology ont = new Ontology();
            ontologyMap.put(login, ont);
        }
        return ontologyMap.get(login);
    }
}
