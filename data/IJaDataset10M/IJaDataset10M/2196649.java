package pl.org.minions.utils.ui.sprite;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

/**
 * Core of the sprite rendering engine.
 * <p>
 * Manages creation of sprites.
 * <p>
 * Manages tiled background.
 * <p>
 * Paints visible part of background and visible sprites.
 */
public class SpriteCanvas {

    /**
     * Abstract class for canvas layers.
     */
    public abstract class CanvasLayer {

        private CanvasLayerGroup group;

        /**
         * Creates a new canvas layer and registers it in
         * the encapsulating canvas object as the topmost
         * layer in the default layer group.
         */
        protected CanvasLayer() {
            this(defaultLayerGroup);
        }

        /**
         * Creates a new canvas layer and registers it in
         * the encapsulating canvas object as the topmost
         * layer in the selected layer group.
         * @param group
         *            layer group to contain the new layer
         */
        protected CanvasLayer(CanvasLayerGroup group) {
            this.group = group;
            group.layers.add(this);
        }

        /**
         * Returns the encapsulating canvas.
         * @return canvas that contains this layer.
         */
        public final SpriteCanvas getCanvas() {
            return SpriteCanvas.this;
        }

        /**
         * Returns layer group this layer is in.
         * @return group
         */
        public final CanvasLayerGroup getGroup() {
            return group;
        }

        /**
         * Paint this layer using given graphics object.
         * <p>
         * The viewport transformation and clipping are
         * already applied to the graphics object.
         * @param graphics
         *            graphics object to paint the layer
         *            with
         */
        public abstract void render(Graphics2D graphics);
    }

    /**
     * A part of {@link SpriteCanvas} that groups
     * {@link CanvasLayer canvas layers}.
     */
    public class CanvasLayerGroup {

        private List<CanvasLayer> layers = new Vector<CanvasLayer>();

        /**
         * Creates a new layer group.
         * <p>
         * Equivalent to
         * {@link SpriteCanvas#newLayerGroup()}
         */
        public CanvasLayerGroup() {
            SpriteCanvas.this.layerGroups.add(this);
        }

        /**
         * Creates a new layer group at specified index.
         * <p>
         * Equivalent to
         * {@link SpriteCanvas#newLayerGroupAt(int)}
         * @param index
         *            new layer group position.
         */
        public CanvasLayerGroup(int index) {
            SpriteCanvas.this.layerGroups.add(index, this);
        }

        /**
         * Removes all the canvas layers from this group.
         * @see java.util.List#clear()
         */
        public void clear() {
            layers.clear();
        }

        /**
         * Checks if this group contains the selected canvas
         * layer.
         * @param layer
         *            canvas layer to check
         * @return <code>true</code>, if this group contains
         *         the layer
         * @see java.util.List#contains(java.lang.Object)
         */
        public boolean contains(CanvasLayer layer) {
            return layers.contains(layer);
        }

        /**
         * Returns the canvas layer at specified position.
         * @param index
         *            position (counting from bottom to top)
         *            of the layer to get.
         * @return the layer at specified position
         * @see java.util.List#get(int)
         */
        public CanvasLayer get(int index) {
            return layers.get(index);
        }

        /**
         * Returns the canvas that this group is a part of.
         * @return canvas
         */
        public final SpriteCanvas getCanvas() {
            return SpriteCanvas.this;
        }

        /**
         * Returns the position of specified canvas layer in
         * this group (counting from bottom to top).
         * @param layer
         *            layer to check the index for
         * @return the position index
         * @see java.util.List#indexOf(java.lang.Object)
         */
        public int indexOf(CanvasLayer layer) {
            return layers.indexOf(layer);
        }

        /**
         * Checks if this group contains no canvas layers.
         * @return <code>true</code> if this group is empty
         * @see java.util.List#isEmpty()
         */
        public boolean isEmpty() {
            return layers.isEmpty();
        }

        /**
         * Removes the selected canvas layer from this
         * group.
         * @param layer
         *            the layer to remove
         * @return <code>true</code> if a layer was removed
         * @see java.util.List#remove(java.lang.Object)
         */
        public boolean remove(CanvasLayer layer) {
            return layers.remove(layer);
        }

        /**
         * Removes the canvas layer at specified position.
         * @param index
         *            position (counting from bottom to top)
         *            of the layer to remove
         * @return the removed layer, if a layer was removed
         * @see java.util.List#remove(int)
         */
        public CanvasLayer remove(int index) {
            return layers.remove(index);
        }

        /**
         * Removes all the canvas layers contained in
         * provided collection.
         * @param c
         *            collection of sprite layers
         * @return <code>true</code> if any element was
         *         removed
         * @see java.util.List#removeAll(java.util.Collection)
         */
        public boolean removeAll(Collection<? extends CanvasLayer> c) {
            return layers.removeAll(c);
        }

        /**
         * Returns the number of layers in this group.
         * @return the number of layers contained
         * @see java.util.List#size()
         */
        public int size() {
            return layers.size();
        }
    }

    /**
     * Represents a rectangular part of canvas that is
     * currently visible.
     */
    public class Viewport {

        private Rectangle bounds = new Rectangle();

        /**
         * Sets the center of the canvas viewport to given
         * location.
         * @param x
         *            horizontal coordinate in pixels
         * @param y
         *            vertical coorinate in pixels
         */
        public void centerAt(int x, int y) {
            bounds.setLocation(x - bounds.width / 2, y - bounds.height / 2);
        }

        /**
         * Sets the center of the canvas viewport to given
         * location.
         * @param location
         *            point on canvas in pixels
         */
        public void centerAt(Point location) {
            centerAt(location.x, location.y);
        }

        /**
         * Returns bounds.
         * @return bounds
         */
        public Rectangle getBounds() {
            return bounds;
        }

        /**
         * Sets new value of bounds.
         * @param bounds
         *            the bounds to set
         */
        public void setBounds(Rectangle bounds) {
            this.bounds = bounds;
        }

        /**
         * Returns the location in world coordinates of a
         * point within view.
         * <p>
         * Equivalent to: <br/>
         * {@code new Point(location.x + getBounds().x,
         * location.y + getBounds().y) }
         * @param location
         *            point in view coordinates in pixels
         * @return point in world coordinates in pixels
         */
        public Point viewToWorld(Point location) {
            return new Point(location.x + bounds.x, location.y + bounds.y);
        }

        /**
         * Returns the location within view of a point in
         * world coordinates.
         * <p>
         * Equivalent to: <br/>
         * {@code new Point(location.x - getBounds().x,
         * location.y - getBounds().y)}
         * @param location
         *            point in world coordinates in pixels
         * @return point in view coordinates in pixels
         */
        public Point worldToView(Point location) {
            return new Point(location.x - bounds.x, location.y - bounds.y);
        }
    }

    private Viewport viewport = new Viewport();

    private Color backgroundColor;

    private GraphicsConfiguration graphicsConfiguration;

    private List<CanvasLayerGroup> layerGroups = new Vector<CanvasLayerGroup>();

    private CanvasLayerGroup defaultLayerGroup = new CanvasLayerGroup();

    /**
     * Creates a new sprite canvas.
     */
    public SpriteCanvas() {
    }

    /**
     * Returns background color.
     * @return background color
     */
    public final Color getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Returns graphicsConfiguration.
     * @return graphicsConfiguration
     */
    public final GraphicsConfiguration getGraphicsConfiguration() {
        return graphicsConfiguration;
    }

    /**
     * Returns viewport.
     * @return viewport
     */
    public final Viewport getViewport() {
        return viewport;
    }

    /**
     * Adds a new layer group to the top of this canvas.
     * @return the created group
     */
    public CanvasLayerGroup newLayerGroup() {
        return new CanvasLayerGroup();
    }

    /**
     * Adds a new layer group to this canvas after (above)
     * the specified group, shifting all the groups after
     * specified group upwards.
     * <p>
     * If the specified group is <code>null</code> or does
     * not belong to this canvas, the new group is created
     * at the beginning (bottom) of the canvas.
     * @param group
     *            group to place the new group after
     * @return the created group
     */
    public CanvasLayerGroup newLayerGroupAfter(CanvasLayerGroup group) {
        return new CanvasLayerGroup(layerGroups.indexOf(group) + 1);
    }

    /**
     * Adds a new layer group to this canvas at the
     * specified position, shifting the group at that
     * position, if any, and all subsequent groups upwards.
     * @param index
     *            position to insert the layer group at
     * @return the created group
     */
    public CanvasLayerGroup newLayerGroupAt(int index) {
        return new CanvasLayerGroup(index);
    }

    /**
     * Adds a new layer group to this canvas before (below)
     * the specified group, shifting the specified group and
     * all subsequent groups upwards.
     * <p>
     * If the specified group is <code>null</code> or does
     * not belong to this canvas, the new group is created
     * at the end (top) of the canvas.
     * @param group
     *            group to place the new group before
     * @return the created group
     */
    public CanvasLayerGroup newLayerGroupBefore(CanvasLayerGroup group) {
        if (layerGroups.contains(group)) return new CanvasLayerGroup(layerGroups.indexOf(group)); else return new CanvasLayerGroup();
    }

    /**
     * Paints the contents of this canvas on a Graphics2D
     * object.
     * @param g
     *            graphics object
     */
    public final void render(Graphics2D g) {
        AffineTransform transform = g.getTransform();
        Shape clip = g.getClip();
        final Rectangle viewportBounds = viewport.getBounds();
        final AffineTransform viewMatrix = AffineTransform.getTranslateInstance(-viewportBounds.x, -viewportBounds.y);
        g.clipRect(0, 0, viewportBounds.width, viewportBounds.height);
        g.transform(viewMatrix);
        if (backgroundColor != null) {
            g.setColor(backgroundColor);
            g.fillRect(viewportBounds.x, viewportBounds.y, viewportBounds.width, viewportBounds.height);
        }
        for (CanvasLayerGroup layerGroup : layerGroups) for (CanvasLayer layer : layerGroup.layers) layer.render(g);
        g.setTransform(transform);
        g.setClip(clip);
    }

    /**
     * Sets new background color. If set to
     * <code>null</code>, then the background shall not be
     * cleared.
     * @param backgroundColor
     *            the background color to set
     */
    public final void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    /**
     * Sets new value of graphicsConfiguration.
     * @param graphicsConfiguration
     *            the graphicsConfiguration to set
     */
    public final void setGraphicsConfiguration(GraphicsConfiguration graphicsConfiguration) {
        this.graphicsConfiguration = graphicsConfiguration;
    }
}
