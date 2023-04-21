package org.nakedobjects.viewer.skylark.basic;

import org.nakedobjects.viewer.skylark.Canvas;
import org.nakedobjects.viewer.skylark.ContentDrag;
import org.nakedobjects.viewer.skylark.Drag;
import org.nakedobjects.viewer.skylark.DragStart;
import org.nakedobjects.viewer.skylark.Toolkit;
import org.nakedobjects.viewer.skylark.View;
import org.nakedobjects.viewer.skylark.ViewAreaType;
import org.nakedobjects.viewer.skylark.abstracts.AbstractBorder;
import org.nakedobjects.viewer.skylark.abstracts.AbstractView;
import org.nakedobjects.viewer.skylark.drawing.Bounds;
import org.nakedobjects.viewer.skylark.drawing.Location;
import org.nakedobjects.viewer.skylark.drawing.Size;
import org.nakedobjects.viewer.skylark.drawing.Text;

public class IconBorder extends AbstractBorder {

    private static final Text TITLE_STYLE = Toolkit.getText("title");

    private int baseline;

    private int titlebarHeight;

    private int padding = 0;

    private IconGraphic icon;

    private TitleText text;

    public IconBorder(final View wrappedView) {
        super(wrappedView);
        icon = new IconGraphic(this, TITLE_STYLE);
        text = new ObjectTitleText(this, TITLE_STYLE);
        titlebarHeight = icon.getSize().getHeight() + 1;
        top = titlebarHeight;
        baseline = icon.getBaseline() + 1;
    }

    public void debugDetails(final StringBuffer b) {
        b.append("IconBorder " + left + " pixels\n");
        b.append("           titlebar " + (top - titlebarHeight) + " pixels");
        super.debugDetails(b);
    }

    public Drag dragStart(final DragStart drag) {
        if (overBorder(drag.getLocation())) {
            View dragOverlay = new DragContentIcon(getContent());
            return new ContentDrag(this, drag.getLocation(), dragOverlay);
        } else {
            return super.dragStart(drag);
        }
    }

    public void draw(final Canvas canvas) {
        int x = left + HPADDING;
        if (AbstractView.debug) {
            canvas.drawDebugOutline(new Bounds(getSize()), baseline, Toolkit.getColor("debug.bounds.draw"));
        }
        icon.draw(canvas, x, baseline);
        x += icon.getSize().getWidth();
        x += View.HPADDING;
        text.draw(canvas, x, baseline);
        super.draw(canvas);
    }

    public int getBaseline() {
        return wrappedView.getBaseline() + baseline + titlebarHeight;
    }

    public Size getRequiredSize(final Size maximumSize) {
        Size size = super.getRequiredSize(maximumSize);
        size.ensureWidth(left + icon.getSize().getWidth() + View.HPADDING + text.getSize().getWidth() + padding + right);
        return size;
    }

    public ViewAreaType viewAreaType(final Location mouseLocation) {
        Bounds title = new Bounds(new Location(), icon.getSize());
        title.extendWidth(left);
        title.extendWidth(text.getSize().getWidth());
        if (title.contains(mouseLocation)) {
            return ViewAreaType.CONTENT;
        } else {
            return super.viewAreaType(mouseLocation);
        }
    }

    public String toString() {
        return wrappedView.toString() + "/WindowBorder [" + getSpecification() + "]";
    }
}
