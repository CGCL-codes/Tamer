package xbrowser.renderer.custom;

import java.awt.*;
import javax.swing.text.*;

public class XPrintView extends BoxView {

    public XPrintView(Element elem, View root, int w, int h) {
        super(elem, Y_AXIS);
        setParent(root);
        setSize(w, h);
        layout(w, h);
    }

    public boolean paintPage(Graphics g, int page_height, int page_index) {
        if (page_index > pageIndex) {
            firstOnPage = lastOnPage + 1;
            if (firstOnPage >= getViewCount()) return false;
            pageIndex = page_index;
        }
        int min = getOffset(Y_AXIS, firstOnPage);
        int max = min + page_height;
        Rectangle rc = new Rectangle();
        for (int i = firstOnPage; i < getViewCount(); i++) {
            rc.x = getOffset(X_AXIS, i);
            rc.y = getOffset(Y_AXIS, i);
            rc.width = getSpan(X_AXIS, i);
            rc.height = getSpan(Y_AXIS, i);
            if (rc.y + rc.height > max) break;
            lastOnPage = i;
            rc.y -= min;
            paintChild(g, rc, i);
        }
        return true;
    }

    private int firstOnPage = 0;

    private int lastOnPage = 0;

    private int pageIndex = 0;
}
