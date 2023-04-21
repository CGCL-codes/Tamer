package zen.scrabbledict.data;

import java.io.Serializable;

/**
 * The SerializableBoard is used to serialize a board with minimal overhead.
 * @author flszen
 */
public class SerializableBoard implements Serializable {

    private static final long serialVersionUID = -2083081293857892L;

    char letters_[][];

    boolean wild_[][];

    private Tiles tiles;

    private BoardDesign boardDesign;

    private MultiplayerInformation multiplayerInformation;

    /**
     * Creates a new SerializableBoard based on an AbstractBoard.
     * @param board The AbstractBoard to load data from.
     */
    public SerializableBoard(AbstractBoard board) {
        letters_ = new char[board.Width][board.Height];
        wild_ = new boolean[board.Width][board.Height];
        for (int x = 0; x < board.Width; x++) {
            letters_[x] = board.letters[x].clone();
            wild_[x] = board.wild[x].clone();
        }
        multiplayerInformation = board.getMultiplayerInformation();
        tiles = board.getTiles();
        boardDesign = board.getDesign();
    }

    /**
     * Gets an instance of this board as an AbstractBoard.
     * @return An AbstractBoard representation of this board.
     */
    public AbstractBoard getAbstractBoard() {
        AbstractBoard board = new AbstractBoard(tiles, boardDesign, multiplayerInformation) {

            @Override
            public AbstractBoard cloneExtension() {
                throw new UnsupportedOperationException("This operation will never be supported.");
            }

            @Override
            protected void fireUpdatedLetterEvent(int x, int y) {
                throw new UnsupportedOperationException("This operation will never be supported.");
            }
        };
        for (int x = 0; x < board.Width; x++) {
            board.letters[x] = this.letters_[x].clone();
            board.wild[x] = this.wild_[x].clone();
        }
        return board;
    }
}
