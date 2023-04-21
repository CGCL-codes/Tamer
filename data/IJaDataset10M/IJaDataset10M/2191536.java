package lojban.tokenizer;

import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;
import lojban.tokenizer.valsi.NullValsi;
import lojban.tokenizer.valsi.Valsi;

public class LojbanTokenizer {

    /**
	 * StringTokenizer to have an easy way to break up according to spaces, after
	 * which further analysis can be done.
	 */
    private StringTokenizer tok;

    /**
	 * Allows client code to configure some words that should never appear in the
	 * output.
	 */
    private HashSet<Valsi> skipWords;

    /**
	 * The current <code>Valsi</code> instance. <code>nextWord()</code> is always
	 * busy returning information from this object. When it runs out of words to
	 * return, it replaces it with the next Valsi instance.
	 */
    private Valsi valsi = null;

    /**
	 * Used to store iterator into <code>valsi.getPagbu()</code> across calls to
	 * <code>nextWord()</code>.
	 */
    private Iterator<Valsi> pagbu_iter = null;

    /**
	 * Initializes the class.
	 * 
	 * @param src
	 *           The string to be broken into separate words.
	 * @param skipWords
	 *           A set of words that should not appear in output. Can be set to
	 *           null or an empty HashSet if no words should be skipped. This set
	 *           should never contain an instance of <code>NullValsi</code> (this
	 *           could cause an infinite loop) - if it does, it will be removed.
	 * @throws TokenizerFailure
	 */
    public LojbanTokenizer(String src, HashSet<Valsi> skipWords) {
        this.tok = new StringTokenizer(src, " .");
        if (skipWords != null) {
            Valsi nullv = new NullValsi();
            if (skipWords.contains(nullv)) {
                skipWords.remove(nullv);
            }
            this.skipWords = skipWords;
        } else {
            this.skipWords = new HashSet<Valsi>();
        }
    }

    /**
	 * Initializes the class such that it will not skip any words.
	 * 
	 * @param src
	 *           The string to be broken into separate words.
	 * @throws TokenizerFailure
	 */
    public LojbanTokenizer(String src) {
        this(src, null);
    }

    /**
	 * This method returns the next word from the string passed to the
	 * constructor. Successive calls to this method will return all the words in
	 * the string one by one.
	 * <p>
	 * If your application is time-sensitive (such as in the case of speech
	 * generation / recognition), you need to take into account that the bulk of
	 * the tokenizer's processing happens here - however, this processing is
	 * amortized over the entire string.
	 * 
	 * @return The next word from the string given to the constructor. An
	 *         instance of <code>NullValsi</code> when there are no more words to
	 *         return.
	 * @throws TokenizerFailure
	 */
    public Valsi nextWord() throws TokenizerFailure {
        Valsi v = getNextWord();
        while (skipWords.contains(v)) {
            v = getNextWord();
        }
        return v;
    }

    private Valsi getNextWord() throws TokenizerFailure {
        if (this.valsi == null) {
            if (tok.hasMoreTokens()) {
                this.valsi = Valsi.analyze(tok.nextToken());
                this.pagbu_iter = valsi.getPagbu().iterator();
            } else {
                return new NullValsi();
            }
        }
        Valsi to_return = null;
        switch(valsi.getKlesi()) {
            case CMAVO:
                if (pagbu_iter != null) {
                    to_return = pagbu_iter.next();
                    if (!pagbu_iter.hasNext()) {
                        doNextToken();
                    }
                }
                break;
            case LUJVO:
            case GISMU:
            case FUhIVLA:
            case CMENE:
            case NALSANJI:
            default:
                to_return = this.valsi;
                doNextToken();
                break;
        }
        if (to_return == null) {
            to_return = new NullValsi();
        }
        return to_return;
    }

    /**
	 * Signal that on the next call to <code>nextWord()</code>, the next token should be analyzed
	 */
    private void doNextToken() {
        valsi = null;
    }
}
