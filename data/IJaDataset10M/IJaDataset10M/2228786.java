package arcane.object.gui.specific;

import java.awt.Color;
import java.awt.Graphics2D;
import org.lwjgl.opengl.GL11;
import arcane.object.game.sprite.MenuSprite;

public class PulsingPanel extends MenuSprite {

    Color c1, c2;

    int pulseX, pulseY;

    int pulseWidth;

    double inc;

    public PulsingPanel() {
        super(null, 0, 0);
        pulseX = pulseY = 0;
        inc = .2;
        pulseWidth = 300;
        c1 = new Color(0f, 0f, 0f, 0f);
        c2 = new Color(0f, 0f, .5f, 1f);
    }

    public void setLocation(int x, int y) {
        super.setLocation(x, y);
        pulseX = x;
        pulseY = y;
    }

    public void update(long time) {
        super.update(time);
        pulseX += Math.floor((inc * time));
        if (pulseX > width) pulseX = -pulseWidth;
    }

    public void render(Graphics2D g, int x, int y) {
        super.render(g, x, y);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBegin(GL11.GL_QUADS);
        setOpenGLColor(c1);
        GL11.glVertex2f(pulseX, y);
        GL11.glVertex2f(pulseX, y + height);
        setOpenGLColor(c2);
        GL11.glVertex2f(pulseX + pulseWidth, y + height);
        GL11.glVertex2f(pulseX + pulseWidth, y);
        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }
}
