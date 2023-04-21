package uk.co.ordnancesurvey.rabbitparser.gate.jape.literalmodifierfinder;

import uk.co.ordnancesurvey.rabbitparser.gate.jape.AddSingleAnnotationOutputCallBack;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.impl.sentencebody.relationassertion.ParsedLiteralModifier;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.relationshipassertion.IParsedLiteralModifier;

/**
 * Handles the finding of a Literal modifier.
 * 
 * @author rdenaux
 * 
 */
public class JapeCB_LiteralModifierPattern extends AddSingleAnnotationOutputCallBack<IParsedLiteralModifier> {

    public JapeCB_LiteralModifierPattern() {
        super(IParsedLiteralModifier.class);
    }

    @Override
    protected IParsedLiteralModifier retrieveFoundPart() {
        return new ParsedLiteralModifier(getSingleRequiredKeyphrase());
    }
}
