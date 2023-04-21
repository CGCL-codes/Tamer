package proguard.gui.splash;

import java.awt.*;

/**
 * This Sprite adds a drop shadow to another Sprite.
 *
 * @author Eric Lafortune
 */
public class ShadowedSprite implements Sprite {

    private VariableInt xOffset;

    private VariableInt yOffset;

    private VariableDouble alpha;

    private VariableInt blur;

    private Sprite sprite;

    private float cachedAlpha = -1.0f;

    private Color cachedColor;

    /**
     * Creates a new ShadowedSprite.
     * @param xOffset the variable x-offset of the shadow, relative to the sprite itself.
     * @param yOffset the variable y-offset of the shadow, relative to the sprite itself.
     * @param alpha   the variable darkness of the shadow (between 0 and 1).
     * @param blur    the variable blur of the shadow (0 for sharp shadows, 1 or
     *                more for increasingly blurry shadows).
     * @param sprite  the Sprite to be painted with its shadow.
     */
    public ShadowedSprite(VariableInt xOffset, VariableInt yOffset, VariableDouble alpha, VariableInt blur, Sprite sprite) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.alpha = alpha;
        this.blur = blur;
        this.sprite = sprite;
    }

    public void paint(Graphics graphics, long time) {
        double l = alpha.getDouble(time);
        int b = blur.getInt(time) + 1;
        OverrideGraphics2D g = new OverrideGraphics2D((Graphics2D) graphics);
        float a = 1.0f - (float) Math.pow(1.0 - l, 1.0 / (b * b));
        if (a != cachedAlpha) {
            cachedAlpha = a;
            cachedColor = new Color(0f, 0f, 0f, a);
        }
        g.setOverrideColor(cachedColor);
        int xo = xOffset.getInt(time) - b / 2;
        int yo = yOffset.getInt(time) - b / 2;
        for (int x = 0; x < b; x++) {
            for (int y = 0; y < b; y++) {
                int xt = xo + x;
                int yt = yo + y;
                g.translate(xt, yt);
                sprite.paint(g, time);
                g.translate(-xt, -yt);
            }
        }
        sprite.paint(graphics, time);
    }
}
