package net.sourceforge.retriever.persistor;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;

/**
 * Persists an object using Lucene.
 * 
 * The object to be persisted through the method persist(Object) must
 * be annotated as described bellow
 * 
 * <code>
 *	@Persistent
 *	public class PersistentClass {
 *
 *	   private int key;
 *
 *	   @LuceneField(name="key_test", indexed=true, tokenized=false, stored=true, compressed=false)
 *	   public int getKey() {
 *		   return this.key;
 *	   }
 *
 *	   public void setKey(final int key) {
 *	       this.key = key;
 *     }
 * }
 * </code>
 */
public class LucenePersistor {

    private String indexFolder;

    private IndexWriter indexWriter;

    public LucenePersistor(final String indexFolder) {
        this.indexFolder = indexFolder;
    }

    /**
	 * Opens the index using the WhitespaceAnalyzer().
	 * 
	 * @param create Creates the index from scratch or not.
	 * @throws IOException If something goes wrong while opening the index.
	 */
    public void open(final boolean create) throws IOException {
        this.open(new WhitespaceAnalyzer(), create);
    }

    /**
	 * Opens the index using Lucene's WhitespaceAnalyzer class.
	 *
	 * @param analyzer Defines an Analyzer to be used when writing to the index.
	 * @param create Determines whether a new index is created, or whether an existing
	 *        index is opened.
	 * @throws IOException If something goes wrong while opening the index.
	 */
    public void open(final Analyzer analyzer, final boolean create) throws IOException {
        if (create) new File(this.indexFolder).mkdir();
        this.indexWriter = new IndexWriter(this.indexFolder, analyzer, create);
    }

    /**
	 * Persists an object that must be annotated with the Persistent and LuceneField
	 * annotations.
	 * 
	 * <code>
	 *	@Persistent
	 *	public class PersistentClass {
	 *
	 *	   private int key;
	 *
	 *	   @LuceneField(name="key_test", indexed=true, tokenized=false, stored=true, compressed=false)
	 *	   public int getKey() {
	 *		   return this.key;
	 *	   }
	 *
	 *	   public void setKey(final int key) {
	 *	       this.key = key;
	 *     }
	 * }
	 * </code>
	 */
    @SuppressWarnings("unchecked")
    public void persist(final Object objectToPersist) throws Exception {
        final Class objectToPersistClass = objectToPersist.getClass();
        if (objectToPersistClass.isAnnotationPresent(Persistent.class)) {
            final Document documentToPersistIntoIndex = new Document();
            final Method[] methods = objectToPersistClass.getMethods();
            Term key = null;
            for (Method method : methods) {
                if (method.isAnnotationPresent(LuceneField.class)) {
                    final Term tempKey = this.fillDocumentToPersist(objectToPersist, documentToPersistIntoIndex, method);
                    if (tempKey != null) key = tempKey;
                }
            }
            this.indexWriter.updateDocument(key, documentToPersistIntoIndex);
        }
    }

    /**
	 * Optimizes the index, turning any logical modification into physical.
	 * 
	 * @throws IOException If something went wrong while optimizing.
	 */
    public void optimize() throws IOException {
        if (this.indexWriter != null) this.indexWriter.optimize();
    }

    /**
	 * Closes the index.
	 * 
	 * You must invoke this method always.
	 * 
	 * @throws IOException If something wrong occurs while closing the index.
	 */
    public void close() throws IOException {
        if (this.indexWriter != null) this.indexWriter.close();
    }

    /**
	 * Verifies if the index folder, defined when constructing the object, has
	 * an index inside it.
	 * 
	 * @return If the index folder, defined when constructing the object, has
	 *         an index inside it.
	 */
    public boolean hasIndex() {
        if (IndexReader.indexExists(this.indexFolder)) {
            return true;
        } else {
            return false;
        }
    }

    private Term fillDocumentToPersist(final Object objectToPersist, final Document documentToPersistIntoIndex, Method method) throws IllegalAccessException, InvocationTargetException {
        final LuceneField luceneFieldAnnotation = method.getAnnotation(LuceneField.class);
        final String fieldName = luceneFieldAnnotation.name();
        final String fieldValue = method.invoke(objectToPersist).toString();
        Store store = null;
        if (luceneFieldAnnotation.stored()) {
            if (luceneFieldAnnotation.compressed()) {
                store = Store.COMPRESS;
            } else {
                store = Store.YES;
            }
        } else {
            store = Store.NO;
        }
        Index index = null;
        if (luceneFieldAnnotation.indexed()) {
            if (luceneFieldAnnotation.tokenized()) {
                index = Index.TOKENIZED;
            } else {
                index = Index.UN_TOKENIZED;
            }
        } else {
            index = Index.NO;
        }
        documentToPersistIntoIndex.add(new Field(fieldName, fieldValue, store, index));
        if (luceneFieldAnnotation.key()) {
            return new Term(fieldName, fieldValue);
        } else {
            return null;
        }
    }
}
