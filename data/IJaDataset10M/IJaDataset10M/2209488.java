package chromolite.ui;

import chromolite.game.BoardPos;
import chromolite.game.StoneMove;

/**
 * Represents a mouse drag action in the context of the game.
 */
public class MouseDrag {

    private static final float PI_ON_6 = (float) Math.PI / 6f;

    private static final float[][] DRAG_VECTOR_BY_SECTOR = { { -PI_ON_6 * 6, -PI_ON_6 * 5, -1f, 0f }, { -PI_ON_6 * 5, -PI_ON_6 * 4, 0f, 0f }, { -PI_ON_6 * 4, -PI_ON_6 * 2, 0f, -1f }, { -PI_ON_6 * 2, -PI_ON_6, 0f, 0f }, { -PI_ON_6, PI_ON_6, +1f, 0f }, { PI_ON_6, PI_ON_6 * 2, 0f, 0f }, { PI_ON_6 * 2, PI_ON_6 * 4, 0f, +1f }, { PI_ON_6 * 4, PI_ON_6 * 5, 0f, 0f }, { PI_ON_6 * 5, PI_ON_6 * 6, -1f, 0f } };

    private static final int MINIMUM_MOVE = Metrics.STONE_WIDTH / 8;

    private int startX;

    private int startY;

    private int dragX;

    private int dragY;

    public MouseDrag(int startX, int startY) {
        this.startX = startX;
        this.startY = startY;
    }

    public void stop(int stopX, int stopY) {
        int vx = stopX - startX;
        int vy = stopY - startY;
        if ((vx * vx + vy * vy) <= MINIMUM_MOVE * MINIMUM_MOVE) {
            return;
        }
        double angle = Math.atan2(vy, vx);
        for (int i = 0; i < DRAG_VECTOR_BY_SECTOR.length; i++) {
            float angleStart = DRAG_VECTOR_BY_SECTOR[i][0];
            float angleEnd = DRAG_VECTOR_BY_SECTOR[i][1];
            if (angleStart <= angle && angle <= angleEnd) {
                dragX = (int) DRAG_VECTOR_BY_SECTOR[i][2];
                dragY = (int) DRAG_VECTOR_BY_SECTOR[i][3];
                break;
            }
        }
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getDragX() {
        return dragX;
    }

    public int getDragY() {
        return dragY;
    }

    /**
	 * Returns the StoneMove corresponding to this MouseDrag or null if there is none.
	 * This method does not check the legality of the returned StoneMove.
	 */
    public StoneMove getStoneMove(BoardRenderer boardRenderer) {
        if (dragX != 0 || dragY != 0) {
            BoardPos dragStartPos = boardRenderer.screenPosToBoardPos(startX, startY);
            if (dragStartPos != null && dragStartPos.getStone() != null) {
                return new StoneMove(boardRenderer.getBoard(), dragStartPos, dragX, dragY);
            }
        }
        return null;
    }
}
