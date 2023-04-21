package xbrowser.widgets;

import java.beans.*;
import javax.swing.*;
import javax.swing.event.*;
import xbrowser.XRepository;
import xbrowser.bookmark.XBookmarkFolder;

class XBookmarkFolderMenu extends JMenu implements PropertyChangeListener, MenuListener {

    XBookmarkFolderMenu(XBookmarkFolder bookmark_folder) {
        super(bookmark_folder.getTitle());
        setIcon(bookmark_folder.isPersonalFolder() ? ICON_PERSONAL_CLOSED_BM_FOLDER : ICON_CLOSED_BM_FOLDER);
        setToolTipText(bookmark_folder.getTitle());
        bookmarkFolder = bookmark_folder;
        if (bookmarkFolder != null) bookmarkFolder.addPropertyChangeListener(this);
        addMenuListener(this);
    }

    public void removingFromParent() {
        if (bookmarkFolder != null) bookmarkFolder.removePropertyChangeListener(this);
    }

    public XBookmarkFolder getBookmarkFolder() {
        return bookmarkFolder;
    }

    public void propertyChange(PropertyChangeEvent evt) {
        String prop_name = evt.getPropertyName();
        Object new_value = evt.getNewValue();
        if (prop_name.equals("Title")) {
            setText((String) new_value);
            setToolTipText((String) new_value);
        }
    }

    public void setPersonal(boolean is_personal) {
        setIcon(is_personal ? ICON_PERSONAL_CLOSED_BM_FOLDER : ICON_CLOSED_BM_FOLDER);
    }

    public void menuCanceled(MenuEvent e) {
        setIcon(bookmarkFolder.isPersonalFolder() ? ICON_PERSONAL_CLOSED_BM_FOLDER : ICON_CLOSED_BM_FOLDER);
    }

    public void menuDeselected(MenuEvent e) {
        setIcon(bookmarkFolder.isPersonalFolder() ? ICON_PERSONAL_CLOSED_BM_FOLDER : ICON_CLOSED_BM_FOLDER);
    }

    public void menuSelected(MenuEvent e) {
        setIcon(bookmarkFolder.isPersonalFolder() ? ICON_PERSONAL_OPEN_BM_FOLDER : ICON_OPEN_BM_FOLDER);
    }

    private final ImageIcon ICON_OPEN_BM_FOLDER = XRepository.getComponentBuilder().buildImageIcon(this, "image.OpenBookmarkFolder");

    private final ImageIcon ICON_PERSONAL_OPEN_BM_FOLDER = XRepository.getComponentBuilder().buildImageIcon(this, "image.PersonalOpenBookmarkFolder");

    private final ImageIcon ICON_CLOSED_BM_FOLDER = XRepository.getComponentBuilder().buildImageIcon(this, "image.ClosedBookmarkFolder");

    private final ImageIcon ICON_PERSONAL_CLOSED_BM_FOLDER = XRepository.getComponentBuilder().buildImageIcon(this, "image.PersonalClosedBookmarkFolder");

    private XBookmarkFolder bookmarkFolder = null;
}
