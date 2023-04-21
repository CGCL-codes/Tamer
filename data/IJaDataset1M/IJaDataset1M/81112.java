package com.comarch.depth.common;

import net.sf.vex.swt.VexWidget;
import org.eclipse.swt.graphics.Point;

public class VexHelper {

    public static Point getCurrentCursorPosition(VexWidget widget) {
        int vertScrollOffset = 0;
        int horiScrollOffset = 0;
        try {
            vertScrollOffset = widget.getVerticalBar().getSelection();
            horiScrollOffset = widget.getHorizontalBar().getSelection();
        } catch (Exception e) {
        }
        Point d = widget.toDisplay(widget.getVexCaret().getX(), widget.getVexCaret().getY());
        int caretHeight = widget.getVexCaret().getBounds().getHeight();
        int caretWidth = widget.getVexCaret().getBounds().getWidth();
        return new Point(d.x + caretWidth - horiScrollOffset, d.y + caretHeight - vertScrollOffset);
    }
}
