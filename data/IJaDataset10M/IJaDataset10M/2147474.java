package uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.impl.sentencebody.relationassertion;

import java.util.List;
import uk.co.ordnancesurvey.rabbitparser.ISentenceType;
import uk.co.ordnancesurvey.rabbitparser.featuretype.ExtendedRabbitFeatureType;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.IParsedKeyphrase;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.entity.IParsedRelation;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.relationshipassertion.IParsedInverseRelationAssertion;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.sentencetypedef.RelationAssertionSentenceType;

/**
 * Default implementation of {@link IParsedInverseRelationAssertion}
 * 
 * @author rdenaux
 * 
 */
public class ParsedInverseRelationAssertion extends BaseParsedRelationshipAssertion implements IParsedInverseRelationAssertion {

    private static final long serialVersionUID = -5117736814766539116L;

    public ParsedInverseRelationAssertion(IParsedRelation modRel, IParsedRelation aInvRel, List<IParsedKeyphrase> keyphrases) {
        super(modRel, keyphrases);
        assert aInvRel != null;
        addNamedPart(ExtendedRabbitFeatureType.R1, aInvRel);
    }

    public IParsedRelation getInverseRelation() {
        return (IParsedRelation) getNamedPart(ExtendedRabbitFeatureType.R1);
    }

    public ISentenceType getSentenceType() {
        return RelationAssertionSentenceType.INVERSE_RELATION_ASSERTION;
    }

    @Override
    public String getAsString() {
        StringBuilder sb = new StringBuilder();
        sb.append("The relationship ");
        sb.append(getModifiedRelation().getAsString());
        sb.append(" is the inverse of ");
        sb.append(getInverseRelation().getAsString());
        return sb.toString();
    }
}
