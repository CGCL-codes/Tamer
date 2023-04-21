package jihad;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * The class in charge of handling all objects on screen during the game.
 * Anything to be drawn on screen and updated should be added and removed
 * through a subclass of this class.
 *
 * @author Pascal Chatterjee
 */
public abstract class Environment extends GameObject {

    private GameContainer container;

    private ArrayList<GameObject> universe;

    private ArrayList<Collidable> collidables;

    private ArrayList<MovingObject> movingObjects;

    private ArrayList<GameObject> universeAddQueue, universeRemoveQueue;

    private ArrayList<MovingObject> movingAddQueue, movingRemoveQueue;

    private ArrayList<StationaryObject> stationaryAddQueue, stationaryRemoveQueue;

    private Image background;

    private Stopwatch timer;

    /**
     * Creates an abstract Environment including the supplied GameContainer.
     * @param container
     */
    public Environment(GameContainer container) {
        this.setGameContainer(container);
        universe = new ArrayList<GameObject>();
        movingObjects = new ArrayList<MovingObject>();
        collidables = new ArrayList<Collidable>();
        universeAddQueue = new ArrayList<GameObject>();
        universeRemoveQueue = new ArrayList<GameObject>();
        movingAddQueue = new ArrayList<MovingObject>();
        movingRemoveQueue = new ArrayList<MovingObject>();
        stationaryAddQueue = new ArrayList<StationaryObject>();
        stationaryRemoveQueue = new ArrayList<StationaryObject>();
        timer = new Stopwatch();
        timer.reset().start();
    }

    public GameContainer getGameContainer() {
        return container;
    }

    public void setGameContainer(GameContainer container) {
        this.container = container;
    }

    public long getTimeElapsedSinceCreationMillis() {
        return timer.getTime();
    }

    /**
     * Sets the background image of the Environment.
     * @param url Url of the background image.
     */
    public void setBackground(String url) {
        try {
            this.background = new Image(url);
        } catch (SlickException ex) {
            System.err.println("Failed to load image: " + url);
        }
    }

    public void addMovingObject(MovingObject m) {
        movingAddQueue.add(m);
    }

    private void addMovingObjects(ArrayList<MovingObject> mlist) {
        Iterator<MovingObject> it = mlist.iterator();
        while (it.hasNext()) {
            MovingObject m = it.next();
            universe.add(m);
            movingObjects.add(m);
            collidables.add(m);
            it.remove();
        }
    }

    public void removeMovingObject(MovingObject m) {
        movingRemoveQueue.add(m);
    }

    private void removeMovingObjects(ArrayList<MovingObject> mlist) {
        Iterator<MovingObject> it = mlist.iterator();
        while (it.hasNext()) {
            MovingObject m = it.next();
            universe.remove(m);
            movingObjects.remove(m);
            collidables.remove(m);
            it.remove();
        }
    }

    public void addStationaryObject(StationaryObject so) {
        stationaryAddQueue.add(so);
    }

    private void addStationaryObjects(ArrayList<StationaryObject> slist) {
        Iterator<StationaryObject> it = slist.iterator();
        while (it.hasNext()) {
            StationaryObject s = it.next();
            universe.add(s);
            collidables.add(s);
            it.remove();
        }
        Collections.sort(universe);
    }

    public void removeStationaryObject(StationaryObject so) {
        stationaryRemoveQueue.add(so);
    }

    private void removeStationaryObjects(ArrayList<StationaryObject> slist) {
        Iterator<StationaryObject> it = slist.iterator();
        while (it.hasNext()) {
            StationaryObject s = it.next();
            universe.remove(s);
            collidables.remove(s);
            it.remove();
        }
    }

    public void addGameObject(GameObject go) {
        universeAddQueue.add(go);
    }

    private void addGameObjects(ArrayList<GameObject> glist) {
        Iterator<GameObject> it = glist.iterator();
        while (it.hasNext()) {
            GameObject g = it.next();
            universe.add(g);
            it.remove();
        }
    }

    public void removeGameObject(GameObject go) {
        universeRemoveQueue.add(go);
    }

    private void removeGameObjects(ArrayList<GameObject> glist) {
        Iterator<GameObject> it = glist.iterator();
        while (it.hasNext()) {
            GameObject g = it.next();
            universe.remove(g);
            it.remove();
        }
    }

    /**
     * Draws everything in the universe, in order of Z-order.
     * @param g
     */
    public void draw(Graphics g) {
        if (background != null) background.draw();
        Iterator<GameObject> it = universe.iterator();
        while (it.hasNext()) {
            GameObject go = it.next();
            go.draw(g);
        }
    }

    /**
     * Carries out all additions and removals from the universe, then
     * calls update() on everything, and finally calls checkCollisions
     * on every moving object.
     */
    public void update() {
        removeMovingObjects(movingRemoveQueue);
        removeStationaryObjects(stationaryRemoveQueue);
        removeGameObjects(universeRemoveQueue);
        addMovingObjects(movingAddQueue);
        addStationaryObjects(stationaryAddQueue);
        addGameObjects(universeAddQueue);
        Iterator<GameObject> it = universe.iterator();
        while (it.hasNext()) {
            GameObject go = it.next();
            go.update();
        }
        for (MovingObject m : movingObjects) {
            m.checkCollisions(collidables);
        }
    }
}
