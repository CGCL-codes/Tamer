package game.room;

import game.Entity;
import game.entity.Terrain;
import game.entity.terrain.Wall;
import applet.Screen;

/** */
public class Room {

    /** */
    public static final int LAYERS = 4;

    /** */
    public static final int ROWS = Screen.HEIGHT / Cell.HEIGHT;

    /** */
    public static final int COLS = Screen.WIDTH / Cell.WIDTH + 1;

    /** */
    private Entity[][][] entities;

    /** */
    public Room() {
        entities = new Entity[LAYERS][ROWS][COLS];
        for (int layer = 0; layer < LAYERS; ++layer) {
            for (int row = 0; row < ROWS; ++row) {
                for (int col = 0; col < COLS; ++col) {
                    entities[layer][row][col] = null;
                }
            }
        }
    }

    /**
	 * @param row - the row.
	 * @param col - the column.
	 * @return the layer just above the wall level.
	 */
    public final int getTopLayer(final int row, final int col) {
        for (int layer = 0; layer < LAYERS; ++layer) {
            if (!(getEntity(layer, row, col) instanceof Terrain)) {
                return layer;
            }
        }
        return LAYERS;
    }

    /**
	 * @param l1 -
	 * @param r1 -
	 * @param c1 -
	 * @param l2 -
	 * @param r2 -
	 * @param c2 -
	 * @return a string representing the terrain in this region of the
	 *         room.
	 */
    public final String getTerrainString(final int l1, final int r1, final int c1, final int l2, final int r2, final int c2) {
        String str = "";
        for (int l = l1; l <= l2; ++l) {
            for (int r = r1; r <= r2; ++r) {
                for (int c = c1; c <= c2; ++c) {
                    Entity e = getEntity(l, r, c);
                    if (e instanceof Terrain) {
                        str += e.getClass().getSimpleName().charAt(0);
                    } else {
                        str += 'N';
                    }
                }
            }
        }
        return str;
    }

    /**
	 * @param layer - the layer.
	 * @param row - the row.
	 * @param col - the column.
	 * @param entity - the entity or null.
	 * @return
	 */
    public final void setEntity(final int layer, final int row, final int col, final Entity entity) {
        if (entity == null || entity.validatePosition(this, layer, row, col)) {
            Entity old = getEntity(layer, row, col);
            entities[layer][row][col] = entity;
            for (int l = -1; l <= 1; ++l) {
                for (int r = -1; r <= 1; ++r) {
                    for (int c = -1; c <= 1; ++c) {
                        Entity s = getEntity(layer + l, row + r, col + c);
                        if (s != null && !s.validatePosition(this, layer + l, row + r, col + c)) {
                            entities[layer][row][col] = old;
                            return;
                        }
                    }
                }
            }
        }
    }

    /**
	 * @param layer - the layer.
	 * @param row - the row.
	 * @param col - the column.
	 * @return the entity at this location or null.
	 */
    public final Entity getEntity(final int layer, final int row, final int col) {
        if (layer < 0) {
            return new Wall();
        } else if (layer >= LAYERS) {
            return null;
        }
        int adjustedRow;
        int adjustedCol;
        if (row < 0) {
            adjustedRow = 0;
        } else if (row >= ROWS) {
            adjustedRow = ROWS - 1;
        } else {
            adjustedRow = row;
        }
        if (col < 0) {
            adjustedCol = 0;
        } else if (col >= COLS) {
            adjustedCol = COLS - 1;
        } else {
            adjustedCol = col;
        }
        return entities[layer][adjustedRow][adjustedCol];
    }
}
