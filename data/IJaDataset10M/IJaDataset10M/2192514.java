package com.jarcgameengine.demo.flightgame;

import com.jarcgameengine.game.DrawableGameComponent;
import com.jarcgameengine.util.ContentLoader;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.LayerManager;
import javax.microedition.lcdui.game.TiledLayer;

/**
 *
 * @author Salânio Júnior
 */
public class BackgroundManager implements DrawableGameComponent {

    private static BackgroundManager backgroundManager;

    private Image backgroundImg;

    private LayerManager layerManager;

    private BackgroundManager(LayerManager layerManager) {
        this.layerManager = layerManager;
    }

    public static BackgroundManager getInstance(LayerManager layerManager) {
        if (backgroundManager == null) {
            backgroundManager = new BackgroundManager(layerManager);
        }
        return backgroundManager;
    }

    public int[] makeTileCells() {
        int[] cells = { 1, 1, 47, 76, 76, 76, 76, 76, 76, 76, 76, 76, 48, 1, 1, 1, 1, 47, 76, 76, 76, 76, 76, 76, 76, 76, 76, 48, 1, 1, 1, 1, 47, 76, 76, 76, 76, 76, 76, 76, 76, 76, 48, 1, 1, 1, 1, 47, 76, 76, 76, 76, 76, 76, 76, 76, 76, 48, 1, 1, 1, 1, 47, 76, 76, 76, 76, 76, 76, 76, 76, 76, 48, 1, 1, 1, 1, 47, 76, 76, 76, 76, 76, 76, 76, 76, 76, 48, 1, 1, 1, 1, 47, 76, 76, 76, 76, 76, 76, 76, 76, 76, 48, 1, 1, 1, 1, 47, 76, 76, 76, 76, 76, 76, 76, 76, 76, 48, 1, 1, 1, 1, 47, 76, 76, 76, 76, 76, 76, 76, 76, 76, 48, 1, 1, 1, 1, 47, 76, 76, 76, 76, 76, 76, 76, 76, 76, 48, 1, 1, 1, 1, 47, 76, 76, 76, 76, 76, 76, 76, 76, 76, 48, 1, 1, 1, 1, 47, 76, 76, 76, 76, 76, 76, 76, 76, 76, 48, 1, 1, 1, 1, 47, 76, 76, 76, 76, 76, 76, 76, 76, 76, 48, 1, 1, 1, 1, 47, 76, 76, 76, 76, 76, 76, 76, 76, 76, 48, 1, 1, 1, 1, 47, 76, 76, 76, 76, 76, 76, 76, 76, 76, 48, 1, 1, 1, 1, 47, 76, 76, 76, 76, 76, 76, 76, 76, 76, 48, 1, 1, 1, 1, 47, 76, 76, 76, 76, 76, 76, 76, 76, 76, 48, 1, 1, 1, 1, 47, 76, 76, 76, 76, 76, 76, 76, 76, 76, 48, 1, 1, 1, 1, 47, 76, 76, 76, 76, 76, 76, 76, 76, 76, 48, 1, 1, 1, 1, 47, 76, 76, 76, 76, 76, 76, 76, 76, 76, 48, 1, 1, 1, 1, 47, 76, 76, 76, 76, 76, 76, 76, 76, 76, 48, 1, 1, 1, 1, 47, 76, 76, 76, 76, 76, 76, 76, 76, 76, 48, 1, 1 };
        return cells;
    }

    public TiledLayer createBackground() {
        int COLS = 15;
        int ROWS = 22;
        TiledLayer backgroundTile = new TiledLayer(COLS, ROWS, backgroundImg, 16, 16);
        int[] tiles = makeTileCells();
        int itr = 0;
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                backgroundTile.setCell(col, row, tiles[itr++]);
            }
        }
        backgroundTile.setPosition(0, 0);
        return backgroundTile;
    }

    public void initialize() {
    }

    public void loadContent() {
        backgroundImg = ContentLoader.createImage(ContentLoader.IMAGE_FILE, "topview_tiles", ContentLoader.PNG_EXT);
        layerManager.append(createBackground());
    }

    public void unloadContent() {
    }

    public void update(long elapsedTime) {
    }

    public void draw(long elapsedTime) {
    }
}
