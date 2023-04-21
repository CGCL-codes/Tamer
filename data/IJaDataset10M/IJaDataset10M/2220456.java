package org.pockit.view.j4me.components;

import java.io.IOException;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import org.j4me.ui.Theme;
import org.pockit.controller.SimpleCommandListener;

public class GraphicMenuButton extends MenuButton {

    private Image icon;

    private String metaInformation = "";

    public GraphicMenuButton(SimpleCommandListener controller, Command fireCommand, String label, String image) {
        super(controller, fireCommand, label);
        try {
            icon = Image.createImage(image);
        } catch (IOException unhandled) {
        }
    }

    public int[] getPreferredComponentSize(Theme theme, int viewportWidth, int viewportHeight) {
        int[] a = new int[2];
        a[0] = 100;
        a[1] = 50;
        return a;
    }

    protected void paintComponent(Graphics g, Theme theme, int width, int height, boolean selected) {
        if (selected) {
            g.setColor(theme.getHighlightColor());
            g.fillRect(0, 0, width, height);
        }
        if (icon != null) {
            g.drawImage(icon, 1, 1, Graphics.TOP | Graphics.LEFT);
        }
        g.setColor(theme.getFontColor());
        g.drawString(label, icon.getWidth() + 3, 6, Graphics.TOP | Graphics.LEFT);
        g.setColor(theme.getFontColor() | 0x707070);
        g.drawString(metaInformation, icon.getWidth() + 3, 28, Graphics.TOP | Graphics.LEFT);
    }

    public void setMetaInformation(String metaInformation) {
        this.metaInformation = metaInformation;
    }
}
