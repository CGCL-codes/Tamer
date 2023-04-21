package opennlp.ccg.parse.supertagger.util;

import java.util.List;
import opennlp.ccg.lexicon.DefaultTokenizer;
import opennlp.ccg.lexicon.Word;
import opennlp.ccg.util.Pair;

/**
 * @author Dennis N. Mehay
 * @version $Revision: 1.1 $, $Date: 2009/08/21 17:20:20 $
 */
public class PipedTokenizer extends DefaultTokenizer {

    public PipedTokenizer() {
        super();
    }

    @Override
    public Word parseToken(String token, boolean strictFactors) {
        String form = token;
        String stem = null;
        String POS = null;
        String pitchAccent = null;
        String supertag = null;
        String semClass = null;
        List<Pair<String, String>> attrValPairs = null;
        int pipePos = token.indexOf('|');
        String suffix = null;
        if (pipePos > 0) {
            form = token.substring(0, pipePos);
            suffix = token.substring(pipePos + 1);
            pipePos = suffix.indexOf('|');
            stem = suffix.substring(0, pipePos);
            if (stem.equals("")) {
                stem = null;
            }
            suffix = suffix.substring(pipePos + 1);
            pipePos = suffix.indexOf('|');
            POS = suffix.substring(0, pipePos);
            suffix = suffix.substring(pipePos + 1);
            if (suffix != null && !suffix.equals("")) {
                supertag = suffix.trim();
            }
        } else {
            throw new RuntimeException("This file is not in the right format: \n" + "form|lemma|POS|(Supertag) ... form|lemma|POS(Supertag).");
        }
        return Word.createWord(form, pitchAccent, attrValPairs, stem, POS, supertag, semClass);
    }
}
