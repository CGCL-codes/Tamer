package org.nakedobjects.viewer.dnd.special;

import org.nakedobjects.viewer.dnd.CompositeViewBuilder;
import org.nakedobjects.viewer.dnd.Location;
import org.nakedobjects.viewer.dnd.Size;
import org.nakedobjects.viewer.dnd.View;
import org.nakedobjects.viewer.dnd.core.AbstractBuilderDecorator;

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
