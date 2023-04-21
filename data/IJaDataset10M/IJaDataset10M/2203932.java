package org.skycastle.util.spatialcontainer;

import org.skycastle.util.ParameterChecker;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Hans Haggstrom
 */
public abstract class AbstractLocatedObject<T extends LocatedObject> implements LocatedObject<T> {

    private final Set<PositionListener<T>> myPositionListeners = new HashSet<PositionListener<T>>(3);

    private double myX;

    private double myY;

    public final double getX() {
        return myX;
    }

    public final double getY() {
        return myY;
    }

    public final void addPositionListener(PositionListener<T> positionListener) {
        ParameterChecker.checkNotNull(positionListener, "addedPositionListener");
        ParameterChecker.checkNotAlreadyContained(positionListener, myPositionListeners, "myPositionListeners");
        myPositionListeners.add(positionListener);
    }

    public final void removePositionListener(PositionListener<T> positionListener) {
        ParameterChecker.checkNotNull(positionListener, "removedPositionListener");
        ParameterChecker.checkContained(positionListener, myPositionListeners, "myPositionListeners");
        myPositionListeners.remove(positionListener);
    }

    /**
     * Creates a new {@link AbstractLocatedObject} located at origo (0,0).
     */
    protected AbstractLocatedObject() {
        this(0, 0);
    }

    /**
     * Creates a new {@link AbstractLocatedObject} located at the specified x and y coordinates.
     */
    protected AbstractLocatedObject(final double x, final double y) {
        setLocation(x, y);
    }

    /**
     * Sets the location of this object.  Also notifies any {@link PositionListener}s about the move.
     *
     * @param x new x coordinate
     * @param y new y coordinate
     */
    protected final void setLocation(final double x, final double y) {
        ParameterChecker.checkNormalNumber(x, "x");
        ParameterChecker.checkNormalNumber(y, "y");
        if (x != myX || y != myY) {
            double oldX = myX;
            double oldY = myY;
            myX = x;
            myY = y;
            for (PositionListener<T> positionListener : myPositionListeners) {
                positionListener.onPositionChanged((T) this, oldX, oldY, myX, myY);
            }
        }
    }
}
