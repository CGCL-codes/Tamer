package com.hp.hpl.jena.reasoner.rdfsReasoner1;

import com.hp.hpl.jena.reasoner.*;
import com.hp.hpl.jena.reasoner.rulesys.Util;
import com.hp.hpl.jena.reasoner.transitiveReasoner.*;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.graph.*;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.hp.hpl.jena.vocabulary.ReasonerVocabulary;

/**
 * @deprecated Obsoleted at jena2p4, replaced by 
 * {@link com.hp.hpl.jena.reasoner.rulesys.RDFSRuleReasoner}.
 * 
 * An RDFS reasoner suited to modest vocabularies but large instance
 * data. It does eager processing on the class and property declarations
 * and caches the results. This means that the initial creation can
 * be slow. However, if the vocabulary and instance data can be
 * separated then at least the class lattice results can be reused.
 * <p>
 * Instance related rules are implemented using a very simple rewrite
 * system. Triple queries that match a rule are rewritten and reapplied 
 * as queries. This is reasonably efficient for fairly ground queries,
 * especially where the predicate is ground. It performs redundant
 * passes over the data for unground queries, especially any that
 * need that might match (*, type, Resource) or (*, type, Property)!</p>
 * 
 * @author <a href="mailto:der@hplb.hpl.hp.com">Dave Reynolds</a>
 * @version $Revision: 1.22 $ on $Date: 2006/03/22 13:52:22 $
 */
public class RDFSReasoner extends TransitiveReasoner implements Reasoner {

    /** The domain property */
    public static final Node domainP = RDFS.Nodes.domain;

    /** The range property */
    public static final Node rangeP = RDFS.Nodes.range;

    /** Note if the reasoner is configured to scan for member properties */
    protected boolean scanProperties = true;

    /** Constructor */
    public RDFSReasoner() {
        super();
    }

    /** 
     * Constructor 
     * @param configuration a set of arbitrary configuration information to be 
     * passed the reasoner, encoded as RDF properties of a base configuration resource,
     * can be null in no custom configuration is required. The
     * only meaningful configuration property at present is scanProperties.
     */
    public RDFSReasoner(Resource configuration) {
        super();
        if (configuration != null) {
            Boolean flag = checkBinaryPredicate(RDFSReasonerFactory.scanProperties, configuration);
            if (flag != null) scanProperties = flag.booleanValue();
        }
    }

    /**
     * Private constructor used by bindSchema when
     * returning a partially bound reasoner instance.
     */
    protected RDFSReasoner(Finder tbox, TransitiveGraphCache subClassCache, TransitiveGraphCache subPropertyCache, boolean scanProperties) {
        super(tbox, subClassCache, subPropertyCache);
        this.scanProperties = scanProperties;
    }

    /**
     * Determine whether the given property is recognized and treated specially
     * by this reasoner. This is a convenience packaging of a special case of getCapabilities.
     * @param property the property which we want to ask the reasoner about, given as a Node since
     * this is part of the SPI rather than API
     * @return true if the given property is handled specially by the reasoner.
     */
    public boolean supportsProperty(Property property) {
        ReasonerFactory rf = RDFSReasonerFactory.theInstance();
        Model caps = rf.getCapabilities();
        Resource root = caps.getResource(rf.getURI());
        return caps.contains(root, ReasonerVocabulary.supportsP, property);
    }

    /**
     * Helper method - extracts the truth of a boolean configuration
     * predicate.
     * @param pred the predicate to be tested
     * @param configuration the configuration node
     * @return null if there is no setting otherwise a Boolean giving the setting value
     */
    private Boolean checkBinaryPredicate(Property predicate, Resource configuration) {
        StmtIterator i = configuration.listProperties(predicate);
        if (i.hasNext()) {
            return new Boolean(i.nextStatement().getObject().toString().equalsIgnoreCase("true"));
        } else {
            return null;
        }
    }

    /**
     * Extracts all of the subClass and subProperty declarations from
     * the given schema/tbox and caches the resultant graphs.
     * It can only be used once, can't stack up multiple tboxes this way.
     * This limitation could be lifted - the only difficulty is the need to
     * reprocess all the earlier tboxes if a new subPropertyOf subPropertyOf
     * subClassOf is discovered.
     * @param tbox schema containing the property and class declarations
     */
    public Reasoner bindSchema(Graph tbox) throws ReasonerException {
        if (this.tbox != null) {
            throw new ReasonerException("Attempt to bind multiple rulesets - disallowed for now");
        }
        FGraph ftbox = new FGraph(tbox);
        TransitiveGraphCache sCc = new TransitiveGraphCache(directSubClassOf, subClassOf);
        TransitiveGraphCache sPc = new TransitiveGraphCache(directSubPropertyOf, subPropertyOf);
        TransitiveEngine.cacheSubPropUtility(ftbox, sPc);
        TransitiveEngine.cacheSubClassUtility(ftbox, sPc, sCc);
        sPc.setCaching(true);
        return new RDFSReasoner(ftbox, sCc, sPc, scanProperties);
    }

    /**
     * Attach the reasoner to a set of RDF ddata to process.
     * The reasoner may already have been bound to specific rules or ontology
     * axioms (encoded in RDF) through earlier bindRuleset calls.
     * @param data the RDF data to be processed, some reasoners may restrict
     * the range of RDF which is legal here (e.g. syntactic restrictions in OWL).
     * @return an inference graph through which the data+reasoner can be queried.
     * @throws ReasonerException if the data is ill-formed according to the
     * constraints imposed by this reasoner.
     */
    public InfGraph bind(Graph data) throws ReasonerException {
        return new RDFSInfGraph(this, data);
    }

    /**
     * Switch on/off drivation logging.
     * If set to true then the InfGraph created from the bind operation will start
     * life with recording of derivations switched on. This is currently only of relevance
     * to rule-based reasoners.
     * <p>
     * Default - false.
     */
    public void setDerivationLogging(boolean logOn) {
    }

    /**
     * Set a configuration parameter for the reasoner. The only supported parameter at present is:
     * are:
     * <ul>
     * <li>RDFSReasonerFactory.scanProperties - set this to Boolean true to
     * enable scanning of all properties looking for container membership properties, default on. </li>
     * </ul>
     * 
     * @param parameter the property identifying the parameter to be changed
     * @param value the new value for the parameter, typically this is a wrapped
     * java object like Boolean or Integer.
     */
    public void setParameter(Property parameter, Object value) {
        if (parameter.equals(RDFSReasonerFactory.scanProperties)) {
            scanProperties = Util.convertBooleanPredicateArg(parameter, value);
        } else {
            throw new IllegalParameterException(parameter.toString());
        }
    }
}
