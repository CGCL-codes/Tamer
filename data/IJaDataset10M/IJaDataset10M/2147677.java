package raptor.chess;

import raptor.chess.pgn.PgnHeader;

/**
 * An implementation of a bughouse game. This approach involves linking two
 * bughouse games together. And setting the others droppable piece counts as
 * pieces are captured.
 * 
 * 
 * NOTE: the xoring for the zobrist is broken. It would need to be fixed to rely
 * on that for a computer program. Also this wont work for bgpn without some
 * changes.
 */
public class BughouseGame extends CrazyhouseGame {

    protected BughouseGame otherBoard;

    public BughouseGame() {
        super();
        setHeader(PgnHeader.Variant, Variant.bughouse.name());
    }

    /**
	 * @param ignoreHashes
	 *            Whether to include copying hash tables.
	 * @return An deep clone copy of this Game object.
	 */
    @Override
    public BughouseGame deepCopy(boolean ignoreHashes) {
        BughouseGame result = new BughouseGame();
        overwrite(result, ignoreHashes);
        return result;
    }

    public BughouseGame getOtherBoard() {
        return otherBoard;
    }

    public void setOtherBoard(BughouseGame bughouseGame) {
        otherBoard = bughouseGame;
    }
}
