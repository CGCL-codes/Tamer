package org.hypergraphdb.app.wordnet.data;

import org.hypergraphdb.HGHandle;

/**
 * 
 * <p>
 * A semantic link that represents and adverb (first target) that was derived
 * from an adjective (second target) or an adjective (first target) that was
 * derived from a noun (second target).
 * </p>
 * 
 * @author Borislav Iordanov
 * 
 */
public class DerivedFrom extends SemanticLink {

    public DerivedFrom() {
        super();
    }

    public DerivedFrom(HGHandle[] outgoingSet) {
        super(outgoingSet);
    }
}
