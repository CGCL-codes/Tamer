package net.sf.jeckit.impl.context;

import java.util.logging.Level;
import net.sf.jeckit.common.ProjectProperties;
import net.sf.jeckit.common.Sentence;
import net.sf.jeckit.common.Token;
import net.sf.jeckit.interfaces.TokenChecker;
import net.sf.jeckit.interfaces.TokenInterface.Flag;
import net.sf.jeckit.resources.Dictionary;
import net.sf.jeckit.verifying.VerifyingException;

public class StubTokenChecker implements TokenChecker {

    @Override
    public void checkTokens(Sentence sentence) throws VerifyingException {
        int i = 0;
        for (Token tok : sentence) {
            if ((!tok.getContent().isEmpty()) && (!Utilities.isWhitespace(tok.getContent()))) {
                i++;
                if (i == 4) {
                    tok.setErrorProbability(1.00d);
                }
            }
        }
    }
}
