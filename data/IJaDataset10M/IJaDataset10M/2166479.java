package com.success.task.web.lucene.interfaces;

import com.success.task.web.exceptions.LuceneException;
import com.success.task.web.lucene.data.SearchResult;

public interface SearcherInterface {

    public SearchResult search(String keywords, int start) throws LuceneException;
}
