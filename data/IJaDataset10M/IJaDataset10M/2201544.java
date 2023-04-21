package a03.swing.plaf.style;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.Icon;
import javax.swing.plaf.UIResource;
import a03.swing.plaf.A03GraphicsUtilities;
import a03.swing.plugin.A03PluginManager;
import a03.swing.plugin.A03FadeTrackerPlugin;

public class A03StyledMaximizeIcon implements Icon, UIResource, A03StyleConstants {

    private A03TitlePaneStyle style;

    public A03StyledMaximizeIcon(A03TitlePaneStyle style) {
        this.style = style;
    }

    public int getIconHeight() {
        return 16;
    }

    public int getIconWidth() {
        return 16;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        Image image = A03GraphicsUtilities.createImage(c, getIconWidth(), getIconHeight());
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        int state = c.isEnabled() ? ENABLED : 0;
        graphics.setColor(style.getToggleForegroundColor(state));
        A03StyledGraphicsUtilities.drawWindow(graphics, 3, 3, 9, 9, true);
        if ((state & ENABLED) != 0) {
            float fadeLevel = (float) A03PluginManager.getInstance().getPlugin(A03FadeTrackerPlugin.class).getFadeLevel(c);
            if (fadeLevel > 0) {
                graphics.setColor(style.getToggleForegroundColor(ENABLED | ARMED));
                graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, fadeLevel));
                A03StyledGraphicsUtilities.drawWindow(graphics, 3, 3, 9, 9, true);
            }
        }
        graphics.dispose();
        g.drawImage(image, x, y, c);
        graphics.dispose();
    }
}
