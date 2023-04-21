package edu.uw.tcss558.team1.client;

import edu.uw.tcss558.team1.server.Connector;
import java.util.List;

/**
 * Defines an interface on which methods the Client can use to access the Server.
 */
public abstract class Client extends Connector {

    /**
     * Check if a certain word is misspelled.
     * @param aWord The word to check if it is misspelled.
     * @return TRUE if it is misspelled, FALSE if it is not misspelled and NULL
     *         if there a problem.
     */
    public abstract Boolean isWordMisspelled(String aWord);

    /**
     * Get a list of suggestions for a given word.
     * @param aWord The word to get suggestions for.
     * @return a list of strings, each string is a suggestion for the word.
     */
    public abstract List<String> getSuggestions(String aWord);

    public abstract Boolean isLoginOk(String aUsername, String aPassword);

    /**
     * Get a list of history.
     * @param aWord The word to get suggestions for.
     * @return a list of strings, each string is a suggestion for the word.
     */
    public abstract List<String> getHistorys();
}
