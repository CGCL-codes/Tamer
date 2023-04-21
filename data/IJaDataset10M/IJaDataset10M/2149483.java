package affd.logic;

import java.util.ArrayList;

public interface NameReplacement {

    /**
	 * Sets the String which replaces the founded names.
	 * 
	 * @param newWord The new String with which the names are replaced.
	 */
    public void setReplacementWord(String newWord);

    /**
	 * Replaces all the instances of the given key (name in its basic form).
	 * 
	 * @param wordToReplace The basic form of the name which should be replaced.
	 * @param allEntries The ArrayList which contains the instances of the given name 
	 * in the document.
	 */
    public void replaceAll(String wordToReplace, ArrayList<Token> allEntries);

    /**
	 * Replace the given word as an address.
	 * 
	 * @param key The word in its basic form
	 * @param arrayList The ArrayList which contains all the instances of the given word in the document
	 */
    public void replaceAsAddress(String key, ArrayList<Token> arrayList);

    /**
	 * Checks if the given word is most probably an address.
	 * 
	 * @param lowerCase The word in lower case which is checked for being an address.
	 * @return Boolean value which tells if the word was recognized to most probably be an address.
	 */
    public boolean isAddress(String lowerCase);

    /**
	 * Gets the next replacement word for email address.
	 * 
	 * @return Replacement for email address.
	 */
    public String getEmailReplacement();

    /**
	 * Sets the HfstStarter for the NameReplacement.
	 * 
	 * @param h HfstStarter which should be used by NameReplacement.
	 */
    public void setHfstStarter(HfstStarter h);
}
