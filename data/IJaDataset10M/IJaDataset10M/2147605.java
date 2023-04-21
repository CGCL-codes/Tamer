package cej.jhebrew.game;

/**
 * @author cjung
 * Generic interface for a vocabulary game
 */
public interface VocabularyGame {

    /**
     * Compares entered value by user, to correct translations of the word.
     * @return true if answer is correct
     */
    public boolean correct();

    /**
     * Prepares next play. 
     */
    public void loadPlay();
}
