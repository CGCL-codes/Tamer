package uk.co.ordnancesurvey.rabbitparser.gate.jape.addparsedtoken;

import gate.Annotation;
import uk.co.ordnancesurvey.rabbitparser.gate.annotation.GateTokenAnnotation;
import uk.co.ordnancesurvey.rabbitparser.gate.jape.ExtendSingleAnnotationOutputCallBack;
import uk.co.ordnancesurvey.rabbitparser.gate.parsedpart.entity.GateParsedToken;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.entity.IParsedToken;

/**
 * Adds a {@link GateParsedToken} to a Token annotation.
 * 
 * @author rdenaux
 * 
 */
public class JapeCB_AddParsedToken extends ExtendSingleAnnotationOutputCallBack<IParsedToken> {

    public JapeCB_AddParsedToken() {
        super(IParsedToken.class);
    }

    @Override
    protected IParsedToken retrieveFoundPart() {
        Annotation ann = getRequiredAnn("Token");
        GateTokenAnnotation gtAnn = new GateTokenAnnotation(ann);
        GateParsedToken result = new GateParsedToken(gtAnn.getString());
        result.setCategory(gtAnn.getCategory());
        result.setKind(gtAnn.getKind());
        result.setRoot(gtAnn.getRoot());
        return result;
    }
}
