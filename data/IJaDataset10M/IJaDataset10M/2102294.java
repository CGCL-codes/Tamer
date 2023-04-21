package meadow.client;

import meadow.common.GridTerrainModel;
import meadow.common.TerrainModel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Rectangle;

/** Base class containing code useful to concrete subclasses.*/
abstract class GridTerrainView extends TerrainView {

    GridTerrainModel model;

    Color[] colours;

    /** @param t The model which this is to be a view <em>of</em>.*/
    GridTerrainView(GridTerrainModel t) {
        model = t;
        colours = new Color[5];
        colours[0] = Color.black;
        colours[1] = Color.green.darker().darker();
        colours[2] = Color.cyan.darker();
        colours[3] = Color.green.darker();
        colours[4] = Color.lightGray;
    }

    public TerrainModel getTerrainModel() {
        return model;
    }

    abstract void drawCell(Graphics g, int type, int x, int y);

    public void draw(Graphics g) {
        int squareSize = model.getSquareSize();
        Rectangle r = g.getClipBounds();
        int xmin = r.x / squareSize;
        int ymin = r.y / squareSize;
        int xmax = ((r.x + r.width) / squareSize) + 1;
        int ymax = ((r.y + r.height) / squareSize) + 1;
        int cx;
        int cy;
        for (int i = xmin; i <= xmax; i++) {
            cx = i * squareSize;
            for (int j = ymin; j <= ymax; j++) {
                cy = j * squareSize;
                int terrain = model.atIndex(i, j);
                if (terrain != TerrainModel.NIL) {
                    drawCell(g, terrain, cx, cy);
                }
            }
        }
    }
}
