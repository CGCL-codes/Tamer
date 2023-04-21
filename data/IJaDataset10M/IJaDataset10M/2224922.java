package uk.co.ordnancesurvey.rabbitparser.gate.jape.diagnosis;

import java.util.ArrayList;
import java.util.List;
import uk.co.ordnancesurvey.rabbitparser.gate.annotation.RbtAnnotation;
import uk.co.ordnancesurvey.rabbitparser.gate.exception.RabbitMissingRabbitFeatureException;
import uk.co.ordnancesurvey.rabbitparser.gate.jape.AddSingleAnnotationOutputCallBack;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.IParsedPart;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.RbtPartType;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.invalid.IInvalidSentence;

/**
 * Base class for invalid sentence callback handlers
 * 
 * @author rdenaux
 * 
 */
public abstract class BaseInvalidSentenceCB extends AddSingleAnnotationOutputCallBack<IInvalidSentence> {

    public BaseInvalidSentenceCB() {
        super(IInvalidSentence.class);
    }

    protected final List<IParsedPart> processTokens() {
        List<IParsedPart> result = new ArrayList<IParsedPart>();
        for (RbtAnnotation ann : getListOfOptionalRbtAnn("token")) {
            result.add(ann.getParsedToken());
        }
        return result;
    }

    /**
     * Try to find a IParsedPart in a {@link RbtAnnotation}. Use the
     * {@link RbtPartType} definitions to do this.
     * 
     * @param ann
     *            must not be <code>null</code>
     * @return
     */
    private IParsedPart findPart(RbtAnnotation ann) {
        assert ann != null;
        IParsedPart result = null;
        for (RbtPartType partType : RbtPartType.values()) {
            try {
                result = ann.getParsedPart(partType.getInterface());
                if (result != null) {
                    break;
                }
            } catch (RabbitMissingRabbitFeatureException e) {
            }
        }
        return result;
    }
}
