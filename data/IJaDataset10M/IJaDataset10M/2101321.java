package net.laubenberger.bogatyr.view.swing;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JDialog;
import javax.swing.JPopupMenu;
import javax.swing.RootPaneContainer;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import net.laubenberger.bogatyr.helper.HelperEnvironment;
import net.laubenberger.bogatyr.helper.HelperLog;
import net.laubenberger.bogatyr.model.misc.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is an extended {@link java.awt.TrayIcon} with {@link JPopupMenu} support.
 *
 * @author Stefan Laubenberger
 * @version 0.9.4 (20101227)
 * @since 0.9.2
 */
public class TrayIcon extends java.awt.TrayIcon {

    private static final Logger log = LoggerFactory.getLogger(TrayIcon.class);

    static final Platform PLATFORM = HelperEnvironment.getPlatform();

    private static final int MAC_MENUBAR_OFFSET = 21;

    int counterMouseClicks = 1;

    private JDialog dialog;

    private JPopupMenu popup;

    private final PopupMenuListener popupListener = new PopupMenuListener() {

        @Override
        public void popupMenuWillBecomeVisible(final PopupMenuEvent e) {
        }

        @Override
        public void popupMenuWillBecomeInvisible(final PopupMenuEvent e) {
        }

        @Override
        public void popupMenuCanceled(final PopupMenuEvent e) {
            hideJPopupMenu();
        }
    };

    {
        init();
    }

    public TrayIcon(final Image image, final String tooltip, final PopupMenu popup) {
        super(image, tooltip, popup);
        if (log.isTraceEnabled()) log.trace(HelperLog.constructor(image, tooltip, popup));
    }

    public TrayIcon(final Image image, final String tooltip) {
        super(image, tooltip);
        if (log.isTraceEnabled()) log.trace(HelperLog.constructor(image, tooltip));
    }

    public TrayIcon(final Image image) {
        super(image);
        if (log.isTraceEnabled()) log.trace(HelperLog.constructor(image));
    }

    public TrayIcon(final Image image, final String tooltip, final JPopupMenu popup) {
        this(image, tooltip);
        if (log.isTraceEnabled()) log.trace(HelperLog.constructor(image, tooltip, popup));
        setJPopupMenu(popup);
    }

    /**
	 * Returns the {@link JPopupMenu}.
	 *
	 * @return used {@link JPopupMenu}
	 * @see JPopupMenu
	 * @since 0.9.2
	 */
    public JPopupMenu getJPopupMenu() {
        return popup;
    }

    /**
	 * Sets the {@link JPopupMenu}.
	 *
	 * @param popup to be used
	 * @see JPopupMenu
	 * @since 0.9.2
	 */
    public void setJPopupMenu(final JPopupMenu popup) {
        if (null != this.popup) {
            this.popup.removePopupMenuListener(popupListener);
        }
        this.popup = popup;
        if (null != this.popup) {
            this.popup.setInvoker(this.popup);
            this.popup.addPopupMenuListener(popupListener);
        }
    }

    private void init() {
        setImageAutoSize(true);
        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(final MouseEvent e) {
                if (Platform.MAC_OSX == PLATFORM) {
                    if (0 == counterMouseClicks % 2) {
                        hideJPopupMenu();
                    } else {
                        showJPopupMenu(e);
                    }
                    counterMouseClicks++;
                }
            }

            @Override
            public void mouseReleased(final MouseEvent e) {
                if (Platform.MAC_OSX != PLATFORM) {
                    if (0 == counterMouseClicks % 2) {
                        hideJPopupMenu();
                    } else {
                        showJPopupMenu(e);
                    }
                    counterMouseClicks++;
                }
            }
        });
    }

    void showJPopupMenu(final MouseEvent e) {
        if (null != popup) {
            if (null == dialog) {
                dialog = new JDialog((Frame) null);
                dialog.setUndecorated(true);
                dialog.setAlwaysOnTop(true);
                final Dimension size = popup.getPreferredSize();
                final Point centerPoint = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
                if (e.getY() > centerPoint.getY()) {
                    dialog.setLocation(Platform.MAC_OSX == PLATFORM ? e.getXOnScreen() : e.getX(), Platform.MAC_OSX == PLATFORM ? MAC_MENUBAR_OFFSET : e.getY() - size.height);
                } else {
                    dialog.setLocation(Platform.MAC_OSX == PLATFORM ? e.getXOnScreen() : e.getX(), Platform.MAC_OSX == PLATFORM ? MAC_MENUBAR_OFFSET : e.getY());
                }
                dialog.setVisible(true);
                popup.show(((RootPaneContainer) dialog).getContentPane(), 0, 0);
                dialog.toFront();
            }
        }
    }

    void hideJPopupMenu() {
        if (null != dialog) {
            dialog.dispose();
            dialog = null;
        }
    }
}
