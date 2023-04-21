package org.mazix.component3D;

import static java.util.logging.Level.SEVERE;
import static javax.media.j3d.BranchGroup.ALLOW_DETACH;
import static javax.media.j3d.Group.ALLOW_CHILDREN_EXTEND;
import static javax.media.j3d.Group.ALLOW_CHILDREN_WRITE;
import static javax.media.j3d.TransformGroup.ALLOW_TRANSFORM_READ;
import static javax.media.j3d.TransformGroup.ALLOW_TRANSFORM_WRITE;
import static org.mazix.constants.GlobalConstants.X_DEFAULT;
import static org.mazix.constants.GlobalConstants.Y_DEFAULT;
import static org.mazix.constants.GlobalConstants.Z_DEFAULT;
import static org.mazix.constants.log.ErrorConstants.UNEXPECTED_ERROR;
import java.util.logging.Logger;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3f;

/**
 * The abstract class which defines the global structure of all 3D objects of the game.
 * 
 * @author Benjamin Croizet (graffity2199@yahoo.fr)
 * @since 0.6
 * @version 0.7
 */
public abstract class GraphicGameComponent implements Cloneable {

    /** The class logger. */
    private static final Logger LOGGER = Logger.getLogger("org.mazix.component3D.GraphicGameComponent");

    /**
     * The 3D {@code BranchGroup} containing all 3D functional shapes. This {@code BranchGroup}
     * contains the most important elements, the ones which are compulsory to describes the 3D
     * object.
     */
    private BranchGroup coreShape = null;

    /**
     * The 3D {@code BranchGroup} containing all 3D decoration shapes. This {@code BranchGroup}
     * contains the less important elements, the ones which aren't necessary and could be removed
     * from the 3D object. It is used to improve the graphical renderer of the object.
     */
    private BranchGroup graphicShape = null;

    /**
     * The {@code TransformGroup} of the object which manages the global position in the 3D scene of
     * the object.
     */
    private TransformGroup transGroup = null;

    /**
     * The {@code Vector3f} containing the position in the 3D scene of the object.
     */
    private Vector3f position = null;

    /**
     * Protected default constructor, to prevent from instantiation. Sets the object position to
     * default.
     * 
     * @since 0.6
     * @see #GraphicGameComponent(Vector3f)
     * @see #GraphicGameComponent(float, float, float)
     */
    protected GraphicGameComponent() {
        this(new Vector3f(X_DEFAULT, Y_DEFAULT, Z_DEFAULT));
    }

    /**
     * Protected constructor, to prevent from instantiation. May throw a {@code
     * NullPointerException} if {@code position} is <code>null</code>.
     * 
     * @param xCoord
     *            the x coordinate of the object.
     * @param yCoord
     *            the y coordinate of the object.
     * @param zCoord
     *            the z coordinate of the object.
     * @since 0.6
     * @see #GraphicGameComponent()
     * @see #GraphicGameComponent(Vector3f)
     */
    protected GraphicGameComponent(final float xCoord, final float yCoord, final float zCoord) {
        this(new Vector3f(xCoord, yCoord, zCoord));
    }

    /**
     * Protected constructor, to prevent from instantiation. May throw a {@code
     * NullPointerException} if {@code position} is <code>null</code>.
     * 
     * @param position
     *            the {@code Vector3f} containing the 3D position of the object, can't be
     *            <code>null</code>.
     * @since 0.6
     * @see #GraphicGameComponent()
     * @see #GraphicGameComponent(float, float, float)
     */
    protected GraphicGameComponent(final Vector3f position) {
        assert position != null : "position is null";
        setPosition(position);
    }

    /**
     * Build or rebuild the 3D core shape of the object at the current object position. This method
     * sets the core shape of the object but also the 3D graphical shape.
     * 
     * @return the object 3D core shape.
     * @see javax.media.j3d.BranchGroup
     * @since 0.6
     */
    private BranchGroup buildCoreShape() {
        final BranchGroup shape = new BranchGroup();
        shape.setCapability(ALLOW_DETACH);
        shape.setCapability(ALLOW_CHILDREN_WRITE);
        shape.setCapability(ALLOW_CHILDREN_EXTEND);
        shape.addChild(getTransGroup());
        return shape;
    }

    /**
     * Builds or rebuilds the graphic shape of the object which manages to create graphic objects at
     * the current position. This method is used to sets all lights, backgrounds and so on. It also
     * sets the {@code BranchGroup} capability to update it. This method calls the
     * buildAdditionalGraphicShape() method which allows lower class to manage their own graphics.
     * 
     * @see javax.media.j3d.BranchGroup
     * @since 0.6
     */
    protected abstract BranchGroup buildGraphicShape();

    /**
     * Build or rebuild the 3D transform group of the object at the current object position.
     * 
     * @return the object transform group to apply following object position.
     * @see javax.media.j3d.TransformGroup
     * @since 0.7
     */
    private TransformGroup buildTransformGroup() {
        assert getPosition() != null : "getPosition() is null";
        final Transform3D trans = new Transform3D();
        trans.setTranslation(getPosition());
        final TransformGroup transGr = new TransformGroup(trans);
        transGr.setCapability(ALLOW_TRANSFORM_READ);
        transGr.setCapability(ALLOW_TRANSFORM_WRITE);
        transGr.setCapability(ALLOW_DETACH);
        transGr.setCapability(ALLOW_CHILDREN_WRITE);
        transGr.setCapability(ALLOW_CHILDREN_EXTEND);
        transGr.addChild(getGraphicShape());
        return transGr;
    }

    /**
     * This method completely resets 3D shapes of the object. After having called this method, the
     * graphic object is considered as non built.
     * 
     * @since 0.6
     */
    public void clearGraphic() {
        setCoreShape(null);
        setGraphicShape(null);
        setTransGroup(null);
    }

    /**
     * This method clones entirely the graphic object.
     * 
     * @see java.lang.Object#clone()
     * @since 0.6
     */
    @Override
    public GraphicGameComponent clone() {
        assert getPosition() != null : "getPosition() is null";
        GraphicGameComponent g = null;
        try {
            g = (GraphicGameComponent) super.clone();
            g.setPosition((Vector3f) getPosition().clone());
            g.clearGraphic();
        } catch (final CloneNotSupportedException cnse) {
            LOGGER.log(SEVERE, UNEXPECTED_ERROR, cnse);
        }
        return g;
    }

    /**
     * Gets the value of coreShape. To get the shape, the shape needs to be build if it hasn't been
     * built before.
     * 
     * @return the value of coreShape.
     * @see javax.media.j3d.BranchGroup
     * @since 0.6
     */
    public BranchGroup getCoreShape() {
        if (!isBuilt()) {
            coreShape = buildCoreShape();
        }
        return coreShape;
    }

    /**
     * Gets the value of graphicShape. To get the shape, the shape needs to be build if it hasn't
     * been built before.
     * 
     * @return the value of graphicShape.
     * @see javax.media.j3d.BranchGroup
     * @see #setGraphicShape(BranchGroup)
     * @since 0.6
     */
    protected BranchGroup getGraphicShape() {
        if (graphicShape == null) {
            graphicShape = buildGraphicShape();
        }
        return graphicShape;
    }

    /**
     * Gets the value of position.
     * 
     * @return the value of position.
     * @see #setPosition(Vector3f)
     * @since 0.6
     */
    public Vector3f getPosition() {
        return position;
    }

    /**
     * Gets the value of transGroup.
     * 
     * @return the value of transGroup.
     * @see #setTransGroup(TransformGroup)
     * @since 0.7
     */
    protected TransformGroup getTransGroup() {
        if (transGroup == null) {
            transGroup = buildTransformGroup();
        }
        return transGroup;
    }

    /**
     * This method tells if the 3D object shape has been built. It is mainly use to know if the
     * shape has to be updated when a field is changed.
     * 
     * @return <code>true</code> if the 3D object shape has been built, <code>false</code>
     *         otherwise.
     * @since 0.6
     */
    protected boolean isBuilt() {
        return coreShape != null;
    }

    /**
     * Sets the value of core shape.
     * 
     * @param value
     *            the new core shape of the object. Can be {code null}.
     * @since 0.7
     * @see #getCoreShape()
     */
    private void setCoreShape(final BranchGroup value) {
        coreShape = value;
    }

    /**
     * Sets the value of graphic shape.
     * 
     * @param value
     *            the new graphic shape of the object. Can be {code null}.
     * @since 0.7
     * @see #getGraphicShape()
     */
    protected void setGraphicShape(final BranchGroup value) {
        graphicShape = value;
    }

    /**
     * Sets the value of position. May throw a {@code NullPointerException} if {@code value} is
     * <code>null</code>. This methods updates the position of the shape if is has been built.
     * 
     * @param xCoord
     *            the x coordinate of the object.
     * @param yCoord
     *            the y coordinate of the object.
     * @param zCoord
     *            the z coordinate of the object.
     * 
     * @see #setPosition(Vector3f)
     * @since 0.6
     */
    public void setPosition(final float xCoord, final float yCoord, final float zCoord) {
        setPosition(new Vector3f(xCoord, yCoord, zCoord));
    }

    /**
     * Sets the value of position. May throw a {@code NullPointerException} if {@code value} is
     * <code>null</code>. This methods updates the position of the shape if is has been built.
     * 
     * @param value
     *            the {@code Vector3f} containing the 3D position of the object, can't be
     *            <code>null</code>.
     * @see #getPosition()
     * @since 0.6
     */
    public void setPosition(final Vector3f value) {
        assert value != null : "value is null";
        position = value;
        updatePosition();
    }

    /**
     * Sets the value of transform group.
     * 
     * @param value
     *            the new transform group of the object. Can be {code null}.
     * @since 0.7
     * @see #getTransGroup()
     */
    private void setTransGroup(final TransformGroup value) {
        transGroup = value;
    }

    /**
     * @see java.lang.Object#toString()
     * @since 0.6
     */
    @Override
    public String toString() {
        return "Object position : " + getPosition() + ", is built : " + isBuilt();
    }

    /**
     * This method updates the object position if the shape has been built.
     * 
     * @since 0.6
     * @version 0.7
     */
    private void updatePosition() {
        assert getPosition() != null : "getPosition() is null";
        if (isBuilt()) {
            assert getTransGroup() != null : "getTransGroup() is null";
            final Transform3D trans = new Transform3D();
            trans.setTranslation(getPosition());
            getTransGroup().setTransform(trans);
        }
    }
}
