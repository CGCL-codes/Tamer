package sun.awt.pocketpc;

import java.util.ResourceBundle;
import java.util.MissingResourceException;
import java.awt.*;
import sun.awt.peer.*;
import java.awt.event.ActionEvent;

/**
 *
 *
 * @author Nicholas Allen
 */
class PPCMenuItemPeer extends PPCObjectPeer implements MenuItemPeer {

    private static native void initIDs();

    static {
        initIDs();
    }

    private boolean disposed = false;

    String shortcutLabel;

    private native void disposeNative();

    protected void disposeImpl() {
        disposeNative();
    }

    public final void dispose() {
        boolean call_disposeImpl = false;
        if (!disposed) {
            synchronized (this) {
                if (!disposed) {
                    disposed = call_disposeImpl = true;
                }
            }
        }
        if (call_disposeImpl) {
            disposeImpl();
        }
    }

    public void setEnabled(boolean b) {
        enable(b);
    }

    /**
     * DEPRECATED:  Replaced by setEnabled(boolean).
     */
    public void enable() {
        enable(true);
    }

    /**
     * DEPRECATED:  Replaced by setEnabled(boolean).
     */
    public void disable() {
        enable(false);
    }

    public void setLabel(String label) {
        if (this instanceof PPCPopupMenuPeer) return;
        MenuShortcut sc = ((MenuItem) target).getShortcut();
        shortcutLabel = (sc != null) ? sc.toString() : null;
        _setLabel(label);
    }

    public native void _setLabel(String label);

    boolean isCheckbox = false;

    protected PPCMenuItemPeer() {
    }

    PPCMenuItemPeer(MenuItem target) {
        this.target = target;
        PPCMenuPeer parent = (PPCMenuPeer) PPCToolkit.getMenuComponentPeer((MenuComponent) target.getParent());
        create(parent);
        MenuShortcut sc = ((MenuItem) target).getShortcut();
        if (sc != null) {
            shortcutLabel = sc.toString();
        }
    }

    native void create(PPCMenuPeer parent);

    native void enable(boolean e);

    protected void finalize() throws Throwable {
        dispose();
        super.finalize();
    }

    void handleAction(int modifiers) {
        PPCToolkit.postEvent(new ActionEvent(target, ActionEvent.ACTION_PERFORMED, ((MenuItem) target).getActionCommand(), modifiers));
    }

    private static Font defaultMenuFont;

    static {
        try {
            ResourceBundle rb = ResourceBundle.getBundle("sun.awt.windows.awtLocalization");
            defaultMenuFont = Font.decode(rb.getString("menuFont"));
        } catch (MissingResourceException e) {
            defaultMenuFont = new Font("SanSerif", Font.PLAIN, 11);
        }
    }

    Font getDefaultFont() {
        return defaultMenuFont;
    }
}
