package corina.gui.menus;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import corina.core.App;
import corina.gui.CanOpener;
import corina.ui.Alert;
import corina.ui.Builder;

/**
    A menu which shows recently-opened files.

    <p>(It's actually implemented as a list of recently-opened files,
    and a factory method to generate <code>JMenu</code>s.  But this will
    change in the near future.)</p>

    <p>To use, simply call <code>OpenRecent.generateMenu()</code>, and
    use that as you would any other <code>JMenu</code>.  It will
    automatically be updated whenever the list changes due to some new
    file being opened.  (When it's no longer strongly
    reachable, it'll be garbage collected.)</p>
    
    <h2>Left to do:</h2>
    <ul>
        <li>rename to OpenRecentMenu
        <li>extend JMenu; the c'tor should take care of notification stuff (add/removeNotify(), if necessary)
        <li>i don't need to use refs if i use add/remove notify, right?
        <li>doesn't use special 1.3 font hack any more -- do i care?
        <li>refactor -- separate model and view?
        <li>catch errors gracefully - "this file may have been moved...", etc.
        <li>if document is already open, just toFront() (needs other work first)
    </ul>

   @author Ken Harris &lt;kbh7 <i style="color: gray">at</i> cornell <i style="color: gray">dot</i> edu&gt;
    @version $Id: OpenRecent.java,v 1.3 2005/01/24 03:09:30 aaron Exp $
*/
public class OpenRecent {

    private static final int NUMBER_TO_REMEMBER = 10;

    private static List recent;

    private static List menus = new ArrayList();

    static {
        loadList();
    }

    /** Indicate to the recent-file list that a file was just opened.
	This also updates every recent-file menu automatically.
	@param filename the (full) name of the file that was opened
     */
    public static void fileOpened(String filename) {
        if (!recent.isEmpty() && ((String) recent.get(0)).equals(filename)) return;
        recent.remove(filename);
        if (recent.size() == NUMBER_TO_REMEMBER) recent.remove(NUMBER_TO_REMEMBER - 1);
        recent.add(0, filename);
        updateAllMenus();
        saveList();
    }

    /** Generate a new recent-file menu.  This menu will contain the
	names of (up to) the last 4 files opened.  As long as the menu
	returned by this method is referenced, it will automatically
	be kept updated. */
    public static JMenu makeOpenRecentMenu() {
        JMenu menu = Builder.makeMenu("open_recent");
        updateMenu(menu);
        menus.add(new WeakReference(menu));
        return menu;
    }

    private static void updateAllMenus() {
        for (int i = 0; i < menus.size(); i++) {
            JMenu m = (JMenu) ((Reference) menus.get(i)).get();
            if (m == null) {
                menus.remove(i);
                continue;
            }
            updateMenu(m);
        }
    }

    private static void updateMenu(JMenu menu) {
        menu.removeAll();
        for (int i = 0; i < recent.size(); i++) {
            String fn = (String) recent.get(i);
            JMenuItem r = new JMenuItem(new File(fn).getName());
            final int glue = i;
            r.addActionListener(new AbstractAction() {

                public void actionPerformed(ActionEvent e) {
                    try {
                        CanOpener.open((String) recent.get(glue));
                    } catch (FileNotFoundException fnfe) {
                        Alert.error("File Isn't There", "The file called '" + recent.get(glue) + "'\n" + "isn't there any more.  If it was moved, " + "you'll have to open it with File -> Open...");
                        recent.remove(glue);
                        updateAllMenus();
                        return;
                    } catch (IOException ioe) {
                        return;
                    }
                }
            });
            menu.add(r);
        }
        JMenuItem clear = Builder.makeMenuItem("clear_menu");
        if (recent.isEmpty()) {
            clear.setEnabled(false);
            menu.add(clear);
        } else {
            clear.addActionListener(new AbstractAction() {

                public void actionPerformed(ActionEvent e) {
                    recent = new ArrayList();
                    updateAllMenus();
                    saveList();
                }
            });
            menu.addSeparator();
            menu.add(clear);
        }
    }

    private static void loadList() {
        recent = new ArrayList();
        StringTokenizer tok = new StringTokenizer(App.prefs.getPref("corina.recent.files", ""), System.getProperty("path.separator"));
        while (tok.hasMoreTokens()) {
            String next = tok.nextToken();
            recent.add(next);
        }
    }

    private static synchronized void saveList() {
        StringBuffer buf = new StringBuffer();
        char sep = File.pathSeparatorChar;
        for (int i = 0; i < recent.size(); i++) {
            buf.append(recent.get(i).toString());
            if (i < recent.size() - 1) buf.append(sep);
        }
        App.prefs.setPref("corina.recent.files", buf.toString());
    }
}
