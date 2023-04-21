package gnu.java.awt.peer.qt;

import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.peer.MenuPeer;
import java.util.Vector;

public class QtMenuPeer extends QtMenuComponentPeer implements MenuPeer {

    Vector items;

    boolean itemsAdded;

    public QtMenuPeer(QtToolkit kit, Menu owner) {
        super(kit, owner);
        itemsAdded = false;
    }

    protected native void init();

    protected void setup() {
        items = new Vector();
        setLabel(((Menu) owner).getLabel());
        if (((Menu) owner).isTearOff()) allowTearOff();
    }

    void addItems() {
        if (!itemsAdded) {
            Menu o = (Menu) owner;
            for (int i = 0; i < o.getItemCount(); i++) {
                MenuItem ci = (MenuItem) o.getItem(i);
                if (ci instanceof Menu && ci.getPeer() != null) ((QtMenuPeer) ci.getPeer()).addItems();
                addItem(ci);
            }
            itemsAdded = true;
        }
    }

    private void fireClick() {
        ActionEvent e = new ActionEvent(owner, ActionEvent.ACTION_PERFORMED, ((Menu) owner).getActionCommand());
        QtToolkit.eventQueue.postEvent(e);
    }

    private native void allowTearOff();

    private native void insertSeperator();

    private native void insertItem(QtMenuItemPeer p);

    private native void insertMenu(QtMenuPeer menu);

    private native void delItem(long ptr);

    private void add(long ptr) {
        items.add(new Long(ptr));
    }

    public void addItem(MenuItem item) {
        if (item instanceof Menu || item instanceof PopupMenu) insertMenu((QtMenuPeer) item.getPeer()); else {
            QtMenuItemPeer p = (QtMenuItemPeer) item.getPeer();
            insertItem(p);
        }
    }

    public void addSeparator() {
        insertSeperator();
    }

    public void delItem(int index) {
        long ptr = ((Long) items.elementAt(index)).longValue();
        delItem(ptr);
        items.removeElementAt(index);
    }

    public void disable() {
        setEnabled(false);
    }

    public void enable() {
        setEnabled(true);
    }

    public native void setEnabled(boolean enabled);

    public native void setLabel(String text);
}
