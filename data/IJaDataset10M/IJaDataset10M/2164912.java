package bill.apps.wordutil;

import java.io.*;
import java.util.*;

/**
 * This class is used to contain information about a letter that is available
 * to be a part of a word.
 *
 * @author      Bill Huebsch
 * @version     0.1
 *
 */
public class Letter {

    protected int _lastLetter;

    private boolean _used;

    private int _index;

    private char _char;

    /**
    * The creator. Sets class variables to and specified values.
    *
    * @param character The character represented by this letter
    * @param index The index (0 based) of the letter in the list of letters
    */
    public Letter(char character, int index) {
        _char = character;
        _index = index;
        _lastLetter = -1;
        _used = false;
    }

    /**
    * Sets the value of a letter to used or not used in the current word being
    * analyzed.
    *
    * @param used <code>true</code> indicates it has been used, <code>false
    * </code> it has not
    */
    public void setUsed(boolean used) {
        _used = used;
    }

    /**
    * Indicates if the cube has been used in the current word.
    *
    * @return <code>true</code> if the cubse has been used, <code>false</code>
    * otherwise
    */
    public boolean isUsed() {
        return _used;
    }

    /**
    * Indicates the index of the letter in the letter list (0 based).
    *
    * @return The letter's index.
    */
    public int getIndex() {
        return _index;
    }

    /**
    * Indicates the character displayed on the letter.
    *
    * @return The letter's character
    */
    public char getCharacter() {
        return _char;
    }

    /**
    * Determines if two Letter objects are the equal. In our case equal means
    * the represent the same character.
    *
    * @param 0 The Letter object to compare this one to.
    * @return Returns <code>true</code> if the Letter objects are equal,
    * otherwise returns <code>false</code>.
    */
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof Letter)) return false;
        char otherChar = ((Letter) o).getCharacter();
        if (otherChar == _char) return true;
        return false;
    }

    public String toString() {
        return "" + _char;
    }
}
