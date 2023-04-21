package pdp.scrabble.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import pdp.scrabble.Externalizable;
import static java.awt.Font.SANS_SERIF;
import static java.awt.Font.BOLD;

/** Letter structure, containing its car, value, and possible operations.
 */
public interface Letter extends Externalizable {

    /** Font used for letter draws. */
    public static final Font FONT_LETTER = new Font(SANS_SERIF, BOLD, 16);

    /** Font used for letter value draws. */
    public static final Font FONT_VALUE = new Font(SANS_SERIF, BOLD, 9);

    /** Normal case color. */
    public static final Color BACKGROUND_COLOR = new Color(228, 202, 132);

    /** Render letter at specified location.
     * @param g graphics output.
     * @param x location x.
     * @param y location y.
     */
    public void render(Graphics g, int x, int y);

    /** Clear letter. */
    public void clear();

    /** Set joker char (character that will replace the joker).
     * @param name character.
     */
    public void setJokerChar(char name);

    /** Get joker char.
     * @return joker char.
     */
    public char getJokerChar();

    /** Set letter name, just for God Mod.
     * @param name char.
     */
    public void setName(char name);

    /** Get letter name.
     * @return letter name.
     */
    public char getName();

    /** Set letter value.
     * @param value letter value.
     */
    public void setValue(int value);

    /** Get letter value.
     * @return letter value.
     */
    public int getValue();

    /** Get ID.
     * @return letter ID;
     */
    public int getID();

    /** Set letter color.
     * @param color letter color.
     */
    public void setColor(Color color);

    /** Check if two letters are equals.
     * @param letter letter to compare.
     * @return true if equals, false else.
     */
    public boolean equals(Letter letter);

    /** Clone letter.
     * @return cloned letter.
     */
    public Letter clone();

    public String toString();
}
