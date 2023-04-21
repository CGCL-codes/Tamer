package GA;

import GA.tournaments.APlayer;
import LUDOSimulator.LUDOBoard;
import LUDOSimulator.LUDOPlayer;

public class GAPlayer extends APlayer implements LUDOPlayer, IPlayer {

    public enum ACTIONS {

        HIT_OPPONENT, HIT_MY_SELF_HOME, IS_STAR
    }

    ;

    LUDOBoard board;

    public GA brain;

    public GAPlayer(LUDOBoard luduboard, Object o) {
        super();
        board = luduboard;
        brain = new GA(this);
    }

    public GAPlayer(LUDOBoard ludoboard) {
        super(null);
        int[] w = { 3, 13, 14, 14, 2, 5, 6, 13, 11, 8 };
        int[] c = { 9, 4, 15, 10, 5, 7, 2, 0, 14 };
        board = ludoboard;
        actionsweight = w;
        choices = c;
        brain = new GA(this);
    }

    public GAPlayer(LUDOBoard ludoboard, int[] w, int[] c) {
        super(null);
        board = ludoboard;
        actionsweight = w;
        choices = c;
        brain = new GA(this);
    }

    private GAPlayer(LUDOBoard luduboard, ChromosomePair gene) {
        super(null);
        board = luduboard;
        brain = new GA(this, gene);
    }

    public GAPlayer clone() {
        return new GAPlayer(board, brain.chromosome);
    }

    @Override
    public void play() {
        board.print("DSL-based Semi Smart player playing");
        board.rollDice();
        float max = -1;
        int bestIndex = -1;
        for (int i = 0; i < 4; i++) {
            float value = board.moveable(i) ? brain.analyzeBrickSituation(i) : (float) 0;
            if (value > max && value > 0) {
                bestIndex = i;
                max = value;
            }
        }
        if (bestIndex != -1) board.moveBrick(bestIndex);
    }

    public float analyzeBrickSituation(int i) {
        if (board.moveable(i)) {
            Object[] twoboard = twoboards_param(i);
            Object[] future = futureSteps_param(i);
            for (int j : actionsweight) {
                switch(j) {
                    case 0:
                        if (hitOpponentHome((int[][]) twoboard[0], (int[][]) twoboard[1], i)) return 5;
                        break;
                    case 1:
                        if (hitMySelfHome((int[][]) twoboard[0], (int[][]) twoboard[1], i)) return (float) 0.1;
                        break;
                    case 2:
                        if (reachStar((Integer) future[0], i)) return 4;
                        break;
                    case 3:
                        if (moveOut((int[][]) twoboard[0], (int[][]) twoboard[1], i)) return 3;
                        break;
                    case 4:
                        if (atHome((Integer) future[0], i)) return 2;
                    case 5:
                        if (inDanger(i)) return 7;
                    case 6:
                        if (almostHome(i)) return 9;
                    case 7:
                        if (closeToHome(i)) return 7;
                    case 8:
                        if (groupingBricks((Integer) future[0], i)) return 4;
                    case 9:
                        if (movingFromGroup(i)) return 2;
                        break;
                }
            }
            return 1;
        } else return 0;
    }

    /**
	 * 
	 * @param current_board
	 * @param new_board
	 * @param i
	 * @return
	 */
    public boolean moveOut(int[][] current_board, int[][] new_board, int i) {
        if (board.inStartArea(current_board[board.getMyColor()][i], board.getMyColor()) && !board.inStartArea(new_board[board.getMyColor()][i], board.getMyColor())) return true;
        return false;
    }

    /**
	 * 
	 * @param current_board
	 * @param new_board
	 * @param l
	 * @return
	 */
    public boolean hitOpponentHome(int[][] current_board, int[][] new_board, int l) {
        for (int i = 0; i < 4; i++) for (int j = 0; j < 4; j++) if (board.getMyColor() != i) if (board.atField(current_board[i][j]) && !board.atField(new_board[i][j])) return true;
        return false;
    }

    /**
	 * 
	 * @param current_board
	 * @param new_board
	 * @param i
	 * @return
	 */
    public boolean hitMySelfHome(int[][] current_board, int[][] new_board, int i) {
        if (!board.inStartArea(current_board[board.getMyColor()][i], board.getMyColor()) && board.inStartArea(new_board[board.getMyColor()][i], board.getMyColor())) return true;
        return false;
    }

    /**
	 * Checks whether brick <b>i</b> has reached the goal.
	 * @param future_board - future index of brick <b>i</b>.
	 * @param i - Brick number.
	 * @return True if the brick has reached the goal.
	 */
    public boolean atHome(int future_board, int i) {
        return board.atHome(future_board, board.getMyColor());
    }

    /**
	 * Masking hitMySelfHome's parameters to only i.
	 * @param i - Brick number
	 * @return True if it will hit it self home on a move.
	 */
    private boolean hitSelfHome(int i) {
        return hitMySelfHome(board.getBoardState(), board.getNewBoardState(i, board.getMyColor(), board.getDice()), i);
    }

    /**
	 * 
	 * @param future_board
	 * @param i
	 * @return
	 */
    public boolean reachStar(int future_board, int i) {
        return !inDanger(i) ? board.isStar(future_board) : false;
    }

    /**
	 * Checks whether a brick is in danger of getting hit in the next round.
	 * It does, however, allow the possibility of getting hit in the next round whether to move forward and get it self hit home.<p>
	 * The logic behind the checking for danger lays in a private method "checkDanger".
	 * @param future_board - the future index of the brick <b>i</b>.
	 * @param i - Brick number.
	 * @return True if a brick is in danger of getting hit.
	 */
    public boolean inDanger(int i) {
        return hitSelfHome(i) ? true : checkDanger(i);
    }

    /**
	 * Checks whether another player can hit a brick home in the next turn. It considers the possibility of another player to hit two sixes in a row.
	 * @param i - Brick number
	 * @return True if it is in danger.
	 */
    private boolean checkDanger(int i) {
        int[][] cb = board.getBoardState();
        for (int color = 0; color < 4; color++) if (!(color == board.getMyColor())) for (int dice = 0; dice <= 6; dice++) {
            for (int brick = 0; brick < 4; brick++) {
                int[][] fb = board.getNewBoardState(brick, color, dice);
                if (hitMySelfHome(cb, fb, i)) return true;
            }
        }
        return false;
    }

    /**
	 * Checks whether the brick <b>i</b> is in the safe-zone of home.
	 * @param i - Brick number.
	 * @return True if it is in the safe-zone just before the goal position.
	 */
    public boolean almostHome(int i) {
        return board.almostHome(i, board.getMyColor());
    }

    /**
	 * Checks whether the brick <b>i</b> is the closest to the safe-zone.
	 * It will however return false, if it is inside the safe-zone.
	 * @param i - Brick number.
	 * @return True if it is the closest brick to the safe-zone. False if almostHome returns True.
	 */
    public boolean closeToHome(int i) {
        int[] endFieldSquares = { 50, 11, 24, 37 };
        int myCloseness = endFieldSquares[board.getMyColor()] - board.getBoardState()[board.getMyColor()][i];
        if (almostHome(i)) return false; else if (!inDanger(i)) for (int brick = 0; brick < 4; brick++) if (i != brick) if (endFieldSquares[board.getMyColor()] - board.getBoardState()[board.getMyColor()][brick] < myCloseness) return false;
        return true;
    }

    /**
	 * Checks whether brick <b>i</b> can move to another brick of same color.
	 * @param future_board - the future index of the brick <b>i</b>.
	 * @param i - Brick number.
	 * @return True if it can create or join a group of bricks of the same color.
	 */
    public boolean groupingBricks(int future_board, int i) {
        if (!almostHome(i)) for (int brick = 0; brick < 4; brick++) if (brick != i && board.getBoardState()[board.getMyColor()][brick] == future_board) return true;
        return false;
    }

    /**
	 * Checks whether a brick moves away from a grouping of bricks.
	 * @param i - Brick number.
	 * @return True if it moves away from a group of bricks.
	 */
    public boolean movingFromGroup(int i) {
        if (!almostHome(i) && !inDanger(i)) for (int brick = 0; brick < 4; brick++) if (brick != i && board.getBoardState()[board.getMyColor()][brick] == board.getBoardState()[board.getMyColor()][i]) return true;
        return false;
    }

    /**
	 * For methods that needs two boards as parameter.
	 * @param i - Brick number
	 * @return int[][] current_board,<p> int[][] new_board,<p> int i
	 */
    public Object[] twoboards_param(int i) {
        Object[] param = new Object[3];
        int[][] current_board = board.getBoardState();
        int[][] new_board = board.getNewBoardState(i, board.getMyColor(), board.getDice());
        param[0] = current_board;
        param[1] = new_board;
        param[2] = i;
        return param;
    }

    /**
	 * For methods that needs a future step as a parameter. The future step comes from <p>
	 * int[][] new_board = <b>board.getNewBoardState</b>.
	 * @param i - Brick number
	 * @return new_board[board.getMyColor()][i],<p> int i
	 */
    public Object[] futureSteps_param(int i) {
        Object[] param = new Object[2];
        int[][] new_board = board.getNewBoardState(i, board.getMyColor(), board.getDice());
        param[0] = new_board[board.getMyColor()][i];
        param[1] = i;
        return param;
    }

    /**
	 * For methods that needs no specific parameter.<p>
	 * It returns the only standard value that every method needs.
	 * @param i - Brick number
	 * @return int i
	 */
    public Object[] brickID_param(int i) {
        Object[] param = new Object[1];
        param[0] = i;
        return param;
    }

    @Override
    public int[] weights() {
        return actionsweight;
    }

    public int[] choices() {
        return choices;
    }

    public void setParents(GAPlayer pl1, GAPlayer pl2) {
        brain.chromosome.inheritance(pl1.brain.chromosome, pl2.brain.chromosome);
    }

    @Override
    public String toString() {
        return brain.chromosome.toString();
    }
}
