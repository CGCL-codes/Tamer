package com.agentfactory.vacworld.vacGui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Holds the contents and location of a single square in the grid.
 * Each square can hold 0..1 ExclusiveObject (e.g. a VacBot) 
 * and 0..* non-exclusive GhostObjects (e.g. a piece of Dust).
 */
public class Square {

    private ExclusiveObject exclusiveObject;

    private List<GhostObject> ghostObjects = new ArrayList<GhostObject>();

    private final GridPoint location;

    /**
	 * Simple constructor to make each SquareContents instance aware of its location
	 * @param location This square's location on the grid
	 */
    public Square(GridPoint location) {
        this.location = location;
    }

    public GridPoint getLocation() {
        return location;
    }

    public void setExclusiveObject(ExclusiveObject exclusiveObject) {
        this.exclusiveObject = exclusiveObject;
    }

    public ExclusiveObject getExclusiveObject() {
        return exclusiveObject;
    }

    /**
	 * Test if this square is occupied by a non-permanent (Movable) object.
	 * @return true if the square is occupied, otherwise false.
	 */
    public boolean isOccupied() {
        if (exclusiveObject == null) return false;
        if (exclusiveObject instanceof Movable) return true;
        return false;
    }

    /**
	 * Test if this square is obstructed by a permanent Obstacle.
	 * @return true if the square is obstructed, otherwise false.
	 */
    public boolean isObstruction() {
        if (exclusiveObject == null) return false;
        if (exclusiveObject instanceof Obstacle) return true;
        return false;
    }

    public synchronized void addGhostObject(GhostObject ghostObject) {
        ghostObjects.add(ghostObject);
    }

    /**
	 * Get the topmost (i.e. most recently added) GhostObject that matches the given type.
	 * Returns null if there are no matching instances of GhostObject in this square.
	 * @return The most recently added matching GhostObject, or null if none exists.
	 */
    @SuppressWarnings("unchecked")
    public synchronized GhostObject getGhostObject(Class c) {
        GhostObject lastFound = null;
        Iterator<GhostObject> i = ghostObjects.iterator();
        while (i.hasNext()) {
            GhostObject ghostObject = i.next();
            if (ghostObject.getClass() == c) {
                lastFound = ghostObject;
            }
        }
        return lastFound;
    }

    /**
	 * Delete the given GhostObject, if it is in this square.
	 * If the object is not found, does nothing.
	 * @param ghostObject A GhostObject in this square, to be deleted.
	 */
    public synchronized void removeGhostObject(GhostObject ghostObject) {
        ghostObjects.remove(ghostObject);
    }

    /**
	 * For your convenience, a type-specific wrapper for getGhostObject ;)
	 * Hides away an ugly cast.
	 * @return The topmost instance of Dust in this square, or null if none is found.
	 */
    public Dust getDust() {
        return (Dust) (getGhostObject(Dust.class));
    }

    public boolean isDusty() {
        if (this.getDust() != null) return true; else return false;
    }

    /**
	 * Returns a count of the ghost and exclusive objects currently occupying this square.
	 * @return the total number of objects in this square
	 */
    public synchronized int getCount() {
        int count = ghostObjects.size();
        if (exclusiveObject != null) count++;
        return count;
    }

    public String toString() {
        boolean separator = false;
        String s = "Square " + location + ":";
        if (exclusiveObject != null) {
            s += " " + exclusiveObject;
            separator = true;
        }
        for (GhostObject go : ghostObjects) {
            if (separator) s += ";";
            s += " " + go;
            separator = true;
        }
        return s;
    }

    /**
	 * Get an iterator to iterate through the contents in drawing order.
	 * @return a SquareIterator in bottom-up order.
	 */
    public SquareIterator iterator() {
        return new SquareIterator(ghostObjects, this);
    }
}
