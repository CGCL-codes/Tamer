package org.dbe.businessModeller.vocabulary.enterprise.sbvr;

import org.dbe.businessModeller.vocabulary.enterprise.VocabularyAttributesHandler;
import org.dbe.businessModeller.vocabulary.enterprise.exchange.ListChecker.SBVRLists;

public class CategorizationScheme extends VetisSBVRManager {

    /**
	 * Constructor
	 * @param newThingList
	 * @param newConceptualSchema
	 * @param newAttributesHandler
	 */
    public CategorizationScheme(SBVRLists newThingList, SBVR.ConceptualSchema newConceptualSchema, VocabularyAttributesHandler newAttributesHandler) {
        super(newThingList, newConceptualSchema, newAttributesHandler);
    }

    /**
	 * Create categorization scheme. Categorization type must be created before. 
	 * @param name
	 * @param categorizationTypeName
	 */
    public void create(SBVR.Name name, String categorizationTypeName) {
        SBVR.Designation catTypeDesignation = list_checker.checkForCategorizationType(categorizationTypeName);
        if (catTypeDesignation != null) {
            SBVR.CategorizationType categorizationType = (SBVR.CategorizationType) catTypeDesignation.getMeaning();
            SBVR.CategorizationScheme categorizationScheme = sbvr_factory.createCategorizationScheme();
            categorizationScheme.getIsFor().add(categorizationType.getIsFor().get(0));
            categorizationScheme.setIsBasedOn(categorizationType);
            ((SBVR.IndividualConcept) (name.getMeaning())).getInstance().add(categorizationScheme);
            name.getRefersTo().add(categorizationScheme);
            thingList.add(categorizationScheme);
        }
    }
}
