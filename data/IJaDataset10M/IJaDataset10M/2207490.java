package xbrowser.widgets;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;
import java.util.*;
import javax.swing.*;
import xbrowser.XRepository;
import xbrowser.bookmark.*;
import xbrowser.bookmark.event.XBookmarkFolderListener;

public class XPersonalToolBar extends JToolBar implements XBookmarkFolderListener {

    public XPersonalToolBar() {
        setFloatable(false);
        setBorder(BorderFactory.createTitledBorder(""));
        XRepository.getBookmarkManager().addBookmarkFolderListener(this);
    }

    private void removeFromToolBar(XAbstractBookmark abs_bm_to_remove) {
        Component[] components = getComponents();
        XAbstractBookmark abs_bm;
        for (int i = 0; i < components.length; i++) {
            if (components[i] instanceof XPersonalBookmarkFolderButton) abs_bm = ((XPersonalBookmarkFolderButton) components[i]).getBookmarkFolder(); else abs_bm = ((XPersonalBookmarkButton) components[i]).getBookmark();
            if (abs_bm == abs_bm_to_remove) {
                if (components[i] instanceof XPersonalBookmarkFolderButton) ((XPersonalBookmarkFolderButton) components[i]).removingFromParent(); else ((XPersonalBookmarkButton) components[i]).removingFromParent();
                remove(i);
                break;
            }
        }
    }

    private void removeFromMenu(XAbstractBookmark abs_bm_to_remove, JMenu folder_menu) {
        Component[] comps = folder_menu.getMenuComponents();
        XAbstractBookmark abs_bm = null;
        for (int i = 0; i < comps.length; i++) {
            if (comps[i] instanceof XBookmarkMenuItem) abs_bm = ((XBookmarkMenuItem) comps[i]).getBookmark(); else if (comps[i] instanceof XBookmarkFolderMenu) abs_bm = ((XBookmarkFolderMenu) comps[i]).getBookmarkFolder();
            if (abs_bm == abs_bm_to_remove) {
                if (comps[i] instanceof XBookmarkMenuItem) ((XBookmarkMenuItem) comps[i]).removingFromParent(); else if (comps[i] instanceof XBookmarkFolderMenu) ((XBookmarkFolderMenu) comps[i]).removingFromParent();
                folder_menu.remove(i);
                break;
            }
        }
    }

    private void addToToolBar(XAbstractBookmark new_abs_bm) {
        Component[] components = getComponents();
        XAbstractBookmark abs_bm;
        JButton btn;
        int i;
        for (i = 0; i < components.length; i++) {
            if (components[i] instanceof XPersonalBookmarkFolderButton) abs_bm = ((XPersonalBookmarkFolderButton) components[i]).getBookmarkFolder(); else abs_bm = ((XPersonalBookmarkButton) components[i]).getBookmark();
            if (comparator.compare(new_abs_bm, abs_bm) < 0) break;
        }
        if (new_abs_bm instanceof XBookmark) btn = new XPersonalBookmarkButton((XBookmark) new_abs_bm); else if (new_abs_bm instanceof XBookmarkFolder) btn = new XPersonalBookmarkFolderButton((XBookmarkFolder) new_abs_bm); else return;
        Dimension size = btn.getPreferredSize();
        XRepository.getComponentBuilder().buildFlatLook(btn, size.width, 25);
        add(btn, i);
    }

    private void addToMenu(XAbstractBookmark new_abs_bm, JMenu folder_menu) {
        Component[] comps = folder_menu.getMenuComponents();
        XAbstractBookmark abs_bm = null;
        int i;
        for (i = 0; i < comps.length; i++) {
            if (comps[i] instanceof XBookmarkMenuItem) abs_bm = ((XBookmarkMenuItem) comps[i]).getBookmark(); else if (comps[i] instanceof XBookmarkFolderMenu) abs_bm = ((XBookmarkFolderMenu) comps[i]).getBookmarkFolder();
            if (comparator.compare(new_abs_bm, abs_bm) < 0) break;
        }
        if (new_abs_bm instanceof XBookmark) folder_menu.insert(new XBookmarkMenuItem((XBookmark) new_abs_bm), i); else if (new_abs_bm instanceof XBookmarkFolder) folder_menu.insert(new XBookmarkFolderMenu((XBookmarkFolder) new_abs_bm), i);
    }

    private XBookmarkFolderMenu getBookmarkMenu(XBookmarkFolder bookmark_folder, XBookmarkFolderMenu base_menu) {
        if (base_menu.getBookmarkFolder() == bookmark_folder) return base_menu;
        Component[] components = base_menu.getMenuComponents();
        XBookmarkFolderMenu menu;
        for (int i = 0; i < components.length; i++) {
            if (components[i] instanceof XBookmarkFolderMenu) {
                menu = (XBookmarkFolderMenu) components[i];
                if (menu.getBookmarkFolder() == bookmark_folder) return menu; else {
                    menu = getBookmarkMenu(bookmark_folder, menu);
                    if (menu != null) return menu;
                }
            }
        }
        return null;
    }

    public void bookmarkAdded(XAbstractBookmark abs_bm, XBookmarkFolder parent) {
        if (parent.isPersonalFolder()) addToToolBar(abs_bm); else if (parent.isUnderPersonalFolder()) {
            Component[] components = getComponents();
            JMenu menu;
            for (int i = 0; i < components.length; i++) {
                if (components[i] instanceof XPersonalBookmarkFolderButton) {
                    menu = getBookmarkMenu(parent, ((XPersonalBookmarkFolderButton) components[i]).getFolderMenu());
                    if (menu != null) {
                        addToMenu(abs_bm, menu);
                        break;
                    }
                }
            }
        }
        if (abs_bm instanceof XBookmarkFolder) {
            XBookmarkFolder folder = (XBookmarkFolder) abs_bm;
            Iterator it = folder.getBookmarks();
            while (it.hasNext()) bookmarkAdded((XAbstractBookmark) it.next(), folder);
        }
    }

    public void bookmarkRemoved(XAbstractBookmark abs_bm, XBookmarkFolder parent) {
        if (abs_bm instanceof XBookmarkFolder) {
            XBookmarkFolder folder = (XBookmarkFolder) abs_bm;
            Iterator it = folder.getBookmarks();
            while (it.hasNext()) bookmarkRemoved((XAbstractBookmark) it.next(), folder);
        }
        if (parent.isPersonalFolder()) removeFromToolBar(abs_bm); else if (parent.isUnderPersonalFolder()) {
            Component[] components = getComponents();
            JMenu menu;
            for (int i = 0; i < components.length; i++) {
                if (components[i] instanceof XPersonalBookmarkFolderButton) {
                    menu = getBookmarkMenu(parent, ((XPersonalBookmarkFolderButton) components[i]).getFolderMenu());
                    if (menu != null) {
                        removeFromMenu(abs_bm, menu);
                        return;
                    }
                }
            }
        }
    }

    public void personalFolderChanged(XBookmarkFolder old_folder, XBookmarkFolder new_folder) {
        if (old_folder != null) {
            Iterator it = old_folder.getBookmarks();
            while (it.hasNext()) removeFromToolBar((XAbstractBookmark) it.next());
        }
        if (new_folder != null) {
            Iterator it = new_folder.getBookmarks();
            while (it.hasNext()) bookmarkAdded((XAbstractBookmark) it.next(), new_folder);
        }
        repaint();
    }

    public void clearBookmarks() {
    }

    private class XPersonalBookmarkButton extends JButton implements ActionListener, PropertyChangeListener {

        public XPersonalBookmarkButton(XBookmark bookmark) {
            super(bookmark.getTitle());
            setIcon(XRepository.getComponentBuilder().buildImageIcon(XPersonalToolBar.this, "image.Bookmark"));
            setToolTipText(bookmark.getHRef());
            this.bookmark = bookmark;
            this.bookmark.addPropertyChangeListener(this);
            addActionListener(this);
        }

        public XBookmark getBookmark() {
            return bookmark;
        }

        public void propertyChange(PropertyChangeEvent evt) {
            String prop_name = evt.getPropertyName();
            Object new_value = evt.getNewValue();
            if (prop_name.equals("Title")) setText((String) new_value); else if (prop_name.equals("HRef")) setToolTipText((String) new_value);
        }

        public void actionPerformed(ActionEvent e) {
            bookmark.openInSamePage();
        }

        public void removingFromParent() {
            bookmark.removePropertyChangeListener(this);
        }

        private XBookmark bookmark = null;
    }

    private class XPersonalBookmarkFolderButton extends JButton implements ActionListener, PropertyChangeListener {

        public XPersonalBookmarkFolderButton(XBookmarkFolder bm_folder) {
            super(bm_folder.getTitle());
            setIcon(XRepository.getComponentBuilder().buildImageIcon(XPersonalToolBar.this, "image.BookmarkFolder"));
            setToolTipText(bm_folder.getTitle());
            bookmarkFolder = bm_folder;
            folderMenu = new XBookmarkFolderMenu(bookmarkFolder);
            bookmarkFolder.addPropertyChangeListener(this);
            addActionListener(this);
        }

        public XBookmarkFolder getBookmarkFolder() {
            return bookmarkFolder;
        }

        public XBookmarkFolderMenu getFolderMenu() {
            return folderMenu;
        }

        public void propertyChange(PropertyChangeEvent evt) {
            String prop_name = evt.getPropertyName();
            Object new_value = evt.getNewValue();
            if (prop_name.equals("Title")) {
                setText((String) new_value);
                setToolTipText((String) new_value);
            }
        }

        public void actionPerformed(ActionEvent e) {
            folderMenu.getPopupMenu().show(this, 0, getHeight());
            folderMenu.getPopupMenu().setInvoker(folderMenu);
        }

        public void removingFromParent() {
            bookmarkFolder.removePropertyChangeListener(this);
        }

        private XBookmarkFolder bookmarkFolder = null;

        private XBookmarkFolderMenu folderMenu = null;
    }

    private XBookmarkComparator comparator = new XBookmarkComparator(0, true);
}
