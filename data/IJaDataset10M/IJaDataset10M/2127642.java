package net.sf.yari.ui.util.internal;

import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;

public class UIUpdate {

    public static void updateLayout(Composite comp) {
        Composite c = comp;
        while (c != null) {
            c.setRedraw(false);
            c = c.getParent();
            if (c instanceof ScrolledComposite) {
                break;
            }
        }
        c = comp;
        while (c != null) {
            c.layout(true);
            c = c.getParent();
            if (c instanceof ScrolledComposite) {
                break;
            }
        }
        c = comp;
        while (c != null) {
            c.setRedraw(true);
            c = c.getParent();
            if (c instanceof ScrolledComposite) {
                break;
            }
        }
    }
}
