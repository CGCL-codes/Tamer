package com.googlecode.saga;

/**
 * 
 */
public class ParallaxLayer {

    private final float depth;

    private int viewX;

    private int viewY;

    private final int screenWidth;

    private final int screenHeight;

    public ParallaxLayer(float depth, int screenWidth, int screenHeight) {
        this.depth = depth;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    /**
     * @return
     */
    public final float getDepth() {
        return this.depth;
    }

    /**
     * This method is called by the ParallaxManager and must not be used by
     * other classes.
     * 
     * @param x
     * @param y
     */
    final void setView(int x, int y) {
        this.viewX = (int) (x * this.depth);
        this.viewY = (int) (y * this.depth);
    }

    /**
     * This method is called by the ParallaxManager and must not be used by
     * other classes.
     * 
     * @param x
     * @param y
     */
    final void moveView(int x, int y) {
        this.viewX += (int) (x * this.depth);
        this.viewY += (int) (y * this.depth);
    }

    public final int getViewX() {
        return this.viewX;
    }

    public final int getViewY() {
        return this.viewY;
    }

    /**
     * @param x
     * @param y
     * @return
     */
    public final Vector2 newVector(int x, int y) {
        return new Vector2(x, y, this.screenWidth, this.screenHeight, this);
    }
}
