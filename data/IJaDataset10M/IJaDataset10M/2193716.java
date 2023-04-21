package com.hp.hpl.jena.ontology.impl;

import com.hp.hpl.jena.enhanced.*;
import com.hp.hpl.jena.graph.*;
import com.hp.hpl.jena.ontology.*;

/**
 * <p>
 * Implementation of the functional property abstraction
 * </p>
 *
 * @author Ian Dickinson, HP Labs
 *         (<a  href="mailto:Ian.Dickinson@hp.com" >email</a>)
 * @version CVS $Id: InverseFunctionalPropertyImpl.java,v 1.9 2006/03/22 13:52:39 andy_seaborne Exp $
 */
public class InverseFunctionalPropertyImpl extends ObjectPropertyImpl implements InverseFunctionalProperty {

    /**
     * A factory for generating InverseFunctionalProperty facets from nodes in enhanced graphs.
     * Note: should not be invoked directly by user code: use 
     * {@link com.hp.hpl.jena.rdf.model.RDFNode#as as()} instead.
     */
    public static Implementation factory = new Implementation() {

        public EnhNode wrap(Node n, EnhGraph eg) {
            if (canWrap(n, eg)) {
                return new InverseFunctionalPropertyImpl(n, eg);
            } else {
                throw new ConversionException("Cannot convert node " + n + " to InverseFunctionalProperty - it must have rdf:type owl:InverseFunctionalProperty or equivalent");
            }
        }

        public boolean canWrap(Node node, EnhGraph eg) {
            Profile profile = (eg instanceof OntModel) ? ((OntModel) eg).getProfile() : null;
            return (profile != null) && profile.isSupported(node, eg, InverseFunctionalProperty.class);
        }
    };

    /**
     * <p>
     * Construct an inverse functional property node represented by the given node in the given graph.
     * </p>
     * 
     * @param n The node that represents the resource
     * @param g The enh graph that contains n
     */
    public InverseFunctionalPropertyImpl(Node n, EnhGraph g) {
        super(n, g);
    }
}
