package org.encuestame.business.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.encuestame.core.search.DirectoryIndexStore;
import org.encuestame.core.service.DirectorySetupOperations;
import org.encuestame.persistence.exception.EnmeFailOperation;

/**
 * Query Search Manager to Lucene Index.
 *
 * @author Morales, Diana Paola paolaATencuestame.org
 * @since Mar 24, 2011
 */
public class SearchAttachmentManager implements SearchManagerOperation {

    /** Log. **/
    private static final Log log = LogFactory.getLog(SearchAttachmentManager.class);

    /** Index Directory Path. **/
    final String directoryIndex;

    /** Option to Read Index Directory. **/
    private Boolean readDirectory = true;

    /** Index Directory **/
    private Directory directory;

    /** {@link DirectoryIndexStore}**/
    private DirectoryIndexStore directoryIndexStore;

    /** Index Searcher **/
    private IndexSearcher indexSearcher;

    /**
    * Constructor.
     * @throws EnmeFailOperation
    */
    public SearchAttachmentManager() throws EnmeFailOperation {
        this.directoryIndex = DirectorySetupOperations.getIndexesDirectory();
    }

    /**
    * Start Index Searcher.
    * @throws IOException
    */
    private void startIndexSearcher(final DirectoryIndexStore d) throws IOException {
        this.directory = d.getDirectory();
        this.indexSearcher = new IndexSearcher(this.directory, true);
    }

    /**
     * Index Searcher.
     * @return
     * @throws CorruptIndexException
     * @throws IOException
     */
    private IndexSearcher indexSearch() throws CorruptIndexException, IOException {
        return this.indexSearcher = new IndexSearcher(this.directory, this.readDirectory);
    }

    /**
    * Search Query in Lucene Index.
    * @param indexDir
    * @param q
    * @throws IOException
    * @throws ParseException
    */
    public List<Document> search(final String queryText, final int max, final String field) throws IOException, ParseException {
        this.startIndexSearcher(this.getDirectoryIndexStore());
        final List<Document> results = new ArrayList<Document>();
        QueryParser parser = new QueryParser(SearchUtils.LUCENE_VERSION, field, new StandardAnalyzer(SearchUtils.LUCENE_VERSION));
        Query query = parser.parse(queryText);
        long start = System.currentTimeMillis();
        TopDocs hits = this.indexSearch().search(query, max);
        long end = System.currentTimeMillis();
        log.debug("Found " + hits.totalHits + " document(s) (in " + (end - start) + " milliseconds) that matched query '" + queryText + "':");
        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = indexSearch().doc(scoreDoc.doc);
            log.debug("Fullpath Document -->" + doc.get("fullpath"));
            results.add(doc);
        }
        indexSearch().close();
        return results;
    }

    /**
    * @return the directoryIndexStore
    */
    public DirectoryIndexStore getDirectoryIndexStore() {
        return directoryIndexStore;
    }

    /**
    * @param directoryIndexStore the directoryIndexStore to set
    */
    public void setDirectoryIndexStore(final DirectoryIndexStore directoryIndexStore) {
        this.directoryIndexStore = directoryIndexStore;
    }
}
