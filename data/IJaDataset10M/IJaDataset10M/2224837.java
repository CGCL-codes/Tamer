package uk.co.ordnancesurvey.rabbitparser.gate.jape.findverbphrase;

import java.util.ArrayList;
import java.util.List;
import uk.co.ordnancesurvey.rabbitparser.gate.jape.AddSingleAnnotationOutputCallBack;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.entity.IParsedRelationshipCandidate;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.entity.IParsedToken;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.impl.entity.ParsedEntityCandidate;

public class JapeCB_ComparativeRelationWithoutCopulaVerb extends AddSingleAnnotationOutputCallBack<IParsedRelationshipCandidate> {

    public JapeCB_ComparativeRelationWithoutCopulaVerb() {
        super(IParsedRelationshipCandidate.class);
    }

    @Override
    protected IParsedRelationshipCandidate retrieveFoundPart() {
        List<IParsedToken> toks = new ArrayList<IParsedToken>();
        addTokens(toks, getRequiredAtLeastOneRbtAnn("comparativeAdjective"));
        addTokens(toks, getRequiredAtLeastOneRbtAnn("than"));
        ParsedEntityCandidate result = new ParsedEntityCandidate(toks);
        result.setMissingCopulaVerb(true);
        return result;
    }
}
