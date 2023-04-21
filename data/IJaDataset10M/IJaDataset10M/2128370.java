package org.nakedobjects.viewer.skylark.basic;

import org.nakedobjects.viewer.skylark.CompositeViewBuilder;
import org.nakedobjects.viewer.skylark.View;
import org.nakedobjects.viewer.skylark.abstracts.AbstractBuilderDecorator;
import org.nakedobjects.viewer.skylark.drawing.Location;
import org.nakedobjects.viewer.skylark.drawing.Size;

public class StackLayout extends AbstractBuilderDecorator {

    private boolean fixedWidth;

    public StackLayout(final CompositeViewBuilder design) {
        super(design);
        this.fixedWidth = false;
    }

    public StackLayout(final CompositeViewBuilder design, final boolean fixedWidth) {
        super(design);
        this.fixedWidth = fixedWidth;
    }

    public Size getRequiredSize(final View view) {
        int height = 0;
        int width = 0;
        View views[] = view.getSubviews();
        for (int i = 0; i < views.length; i++) {
            View v = views[i];
            Size s = v.getRequiredSize(new Size());
            width = Math.max(width, s.getWidth());
            height += s.getHeight();
        }
        return new Size(width, height);
    }

    public boolean isOpen() {
        return true;
    }

    public void layout(final View view, final Size maximumSize) {
        int x = 0, y = 0;
        View subviews[] = view.getSubviews();
        int maxWidth = 0;
        for (int i = 0; i < subviews.length; i++) {
            View v = subviews[i];
            v.layout(new Size(maximumSize));
            Size s = v.getRequiredSize(new Size(maximumSize));
            maxWidth = Math.max(maxWidth, s.getWidth());
        }
        for (int i = 0; i < subviews.length; i++) {
            View v = subviews[i];
            Size s = v.getRequiredSize(new Size());
            if (fixedWidth || v.getSpecification() instanceof TextFieldSpecification) {
                s.ensureWidth(maxWidth);
            }
            v.setSize(s);
            v.setLocation(new Location(x, y));
            y += s.getHeight();
        }
    }
}
