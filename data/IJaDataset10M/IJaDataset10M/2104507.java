package MobileBG.board;

import java.util.Vector;

/**
 * A <code>Vector</code> that can contain only valid moves.
 *
 * @author ttmh
 */
public class ValidMovesVector extends Vector {

    /** Number of dices used. */
    private byte nbrOfDices;

    /** <code>true</code> if the high dice is used, <code>false</code> otherwise. */
    private boolean highDiceUsed;

    /**
	 * Constructor for <code>ValidMovesVector</code>.
	 * @param size Initial size of vector.
	 */
    public ValidMovesVector(int size) {
        super(size);
    }

    /**
	 * Adds a <code>Move</code> to the vector.
	 * @param m <code>Move</code> to add.
	 * @param dices Dices used for generating this move.
	 * @param b <code>Board</code> used for generating this move.
	 */
    public synchronized void addElement(Move m, byte[] dices, Board b) {
        byte usedDices = m.getNbrOfMoves();
        if (usedDices < nbrOfDices) {
            return;
        }
        byte pos = (byte) (b.getMovingPlayer() == 1 ? 25 : 0);
        if (b.getCheckers(pos) != 0) {
            byte tmp = (byte) Math.abs(b.getCheckers(pos));
            boolean otherMove = false;
            for (byte i = 0; i < m.getNbrOfMoves() && tmp != 0; i++) {
                if (m.getMoves()[i << 1] == pos) {
                    tmp--;
                } else {
                    otherMove = true;
                }
            }
            if (tmp != 0 && otherMove) {
                return;
            }
        }
        if (usedDices > nbrOfDices) {
            removeAllElements();
            nbrOfDices = usedDices;
        }
        if (usedDices == 1 && dices[0] != dices[1]) {
            byte lowDice = (byte) Math.min(dices[0], dices[1]);
            byte usedDice = (byte) Math.abs(m.getMoves()[0] - m.getMoves()[1]);
            if (usedDice == lowDice && highDiceUsed) {
                return;
            }
            if (usedDice > lowDice && !highDiceUsed) {
                highDiceUsed = true;
                removeAllElements();
            }
        }
        addElement(m);
    }
}
