package com.rapidminer.gui.look.icons;

import java.awt.Component;
import java.awt.Graphics;
import java.io.Serializable;
import javax.swing.Icon;
import javax.swing.plaf.UIResource;
import com.rapidminer.gui.look.painters.CashedPainter;

/**
 * The radio button icon.
 *
 * @author Ingo Mierswa
 */
public class RadioButtonIcon implements Icon, UIResource, Serializable {

    private static final long serialVersionUID = -2576744883403903818L;

    public void paintIcon(Component c, Graphics g, int x, int y) {
        g.translate(x, y);
        CashedPainter.drawRadioButton(c, g);
        g.translate(-x, -y);
    }

    public int getIconWidth() {
        return 16;
    }

    public int getIconHeight() {
        return 16;
    }
}
