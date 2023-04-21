package basics;

import basics.Tile.Side;

public class TestSolution {

    private static final int BORDER_COLOR = 0;

    private static final int BORDER_POSITION_X = 0;

    private static final int BORDER_POSITION_Y = 0;

    public static boolean isSolution(Board board) {
        if (board == null) {
            return false;
        }
        for (int i = 0; i < board.getDimensionX(); i++) {
            for (int j = 0; j < board.getDimensionY(); j++) {
                if (!board.getCell(i, j).isUsed()) {
                    return false;
                }
            }
        }
        for (int i = 0; i < board.getDimensionX(); i++) {
            for (int j = 0; j < board.getDimensionY(); j++) {
                if (!fitPiece(board, i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean fitPiece(Board board, int x, int y) {
        boolean fit = true;
        int[] sColors = new int[4];
        if (x == BORDER_POSITION_X) {
            sColors[Side.LEFT.get()] = BORDER_COLOR;
            sColors[Side.RIGHT.get()] = board.getTile(x + 1, y).getRotatedColor(Side.LEFT, board.getRotation(x + 1, y));
        } else if (x == board.getDimensionX() - 1) {
            sColors[Side.RIGHT.get()] = BORDER_COLOR;
            sColors[Side.LEFT.get()] = board.getTile(x - 1, y).getRotatedColor(Side.RIGHT, board.getRotation(x - 1, y));
        } else {
            sColors[Side.LEFT.get()] = board.getTile(x - 1, y).getRotatedColor(Side.RIGHT, board.getRotation(x - 1, y));
            sColors[Side.RIGHT.get()] = board.getTile(x + 1, y).getRotatedColor(Side.LEFT, board.getRotation(x + 1, y));
        }
        if (y == BORDER_POSITION_Y) {
            sColors[Side.TOP.get()] = BORDER_COLOR;
            sColors[Side.BOTTOM.get()] = board.getTile(x, y + 1).getRotatedColor(Side.TOP, board.getRotation(x, y + 1));
        } else if (y == board.getDimensionY() - 1) {
            sColors[Side.BOTTOM.get()] = BORDER_COLOR;
            sColors[Side.TOP.get()] = board.getTile(x, y - 1).getRotatedColor(Side.BOTTOM, board.getRotation(x, y - 1));
        } else {
            sColors[Side.BOTTOM.get()] = board.getTile(x, y + 1).getRotatedColor(Side.TOP, board.getRotation(x, y + 1));
            sColors[Side.TOP.get()] = board.getTile(x, y - 1).getRotatedColor(Side.BOTTOM, board.getRotation(x, y - 1));
        }
        if ((sColors[Side.LEFT.get()] != board.getTile(x, y).getRotatedColor(Side.LEFT, board.getRotation(x, y))) || (sColors[Side.RIGHT.get()] != board.getTile(x, y).getRotatedColor(Side.RIGHT, board.getRotation(x, y))) || (sColors[Side.TOP.get()] != board.getTile(x, y).getRotatedColor(Side.TOP, board.getRotation(x, y))) || (sColors[Side.BOTTOM.get()] != board.getTile(x, y).getRotatedColor(Side.BOTTOM, board.getRotation(x, y)))) {
            fit = false;
        }
        return fit;
    }
}
