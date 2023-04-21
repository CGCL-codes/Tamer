package org.nakedobjects.plugins.dnd.viewer.builder;

import org.nakedobjects.plugins.dnd.LabelAxis;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewBuilder;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;

public class LabelledFieldLayout extends AbstractBuilderDecorator {

    public LabelledFieldLayout(final ViewBuilder design) {
        super(design);
    }

    @Override
    public Size getRequiredSize(final View view) {
        int height = 0;
        int width = 0;
        final View views[] = view.getSubviews();
        for (int i = 0; i < views.length; i++) {
            final View v = views[i];
            final Size s = v.getRequiredSize(new Size());
            height = Math.max(height, s.getHeight());
            width += s.getWidth();
        }
        width += View.HPADDING;
        return new Size(width, height);
    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public void layout(final View view, final Size maximumSize) {
        int x = 0;
        final int y = 0;
        final View subviews[] = view.getSubviews();
        for (int i = 0; i < subviews.length; i++) {
            final View v = subviews[i];
            v.layout(maximumSize);
            final Size s = v.getRequiredSize(new Size());
            v.setSize(s);
            v.setLocation(new Location(x, y));
            x = ((LabelAxis) v.getViewAxis()).getWidth() + View.HPADDING;
        }
    }
}
