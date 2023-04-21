package org.nakedobjects.viewer.skylark.basic;

import org.nakedobjects.object.Naked;
import org.nakedobjects.viewer.skylark.Canvas;
import org.nakedobjects.viewer.skylark.Content;
import org.nakedobjects.viewer.skylark.View;
import org.nakedobjects.viewer.skylark.ViewAxis;
import org.nakedobjects.viewer.skylark.ViewSpecification;
import org.nakedobjects.viewer.skylark.drawing.Size;
import org.nakedobjects.viewer.skylark.drawing.Text;

public class SmallObjectIcon extends ObjectView {

    private IconGraphic icon;

    private TitleText text;

    public SmallObjectIcon(final Content content, final ViewSpecification specification, final ViewAxis axis, final Text style) {
        super(content, specification, axis);
        icon = new IconGraphic(this, style);
        text = new ObjectTitleText(this, style);
    }

    public void draw(final Canvas canvas) {
        super.draw(canvas);
        int x = 0;
        int y = icon.getBaseline();
        icon.draw(canvas, x, y);
        x += icon.getSize().getWidth();
        x += View.HPADDING;
        text.draw(canvas, x, y);
    }

    public int getBaseline() {
        return icon.getBaseline();
    }

    public Size getMaximumSize() {
        Size size = icon.getSize();
        size.extendWidth(View.HPADDING);
        size.extendWidth(text.getSize().getWidth());
        return size;
    }

    public String toString() {
        return "SmallObjectIcon" + getId();
    }

    public void update(final Naked object) {
        View p = getParent();
        if (p != null) {
            p.invalidateLayout();
        }
    }
}
