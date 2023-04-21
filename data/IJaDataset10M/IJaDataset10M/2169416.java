package org.jma.app.ewisdom.services.utils;

import java.io.File;
import java.io.IOException;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.pdfbox.searchengine.lucene.LucenePDFDocument;
import org.jma.app.ewisdom.entities.FileDescriptor;
import org.jma.lib.soap.utils.XMLSerializable;

/**
 * LuceneIndexer class creates an index and allows to add new documents and execute queries against 
 * the index.
 * @author jesmari@ono.com
 * @version 1.0
 * @since 1.0
 */
public class LuceneIndexer {

    public static String INDEX_REPOSITORY;

    public static int MAX_RESULTS = 50;

    Analyzer analyzer;

    IndexWriter writer;

    /**
	 * LuceneIndexer constructor
	 * @throws CorruptIndexException
	 * @throws LockObtainFailedException
	 * @throws IOException
	 */
    public LuceneIndexer() throws CorruptIndexException, LockObtainFailedException, IOException {
        analyzer = new StandardAnalyzer();
        try {
            writer = new IndexWriter(INDEX_REPOSITORY, analyzer, false, IndexWriter.MaxFieldLength.UNLIMITED);
        } catch (java.io.FileNotFoundException fnfe) {
            writer = new IndexWriter(INDEX_REPOSITORY, analyzer, true, IndexWriter.MaxFieldLength.UNLIMITED);
        }
    }

    /**
	 * Adds a new pdf file to the index, stores information about its FileDescriptor inside the index
	 * to allow FileDescritor location. 
	 * @param doc
	 * @param fd
	 * @throws CorruptIndexException
	 * @throws IOException
	 */
    public void addDocument(File doc, FileDescriptor fd) throws CorruptIndexException, IOException {
        Document luceneDocument = LucenePDFDocument.getDocument(doc);
        luceneDocument.add(new Field("FID", fd.getStringHashCode(), Field.Store.YES, Field.Index.NO));
        luceneDocument.add(new Field("path", fd.getPath(), Field.Store.YES, Field.Index.NO));
        luceneDocument.add(new Field("title", fd.getTitle(), Field.Store.YES, Field.Index.ANALYZED));
        luceneDocument.add(new Field("fileName", fd.getFileName(), Field.Store.YES, Field.Index.ANALYZED));
        luceneDocument.add(new Field("owner", fd.getOwner(), Field.Store.YES, Field.Index.ANALYZED));
        writer.addDocument(luceneDocument);
        writer.close();
    }

    /**
	 * Executes a query and returns a Vector of Doc, ScoreDocs
	 * @param queryString query string in lucene format
	 * @return a ScoreDocs.
	 * @throws CorruptIndexException
	 * @throws IOException
	 * @throws ParseException
	 */
    public synchronized XMLSerializable doQuery(String queryString) throws CorruptIndexException, IOException, ParseException {
        Searcher searcher = new IndexSearcher(INDEX_REPOSITORY);
        QueryParser queryParser = new QueryParser("contents", analyzer);
        Query query = queryParser.parse(queryString);
        TopDocs hits = searcher.search(query, MAX_RESULTS);
        ScoreDocs docHits = new ScoreDocs();
        for (int i = 0; i < hits.totalHits; i++) {
            Document doc = searcher.doc(i);
            Doc resumDoc = new Doc();
            resumDoc.setFID(doc.get("FID"));
            resumDoc.setOwner(doc.get("owner"));
            resumDoc.setPath(doc.get("path"));
            resumDoc.setScore(String.valueOf((hits.scoreDocs[i].score)));
            resumDoc.setTitle(doc.get("title"));
            resumDoc.setFileName(doc.get("fileName"));
            docHits.add(resumDoc);
        }
        return docHits;
    }
}
