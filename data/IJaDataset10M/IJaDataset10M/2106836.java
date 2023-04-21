package com.liferay.portal.search.lucene;

import com.liferay.portal.kernel.search.IndexSearcher;
import com.liferay.portal.kernel.search.IndexWriter;
import com.liferay.portal.kernel.search.SearchEngine;
import com.liferay.portal.util.PropsValues;

/**
 * <a href="LuceneSearchEngineImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author Bruno Farache
 *
 */
public class LuceneSearchEngineImpl implements SearchEngine {

    public LuceneSearchEngineImpl() {
        _searcher = new LuceneIndexSearcherImpl();
        _writer = new LuceneIndexWriterImpl();
    }

    public String getName() {
        return _NAME;
    }

    public IndexSearcher getSearcher() {
        return _searcher;
    }

    public IndexWriter getWriter() {
        return _writer;
    }

    public boolean isIndexReadOnly() {
        return PropsValues.INDEX_READ_ONLY;
    }

    private static final String _NAME = "LUCENE";

    private IndexSearcher _searcher;

    private IndexWriter _writer;
}
