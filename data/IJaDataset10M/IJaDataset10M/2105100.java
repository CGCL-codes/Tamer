package com.hp.hpl.jena.reasoner.rdfsReasoner1;

import com.hp.hpl.jena.reasoner.*;
import com.hp.hpl.jena.reasoner.transitiveReasoner.*;
import com.hp.hpl.jena.datatypes.*;
import com.hp.hpl.jena.graph.*;
import com.hp.hpl.jena.graph.impl.*;
import com.hp.hpl.jena.vocabulary.*;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.util.iterator.UniqueExtendedIterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.*;

/**
 * An RDFS reasoner that has been bound to both a TBox and an ABox.
 * It cannot be bound any futher. Once this Bound reasoner has been
 * created all the class, property and associated declarations have
 * been extracted and cached and all queries are answerable directly
 * from the cached results or from query rewrites.
 * 
 * <p>Initially the subClass/subProperty caches are shared with
 * the parent RDFSReasoner so they can be shared across instance data.
 * However, if any update includes any such declarations then the caches
 * have to be cloned and separated.</p>
 * 
 * @author <a href="mailto:der@hplb.hpl.hp.com">Dave Reynolds</a>
 * @version $Revision: 1.23 $ on $Date: 2006/03/22 13:52:22 $
 */
public class RDFSInfGraph extends BaseInfGraph {

    /** The precomputed cache of the subClass graph */
    protected TransitiveGraphCache subClassCache;

    /** Flag to indicate that this cache has already been split off from
     * the parent reasoner */
    protected boolean haveSplitSubClassCache = false;

    /** The precomputed cache of the subProperty graph */
    protected TransitiveGraphCache subPropertyCache;

    /** Router which maps queries onto different cache components that can answer then */
    protected PatternRouter router;

    /** Cache of axiomatci triples to be included in the tripleCache */
    protected FGraph axioms = new FGraph(Factory.createGraphMem());

    /** The data supplied as a tbox, may be null, will be included as part of tripleCache if not null */
    protected Finder tbox;

    /** Cache of precomputed triples which are added to data - this includes 
     * the tbox, axioms and forward deductions */
    protected Finder tripleCache;

    /** Optional map of property node to datatype ranges */
    protected HashMap dtRange = null;

    /** Flag to control whether properties are eagerly scanned */
    protected boolean scanProperties = true;

    protected static Log logger = LogFactory.getLog(RDFSInfGraph.class);

    /** The RDFS forward rule set */
    protected static BaseFRule[] rules = new BaseFRule[] { new AssertFRule("?x rdf:type rdfs:Class -> ?x rdfs:subClassOf rdfs:Resource"), new AssertFRule("?x rdf:type rdfs:Class -> ?x rdfs:subClassOf ?x"), new AssertFRule("?x rdf:type rdf:Property -> ?x rdfs:subPropertyOf ?x"), new BackchainFRule("?p rdfs:subPropertyOf ?q -> ?s ?q ?o <- ?s ?p ?o"), new BackchainFRule("?c rdfs:subClassOf ?d -> ?s rdf:type ?d <- ?s rdf:type ?c"), new BackchainFRule("?p rdfs:domain ?z -> ?s rdf:type ?z <- ?s ?p _"), new BackchainFRule("?p rdfs:range ?z -> ?o rdf:type ?z <- _ ?p ?s") };

    /** The RDFS special case backward rule set */
    protected static BRWRule[] brules = new BRWRule[] { new ResourceBRWRule(), new PropertyBRWRule() };

    /** The RDFS built in axioms */
    protected static Triple[] baseAxioms = new Triple[] { BaseFRule.parseTriple("rdf:type rdfs:range rdfs:Class"), BaseFRule.parseTriple("rdfs:Resource rdf:type rdfs:Class"), BaseFRule.parseTriple("rdfs:Literal rdf:type rdfs:Class"), BaseFRule.parseTriple("rdf:Statement rdf:type rdfs:Class"), BaseFRule.parseTriple("rdf:nil rdf:type rdf:List"), BaseFRule.parseTriple("rdf:XMLLiteral rdf:type rdfs:Datatype"), BaseFRule.parseTriple("rdf:Alt rdf:type rdfs:Class"), BaseFRule.parseTriple("rdf:Seq rdf:type rdfs:Class"), BaseFRule.parseTriple("rdf:Bag rdf:type rdfs:Class"), BaseFRule.parseTriple("rdf:XMLLiteral rdf:type rdfs:Class"), BaseFRule.parseTriple("rdfs:Container rdf:type rdfs:Class"), BaseFRule.parseTriple("rdfs:ContainerMembershipProperty rdf:type rdfs:Class"), BaseFRule.parseTriple("rdfs:isDefinedBy rdf:type rdf:Property"), BaseFRule.parseTriple("rdfs:seeAlso rdf:type rdf:Property"), BaseFRule.parseTriple("rdfs:comment rdf:type rdf:Property"), BaseFRule.parseTriple("rdfs:label rdf:type rdf:Property"), BaseFRule.parseTriple("rdf:subject rdf:type rdf:Property"), BaseFRule.parseTriple("rdf:predicate rdf:type rdf:Property"), BaseFRule.parseTriple("rdf:object rdf:type rdf:Property"), BaseFRule.parseTriple("rdf:first rdf:type rdf:Property"), BaseFRule.parseTriple("rdf:rest rdf:type rdf:Property"), BaseFRule.parseTriple("rdf:type rdf:type rdf:Property"), BaseFRule.parseTriple("rdfs:range rdf:type rdf:Property"), BaseFRule.parseTriple("rdfs:domain rdf:type rdf:Property"), BaseFRule.parseTriple("rdfs:subPropertyOf rdfs:domain rdf:Property"), BaseFRule.parseTriple("rdfs:subPropertyOf rdfs:range rdf:Property"), BaseFRule.parseTriple("rdfs:subClassOf rdfs:domain rdfs:Class"), BaseFRule.parseTriple("rdfs:subClassOf rdfs:range rdfs:Class"), BaseFRule.parseTriple("rdfs:subPropertyOf rdfs:subPropertyOf rdfs:subPropertyOf"), BaseFRule.parseTriple("rdfs:subClassOf rdfs:subPropertyOf rdfs:subClassOf"), BaseFRule.parseTriple("rdf:subject rdfs:subPropertyOf rdf:subject"), BaseFRule.parseTriple("rdf:predicate rdfs:subPropertyOf rdf:predicate"), BaseFRule.parseTriple("rdf:object rdfs:subPropertyOf rdf:object"), BaseFRule.parseTriple("rdf:first rdfs:subPropertyOf rdf:first"), BaseFRule.parseTriple("rdf:rest rdfs:subPropertyOf rdf:rest"), BaseFRule.parseTriple("rdf:type rdfs:subPropertyOf rdf:type"), BaseFRule.parseTriple("rdfs:range rdfs:subPropertyOf rdfs:range"), BaseFRule.parseTriple("rdfs:domain rdfs:subPropertyOf rdfs:domain") };

    /**
     * Constructor
     * @param data the raw data graph being bound to the reasoner
     * @param reasoner the RDFSReasoner which spawned this InfGraph
     */
    public RDFSInfGraph(RDFSReasoner reasoner, Graph data) {
        super(data, reasoner);
        this.scanProperties = reasoner.scanProperties;
    }

    /**
     * Returns the scanProperties flag.
     * 
     * <p>If this is set to true then when a reasoner instance is constructed
     * the whole data graph is scanned to detect all properties and the
     * results are cached. This is expensive but without this
     * some cases of rdf:_n properties will not be handled.</p>
     * 
     * <p>This method is just here for development purposes and will
     * be replaced by the configuration machinery</p>
     * @return boolean
     */
    public boolean getScanProperties() {
        return scanProperties;
    }

    /**
     * Sets the scanProperties flag
     * 
     * <p>If this is set to true then when a reasoner instance is constructed
     * the whole data graph is scanned to detect all properties and the
     * results are cached. This is expensive but without this
     * some cases of rdf:_n properties will not be handled.</p>
     * 
     * <p>This method is just here for development purposes and will
     * be replaced by the configuration machinery</p>
     * @param scanProperties The scanProperties to set
     */
    public void setScanProperties(boolean scanProperties) {
        this.scanProperties = scanProperties;
    }

    /**
    * Return the schema graph, if any, bound into this inference graph.
    */
    public Graph getSchemaGraph() {
        if (tbox == null) return null;
        if (tbox instanceof FGraph) {
            return ((FGraph) tbox).getGraph();
        } else {
            throw new ReasonerException("RDFS1 reasoner got into an illegal state");
        }
    }

    /**
    * Perform any initial processing and caching. This call is optional. Most
    * engines either have negligable set up work or will perform an implicit
    * "prepare" if necessary. The call is provided for those occasions where
    * substantial preparation work is possible (e.g. running a forward chaining
    * rule system) and where an application might wish greater control over when
    * this prepration is done.
    */
    public void prepare() {
        this.subClassCache = ((TransitiveReasoner) reasoner).getSubClassCache();
        this.subPropertyCache = ((TransitiveReasoner) reasoner).getSubPropertyCache().deepCopy();
        this.tbox = ((TransitiveReasoner) reasoner).getTbox();
        haveSplitSubClassCache = false;
        if (tbox == null) {
            tripleCache = axioms;
        } else {
            tripleCache = FinderUtil.cascade(axioms, tbox);
        }
        Graph data = fdata.getGraph();
        if ((TransitiveEngine.checkOccuranceUtility(RDFSReasoner.subPropertyOf, data, subPropertyCache) || TransitiveEngine.checkOccuranceUtility(RDFSReasoner.subClassOf, data, subPropertyCache) || TransitiveEngine.checkOccuranceUtility(RDFSReasoner.domainP, data, subPropertyCache) || TransitiveEngine.checkOccuranceUtility(RDFSReasoner.rangeP, data, subPropertyCache))) {
            Finder tempTbox = tbox == null ? fdata : FinderUtil.cascade(tbox, fdata);
            splitSubClassCache();
            TransitiveEngine.cacheSubPropUtility(tempTbox, subPropertyCache);
            TransitiveEngine.cacheSubClassUtility(tempTbox, subPropertyCache, subClassCache);
            subPropertyCache.setCaching(true);
        }
        for (int i = 0; i < baseAxioms.length; i++) {
            axioms.getGraph().add(baseAxioms[i]);
        }
        TransitiveEngine.cacheSubPropUtility(axioms, subPropertyCache);
        if (scanProperties) {
            ExtendedIterator it = tripleCache.findWithContinuation(new TriplePattern(null, null, null), fdata);
            HashSet properties = new HashSet();
            String memberPrefix = RDF.getURI() + "_";
            Node sP = RDF.Property.asNode();
            while (it.hasNext()) {
                Triple triple = (Triple) it.next();
                Node prop = triple.getPredicate();
                if (prop.equals(RDF.type.asNode()) && prop.equals(RDF.Property.asNode())) {
                    prop = triple.getSubject();
                }
                if (properties.add(prop)) {
                    subPropertyCache.addRelation(new Triple(prop, sP, prop));
                    if (prop.getURI().startsWith(memberPrefix)) {
                        axioms.getGraph().add(new Triple(prop, RDF.type.asNode(), RDFS.ContainerMembershipProperty.asNode()));
                        subPropertyCache.addRelation(new Triple(prop, sP, RDFS.member.asNode()));
                    }
                }
            }
        }
        router = new PatternRouter();
        router.register(subPropertyCache);
        router.register(subClassCache);
        checkAllForwardRules();
        for (int i = 0; i < brules.length; i++) {
            addBRule(brules[i]);
        }
        isPrepared = true;
    }

    /**
     * Extended find interface used in situations where the implementator
     * may or may not be able to answer the complete query. It will
     * attempt to answer the pattern but if its answers are not known
     * to be complete then it will also pass the request on to the nested
     * Finder to append more results.
     * @param pattern a TriplePattern to be matched against the data
     * @param continuation either a Finder or a normal Graph which
     * will be asked for additional match results if the implementor
     * may not have completely satisfied the query.
     */
    public ExtendedIterator findWithContinuation(TriplePattern pattern, Finder continuation) {
        checkOpen();
        if (!isPrepared) prepare();
        return new UniqueExtendedIterator(router.find(pattern, tripleCache, continuation, this));
    }

    /**
     * Variant on find called by backward rules, additional 
     * argument used to pass set of instantiated rules to prevent
     * run-away rule firing.
     */
    public ExtendedIterator findNested(TriplePattern pattern, Finder continuation, HashSet firedRules) {
        return router.find(pattern, tripleCache, continuation, this, firedRules);
    }

    /**
     * Variant on find called by special backward rules that only 
     * access the raw data and axioms and bypass further rules
     */
    public ExtendedIterator findRawWithContinuation(TriplePattern pattern, Finder continuation) {
        return tripleCache.findWithContinuation(pattern, continuation);
    }

    /**
     * Variant on find called by special backward rules that need
     * to list all pre-registered properties. The iterator returns Nodes
     * not triples.
     */
    public ExtendedIterator findProperties() {
        return subPropertyCache.listAllSubjects();
    }

    /**
     * Variant on find called by special backward rules that need
     * to list check for a specific preregistered property.
     */
    public boolean isProperty(Node prop) {
        return subPropertyCache.isSubject(prop);
    }

    /**
     * Test the consistency of the bound data. This normally tests
     * the validity of the bound instance data against the bound
     * schema data. 
     * @return a ValidityReport structure
     */
    public ValidityReport validate() {
        StandardValidityReport report = new StandardValidityReport();
        HashMap dtRange = getDTRange();
        for (Iterator props = dtRange.keySet().iterator(); props.hasNext(); ) {
            Node prop = (Node) props.next();
            for (Iterator i = find(null, prop, null); i.hasNext(); ) {
                Triple triple = (Triple) i.next();
                report.add(checkLiteral(prop, triple.getObject()));
            }
        }
        return report;
    }

    /**
     * Return a map from property nodes to a list of RDFDatatype objects
     * which have been declared as the range of that property.
     */
    private HashMap getDTRange() {
        if (dtRange == null) {
            dtRange = new HashMap();
            for (Iterator i = find(null, RDFS.range.asNode(), null); i.hasNext(); ) {
                Triple triple = (Triple) i.next();
                Node prop = triple.getSubject();
                Node rangeValue = triple.getObject();
                if (rangeValue.isURI()) {
                    RDFDatatype dt = TypeMapper.getInstance().getTypeByName(rangeValue.getURI());
                    if (dt != null) {
                        List range = (ArrayList) dtRange.get(prop);
                        if (range == null) {
                            range = new ArrayList();
                            dtRange.put(prop, range);
                        }
                        range.add(dt);
                    }
                }
            }
        }
        return dtRange;
    }

    /**
     * Check a given literal value for a property against the set of
     * known range constraints for it.
     * @param prop the property node whose range is under scrutiny
     * @param value the literal node whose value is to be checked
     * @return null if the range is legal, otherwise a ValidityReport.Report
     * which describes the problem.
     */
    private ValidityReport.Report checkLiteral(Node prop, Node value) {
        List range = (List) getDTRange().get(prop);
        if (range != null) {
            if (!value.isLiteral()) {
                return new ValidityReport.Report(true, "dtRange", "Property " + prop + " has a typed range but was given a non literal value " + value);
            }
            LiteralLabel ll = value.getLiteral();
            for (Iterator i = range.iterator(); i.hasNext(); ) {
                RDFDatatype dt = (RDFDatatype) i.next();
                if (!dt.isValidLiteral(ll)) {
                    return new ValidityReport.Report(true, "dtRange", "Property " + prop + " has a typed range " + dt + "that is not compatible with " + value);
                }
            }
        }
        return null;
    }

    /**
     * Run all the builtin forward rules, on all the elements in the tbox and data
     * graphs. Checkes for all subProperties of the properties mentioned in the
     * rules themselves.
     */
    private void checkAllForwardRules() {
        Finder caches = FinderUtil.cascade(subPropertyCache, subClassCache, tripleCache);
        for (int i = 0; i < rules.length; i++) {
            BaseFRule rule = rules[i];
            TriplePattern head = rule.getHead();
            Node pPattern = head.getPredicate();
            if (pPattern.isVariable()) {
                checkRule(head, rule, caches);
            } else {
                TriplePattern spPatt = new TriplePattern(null, TransitiveReasoner.subPropertyOf, pPattern);
                ExtendedIterator sps = subPropertyCache.find(spPatt);
                while (sps.hasNext()) {
                    TriplePattern altHead = new TriplePattern(head.getSubject(), ((Triple) sps.next()).getSubject(), head.getObject());
                    checkRule(altHead, rule, caches);
                }
            }
        }
    }

    /**
     * Run a single rule, with the rewritten head, against the data
     */
    private void checkRule(TriplePattern altHead, BaseFRule rule, Finder caches) {
        Iterator it = caches.findWithContinuation(altHead, fdata);
        while (it.hasNext()) {
            Triple t = (Triple) it.next();
            rule.bindAndFire(t, this);
        }
    }

    /**
     * Assert a triple into the triple cache.
     * Called by FRules when they fire
     */
    public void assertTriple(Triple t) {
        axioms.getGraph().add(t);
    }

    /**
     * Add a new backchaining rule into the rule set.
     * Called by FRules when they fire
     */
    public void addBRule(BRWRule rule) {
        router.register(rule);
    }

    /**
     * Separate the cache of subClassOf relations from the parent reasoner
     * because new added data has changed the class lattice
     */
    private void splitSubClassCache() {
        if (!haveSplitSubClassCache) {
            subClassCache = subClassCache.deepCopy();
            haveSplitSubClassCache = true;
        }
    }

    /**
     * Printable version of the whole reasoner state.
     * Used during debugging
     */
    public String toString() {
        StringBuffer state = new StringBuffer();
        TriplePattern all = new TriplePattern(null, null, null);
        if (tripleCache != null) {
            state.append("axioms + tbox\n");
            for (Iterator i = tripleCache.find(all); i.hasNext(); ) {
                state.append(TriplePattern.simplePrintString((Triple) i.next()));
                state.append("\n");
            }
        }
        if (fdata != null) {
            state.append("Bound raw data\n");
            for (Iterator i = fdata.find(all); i.hasNext(); ) {
                state.append(TriplePattern.simplePrintString((Triple) i.next()));
                state.append("\n");
            }
        }
        if (router != null) {
            state.append("Rule set\n");
            state.append(router.toString());
        }
        return state.toString();
    }
}
