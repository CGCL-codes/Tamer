package org.eclipse.ui.internal.presentations.defaultpresentation;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.internal.presentations.util.AbstractTabFolder;
import org.eclipse.ui.internal.presentations.util.AbstractTabItem;
import org.eclipse.ui.internal.presentations.util.EnhancedFillLayout;
import org.eclipse.ui.internal.presentations.util.PartInfo;

/**
 * Implements the AbstractTabFolder interface, however this object only displays
 * the content of the currently selected part. There are no tabs, no title, no toolbar,
 * etc. There is no means to select a different part, unless it is done programmatically.
 * 
 * @since 3.1
 */
public class EmptyTabFolder extends AbstractTabFolder {

    private Composite control;

    private Control childControl;

    private Color borderColor;

    public EmptyTabFolder(Composite parent, boolean showborder) {
        control = new Composite(parent, SWT.NONE);
        EnhancedFillLayout layout = new EnhancedFillLayout();
        control.setLayout(layout);
        borderColor = parent.getDisplay().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);
        if (showborder) {
            layout.xmargin = 1;
            layout.ymargin = 1;
            control.addPaintListener(new PaintListener() {

                public void paintControl(PaintEvent e) {
                    e.gc.setForeground(borderColor);
                    Rectangle rect = control.getClientArea();
                    rect.width--;
                    rect.height--;
                    e.gc.drawRectangle(rect);
                }
            });
        }
    }

    public Point computeSize(int widthHint, int heightHint) {
        if (childControl != null) {
            if (childControl instanceof Composite) {
                Composite composite = (Composite) childControl;
                if (composite.getChildren().length == 0) {
                    EnhancedFillLayout layout = (EnhancedFillLayout) control.getLayout();
                    int w = widthHint == SWT.DEFAULT ? layout.xmargin * 2 : widthHint;
                    int h = heightHint == SWT.DEFAULT ? layout.ymargin * 2 : heightHint;
                    return new Point(w, h);
                }
            }
            return childControl.computeSize(widthHint, heightHint);
        }
        return new Point(0, 0);
    }

    public AbstractTabItem add(int index, int flags) {
        return new EmptyTabItem();
    }

    public Composite getContentParent() {
        return control;
    }

    public void setContent(Control newContent) {
        childControl = newContent;
    }

    public AbstractTabItem[] getItems() {
        return new AbstractTabItem[0];
    }

    public AbstractTabItem getSelection() {
        return null;
    }

    public void setSelection(AbstractTabItem toSelect) {
    }

    public void setToolbar(Control toolbar) {
        if (toolbar != null) {
            toolbar.setVisible(false);
        }
    }

    public void layout(boolean flushCache) {
        super.layout(flushCache);
        control.layout(flushCache);
    }

    public void setSelectedInfo(PartInfo info) {
    }

    public void enablePaneMenu(boolean enabled) {
    }

    public Composite getToolbarParent() {
        return control;
    }

    public Control getControl() {
        return control;
    }

    public Rectangle getTabArea() {
        return new Rectangle(0, 0, 0, 0);
    }
}
