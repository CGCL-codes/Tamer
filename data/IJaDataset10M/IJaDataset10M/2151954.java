package mz2d.example;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import mz2d.engine.MZ2D;
import mz2d.render.ProjectorException;
import mz2d.graphics.sprite.impl.DrawingEntity;
import mz2d.map.Map;
import mz2d.map.MapBuffer;
import mz2d.map.MapException;
import mz2d.map.MapStreamer;
import mz2d.entity.player.Player;

/**
 *
 * @author Laptop
 */
public class Example extends MZ2D {

    private MapStreamer mapStreamer;

    private Map testMap;

    private DrawingEntity tile0;

    private DrawingEntity tile1;

    private DrawingEntity tileLos;

    private Player myPlayer;

    private int mapEId;

    private int viewX = 0;

    private int viewY = 0;

    private int snapX = 0;

    private int snapY = 0;

    public static void main(String args[]) {
        new Example();
    }

    /**
     * Initialize MZ2D
     */
    public Example() {
        createWindow(800, 640);
        window.setTitle("MZ2D - Game Example");
        testMap = new Map();
        myPlayer = new Player();
    }

    /**
     * Game Start
     */
    @Override
    public void gameStart() {
        try {
            DrawingEntity myTile = new DrawingEntity(getResourceManager().floadSprite("./data/tree.png"));
            myTile.setDrawPosX(110);
            myTile.setDrawPosY(110);
            myTile.setLayer(2);
            getProjector().addEntity(myTile);
            tile0 = new DrawingEntity(getResourceManager().floadSprite("./data/tile0.png"));
            tile1 = new DrawingEntity(getResourceManager().floadSprite("./data/tile1.png"));
            tileLos = new DrawingEntity(getResourceManager().floadSprite("./data/tileLos.png"));
            mapStreamer = new MapStreamer();
            mapEId = getProjector().addEntity(new DrawingEntity(getDrawingComponent().createImage(width, height), null));
            renderMap();
        } catch (IOException iex) {
            iex.printStackTrace();
        }
    }

    @Override
    public void mousePressed(MouseEvent me) {
        clickX = me.getX();
        clickY = me.getY();
        clickType = me.getButton();
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        char charCode = ke.getKeyChar();
        boolean pressed = false;
        if (charCode == 'a') {
            if (viewX > 0) {
                viewX--;
                pressed = true;
            }
        } else if (charCode == 'w') {
            if (viewY > 0) {
                viewY--;
                pressed = true;
            }
        } else if (charCode == 's') {
            if (viewY < (mapStreamer.getTiles().length / mapStreamer.getTileWidth())) {
                viewY++;
                pressed = true;
            }
        } else if (charCode == 'd') {
            if (viewX < mapStreamer.getTileWidth()) {
                viewX++;
                pressed = true;
            }
        }
        if (pressed) {
            renderMap();
        }
    }

    /**
     * Map Render
     */
    public void renderMap() {
        try {
            Graphics g = getProjector().getEntity(mapEId).getImage().getGraphics();
            MapBuffer mapBuffer = mapStreamer.getBuffer(viewX, viewY, 25, 20);
            for (int x = 0; x < 25; x++) {
                for (int y = 0; y < 20; y++) {
                    int tileType = mapBuffer.getTiles()[(mapBuffer.getTileWidth() * y) + x].getType();
                    if (tileType == 0) {
                        g.drawImage(tile0.getImage(), x * 32, y * 32, null);
                    } else if (tileType == 1) {
                        g.drawImage(tile1.getImage(), x * 32, y * 32, null);
                    } else if (tileType == -1) {
                        g.drawImage(tileLos.getImage(), x * 32, y * 32, null);
                    }
                }
            }
        } catch (MapException me) {
            me.printStackTrace();
        }
    }

    /**
     * Loop the game
     */
    @Override
    public void gameUpdate() {
        if (clickType != -1) {
            clickX -= 4;
            clickY += 8;
            snapX = Math.abs(clickX / 32);
            snapY = Math.abs(clickY / 32);
            System.err.println("Click Snap: " + snapX + " / " + snapY);
            clickType = -1;
        }
    }

    /**
     * Render
     */
    @Override
    public void gameRender() {
        try {
            getProjector().renderFrame();
            getProjector().renderEntities();
            if (snapX != -1 && snapY != -1) {
                Graphics g = getProjector().getGraphics();
                g.setColor(Color.BLUE);
                g.drawRect(snapX * 32, snapY * 32 - 32, 32, 32);
            }
            getProjector().project();
        } catch (ProjectorException pe) {
            pe.printStackTrace();
        }
    }
}
