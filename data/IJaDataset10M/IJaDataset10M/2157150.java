package com.ontotext.ordi.sar;

import java.io.IOException;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.impl.LiteralImpl;
import org.openrdf.model.impl.URIImpl;
import com.ontotext.platform.iteration.CloseableIterator;
import com.ontotext.platform.rdf.RdfQuery;
import com.ontotext.platform.rdf.RdfQueryException;
import com.ontotext.platform.rdf.RdfStore;
import com.ontotext.platform.rdf.RdfStoreException;

public class RdfContentStorage implements DocumentContentStorage {

    RdfStore store;

    RdfQuery query;

    public RdfContentStorage(RdfStore store, RdfQuery query) {
        this.store = store;
        this.query = query;
    }

    public String load(String id) throws IOException {
        URI subj = new URIImpl(id);
        URI pred = new URIImpl(NamingUtility.HAS_CONTENT);
        String contentStr = null;
        CloseableIterator<? extends Statement> result;
        try {
            result = query.search(subj, pred, null, subj, null, false);
        } catch (RdfQueryException e) {
            throw new IOException(e);
        }
        while (result.hasNext()) {
            if (contentStr != null) {
                throw new IOException(String.format("Ambigious data: multiple results obtained while looking up for the content of %s.", id));
            }
            Statement statement = result.next();
            contentStr = statement.getObject().stringValue();
            return contentStr;
        }
        throw new IOException("Missing document content statement.");
    }

    public void store(String id, String content) throws IOException {
        URI subj = new URIImpl(id);
        URI pred = new URIImpl(NamingUtility.HAS_CONTENT);
        Value obj = new LiteralImpl(content);
        try {
            store.add(subj, pred, obj, subj);
        } catch (RdfStoreException e) {
            throw new IOException(e);
        }
    }
}
