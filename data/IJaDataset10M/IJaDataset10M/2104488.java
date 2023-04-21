package za.org.meraka.dictionarymaker.model;

/**
 * DictionaryNotificationListener
 * 
 * An interface for listeners who want to be notified about changes to a 
 * dictionary
 *
 * @author Thomas Fogwill <tfogwill@users.sourceforge.net>
 * @date	   Mar 13, 2006
 */
public interface DictionaryNotificationListener {

    /**
     * Notification when the status of a word in the dictionary changes.
     * 
     * @param dictionary The dictionary
     * @param word The word 
     * @param oldStatus The old status
     * @param newStatus The new status
     */
    public void wordStatusChanged(Dictionary dictionary, WordData word, int oldStatus, int newStatus);

    /**
     * Notification that words were added to the dictionary.
     * @param dictionary The dictionary to which they were added 
     * @param words The words added
     */
    public void wordsAdded(Dictionary dictionary, WordData[] words);

    /**
     * Notification that words were removed from the dictionary.
     * @param dictionary The dictionary from which they were removed
     * @param words The words removed
     */
    public void wordsRemoved(Dictionary dictionary, WordData[] words);

    /**
     * Notification that phonemes were added to the dictionary.
     * @param dictionary The dictionary to which they were added 
     * @param phonemes The phonemes added
     */
    public void phonemesAdded(Dictionary dictionary, Phoneme[] phonemes);

    /**
     * Notification that phonemes were removed from the dictionary.
     * @param dictionary The dictionary from which they were removed 
     * @param phonemes The phonemes removed
     */
    public void phonemesRemoved(Dictionary dictionary, Phoneme[] phonemes);

    /**
     * Notification that phonemes were renamed in the dictionary.
     * @param dictionary The dictionary  
     * @param phonemes The phonemes renamed
     */
    public void phonemesRenamed(Dictionary dictionary, Phoneme[] phonemes);

    /**
     * Notification that graphemes were added to the dictionary.
     * @param dictionary The dictionary to which they were added 
     * @param graphemes The graphemes added
     */
    public void graphemesAdded(Dictionary dictionary, String[] graphemes);

    /**
     * Notification that graphemes were removed from the dictionary.
     * @param dictionary The dictionary to which they were removed 
     * @param graphemes The graphemes removed
     */
    public void graphemesRemoved(Dictionary dictionary, String[] graphemes);

    /**
     * Notification that the rules were updated (fully, in batch mode) for 
     * this dictionary.
     * 
     * @param dictionary The dictionary
     */
    public void rulesBatchUpdated(Dictionary dictionary);
}
