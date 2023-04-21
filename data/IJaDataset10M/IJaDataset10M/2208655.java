package net.sf.jeckit.resources;

/**
 * This class encapsulates a word from the dictionary.
 * This class is suppossed to also contain pos-tags, frequencies etc. of this word in the long run.
 * 
 * @author Martin Schierle
 *
 */
public class DictionaryWord {

    /** The word itself, as written in the dictionary */
    private String word;

    /** The phonetic code of this word */
    private String phonetic_code;

    /** The frequency of this word with respect to the reference corpora */
    private int frequency;

    /**
	 * Will create a new DictionaryWord
	 * @param word The word from the dictionary
	 * @param phonetic_code The phonetic code of the word
	 * @param frequency The frequency of this word
	 */
    public DictionaryWord(String word, String phonetic_code, int frequency) {
        this.word = word;
        this.phonetic_code = phonetic_code;
        this.frequency = frequency;
    }

    /**
	 * @return the word
	 */
    public String getWord() {
        return word;
    }

    /**
	 * @param word the word to set
	 */
    public void setWord(String word) {
        this.word = word;
    }

    /**
	 * @return the phonetic_code
	 */
    public String getPhonetic_code() {
        return phonetic_code;
    }

    /**
	 * @param phonetic_code the phonetic_code to set
	 */
    public void setPhonetic_code(String phonetic_code) {
        this.phonetic_code = phonetic_code;
    }

    /**
	 * @return the frequency
	 */
    public int getFrequency() {
        return frequency;
    }

    /**
	 * @param frequency the frequency to set
	 */
    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }
}
