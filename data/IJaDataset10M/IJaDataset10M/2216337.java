package at.jku.rdfstats.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import at.jku.rdfstats.Constants;
import at.jku.rdfstats.GeneratorException;
import at.jku.rdfstats.RDFStatsConfiguration;
import at.jku.rdfstats.RDFStatsDataset;
import at.jku.rdfstats.RDFStatsModel;
import at.jku.rdfstats.RDFStatsModelException;
import at.jku.rdfstats.RDFStatsModelFactory;
import at.jku.rdfstats.RDFStatsUpdatableModel;
import at.jku.rdfstats.RDFStatsUpdatableModelImpl;
import at.jku.rdfstats.hist.Histogram;
import at.jku.rdfstats.hist.RDF2JavaMapper;
import at.jku.rdfstats.hist.builder.HistogramBuilder;
import at.jku.rdfstats.hist.builder.HistogramBuilderException;
import at.jku.rdfstats.hist.builder.HistogramBuilderFactory;
import at.jku.rdfstats.hist.builder.HistogramCodec;
import at.jku.rdfstats.vocabulary.Stats;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.query.Syntax;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.engine.http.QueryExceptionHTTP;
import com.hp.hpl.jena.vocabulary.RDFS;

/**
 * Statistics generator base class
 * 
 * @author AndyL <al@jku.at>
 *
 */
public abstract class RDFStatsGeneratorBase {

    private static final Log log = LogFactory.getLog(RDFStatsGeneratorBase.class);

    /** model for the generated statistics */
    protected final RDFStatsUpdatableModel stats;

    /** temporary model for creating properties */
    protected final Model tempModel;

    /** the SCOVO dataset resource */
    protected RDFStatsDataset dataset;

    /** the URL of the RDF source (SPARQL endpoint URI or RDF document URL) */
    protected String sourceUrl;

    /** configuration */
    protected final RDFStatsConfiguration config;

    /** generator already used => invalid = true */
    protected boolean invalid = false;

    /** default constructor */
    public RDFStatsGeneratorBase() throws GeneratorException {
        this(null);
    }

    /** construct new generator with specified configuration */
    public RDFStatsGeneratorBase(RDFStatsConfiguration config) throws GeneratorException {
        if (config == null) {
            if (log.isInfoEnabled()) log.info("No configuration specified, using default configuration set.");
            this.config = RDFStatsConfiguration.getDefault();
        } else {
            this.config = config;
        }
        stats = RDFStatsModelFactory.createUpdatable(this.config.getStatsModel());
        tempModel = ModelFactory.createDefaultModel();
    }

    /** get or create the RDFStatsDataset depending on the generator and acquire the exclusive write lock for the {@link RDFStatsUpdatableModel} */
    public abstract RDFStatsDataset initDatasetAndLock() throws RDFStatsModelException;

    /** get query execution depending on the generator */
    public abstract QueryExecution getQueryExecution(Query cq);

    /**
	 * generate method
	 * 
	 * @throws GeneratorException
	 */
    public void generate() throws GeneratorException {
        if (invalid) throw new GeneratorException("This generator instance has been already used, create a new instance.");
        try {
            dataset = initDatasetAndLock();
            if (log.isInfoEnabled()) log.info("Generating statistics for " + dataset + "...");
            if (log.isDebugEnabled()) log.debug("Generating subject and property histograms...");
            boolean changed = generateSubjectHistograms();
            if (changed || !config.quickMode()) generatePropertyHistograms(); else {
                for (String prop : stats.getPropertyHistogramProperties(dataset.getSourceUrl())) for (String range : stats.getPropertyHistogramRanges(dataset.getSourceUrl(), prop)) stats.keepPropertyHistogram(dataset, prop, range);
            }
            stats.removeUnchangedItems(dataset);
            if (log.isInfoEnabled()) log.info("Statistics for " + dataset + " generated.");
        } catch (GeneratorException e) {
            throw new GeneratorException("Statistics generator failed.", e);
        } catch (Exception e) {
            throw new GeneratorException("Unexpected " + e.getClass().getSimpleName() + " during processing " + dataset + ".", e);
        } finally {
            try {
                stats.returnExclusiveWriteLock(dataset);
            } catch (RDFStatsModelException e) {
                log.error("Failed to return the exclusive lock.", e);
            }
            invalid = true;
        }
    }

    /**
	 * generate histograms over subjects (one for URI subjects and one for bnodes)
	 * returns boolean state value in order to make use of quickMode
	 * 
	 * @return true if subject histograms already existed and values changed
	 * @throws GeneratorException
	 * @throws RDFStatsModelException 
	 * @throws HistogramBuilderException 
	 */
    private boolean generateSubjectHistograms() throws GeneratorException, HistogramBuilderException, RDFStatsModelException {
        String qry;
        log.info("Generating subject histograms...");
        qry = "SELECT DISTINCT ?s WHERE { ?s ?p ?o }";
        Query q = QueryFactory.create(qry);
        HistogramBuilder<?> histBuilderURI = null;
        histBuilderURI = HistogramBuilderFactory.createBuilder(RDFS.Resource.getURI(), null, config.getPrefSize(), config);
        HistogramBuilder<?> histBuilderBNode = null;
        histBuilderBNode = HistogramBuilderFactory.createBuilder(Stats.blankNode.getURI(), null, config.getPrefSize(), config);
        Node sbj = null;
        QueryExecution qe = null;
        try {
            qe = getQueryExecution(q);
            ResultSet r = qe.execSelect();
            while (r.hasNext()) {
                sbj = r.nextSolution().get("s").asNode();
                if (sbj.isURI()) histBuilderURI.addNodeValue(sbj); else if (sbj.isBlank()) histBuilderBNode.addNodeValue(sbj);
            }
        } catch (Exception e) {
            log.error("Error adding subject <" + sbj + "> to histogram builder, value skipped.", e);
        } finally {
            if (qe != null) qe.close();
        }
        if (log.isDebugEnabled()) log.debug("Generated subject histogram.");
        Histogram<?> shURI = histBuilderURI.getHistogram();
        Histogram<?> shBNode = histBuilderBNode.getHistogram();
        String encodedURI = HistogramCodec.base64encode(shURI);
        String beforeURI = stats.getSubjectHistogramEncoded(dataset.getSourceUrl(), false);
        String encodedBNode = HistogramCodec.base64encode(shBNode);
        String beforeBNode = stats.getSubjectHistogramEncoded(dataset.getSourceUrl(), true);
        if (config.quickMode() && beforeURI != null && encodedURI.equals(beforeURI) && beforeBNode != null && encodedBNode.equals(beforeBNode)) {
            stats.keepSubjectHistogram(dataset, false);
            stats.keepSubjectHistogram(dataset, true);
            return false;
        } else {
            stats.addOrUpdateSubjectHistogram(dataset, false, encodedURI);
            stats.addOrUpdateSubjectHistogram(dataset, true, encodedBNode);
            return true;
        }
    }

    /**
	 * @throws GeneratorException
	 */
    private void generatePropertyHistograms() throws GeneratorException {
        List<String> properties;
        try {
            properties = getProperties();
        } catch (Exception e) {
            log.error("Error obtaining list of properties.", e);
            return;
        }
        if (log.isDebugEnabled()) {
            log.debug("Generating property histograms over all properties...");
        }
        for (String p : properties) {
            try {
                generatePropertyHistograms(p);
            } catch (HistogramBuilderException e) {
                String part = "property <" + p + "> ";
                log.error("Couldn't generate histograms for " + part + "because of a problem with the histogram builder, skipping...", e);
            } catch (Exception e) {
                String part = "property <" + p + "> ";
                log.error("Couldn't generate histograms for " + part + ", skipping...", e);
            }
            if (Constants.WAIT_BETWEEN_QUERIES > 0) try {
                Thread.sleep(Constants.WAIT_BETWEEN_QUERIES);
            } catch (InterruptedException ignore) {
            }
        }
        if (log.isInfoEnabled()) {
            log.info(properties.size() + " properties processed.");
        }
    }

    /**
	 * Example:
	 * 
	 * :h1213		a	stats:Histogram ;
	 *	scv:dataset	:local8888 ;
	 *	scv:dimension [
	 *		a stats:PropertyDimension ;
	 *		stats:property	<http://example.org/p1> ] ;
	 *	scv:dimension [
	 *		a stats:RangeDimension ;
	 *		stats:range		<xsd:string> ] ;
	 *	rdf:value "ENCODED HISTOGRAM h1213" .
	 * 
	 * @param p
	 * 
	 * @throws QueryExceptionHTTP
	 * @throws RDFStatsModelException 
	 */
    private void generatePropertyHistograms(String p) throws QueryExceptionHTTP, HistogramBuilderException, RDFStatsModelException {
        String qry;
        log.info("Generating property histograms for <" + p + ">...");
        qry = "SELECT ?val WHERE { ?s <" + p + "> ?val }";
        Map<String, HistogramBuilder<?>> histBuilders = new HashMap<String, HistogramBuilder<?>>();
        Query q = QueryFactory.create(qry);
        long records = 0;
        QueryExecution qe = null;
        try {
            qe = getQueryExecution(q);
            ResultSet r = qe.execSelect();
            QuerySolution s = null;
            Node val = null;
            String type = null;
            HistogramBuilder<?> histBuilder;
            while (r.hasNext()) {
                try {
                    s = r.nextSolution();
                    val = s.get("val").asNode();
                    records++;
                    type = RDF2JavaMapper.getType(val);
                    if (histBuilders.containsKey(type)) {
                        histBuilder = histBuilders.get(type);
                    } else {
                        histBuilder = HistogramBuilderFactory.createBuilder(type, p, config.getPrefSize(), config);
                        histBuilders.put(type, histBuilder);
                    }
                    histBuilder.addNodeValue(val);
                } catch (Exception e) {
                    log.error("Error adding value '" + val + "' (type: " + type + ") of property <" + p + "> to the histogram builder, value skipped.", e);
                    continue;
                }
            }
        } finally {
            if (qe != null) qe.close();
        }
        for (String t : histBuilders.keySet()) {
            String encoded = HistogramCodec.base64encode(histBuilders.get(t).getHistogram());
            stats.addOrUpdatePropertyHistogram(dataset, p, t, encoded);
        }
        int n = histBuilders.size();
        if (log.isDebugEnabled()) if (n == 1) log.debug("Generated histogram for property <" + p + ">. " + records + " object values have been analyzed."); else log.debug("Generated " + n + " histograms for different ranges of property <" + p + ">. " + records + " property values have been analyzed.");
        histBuilders.clear();
    }

    /**
	 * Fetches the distinct set of properties
	 * 
	 * @return
	 * @throws QueryExceptionHTTP
	 */
    private List<String> getProperties() throws QueryExceptionHTTP {
        String pQry;
        if (log.isInfoEnabled()) log.info("Fetching distinct set of properties...");
        pQry = "SELECT DISTINCT ?prop WHERE { [] ?prop ?o }";
        List<String> properties = new ArrayList<String>();
        Query cq = QueryFactory.create(pQry, Syntax.syntaxARQ);
        QueryExecution qe = null;
        try {
            qe = getQueryExecution(cq);
            ResultSet r = qe.execSelect();
            QuerySolution s;
            Resource re;
            while (r.hasNext()) {
                s = r.nextSolution();
                if (s.get("prop").isURIResource()) {
                    re = s.getResource("prop");
                    properties.add(re.getURI());
                } else log.error("Invalid property '" + s.get("prop") + "' ingnored (should be an URI resource).");
            }
        } finally {
            if (qe != null) qe.close();
        }
        if (log.isDebugEnabled()) {
            log.debug("Fetched distinct set of properties (" + properties.size() + " total).");
        }
        return properties;
    }

    public RDFStatsModel getRDFStatsModel() {
        return RDFStatsModelFactory.create(config.getStatsModel());
    }

    public void printRetrievalDetails() {
        if (!log.isDebugEnabled()) return;
    }
}
