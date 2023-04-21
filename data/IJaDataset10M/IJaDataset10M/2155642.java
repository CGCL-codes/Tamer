package de.enough.polish.ui;

import de.enough.polish.android.lcdui.Graphics;
import de.enough.polish.ui.game.Sprite;
import de.enough.polish.ui.StyleSheet;

/**
 * <p>Allows to use sprites within normal forms.</p>
 *
 * <p>Copyright (c) Enough Software 2005 - 2009</p>
 * <pre>
 * history
 *        15-Feb-2005 - rob creation
 * </pre>
 * @author Robert Virkus, j2mepolish@enough.de
 */
public class SpriteItem extends CustomItem {

    private final Sprite sprite;

    private final long animationInterval;

    private final int defaultFrameIndex;

    private final boolean repeatAnimation;

    private boolean isSpriteItemFocused;

    private int currentStep;

    private int maxStep;

    private long lastAnimationTime;

    /**
	 * Creates a new sprite item.
	 * 
	 * @param label the label of this item
	 * @param sprite the sprite that should be painted
	 * @param animationInterval the interval in milliseconds for animating this item
	 * @param defaultFrameIndex the frame that is shown when the SpriteItem is not focused 
	 * @param repeatAnimation defines whether the animation should be repeated when the last frame
	 *        of the frame-sequence has been reached. 
	 */
    public SpriteItem(String label, Sprite sprite, long animationInterval, int defaultFrameIndex, boolean repeatAnimation) {
        this(label, sprite, animationInterval, defaultFrameIndex, repeatAnimation, null);
    }

    /**
	 * Creates a new sprite item.
	 * 
	 * @param label the label of this item
	 * @param sprite the sprite that should be painted
	 * @param animationInterval the interval in milliseconds for animating this item
	 * @param defaultFrameIndex the frame that is shown when the SpriteItem is not focused 
	 * @param repeatAnimation defines whether the animation should be repeated when the last frame
	 *        of the frame-sequence has been reached. 
	 * @param style the CSS style
	 */
    public SpriteItem(String label, Sprite sprite, long animationInterval, int defaultFrameIndex, boolean repeatAnimation, Style style) {
        super(label, style);
        this.sprite = sprite;
        this.animationInterval = animationInterval;
        this.defaultFrameIndex = defaultFrameIndex;
        this.repeatAnimation = repeatAnimation;
        sprite.setFrame(defaultFrameIndex);
    }

    protected int getMinContentWidth() {
        return this.sprite.getWidth();
    }

    protected int getMinContentHeight() {
        return this.sprite.getHeight();
    }

    protected int getPrefContentWidth(int height) {
        return this.sprite.getWidth();
    }

    protected int getPrefContentHeight(int width) {
        return this.sprite.getHeight();
    }

    protected void paint(Graphics g, int w, int h) {
        this.sprite.paint(g);
    }

    public boolean animate() {
        long time = System.currentTimeMillis();
        if (time - this.lastAnimationTime >= this.animationInterval) {
            this.lastAnimationTime = time;
            if (this.repeatAnimation || this.currentStep < this.maxStep) {
                this.sprite.nextFrame();
                this.currentStep++;
                return true;
            }
        }
        return false;
    }

    protected boolean traverse(int direction, int viewportWidth, int viewportHeight, int[] inoutRect) {
        if (this.isSpriteItemFocused) {
            return false;
        } else {
            this.currentStep = 0;
            if (!this.repeatAnimation) {
                this.maxStep = this.sprite.getFrameSequenceLength() - 1;
            }
            this.isSpriteItemFocused = true;
            return true;
        }
    }

    protected void traverseOut() {
        this.isSpriteItemFocused = false;
        this.sprite.setFrame(this.defaultFrameIndex);
    }
}
