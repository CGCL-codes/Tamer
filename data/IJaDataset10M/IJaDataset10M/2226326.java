package uk.co.ordnancesurvey.rabbitparser.parsedsentences.actionrule;

import uk.co.ordnancesurvey.rabbitparser.parsedsentences.BaseValidatedParsedSentence;

/**
 * Base class for all action rules
 * 
 * @author rdenaux
 * 
 */
public abstract class BaseParsedSentenceActionRule implements IParsedSentencePartActionRule {

    BaseValidatedParsedSentence sentenceToActOn;

    public BaseParsedSentenceActionRule(BaseValidatedParsedSentence aSentence) {
        sentenceToActOn = aSentence;
    }

    /**
	 * @return the sentenceToActOn
	 */
    protected final BaseValidatedParsedSentence getSentenceToActOn() {
        return sentenceToActOn;
    }
}
