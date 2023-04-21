package org.eclipse.swt.internal.dnd;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.internal.xhtml.CSSStyle;
import org.eclipse.swt.internal.xhtml.document;

/**
 * @author zhou renjian
 *
 * 2006-3-15
 */
public class ScaleDND implements DragListener {

    protected int sourceX = 0;

    protected int sourceY = 0;

    protected boolean isHorizontal;

    public boolean dragBegan(DragEvent e) {
        String cssName = e.sourceElement.className;
        if (cssName.indexOf("horizontal") != -1) {
            isHorizontal = true;
        } else {
            isHorizontal = false;
        }
        CSSStyle style = e.sourceElement.style;
        this.sourceX = style.left.length() > 0 ? Integer.parseInt(style.left) : 0;
        this.sourceY = style.top.length() > 0 ? Integer.parseInt(style.top) : 0;
        e.startX = e.currentX;
        e.startY = e.currentY;
        return true;
    }

    public boolean dragCanceled(DragEvent e) {
        document.body.style.cursor = "auto";
        return true;
    }

    public boolean dragEnded(DragEvent e) {
        document.body.style.cursor = "auto";
        return true;
    }

    protected Point currentLocation(DragEvent e) {
        int xx = this.sourceX + e.deltaX();
        int yy = this.sourceY + e.deltaY();
        CSSStyle parentStyle = e.sourceElement.parentNode.style;
        int gHeight = parentStyle.height.length() > 0 ? Integer.parseInt(parentStyle.height) : 0;
        int gWidth = parentStyle.width.length() > 0 ? Integer.parseInt(parentStyle.width) : 0;
        CSSStyle style = e.sourceElement.style;
        int dWidth = style.width.length() > 0 ? Integer.parseInt(style.width) : 0;
        int dHeight = style.height.length() > 0 ? Integer.parseInt(style.height) : 0;
        if (isHorizontal) {
            dWidth = 10;
            dHeight = 18;
        } else {
            dWidth = 18;
            dHeight = 10;
        }
        if (xx < 0) {
            xx = 0;
        } else if (xx > gWidth - dWidth - 2) {
            xx = gWidth - dWidth - 2;
        }
        if (yy < 0) {
            yy = 0;
        } else if (yy > gHeight - dHeight - 2) {
            yy = gHeight - dHeight - 2;
        }
        return new Point(xx, yy);
    }

    public boolean dragging(DragEvent e) {
        if (isHorizontal) {
            e.sourceElement.style.left = currentLocation(e).x + "px";
        } else {
            e.sourceElement.style.top = currentLocation(e).y + "px";
        }
        return true;
    }

    public boolean isDraggable(HTMLEventWrapper e) {
        return true;
    }
}
