package uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.impl.sentencebody.entitydeclaration;

import java.util.Collections;
import uk.co.ordnancesurvey.rabbitparser.ISentenceType;
import uk.co.ordnancesurvey.rabbitparser.featuretype.RabbitFeatureType;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.IParsedKeyphrase;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.entity.IParsedRelation;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.impl.entity.ParsedRelation;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.impl.sentencebody.conceptassertion.BaseParsedSentenceBody;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.IParsedRelationshipDeclaration;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.sentencetypedef.EntityDeclarationSentenceType;

/**
 * Default implementation of a {@link IParsedRelationshipDeclaration}.
 * 
 * @author rdenaux
 * 
 */
public class ParsedRelationshipDeclaration extends BaseParsedSentenceBody implements IParsedRelationshipDeclaration {

    private static final long serialVersionUID = -6125655446994872238L;

    public ParsedRelationshipDeclaration(IParsedRelation aDeclaredRel, IParsedKeyphrase aKP) {
        super(Collections.singletonList(aKP));
        assert aDeclaredRel != null;
        assert aKP != null;
        addNamedPart(RabbitFeatureType.RELATION, aDeclaredRel);
    }

    public IParsedRelation getDeclaredRelation() {
        return (IParsedRelation) getNamedPart(RabbitFeatureType.RELATION);
    }

    public ISentenceType getSentenceType() {
        return EntityDeclarationSentenceType.RELATIONSHIP_DECLARATION;
    }

    @Override
    public String getAsString() {
        return getDeclaredRelation().getAsString() + " is a relationship";
    }

    @Override
    public String getOldRendering() {
        return getClass().getSimpleName() + " " + getAsString();
    }

    @Override
    protected void doOnNewParsedResult() {
        ((ParsedRelation) getDeclaredRelation()).setBeingDefined(true);
        super.doOnNewParsedResult();
    }
}
