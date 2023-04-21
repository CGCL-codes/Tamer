package net.sf.dragonchess.game.piece;

public class Warrior extends AbstractPieceType implements Promotable {

    /**
	 * @inheritDoc
	 * 
	 * <p>Warriors can be promoted to Heros.</p>
	 */
    public PieceType getPromoted() {
        return PieceTypes.HERO;
    }
}
