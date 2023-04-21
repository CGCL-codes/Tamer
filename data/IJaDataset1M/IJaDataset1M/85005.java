package au.edu.diasb.annotation.danno.impl.sesame;

import org.openrdf.query.GraphQuery;
import org.openrdf.query.Query;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.TupleQuery;
import org.openrdf.repository.RepositoryConnection;
import au.edu.diasb.annotation.danno.model.AnnoteaTypeException;
import au.edu.diasb.annotation.danno.model.RDFContainer;
import au.edu.diasb.annotation.danno.model.TripleStoreException;
import au.edu.diasb.annotation.danno.sparql.SPARQLQuery;
import au.edu.diasb.annotation.danno.sparql.SPARQLResultSet;

public class SesameQuery implements SPARQLQuery {

    private final RepositoryConnection conn;

    private final Query query;

    private final SesameAnnoteaTypeFactory factory;

    SesameQuery(SesameAnnoteaTypeFactory factory, Query query, RepositoryConnection conn) {
        this.factory = factory;
        this.query = query;
        this.conn = conn;
    }

    @Override
    public SPARQLResultSet executeSelect(RDFContainer container) {
        if (conn != ((SesameRDFContainer) container).getConnection()) {
            throw new IllegalArgumentException("container doesn't match query");
        }
        if (query instanceof TupleQuery) {
            try {
                return new SesameResultSet(factory, conn, ((TupleQuery) query).evaluate());
            } catch (QueryEvaluationException ex) {
                throw new TripleStoreException(ex);
            }
        } else {
            throw new IllegalArgumentException("wrong kind of query");
        }
    }

    @Override
    public RDFContainer executeConstruct(RDFContainer container) throws AnnoteaTypeException {
        if (conn != ((SesameRDFContainer) container).getConnection()) {
            throw new IllegalArgumentException("container doesn't match query");
        }
        if (query instanceof GraphQuery) {
            try {
                return factory.reify(((GraphQuery) query).evaluate());
            } catch (QueryEvaluationException ex) {
                throw new TripleStoreException(ex);
            }
        } else {
            throw new IllegalArgumentException("wrong kind of query");
        }
    }
}
