package uk.co.ordnancesurvey.rabbitparser.metadata.impl;

import java.util.Collections;
import java.util.Set;
import uk.co.ordnancesurvey.rabbitparser.metadata.IKeyphraseDefinition;
import uk.co.ordnancesurvey.rabbitparser.metadata.IParsedKeywordType;
import uk.co.ordnancesurvey.rabbitparser.metadata.IParsedPartType;
import uk.co.ordnancesurvey.rabbitparser.metadata.impl.base.BaseParsedPartDefinition;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.RbtPartType;

/**
 * Default implementation of {@link IKeyphraseDefinition}
 * 
 * @author rdenaux
 * 
 */
public class KeyphraseDefinition extends BaseParsedPartDefinition implements IKeyphraseDefinition {

    private IParsedKeywordType kwType;

    public KeyphraseDefinition(IParsedKeywordType aKwType) {
        assert aKwType != null;
        kwType = aKwType;
        setType(RbtPartType.Keyphrase);
    }

    public IParsedKeywordType getKeywordType() {
        return kwType;
    }

    public String getAsBNF() {
        return kwType.toString();
    }

    public Set<IParsedPartType> getSubParts() {
        return Collections.emptySet();
    }

    @Override
    public final IParsedPartType getPartType() {
        IParsedPartType result = super.getPartType();
        assert RbtPartType.Keyphrase.equals(result);
        return result;
    }
}
