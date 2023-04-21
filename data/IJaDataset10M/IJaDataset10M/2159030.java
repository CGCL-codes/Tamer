package laf;

import graphic.ClanImageProvider;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 * @author Imi
 */
public class ClanButtonUI extends BasicButtonUI {

    Image background = ClanImageProvider.get("laf/back");

    public static ComponentUI createUI(JComponent c) {
        return new ClanButtonUI();
    }

    /**
	 * @see javax.swing.plaf.ComponentUI#paint(Graphics, JComponent)
	 */
    public void paint(Graphics g, JComponent c) {
        Insets s = c.getInsets();
        if (c.isOpaque()) g.drawImage(background, s.left, s.top, null);
        super.paint(g, c);
    }
}
