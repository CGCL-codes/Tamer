package org.simbrain.world.visionworld;

/**
 * Receptive field.
 */
public final class ReceptiveField {

    /** X offset. */
    private final int x;

    /** Y offset. */
    private final int y;

    /** Width. */
    private final int width;

    /** Height. */
    private final int height;

    public ReceptiveField(final int x, final int y, final int width, final int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Return the x offset for this receptive field.
     *
     * @return the x offset for this receptive field
     */
    public int getX() {
        return x;
    }

    /**
     * Return the y offset for this receptive field.
     *
     * @return the y offset for this receptive field
     */
    public int getY() {
        return y;
    }

    /**
     * Return the width of this receptive field.
     *
     * @return the width of this receptive field
     */
    public int getWidth() {
        return width;
    }

    /**
     * Return the height of this receptive field.
     *
     * @return the height of this receptive field
     */
    public int getHeight() {
        return height;
    }

    /**
     * Return the center x coordinate for this receptive field.
     *
     * @return the center x coordinate for this receptive field
     */
    public double getCenterX() {
        return x + (width / 2.0d);
    }

    /**
     * Return the center y coordinate for this receptive field.
     *
     * @return the center y coordinate for this receptive field
     */
    public double getCenterY() {
        return y + (height / 2.0d);
    }
}
