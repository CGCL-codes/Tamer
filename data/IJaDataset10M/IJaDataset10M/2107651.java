package org.zmpp.vm;

import java.util.List;

/**
 * This interface is used from CommandHistory to manipulate the input line.
 * 
 * @author Wei-ju Wu
 * @version 1.5
 */
public interface InputLine {

    /**
   * Deletes the previous character in the input line.
   * 
   * @param inputbuffer the input buffer
   * @param pointer the pointer
   * @return the new pointer after delete
   */
    int deletePreviousChar(List<Character> inputbuffer, int pointer);

    /**
   * Adds a character to the current input line.
   * 
   * @param inputbuffer the input buffer
   * @param textbuffer the textbuffer address
   * @param pointer the pointer address
   * @param zsciiChar the character to add
   * @return the new pointer
   */
    int addChar(List<Character> inputbuffer, int textbuffer, int pointer, char zsciiChar);
}
