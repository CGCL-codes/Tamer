package net.sf.appomatox.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JToolBar;
import javax.swing.border.Border;
import org.jdesktop.swingx.border.DropShadowBorder;
import org.jdesktop.swingx.painter.CompoundPainter;
import org.jdesktop.swingx.painter.GlossPainter;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.painter.Painter;
import org.jdesktop.swingx.util.OS;

/**
 * ToolBar mit einem Windows-Vista-Hintergrund. Unter anderen Betriebssystemen
 * wie Windows Vista wird eine Standard-JToolbar angezeigt.
 * 
 * @author Frank Schnitzer
 */
public class CoolBar extends JToolBar {

    /**
	 * Die Gr��e des Schattens unter der Toolbar.
	 */
    private static final int s_iShadowSize = 5;

    /**
	 * Gibt an, ob die Vista-spezifischen Styles benutzt werden sollen.
	 */
    private final boolean m_bUseVistaStyle = OS.isWindowsVista();

    private transient Painter<CoolBar> m_Painter;

    /**
     * Erzeugt eine neue Instanz
     */
    public CoolBar() {
        if (m_bUseVistaStyle) {
            Border outerBorder = new DropShadowBorder(Color.BLACK, s_iShadowSize, .5f, 12, false, true, true, true) {

                @Override
                public void paintBorder(Component c, Graphics graphics, int x, int y, int width, int height) {
                    super.paintBorder(c, graphics, x - 5, y, width + 10, height);
                }
            };
            setBorder(outerBorder);
            GlossPainter<CoolBar> gloss = new GlossPainter<CoolBar>(new Color(0.9f, 0.9f, 0.9f, 0.2f), GlossPainter.GlossPosition.TOP);
            MattePainter<CoolBar> matte = new MattePainter<CoolBar>(new Color(71, 71, 71));
            m_Painter = new CompoundPainter<CoolBar>(matte, gloss);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (m_Painter != null) {
            Graphics2D g2 = (Graphics2D) g;
            m_Painter.paint(g2, this, getWidth(), getHeight() - s_iShadowSize);
        }
    }

    /**
     * Liefert einen Button, der der Toolbar hinzugef�gt werden kann. Der 
     * Button ist dem Hintergrund angepasst.
     * @param act Die Action, die dem Button zugewiesen wird.
     * @return Der Button
     */
    protected JButton getButton(Action act) {
        JButton btn = new JButton(act);
        btn.setFocusable(false);
        btn.setOpaque(false);
        if (m_bUseVistaStyle) {
            btn.setForeground(Color.WHITE);
        }
        btn.setMinimumSize(new Dimension(32, 32));
        return btn;
    }
}
