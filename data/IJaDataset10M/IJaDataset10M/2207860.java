package ntorrent.core.view.component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import ntorrent.NtorrentApplication;
import ntorrent.Session;
import ntorrent.locale.ResourcePool;

/**
 * @author Kim Eik
 *
 */
public class ConnectionTab extends JTabbedPane implements MouseListener, ActionListener {

    private static final long serialVersionUID = 1L;

    private static final JPopupMenu popup = new JPopupMenu();

    public static final String[] actions = { "tab.new", "tab.close" };

    public ConnectionTab(int placement) {
        super(placement);
        addMouseListener(this);
        for (String s : actions) {
            JMenuItem item = new JMenuItem(ResourcePool.getString(s, this));
            item.setActionCommand(s);
            item.addActionListener(this);
            popup.add(item);
        }
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        if (e.isPopupTrigger()) popup.show(this, e.getX(), e.getY());
    }

    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger()) popup.show(this, e.getX(), e.getY());
    }

    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        if (action.equals(actions[0])) {
        } else if (action.equals(actions[1])) {
            Session remove = null;
            for (Session s : NtorrentApplication.SESSIONS) {
            }
            if (getTabCount() != 0) removeTabAt(getSelectedIndex());
            if (remove != null) NtorrentApplication.SESSIONS.remove(remove);
        }
    }
}
