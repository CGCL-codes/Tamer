package com.plexobject.docusearch.persistence;

import java.util.Iterator;
import java.util.List;
import com.plexobject.docusearch.domain.Document;

public class DocumentsIterator implements Iterator<List<Document>> {

    private final DocumentRepository repository;

    private final String database;

    private final int limit;

    private String startKey;

    private PagedList<Document> docs;

    private int total;

    private int requests;

    private boolean consumedFirstBatch;

    public DocumentsIterator(final DocumentRepository repository, final String database, final int limit) {
        this(repository, database, null, limit);
    }

    public DocumentsIterator(final DocumentRepository repository, final String database, final String startKey, final int limit) {
        if (repository == null) {
            throw new NullPointerException("null repository");
        }
        this.repository = repository;
        this.database = database;
        this.startKey = startKey;
        this.limit = limit;
        fetchNext();
    }

    private void fetchNext() {
        final String endKey = null;
        docs = repository.getAllDocuments(database, startKey, endKey, limit + 1);
        if (docs == null) {
            throw new IllegalStateException("null documents from " + repository);
        }
        startKey = docs.hasMore() ? docs.remove(limit).getId() : null;
        total += docs.size();
        requests++;
    }

    @Override
    public boolean hasNext() {
        return consumedFirstBatch ? docs.hasMore() : docs.size() > 0;
    }

    @Override
    public List<Document> next() {
        final List<Document> docsToReturn = docs;
        consumedFirstBatch = true;
        if (docs.hasMore()) {
            fetchNext();
        }
        return docsToReturn;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    public int getTotal() {
        return total;
    }

    public int getNumberOfRequests() {
        return requests;
    }

    @Override
    public String toString() {
        return "Document Iterator " + total + " - startKey " + startKey + ", requests " + requests;
    }
}
