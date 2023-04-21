package uk.co.ordnancesurvey.rabbitparser.gate.jape.urlfinder;

import java.util.ArrayList;
import java.util.List;
import uk.co.ordnancesurvey.rabbitparser.gate.annotation.RbtAnnotation;
import uk.co.ordnancesurvey.rabbitparser.gate.jape.AddSingleAnnotationOutputCallBack;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.entity.IParsedToken;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.impl.importsbasic.ParsedURL;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.importsbasic.IParsedURL;

/**
 * Handles the finding of a URL pattern
 * 
 * @author rdenaux
 * 
 */
public class JapeCB_UrlPattern extends AddSingleAnnotationOutputCallBack<IParsedURL> {

    public JapeCB_UrlPattern() {
        super(IParsedURL.class);
    }

    @Override
    protected IParsedURL retrieveFoundPart() {
        List<IParsedToken> urlTokens = new ArrayList<IParsedToken>();
        addOptionalToken(urlTokens, "protocol");
        addOptionalToken(urlTokens, "colon");
        addOptionalToken(urlTokens, "slash1");
        addOptionalToken(urlTokens, "slash2");
        addOptionalToken(urlTokens, "www");
        addOptionalToken(urlTokens, "dot");
        for (RbtAnnotation ann : getListOfOptionalRbtAnn("urlBody")) {
            urlTokens.add(ann.getParsedToken());
        }
        assert !urlTokens.isEmpty();
        return new ParsedURL(urlTokens);
    }

    private void addOptionalToken(List<IParsedToken> result, String aBinding) {
        IParsedToken tok = getOptionalPart(aBinding, IParsedToken.class);
        if (tok != null) {
            result.add(tok);
        }
    }
}
