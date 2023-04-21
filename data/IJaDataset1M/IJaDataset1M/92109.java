package net.bull.javamelody.swing;

import java.awt.event.ActionEvent;
import javax.swing.Icon;
import javax.swing.JMenuItem;
import net.bull.javamelody.swing.util.MSwingUtilities;
import net.bull.javamelody.swing.util.MWaitCursor;

/**
 * MenuItem.
 *
 * @author Emeric Vernat
 */
public class MMenuItem extends JMenuItem {

    private static final long serialVersionUID = 1L;

    /**
	 * Constructeur.
	 */
    public MMenuItem() {
        this(null);
    }

    /**
	 * Constructeur.
	 *
	 * @param text
	 *           String
	 */
    public MMenuItem(final String text) {
        super(text);
    }

    /**
	 * Constructeur.
	 *
	 * @param text
	 *           String
	 * @param icon
	 *           Icon
	 */
    public MMenuItem(final String text, final Icon icon) {
        super(text, icon);
    }

    /**
	 * Méthode interne pour notifier tous les listeners qui ont enregistré leur intérêt par menuItem.addActionListener pour les évènements d'action sur cet item.
	 *
	 * Dans la surcharge de cette méthode, le curseur sablier est ici automatiquement affiché.
	 *
	 * @param event
	 *           ActionEvent
	 */
    @Override
    protected void fireActionPerformed(final ActionEvent event) {
        try {
            final MWaitCursor waitCursor = new MWaitCursor(this);
            try {
                super.fireActionPerformed(event);
            } finally {
                waitCursor.restore();
            }
        } catch (final Throwable t) {
            MSwingUtilities.showException(t);
        }
    }
}
