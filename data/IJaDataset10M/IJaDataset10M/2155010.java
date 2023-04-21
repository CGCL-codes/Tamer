package org.fcrepo.server.storage;

import java.io.InputStream;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;
import org.jrdf.graph.Literal;
import org.jrdf.graph.ObjectNode;
import org.jrdf.graph.Triple;
import org.trippi.RDFFormat;
import org.trippi.TripleIterator;
import org.trippi.TrippiException;
import org.fcrepo.server.errors.GeneralException;
import org.fcrepo.server.errors.ServerException;
import org.fcrepo.server.storage.types.Datastream;
import org.fcrepo.server.storage.types.RelationshipTuple;

public abstract class RDFRelationshipReader {

    public static Set<RelationshipTuple> readRelationships(Datastream ds) throws ServerException {
        if (ds == null) {
            return new HashSet<RelationshipTuple>();
        }
        try {
            return readRelationships(ds.getContentStream());
        } catch (TrippiException e) {
            throw new GeneralException(e.getMessage(), e);
        }
    }

    public static Set<RelationshipTuple> readRelationships(InputStream dsContent) throws TrippiException {
        Set<RelationshipTuple> tuples = new HashSet<RelationshipTuple>();
        TripleIterator iter = null;
        try {
            iter = TripleIterator.fromStream(dsContent, RDFFormat.RDF_XML);
            Triple triple;
            ObjectNode objectNode;
            boolean isLiteral;
            URI datatypeURI;
            String subject, predicate, object, datatype;
            while (iter.hasNext()) {
                triple = iter.next();
                subject = triple.getSubject().toString();
                predicate = triple.getPredicate().toString();
                objectNode = triple.getObject();
                isLiteral = objectNode instanceof Literal;
                datatype = null;
                if (isLiteral) {
                    object = ((Literal) objectNode).getLexicalForm();
                    datatypeURI = ((Literal) objectNode).getDatatypeURI();
                    if (datatypeURI != null) {
                        datatype = datatypeURI.toString();
                    }
                } else {
                    object = triple.getObject().toString();
                }
                tuples.add(new RelationshipTuple(subject, predicate, object, isLiteral, datatype));
            }
        } finally {
            if (iter != null) {
                iter.close();
            }
        }
        return tuples;
    }
}
