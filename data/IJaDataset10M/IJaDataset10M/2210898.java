package alx.library;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.ru.RussianAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;

/**
 * The purpose of this class is to control reader and writer usage in
 * application, in particular to ensure that there are no multiple instances.
 * 
 */
public class IndexResources {

    private static final Logger log = Logger.getLogger(IndexResources.class.getName());

    private static IndexWriter writer;

    private static final int MAX_FIELD_LENGTH = 10000000;

    private static Analyzer analyzer = null;

    private static int documentsBefore = 0;

    private static IndexReader reader;

    private static ReadOnlySettings settings;

    public static final Version VERSION = Version.LUCENE_30;

    public static void setSettings(ReadOnlySettings _settings) {
        settings = _settings;
    }

    private IndexResources() {
        throw new UnsupportedOperationException();
    }

    public static synchronized IndexWriter getWriter() throws CorruptIndexException, LockObtainFailedException, IOException {
        if (writer != null) {
            log.warning("Writer session already started");
        } else {
            log.info("Starting new writer session");
            writer = new IndexWriter(settings.getIndexDir(), getAnalyzer(), IndexWriter.MaxFieldLength.UNLIMITED);
            writer.setMaxFieldLength(MAX_FIELD_LENGTH);
            log.fine("Writer session started");
        }
        documentsBefore = getDocsNumber(getReader());
        return writer;
    }

    public static synchronized void releaseWriter() {
        releaseWriter(true, true);
    }

    /**
	 * 
	 * @param commit
	 * @param optimize
	 */
    public static synchronized void releaseWriter(boolean commit, boolean optimize) {
        if (writer == null) {
            log.warning("Writer session already stopped");
            return;
        }
        log.info("Stopping writer session");
        try {
            if (commit) {
                log.finer("Committing...");
                writer.commit();
            }
            if (optimize) {
                log.finer("Optimizing...");
                writer.optimize();
            }
            writer.close();
        } catch (IOException e) {
            log.log(Level.WARNING, "Failed to close writer", e);
        } finally {
            writer = null;
        }
        log.info("Writer session stopped");
        int indexSizeChange = getDocsNumber() - documentsBefore;
        log.info("Index size change: " + indexSizeChange);
        documentsBefore = 0;
    }

    public static synchronized Analyzer getAnalyzer() {
        if (analyzer == null) {
            switch(settings.getLang()) {
                case RUSSIAN:
                    analyzer = new RussianAnalyzer(VERSION);
                    break;
                case ENGLISH:
                    analyzer = new StandardAnalyzer(VERSION);
                    break;
            }
        }
        return analyzer;
    }

    /**
	 * 
	 * @return Read-only reader, either new or reopened, i.e. reflecting all the
	 *         recent changes in the index.
	 * @throws CorruptIndexException
	 * @throws IOException
	 */
    public static synchronized IndexReader getReader() throws CorruptIndexException, IOException {
        return getReader(true);
    }

    /**
	 * 
	 * @param readOnly
	 * @return New or reopened reader, never old.
	 * @throws CorruptIndexException
	 * @throws IOException
	 */
    public static synchronized IndexReader getReader(boolean readOnly) throws CorruptIndexException, IOException {
        if (reader == null) {
            log.finer("Opening new reader in " + (readOnly ? "read only" : "read/write") + " mode");
            reader = IndexReader.open(settings.getIndexDir(), readOnly);
        } else {
            IndexReader newReader = reader.reopen();
            if (newReader != reader) {
                reader.close();
            }
            reader = newReader;
        }
        return reader;
    }

    public static synchronized IndexReader getNewReader() throws CorruptIndexException, IOException {
        releaseReader();
        return getReader();
    }

    public static synchronized IndexReader getNewReadWriteReader() throws CorruptIndexException, IOException {
        releaseReader();
        return getReader(false);
    }

    /**
	 * Closes the reader. Next call to getReader will return a new instance.
	 */
    public static void releaseReader() {
        if (reader != null) {
            log.finer("Closing reader");
            try {
                reader.close();
            } catch (IOException e) {
                log.log(Level.WARNING, "Error closing reader", e);
            }
            reader = null;
        } else {
            log.warning("Reader already closed");
        }
    }

    /**
	 * 
	 * @return The number of indexed documents, or 0 if index does not exist.
	 */
    public static int getDocsNumber() {
        int docs = 0;
        if (Engine.indexExists()) {
            try {
                getReader();
                docs = reader.numDocs();
            } catch (IOException e) {
                log.log(Level.WARNING, "Error", e);
            } finally {
            }
        }
        return docs;
    }

    public static int getDocsNumber(IndexReader indexReader) {
        int docs = 0;
        if (Engine.indexExists()) {
            docs = indexReader.numDocs();
        }
        return docs;
    }

    public static void commit() throws CorruptIndexException, LockObtainFailedException, IOException {
        commit(true);
    }

    /**
	 * 
	 * @param optimize
	 * @throws CorruptIndexException
	 * @throws LockObtainFailedException
	 * @throws IOException
	 */
    public static void commit(boolean optimize) throws CorruptIndexException, LockObtainFailedException, IOException {
        log.info("Doing forced commit");
        getWriter();
        releaseWriter(true, optimize);
        log.fine("Forced commit done");
    }
}
