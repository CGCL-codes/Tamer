package dnl.net.netclip;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import org.apache.commons.lang.SystemUtils;

/**
 * 
 * @author <a href="mailto:daniel_or_else@yahoo.com">Daniel Or </a>
 */
public abstract class TrayManager {

    private Map<String, TrayMenu> menues = new HashMap<String, TrayMenu>();

    public static TrayManager getTrayManager(String name, ImageIcon icon) {
        if (SystemUtils.IS_OS_LINUX) {
            return new LinuxTrayManager(name, icon);
        } else if (SystemUtils.IS_OS_WINDOWS) {
            return new TrayManagerImpl(name, icon);
        }
        throw new UnsupportedOperationException("");
    }

    public TrayManager(String name, ImageIcon imageIcon) {
    }

    public TrayMenu getTrayMenu(String path) {
        TrayMenu trayMenu = menues.get(path);
        if (trayMenu == null && "/".equals(path)) {
            TrayMenu root = getRoot();
            menues.put("/", root);
            return root;
        }
        return trayMenu;
    }

    public void addMenu(String menuItemPath) {
        TrayMenu trayMenu = addMenuItemImpl(menuItemPath, true);
        TrayMenu parent = getTrayMenu(trayMenu.getParentPath());
        parent.addMenuItem(trayMenu);
        menues.put(menuItemPath, trayMenu);
    }

    public void removeAllChildrenItems(String menuItemPath) {
        TrayMenu parent = getTrayMenu(menuItemPath);
        parent.removeAllChildrenItems();
    }

    public void addMenuItem(String menuItemPath) {
        TrayMenu trayMenu = addMenuItemImpl(menuItemPath, false);
        TrayMenu parent = getTrayMenu(trayMenu.getParentPath());
        parent.addMenuItem(trayMenu);
        menues.put(menuItemPath, trayMenu);
    }

    protected abstract TrayMenu getRoot();

    protected abstract TrayMenu addMenuItemImpl(String path, boolean menu);

    public void addActionListener(String menuItemPath, ActionListener al) {
        TrayMenu trayMenu = getTrayMenu(menuItemPath);
        trayMenu.addActionListener(al);
    }

    protected abstract void addSeperator();

    public abstract void displayMessage(String caption, String message);

    public abstract void displayInfoMessage(String caption, String message);

    public abstract void displayErrorMessage(String caption, String message);

    public void addDefaultExitMenu() {
        addMenuItem("/Exit");
        TrayMenu exitMenuItem = getTrayMenu("/Exit");
        exitMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                System.exit(0);
            }
        });
    }

    protected abstract static class TrayMenu {

        private String path;

        public TrayMenu(String path) {
            this.path = path;
        }

        public abstract void addActionListener(ActionListener al);

        public abstract void addMenuItem(TrayMenu parent);

        public abstract void removeAllChildrenItems();

        public String getPath() {
            return path;
        }

        public String getParentPath() {
            int i = path.lastIndexOf('/');
            if (i <= 0) {
                return "/";
            }
            return path.substring(0, i);
        }

        public String getName() {
            int i = path.lastIndexOf('/');
            if (i < 0) {
                return path;
            }
            return path.substring(i + 1);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((path == null) ? 0 : path.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            final TrayMenu other = (TrayMenu) obj;
            if (path == null) {
                if (other.path != null) return false;
            } else if (!path.equals(other.path)) return false;
            return true;
        }
    }
}
