package net.sf.jeckit.verifying;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jeckit.common.ProjectProperties;
import net.sf.jeckit.common.Sentence;
import net.sf.jeckit.common.Token;
import net.sf.jeckit.interfaces.TokenChecker;
import net.sf.jeckit.interfaces.TokenInterface;

public class VerifyingTokenChecker_icu_stanford implements TokenChecker {

    private Logger logger;

    private NamedEntitiesGuesserInterface stanford;

    private ICULanguageGuesser icu;

    public VerifyingTokenChecker_icu_stanford() throws VerifyingException {
        this.logger = ProjectProperties.getInstance().getLogger();
        this.icu = new ICULanguageGuesser();
        try {
            this.stanford = new StanfordNEGuesser();
        } catch (VerifyingException e) {
            this.logger.log(Level.WARNING, "couldn't initialize the stanford ne guesser", e);
            throw e;
        }
    }

    @SuppressWarnings("unchecked")
    public void checkTokens(Sentence sentence) throws VerifyingException {
        Set<String> ne_set = this.stanford.recognizeNamedEntities(sentence.toString(), sentence.getLocale());
        for (Token token : sentence) {
            if (!this.icu.checkLanguage(token.getContent(), token.getLocale())) {
                token.setFlag(TokenInterface.Flag.WRONG_LANGUAGE);
                this.logger.log(Level.INFO, "Detected different language for token '" + token.getContent() + "'");
            }
            if (ne_set.contains(token.getContent())) {
                token.setFlag(TokenInterface.Flag.NE);
            }
        }
    }
}
