package joshua.corpus;

import java.util.ArrayList;

/**
 * This class provides finer-grained abstraction of basic phrases,
 * allowing for multiple implementations of abstract phrase to share
 * as much code as possible.
 *  
 * @author Lane Schwartz
 */
public abstract class AbstractBasicPhrase extends AbstractPhrase {

    public ArrayList<Phrase> getSubPhrases() {
        return this.getSubPhrases(this.size());
    }

    public ArrayList<Phrase> getSubPhrases(int maxLength) {
        ArrayList<Phrase> phrases = new ArrayList<Phrase>();
        int len = this.size();
        for (int n = 1; n <= maxLength; n++) for (int i = 0; i <= len - n; i++) phrases.add(this.subPhrase(i, i + n - 1));
        return phrases;
    }
}
