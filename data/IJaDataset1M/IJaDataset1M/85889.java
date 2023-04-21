package uk.co.ordnancesurvey.rabbitparser.gate.jape.relationshipphrasefinder;

import gate.Annotation;
import uk.co.ordnancesurvey.rabbitparser.gate.jape.base.BaseRelationshipPhrasePatternCB;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.entity.IParsedRelation;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.impl.object.ParsedRelationshipPhrase;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.object.IParsedCompoundObject;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.object.IParsedObject;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.object.IParsedObjectAndList;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.object.IParsedObjectList;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.object.IParsedRelationshipPhrase;

/**
 * Handles the callback of a Relationship phrase pattern found by a jape
 * transducer.
 * 
 * @author rdenaux
 * 
 */
public class JapeCB_RelationshipPhrasePattern extends BaseRelationshipPhrasePatternCB<IParsedRelationshipPhrase> {

    public JapeCB_RelationshipPhrasePattern() {
        super(IParsedRelationshipPhrase.class);
    }

    /**
     * Allow <code>null</code> output for {@link #retrieveFoundPart()} when
     * the found part could also be a subsumption phrase
     * 
     * @see uk.co.ordnancesurvey.rabbitparser.gate.jape.BaseSingleAnnotationOutputCallBack#isNullOutputAllowed()
     */
    @Override
    protected boolean isNullOutputAllowed() {
        return true;
    }

    @Override
    protected IParsedRelationshipPhrase retrieveFoundPart() {
        Annotation ann = getRequiredAnn("relation");
        IParsedRelation rel = (IParsedRelation) convertToEntity(ann);
        IParsedCompoundObject compoundObj = getOptionalPart("object", IParsedObject.class);
        if (compoundObj == null) {
            compoundObj = getOptionalPart("objectList", IParsedObjectList.class);
        }
        if (compoundObj == null) {
            compoundObj = getOptionalPart("objectAndList", IParsedObjectAndList.class);
        }
        assert compoundObj != null;
        IParsedRelationshipPhrase result = new ParsedRelationshipPhrase(rel, compoundObj);
        if (isSubsumptionPhrase(result)) {
            result = null;
        }
        return result;
    }

    /**
     * Returns <code>true</code> if aRelPhrase could also be a subsumption
     * phrase
     * 
     * @param aRelPhrase
     * @return
     */
    private boolean isSubsumptionPhrase(IParsedRelationshipPhrase aRelPhrase) {
        return aRelPhrase.getRelation().getAsString().equals("is") && aRelPhrase.getCompoundObject().getAsString().startsWith("a kind of");
    }
}
