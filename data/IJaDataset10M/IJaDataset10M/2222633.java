package ui;

import controller.CommandPreprocessor;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import model.misc.Array2D;
import model.misc.Tile;

/**
 *
 * @author Vince Kane
 */
public class PlayerGUIFrame_STUB extends JFrame {

    protected View2DMap_STUB viewer;

    protected int viewableRows = 51;

    protected int viewableCols = 51;

    public PlayerGUIFrame_STUB() {
        super("");
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        viewer = new View2DMap_STUB(viewableCols * 32, viewableRows * 32);
        add(viewer, BorderLayout.CENTER);
        pack();
        viewer.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                processMouseClick(e.getX(), e.getY());
            }
        });
    }

    public void update2DMapView(Array2D<Tile> tileMap) {
        viewer.repaintMapView(tileMap, viewableCols, viewableRows);
    }

    public int getViewableRows() {
        return viewableRows;
    }

    public int getViewableCols() {
        return viewableCols;
    }

    public void processMouseClick(int x, int y) {
        int cx = viewer.getWidth() / 2;
        int cy = viewer.getHeight() / 2;
        int dx = Tile.getTileWidth();
        int dy = Tile.getTileHeight();
        char c = '.';
        if (x < (cx - dx)) {
            c = 'a';
            CommandPreprocessor.processKeyPressed(c);
        }
        if (y < (cy - dy)) {
            c = 'w';
            CommandPreprocessor.processKeyPressed(c);
        }
        if (y > (cy + dy)) {
            c = 's';
            CommandPreprocessor.processKeyPressed(c);
        }
        if (x > (cx + dx)) {
            c = 'd';
            CommandPreprocessor.processKeyPressed(c);
        }
    }
}
