package uk.ac.roslin.ensembl.model.compara;

import uk.ac.roslin.ensembl.model.core.Gene;
import uk.ac.roslin.ensembl.model.relationship.BinaryRelationship;

/**
 * extends HomologyRelationship for those Relationships that are binary
 * @author tpaterso
 */
public interface HomologyPairRelationship<CLAZZ extends Gene> extends HomologyRelationship<CLAZZ>, BinaryRelationship<CLAZZ> {

    public HomologyAlignmentProperties getSourceProperties();

    public HomologyAlignmentProperties getTargetProperties();
}
