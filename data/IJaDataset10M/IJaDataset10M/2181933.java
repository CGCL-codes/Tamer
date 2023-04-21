package weiqi;

import java.util.Enumeration;
import java.util.Stack;
import java.util.Vector;
import common.Point;
import common.Rectangle;

/**
 * Represents the game board (2D array)
 */
public class WeiqiBoard {

    public static final int BLACK = 1;

    public static final int WHITE = -1;

    public static final int EMPTY = 0;

    public static final int[][] direction = { { 0, -1 }, { 1, 0 }, { 0, 1 }, { -1, 0 } };

    int[][] board;

    int boardSize;

    public WeiqiBoard() {
        reset(19);
    }

    public WeiqiBoard(int newBoardSize) {
        reset(newBoardSize);
    }

    /**
   * 
   * @param newBoardSize
   */
    public void reset(int newBoardSize) {
        boardSize = newBoardSize;
        board = new int[boardSize][boardSize];
    }

    /**
   * place the stone as current player on given point, it will not check if
   * this move legal, the caller should guarentee to call isLegalMove before.
   * 
   * it will reverse the current player after placing the stone
   * @param point
   * @return the dead stones, e.g. a vector of Point objects
   */
    public Vector play(Point point, int player) {
        placeStone(point, player);
        Vector vec = this.markDeadStones(point, player);
        placeStones(vec, EMPTY);
        return vec;
    }

    public void placeStone(Point point, int color) {
        if (point == null) return;
        int x = point.getX();
        int y = point.getY();
        board[x][y] = color;
    }

    public boolean isValidMove(Point pt, int color) {
        if (this.getPosition(pt) != EMPTY) return true;
        boolean result = false;
        this.placeStone(pt, color);
        Vector vec = this.markDeadStones(pt, color);
        if (vec.size() > 0) {
            result = true;
        } else {
            int[][] history = new int[boardSize][boardSize];
            vec = this.findDeadGroup(pt, history);
            result = !(vec.size() > 0);
        }
        this.placeStone(pt, EMPTY);
        return result;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public int getPosition(Point pt) {
        return board[pt.getX()][pt.getY()];
    }

    public Rectangle getBoardArea() {
        return new Rectangle(0, 0, boardSize - 1, boardSize - 1);
    }

    /**
   * suppose *lastplayer* just played at *lastmove* (the board is already changed)
   * find out the opponendt's dead groups (but not set them to EMPTY yet)
   * 
   * @param lastmove
   * @param lastplayer
   * @return
   */
    public Vector markDeadStones(Point lastmove, int lastplayer) {
        Vector vec = new Vector();
        Rectangle area = this.getBoardArea();
        int[][] history = new int[boardSize][boardSize];
        for (int i = 0; i < direction.length; ++i) {
            Point nextpt = new Point(lastmove);
            nextpt.move(direction[i][0], direction[i][1]);
            if (area.contains(nextpt) && (this.getPosition(nextpt) * lastplayer == -1) && history[nextpt.getX()][nextpt.getY()] == 0) {
                Vector tempVec = this.findDeadGroup(nextpt, history);
                for (Enumeration e = tempVec.elements(); e.hasMoreElements(); ) {
                    vec.addElement(e.nextElement());
                }
            }
        }
        return vec;
    }

    /**
   * start from the given point, test if this is a dead group
   * 
   * It's a dead group if 1. no liberty is found (the dead groupis returned) or 2. it's connected to a certain known
   * dead group that is stored in history (empty is returned because it's already in the history)
   * 
   * @param pt the point need to test
   * @param history a board that contains only the already known dead stones.
   * @return the dead group only if it's not in the history yet.
   */
    public Vector findDeadGroup(Point pt, int[][] history) {
        Stack stack = new Stack();
        Vector dead = new Vector();
        int deadcolor = this.getPosition(pt);
        Rectangle area = this.getBoardArea();
        stack.push(pt);
        history[pt.getX()][pt.getY()] = 1;
        while (!stack.isEmpty()) {
            Point curpt = (Point) stack.peek();
            int status = history[curpt.getX()][curpt.getY()];
            if (status == 5) {
                dead.addElement(curpt);
                stack.pop();
                continue;
            }
            Point nextpt = new Point(curpt);
            if (status >= 1 && status <= 4) {
                history[curpt.getX()][curpt.getY()] = status + 1;
                nextpt.move(direction[status - 1][0], direction[status - 1][1]);
            }
            if (!area.contains(nextpt)) continue;
            if (history[nextpt.getX()][nextpt.getY()] > 0) continue;
            int nextcolor = this.getPosition(nextpt);
            if (nextcolor * deadcolor == -1) continue;
            if (nextcolor == deadcolor) {
                stack.push(nextpt);
                history[nextpt.getX()][nextpt.getY()] = 1;
                continue;
            }
            {
                for (Enumeration e = dead.elements(); e.hasMoreElements(); ) {
                    Point deadpt = (Point) (e.nextElement());
                    history[deadpt.getX()][deadpt.getY()] = 0;
                }
                for (Enumeration e = stack.elements(); e.hasMoreElements(); ) {
                    Point stackpt = (Point) (e.nextElement());
                    history[stackpt.getX()][stackpt.getY()] = 0;
                }
                dead.removeAllElements();
                break;
            }
        }
        return dead;
    }

    public void placeStones(Vector vec, int color) {
        if (vec == null) return;
        for (Enumeration e = vec.elements(); e.hasMoreElements(); ) {
            Point pt = (Point) (e.nextElement());
            this.placeStone(pt, color);
        }
    }
}
