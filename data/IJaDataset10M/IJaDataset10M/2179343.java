package org.thenesis.planetino2.test;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import org.thenesis.planetino2.bsp2D.BSPRenderer;
import org.thenesis.planetino2.bsp2D.BSPTree;
import org.thenesis.planetino2.bsp2D.MapLoader;
import org.thenesis.planetino2.engine.shooter3D.Bot;
import org.thenesis.planetino2.engine.shooter3D.ShooterCore;
import org.thenesis.planetino2.game.CollisionDetection;
import org.thenesis.planetino2.game.GameObject;
import org.thenesis.planetino2.game.GameObjectRenderer;
import org.thenesis.planetino2.game.GridGameObjectManager;
import org.thenesis.planetino2.game.Player;
import org.thenesis.planetino2.graphics.Color;
import org.thenesis.planetino2.graphics.Screen;
import org.thenesis.planetino2.input.InputManager;
import org.thenesis.planetino2.math3D.PointLight3D;
import org.thenesis.planetino2.math3D.PolygonGroup;
import org.thenesis.planetino2.math3D.PolygonGroupBounds;
import org.thenesis.planetino2.math3D.Transform3D;

public class CollisionTest extends ShooterCore {

    protected BSPTree bspTree;

    protected String mapFile;

    public CollisionTest(Screen screen, InputManager inputManager) {
        super(screen, inputManager);
        this.inputManager = inputManager;
    }

    public void createPolygons() {
        Graphics g = screen.getGraphics();
        g.setColor(Color.BLACK.getRGB());
        g.fillRect(0, 0, screen.getWidth(), screen.getHeight());
        g.setColor(Color.WHITE.getRGB());
        float ambientLightIntensity = .2f;
        Vector lights = new Vector();
        lights.addElement(new PointLight3D(-100, 100, 100, .3f, -1));
        lights.addElement(new PointLight3D(100, 100, 0, .3f, -1));
        MapLoader loader = new MapLoader();
        loader.setObjectLights(lights, ambientLightIntensity);
        try {
            bspTree = loader.loadMap("/res/", "test.map");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        CollisionDetection collisionDetection = new CollisionDetection(bspTree);
        gameObjectManager = new GridGameObjectManager(bspTree.calcBounds(), collisionDetection);
        gameObjectManager.addPlayer(new GameObject(new PolygonGroup("Player")));
        PolygonGroupBounds playerBounds = gameObjectManager.getPlayer().getBounds();
        playerBounds.setTopHeight(Player.DEFAULT_PLAYER_HEIGHT);
        playerBounds.setRadius(Player.DEFAULT_PLAYER_RADIUS);
        ((BSPRenderer) polygonRenderer).setGameObjectManager(gameObjectManager);
        createGameObjects(loader.getObjectsInMap());
        Transform3D start = loader.getPlayerStartLocation();
        gameObjectManager.getPlayer().getTransform().setTo(start);
    }

    private void createGameObjects(Vector mapObjects) {
        Enumeration e = mapObjects.elements();
        while (e.hasMoreElements()) {
            PolygonGroup group = (PolygonGroup) e.nextElement();
            String filename = group.getFilename();
            if ("robot.obj3d".equals(filename)) {
                gameObjectManager.add(new Bot(group));
            } else {
                gameObjectManager.add(new GameObject(group));
            }
        }
    }

    public void drawPolygons(Graphics g) {
        polygonRenderer.startFrame(screen);
        ((BSPRenderer) polygonRenderer).draw(g, bspTree);
        gameObjectManager.draw(g, (GameObjectRenderer) polygonRenderer);
        polygonRenderer.endFrame(screen);
    }
}
