package logic.game;

import geom.Point;

/**
 * Abstract class responsible for the common fields and methods of objects contained by the {@link GameField} class
 * @author Peter Zastoupil, peterzastoupil@gmail.com
 *
 */
public abstract class GameObject {

    /**
	 * Represents the GameObject's position in the {@link GameField}
	 */
    Point position;

    /**
	 * Represents the GameObject's unique ID relative to the {@link GameField}
	 */
    int objectID;

    /**
	 * Sets the position of the GameObject
	 * @param position is the new position of the GameObject
	 */
    void setPosition(Point position) {
        this.position = position;
    }

    /**
	 * Returns the position of the GameObject as a {@link Point} object
	 * @return the position of the GameObject
	 */
    Point getPosition() {
        return this.position;
    }

    /**
	 * Sets the ID number of the GameObject
	 * @param objectID
	 */
    abstract void setObjectID(int objectID);

    /**
	 * Returns the GameObject's numeric ID as an Integer
	 * @return the GameObject's ID number
	 */
    int getObjectID() {
        return this.objectID;
    }
}
