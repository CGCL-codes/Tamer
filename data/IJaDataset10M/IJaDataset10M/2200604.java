package org.linkedgeodata.util.sparql.cache;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.aksw.commons.collections.random.RandomUtils;
import org.apache.log4j.PropertyConfigurator;
import org.linkedgeodata.osm.osmosis.plugins.RDFDiff;
import org.linkedgeodata.util.CollectionUtils;
import org.linkedgeodata.util.ModelUtil;
import org.linkedgeodata.util.StringUtil;
import org.linkedgeodata.util.VirtuosoUtils;
import org.linkedgeodata.util.collections.CacheSet;
import org.linkedgeodata.util.sparql.ISparqlExecutor;
import org.linkedgeodata.util.sparql.ISparulExecutor;
import org.linkedgeodata.util.sparql.JenaSparulExecutor;
import org.linkedgeodata.util.sparql.SparqlEndpointExecutor;
import org.linkedgeodata.util.sparql.VirtuosoJdbcSparulExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;

/**
 * This class is not finished yet.
 * 
 * Some thoughts: It would be cool to have a "construct cache:"
 * 
 * E.g. Construct { ?p :hasName ?name . ?p :hasAddress  ?a . }
 * The graph pattern can be decomposed into clauses:
 * (And(.)
 * 
 * Our cache function is vars(Query) -> Model
 * (e.g. (?p ?a) -> Model) (Hm actually it doesn't matter whether its a model or not)
 * 
 * 
 * Whenever a triple is inserted, that satisfied one of the clauses,  we
 * need to invalidate a corresponding cache entry.
 * 
 * 
 * 
 * @author raven
 *
 */
public class GraphBackedResourceCache {

    private static final Logger logger = LoggerFactory.getLogger(GraphBackedResourceCache.class);

    private String graphName;

    private ISparulExecutor graphDAO;

    private int batchSize = 1024;

    private RDFDiff pendingUpdates = new RDFDiff();

    private Model cacheData = ModelFactory.createDefaultModel();

    private CacheSet<Resource> posCache = new CacheSet<Resource>();

    private CacheSet<Resource> negCache = new CacheSet<Resource>();

    public static void mainThis(String[] args) throws Throwable {
        PropertyConfigurator.configure("log4j.properties");
        System.out.println("Test");
        String graphName = "http://test.org";
        Connection conn = VirtuosoUtils.connect("localhost", "dba", "dba");
        ISparulExecutor graphDAO = new VirtuosoJdbcSparulExecutor(conn, graphName);
        GraphBackedResourceCache cache = new GraphBackedResourceCache(graphDAO);
        List<Resource> resources = Arrays.asList(new Resource[] { ResourceFactory.createResource("http://s.org"), ResourceFactory.createResource("http://linkedgeodata.org/triplify/way54888992/nodes") });
        Model m;
        m = cache.lookup(resources);
        m = cache.lookup(resources);
        System.out.println(ModelUtil.toString(m));
    }

    public GraphBackedResourceCache(ISparulExecutor graphDAO) {
        this.graphDAO = graphDAO;
    }

    public Model lookup(Collection<Resource> resources) throws Exception {
        Model result = ModelFactory.createDefaultModel();
        Set<Resource> ress = new HashSet<Resource>(resources);
        int negCacheHits = 0;
        int posCacheHits = 0;
        Iterator<Resource> it = ress.iterator();
        while (it.hasNext()) {
            Resource resource = it.next();
            if (negCache.contains(resource)) {
                it.remove();
                ++negCacheHits;
                continue;
            }
            if (posCache.contains(resource)) {
                result.add(cacheData.listStatements(resource, null, (RDFNode) null));
                posCache.renew(ress);
                it.remove();
                ++posCacheHits;
            }
        }
        Model lookup = lookupBySubject(graphDAO, ress, graphName, batchSize);
        Set<Resource> subjects = lookup.listSubjects().toSet();
        for (Resource resource : ress) {
            if (subjects.contains(resource)) {
                Resource removed = posCache.addAndGetRemoved(resource);
                if (removed != null) {
                    cacheData.remove(removed, null, (RDFNode) null);
                }
                cacheData.add(lookup.listStatements(resource, null, (RDFNode) null));
            } else {
                negCache.add(resource);
            }
        }
        result.add(lookup);
        logger.debug("Cache statistics for lookup on " + resources.size() + " resources: posHit/negHit/retrieve = " + posCacheHits + "/" + negCacheHits + "/" + ress.size());
        return result;
    }

    public void insert(Model model) throws Exception {
        Model added = ModelFactory.createDefaultModel();
        added.add(model);
        Model oldModel = lookup(model.listSubjects().toSet());
        added.remove(oldModel);
        pendingUpdates.add(added);
        for (Resource resource : model.listSubjects().toSet()) {
            if (negCache.contains(resource)) {
                negCache.remove(resource);
                Resource removed = posCache.addAndGetRemoved(resource);
                cacheData.remove(removed, null, (RDFNode) null);
            }
            if (posCache.contains(resource)) {
                cacheData.add(added.listStatements(resource, null, (RDFNode) null));
            }
        }
    }

    public void remove(Model model) throws Exception {
        Model removed = ModelFactory.createDefaultModel();
        removed.add(model);
        Model oldModel = lookup(model.listSubjects().toSet());
        removed.remove(oldModel);
        pendingUpdates.remove(removed);
        for (Resource resource : model.listSubjects().toSet()) {
            if (negCache.contains(resource)) {
                negCache.remove(resource);
                Resource rem = posCache.addAndGetRemoved(resource);
                cacheData.remove(rem, null, (RDFNode) null);
            }
            if (posCache.contains(resource)) {
                cacheData.add(removed.listStatements(resource, null, (RDFNode) null));
            }
        }
    }

    public void applyChanges() throws Exception {
        graphDAO.remove(pendingUpdates.getRemoved(), graphName);
        graphDAO.insert(pendingUpdates.getAdded(), graphName);
    }

    private static Model lookupBySubject(ISparulExecutor graphDAO, Collection<Resource> subjects, String graphName, int batchSize) throws Exception {
        Model result = ModelFactory.createDefaultModel();
        List<List<Resource>> chunks = CollectionUtils.chunk(subjects, batchSize);
        for (List<Resource> chunk : chunks) {
            String resources = "<" + StringUtil.implode(">,<", chunk) + ">";
            String fromPart = (graphName != null) ? "From <" + graphName + "> " : "";
            String query = "Construct { ?s ?p ?o . } " + fromPart + "{ ?s ?p ?o . Filter(?s In (" + resources + ")) . }";
            Model tmp = graphDAO.executeConstruct(query);
            result.add(tmp);
        }
        return result;
    }

    public static String myToString(Collection<?> collection) {
        return "(" + collection.size() + ")" + collection;
    }

    public static Set<List<Object>> toKeys(Collection<Resource> resources) {
        Set<List<Object>> result = new HashSet<List<Object>>();
        for (Resource item : resources) {
            result.add(Collections.singletonList((Object) item.asNode()));
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        PropertyConfigurator.configure("log4j.properties");
        List<String> a = Arrays.asList("a");
        List<String> b = new ArrayList<String>(a);
        List<String> c = new LinkedList<String>(a);
        List<String> d = Collections.singletonList("a");
        System.out.println(a.hashCode());
        System.out.println(b.hashCode());
        System.out.println(c.hashCode());
        System.out.println(d.hashCode());
        Random random = new Random(0);
        ISparqlExecutor tmpSparqlEndpoint = new SparqlEndpointExecutor("http://localhost:8890/sparql", "http://test.org");
        List<QuerySolution> qss = tmpSparqlEndpoint.executeSelect("Select Distinct ?s From <http://Exp3Random.log> {?s ?p ?o. }");
        Set<Resource> subjects = new HashSet<Resource>();
        for (QuerySolution qs : qss) {
            subjects.add(qs.getResource("s"));
        }
        Model tmpModel = tmpSparqlEndpoint.executeConstruct("Construct {?s ?p ?o. } From <http://Exp3Random.log> {?s ?p ?o. }");
        System.out.println("TmpModelSize: " + tmpModel.size());
        JenaSparulExecutor sparqlEndpoint = new JenaSparulExecutor(tmpModel);
        SparqlEndpointFilteredGraph graph = new SparqlEndpointFilteredGraph(sparqlEndpoint, "http://test.org");
        TripleCacheIndexImpl.create(graph, 100000, 100000, 100000, 0);
        Node n = Node.createURI("http://nke/Exp3Random/Actors_from_Tennessee/fold/1/phase/1/5");
        DeltaGraph deltaGraph = new DeltaGraph(graph);
        Set<Triple> triples = new HashSet<Triple>();
        Triple myTriple = new Triple(n, Node.createURI("http://p"), Node.createURI("http://o"));
        triples.add(myTriple);
        deltaGraph.add(triples);
        deltaGraph.remove(triples);
        Set<Triple> qr = deltaGraph.bulkFind(Collections.singleton(Collections.singletonList((Object) n)), new int[] { 0 });
        System.out.println(myToString(qr));
        System.out.println(deltaGraph.getBaseGraph());
        for (int i = 0; i < 100000; ++i) {
            Set<Resource> resources = RandomUtils.randomSampleSet(subjects, 1000, random);
            Set<List<Object>> keys = toKeys(resources);
            logger.trace("Finding " + keys.size() + " keys");
            Set<Triple> cache = deltaGraph.bulkFind(keys, new int[] { 0 });
            if (i % 1000 == 0) {
                System.out.println(i + ": " + deltaGraph.getBaseGraph());
            }
        }
        if (true) return;
        String[] resourceStrs = { "http://nke/Exp3Random/Actors_from_Tennessee/fold/1/phase/1/5", "http://nke/Exp3Random/Actors_from_Tennessee/fold/1/phase/1/11/14", "http://nke/Exp3Random/Actors_from_Tennessee/fold/1/phase/1/3" };
        List<List<Object>> resources = new ArrayList<List<Object>>();
        for (String resourceStr : resourceStrs) {
            Resource resource = ResourceFactory.createResource(resourceStr);
            resources.add(Collections.singletonList((Object) resource));
        }
        List<QuerySolution> rs = sparqlEndpoint.executeSelect("Select Distinct ?s From <http://Exp3Random.log> {?s ?p ?o .} Limit 20");
        for (QuerySolution qs : rs) {
            System.out.println(qs.get("s"));
        }
    }
}
