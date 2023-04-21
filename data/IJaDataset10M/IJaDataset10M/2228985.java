package eu.redseeds.sc.query.ui.view.composite.scenariosDiff;

import eu.redseeds.scl.model.rsl.rslrequirementrepresentationsentences.constrainedlanguagesentences.ConstrainedLanguageSentenceDTO;

interface IDiffSentencesPairStrategy {

    /**
	 * This checks the words in the order in which they appear and stop on first similar
	 *
	 * @param currentSvoSentences
	 * @param pastSvoSentences
	 * @return ISVOSentencesMatch if there is at least one common word or null
	 *         if not
	 */
    public IConstrainedLanguageSentenceMatch diffSentences(ConstrainedLanguageSentenceDTO currentSentences, ConstrainedLanguageSentenceDTO pastSentences);
}
