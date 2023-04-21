package engine;

import org.lwjgl.*;
import org.lwjgl.opengl.*;
import org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.vector.Vector3f;
import util.Distance;
import java.util.LinkedList;
import java.util.Iterator;
import util.Renderer;

/**
 *
 * @author  jmadar
 */
public class GameObjectManager {

    private Game game;

    private Camera camera;

    private World world;

    private LinkedList<GameObject> collidableObjects;

    private LinkedList<GameObject> nonCollidableObjects;

    private LinkedList<GameObject> removeList;

    private long currentTime;

    /** Creates a new instance of GameObjectManager */
    public GameObjectManager() {
        collidableObjects = new LinkedList<GameObject>();
        nonCollidableObjects = new LinkedList<GameObject>();
        removeList = new LinkedList<GameObject>();
        currentTime = 0;
    }

    public void addObject(GameObject obj) {
        obj.setGameObjectManager(this);
        if (obj.isCollidable()) collidableObjects.add(obj); else nonCollidableObjects.add(obj);
    }

    public void removeObject(GameObject obj) {
        if (obj.isCollidable()) collidableObjects.remove(obj); else nonCollidableObjects.remove(obj);
    }

    public void removeAllObjects() {
        collidableObjects.clear();
        nonCollidableObjects.clear();
    }

    public void detectCollision2d() {
        for (int i = 0; i < collidableObjects.size(); i++) {
            GameObject obj = collidableObjects.get(i);
            for (int j = i + 1; j < collidableObjects.size(); j++) {
                GameObject obj2 = collidableObjects.get(j);
                if (obj != obj2) {
                    float left1 = obj.getPosition().x - 0.5f;
                    float right1 = obj.getPosition().x + 0.5f;
                    float bottom1 = obj.getPosition().y - 0.5f;
                    float top1 = obj.getPosition().y + 0.5f;
                    float left2 = obj2.getPosition().x - 0.5f;
                    float right2 = obj2.getPosition().x + 0.5f;
                    float bottom2 = obj2.getPosition().y - 0.5f;
                    float top2 = obj2.getPosition().y + 0.5f;
                    boolean leftbtw = false, rightbtw = false, topbtw = false, bottombtw = false;
                    if (left1 >= left2 && left1 <= right2) {
                        leftbtw = true;
                    }
                    if (right1 >= left2 && right1 <= right2) {
                        rightbtw = true;
                    }
                    if (top1 <= top2 && top1 >= bottom2) {
                        topbtw = true;
                    }
                    if (bottom1 <= top2 && bottom1 >= bottom2) {
                        bottombtw = true;
                    }
                    if (leftbtw || rightbtw) {
                        if (topbtw || bottombtw) {
                            obj.hit();
                            obj2.hit();
                        }
                    }
                }
            }
        }
    }

    private void detectCollision3d() {
        world.hit(camera);
        for (int i = 0; i < collidableObjects.size(); i++) {
            world.hit(collidableObjects.get(i));
        }
        for (int i = 0; i < collidableObjects.size(); i++) {
            GameObject obj = collidableObjects.get(i);
            for (int j = i + 1; j < collidableObjects.size(); j++) {
                GameObject obj2 = collidableObjects.get(j);
                if (obj != obj2 && obj.isVisible() && obj2.isVisible()) {
                    float collisionDistance = obj.getRadius() + obj2.getRadius();
                    collisionDistance *= collisionDistance;
                    float dx = obj.getPosition().x - obj2.getPosition().x;
                    float dy = obj.getPosition().y - obj2.getPosition().y;
                    float dz = obj.getPosition().z - obj2.getPosition().z;
                    float objectDistance = (dx * dx + dy * dy + dz * dz);
                    if (objectDistance < collisionDistance) {
                        obj.hit(obj2);
                        obj2.hit(obj);
                    }
                }
            }
        }
    }

    private void detectCollision3dLine() {
        world.hit(camera);
        for (int i = 0; i < collidableObjects.size(); i++) {
            world.hit(collidableObjects.get(i));
        }
        for (int i = 0; i < collidableObjects.size(); i++) {
            GameObject obj = collidableObjects.get(i);
            for (int j = i; j < collidableObjects.size(); j++) {
                GameObject obj2 = collidableObjects.get(j);
                if (obj != obj2 && obj.isVisible() && obj2.isVisible()) {
                    float collisionDistance = obj.getRadius() + obj2.getRadius();
                    float objectDistance = Distance.distance(obj, obj2);
                    if (objectDistance < collisionDistance) {
                        obj.hit(obj2);
                        obj2.hit(obj);
                    }
                }
            }
        }
    }

    private void detectCollision() {
        detectCollision3dLine();
    }

    private void render(LinkedList<GameObject> objects, long elapsedTime) {
        for (int i = 0; i < objects.size(); i++) {
            GameObject obj = objects.get(i);
            if (obj.isVisible()) if (obj.isNewObj()) obj.render(0); else obj.render(elapsedTime); else removeList.add(obj);
        }
        collidableObjects.removeAll(removeList);
        while (removeList.size() > 0) removeList.removeFirst();
    }

    public void render() {
        long elapsedTime;
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        if (currentTime == 0) currentTime = System.currentTimeMillis();
        elapsedTime = System.currentTimeMillis() - currentTime;
        camera.render(elapsedTime);
        world.render();
        render(nonCollidableObjects, elapsedTime);
        render(collidableObjects, elapsedTime);
        detectCollision();
        currentTime += elapsedTime;
    }

    /** Getter for property game.
     * @return Value of property game.
     *
     */
    public engine.Game getGame() {
        return game;
    }

    /** Setter for property game.
     * @param game New value of property game.
     *
     */
    public void setGame(engine.Game game) {
        this.game = game;
    }

    /** Getter for property camera.
     * @return Value of property camera.
     *
     */
    public engine.Camera getCamera() {
        return camera;
    }

    /** Setter for property camera.
     * @param camera New value of property camera.
     *
     */
    public void setCamera(engine.Camera camera) {
        this.camera = camera;
        camera.setGameObjectManager(this);
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        world.setGameObjectManager(this);
        this.world = world;
    }

    public int getNumberCollidableObject() {
        return collidableObjects.size();
    }
}
