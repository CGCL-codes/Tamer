package uk.ac.shef.wit.simmetrics.tokenisers;

import uk.ac.shef.wit.simmetrics.wordhandlers.InterfaceTermHandler;
import uk.ac.shef.wit.simmetrics.wordhandlers.DummyStopTermHandler;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.io.Serializable;

/**
 * Package: uk.ac.shef.wit.simmetrics.tokenisers Description: TokeniserQGram2
 * implements a QGram Tokeniser.
 * 
 * Date: 25-Nov-2006 Time: 14:07:24
 * 
 * @author Sam Chapman <a href="http://www.dcs.shef.ac.uk/~sam/">Website</a>, <a
 *         href="mailto:sam@dcs.shef.ac.uk">Email</a>.
 * @version 1.1
 */
public final class TokeniserQGram2 implements InterfaceTokeniser, Serializable {

    /**
	 * stopWordHandler used by the tokenisation.
	 */
    private InterfaceTermHandler stopWordHandler = new DummyStopTermHandler();

    /**
	 * displays the tokenisation method.
	 * 
	 * @return the tokenisation method
	 */
    public final String getShortDescriptionString() {
        return "TokeniserQGram2";
    }

    /**
	 * gets the stop word handler used.
	 * 
	 * @return the stop word handler used
	 */
    public InterfaceTermHandler getStopWordHandler() {
        return stopWordHandler;
    }

    /**
	 * sets the stop word handler used with the handler given.
	 * 
	 * @param stopWordHandler
	 *            the given stop word hanlder
	 */
    public void setStopWordHandler(final InterfaceTermHandler stopWordHandler) {
        this.stopWordHandler = stopWordHandler;
    }

    /**
	 * displays the delimiters used - ie none.
	 * 
	 * @return the delimiters used - i.e. none ""
	 */
    public final String getDelimiters() {
        return "";
    }

    /**
	 * Return tokenized version of a string.
	 * 
	 * @param input
	 * @return tokenized version of a string
	 */
    public final ArrayList<String> tokenizeToArrayList(final String input) {
        final ArrayList<String> returnArrayList = new ArrayList<String>();
        int curPos = 0;
        final int length = input.length() - 1;
        while (curPos < length) {
            final String term = input.substring(curPos, curPos + 2);
            if (!stopWordHandler.isWord(term)) {
                returnArrayList.add(term);
            }
            curPos++;
        }
        return returnArrayList;
    }

    /**
	 * Return tokenized set of a string.
	 * 
	 * @param input
	 * @return tokenized version of a string as a set
	 */
    public Set<String> tokenizeToSet(final String input) {
        final Set<String> returnSet = new HashSet<String>();
        returnSet.addAll(tokenizeToArrayList(input));
        return returnSet;
    }
}
