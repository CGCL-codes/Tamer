package org.iwidget.desktop.core.javascript;

import java.awt.Toolkit;
import java.awt.datatransfer.*;
import org.mozilla.javascript.ScriptableObject;

public class IwidgetClipboard extends ScriptableObject implements ClipboardOwner {

    public String getClassName() {
        return "clipboard";
    }

    public IwidgetClipboard() {
    }

    public void jsConstructor() {
    }

    public void lostOwnership(Clipboard clipboard, Transferable transferable) {
    }

    public void jsSet_set(String string) {
        set(string);
    }

    public String jsGet_get() {
        return get();
    }

    public void jsFunction_set(String string) {
        set(string);
    }

    public String jsFunction_get() {
        return get();
    }

    public void set(String data) {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) try {
            sm.checkSystemClipboardAccess();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toolkit tk = Toolkit.getDefaultToolkit();
        StringSelection st = new StringSelection(data);
        Clipboard cp = tk.getSystemClipboard();
        cp.setContents(st, this);
    }

    public String get() {
        try {
            Transferable contents;
            SecurityManager sm = System.getSecurityManager();
            if (sm != null) {
                try {
                    sm.checkSystemClipboardAccess();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Toolkit tk = Toolkit.getDefaultToolkit();
            Clipboard cp = tk.getSystemClipboard();
            contents = cp.getContents(this);
            if (!contents.isDataFlavorSupported(DataFlavor.stringFlavor)) return null;
            return (String) contents.getTransferData(DataFlavor.stringFlavor);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static final long serialVersionUID = 0x3136313435393939L;
}
