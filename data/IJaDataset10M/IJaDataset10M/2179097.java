package org.hypergraphdb.app.sail.helpers;

import info.aduna.iteration.CloseableIteration;
import org.hypergraphdb.app.sail.HyperGraphConnection;
import org.hypergraphdb.app.sail.helpers.QueryEvaluationIteration;
import org.openrdf.model.*;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.algebra.evaluation.TripleSource;
import org.openrdf.sail.SailException;

/**
 * created Feb 1, 2010  - 12:23:45 PM
 *
 * @author IanHolsman
 *         Copyright (C) 2010 by Aol. All Rights Reserved.
 */
public class HyperGraphTripleSource implements TripleSource {

    boolean inferred;

    final HyperGraphConnection conn;

    public HyperGraphTripleSource(final HyperGraphConnection conn, boolean inferred) {
        this.inferred = inferred;
        this.conn = conn;
    }

    public HyperGraphTripleSource(final HyperGraphConnection conn) {
        inferred = false;
        this.conn = conn;
    }

    public CloseableIteration<? extends Statement, QueryEvaluationException> getStatements(Resource s, URI p, Value o, Resource... contexts) throws QueryEvaluationException {
        final CloseableIteration<? extends Statement, SailException> src;
        try {
            src = conn.getStatements(s, p, o, inferred, contexts);
        } catch (SailException e) {
            throw new QueryEvaluationException(e);
        }
        return new QueryEvaluationIteration<Statement>(src);
    }

    public ValueFactory getValueFactory() {
        return conn.getValueFactory();
    }
}
