package net.sf.gumshoe.indexer;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.DateField;
import org.apache.lucene.document.Document;

/**
 * @author Gabor
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DiskIndexer {

    protected Logger logger = null;

    public DiskIndexer() {
        logger = Logger.getLogger(getClass().getName());
    }

    public void index(File indexDir, File dataDir, boolean recurse) throws IOException {
        if (!dataDir.exists() || !dataDir.isDirectory()) {
            throw new IOException(dataDir + " does not exist or is not a directory");
        }
        IndexWriter iw = null;
        try {
            iw = new IndexWriter(indexDir, new StandardAnalyzer(), !IndexReader.indexExists(indexDir));
            iw.close();
        } catch (IOException ioe) {
            Directory fsDir = FSDirectory.getDirectory(indexDir, !IndexReader.indexExists(indexDir));
            IndexReader.unlock(fsDir);
        }
        indexDirectory(indexDir, dataDir, recurse);
    }

    private void indexDirectory(File indexDir, File dir, boolean recurse) throws IOException {
        if (indexDir.equals(dir)) {
            return;
        }
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            if (f.isDirectory()) {
                if (recurse) {
                    indexDirectory(indexDir, f, recurse);
                }
            } else {
                try {
                    indexFile(indexDir, f);
                } catch (Throwable t) {
                    logger.log(Level.SEVERE, "indexing failed for " + f.getCanonicalPath(), t);
                }
            }
        }
    }

    private void indexFile(File indexDir, File f) throws Exception {
        IndexReader ir = null;
        try {
            Directory fsDir = FSDirectory.getDirectory(indexDir, !IndexReader.indexExists(indexDir));
            ir = IndexReader.open(fsDir);
            TermDocs td = ir.termDocs(new Term(ContentReader.FILENAME, f.getCanonicalPath()));
            boolean found = td.next();
            if (found) {
                Document doc = ir.document(td.doc());
                long prevModified = DateField.stringToTime(doc.get(ContentReader.MODIFIED));
                if (f.lastModified() == prevModified) {
                    logger.fine("match found for " + f.getCanonicalPath());
                    return;
                } else {
                    ir.delete(td.doc());
                }
            }
        } finally {
            if (ir != null) {
                ir.close();
            }
        }
        IndexWriter iw = null;
        try {
            Document d = ContentReaderFactory.getInstance().getDocument(f);
            iw = new IndexWriter(indexDir, new StandardAnalyzer(), !IndexReader.indexExists(indexDir));
            iw.addDocument(d);
            logger.fine("indexed " + f.getCanonicalPath());
        } finally {
            if (iw != null) {
                iw.close();
            }
        }
    }
}
