package com.tensegrity.palorules.source;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import com.tensegrity.palorules.EditorResources;
import com.tensegrity.palorules.IRuleSourcePartEditor;
import com.tensegrity.palorules.RuleEditorMessages;

/**
 * A button to fold / unfold items.
 * 
 * @author AndreasEbbert
 * @version $Id: FoldButton.java,v 1.4 2008/05/19 16:13:02 AndreasEbbert Exp $
 */
class FoldButton extends Canvas implements PaintListener {

    static final Rectangle FB_BOUNDS;

    static {
        FB_BOUNDS = EditorResources.ICON_FOLDED.getBounds();
    }

    private Image image;

    private final IContentItem cItem;

    FoldButton(Composite parent, final IContentItem item, IRuleSourcePartEditor ed, Color bgColor) {
        this(parent, item, false, ed, bgColor);
    }

    FoldButton(Composite parent, final IContentItem item, boolean folded, IRuleSourcePartEditor ed, Color bgColor) {
        super(parent, SWT.NONE);
        image = folded ? EditorResources.ICON_FOLDED : EditorResources.ICON_UNFOLDED;
        this.cItem = item;
        setFolded(false);
        setBackground(bgColor);
        setToolTipText(RuleEditorMessages.getString("button.fold"));
        addMouseListener(new MouseAdapter() {

            public void mouseDown(MouseEvent e) {
                setFolded(!isFolded());
            }
        });
        addPaintListener(this);
    }

    private void setFolded(boolean b) {
        if (cItem.isFolded() == b) return;
        cItem.setFolded(b);
        redraw();
    }

    private boolean isFolded() {
        return cItem.isFolded();
    }

    public Point computeSize(int wHint, int hHint, boolean changed) {
        int w = 3;
        int h = 3;
        if (image != null) {
            w += image.getBounds().width;
            h += image.getBounds().height;
        }
        return new Point(w, h);
    }

    public void paintControl(PaintEvent e) {
        GC gc = e.gc;
        if (image != null) {
            gc.drawImage(image, 1, 1);
        }
    }
}
