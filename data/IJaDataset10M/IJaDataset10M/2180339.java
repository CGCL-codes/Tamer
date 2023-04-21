package a03.swing.plaf;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicToolBarUI;
import a03.swing.plugin.A03PluginManager;

public class A03ToolBarUI extends BasicToolBarUI {

    public static ComponentUI createUI(JComponent c) {
        return new A03ToolBarUI();
    }

    private A03ToolBarDelegate delegate;

    @Override
    public void installUI(JComponent c) {
        this.delegate = (A03ToolBarDelegate) A03SwingUtilities.getDelegate(c, UIManager.get("ToolBar.delegate"));
        super.installUI(c);
        A03PluginManager.getInstance().registerComponent(c);
    }

    @Override
    public void uninstallUI(JComponent c) {
        A03PluginManager.getInstance().unregisterComponent(c);
        super.uninstallUI(c);
    }

    @Override
    public void update(Graphics g, JComponent c) {
        Graphics2D graphics = (Graphics2D) g.create();
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        if (c.isOpaque()) {
            graphics.setColor(c.getBackground());
            graphics.fillRect(0, 0, c.getWidth(), c.getHeight());
        }
        paint(graphics, c);
        graphics.dispose();
    }

    @Override
    protected void setBorderToNonRollover(Component c) {
        if (c instanceof AbstractButton) {
            AbstractButton b = (AbstractButton) c;
            b.setMargin(delegate.getMargin());
            if (b instanceof JToggleButton) {
                b.setBorder(UIManager.getBorder("ToolBar.nonRolloverToggleBorder"));
            } else {
                b.setBorder(UIManager.getBorder("ToolBar.nonRolloverBorder"));
            }
        }
    }
}
