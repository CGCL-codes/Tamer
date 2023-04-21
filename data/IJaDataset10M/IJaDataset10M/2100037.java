package laf;

import graphic.ClanImageProvider;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTextFieldUI;

/**
 * @author Imi
 */
public class ClanTextFieldUI extends BasicTextFieldUI {

    static Image background = ClanImageProvider.get("laf/back");

    JComponent component;

    public ClanTextFieldUI(JComponent c) {
        component = c;
    }

    public static ComponentUI createUI(JComponent c) {
        return new ClanTextFieldUI(c);
    }

    protected void paintBackground(Graphics g) {
        Insets s = component.getInsets();
        g.drawImage(background, s.left, s.top, null);
    }
}
