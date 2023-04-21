package test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Searcher {

    private Log log = LogFactory.getLog(getClass());

    private Analyzer analyzer = new StandardAnalyzer();

    private Directory directory = null;

    private IndexWriter indexWriter = null;

    public Searcher(String path) {
        File dir = new File(path);
        if (!dir.isDirectory()) dir.mkdirs();
        try {
            directory = FSDirectory.getDirectory(dir);
            if (!IndexReader.indexExists(directory)) {
                log.info("Index is not exist. Try to create an EMPTY index.");
                new IndexWriter(directory, analyzer, true).close();
            } else {
                if (IndexReader.isLocked(directory)) {
                    log.warn("Directory is locked! Try unlock!");
                    IndexReader.unlock(directory);
                }
            }
            indexWriter = new IndexWriter(directory, analyzer);
            Runtime.getRuntime().addShutdownHook(new Thread() {

                public void run() {
                    try {
                        indexWriter.close();
                    } catch (IOException e) {
                    }
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void index(Book book) {
        Document doc = new Document();
        doc.add(new Field("id", book.getId(), Field.Store.YES, Field.Index.UN_TOKENIZED));
        doc.add(new Field("title", book.getTitle(), Field.Store.YES, Field.Index.TOKENIZED));
        doc.add(new Field("author", book.getAuthor(), Field.Store.YES, Field.Index.TOKENIZED));
        doc.add(new Field("price", String.valueOf(book.getPrice()), Field.Store.YES, Field.Index.NO));
        try {
            indexWriter.addDocument(doc);
            indexWriter.flush();
        } catch (CorruptIndexException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void unindex(Book book) {
        try {
            indexWriter.deleteDocuments(new Term("id", book.getId()));
            indexWriter.flush();
        } catch (CorruptIndexException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void reindex(Book book) {
        unindex(book);
        index(book);
    }

    public List<Book> search(String q) {
        IndexSearcher indexSearcher = null;
        try {
            indexSearcher = new IndexSearcher(directory);
            Query query = MultiFieldQueryParser.parse(q, new String[] { "title", "author" }, new Occur[] { Occur.SHOULD, Occur.SHOULD }, analyzer);
            Hits hits = indexSearcher.search(query);
            int len = hits.length();
            if (len == 0) return Collections.emptyList();
            List<Book> books = new ArrayList<Book>(len);
            for (int i = 0; i < len; i++) {
                Document doc = hits.doc(i);
                Book book = new Book(doc.get("id"), doc.get("title"), doc.get("author"), Float.parseFloat(doc.get("price")));
                books.add(book);
            }
            return books;
        } catch (CorruptIndexException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } finally {
            if (indexSearcher != null) {
                try {
                    indexSearcher.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
