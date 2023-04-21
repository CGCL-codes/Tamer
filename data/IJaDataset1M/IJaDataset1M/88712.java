package com.ivis.xprocess.ui.util;

import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;
import com.ivis.xprocess.ui.datawrappers.IElementWrapper;
import com.ivis.xprocess.ui.util.transfer.ElementTransfer;

public class ClipboardUtil {

    public static boolean containsTreeObject() {
        Clipboard clipBoard = new Clipboard(Display.getDefault());
        Transfer elementTransfer = ElementTransfer.getInstance();
        Object object = clipBoard.getContents(elementTransfer);
        if (object instanceof Object[]) {
            Object[] objs = (Object[]) object;
            for (int i = 0; i < objs.length; i++) {
                if (objs[i] instanceof IElementWrapper) {
                    return true;
                }
            }
        }
        return false;
    }
}
