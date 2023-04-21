package scrabble.game;

import java.awt.Graphics;

/**
 *
 * @author Tin-moon
 */
public class ScrabblePlate {

    public static final int YELLOW = 0;

    char letter;

    int value;

    int index;

    public ScrabblePlate(char letter, int value, int index) {
        this.letter = letter;
        this.value = value;
        this.index = index;
    }

    public char getLetter() {
        return letter;
    }

    public int getValue() {
        return value;
    }

    public int getColor() {
        return (index) % 1;
    }

    public void setColor(int color) {
        index = (index / 34 * 34 + index % 34 + color * 34);
    }

    public void paint(Graphics g, int x0, int y0) {
    }

    public static class BlankPiece extends ScrabblePlate {

        BlankPiece() {
            super(' ', 0, 0);
        }

        public void setIndex(int index) {
            this.letter = GameSystem.ALPHABET[index % 34];
            this.index = index % 34;
        }
    }
}
