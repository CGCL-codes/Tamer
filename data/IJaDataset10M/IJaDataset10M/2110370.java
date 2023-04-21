package org.springframework.lucene.index.factory;

import java.io.IOException;
import junit.framework.TestCase;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.store.RAMDirectory;
import org.springframework.lucene.index.LuceneIndexAccessException;

/**
 * @author Thierry Templier
 */
public class SimpleIndexFactoryTests extends TestCase {

    private RAMDirectory directory;

    /**
	 * @see junit.framework.TestCase#setUp()
	 */
    protected void setUp() throws Exception {
        this.directory = new RAMDirectory();
        IndexWriter indexWriter = new IndexWriter(this.directory, new SimpleAnalyzer(), true, MaxFieldLength.UNLIMITED);
        Document document = new Document();
        document.add(new Field("field", "a sample", Field.Store.YES, Field.Index.ANALYZED));
        document.add(new Field("filter", "a sample filter", Field.Store.YES, Field.Index.ANALYZED));
        document.add(new Field("sort", "2", Field.Store.YES, Field.Index.NOT_ANALYZED));
        indexWriter.addDocument(document);
        indexWriter.close();
    }

    /**
	 * @see junit.framework.TestCase#tearDown()
	 */
    protected void tearDown() throws Exception {
        this.directory = null;
    }

    private void closeIndexReader(IndexReaderWrapper indexReader) {
        if (indexReader != null) {
            try {
                indexReader.close();
            } catch (IOException ex) {
                fail();
            }
        }
    }

    private void closeIndexWriter(IndexWriterWrapper indexWriter) {
        if (indexWriter != null) {
            try {
                indexWriter.close();
            } catch (IOException ex) {
                fail();
            }
        }
    }

    private void closeIndexReader(IndexReader indexReader) {
        if (indexReader != null) {
            try {
                indexReader.close();
            } catch (IOException ex) {
                fail();
            }
        }
    }

    private void closeIndexWriter(IndexWriter indexWriter) {
        if (indexWriter != null) {
            try {
                indexWriter.close();
            } catch (IOException ex) {
                fail();
            }
        }
    }

    public final void testGetIndexFactoryReader() {
        SimpleIndexFactory indexFactory = new SimpleIndexFactory();
        try {
            indexFactory.getIndexReader();
            fail();
        } catch (LuceneIndexAccessException ex) {
        }
    }

    public final void testGetIndexFactoryWriter() {
        SimpleIndexFactory indexFactory = new SimpleIndexFactory();
        try {
            indexFactory.getIndexWriter();
            fail();
        } catch (LuceneIndexAccessException ex) {
        }
    }

    public final void testIndexLockingWithReader() throws Exception {
        SimpleIndexFactory indexFactory = new SimpleIndexFactory();
        indexFactory.setDirectory(this.directory);
        IndexWriter indexWriter = null;
        IndexReaderWrapper indexReader = null;
        try {
            indexWriter = new IndexWriter(this.directory, new SimpleAnalyzer(), false, MaxFieldLength.UNLIMITED);
            indexReader = indexFactory.getIndexReader();
        } catch (LuceneIndexAccessException ex) {
        } finally {
            closeIndexReader(indexReader);
            closeIndexWriter(indexWriter);
        }
    }

    public final void testIndexLockingResolvingWithReader() throws Exception {
        SimpleIndexFactory indexFactory = new SimpleIndexFactory();
        indexFactory.setDirectory(this.directory);
        indexFactory.setResolveLock(true);
        IndexWriter indexWriter = null;
        IndexReaderWrapper indexReader = null;
        try {
            indexWriter = new IndexWriter(this.directory, new SimpleAnalyzer(), false, MaxFieldLength.UNLIMITED);
            indexReader = indexFactory.getIndexReader();
        } catch (LuceneIndexAccessException ex) {
        } finally {
            closeIndexReader(indexReader);
            closeIndexWriter(indexWriter);
        }
    }

    public final void testGetIndexWriter() throws Exception {
        SimpleIndexFactory indexFactory = new SimpleIndexFactory();
        indexFactory.setDirectory(this.directory);
        indexFactory.setAnalyzer(new SimpleAnalyzer());
        IndexWriterWrapper indexWriter = null;
        try {
            indexWriter = indexFactory.getIndexWriter();
            assertNotNull(indexWriter);
            Document document = new Document();
            document.add(new Field("field", "a sample", Field.Store.YES, Field.Index.ANALYZED));
            document.add(new Field("filter", "a sample filter", Field.Store.YES, Field.Index.ANALYZED));
            document.add(new Field("sort", "3", Field.Store.YES, Field.Index.NOT_ANALYZED));
            indexWriter.addDocument(document);
        } finally {
            closeIndexWriter(indexWriter);
        }
    }

    public final void testIndexLockingWithWriter() throws Exception {
        SimpleIndexFactory indexFactory = new SimpleIndexFactory();
        indexFactory.setDirectory(this.directory);
        IndexWriter indexWriter1 = null;
        IndexWriterWrapper indexWriter2 = null;
        try {
            indexWriter1 = new IndexWriter(this.directory, new SimpleAnalyzer(), false, MaxFieldLength.UNLIMITED);
            indexWriter2 = indexFactory.getIndexWriter();
            fail();
        } catch (LuceneIndexAccessException ex) {
        } finally {
            closeIndexWriter(indexWriter2);
            closeIndexWriter(indexWriter1);
        }
    }

    public final void testIndexLockingResolvingWithWriter() throws Exception {
        SimpleIndexFactory indexFactory = new SimpleIndexFactory();
        indexFactory.setDirectory(this.directory);
        indexFactory.setResolveLock(true);
        IndexWriter indexWriter1 = null;
        IndexWriterWrapper indexWriter2 = null;
        try {
            indexWriter1 = new IndexWriter(this.directory, new SimpleAnalyzer(), false, MaxFieldLength.UNLIMITED);
            indexWriter2 = indexFactory.getIndexWriter();
        } catch (LuceneIndexAccessException ex) {
        } finally {
            closeIndexWriter(indexWriter2);
            closeIndexWriter(indexWriter1);
        }
    }

    public final void testIndexNoCreationWithReader() throws Exception {
        SimpleIndexFactory indexFactory = new SimpleIndexFactory();
        indexFactory.setDirectory(new RAMDirectory());
        indexFactory.setCreate(false);
        IndexReaderWrapper indexReader = null;
        try {
            indexReader = indexFactory.getIndexReader();
            fail();
        } catch (LuceneIndexAccessException ex) {
        } finally {
            closeIndexReader(indexReader);
        }
    }

    public final void testIndexCreationWithReader() throws Exception {
        SimpleIndexFactory indexFactory = new SimpleIndexFactory();
        indexFactory.setDirectory(new RAMDirectory());
        indexFactory.setCreate(true);
        IndexReaderWrapper indexReader = null;
        try {
            indexReader = indexFactory.getIndexReader();
        } catch (LuceneIndexAccessException ex) {
        } finally {
            closeIndexReader(indexReader);
        }
    }

    public final void testIndexNoCreationWithWriter() throws Exception {
        SimpleIndexFactory indexFactory = new SimpleIndexFactory();
        indexFactory.setDirectory(new RAMDirectory());
        indexFactory.setCreate(false);
        IndexWriterWrapper indexWriter = null;
        try {
            indexWriter = indexFactory.getIndexWriter();
            fail();
        } catch (LuceneIndexAccessException ex) {
        } finally {
            closeIndexWriter(indexWriter);
        }
    }

    public final void testIndexCreationWithWriter() throws Exception {
        SimpleIndexFactory indexFactory = new SimpleIndexFactory();
        indexFactory.setDirectory(new RAMDirectory());
        indexFactory.setCreate(true);
        IndexWriterWrapper indexWriter = null;
        try {
            indexWriter = indexFactory.getIndexWriter();
        } finally {
            closeIndexWriter(indexWriter);
        }
    }
}
