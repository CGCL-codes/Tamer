package edu.mit.lcs.haystack.server.extensions.query;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Vector;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.StopAnalyzer;
import edu.mit.lcs.haystack.lucene.document.Document;
import edu.mit.lcs.haystack.lucene.index.ForwardIndexWriter;
import edu.mit.lcs.haystack.lucene.index.FrequencyMap;
import edu.mit.lcs.haystack.lucene.index.IndexReader;
import edu.mit.lcs.haystack.lucene.index.IndexWriter;
import edu.mit.lcs.haystack.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import edu.mit.lcs.haystack.Constants;
import edu.mit.lcs.haystack.content.ContentClient;
import edu.mit.lcs.haystack.ozone.core.Context;
import edu.mit.lcs.haystack.proxy.IServiceAccessor;
import edu.mit.lcs.haystack.rdf.IRDFContainer;
import edu.mit.lcs.haystack.rdf.Literal;
import edu.mit.lcs.haystack.rdf.RDFException;
import edu.mit.lcs.haystack.rdf.RDFNode;
import edu.mit.lcs.haystack.rdf.Resource;
import edu.mit.lcs.haystack.rdf.Statement;
import edu.mit.lcs.haystack.rdf.Utilities;
import edu.mit.lcs.haystack.security.Identity;
import edu.mit.lcs.haystack.server.core.service.IService;
import edu.mit.lcs.haystack.server.core.service.ServiceException;
import edu.mit.lcs.haystack.server.core.service.ServiceManager;
import edu.mit.lcs.haystack.server.extensions.navigation.NavigationConstants;
import edu.mit.lcs.haystack.server.standard.scheduler.IScheduledTask;

/**
 * @author yks
 */
public class LuceneAgent implements ILuceneAgent, IService, IScheduledTask, ITextContainerListener {

    String m_datafile;

    ServiceManager m_sm;

    Resource m_resService;

    String m_dataFilePath;

    boolean m_regenerating = false;

    static org.apache.log4j.Logger s_logger = org.apache.log4j.Logger.getLogger(LuceneAgent.class);

    public static final String s_lucene_namespace = "http://haystack.lcs.mit.edu/agents/lucene#";

    public static final Resource s_lucene_lastModified = new Resource(s_lucene_namespace + "lastModified");

    public static final Resource s_lucene_LuceneAgent = new Resource(s_lucene_namespace + "LuceneAgent");

    public static final Resource s_lucene_uri = new Resource(s_lucene_namespace + "uri");

    public static final Resource s_lucene_content = new Resource(s_lucene_namespace + "content");

    public static final Resource s_lucene_globalDocText = new Resource(s_lucene_namespace + "globalDocText");

    public static final String s_str_lucene_uri = s_lucene_uri.toString();

    public static final String s_str_lucene_content = s_lucene_content.toString();

    public static final String s_str_lucene_globalDocText = s_lucene_globalDocText.toString();

    public Resource getServiceResource() {
        return m_resService;
    }

    /**
     * @see IService#cleanup()
     */
    public void cleanup() throws ServiceException {
    }

    /**
     * @see IService#init(String, ServiceManager, Resource)
     */
    public void init(String basePath, ServiceManager manager, Resource resService) throws ServiceException {
        m_sm = manager;
        m_resService = resService;
        m_dataFilePath = basePath + "lucene";
        File file = new File(m_dataFilePath);
        try {
            file.mkdirs();
        } catch (Exception e) {
            s_logger.error("Could not create directory " + m_dataFilePath, e);
        }
    }

    /**
     * @see IService#shutdown()
     */
    public void shutdown() throws ServiceException {
    }

    /**
     * @see IScheduledTask#performScheduledTask(Resource)
     */
    public void performScheduledTask(Resource resTask) throws ServiceException {
        regenerateIndices();
    }

    /**
     * Performs a query.
     * 
     * @return An array of resources satisfying this query.
     */
    public Set query(String queryString) {
        return query(s_str_lucene_globalDocText, queryString);
    }

    public Set query(String field, String queryString) {
        try {
            return queryInternal(field, queryString);
        } catch (Exception e) {
            regenerateIndices();
            try {
                return queryInternal(field, queryString);
            } catch (Exception e2) {
                s_logger.warn("Query failed: " + field + ":" + queryString, e2);
                return new HashSet();
            }
        }
    }

    public Set queryInternal(String field, String queryString) throws Exception {
        Searcher searcher = new IndexSearcher(m_dataFilePath);
        Analyzer analyzer = new StopAnalyzer();
        QueryParser parser = new QueryParser(field, analyzer);
        parser.setOperator(QueryParser.DEFAULT_OPERATOR_AND);
        Query query = parser.parse(queryString);
        Hits hits = searcher.search(query);
        Set results = new HashSet();
        for (int i = 0; i < hits.length(); i++) {
            results.add(RDFTerm.getRDFNode(hits.doc(i).get(s_str_lucene_uri)));
        }
        return results;
    }

    /**
     * Only available for testing purposes
     */
    public String getDataFilePath() {
        return m_dataFilePath;
    }

    public void setDataFilePath(String newDataFilePath) {
        m_dataFilePath = newDataFilePath;
    }

    protected IndexReader openReader() {
        try {
            return IndexReader.open(m_dataFilePath);
        } catch (FileNotFoundException e) {
            s_logger.info("Index is empty");
            addDocuments(new Vector());
            return openReader();
        } catch (IOException e) {
            s_logger.error("Unexpected exception opening reader", e);
            return null;
        }
    }

    protected void closeReader(IndexReader rdr) {
        try {
            ((IndexReader) rdr).close();
        } catch (IOException e) {
        }
    }

    /**
     * @author yks
     *
     * This class wraps around an index wrapper instance
     * having this wrapper provides an abstracted "indexReader" handle
     * for code that does multiple "forwardQueries" on an index.
     */
    private class IndexReaderWrapper {

        IndexReader reader = null;

        public IndexReaderWrapper() {
            open();
        }

        public IndexReader getReader() {
            return reader;
        }

        public void reopen() {
            closeReader(reader);
            reader = openReader();
        }

        public void open() {
            reader = openReader();
        }

        public void close() {
            closeReader(reader);
        }
    }

    /**
     * opens a reader handle [Object], which the user can use to query 
     * by calling multiQueryForwardExec.
     * @return
     */
    public Object multiQueryForwardBegin() {
        return new IndexReaderWrapper();
    }

    /**
     * closes the indexReader handle [Object] opened by multiQueryForwardBeg
     */
    public void multiQueryForwardEnd(Object indexHandle) {
        ((IndexReaderWrapper) indexHandle).close();
    }

    /**
     * Called when a query fails during multiQueryForwardExec because either
     * the document is not in the index or some illegal argument exception occurs.
     * This method will try to reindex the given uri, and retries the query.
     * @param indexHandle
     * @param uri
     * @return the frequency map of the given uri
     */
    protected FrequencyMap multiQueryForwardTryJITAndExec(Object indexHandle, Resource uri) {
        IndexReaderWrapper readerWrapper = (IndexReaderWrapper) indexHandle;
        try {
            Vector v = new Vector();
            v.add(uri);
            addDocuments(v);
            readerWrapper.reopen();
            Document doc = readerWrapper.getReader().document(RDFTerm.getLuceneStr(uri));
            if (doc != null) {
                return doc.getFrequencyMap();
            } else {
                s_logger.warn("Got null on trying indexing document and retrieving");
            }
        } catch (Exception e) {
            s_logger.warn("Exception on trying indexing and retrieving", e);
        }
        return new FrequencyMap(null, null);
    }

    /**
     * Given a index handle, and a uri, looks up the FrequencyMap associated
     * with that uri.
     * Index handle is opened by multiQueryForwardOpen, and closed by multiQueryForwardClose
     * @param reader
     * @param uri
     * @return the frequency map of the given uri
     */
    public FrequencyMap multiQueryForwardExec(Object indexHandle, Resource uri) {
        IndexReader reader = ((IndexReaderWrapper) indexHandle).getReader();
        synchronized (this) {
            try {
                Document doc = reader.document(RDFTerm.getLuceneStr(uri));
                if (doc == null) {
                    throw new FileNotFoundException();
                }
                return doc.getFrequencyMap();
            } catch (Exception e) {
                if (e instanceof IllegalArgumentException || e instanceof FileNotFoundException) {
                    s_logger.warn("Exception while trying to query: " + uri + "...retrying", e);
                    return multiQueryForwardTryJITAndExec(indexHandle, uri);
                } else {
                    s_logger.error("Unhandled exception while trying to query: " + uri, e);
                }
            }
        }
        return new FrequencyMap(null, null);
    }

    /**
     * Performs a forward query.  Given a resource instance, does
     * a search on the current forward search index.  If the given
     * resource is not found, it is added to the index.
     * 
     * @return the term FrequencyMap corresponding to this resource.
     */
    public FrequencyMap queryForward(Resource uri) {
        IndexReader reader = null;
        synchronized (this) {
            try {
                reader = openReader();
                Document doc = reader.document(RDFTerm.getLuceneStr(uri));
                if (doc == null) {
                    throw new FileNotFoundException();
                }
                return doc.getFrequencyMap();
            } catch (Exception e) {
                if (e instanceof FileNotFoundException || e instanceof IllegalArgumentException) {
                    s_logger.info("File is not found: " + uri + " ...adding it to the index and retrying.");
                    Vector v = new Vector();
                    v.add(uri);
                    addDocuments(v);
                    try {
                        closeReader(reader);
                        reader = openReader();
                        Document doc = reader.document(RDFTerm.getLuceneStr(uri));
                        return doc.getFrequencyMap();
                    } catch (Exception e2) {
                        s_logger.warn("Query after reindexing failed: " + uri, e2);
                        return null;
                    }
                } else {
                    s_logger.error("Unhandled exception on doc: " + uri, e);
                    return null;
                }
            } finally {
                try {
                    closeReader(reader);
                } catch (Exception e) {
                    s_logger.error("Exception on closing index reader");
                }
            }
        }
    }

    /**
     * All lucene indexable objects has a last modified date and last "cached" modified date or
     * (date last mod from the search index). This method checks the last modified date with the last
     * mod index date and determines if the given Resource instance needs to be
     * reindexed.
     * SIDE EFFECT: updates the cached-last-modified-date if it is found that the last-modified-date
     * is more recent than the cached-last-modified-date
     * 
     * @param checkRes
     *            the resource being checked
     * @return true if resource has been out-dated and needs to be indexed false
     *         otherwise
     */
    protected boolean checkForIndexing(Resource checkRes) {
        s_logger.info("checkForIndex " + checkRes);
        IRDFContainer rootRDFContainer = m_sm.getRootRDFContainer();
        ContentClient cc = ContentClient.getContentClient(checkRes, rootRDFContainer, m_sm);
        Date dateCachedLastModified = null;
        Date dateCurrentLastModified = null;
        String strCachedLastModified = Utilities.getLiteralProperty(checkRes, s_lucene_lastModified, rootRDFContainer);
        if (strCachedLastModified != null) {
            if (strCachedLastModified.equals("0")) {
                s_logger.warn("Resource: " + checkRes.getURI() + " not being indexed properly.");
                return false;
            }
            try {
                dateCachedLastModified = new Date(Long.parseLong(strCachedLastModified));
            } catch (NumberFormatException e) {
            }
        }
        try {
            dateCurrentLastModified = cc.getDate();
        } catch (Exception e) {
            s_logger.error("An error occurred trying to get date on " + checkRes, e);
        }
        if ((dateCachedLastModified != null && dateCurrentLastModified != null) && (dateCachedLastModified.getTime() >= dateCurrentLastModified.getTime())) {
            return false;
        }
        if (dateCurrentLastModified != null) {
            try {
                rootRDFContainer.replace(checkRes, s_lucene_lastModified, null, new Literal(Long.toString(dateCurrentLastModified.getTime())));
            } catch (RDFException e) {
                s_logger.error("An error occurred while trying to update modification time for " + checkRes, e);
            }
        }
        return true;
    }

    /**
     * given a list of Resource instances, this method deletes these resources
     * from the underlying lucene index
     * 
     * @param list
     */
    public void removeDocuments(Vector list) {
        if (!IndexReader.indexExists(m_dataFilePath)) {
            return;
        }
        synchronized (this) {
            IndexReader reader = null;
            try {
                reader = IndexReader.open(m_dataFilePath);
                Iterator it = list.iterator();
                while (it.hasNext()) {
                    String uri = ((Resource) it.next()).getURI();
                    Term term = new Term(s_str_lucene_uri, uri);
                    reader.delete(term);
                }
            } catch (IOException e) {
                s_logger.error("An unexpected error occurred trying to remove documents from index", e);
            } finally {
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * Adds a list of resources to be added to a Lucene Forward index
     * (ForwardIndexWriter) for each listed resource, this method, performs a
     * "visit" of its surrounding nodes, adding any literal to the document as
     * lucene fields.
     * 
     * @param list
     *            a list of Resource instances
     */
    public void addDocuments(Vector list) {
        synchronized (this) {
            IndexWriter writer = null;
            try {
                writer = new ForwardIndexWriter(s_str_lucene_uri, m_dataFilePath, new StopAnalyzer(), !IndexReader.indexExists(m_dataFilePath));
                writer.mergeFactor = 20;
                Iterator it = list.iterator();
                int cnt = 0;
                double pct = 0;
                int max = list.size();
                int bucket = max / 10;
                while (it.hasNext()) {
                    Resource resDoc = (Resource) it.next();
                    Document luceneDoc = new Document();
                    try {
                        DocumentAcceptor.visitDocument(new LuceneDocumentVisitor(this, luceneDoc), resDoc);
                    } catch (Exception e1) {
                        s_logger.error("Error while trying to index document: " + resDoc, e1);
                    }
                    writer.addDocument(luceneDoc);
                    if (max > 100 && cnt % bucket == 0) {
                        pct = Math.rint(((double) cnt / (double) max) * 1000) / 10;
                        s_logger.info("Done: " + pct + "% [" + (cnt + 1) + "/" + (max + 1) + "]");
                    }
                    cnt++;
                }
                pct = Math.rint(((double) cnt / (double) max) * 1000) / 10;
                s_logger.info("Done: " + pct + "% [" + (cnt + 1) + "/" + (max + 1) + "]");
                writer.optimize();
            } catch (IOException e) {
                s_logger.error("An error occurred trying to add documents to index ", e);
            } finally {
                try {
                    writer.close();
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * Regenerates the index that is used for performing search on queries.
     * 
     * Updates the index that is used for performing search on queries.
     * 
     * @param checkForFreshness if set, then only changed (expired last mod date)
     * documents are reindexed
     *
     */
    public void indicesUpdate(boolean checkForFreshness) {
        synchronized (this) {
            if (m_regenerating) {
                return;
            }
            m_regenerating = true;
        }
        Vector refreshResources = new Vector();
        IRDFContainer rdfc = m_sm.getRootRDFContainer();
        try {
            IRDFContainer rootRDFContainer = m_sm.getRootRDFContainer();
            Set docs = rootRDFContainer.query(new Statement[] { new Statement(Utilities.generateWildcardResource(1), Constants.s_rdf_type, Utilities.generateWildcardResource(2)), new Statement(Utilities.generateWildcardResource(2), NavigationConstants.s_nav_enableCaching, new Literal("true")) }, Utilities.generateWildcardResourceArray(2), new Resource[] { Utilities.generateWildcardResource(1) });
            Iterator docIt = docs.iterator();
            while (docIt.hasNext()) {
                RDFNode[] statement = (RDFNode[]) docIt.next();
                Resource resDoc = (Resource) statement[0];
                if (checkForFreshness && checkForIndexing(resDoc)) {
                    refreshResources.add(resDoc);
                }
            }
            removeDocuments(refreshResources);
            addDocuments(refreshResources);
        } catch (RDFException e) {
            s_logger.error("An error occurred trying to add documents to index ", e);
        } catch (Error e) {
            s_logger.error("An unexpected error occurred trying to add documents to index ", e);
        }
        synchronized (this) {
            m_regenerating = false;
        }
    }

    /**
     * Regenerates the index that is used for performing search on queries.
     * we do not checkForIndexing here
     * referenced in adenine.
     */
    public void regenerateIndices() {
        indicesUpdate(false);
    }

    /**
     * Reindexes documents (resources) that have been changed since the last
     * indexing cycle.
     */
    public void incrementalIndicesUpdate() {
        indicesUpdate(true);
    }

    public void textAdded(Resource res, Resource relationship, String text) {
        synchronized (this) {
        }
    }

    public void textRemoved(Resource res, Resource relationship, String text) {
    }

    /**
     * static method for obtaining a single instance of the lucene agent, this
     * method returns the first found instance (which may not be deterministic).
     * To get a list of all available LuceneAgents, use getLuceneAgents();
     * 
     * @param rdfc
     * @param context
     * @return
     */
    public static LuceneAgent getLuceneAgent(IRDFContainer rdfc, Context context) {
        try {
            Set agents = LuceneAgent.getLuceneAgents(rdfc, context);
            if (agents.isEmpty()) {
                s_logger.error("No lucene agent found!");
                return null;
            } else {
                return (LuceneAgent) agents.iterator().next();
            }
        } catch (Exception e) {
            s_logger.error("Error while trying to connect to service", e);
            return null;
        }
    }

    /**
     * static method for obtaining an list of LuceneAgent instances from the
     * given rdfc container. It uses IRDFContainer's query method to find all
     * instances, There is no guarantees on what order the agents will be
     * returned
     * 
     * @param rdfc
     * @param context
     * @return
     */
    public static Set getLuceneAgents(IRDFContainer rdfc, Context context) {
        try {
            Set agents = rdfc.query(new Statement[] { new Statement(Utilities.generateWildcardResource(1), Constants.s_rdf_type, LuceneAgent.s_lucene_LuceneAgent) }, Utilities.generateWildcardResourceArray(1), new Resource[] { Utilities.generateWildcardResource(1) });
            IServiceAccessor serviceAccessor = context.getServiceAccessor();
            Identity identity = context.getUserIdentity();
            LinkedHashSet result = new LinkedHashSet();
            Iterator it = agents.iterator();
            while (it.hasNext()) {
                RDFNode[] nodes = (RDFNode[]) it.next();
                Resource agentRes = (Resource) nodes[0];
                result.add(serviceAccessor.connectToService(agentRes, identity));
            }
            return result;
        } catch (Exception e) {
            s_logger.error("Error while trying to connect to service", e);
            return null;
        }
    }
}
