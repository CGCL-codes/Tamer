package org.nakedobjects.viewer.skylark.special;

import org.nakedobjects.viewer.skylark.CompositeViewBuilder;
import org.nakedobjects.viewer.skylark.Location;
import org.nakedobjects.viewer.skylark.Size;
import org.nakedobjects.viewer.skylark.View;
import org.nakedobjects.viewer.skylark.core.AbstractBuilderDecorator;

public class GridLayout extends AbstractBuilderDecorator {

    public GridLayout(CompositeViewBuilder design) {
        super(design);
    }

    public Size getRequiredSize(View view) {
        int height = 0;
        int width = 0;
        View views[] = view.getSubviews();
        for (int i = 0; i < views.length; i++) {
            View v = views[i];
            Size s = v.getRequiredSize();
            height = Math.max(height, s.getHeight());
            width += s.getWidth();
        }
        return new Size(width, height);
    }

    public boolean isOpen() {
        return true;
    }

    public void layout(View view) {
        int x = 0, y = 0;
        View views[] = view.getSubviews();
        for (int i = 0; i < views.length; i++) {
            View v = views[i];
            Size s = v.getRequiredSize();
            v.setSize(s);
            v.setLocation(new Location(x, y));
            x += s.getWidth();
        }
    }
}
