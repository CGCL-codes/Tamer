package net.sf.freecol.client.gui.plaf;

import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTextAreaUI;
import net.sf.freecol.client.gui.ImageLibrary;

/**
 * Provides a tiled background image "background.FreeColTextArea" to
 * text areas.
 */
public class FreeColTextAreaUI extends BasicTextAreaUI {

    private JComponent c;

    public FreeColTextAreaUI(JComponent c) {
        this.c = c;
    }

    public static ComponentUI createUI(JComponent c) {
        return new FreeColTextAreaUI(c);
    }

    @Override
    public void paintSafely(Graphics g) {
        LAFUtilities.setProperties(g, c);
        super.paintSafely(g);
    }

    public void paintBackground(java.awt.Graphics g) {
        JComponent c = getComponent();
        if (c.isOpaque()) {
            ImageLibrary.drawTiledImage("background.FreeColTextArea", g, c, null);
        }
    }
}
