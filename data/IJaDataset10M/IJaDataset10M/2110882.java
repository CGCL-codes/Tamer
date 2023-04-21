package game.gamemodes;

import game.*;
import java.util.Vector;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

/**
 * This is the Memory Game Mode, it consits of tiles which the player has to match up.
 * 
 * @author Dirk
 */
public class MemoryGameMode implements IGameMode, GameLayer {

    /**
     * the tiles that mak up the game board.
     */
    private MemoryTile[][] board;

    /**
     * the tile where the cursor currently is.
     */
    private Point selected;

    /**
     * The tile, if any, which has been flipped.
     */
    private Point flipped;

    /**
     * If true then reveal the letetrs on all the tiles, otherwise hide them to make it a game of memory.
     */
    private boolean tilesShown = true;

    /**
     * The color used for the background.
     */
    public static final int backgroundColor = 16777215;

    /**
     * The color used for the tiles.
     */
    public static final int itemColor = 16716049;

    /**
     * The color used for the flipped tile.
     */
    public static final int flippedColor = 187;

    /**
     * The color used for the tile the cursor is currently on.
     */
    public static final int selectedColor = 0;

    /**
     * The height of the tiles.
     */
    private static final int itemHeight = 30;

    /**
     * the border between item boxes and between the item and the surounding box.
     */
    private static final int itemBorder = 5;

    /**
     * The width of the tiles.
     */
    private static final int itemWidth = 30;

    /**
     * the scrollinng layer manager for this game layer.
     */
    private final ScrollingLayerManager scroll;

    /**
     * the Canvas this is painting on.
     */
    private final QuestForWordsGameCanvas canvas;

    /**
     * Restore the constructor from a byte array.
     * @param data the byte array with the save data.
     * @param screenWidth the width of the screen
     * @param screenHeight the height of the screen
     * @param canvas the canvas to draw upon.
     */
    public MemoryGameMode(byte[] data, int screenWidth, int screenHeight, QuestForWordsGameCanvas canvas) {
        this.canvas = canvas;
        int boardSizeX, boardSizeY;
        tilesShown = data[0] == 0;
        boardSizeX = SaveGameHandler.byteArrayToInt(data, 1);
        boardSizeY = SaveGameHandler.byteArrayToInt(data, 5);
        selected = new Point(data, 9);
        flipped = new Point(data, 17);
        if (flipped.equals(-1, -1)) {
            flipped = null;
        }
        board = new MemoryTile[boardSizeX][];
        for (int x = 0; x < boardSizeX; x++) {
            board[x] = new MemoryTile[boardSizeY];
            for (int y = 0; y < boardSizeY; y++) {
                board[x][y] = new MemoryTile(data, 25 + y * board.length * MemoryTile.datasize + x * MemoryTile.datasize);
                if (board[x][y].isNull()) {
                    board[x][y] = null;
                }
            }
        }
        scroll = new ScrollingLayerManager(this);
        scroll.setViewWindow(0, 0, screenWidth, screenHeight);
    }

    /**
     * Constructs a new MemoryGameMode with the specified number of tiles, at
     * least one of the boardSizes must be an even number.
     * @param boardSizeX the width of the board in tiles..
     * @param boardSizeY the height of the board in tiles.
     * @param hiddenTiles if true only one tile is revealed at a time and the others are hidden.
     * @param screenWidth the width of the screen
     * @param screenHeight the height of the screen
     * @param canvas the canvas to draw upon.
     */
    public MemoryGameMode(int boardSizeX, int boardSizeY, boolean hiddenTiles, int screenWidth, int screenHeight, QuestForWordsGameCanvas canvas) {
        selected = new Point(0, 0);
        flipped = null;
        Dictionary dict = Dictionary.getInstance();
        Vector data = dict.getCharacterPairs((boardSizeX * boardSizeY) / 2);
        this.canvas = canvas;
        board = new MemoryTile[boardSizeX][];
        for (int x = 0; x < boardSizeX; x++) {
            board[x] = new MemoryTile[boardSizeY];
            for (int y = 0; y < boardSizeY; y++) {
                board[x][y] = new MemoryTile((CharacterType) data.firstElement());
                data.removeElementAt(0);
            }
        }
        tilesShown = !hiddenTiles;
        for (int i = boardSizeY * boardSizeX; i > 1; i--) {
            swap(i - 1, dict.getInt(i));
        }
        scroll = new ScrollingLayerManager(this);
        scroll.setViewWindow(0, 0, screenWidth, screenHeight);
    }

    public int sizeX() {
        return 6 * (itemWidth + itemBorder) + itemBorder * 2;
    }

    public int sizeY() {
        return 6 * (itemHeight + itemBorder) + itemBorder * 2;
    }

    /**
     * Swap the position of tiles a and b.
     * the position on the board is calculated like this: [x / unit][x % unit]
     * where x is a or b, this allows us to treat the board as an array.
     * @param a
     * @param b
     */
    private void swap(int a, int b) {
        int unit = board[0].length;
        MemoryTile temp = board[a / unit][a % unit];
        board[a / unit][a % unit] = board[b / unit][b % unit];
        board[b / unit][b % unit] = temp;
    }

    /**
     * Paints the scrolling Layar manager responsible for this game mode.
     * @param g the graphics object to paint on.
     */
    public void paint(Graphics g) {
        scroll.paint(g, 0, 0);
    }

    public void paint(Graphics g, int screenWidth, int screenHeight) {
        g.setColor(backgroundColor);
        g.fillRect(0, 0, sizeX(), sizeY());
        int indent;
        int down;
        g.setColor(itemColor);
        MemoryTile current;
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[x].length; y++) {
                current = board[x][y];
                indent = itemBorder * (x + 1) + itemWidth * x;
                down = itemBorder * (y + 1) + itemHeight * y;
                if (selected.equals(x, y)) {
                    g.setColor(selectedColor);
                    g.drawRoundRect(indent, down, itemWidth, itemHeight, 5, 5);
                } else {
                    g.setColor(itemColor);
                }
                if (current != null) {
                    if (tilesShown) {
                        if (flipped != null && flipped.equals(x, y)) {
                            g.setColor(flippedColor);
                        }
                        g.drawRoundRect(indent, down, itemWidth, itemHeight, 5, 5);
                        g.drawString(current.toString(), indent + 15, down + 20, Graphics.HCENTER | Graphics.BASELINE);
                    } else {
                        if (flipped != null && flipped.equals(x, y)) {
                            g.drawRoundRect(indent, down, itemWidth, itemHeight, 5, 5);
                            g.drawString(current.toString(), indent + 15, down + 20, Graphics.HCENTER | Graphics.BASELINE);
                        } else {
                            g.fillRoundRect(indent, down, itemWidth, itemHeight, 5, 5);
                        }
                    }
                }
            }
        }
    }

    public void checkUserInput(int keys, boolean same) {
        if (same) {
            return;
        }
        if ((keys & QuestForWordsGameCanvas.DOWN_PRESSED) != 0) {
            selected.y++;
            if (selected.y >= board[0].length) {
                selected.y = 0;
            }
        }
        if ((keys & QuestForWordsGameCanvas.UP_PRESSED) != 0) {
            selected.y--;
            if (selected.y < 0) {
                selected.y = board[0].length - 1;
            }
        }
        if ((keys & QuestForWordsGameCanvas.RIGHT_PRESSED) != 0) {
            selected.x++;
            if (selected.x >= board.length) {
                selected.x = 0;
            }
        }
        if ((keys & QuestForWordsGameCanvas.LEFT_PRESSED) != 0) {
            selected.x--;
            if (selected.x < 0) {
                selected.x = board.length - 1;
            }
        }
        if ((keys & QuestForWordsGameCanvas.FIRE_PRESSED) != 0) {
            if (board[selected.x][selected.y] != null) {
                if (flipped == null) {
                    flipped = new Point(selected);
                } else {
                    if ((!selected.equals(flipped)) && board[flipped.x][flipped.y].sameAs(board[selected.x][selected.y])) {
                        board[flipped.x][flipped.y] = null;
                        board[selected.x][selected.y] = null;
                        flipped = null;
                    } else {
                        flipped = new Point(selected);
                    }
                }
            }
        }
    }

    public void verifyGameState() {
        scroll.centerViewWindow(selected.x * (itemWidth + itemBorder), selected.y * (itemHeight + itemBorder));
        if (flipped != null && board[flipped.x][flipped.y] == null) {
            flipped = null;
        }
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[x].length; y++) {
                if (board[x][y] != null) {
                    return;
                }
            }
        }
        canvas.player.gainExperinece(45);
        canvas.player.modifyHP(25);
        canvas.popGameMode();
    }

    /**
     * The tiles used in the memory game.
     */
    private class MemoryTile {

        /**
         * How many bytes are used to save one of these.
         */
        public static final int datasize = 2;

        /**
         * The caharcetr of this tile.
         */
        public CharacterType word;

        /**
         * Create a new memory tile for the specified CharacterType.
         * @param str the charactertype for this tile.
         */
        private MemoryTile(CharacterType str) {
            super();
            word = str;
        }

        /**
         * Restore a Memory tile from a byte array.
         * @param data the byte array to recover from.
         * @param offset the offset where to start recovery.
         */
        private MemoryTile(byte[] data, int offset) {
            super();
            word = new CharacterType(SaveGameHandler.byteArrayToChar(data, offset));
        }

        /**
         * Compares this MemoryTile to another. Returns true if they represent the samy symbol.
         * @param other the memory tile to compare to.
         * @return tre if they have the same character, false otherwise.
         */
        public boolean sameAs(MemoryTile other) {
            return word.equals(other.word);
        }

        /**
         * The font used for this memory tile.
         * @return the font.
         */
        public Font getFont() {
            return word.getFont();
        }

        /**
         * Get the string representation of this tile.
         * @return a string.
         */
        public String toString() {
            return word.getPrintable();
        }

        /**
         * Checks to see if this tile contains the nul unicode character (0x0000).
         * @return true if this is a blank tile.
         */
        public boolean isNull() {
            return word.getPrintable().charAt(0) == (char) 0;
        }

        /**
         * Saves this tile into a byte array.
         * @param data the array to save into.
         * @param offset the offset where to begin saving.
         */
        public void intoByteArray(byte[] data, int offset) {
            SaveGameHandler.charIntoByteArray(word.getPrintable().charAt(0), data, offset);
        }
    }

    public byte getTypeID() {
        return SaveGameHandler.MemoryGameMode_ID;
    }

    /**
     * Returns an array with all the data needed to save this Game Mode.
     * @return
     */
    public byte[] saveStats() {
        byte[] temp = new byte[25 + board.length * board[0].length * MemoryTile.datasize];
        temp[0] = (byte) (tilesShown ? 0 : 1);
        SaveGameHandler.intIntoByteArray(board.length, temp, 1);
        SaveGameHandler.intIntoByteArray(board[0].length, temp, 5);
        selected.intoByteArray(9, temp);
        if (flipped == null) {
            flipped = new Point(-1, -1);
            flipped.intoByteArray(17, temp);
            flipped = null;
        } else {
            flipped.intoByteArray(17, temp);
        }
        for (int x = 0; x < board.length; x++) {
            for (int y = 0; y < board[x].length; y++) {
                if (board[x][y] != null) {
                    board[x][y].intoByteArray(temp, 25 + y * board.length * MemoryTile.datasize + x * MemoryTile.datasize);
                } else {
                    temp[25 + y * board.length * MemoryTile.datasize + x * MemoryTile.datasize] = 0;
                    temp[26 + y * board.length * MemoryTile.datasize + x * MemoryTile.datasize] = 0;
                }
            }
        }
        return temp;
    }
}
