package net.sf.japi.progs.jeduca.swing.recent;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import net.sf.japi.progs.jeduca.swing.io.CanLoad;
import net.sf.japi.swing.ActionFactory;

/** Class for a menu showing the recently used URLs.
 * @todo think about serialization of recent and program
 */
public class RecentURLsMenu extends JMenu implements RecentURLsListener {

    /** Serial Version. */
    @SuppressWarnings({ "AnalyzingVariableNaming" })
    private static final long serialVersionUID = 1L;

    /** The object providing the RecentURLs information. */
    private transient RecentURLs recent;

    /** The program opening documents. */
    private transient CanLoad program;

    /** Create a RecentURLsMenu.
     * @param recent object with RecentURLs information.
     * @param program program that opens documents
     */
    public RecentURLsMenu(final RecentURLs recent, final CanLoad program) {
        super(ActionFactory.getFactory("net.sf.japi.progs.jeduca.swing.recent").createAction(true, "recent"));
        this.program = program;
        this.recent = recent;
        recent.addRecentURLsListener(this);
        updateMenu();
    }

    /** Create a RecentURLsMenu.
     * The list of recent URLs is stored in the preferences for the class of <var>c</var>.
     * @param c Class to obtain RecentURLs information for
     * @param program program that opens documents
     * @see PrefsRecentURLs
     */
    public RecentURLsMenu(final Class<?> c, final CanLoad program) {
        this(PrefsRecentURLs.getInstance(c), program);
    }

    /** Create a RecentURLsMenu.
     * The list of recent URLs is stored in the preferences for the class of <var>program</var>.
     * @param program program that opens documents
     * @see PrefsRecentURLs
     */
    public RecentURLsMenu(final CanLoad program) {
        this(PrefsRecentURLs.getInstance(program.getClass()), program);
    }

    /** Update the menu. */
    private void updateMenu() {
        removeAll();
        for (String url : recent.getRecentlyURLs()) {
            add(new JMenuItem(new URLAction(url)));
        }
    }

    /** {@inheritDoc} */
    public void recentURLsChanged(final RecentURLsEvent e) {
        assert e.getSource() == recent;
        updateMenu();
    }

    /** Class for URL Actions.
     */
    private class URLAction extends AbstractAction {

        /** Serial Version. */
        @SuppressWarnings({ "AnalyzingVariableNaming" })
        private static final long serialVersionUID = 1L;

        /** The URL to be opened. */
        private String url;

        /** Create a URLAction. */
        URLAction(final String url) {
            putValue(NAME, url);
            this.url = url;
        }

        /** {@inheritDoc} */
        public void actionPerformed(final ActionEvent e) {
            program.load(url);
        }
    }
}
