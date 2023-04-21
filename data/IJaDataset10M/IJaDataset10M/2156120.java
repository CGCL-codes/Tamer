package linker.message;

import javax.swing.JPopupMenu;
import linker.gui.MainWindow;
import linker.list.ListItem;
import linker.list.Listmodel;

/**
 * The messagelist, used to hold messages and show the message as a list.
 * 
 * @version 2008-05-16
 * @author Jianfeng tujf.cn@gmail.com
 * 
 */
public class MessageList extends Listmodel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * The mainWindow refer.
	 */
    private MainWindow mainWindow;

    /**
	 * 
	 * @param window
	 *            The mainWindow's refer.
	 */
    public MessageList(final MainWindow window) {
        this.mainWindow = window;
    }

    /**
	 * Double click event.
	 * 
	 * @param item
	 *            The listItem which has been clicked.
	 */
    public final void doubleClickByItem(final ListItem item) {
        item.doubleClick();
    }

    /**
	 * Get popupmenu by the given item.
	 * 
	 * @param listItem
	 *            The listItem been clicked.
	 * @return The popupMenu.
	 */
    public final JPopupMenu getPopupMenuByItem(final ListItem listItem) {
        return null;
    }

    /**
	 * Add messageItem to the messageList.
	 * 
	 * @param messageItem
	 *            The item to be added.
	 */
    public final void addMessage(final MessageItem messageItem) {
        if (!contains(messageItem)) {
            addElement(messageItem);
        }
        mainWindow.setStatusLabel(size());
    }

    /**
	 * Remove messageItem to the messageList.
	 * 
	 * @param message
	 *            The messageItem to be removed.
	 */
    public final void removeMessage(final MessageItem message) {
        if (contains(message)) {
            removeElement(message);
        }
        mainWindow.setStatusLabel(size());
    }

    /**
	 * Open first message in the message list. When it is fail ,return false.
	 * 
	 * @return If open first message success, return true.
	 */
    public final boolean openFirst() {
        if (size() == 0) {
            return false;
        } else {
            ((MessageItem) firstElement()).doubleClick();
        }
        return true;
    }
}
