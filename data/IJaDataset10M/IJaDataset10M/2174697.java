package chromolite.game;

/**
 * Describes the move of a stone (coming from a reserve or inside the arena).
 * 
 * @author viphe
 */
public class StoneMove {

    private final Board board;

    private final Stone stone;

    private final BoardPos startPos;

    private final int vx;

    private final int vy;

    private BoardPos endPos;

    public StoneMove(Board board, BoardPos startPos, int vx, int vy) {
        if (board == null) {
            throw new IllegalStateException("board should not be null");
        }
        if (startPos == null) {
            throw new IllegalStateException("startPos should not be null");
        }
        this.board = board;
        this.startPos = startPos;
        this.vx = vx;
        this.vy = vy;
        this.endPos = null;
        this.stone = startPos.getStone();
        if (this.stone == null) {
            throw new IllegalStateException("can't make a move of nothing: stone == null");
        }
        if (stone.canMove() && startPos.isLegal()) {
            computeEndPosition();
        }
    }

    public Stone getStone() {
        return stone;
    }

    public BoardPos getStartPos() {
        return startPos;
    }

    public BoardPos getEndPos() {
        return endPos;
    }

    public int getVX() {
        return vx;
    }

    public int getVY() {
        return vy;
    }

    public boolean isPossible() {
        return endPos != null && !startPos.equals(endPos);
    }

    public void start() {
        if (!isPossible()) return;
        startPos.setStone(null);
        stone.decreaseAvailableMoveCount();
    }

    public void end() {
        if (!isPossible()) return;
        if (startPos.isReserve()) {
            board.fillReserve(startPos.reservePosition, startPos.reserveL);
        }
        endPos.setStone(stone);
    }

    private void computeEndPosition() {
        int x = startPos.x;
        int y = startPos.y;
        while (true) {
            int newX = x + vx;
            int newY = y + vy;
            if (!board.isCell(newX, newY)) {
                break;
            }
            Cell cell = board.getCell(newX, newY);
            if (cell.getStone() == null) {
                x = newX;
                y = newY;
            } else {
                endPos = new BoardPos(board, x, y);
                if (!endPos.isCell()) {
                    endPos = null;
                }
                break;
            }
        }
    }
}
