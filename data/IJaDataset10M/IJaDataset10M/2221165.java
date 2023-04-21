package se.liu.oschi129.world.model;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.TreeSet;
import se.liu.oschi129.world.interfaces.InputAction;
import se.liu.oschi129.world.interfaces.WorldListener;
import se.liu.oschi129.world.objects.WorldMovableObject;
import se.liu.oschi129.world.objects.WorldObject;
import se.liu.oschi129.world.objects.WorldObject.Type;

/**
 * This class is a "World" in which WorldObjects can be added. The world-class also stores
 * a bunch of methods which i.e. can be used to check if a position is free or if an object
 * can be moved to a certain location without a collision.
 * 
 * Furthermore this class is the "model container" for the Model-View-Controller design
 * pattern. This class should be connected to a WorldViewController in order to work properly.
 * 
 * IMPORTANT! positionFree, placeFree, makeContact and objectExists takes O(n) time where
 * n is the number of objects in the world. If they are used within an object the total
 * time complexity of the performStepAction will be Ω(n²). For optimal usage of this class
 * the time complexity of the performStepAction should not be higher than Θ(n²).
 * 
 * @author oschi129
 */
public class World {

    private int lives;

    private int coins;

    private int points;

    private double time, timeTick;

    private double width, height;

    private WorldPhysicalRules rules;

    private WorldView view;

    private Object objectLock = new Object();

    private Object interactiveObjectLock = new Object();

    private final TreeSet<WorldObject> objects = new TreeSet<WorldObject>(new WorldDepthComparator());

    private final List<InputAction> interactiveObjects = new ArrayList<InputAction>();

    /** Synchronized content of the world (added to the real world and cleared at the end of the step) */
    private boolean clearObjects, clearInteractiveObjects;

    private final List<WorldObject> synchronizedObjects = new ArrayList<WorldObject>();

    private final List<InputAction> synchronizedInteractiveObjects = new ArrayList<InputAction>();

    private final List<WorldObject> synchronizedRemovedObjects = new ArrayList<WorldObject>();

    private final List<InputAction> synchronizedRemovedInteractiveObjects = new ArrayList<InputAction>();

    private final List<WorldListener> listeners = new ArrayList<WorldListener>();

    /** World stack */
    private boolean popWorld;

    private Stack<World> worldStack = new Stack<World>();

    /**
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * - - - - - - - - - - - - Constructors - - - - - - - - - - - 
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 */
    public World(WorldView view) {
        this(view, 640, 480);
    }

    public World(WorldView view, double width, double height) {
        this(view, width, height, 300, 0.05, 0, 0, 0);
    }

    public World(WorldView view, double width, double height, double time, double timeTick, int lives, int coins, int points) {
        setView(view);
        setWidth(width);
        setHeight(height);
        setTime(time);
        setTimeTick(timeTick);
        setLives(lives);
        setCoins(coins);
        setPoints(points);
        clearObjects = false;
        clearInteractiveObjects = false;
        popWorld = false;
    }

    /**
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 * - - - - - - - - - - - Public methods - - - - - - - - - - 
	 * - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
	 */
    public void setView(WorldView view) {
        this.view = view;
    }

    public WorldView getView() {
        return view;
    }

    /** Returns the width of the world */
    public double getWidth() {
        return width;
    }

    /** Returns the height of the world */
    public double getHeight() {
        return height;
    }

    /** Sets the width of the world */
    public void setWidth(double width) {
        this.width = width;
    }

    /** Sets the height of the world */
    public void setHeight(double height) {
        this.height = height;
    }

    /** Returns the physical rules in the world */
    public WorldPhysicalRules getPhysicalRules() {
        return rules;
    }

    /** Sets the physical rules in the world */
    public void setPhysicalRules(WorldPhysicalRules rules) {
        this.rules = rules;
    }

    /** Returns the current amount of lives */
    public int getLives() {
        return lives;
    }

    /** Sets the amount of lives */
    public void setLives(int lives) {
        this.lives = lives;
    }

    /** Returns the current amount of lives */
    public int getCoins() {
        return coins;
    }

    /** Sets the amount of lives */
    public void setCoins(int coins) {
        this.coins = coins;
        if (this.coins >= 100) {
            lives++;
            coins -= 100;
        }
    }

    /** Sets the amount of points */
    public void setPoints(int points) {
        this.points = points;
    }

    /** Returns the current amount of points */
    public int getPoints() {
        return points;
    }

    /** Returns the time (as an integer) */
    public double getTime() {
        return time;
    }

    /** Sets the time */
    public void setTime(double time) {
        this.time = Math.max(0, time);
    }

    /** Returns the countdown speed of the time */
    public double getTimeTick() {
        return timeTick;
    }

    /** Sets the time difference (countdown speed) for each step */
    public void setTimeTick(double timeTick) {
        this.timeTick = Math.max(0, timeTick);
    }

    /** Returns the synchronization lock for the objects in the world */
    public Object getObjectLock() {
        return objectLock;
    }

    /** Returns the synchronization lock for the interactive objects in the world */
    public Object getInteractiveObjectLock() {
        return interactiveObjectLock;
    }

    /** Adds an object to the world */
    public void addObject(WorldObject obj) {
        objects.add(obj);
    }

    /** Adds an object to the world in the next step */
    public void addObjectSynchronized(WorldObject obj) {
        synchronizedObjects.add(obj);
    }

    /** Adds an object to the world in the next step and set it position */
    public void addObjectSynchronized(WorldObject obj, double x, double y) {
        obj.setX(x);
        obj.setY(y);
        synchronizedObjects.add(obj);
    }

    /** Adds an interactive object to the world (this does not add the object to the world) */
    public void addInteractiveObject(InputAction obj) {
        interactiveObjects.add(obj);
    }

    /** Removes an object from the world in the next step */
    public void removeObjectSynchronized(WorldObject obj) {
        synchronizedRemovedObjects.add(obj);
    }

    /** Removes an interactive object from the world (this does not remove the object to the world) */
    public void removeInteractiveObjectSynchronized(InputAction obj) {
        synchronizedRemovedInteractiveObjects.add(obj);
    }

    /** Adds an interactive object to the world (this does not add the object to the world) in the next step */
    public void addInteractiveObjectSynchronized(InputAction obj) {
        synchronizedInteractiveObjects.add(obj);
    }

    /** Removes an object from the world and perform its destruction action */
    public void removeObject(WorldObject obj) {
        obj.performDestructionAction(this);
        objects.remove(obj);
    }

    /** Removes an interactive object from the world */
    public void removeInteractiveObject(InputAction obj) {
        interactiveObjects.remove(obj);
    }

    /** Adds a listener to this object */
    public void addListener(WorldListener listener) {
        this.listeners.add(listener);
    }

    /** Removes a listener from this object */
    public void removeListener(WorldListener listener) {
        this.listeners.remove(listener);
    }

    /** Clears the whole content of the world */
    public void clear() {
        clearObjects();
        clearInteractiveObjects();
    }

    /** Clears all objects in world */
    public void clearObjects() {
        objects.clear();
        synchronizedObjects.clear();
        synchronizedRemovedObjects.clear();
    }

    /** Clears all interactive objects in world */
    public void clearInteractiveObjects() {
        interactiveObjects.clear();
        synchronizedInteractiveObjects.clear();
        synchronizedRemovedInteractiveObjects.clear();
    }

    /** Clears the whole content of the world in the end of the step */
    public void clearSynchronized() {
        clearObjectsSynchronized();
        clearInteractiveObjectsSynchronized();
    }

    /** Clears all objects in the end of the step */
    public void clearObjectsSynchronized() {
        clearObjects = true;
    }

    /** Clears all interactive objects in the end of the step */
    public void clearInteractiveObjectsSynchronized() {
        clearInteractiveObjects = true;
    }

    /** Returns an unmodifiable list of all objects in the world */
    public Collection<WorldObject> getObjects() {
        return Collections.unmodifiableSortedSet(objects);
    }

    /** Returns an unmodifiable list of all objects in the world */
    public Collection<InputAction> getInteractiveObjects() {
        return Collections.<InputAction>unmodifiableList(interactiveObjects);
    }

    /** Pushes the current world into the world stack and clears the current world. 
	 * The world stack will store all objects, interactive objects, the world size and a clone of the physical rules for the world. */
    public void pushSyncrhonized() {
        World worldStorage = new World(getView().clone(), getWidth(), getHeight());
        for (WorldObject obj : getObjects()) worldStorage.addObject(obj);
        for (InputAction obj : getInteractiveObjects()) worldStorage.addInteractiveObject(obj);
        worldStorage.setPhysicalRules(getPhysicalRules().clone());
        worldStack.push(worldStorage);
        clearSynchronized();
    }

    /** Pops a world from the world stack (if the stack is not empty) after clearing the current world.
	 * The pop method will load objects, interactive objects, the world size and the physical rules for the stored world. */
    public void popSynchronized() {
        popWorld = true;
        clearSynchronized();
    }

    /** Clears the world stack, removing all stored worlds in the stack */
    public void clearStack() {
        worldStack.clear();
    }

    /** Performs a step action for all objects in the world */
    public void performStepAction() {
        setTime(getTime() - getTimeTick());
        for (WorldObject obj : objects) {
            obj.performStepAction(this);
        }
        synchronizeWorldChanges();
        synchronizeWorldPop();
        view.update(this);
        notifyListeners();
    }

    /** Returns true if position (x,y) is free. 
	 * This method takes O(n) time where n is the number of objects in the world. */
    public boolean positionFree(double x, double y) {
        for (WorldObject obj : objects) {
            if (obj.contains(x, y)) return false;
        }
        return true;
    }

    /** Returns true if the WorldObject obj can be moved to (x,y) without a collision with a solid object or a platform.
	 * This method takes O(n) time where n is the number of objects in the world. */
    public boolean placeFree(WorldMovableObject obj, double x, double y) {
        Point2D.Double point = obj.getPosition();
        obj.setPosition(new Point2D.Double(x, y));
        for (WorldObject other : objects) {
            if (obj.collision(other) && obj != other) {
                if (other.getType() == Type.SOLID) {
                    obj.setPosition(point);
                    return false;
                } else if (other.getType() == Type.PLATFORM && point.getY() + obj.getHeight() <= other.getY()) {
                    obj.setPosition(point);
                    return false;
                }
            }
        }
        obj.setPosition(point);
        return true;
    }

    /** Moves obj stepwise (dx,dy) until a collision occurs within 16 steps.
	 * This method takes O(n) time where n is the number of objects in the world. */
    public void makeContact(WorldMovableObject obj, double dx, double dy) {
        makeContact(obj, dx, dy, 16 * Math.sqrt(dx * dx + dy * dy));
    }

    /** Moves obj stepwise (dx,dy) until a collision occurs within maxDistance.
	 * This method takes O(n) time where n is the number of objects in the world. */
    public void makeContact(WorldMovableObject obj, double dx, double dy, double maxDistance) {
        double movedDistance = 0, stepDistance = Math.sqrt(dx * dx + dy * dy);
        Point2D.Double point = obj.getPosition();
        while (placeFree(obj, obj.getX() + dx, obj.getY() + dy)) {
            obj.setX(obj.getX() + dx);
            obj.setY(obj.getY() + dy);
            movedDistance += stepDistance;
            if (maxDistance < movedDistance) {
                obj.setPosition(point);
            }
        }
    }

    /** If a world object exists then return the first found instance of the object, otherwise null.
	 * This method takes O(n) time where n is the number of objects in the world. */
    @SuppressWarnings("rawtypes")
    public WorldObject objectExists(Class object) {
        for (WorldObject obj : getObjects()) {
            if (object.isInstance(obj)) return obj;
        }
        return null;
    }

    /** Returns true if obj exists in the world 
	 * This method takes O(log n) time where n is the number of objects in the world. */
    public boolean objectExists(WorldObject obj) {
        return objects.contains(obj);
    }

    /** Notifies to all listeners that the world has been changed */
    private void notifyListeners() {
        for (WorldListener listener : listeners) {
            listener.notifyWorldChanged();
        }
    }

    /** Adds and removes objects from the world which has been added with (...)Synchronized methods.*/
    private void synchronizeWorldChanges() {
        synchronized (getObjectLock()) {
            if (!synchronizedRemovedObjects.isEmpty()) {
                for (WorldObject obj : synchronizedRemovedObjects) {
                    removeObject(obj);
                }
                synchronizedRemovedObjects.clear();
            }
            if (clearObjects) {
                synchronizedRemovedObjects.clear();
                objects.clear();
                clearObjects = false;
            }
            if (!synchronizedObjects.isEmpty()) {
                objects.addAll(synchronizedObjects);
                synchronizedObjects.clear();
            }
        }
        synchronized (getInteractiveObjectLock()) {
            if (!synchronizedRemovedInteractiveObjects.isEmpty()) {
                for (InputAction obj : synchronizedRemovedInteractiveObjects) {
                    removeInteractiveObject(obj);
                }
                synchronizedRemovedInteractiveObjects.clear();
            }
            if (clearInteractiveObjects) {
                synchronizedRemovedInteractiveObjects.clear();
                interactiveObjects.clear();
                clearInteractiveObjects = false;
            }
            if (!synchronizedInteractiveObjects.isEmpty()) {
                interactiveObjects.addAll(synchronizedInteractiveObjects);
                synchronizedInteractiveObjects.clear();
            }
        }
    }

    /** Pops the world in the end of the step after using the popSynchronized method */
    private void synchronizeWorldPop() {
        if (popWorld) synchronized (getObjectLock()) {
            synchronized (getInteractiveObjectLock()) {
                popWorld = false;
                if (!worldStack.isEmpty()) {
                    World storedWorld = worldStack.pop();
                    for (WorldObject obj : storedWorld.getObjects()) addObject(obj);
                    for (InputAction obj : storedWorld.getInteractiveObjects()) addInteractiveObject(obj);
                    setWidth(storedWorld.getWidth());
                    setHeight(storedWorld.getHeight());
                    setView(storedWorld.getView());
                    setPhysicalRules(storedWorld.getPhysicalRules());
                }
            }
        }
    }
}
