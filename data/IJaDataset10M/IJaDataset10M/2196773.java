package org.skycastle.scratchpad.evolvingcreatures.facets;

import org.skycastle.kernel.Entity;
import org.skycastle.kernel.EntityId;
import org.skycastle.kernel.Facet;
import org.skycastle.kernel.messaging.Message;
import org.skycastle.util.ParameterChecker;
import org.skycastle.util.parameters.MutableParameterSet;
import org.skycastle.util.parameters.ParameterSet;
import org.skycastle.util.spatialcontainer.AbstractLocatedObject;
import org.skycastle.util.spatialcontainer.PositionListener;

public final class LocationFacet extends AbstractLocatedObject implements Facet {

    private Entity myHostEntity;

    private double myDirection;

    private static final long serialVersionUID = 1L;

    private static final String SET_POSITION = "setPosition";

    /**
     * Creates a new {@link LocationFacet} with position at origo.
     */
    public LocationFacet() {
        this(0, 0);
    }

    /**
     * Creates a new {@link LocationFacet} with the specified position.
     *
     * @param x world coordinates for the {@link Entity} that this {@link LocationFacet} represents.
     * @param y world coordinates for the {@link Entity} that this {@link LocationFacet} represents.
     */
    public LocationFacet(final double x, final double y) {
        this(x, y, 0);
    }

    /**
     * Creates a new {@link LocationFacet} with the specified position.
     *
     * @param x         world coordinates for the {@link Entity} that this {@link LocationFacet} represents.
     * @param y         world coordinates for the {@link Entity} that this {@link LocationFacet} represents.
     * @param direction the initial direction, in radians, with zero is along positive x axis, and grows
     *                  clockwise.
     */
    public LocationFacet(final double x, final double y, final double direction) {
        super(x, y);
        myDirection = direction;
    }

    public void init(final Entity hostEntity) {
        ParameterChecker.checkNotNull(hostEntity, "hostEntity");
        myHostEntity = hostEntity;
        getSpaceFacet().addEntity(myHostEntity.getId());
        addPositionListener(createPositionListener());
    }

    public void deinit(final Entity hostEntity) {
        final SpaceFacet spaceFacet = getSpaceFacet();
        spaceFacet.removeEntity(myHostEntity.getId());
        myHostEntity = null;
    }

    public boolean shouldHandle(final String messageType) {
        return messageType.equals(SET_POSITION);
    }

    public Facet createCopy() {
        return new LocationFacet(getX(), getY(), myDirection);
    }

    public void getSensedParameters(final Entity observer, final MutableParameterSet parameterSet) {
        parameterSet.setParameter("x", getX());
        parameterSet.setParameter("y", getY());
        parameterSet.setParameter("direction", getDirection());
    }

    public void setParameters(final ParameterSet parameters) {
        setLocation(parameters.getDouble("x", getX()), parameters.getDouble("y", getY()));
        setDirection(parameters.getDouble("direction", getDirection()));
    }

    public void onMessage(final EntityId sender, final EntityId target, final Message message) {
        final double x = message.getMessageContent().getDouble("x", getX());
        final double y = message.getMessageContent().getDouble("y", getY());
        setLocation(x, y);
    }

    /**
     * @return the direction that the {@link Entity} is facing in, in radians.
     */
    public double getDirection() {
        return myDirection;
    }

    /**
     * @param direction the direction that the {@link Entity} is facing in, in radians.
     */
    public void setDirection(final double direction) {
        if (myDirection != direction) {
            myDirection = direction;
            notifyPositionChanged(getX(), getY(), myDirection);
        }
    }

    /**
     * @return the entity that contains this {@link Facet}.
     */
    public Entity getHostEntity() {
        return myHostEntity;
    }

    /**
     * @param dx amount to add to x position.
     * @param dy amount to add to y position.
     */
    public void addToPosition(final double dx, final double dy) {
        setLocation(getX() + dx, getY() + dy);
    }

    /**
     * @param movementAmount the amount to move the position in the direction specified by the direction
     *                       property.
     */
    public void moveInDirection(final double movementAmount) {
        final double direction = getDirection();
        double dx = movementAmount * Math.cos(direction);
        double dy = movementAmount * Math.sin(direction);
        addToPosition(dx, dy);
    }

    /**
     * @param otherLocation the location to get the distance to.
     *
     * @return the distance between the position indicated by this {@link LocationFacet} and the specified
     *         {@link LocationFacet} in world units. Returns Double.POSITIVE_INFINITY if the {@link
     *         LocationFacet}s are located in different spaces and no path between them could be found.
     */
    public double calculateDistanceTo(LocationFacet otherLocation) {
        ParameterChecker.checkNotNull(otherLocation, "otherLocation");
        double dx = getX() - otherLocation.getX();
        double dy = getY() - otherLocation.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Updates the position and direction of this {@link LocationFacet}.
     *
     * @param x   new world x position
     * @param y   new world y position
     * @param dir new direction (in radians)
     */
    public void setPositionAndDirection(final double x, final double y, final double dir) {
        if (x == getX() && y == getY()) {
            setDirection(dir);
        } else {
            myDirection = dir;
            setLocation(x, y);
        }
    }

    private PositionListener<LocationFacet> createPositionListener() {
        return new PositionListener<LocationFacet>() {

            public void onPositionChanged(final LocationFacet locatedObject, final double oldX, final double oldY, final double newX, final double newY) {
                if (oldX != newX || oldY != newY) {
                    notifyPositionChanged(newX, newY, getDirection());
                }
            }
        };
    }

    private void notifyPositionChanged(final double x, final double y, final double direction) {
        if (getHostEntity() != null) {
            getSpaceFacet().entityMoved(getHostEntity().getId(), x, y, direction);
        }
    }

    private SpaceFacet getSpaceFacet() {
        final Entity space = getHostEntity().getContainer().getEntity("Space");
        return space.getFacet(SpaceFacet.class);
    }

    private double calculateDistanceTo(final Entity observer) {
        final LocationFacet targetLocationFacet = observer.getFacet(LocationFacet.class);
        if (targetLocationFacet == null) {
            return Double.POSITIVE_INFINITY;
        } else {
            return calculateDistanceTo(targetLocationFacet);
        }
    }
}
