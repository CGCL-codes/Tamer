package proguard.gui.splash;

import java.awt.*;

/**
 * This Sprite sets the font for another given sprite.
 *
 * @author Eric Lafortune
 */
public class FontSprite implements Sprite {

    private final VariableFont font;

    private final Sprite sprite;

    /**
     * Creates a new FontSprite.
     * @param font   the variable Font of the given sprite.
     * @param sprite the sprite that will be provided of a font and painted.
     */
    public FontSprite(VariableFont font, Sprite sprite) {
        this.font = font;
        this.sprite = sprite;
    }

    public void paint(Graphics graphics, long time) {
        Font oldFont = graphics.getFont();
        graphics.setFont(font.getFont(time));
        sprite.paint(graphics, time);
        graphics.setFont(oldFont);
    }
}
