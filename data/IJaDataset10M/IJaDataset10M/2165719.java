package net.laubenberger.bogatyr.view.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import javax.swing.JWindow;
import javax.swing.UIManager;
import net.laubenberger.bogatyr.helper.HelperLog;
import net.laubenberger.bogatyr.helper.HelperString;
import net.laubenberger.bogatyr.misc.Displayable;
import net.laubenberger.bogatyr.misc.Fadeable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is an extended JWindow.
 *
 * @author Stefan Laubenberger
 * @version 0.9.2 (20100611)
 * @since 0.9.2
 */
public class Window extends JWindow implements Fadeable, Displayable {

    private static final long serialVersionUID = 7476360387134225315L;

    private static final Logger log = LoggerFactory.getLogger(Window.class);

    private boolean isFading;

    private Color colorFader = new Color(0, 0, 0, 100);

    {
        setLocationRelativeTo(getOwner());
    }

    public Window() {
        super();
        if (log.isTraceEnabled()) log.trace(HelperLog.constructor());
    }

    public Window(final Frame owner) {
        super(owner);
        if (log.isTraceEnabled()) log.trace(HelperLog.constructor(owner));
    }

    public Window(final GraphicsConfiguration gc) {
        super(gc);
        if (log.isTraceEnabled()) log.trace(HelperLog.constructor(gc));
    }

    public Window(final Window owner, final GraphicsConfiguration gc) {
        super(owner, gc);
        if (log.isTraceEnabled()) log.trace(HelperLog.constructor(owner, gc));
    }

    public Window(final Window owner) {
        super(owner);
        if (log.isTraceEnabled()) log.trace(HelperLog.constructor(owner));
    }

    @Override
    public void setEnabled(final boolean isEnabled) {
        super.setEnabled(isEnabled);
        final Component[] components = getComponents();
        for (final Component component : components) {
            component.setEnabled(isEnabled);
        }
    }

    @Override
    public void createAndShowGUI() {
        if (log.isDebugEnabled()) log.debug(HelperLog.methodStart());
        setVisible(true);
        if (log.isDebugEnabled()) log.debug(HelperLog.methodExit());
    }

    @Override
    public void clearAndHide() {
        if (log.isDebugEnabled()) log.debug(HelperLog.methodStart());
        dispose();
        if (log.isDebugEnabled()) log.debug(HelperLog.methodExit());
    }

    @Override
    public void setFading(final boolean isFading) {
        if (log.isDebugEnabled()) log.debug(HelperLog.methodStart(isFading));
        this.isFading = isFading;
        if (HelperString.contains(UIManager.getLookAndFeel().toString(), "swing.plaf") || HelperString.contains(UIManager.getLookAndFeel().toString(), "apple.laf")) {
            final Container containerGlass = (Container) getGlassPane();
            containerGlass.removeAll();
            containerGlass.setVisible(isFading);
            if (isFading) {
                containerGlass.setLayout(new BorderLayout());
                containerGlass.add(new Panel(colorFader), BorderLayout.CENTER);
            }
            validate();
            repaint();
        }
        if (log.isDebugEnabled()) log.debug(HelperLog.methodExit());
    }

    @Override
    public void setFaderColor(final Color colorFader) {
        if (log.isDebugEnabled()) log.debug(HelperLog.methodStart(colorFader));
        this.colorFader = colorFader;
        if (log.isDebugEnabled()) log.debug(HelperLog.methodExit());
    }

    @Override
    public boolean isFading() {
        if (log.isDebugEnabled()) log.debug(HelperLog.methodStart());
        if (log.isDebugEnabled()) log.debug(HelperLog.methodExit(isFading));
        return isFading;
    }
}
