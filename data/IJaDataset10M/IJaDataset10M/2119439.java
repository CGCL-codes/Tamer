package com.hp.hpl.jena.reasoner.dig;

import org.w3c.dom.*;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.reasoner.TriplePattern;
import com.hp.hpl.jena.util.iterator.*;

/**
 * <p>
 * Translator that generates DIG 'types' queries in response to a find queries:
 * <pre>
 * :i rdf:type *
 * </pre>
 * or similar.
 * </p>
 *
 * @author Ian Dickinson, HP Labs (<a href="mailto:Ian.Dickinson@hp.com">email</a>)
 * @version CVS $Id: DIGQueryTypesTranslator.java,v 1.8 2006/03/22 13:52:53 andy_seaborne Exp $
 */
public class DIGQueryTypesTranslator extends DIGQueryTranslator {

    /**
     * <p>Construct a translator for the DIG query 'instances'.</p>
     * @param predicate The predicate URI to trigger on
     */
    public DIGQueryTypesTranslator(String predicate) {
        super(null, predicate, ALL);
    }

    /**
     * <p>Answer a query that will list the instances of a concept</p>
     */
    public Document translatePattern(TriplePattern pattern, DIGAdapter da) {
        DIGConnection dc = da.getConnection();
        Document query = dc.createDigVerb(DIGProfile.ASKS, da.getProfile());
        Element types = da.createQueryElement(query, DIGProfile.TYPES);
        da.addNamedElement(types, DIGProfile.INDIVIDUAL, da.getNodeID(pattern.getSubject()));
        return query;
    }

    /**
     * <p>Answer an iterator of triples that match the original find query.</p>
     */
    public ExtendedIterator translateResponseHook(Document response, TriplePattern query, DIGAdapter da) {
        return translateConceptSetResponse(response, query, true, da);
    }

    public Document translatePattern(TriplePattern pattern, DIGAdapter da, Model premises) {
        return null;
    }

    public boolean checkSubject(com.hp.hpl.jena.graph.Node subject, DIGAdapter da, Model premises) {
        return subject.isConcrete() && da.isIndividual(subject);
    }
}
