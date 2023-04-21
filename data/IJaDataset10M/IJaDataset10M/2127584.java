package uk.co.ordnancesurvey.rabbitparser.gate.jape.disambiguation;

import java.util.List;
import uk.co.ordnancesurvey.rabbitparser.disambiguator.impl.ExactOverlapDisambiguator;
import uk.co.ordnancesurvey.rabbitparser.gate.annotation.RbtAnnotationType;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.object.IParsedObject;

/**
 * Disambiguates the annotations with type objects.
 * 
 * @author rdenaux
 * 
 */
public class JapeCB_ObjectAmbiguation extends BaseDisambiguationJapeCallBack<IParsedObject> {

    public JapeCB_ObjectAmbiguation() {
        super(RbtAnnotationType.RABBIT_OBJECT, IParsedObject.class);
    }

    @Override
    protected List<IParsedObject> disambiguateParts(List<IParsedObject> aListToDisambiguate) {
        ExactOverlapDisambiguator<IParsedObject> disambiguator = new ExactOverlapDisambiguator<IParsedObject>(IParsedObject.class);
        return disambiguator.disambiguate(aListToDisambiguate);
    }
}
