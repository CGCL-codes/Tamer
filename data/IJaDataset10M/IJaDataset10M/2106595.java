package uk.ac.ebi.hawthorn;

import java.util.HashMap;
import java.util.Map;
import java.util.Collections;
import java.io.IOException;

/**
 * Manages a set of ontology instances.
 *
 * @author  Antony Quinn
 * @version $Id: OntologyManager.java,v 1.2 2005/06/21 14:49:11 aquinn Exp $
 * @since   1.0
 */
public class OntologyManager {

    private static Map ontologyFactories = new HashMap();

    /**
     * Remove ontology from list of registered ontologies.
     *
     * @param className Ontology class name
     */
    public static void deregisterOntology(String className) {
        ontologyFactories.remove(className);
    }

    /**
     * Registers ontology.
     *
     * @param className         Ontology class name
     * @param ontologyFactory   OntologyFactory instance
     * @see   Ontology
     * @see   OntologyFactory
     */
    public static void registerOntology(String className, OntologyFactory ontologyFactory) {
        ontologyFactories.put(className, ontologyFactory);
    }

    /**
     * Returns ontology instance.
     *
     * @param   className               Ontology class name
     * @param   prefix                  Ontology prefix
     * @param   uri                     URL or relative path to ontology
     * @param   userName                User name to access secured ontologies (optional)
     * @param   password                Password to access secured ontologies (optional)
     * @param   refreshInterval         How often to check ontology for updates
     * @param   tolerateRefreshException Allow refresh exceptions to be logged or thrown
     * @param   inputStreamListener     Listens for calls to obtain input streams 
     * @return  ontology instance
     * @throws  IOException             if could not load ontology
     * @throws  ClassNotFoundException  if could not find <code>className</code>
     */
    public static Ontology getOntology(String className, String prefix, String uri, String userName, String password, int refreshInterval, boolean tolerateRefreshException, InputStreamListener inputStreamListener) throws IOException, ClassNotFoundException {
        if (ontologyFactories.containsKey(className)) {
            OntologyFactory ontologyFactory = (OntologyFactory) ontologyFactories.get(className);
            return ontologyFactory.getInstance(prefix, uri, userName, password, refreshInterval, tolerateRefreshException, inputStreamListener);
        }
        throw new ClassNotFoundException("Could not find " + className);
    }

    /**
     * Returns map of all registered ontology factories
     *
     * @return  map of all registered ontology factories
     * @see     OntologyFactory
     */
    public static Map getOntologyFactories() {
        return Collections.unmodifiableMap(ontologyFactories);
    }
}
