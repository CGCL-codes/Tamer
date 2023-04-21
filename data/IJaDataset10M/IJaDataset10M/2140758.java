package javax.swing.plaf;

import java.awt.event.MouseEvent;
import javax.swing.JPopupMenu;
import javax.swing.Popup;
import javax.swing.PopupFactory;

/**
 * An abstract base class for delegates that implement the pluggable
 * look and feel for a <code>JPopupMenu</code>.
 *
 * @see javax.swing.JPopupMenu
 *
 * @author Andrew Selkirk (aselkirk@sympatico.ca)
 * @author Sascha Brawer (brawer@dandelis.ch)
 */
public abstract class PopupMenuUI extends ComponentUI {

    /**
   * Constructs a new <code>PopupMenuUI</code>.
   */
    public PopupMenuUI() {
    }

    /**
   * Tests whether or not a mouse event triggers a popup menu.
   *
   * <p>The default implementation calls
   * <code>event.isPopupTrigger()</code>, which checks for the gesture
   * that is common for the platform on which the application runs. If
   * a look and feel wants to employ non-standard conventions for
   * triggering a popup menu, it can override this method.
   *
   * @param event the event to check.
   *
   * @return <code>true</code> if the event triggers a popup menu;
   *         <code>false</code> otherwise.
   *
   * @since 1.3
   */
    public boolean isPopupTrigger(MouseEvent event) {
        return event.isPopupTrigger();
    }

    /**
   * Creates a <code>Popup</code> for displaying the popup menu.  The
   * default implementation uses the {@link javax.swing.PopupFactory}
   * for retrieving a suitable <code>Popup</code>, but subclasses
   * might want to override this method if a LookAndFeel needs special
   * Popups.
   *
   * @param popup the <code>JPopupMenu</code> for whose display
   *        a <code>Popup</code> is needed.
   *
   * @param x the horizontal position where the popup will be
   *        displayed.
   *
   * @param y the vertical position where the popup will be
   *        displayed.
   *
   * @return a <code>Popup</code> for showing and hiding
   *         the menu.
   *
   * @since 1.4
   */
    public Popup getPopup(JPopupMenu popup, int x, int y) {
        return PopupFactory.getSharedInstance().getPopup(popup.getInvoker(), popup, x, y);
    }
}
