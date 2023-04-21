package com.esri.gpt.control.webharvest.engine;

import com.esri.gpt.framework.util.UuidUtil;
import com.esri.gpt.framework.util.Val;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.NoLockFactory;

/**
 * Source URI's array.
 */
class SourceUriArray implements Iterable<String[]> {

    /** logger */
    private static final Logger LOGGER = Logger.getLogger(SourceUriArray.class.getCanonicalName());

    /** subfolder of the collections index */
    private static final String SUBFOLDER = "OnFileStringCollection";

    /** names */
    private String[] names;

    /** lucene directory */
    private FSDirectory directory;

    /** writer */
    private IndexWriter writer;

    /** reader */
    private IndexReader reader;

    /**
 * Creates instance of the collection.
 * @throws IOException if creating instance fails
 */
    public SourceUriArray(String[] names) throws IOException {
        this.names = names != null ? names : new String[] {};
        String name = UuidUtil.makeUuid();
        String path = System.getProperty("java.io.tmpdir") + File.separator + SUBFOLDER + File.separator + name;
        File folder = new File(path);
        folder.mkdirs();
        directory = FSDirectory.open(folder, NoLockFactory.getNoLockFactory());
        openForWriting();
    }

    /**
 * Closes collection.
 * @throws IOException if clossing collection fails
 */
    public void close() throws IOException {
        File folder = null;
        if (reader != null) {
            reader.close();
            reader = null;
        }
        if (writer != null) {
            writer.close();
            writer = null;
        }
        if (directory != null) {
            folder = directory.getFile();
            directory.close();
        }
        if (folder != null) {
            for (File f : folder.listFiles()) {
                f.delete();
            }
            folder.delete();
        }
    }

    /**
 * Adds new string to the collection.
 * @param values array of values
 * @return <code>true</code> if adding values succeed
 */
    public boolean add(String[] values) {
        try {
            openForWriting();
            Document doc = new Document();
            for (int i = 0; i < names.length && i < values.length; i++) {
                Field fld = new Field(names[i], values[i], Field.Store.YES, Field.Index.NOT_ANALYZED, Field.TermVector.YES);
                doc.add(fld);
            }
            writer.addDocument(doc);
            return true;
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Error adding value to the collector.", ex);
            return false;
        }
    }

    /**
 * Removes string from the collection.
 * @param name field name
 * @param value field value
 * @return <code>true</code> if removing array succeed
 */
    public boolean remove(String name, String value) {
        try {
            openForWriting();
            writer.deleteDocuments(new Term(name, value));
            return true;
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Error removing value from the collector.", ex);
            return false;
        }
    }

    /**
 * Provides iterator over the current collection of stored pairs.
 * @return iterator
 */
    public Iterator<String[]> iterator() {
        return new PairIterator();
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            close();
        } catch (IOException ex) {
        }
        super.finalize();
    }

    /**
 * Opens collection for writing.
 * @throws IOException if opening collection fails
 */
    private void openForWriting() throws IOException {
        if (writer == null) {
            if (reader != null) {
                reader.close();
                reader = null;
            }
            Analyzer analyzer = new KeywordAnalyzer();
            writer = new IndexWriter(directory, analyzer, IndexWriter.MaxFieldLength.UNLIMITED);
        }
    }

    /**
 * Opens collection for reading
 * @throws CorruptIndexException if index corrupted
 * @throws IOException if opening collection fails
 */
    private void openForReading() throws CorruptIndexException, IOException {
        if (reader == null) {
            if (writer != null) {
                writer.commit();
                writer.close();
                writer = null;
            }
            reader = IndexReader.open(directory, true);
        }
    }

    /**
 * Iterator implementation.
 */
    private class PairIterator implements Iterator<String[]> {

        private int index = -1;

        private Document nextDoc;

        public boolean hasNext() {
            try {
                openForReading();
                if (nextDoc == null) {
                    while (index + 1 < reader.maxDoc()) {
                        index++;
                        if (!reader.isDeleted(index)) {
                            nextDoc = reader.document(index);
                            break;
                        }
                    }
                }
                if (nextDoc == null) {
                    reader.close();
                }
                return nextDoc != null;
            } catch (Exception ex) {
                LOGGER.log(Level.WARNING, "Error determining if there is more elemnts to iterate.", ex);
                return false;
            }
        }

        public String[] next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more elements.");
            }
            ArrayList<String> list = new ArrayList<String>();
            for (int i = 0; i < names.length; i++) {
                list.add(Val.chkStr(nextDoc.get(names[i])));
            }
            nextDoc = null;
            return list.toArray(new String[list.size()]);
        }

        public void remove() {
        }
    }
}
