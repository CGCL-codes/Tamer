package org.xith3d.scenegraph;

import org.openmali.vecmath2.Colorf;
import org.openmali.vecmath2.Point3f;
import org.openmali.vecmath2.Tuple3f;

/**
 * PointLight defines a point light source located at some point in space and
 * radiating in all directions.
 * 
 * @author David Yazel
 * @author Marvin Froehlich (aka Qudus)
 */
public class PointLight extends Light {

    public static final Point3f DEFAULT_LOCATION = Point3f.newReadOnly(0f, 0f, 0f);

    public static final Tuple3f DEFAULT_ATTENUATION = Tuple3f.newReadOnly(1f, 0f, 0f);

    private Node trackedNode = null;

    /**
     * The location of the light.
     */
    private Point3f location = new Point3f(DEFAULT_LOCATION);

    /**
     * The attenuation of the light.
     */
    private Tuple3f attenuation = new Tuple3f(DEFAULT_ATTENUATION);

    /**
     * Sets the Node to be tracked<br>
     * If this is not null, the PointLight's location will not be an absolute
     * one anymore, but relative to the tracked Node's world-transform.<br>
     * 
     * @param node
     */
    public void setTrackedNode(Node node) {
        this.trackedNode = node;
    }

    /**
     * @return the Node to be tracked<br>
     * If this is not null, the PointLight's location will not be an absolute
     * one anymore, but relative to the tracked Node's world-transform.<br>
     * <br>
     */
    public final Node getTrackedNode() {
        return (trackedNode);
    }

    /**
     * Sets the location for this object.
     * 
     * @param x
     * @param y
     * @param z
     * 
     * @return the internal point.
     */
    public Point3f setLocation(float x, float y, float z) {
        this.location.set(x, y, z);
        return (this.location);
    }

    /**
     * Sets the location for this object.
     * 
     * @param location
     * 
     * @return the internal point.
     */
    public final Point3f setLocation(Tuple3f location) {
        return (setLocation(location.getX(), location.getY(), location.getZ()));
    }

    /**
     * Gets the location for this object.
     * 
     * @return the internal point.
     */
    public final Point3f getLocation() {
        return (location);
    }

    /**
     * Gets the location for this object.
     * 
     * @param location
     * 
     * @param <T> type restriction.
     * 
     * @return the parameter tuple back again.
     */
    public final <T extends Tuple3f> T getLocation(T location) {
        location.set(this.location);
        return (location);
    }

    /**
     * If this light has a tracked-node, the light's location is transformed
     * by the tracked-node's world-transform.
     * If it doesn't have a tracked-node, the plain light's location is returned.
     * 
     * @param location
     */
    public final void getComputedLocation(Point3f location) {
        if (getTrackedNode() == null) getLocation(location); else getTrackedNode().getWorldTransform().transform(getLocation(), location);
    }

    /**
     * Sets the attenuation for this object.
     * 
     * @param attConstant
     * @param attLinear
     * @param attQuadratic
     * 
     * @return the internal attenuation.
     */
    public Tuple3f setAttenuation(float attConstant, float attLinear, float attQuadratic) {
        this.attenuation.set(attConstant, attLinear, attQuadratic);
        return (this.attenuation);
    }

    /**
     * Sets the attenuation for this object.
     * 
     * @param attenuation
     * 
     * @return the parameter instance.
     */
    public final Tuple3f setAttenuation(Tuple3f attenuation) {
        this.attenuation.set(attenuation);
        return (attenuation);
    }

    /**
     * Gets the attenuation for this object.
     * 
     * @return the attenuation for this object.
     */
    public final Tuple3f getAttenuation() {
        return (attenuation);
    }

    /**
     * Gets the attenuation for this object.
     * 
     * @param attenuation
     * 
     * @return the parameter value.
     */
    public final Tuple3f getAttenuation(Tuple3f attenuation) {
        attenuation.set(this.attenuation);
        return (attenuation);
    }

    /**
     * Constructs a new PointLight object with the specified color, location and
     * attenuation.
     * 
     * @param enabled
     * @param colorR
     * @param colorG
     * @param colorB
     * @param locX
     * @param locY
     * @param locZ
     * @param attenuationConstant
     * @param attenuationLinear
     * @param attenuationQuadratic
     */
    public PointLight(boolean enabled, float colorR, float colorG, float colorB, float locX, float locY, float locZ, float attenuationConstant, float attenuationLinear, float attenuationQuadratic) {
        super(enabled, colorR, colorG, colorB);
        this.location.set(locX, locY, locZ);
        this.attenuation.set(attenuationConstant, attenuationLinear, attenuationQuadratic);
    }

    /**
     * Constructs a new PointLight object with the specified color, location and
     * attenuation.
     * 
     * @param colorR
     * @param colorG
     * @param colorB
     * @param locX
     * @param locY
     * @param locZ
     * @param attenuationConstant
     * @param attenuationLinear
     * @param attenuationQuadratic
     */
    public PointLight(float colorR, float colorG, float colorB, float locX, float locY, float locZ, float attenuationConstant, float attenuationLinear, float attenuationQuadratic) {
        this(true, colorR, colorG, colorB, locX, locY, locZ, attenuationConstant, attenuationLinear, attenuationQuadratic);
    }

    /**
     * Constructs a new PointLight object with the specified color, location and
     * attenuation.
     * 
     * @param enabled
     * @param colorR
     * @param colorG
     * @param colorB
     * @param locX
     * @param locY
     * @param locZ
     * @param attenuation
     */
    public PointLight(boolean enabled, float colorR, float colorG, float colorB, float locX, float locY, float locZ, float attenuation) {
        this(enabled, colorR, colorG, colorB, locX, locY, locZ, attenuation, attenuation, attenuation);
    }

    /**
     * Constructs a new PointLight object with the specified color, location and
     * attenuation.
     * 
     * @param colorR
     * @param colorG
     * @param colorB
     * @param locX
     * @param locY
     * @param locZ
     * @param attenuation
     */
    public PointLight(float colorR, float colorG, float colorB, float locX, float locY, float locZ, float attenuation) {
        this(true, colorR, colorG, colorB, locX, locY, locZ, attenuation, attenuation, attenuation);
    }

    protected static final Tuple3f getAtt(Tuple3f att) {
        return ((att == null) ? DEFAULT_ATTENUATION : att);
    }

    protected static final Tuple3f getLoc(Tuple3f loc) {
        return ((loc == null) ? DEFAULT_LOCATION : loc);
    }

    /**
     * Constructs a new PointLight object with the specified color, location and
     * attenuation.
     * 
     * @param enabled
     * @param color
     * @param location
     * @param attenuation
     */
    public PointLight(boolean enabled, Colorf color, Tuple3f location, Tuple3f attenuation) {
        this(enabled, getCol(color).getRed(), getCol(color).getGreen(), getCol(color).getBlue(), getLoc(location).getX(), getLoc(location).getY(), getLoc(location).getZ(), getAtt(attenuation).getX(), getAtt(attenuation).getY(), getAtt(attenuation).getZ());
    }

    /**
     * Constructs a new PointLight object with the specified color, location and
     * attenuation.
     * 
     * @param color
     * @param location
     * @param attenuation
     */
    public PointLight(Colorf color, Tuple3f location, Tuple3f attenuation) {
        this(true, color, location, attenuation);
    }

    /**
     * Constructs a new PointLight object with the specified color, location and
     * attenuation.
     * 
     * @param enabled
     * @param color
     * @param location
     * @param attenuation
     */
    public PointLight(boolean enabled, Colorf color, Tuple3f location, float attenuation) {
        this(enabled, getCol(color).getRed(), getCol(color).getGreen(), getCol(color).getBlue(), getLoc(location).getX(), getLoc(location).getY(), getLoc(location).getZ(), attenuation, attenuation, attenuation);
    }

    /**
     * Constructs a new PointLight object with the specified color, location and
     * attenuation.
     * 
     * @param color
     * @param location
     * @param attenuation
     */
    public PointLight(Colorf color, Tuple3f location, float attenuation) {
        this(true, color, location, attenuation);
    }

    /**
     * Constructs a new PointLight object with the specified color, location and
     * attenuation.
     * 
     * @param enabled
     * @param colorR
     * @param colorG
     * @param colorB
     * @param trackedNode
     * @param attenuationConstant
     * @param attenuationLinear
     * @param attenuationQuadratic
     */
    public PointLight(boolean enabled, float colorR, float colorG, float colorB, Node trackedNode, float attenuationConstant, float attenuationLinear, float attenuationQuadratic) {
        this(enabled, colorR, colorG, colorB, DEFAULT_LOCATION.getX(), DEFAULT_LOCATION.getY(), DEFAULT_LOCATION.getZ(), attenuationConstant, attenuationLinear, attenuationQuadratic);
        this.setTrackedNode(trackedNode);
    }

    /**
     * Constructs a new PointLight object with the specified color, location and
     * attenuation.
     * 
     * @param colorR
     * @param colorG
     * @param colorB
     * @param trackedNode
     * @param attenuationConstant
     * @param attenuationLinear
     * @param attenuationQuadratic
     */
    public PointLight(float colorR, float colorG, float colorB, Node trackedNode, float attenuationConstant, float attenuationLinear, float attenuationQuadratic) {
        this(true, colorR, colorG, colorB, trackedNode, attenuationConstant, attenuationLinear, attenuationQuadratic);
    }

    /**
     * Constructs a new PointLight object with the specified color, location and
     * attenuation.
     * 
     * @param enabled
     * @param colorR
     * @param colorG
     * @param colorB
     * @param trackedNode
     * @param attenuation
     */
    public PointLight(boolean enabled, float colorR, float colorG, float colorB, Node trackedNode, float attenuation) {
        this(enabled, colorR, colorG, colorB, trackedNode, attenuation, attenuation, attenuation);
    }

    /**
     * Constructs a new PointLight object with the specified color, location and
     * attenuation.
     * 
     * @param colorR
     * @param colorG
     * @param colorB
     * @param trackedNode
     * @param attenuation
     */
    public PointLight(float colorR, float colorG, float colorB, Node trackedNode, float attenuation) {
        this(true, colorR, colorG, colorB, trackedNode, attenuation);
    }

    /**
     * Constructs a new PointLight object with the specified color, location and
     * attenuation.
     * 
     * @param enabled
     * @param color
     * @param trackedNode
     * @param attenuation
     */
    public PointLight(boolean enabled, Colorf color, Node trackedNode, Tuple3f attenuation) {
        this(enabled, color, DEFAULT_LOCATION, attenuation);
        this.setTrackedNode(trackedNode);
    }

    /**
     * Constructs a new PointLight object with the specified color, location and
     * attenuation.
     * 
     * @param color
     * @param trackedNode
     * @param attenuation
     */
    public PointLight(Colorf color, Node trackedNode, Tuple3f attenuation) {
        this(true, color, trackedNode, attenuation);
    }

    /**
     * Constructs a new PointLight object with the specified color, location and
     * attenuation.
     * 
     * @param enabled
     * @param color
     * @param trackedNode
     * @param attenuation
     */
    public PointLight(boolean enabled, Colorf color, Node trackedNode, float attenuation) {
        this(enabled, color, DEFAULT_LOCATION, attenuation);
        this.setTrackedNode(trackedNode);
    }

    /**
     * Constructs a new PointLight object with the specified color, location and
     * attenuation.
     * 
     * @param color
     * @param trackedNode
     * @param attenuation
     */
    public PointLight(Colorf color, Node trackedNode, float attenuation) {
        this(true, color, trackedNode, attenuation);
    }

    /**
     * Constructs a new PointLight object with a default color of white and
     * default location of (0,0,0).
     * 
     * @param enabled
     */
    public PointLight(boolean enabled) {
        this(enabled, DEFAULT_COLOR, DEFAULT_LOCATION, DEFAULT_ATTENUATION);
    }

    /**
     * Constructs a new PointLight object with a default color of white and
     * default location of (0,0,0).
     * 
     * @param enabled
     * @param trackedNode
     */
    public PointLight(boolean enabled, Node trackedNode) {
        this(enabled);
        this.setTrackedNode(trackedNode);
    }

    /**
     * Constructs a new PointLight object with a default color of white and
     * default location of (0,0,0).
     */
    public PointLight() {
        this(true);
    }
}
