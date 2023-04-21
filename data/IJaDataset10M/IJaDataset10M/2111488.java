package com.timk.goserver.client.board;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import com.timk.goserver.client.compatibility.Point;

/** Pass command */
public class PassCommand extends AbstractCommand {

    private static final Point location = new Point(BoardController.PASS_COORD, BoardController.PASS_COORD);

    private final boolean isSecondPass;

    private final Set deadStones;

    /**
	 * Creates a PassCommand
	 * @param controller the BoardController
	 * @param board the Board
	 * @param player the player - 0 to find using previous move
	 * @param prevMove the previous move
	 * @param comment the comment
	 */
    public PassCommand(BoardController controller, Board board, int player, AbstractCommand prevMove, String comment) {
        super(controller, board, player, prevMove, comment);
        isSecondPass = prevMove instanceof PassCommand;
        deadStones = new HashSet();
    }

    public void localDoIt() {
        board.setKoPoint(null);
        if (isSecondPass) {
            controller.setScoringMode(true);
            setDeadStones(deadStones);
            findTerritories();
        }
    }

    public void localUndoIt() {
        if (isSecondPass) {
            controller.setScoringMode(false);
            clearTerritoryMarks();
        }
    }

    public void toSgf(StringBuffer buf) {
        buf.append(';');
        if (player == 1) {
            buf.append('B');
        } else {
            buf.append('W');
        }
        buf.append("[tt]");
        if (comment != null) {
            buf.append("C[").append(comment).append(']');
        }
    }

    private void findTerritories() {
        Set points = new HashSet(300);
        int size = board.getSize();
        int whiteScore = 0;
        int blackScore = 0;
        findEmptyPoints(points);
        while (!points.isEmpty()) {
            List thisTerritory = new ArrayList();
            boolean sawBlack = false;
            boolean sawWhite = false;
            Point p = (Point) points.iterator().next();
            Set pointsToProcess = new HashSet();
            pointsToProcess.add(p);
            while (!pointsToProcess.isEmpty()) {
                p = (Point) pointsToProcess.iterator().next();
                pointsToProcess.remove(p);
                points.remove(p);
                thisTerritory.add(p);
                if (deadStones.contains(p)) {
                    if (board.getAt(p) == 1) {
                        whiteScore++;
                    } else {
                        blackScore++;
                    }
                }
                for (int i = 0; i < 4; i++) {
                    int nx = p.x + Board.offsets[i][0];
                    int ny = p.y + Board.offsets[i][1];
                    if (nx < 0 || nx >= size || ny < 0 || ny >= size) {
                        continue;
                    }
                    int color = board.getAt(nx, ny);
                    Point nextPoint = new Point(nx, ny);
                    if (color == 0 || deadStones.contains(nextPoint)) {
                        if (!thisTerritory.contains(nextPoint)) {
                            pointsToProcess.add(nextPoint);
                        }
                    } else if (color == 1) {
                        sawBlack = true;
                    } else if (color == -1) {
                        sawWhite = true;
                    }
                }
            }
            int mark;
            if (sawBlack && !sawWhite) {
                mark = Board.MARK_TERRBLACK;
                blackScore += thisTerritory.size();
            } else if (sawWhite && !sawBlack) {
                mark = Board.MARK_TERRWHITE;
                whiteScore += thisTerritory.size();
            } else {
                mark = Board.MARK_TERRUNKNOWN;
            }
            for (Iterator iter = thisTerritory.iterator(); iter.hasNext(); ) {
                p = (Point) iter.next();
                board.setMarkAt(p, mark);
            }
        }
        controller.setScore(blackScore, whiteScore);
    }

    private void findEmptyPoints(Set points) {
        int size = board.getSize();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board.getAt(i, j) == 0) {
                    points.add(new Point(i, j));
                } else if (deadStones.contains(new Point(i, j))) {
                    points.add(new Point(i, j));
                }
            }
        }
    }

    private void clearTerritoryMarks() {
        int size = board.getSize();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board.setMarkAt(i, j, Board.MARK_NONE);
            }
        }
    }

    /**
	 * Toggles the status of a group
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
    public void toggleGroup(int x, int y) {
        int toggleColor = board.getAt(x, y);
        if (toggleColor == 0) {
            return;
        }
        boolean stoneWasAlive = board.getMarkAt(x, y) == Board.MARK_NONE;
        int stopColor = toggleColor * -1;
        Set pointsChecked = new HashSet();
        Set pointsToCheck = new HashSet();
        pointsToCheck.add(new Point(x, y));
        int size = board.getSize();
        while (!pointsToCheck.isEmpty()) {
            Point p = (Point) pointsToCheck.iterator().next();
            if (board.getAt(p) == toggleColor) {
                if (stoneWasAlive) {
                    deadStones.add(p);
                } else {
                    deadStones.remove(p);
                    board.setMarkAt(p, Board.MARK_NONE);
                }
            }
            pointsToCheck.remove(p);
            pointsChecked.add(p);
            for (int i = 0; i < 4; i++) {
                int nx = p.x + Board.offsets[i][0];
                int ny = p.y + Board.offsets[i][1];
                if (nx < 0 || nx >= size || ny < 0 || ny >= size) {
                    continue;
                }
                Point nextPoint = new Point(nx, ny);
                int nextColor = board.getAt(nx, ny);
                if (nextColor == stopColor) {
                    if (stoneWasAlive) {
                        markGroupAlive(nextPoint);
                    }
                    continue;
                }
                if (!pointsChecked.contains(nextPoint)) {
                    pointsToCheck.add(nextPoint);
                }
            }
        }
        findTerritories();
    }

    private void markGroupAlive(Point startingPoint) {
        int color = board.getAt(startingPoint);
        int size = board.getSize();
        Set pointsToCheck = new HashSet();
        Set pointsChecked = new HashSet();
        pointsToCheck.add(startingPoint);
        while (!pointsToCheck.isEmpty()) {
            Point p = (Point) pointsToCheck.iterator().next();
            pointsToCheck.remove(p);
            pointsChecked.add(p);
            deadStones.remove(p);
            board.setMarkAt(p, Board.MARK_NONE);
            for (int i = 0; i < 4; i++) {
                int nx = p.x + Board.offsets[i][0];
                int ny = p.y + Board.offsets[i][1];
                if (nx < 0 || nx >= size || ny < 0 || ny >= size) {
                    continue;
                }
                Point nextPoint = new Point(nx, ny);
                if (board.getAt(nextPoint) == color && !pointsChecked.contains(nextPoint)) {
                    pointsToCheck.add(nextPoint);
                }
            }
        }
    }

    /**
	 * Sets the dead stones on the board
	 * @param stones Collection&lt;Point&gt;
	 */
    public void setDeadStones(Collection stones) {
        for (Iterator iter = stones.iterator(); iter.hasNext(); ) {
            Point stone = (Point) iter.next();
            if (!deadStones.contains(stone)) {
                toggleGroup(stone.x, stone.y);
            }
        }
    }

    public int getPlayer() {
        return player;
    }

    public Point getLocation() {
        return location;
    }

    public Point getKoPoint() {
        return null;
    }

    /**
	 * Returns the dead stones
	 * @return Set&lt;Point&gt;
	 */
    public Set getDeadStones() {
        return deadStones;
    }
}
