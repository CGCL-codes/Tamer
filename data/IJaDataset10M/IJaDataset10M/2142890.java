package org.spirit.servlet.bean;

import java.io.Serializable;

/**
 * This is class is used by botverse.
 * @author Berlin Brown
 *
 */
public class BotListConcatValue implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4007269620924320835L;

    public static final int DEFAULT_MAX_LEN = 10;

    /**
	 * Synomous with value
	 */
    private String word;

    private int maxLen = -1;

    /**
	 * @return the maxLen
	 */
    public int getMaxLen() {
        if (maxLen <= 0) {
            maxLen = DEFAULT_MAX_LEN;
        }
        return maxLen;
    }

    /**
	 * @param maxLen the maxLen to set
	 */
    public void setMaxLen(int maxLen) {
        this.maxLen = maxLen;
    }

    /**
	 * @return the word
	 */
    public String getWord() {
        if (word != null && (word.length() > (maxLen + 3))) {
            word = word.substring(0, (maxLen - 1)) + "...";
        }
        return word;
    }

    public static final String getMaxWord(String value, Integer curMaxLenI) {
        int curMaxLen = curMaxLenI.intValue();
        String curWord = value;
        if (value != null && (value.length() > (curMaxLen + 3))) {
            curWord = value.substring(0, (curMaxLen - 1)) + "...";
        }
        return curWord;
    }

    /**
	 * @param word the word to set
	 */
    public void setWord(String word) {
        this.word = word;
    }
}
