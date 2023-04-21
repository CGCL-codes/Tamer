package com.timk.goserver.client.board;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import com.timk.goserver.client.compatibility.Point;
import com.timk.goserver.client.sgf.SGFUtils;

/** Move command */
public class MoveCommand extends AbstractCommand {

    private final Point location;

    private final Set removedStones;

    private Point koPoint;

    /**
	 * Creates a MoveCommand
	 * @param controller the BoardController
	 * @param board the Board
	 * @param player the player - 0 to find using previous move
	 * @param location the x,y location
	 * @param prevMove the previous move, or null
	 * @param comment the comment
	 */
    public MoveCommand(BoardController controller, Board board, int player, Point location, AbstractCommand prevMove, String comment) {
        super(controller, board, player, prevMove, comment);
        this.location = location;
        removedStones = new HashSet();
    }

    public void localDoIt() {
        board.move(player, location, removedStones);
        koPoint = board.getKoPoint();
        board.setMarkAt(location, Board.MARK_CURMOVE);
        if (player == 1) {
            controller.changeCapturedByBlack(removedStones.size());
        } else {
            controller.changeCapturedByWhite(removedStones.size());
        }
    }

    public void localUndoIt() {
        board.remove(location);
        board.setMarkAt(location, Board.MARK_NONE);
        if (player == 1) {
            controller.changeCapturedByBlack(-1 * removedStones.size());
        } else {
            controller.changeCapturedByWhite(-1 * removedStones.size());
        }
        int otherPlayer = player * -1;
        for (Iterator iter = removedStones.iterator(); iter.hasNext(); ) {
            Point point = (Point) iter.next();
            board.setAt(point, otherPlayer);
        }
        removedStones.clear();
    }

    public void toSgf(StringBuffer buf) {
        buf.append(';');
        if (player == 1) {
            buf.append('B');
        } else {
            buf.append('W');
        }
        buf.append('[');
        SGFUtils.pointToString(location, buf);
        buf.append(']');
        if (comment != null) {
            buf.append("C[").append(comment).append(']');
        }
    }

    public Point getLocation() {
        return location;
    }

    public Point getKoPoint() {
        return koPoint;
    }

    /**
	 * Returns the stones captured by this move
	 * @return the stones captured by this move
	 */
    public Set getRemovedStones() {
        return removedStones;
    }
}
