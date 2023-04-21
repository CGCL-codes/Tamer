package uk.co.ordnancesurvey.rabbitparser.gate.gatetoresultconverter;

import gate.Annotation;
import uk.co.ordnancesurvey.rabbitparser.RabbitKeyphraseType;
import uk.co.ordnancesurvey.rabbitparser.gate.annotation.GateLookupAnnotation;
import uk.co.ordnancesurvey.rabbitparser.gate.annotation.GateTokenAnnotation;
import uk.co.ordnancesurvey.rabbitparser.gate.annotation.RabbitAnnotation;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.IParsedKeyphrase;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.impl.ParsedKeyphrase;

/**
 * This converter converts a Gate {@link Annotation} with type
 * {@link RabbitAnnotation#}
 * 
 * @author rdenaux
 * 
 */
public class GateAnnotationToParsedKeyphraseConverter extends BaseAnnotationToRabbitParserResultConverter implements IGateAnnotationToParsedPartConverter<IParsedKeyphrase> {

    @Override
    public boolean canConvert(Annotation aAnn) {
        boolean result = false;
        final RabbitAnnotation annotationType = RabbitAnnotation.getByName(aAnn.getType());
        switch(annotationType) {
            case GATE_LOOKUP:
                try {
                    new GateLookupAnnotation(aAnn);
                    result = true;
                } catch (final IllegalArgumentException ex) {
                    result = false;
                }
                break;
            case GATE_SPLIT:
                result = true;
            case GATE_TOKEN:
                final GateTokenAnnotation gtAnn = new GateTokenAnnotation(aAnn);
                result = "punctuation".equals(gtAnn.getKind());
        }
        return result;
    }

    @Override
    public IParsedKeyphrase convert(Annotation aAnn) {
        ParsedKeyphrase result = null;
        final RabbitAnnotation annotationType = RabbitAnnotation.getByName(aAnn.getType());
        switch(annotationType) {
            case GATE_LOOKUP:
                final GateLookupAnnotation lAnn = new GateLookupAnnotation(aAnn);
                result = new ParsedKeyphrase(getRabbitKeyphraseType(lAnn));
                break;
            case GATE_SPLIT:
                result = new ParsedKeyphrase(RabbitKeyphraseType.SentenceSplit);
                break;
            case GATE_TOKEN:
                result = new ParsedKeyphrase(RabbitKeyphraseType.Punctuation);
                break;
            default:
                throw new RuntimeException("Can't convert annotationType " + annotationType + " into a ParsedKeyphrase. " + "Annotation was " + aAnn);
        }
        if (result != null) {
            assert result.getKeyphraseType() != null;
            setSpan(aAnn, result);
        }
        return result;
    }

    /**
     * Returns the correct {@link RabbitKeyphraseType} for aAnnotation. If no
     * match can be performed, this method throws a {@link RuntimeException}.
     * 
     * @param aGateLookupAnnotation
     * @return
     */
    private RabbitKeyphraseType getRabbitKeyphraseType(GateLookupAnnotation aGateLookupAnnotation) {
        final String minorType = aGateLookupAnnotation.getMinorTypeWithoutPrefix();
        return RabbitKeyphraseType.valueOf(minorType);
    }
}
