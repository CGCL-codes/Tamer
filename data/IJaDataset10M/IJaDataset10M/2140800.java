package org.dbe.businessModeller.vocabulary.enterprise.sbvr.factType.binaryFactType.partitiveFactType;

import org.dbe.businessModeller.vocabulary.enterprise.VocabularyAttributesHandler;
import org.dbe.businessModeller.vocabulary.enterprise.exchange.ListChecker.SBVRLists;
import org.dbe.businessModeller.vocabulary.enterprise.sbvr.factType.binaryFactType.BinaryFactType;

public class PartitiveFactType extends BinaryFactType {

    /**
	 * Constructor
	 * @param newThingList
	 * @param newConceptualSchema
	 * @param newDirection
	 * @param newAttributesHandler
	 */
    public PartitiveFactType(SBVRLists newThingList, SBVR.ConceptualSchema newConceptualSchema, String newDirection, VocabularyAttributesHandler newAttributesHandler) {
        super(newThingList, newConceptualSchema, newDirection, newAttributesHandler);
    }

    public SBVR.FactType create(String primaryRepresentation) {
        init(primaryRepresentation, 0);
        if (direction.equals("1")) init(primaryRepresentation, 1); else init(primaryRepresentation, 0);
        factType = sbvr_factory.createPartitiveFactType();
        super.create(true, false, true);
        return factType;
    }

    /**
	 * Creates synonym of the given fact type
	 * @param primaryRepresentation
	 * @param synonym
	 */
    public void createSynonym(String primaryRepresentation, String synonym) {
        factType = list_checker.checkForFactType(primaryRepresentation);
        init(synonym, 0);
        if (super.create(true, true, true)) {
            super.factSymbol.setPreferred(false);
            super.factSymbol.setProhibited(true);
        }
    }
}
