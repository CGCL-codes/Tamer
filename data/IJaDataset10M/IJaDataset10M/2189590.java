package freestyleLearningGroup.independent.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

public class FLGLeftToRightLayout implements LayoutManager {

    protected int horizontalSpacing = 5;

    protected int verticalSpacing = 5;

    public FLGLeftToRightLayout(int horizontalSpacing) {
        this(horizontalSpacing, horizontalSpacing);
    }

    public FLGLeftToRightLayout(int horizontalSpacing, int verticalSpacing) {
        this.horizontalSpacing = horizontalSpacing;
        this.verticalSpacing = verticalSpacing;
    }

    public void layoutContainer(Container target) {
        Insets insets = target.getInsets();
        Dimension target_size = target.getSize();
        int maxwidth = target.getWidth() - (insets.left + insets.right + horizontalSpacing * 2);
        Component child;
        int nChildren = target.getComponentCount();
        int visibleHeight = target_size.height - insets.top - insets.bottom;
        int totalPrefWidth = 0;
        for (int i = 0; i < nChildren; i++) {
            child = target.getComponent(i);
            if (!child.isVisible()) continue;
            Dimension pref = child.getPreferredSize();
            totalPrefWidth = totalPrefWidth + pref.width;
        }
        int x = insets.left;
        int y = insets.top;
        for (int i = 0; i < nChildren; i++) {
            child = target.getComponent(i);
            if (!child.isVisible()) continue;
            Dimension pref = child.getPreferredSize();
            child.setBounds(x, y, pref.width, pref.height);
            int next_x = x + pref.width + horizontalSpacing;
            x = next_x;
        }
    }

    public Dimension preferredLayoutSize(Container target) {
        int totalPrefWidth = 0;
        int maxHeight = 0;
        Component child;
        int nChildren = target.getComponentCount();
        Insets insets = target.getInsets();
        for (int i = 0; i < nChildren; i++) {
            child = target.getComponent(i);
            if (!child.isVisible()) continue;
            Dimension pref = child.getPreferredSize();
            if (pref.height > maxHeight) maxHeight = pref.height;
            totalPrefWidth = totalPrefWidth + pref.width + horizontalSpacing;
        }
        Dimension dim = new Dimension(totalPrefWidth - horizontalSpacing + insets.left + insets.right, maxHeight + insets.top + insets.bottom);
        return dim;
    }

    public Dimension minimumLayoutSize(Container target) {
        return preferredLayoutSize(target);
    }

    public Dimension maximumLayoutSize(Container target) {
        return preferredLayoutSize(target);
    }

    public void addLayoutComponent(String constraint, Component comp) {
    }

    public void addLayoutComponent(Component comp, Object constraint) {
    }

    public void removeLayoutComponent(Component comp) {
    }

    public void invalidateLayout(Container target) {
    }

    public float getLayoutAlignmentX(Container target) {
        return 0.5f;
    }

    public float getLayoutAlignmentY(Container target) {
        return 0.5f;
    }
}
