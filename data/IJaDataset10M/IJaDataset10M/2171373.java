package uk.ac.shef.wit.simmetrics.tokenisers;

import java.util.ArrayList;
import java.util.Set;
import uk.ac.shef.wit.simmetrics.wordhandler.InterfaceTermHandler;

/**
 * Package: uk.ac.shef.wit.simmetrics.api Description: InterfaceTokeniser
 * interface for a Tokeniser class. Date: 31-Mar-2004 Time: 15:09:09
 * 
 * @author Sam Chapman <a href="http://www.dcs.shef.ac.uk/~sam/">Website</a>,
 *         <a href="mailto:sam@dcs.shef.ac.uk">Email</a>.
 * @version 1.1
 */
public interface InterfaceTokeniser {

    /**
	 * displays the tokenisation method.
	 * 
	 * @return short description string
	 */
    public String getShortDescriptionString();

    /**
	 * displays the delimitors used - (if applicable).
	 * 
	 * @return string for the delimitors used - (if applicable) "" otherwise
	 */
    public String getDelimiters();

    /**
	 * gets the stop word handler used.
	 * 
	 * @return the stop word handler used
	 */
    public InterfaceTermHandler getStopWordHandler();

    /**
	 * sets the stop word handler used with the handler given.
	 * 
	 * @param stopWordHandler
	 *            the given stop word hanlder
	 */
    public void setStopWordHandler(InterfaceTermHandler stopWordHandler);

    /**
	 * Return tokenized version of a string as an ArrayList.
	 * 
	 * @param input
	 * 
	 * @return ArrayList tokenized version of a string
	 */
    public ArrayList<String> tokenizeToArrayList(String input);

    /**
	 * Return tokenized version of a string as a set.
	 * 
	 * @param input
	 * 
	 * @return tokenized version of a string as a set
	 */
    public Set<String> tokenizeToSet(String input);
}
