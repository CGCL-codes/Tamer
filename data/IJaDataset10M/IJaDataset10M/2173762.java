package com.hp.hpl.jena.rdf.arp;

import java.util.Arrays;
import com.hp.hpl.jena.graph.BulkUpdateHandler;
import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.rdf.arp.impl.ARPSaxErrorHandler;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFErrorHandler;
import com.hp.hpl.jena.shared.JenaException;
import com.hp.hpl.jena.shared.impl.PrefixMappingImpl;

final class JenaHandler extends ARPSaxErrorHandler implements StatementHandler, NamespaceHandler {

    private static final int BULK_UPDATE_SIZE = 1000;

    private final BulkUpdateHandler bulk;

    private final Model model;

    final Triple triples[];

    int ix = 0;

    public JenaHandler(Model m, RDFErrorHandler e) {
        this(m.getGraph(), m, e);
    }

    JenaHandler(Graph g, RDFErrorHandler e) {
        this(g, null, e);
    }

    public JenaHandler(Graph g, Model m, RDFErrorHandler e) {
        this(g.getBulkUpdateHandler(), m, e);
    }

    private JenaHandler(BulkUpdateHandler bulk, Model model, RDFErrorHandler errorHandler) {
        super(errorHandler);
        this.bulk = bulk;
        this.model = model;
        triples = new Triple[BULK_UPDATE_SIZE];
    }

    public void useWith(ARPHandlers h) {
        h.setStatementHandler(this);
        h.setErrorHandler(this);
        h.setNamespaceHandler(this);
    }

    public void statement(AResource subj, AResource pred, AResource obj) {
        try {
            triples[ix++] = JenaReader.convert(subj, pred, obj);
        } catch (JenaException e) {
            errorHandler.error(e);
        }
        if (ix == BULK_UPDATE_SIZE) bulkUpdate();
    }

    public void statement(AResource subj, AResource pred, ALiteral lit) {
        try {
            triples[ix++] = JenaReader.convert(subj, pred, lit);
        } catch (JenaException e) {
            errorHandler.error(e);
        }
        if (ix == BULK_UPDATE_SIZE) bulkUpdate();
    }

    public void bulkUpdate() {
        try {
            if (ix == BULK_UPDATE_SIZE) bulk.add(triples); else bulk.add(Arrays.asList(triples).subList(0, ix));
        } catch (JenaException e) {
            errorHandler.error(e);
        } finally {
            ix = 0;
        }
    }

    public void startPrefixMapping(String prefix, String uri) {
        if (model != null && PrefixMappingImpl.isNiceURI(uri)) model.setNsPrefix(prefix, uri);
    }

    public void endPrefixMapping(String prefix) {
    }
}
