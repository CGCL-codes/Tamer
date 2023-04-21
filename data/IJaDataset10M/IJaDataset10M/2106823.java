package javax.accessibility;

import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.text.AttributeSet;

/**
 * Objects which present textual information on the display should implement
 * this interface.  Accessibility software can use the implementations of
 * this interface to change the attributes and spacial location of the text.
 *
 * <p>The <code>AccessibleContext.getAccessibleText()</code> method
 * should return <code>null</code> if an object does not implement this
 * interface.
 *
 * @author Eric Blake (ebb9@email.byu.edu)
 * @see Accessible
 * @see AccessibleContext
 * @see AccessibleContext#getAccessibleText()
 * @since 1.2
 * @status updated to 1.4
 */
public interface AccessibleText {

    /**
   * Constant designating that the next selection should be a character.
   *
   * @see #getAtIndex(int, int)
   * @see #getAfterIndex(int, int)
   * @see #getBeforeIndex(int, int)
   */
    int CHARACTER = 1;

    /**
   * Constant designating that the next selection should be a word.
   *
   * @see #getAtIndex(int, int)
   * @see #getAfterIndex(int, int)
   * @see #getBeforeIndex(int, int)
   */
    int WORD = 2;

    /**
   * Constant designating that the next selection should be a sentence.
   *
   * @see #getAtIndex(int, int)
   * @see #getAfterIndex(int, int)
   * @see #getBeforeIndex(int, int)
   */
    int SENTENCE = 3;

    /**
   * Given a point in the coordinate system of this object, return the
   * 0-based index of the character at that point, or -1 if there is none.
   *
   * @param p the point to look at
   * @return the character index, or -1
   */
    int getIndexAtPoint(Point point);

    /**
   * Determines the bounding box of the indexed character. Returns an empty
   * rectangle if the index is out of bounds.
   *
   * @param index the 0-based character index
   * @return the bounding box, may be empty
   */
    Rectangle getCharacterBounds(int index);

    /**
   * Return the number of characters.
   *
   * @return the character count
   */
    int getCharCount();

    /**
   * Return the offset of the character. The offset matches the index of the
   * character to the right, since the carat lies between characters.
   *
   * @return the 0-based caret position
   */
    int getCaretPosition();

    /**
   * Returns the section of text at the index, or null if the index or part
   * is invalid.
   *
   * @param part {@link #CHARACTER}, {@link #WORD}, or {@link #SENTENCE}
   * @param index the 0-based character index
   * @return the selection of text at that index, or null
   */
    String getAtIndex(int part, int index);

    /**
   * Returns the section of text after the index, or null if the index or part
   * is invalid.
   *
   * @param part {@link #CHARACTER}, {@link #WORD}, or {@link #SENTENCE}
   * @param index the 0-based character index
   * @return the selection of text after that index, or null
   */
    String getAfterIndex(int part, int index);

    /**
   * Returns the section of text before the index, or null if the index or part
   * is invalid.
   *
   * @param part {@link #CHARACTER}, {@link #WORD}, or {@link #SENTENCE}
   * @param index the 0-based character index
   * @return the selection of text before that index, or null
   */
    String getBeforeIndex(int part, int index);

    /**
   * Returns the attributes of a character at an index, or null if the index
   * is out of bounds.
   *
   * @param index the 0-based character index
   * @return the character's attributes
   */
    AttributeSet getCharacterAttribute(int index);

    /**
   * Returns the start index of the selection. If there is no selection, this
   * is the same as the caret location.
   *
   * @return the 0-based character index of the selection start
   */
    int getSelectionStart();

    /**
   * Returns the end index of the selection. If there is no selection, this
   * is the same as the caret location.
   *
   * @return the 0-based character index of the selection end
   */
    int getSelectionEnd();

    /**
   * Returns the selected text. This may be null or "" if no text is selected.
   *
   * @return the selected text
   */
    String getSelectedText();
}
