package uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.impl.sentencebody.conceptassertion.validation;

import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.complexobject.IParsedComplexRelationshipPhrase;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.impl.sentencebody.conceptassertion.ParsedClosureAssertion;

/**
 * Validates the relationship phrase because it appears along with the 'only'
 * keyword in the {@link ParsedClosureAssertion}
 * 
 * @author rdenaux
 * 
 */
public class ComplexRelationshipPhraseInClosureVR extends BaseOnlyWithComplexRelationshipPhraseVR<ParsedClosureAssertion> {

    private static final long serialVersionUID = -5776079179051071680L;

    public ComplexRelationshipPhraseInClosureVR(ParsedClosureAssertion bodyToValidate) {
        super(bodyToValidate);
    }

    @Override
    protected IParsedComplexRelationshipPhrase getComplexRelationshipPhrase() {
        return getPartToValidate().getComplexRelationshipPhrase();
    }
}
