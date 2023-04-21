package org.roosster.store;

import java.net.URL;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.security.*;
import java.util.logging.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Hits;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.index.IndexWriter;
import org.roosster.*;

/**
 * TODO better synchronizing
 *
 * @author <a href="mailto:benjamin@roosster.org">Benjamin Reitzammer</a>
 * @version $Id: EntryStore.java,v 1.1 2004/12/03 14:30:15 firstbman Exp $
 */
public class EntryStore implements Plugin {

    private static Logger LOG = Logger.getLogger(EntryStore.class.getName());

    /**
     */
    public static final String PROP_INDEXDIR = "store.indexdir";

    public static final String PROP_ANALYZER = "store.analyzerclass";

    public static final String PROP_CREATEIND = "store.createindex";

    private static final String URLHASH = "urlhash";

    private static String indexDir = null;

    private static Class analyzerClass = null;

    private static String createIndexProp = null;

    private static boolean initialized = false;

    /**
     */
    public EntryStore() {
    }

    /**
     *
     */
    public void init(Registry registry) throws InitializeException {
        Configuration conf = registry.getConfiguration();
        LOG.config("Initializing Plugin " + getClass());
        String className = null;
        try {
            className = conf.getProperty(PROP_ANALYZER);
            if (className == null) throw new InitializeException("No '" + PROP_ANALYZER + "'-argument provided");
            LOG.fine("Trying to load analyzer-class: " + className);
            analyzerClass = Class.forName(className);
            Analyzer testInstance = (Analyzer) analyzerClass.newInstance();
        } catch (ClassCastException ex) {
            throw new InitializeException("Specified class is not an instance of " + Analyzer.class + ": " + className);
        } catch (ClassNotFoundException ex) {
            throw new InitializeException("Can't load analyzer-class: " + className);
        } catch (Exception ex) {
            throw new InitializeException("Exception occured during database init", ex);
        }
        indexDir = conf.getProperty(PROP_INDEXDIR);
        if (indexDir == null) throw new InitializeException("No '" + PROP_INDEXDIR + "'-argument provided");
        LOG.fine("Directory of index is: " + indexDir);
        LOG.config("Finished initialize of " + getClass());
        createIndexProp = conf.getProperty(PROP_CREATEIND);
        initialized = true;
    }

    /**
     *
     */
    public void shutdown(Registry registry) throws Exception {
        initialized = false;
    }

    /**
     *
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
    * @return an array of {@link Entry Entry}-objects that's never <code>null</code>
     */
    public Entry[] search(String queryStr) throws IOException, ParseException {
        QueryParser parser = new QueryParser(Entry.ALL, createAnalyzer());
        Query query = parser.parse(queryStr);
        LOG.config("Executing Query: " + query);
        IndexSearcher searcher = new IndexSearcher(indexDir);
        Hits hits = searcher.search(query);
        int hitsNum = hits.length();
        LOG.info("Found " + hitsNum + " matches for query: <" + query + ">");
        List entries = new ArrayList();
        for (int i = 0; i < hitsNum; i++) {
            entries.add(new Entry(hits.doc(i)));
        }
        return (Entry[]) entries.toArray(new Entry[0]);
    }

    /**
     * @return <code>null</code> if there is no entry with the
     * specified URL
     */
    public Entry getEntry(URL url) throws IOException {
        Entry[] entries = getEntries(url, null);
        if (entries.length > 1) LOG.warning("More than one Entry found for URL " + url);
        return entries.length > 0 ? entries[0] : null;
    }

    /**
     *
     */
    public void addEntry(Entry entry) throws IOException {
        addEntry(entry, false);
    }

    /**
     *
     */
    public void addEntry(Entry entry, boolean force) throws IOException {
        addEntries(new Entry[] { entry }, force);
    }

    /**
     *
     */
    public void addEntries(Entry[] entries) throws IOException {
        addEntries(entries, false);
    }

    /**
     * @exception DuplicateEntryException
     */
    public void addEntries(Entry[] entries, boolean force) throws IOException {
        if (entries != null) {
            if (!force && IndexReader.indexExists(indexDir)) {
                IndexReader reader = null;
                try {
                    reader = getReader();
                    for (int i = 0; i < entries.length; i++) {
                        Entry[] stored = getEntries(entries[i].getUrl(), reader);
                        if (stored.length > 0) throw new DuplicateEntryException(entries[i].getUrl());
                    }
                } finally {
                    if (reader != null) reader.close();
                }
            }
            storeEntries(entries);
        }
    }

    /**
     *
     */
    public int deleteEntry(URL url) throws IOException {
        return deleteEntry(url, null);
    }

    /**
     *
     */
    protected Entry[] getEntries(URL url, IndexReader reader) throws IOException {
        if (!isInitialized()) throw new IllegalStateException("Database must be initialized before use!");
        if (url == null) throw new IllegalArgumentException("Parameter 'url' is not allowed to be null");
        boolean closeReader = false;
        TermDocs docs = null;
        try {
            LOG.fine("Getting Entry with URL: " + url);
            if (reader == null) {
                reader = getReader();
                closeReader = true;
            }
            Term term = new Term(URLHASH, computeHash(url.toString()));
            docs = reader.termDocs(term);
            List entries = new ArrayList();
            while (docs.next()) {
                entries.add(new Entry(reader.document(docs.doc())));
            }
            LOG.fine("Found " + entries.size() + " entries for URL " + url);
            return (Entry[]) entries.toArray(new Entry[0]);
        } finally {
            if (closeReader && reader != null) reader.close();
            if (docs != null) docs.close();
        }
    }

    /**
     *
     */
    protected int deleteEntry(URL url, IndexReader reader) throws IOException {
        if (!isInitialized()) throw new IllegalStateException("Database must be initialized before use!");
        if (url == null) throw new IllegalArgumentException("Parameter 'url' is not allowed to be null");
        boolean closeReader = false;
        try {
            LOG.fine("Deleting Entry with URL: " + url.toString());
            if (reader == null) {
                reader = getReader();
                closeReader = true;
            }
            Term term = new Term(URLHASH, computeHash(url.toString()));
            int numDeleted = reader.delete(term);
            LOG.info("Deleted " + numDeleted + " Entries");
            return numDeleted;
        } finally {
            if (closeReader && reader != null) reader.close();
        }
    }

    /**
     * @param entry the <code>Entry</code>-object that should be added to the store,
     * if this is null, no action will be taken.
     * @exception IOException if the writing to the index fails due to some I/O reason
     * @exception IllegalStateException if an instance of this class was not properly
     * initialized before calling this method
     */
    private synchronized void storeEntries(Entry[] entries) throws IOException {
        if (!isInitialized()) throw new IllegalStateException("Database must be initialized before use!");
        if (entries == null) throw new IllegalArgumentException("Parameter 'entries' is not allowed to be null");
        IndexWriter writer = null;
        IndexReader reader = null;
        try {
            if (IndexReader.indexExists(indexDir)) {
                reader = getReader();
                for (int i = 0; i < entries.length; i++) {
                    deleteEntry(entries[i].getUrl(), reader);
                }
                reader.close();
                reader = null;
            }
            writer = getWriter();
            for (int i = 0; i < entries.length; i++) {
                LOG.fine("Adding Entry to index: " + entries[i].getUrl().toString());
                Document doc = entries[i].getDocument();
                doc.add(Field.Keyword(URLHASH, computeHash(entries[i].getUrl().toString())));
                writer.addDocument(doc);
            }
            writer.optimize();
        } finally {
            if (writer != null) writer.close();
            if (reader != null) reader.close();
        }
    }

    /**
     *
     */
    private IndexWriter getWriter() throws IOException {
        boolean createIndex = false;
        if (createIndexProp == null) {
            createIndex = IndexReader.indexExists(indexDir) ? false : true;
        } else if ("1".equals(createIndexProp.trim()) || "true".equalsIgnoreCase(createIndexProp.trim())) {
            createIndex = true;
        }
        IndexWriter writer = new IndexWriter(indexDir, createAnalyzer(), createIndex);
        writer.maxFieldLength = 1000000;
        return writer;
    }

    /**
     *
     */
    private IndexReader getReader() throws IOException {
        return IndexReader.open(indexDir);
    }

    /**
     *
     */
    private Analyzer createAnalyzer() throws IllegalStateException {
        String exceptionMsg = null;
        Analyzer analyzer = null;
        try {
            analyzer = (Analyzer) analyzerClass.newInstance();
        } catch (InstantiationException ex) {
            exceptionMsg = "The provided Analyzer-class could not be instantiated: " + ex.getMessage();
        } catch (ExceptionInInitializerError ex) {
            exceptionMsg = "The provided Analyzer-class could not be instantiated: " + ex.getMessage();
        } catch (SecurityException ex) {
            exceptionMsg = "The provided Analyzer-class could not be instantiated: " + ex.getMessage();
        } catch (IllegalAccessException ex) {
            exceptionMsg = "The provided Analyzer-class could not be instantiated: " + ex.getMessage();
        } finally {
            if (exceptionMsg != null) throw new IllegalStateException(exceptionMsg);
        }
        return analyzer;
    }

    /**
     *
     */
    private String computeHash(String str) {
        String algorithm = "MD5";
        try {
            MessageDigest md5 = MessageDigest.getInstance(algorithm);
            byte[] end = md5.digest(str.getBytes());
            StringBuffer endString = new StringBuffer();
            for (int i = 0; i < end.length; i++) {
                int tmp = end[i] & 0xFF;
                endString.append(tmp < 16 ? "0" : "").append(Integer.toHexString(tmp));
            }
            return endString.toString();
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("FATAL: Your system does not support '" + algorithm + "' hashing");
        }
    }
}
