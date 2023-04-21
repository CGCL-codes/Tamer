package com.jgoodies.looks.windows;

import java.awt.Component;
import java.awt.Container;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.border.Border;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalToolBarUI;
import com.jgoodies.looks.BorderStyle;
import com.jgoodies.looks.HeaderStyle;
import com.jgoodies.looks.Options;

/**
 * Corrects superclass behavior for rollover borders
 * and adds behavior for handling different types of borders.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.6 $
 */
public final class WindowsToolBarUI extends MetalToolBarUI {

    private PropertyChangeListener listener;

    public static ComponentUI createUI(JComponent b) {
        return new WindowsToolBarUI();
    }

    protected void installDefaults() {
        super.installDefaults();
        installSpecialBorder();
    }

    protected void installListeners() {
        super.installListeners();
        listener = createBorderStyleListener();
        toolBar.addPropertyChangeListener(listener);
    }

    protected void uninstallListeners() {
        toolBar.removePropertyChangeListener(listener);
        super.uninstallListeners();
    }

    private PropertyChangeListener createBorderStyleListener() {
        return new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent e) {
                String prop = e.getPropertyName();
                if (prop.equals(Options.HEADER_STYLE_KEY) || prop.equals(WindowsLookAndFeel.BORDER_STYLE_KEY)) {
                    WindowsToolBarUI.this.installSpecialBorder();
                }
            }
        };
    }

    /**
     * Installs a special border, if either a look-dependent
     * <code>BorderStyle</code> or a look-independent
     * <code>HeaderStyle</code> has been specified.
     * A look specific BorderStyle shadows a HeaderStyle.<p>
     *
     * Specifying a HeaderStyle is recommend.
     */
    private void installSpecialBorder() {
        String suffix;
        BorderStyle borderStyle = BorderStyle.from(toolBar, WindowsLookAndFeel.BORDER_STYLE_KEY);
        if (borderStyle == BorderStyle.EMPTY) suffix = "emptyBorder"; else if (borderStyle == BorderStyle.SEPARATOR) suffix = "separatorBorder"; else if (borderStyle == BorderStyle.ETCHED) suffix = "etchedBorder"; else if (HeaderStyle.from(toolBar) == HeaderStyle.BOTH) suffix = "headerBorder"; else return;
        LookAndFeel.installBorder(toolBar, "ToolBar." + suffix);
    }

    /**
     * Unlike the superclass MetalToolBarUI,
     * this class uses the docking listener from the BasicToolBarUI.
     */
    protected MouseInputListener createDockingListener() {
        return new DockingListener(toolBar);
    }

    protected Border createRolloverBorder() {
        return WindowsBorders.getRolloverButtonBorder();
    }

    protected void setBorderToRollover(Component c) {
        if (c instanceof AbstractButton) {
            super.setBorderToRollover(c);
        } else if (c instanceof Container) {
            Container cont = (Container) c;
            for (int i = 0; i < cont.getComponentCount(); i++) super.setBorderToRollover(cont.getComponent(i));
        }
    }
}
