package tjacobs.ui.menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import tjacobs.io.Main;
import tjacobs.ui.dialogs.StandardDialog;
import tjacobs.ui.ex.EditableList;
import tjacobs.ui.ex.EditableList.ELStandardDialog;
import tjacobs.ui.util.WindowTiler;

public class BookmarkMenu extends JMenu {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private JMenuItem mAdd = new JMenuItem("Add Bookmark"), mEdit = new JMenuItem("Edit Bookmarks");

    private BookmarkHandler mHandler;

    private BookmarkListener mBMListener = new BookmarkListener();

    private ArrayList<String> mBookmarks = new ArrayList<String>(4);

    private String mQueuedBookmark;

    private class AddEditListener implements ActionListener {

        public void actionPerformed(ActionEvent ae) {
            if (ae.getSource() == mAdd) {
                mBookmarks.add(mQueuedBookmark);
                createBookmarkMenu();
                Main m = Main.getSingleton();
                if (m != null) {
                    m.setProperty("BookmarkMenu", getPropertyString());
                    m.saveProperties();
                }
            }
            if (ae.getSource() == mEdit) {
                editBookmarks();
            }
        }
    }

    private void loadPropertyString(String str) {
        String[] parts = str.split("[|]");
        for (String s : parts) {
            mBookmarks.add(s);
        }
        createBookmarkMenu();
    }

    private String getPropertyString() {
        StringBuilder sb = new StringBuilder();
        for (String s : mBookmarks) {
            sb.append(s + "|");
        }
        return sb.toString();
    }

    private class BookmarkListener implements ActionListener {

        public void actionPerformed(ActionEvent ae) {
            mHandler.bookmarkInvoked(ae.getActionCommand());
        }
    }

    public BookmarkMenu(BookmarkHandler handler) {
        super("Bookmarks");
        setNoAvailableBookmark();
        mHandler = handler;
        ActionListener al = new AddEditListener();
        mAdd.addActionListener(al);
        mEdit.addActionListener(al);
        add(mAdd);
        add(mEdit);
        loadBookmarks();
        createBookmarkMenu();
    }

    private void loadBookmarks() {
        Main m = Main.getSingleton();
        if (m == null) return;
        String s = m.getProperty("BookmarkMenu");
        if (s != null) loadPropertyString(s);
        createBookmarkMenu();
    }

    public void editBookmarks() {
        Object data[] = mBookmarks.toArray();
        final ELStandardDialog sd = EditableList.getModalDialog(data);
        sd.addWindowListener(new WindowAdapter() {

            public void windowClosed(WindowEvent we) {
                if (sd.getCancelState() == StandardDialog.OK) {
                    Object[] data = sd.getEditableList().getData();
                    if (data != null) {
                        mBookmarks.clear();
                        for (Object o : data) {
                            mBookmarks.add(o.toString());
                        }
                        Main m = Main.getSingleton();
                        if (m != null) {
                            m.setProperty("BookmarkMenu", getPropertyString());
                            m.saveProperties();
                        }
                        createBookmarkMenu();
                    }
                }
            }
        });
        sd.pack();
        sd.setLocation(WindowTiler.SuggestLocation(sd));
        sd.setVisible(true);
    }

    private void createBookmarkMenu() {
        int count = getMenuComponentCount();
        for (int i = count - 1; i >= 3; i--) {
            JMenuItem item = (JMenuItem) getMenuComponent(i);
            item.removeActionListener(mBMListener);
            remove(item);
        }
        if (getMenuComponentCount() > 2) {
            remove(2);
        }
        if (mBookmarks.size() != 0) {
            addSeparator();
            for (String s : mBookmarks) {
                add(createMenuItem(s));
            }
        }
    }

    private JMenuItem createMenuItem(String s) {
        JMenuItem item = new JMenuItem(s);
        item.addActionListener(mBMListener);
        return item;
    }

    public static void main(String[] args) {
    }

    public void setAvailableBookmark(String bookmark) {
        mQueuedBookmark = bookmark;
        mAdd.setEnabled(bookmark != null);
    }

    public void setNoAvailableBookmark() {
        setAvailableBookmark(null);
    }

    public static interface BookmarkHandler {

        public void bookmarkInvoked(String bookmark);
    }
}
