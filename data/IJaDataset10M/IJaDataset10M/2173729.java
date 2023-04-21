package de.matthiasmann.twl;

/**
 * A widget which reorders it's child when they receive focus
 * @author Matthias Mann
 */
public class DesktopArea extends Widget {

    public DesktopArea() {
        setFocusKeyEnabled(false);
    }

    @Override
    protected void keyboardFocusChildChanged(Widget child) {
        super.keyboardFocusChildChanged(child);
        if (child != null) {
            int fromIdx = getChildIndex(child);
            assert fromIdx >= 0;
            int numChildren = getNumChildren();
            if (fromIdx < numChildren - 1) {
                moveChild(fromIdx, numChildren - 1);
            }
        }
    }

    @Override
    protected void layout() {
        restrictChildrenToInnerArea();
    }

    @Override
    protected void applyTheme(ThemeInfo themeInfo) {
        super.applyTheme(themeInfo);
        invalidateLayout();
    }

    protected void restrictChildrenToInnerArea() {
        final int top = getInnerY();
        final int left = getInnerX();
        final int right = getInnerRight();
        final int bottom = getInnerBottom();
        final int width = Math.max(0, right - left);
        final int height = Math.max(0, bottom - top);
        for (int i = 0, n = getNumChildren(); i < n; i++) {
            Widget w = getChild(i);
            w.setSize(Math.min(Math.max(width, w.getMinWidth()), w.getWidth()), Math.min(Math.max(height, w.getMinHeight()), w.getHeight()));
            w.setPosition(Math.max(left, Math.min(right - w.getWidth(), w.getX())), Math.max(top, Math.min(bottom - w.getHeight(), w.getY())));
        }
    }
}
