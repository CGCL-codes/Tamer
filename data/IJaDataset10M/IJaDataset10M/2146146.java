package com.aem.sticky.button;

import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Rectangle;

/**
 * 
 * 
 * @author abacs
 *
 */
public class CentralisedTextButton extends TextButton {

    /**
	 * @param centerX
	 * @param centerY
	 * @param text
	 * @param click
	 */
    public CentralisedTextButton(float centerX, float centerY, String text, Sound click) {
        super(centerX, centerY, text, click);
    }

    @Override
    public void render(GameContainer container, Graphics graphics) {
        if (this.shape.getWidth() == 0) {
            Font font = graphics.getFont();
            int width = font.getWidth(text) + this.padding * 2;
            int height = font.getHeight(text) + this.padding * 2;
            this.shape = new Rectangle(this.x - width / 2, this.y - height / 2, width, height);
            this.setShape(this.shape);
        }
        super.render(container, graphics);
    }
}
