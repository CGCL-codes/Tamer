package de.fzi.herakles.strategy.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.apache.log4j.Logger;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLOntology;
import de.fzi.herakles.commons.strategy.LoadException;

/**
 * @author bock
 *
 */
public class AnalysingLoadStrategy extends BasicLoadStrategy {

    private Map<OWLOntology, Properties> ontologyProperties;

    private Logger logger;

    static {
        property = new Properties();
        property.setProperty("name", "Analysing Load Strategy");
        property.setProperty("description", "All reasoners load all ontologies and analyse the ontologies.");
    }

    public AnalysingLoadStrategy() {
        super();
        this.ontologyProperties = new HashMap<OWLOntology, Properties>();
        logger = Logger.getLogger(AnalysingLoadStrategy.class);
    }

    @Override
    public Map<OWLOntology, Properties> getOntologyProperties() {
        return this.ontologyProperties;
    }
}
