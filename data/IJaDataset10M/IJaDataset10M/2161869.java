package net.sourceforge.chesstree.chainlogic;

import java.io.Serializable;
import javax.swing.tree.DefaultMutableTreeNode;
import net.sourceforge.chesstree.chesslogic.BoardState;

/**
 * ChainkLink represents one move in a link of moves. It stores:<br>
 * <li>The board state for the given move</li>
 * <li>The commentary for the given move</li>
 * <li>Which chain link it belongs to</li>
 * <br>
 * It is represented as a node in a tree<br>
 * <br>
 * ChainLink does not know where it belongs in a chain of moves.<br>
 * ChainManager is responsible for this information
 */
public class ChainLink extends DefaultMutableTreeNode implements Serializable {

    /** Which chain this link belongs to */
    int chainNo;

    /**  Commentary for this move, if any */
    String commentary;

    /**  The board state of this link. Contains all the information to recreate this move */
    BoardState boardS;

    public ChainLink(int chainNo, BoardState boardS) {
        this.chainNo = chainNo;
        this.boardS = boardS;
    }

    /** Gets the chain number this link is in as an Integer
	 * @return The chain number this link is in as an Integer
	 */
    public Integer getKey() {
        return new Integer(chainNo);
    }

    /** Gets the board state for this chain link
	 * @return The board state for this chain link
	 */
    public BoardState getBoardS() {
        return boardS;
    }

    /** Gets the chain number this link is in
	 * @return The chain number this link is in
	 */
    public int getChainNo() {
        return chainNo;
    }

    /** Gets the commentary for this move
	 * @return The commentary for this move
	 */
    public String getCommentary() {
        return commentary;
    }

    /** Sets the board state for this link
	 * @param state The new board state 
	 */
    public void setBoardS(BoardState state) {
        boardS = state;
    }

    /** Sets the chain number for this link
	 * @param i The new chain number
	 */
    public void setChainNo(int i) {
        chainNo = i;
    }

    /** Sets the commentary for this link
	 * @param string The new commentary
	 */
    public void setCommentary(String string) {
        commentary = string;
    }
}
