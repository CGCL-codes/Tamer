package de.matthiasmann.twl;

/**
 *
 * @author Matthias Mann
 */
public class BoxLayout extends Widget {

    public enum Direction {

        HORIZONTAL, VERTICAL
    }

    ;

    private Direction direction;

    private int spacing;

    private boolean scroll;

    private Alignment alignment = Alignment.TOP;

    public BoxLayout() {
        this(Direction.HORIZONTAL);
    }

    public BoxLayout(Direction direction) {
        this.direction = direction;
    }

    public int getSpacing() {
        return spacing;
    }

    public void setSpacing(int spacing) {
        if (this.spacing != spacing) {
            this.spacing = spacing;
            invalidateLayout();
        }
    }

    public boolean isScroll() {
        return scroll;
    }

    public void setScroll(boolean scroll) {
        if (this.scroll != scroll) {
            this.scroll = scroll;
            invalidateLayout();
        }
    }

    public Alignment getAlignment() {
        return alignment;
    }

    public void setAlignment(Alignment alignment) {
        if (alignment == null) {
            throw new NullPointerException("alignment");
        }
        if (this.alignment != alignment) {
            this.alignment = alignment;
            invalidateLayout();
        }
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        if (direction == null) {
            throw new NullPointerException("direction");
        }
        if (this.direction != direction) {
            this.direction = direction;
            invalidateLayout();
        }
    }

    @Override
    public int getMinWidth() {
        int minWidth = (direction == Direction.HORIZONTAL) ? computeMinWidthHorizontal(this, spacing) : computeMinWidthVertical(this);
        return Math.max(super.getMinWidth(), minWidth + getBorderHorizontal());
    }

    @Override
    public int getMinHeight() {
        int minHeight = (direction == Direction.HORIZONTAL) ? computeMinHeightHorizontal(this) : computeMinHeightVertical(this, spacing);
        return Math.max(super.getMinHeight(), minHeight + getBorderVertical());
    }

    @Override
    public int getPreferredInnerWidth() {
        return (direction == Direction.HORIZONTAL) ? computePreferredWidthHorizontal(this, spacing) : computePreferredWidthVertical(this);
    }

    @Override
    public int getPreferredInnerHeight() {
        return (direction == Direction.HORIZONTAL) ? computePreferredHeightHorizontal(this) : computePreferredHeightVertical(this, spacing);
    }

    @Override
    protected void applyTheme(ThemeInfo themeInfo) {
        super.applyTheme(themeInfo);
        setSpacing(themeInfo.getParameter("spacing", 0));
        setAlignment(themeInfo.getParameter("alignment", Alignment.TOP));
    }

    public static int computeMinWidthHorizontal(Widget container, int spacing) {
        final int n = container.getNumChildren();
        int minWidth = Math.max(0, n - 1) * spacing;
        for (int i = 0; i < n; i++) {
            minWidth += container.getChild(i).getMinWidth();
        }
        return minWidth;
    }

    public static int computeMinHeightHorizontal(Widget container) {
        final int n = container.getNumChildren();
        int minHeight = 0;
        for (int i = 0; i < n; i++) {
            minHeight = Math.max(minHeight, container.getChild(i).getMinHeight());
        }
        return minHeight;
    }

    public static int computePreferredWidthHorizontal(Widget container, int spacing) {
        final int n = container.getNumChildren();
        int prefWidth = Math.max(0, n - 1) * spacing;
        for (int i = 0; i < n; i++) {
            prefWidth += getPrefChildWidth(container.getChild(i));
        }
        return prefWidth;
    }

    public static int computePreferredHeightHorizontal(Widget container) {
        final int n = container.getNumChildren();
        int prefHeight = 0;
        for (int i = 0; i < n; i++) {
            prefHeight = Math.max(prefHeight, getPrefChildHeight(container.getChild(i)));
        }
        return prefHeight;
    }

    public static int computeMinWidthVertical(Widget container) {
        final int n = container.getNumChildren();
        int minWidth = 0;
        for (int i = 0; i < n; i++) {
            minWidth = Math.max(minWidth, container.getChild(i).getMinWidth());
        }
        return minWidth;
    }

    public static int computeMinHeightVertical(Widget container, int spacing) {
        final int n = container.getNumChildren();
        int minHeight = Math.max(0, n - 1) * spacing;
        for (int i = 0; i < n; i++) {
            minHeight += container.getChild(i).getMinHeight();
        }
        return minHeight;
    }

    public static int computePreferredWidthVertical(Widget container) {
        final int n = container.getNumChildren();
        int prefWidth = 0;
        for (int i = 0; i < n; i++) {
            prefWidth = Math.max(prefWidth, getPrefChildWidth(container.getChild(i)));
        }
        return prefWidth;
    }

    public static int computePreferredHeightVertical(Widget container, int spacing) {
        final int n = container.getNumChildren();
        int prefHeight = Math.max(0, n - 1) * spacing;
        for (int i = 0; i < n; i++) {
            prefHeight += getPrefChildHeight(container.getChild(i));
        }
        return prefHeight;
    }

    public static void layoutHorizontal(Widget container, int spacing, Alignment alignment, boolean scroll) {
        final int numChildren = container.getNumChildren();
        final int height = container.getInnerHeight();
        int x = container.getInnerX();
        int y = container.getInnerY();
        if (scroll) {
            int width = computePreferredWidthHorizontal(container, spacing);
            if (width > container.getInnerWidth()) {
                x -= width - container.getInnerWidth();
            }
        }
        for (int idx = 0; idx < numChildren; idx++) {
            Widget child = container.getChild(idx);
            int childWidth = getPrefChildWidth(child);
            int childHeight = (alignment == Alignment.FILL) ? height : getPrefChildHeight(child);
            int yoff = (height - childHeight) * alignment.vpos / 2;
            child.setSize(childWidth, childHeight);
            child.setPosition(x, y + yoff);
            x += childWidth + spacing;
        }
    }

    public static void layoutVertical(Widget container, int spacing, Alignment alignment, boolean scroll) {
        final int numChildren = container.getNumChildren();
        final int width = container.getInnerWidth();
        int x = container.getInnerX();
        int y = container.getInnerY();
        if (scroll) {
            int height = computePreferredHeightVertical(container, spacing);
            if (height > container.getInnerHeight()) {
                x -= height - container.getInnerHeight();
            }
        }
        for (int idx = 0; idx < numChildren; idx++) {
            Widget child = container.getChild(idx);
            int childWidth = (alignment == Alignment.FILL) ? width : getPrefChildWidth(child);
            int childHeight = getPrefChildHeight(child);
            int xoff = (width - childWidth) * alignment.hpos / 2;
            child.setSize(childWidth, childHeight);
            child.setPosition(x + xoff, y);
            y += childHeight + spacing;
        }
    }

    @Override
    protected void layout() {
        if (getNumChildren() > 0) {
            if (direction == Direction.HORIZONTAL) {
                layoutHorizontal(this, spacing, alignment, scroll);
            } else {
                layoutVertical(this, spacing, alignment, scroll);
            }
        }
    }

    private static int getPrefChildWidth(Widget child) {
        return computeSize(child.getMinWidth(), child.getPreferredWidth(), child.getMaxWidth());
    }

    private static int getPrefChildHeight(Widget child) {
        return computeSize(child.getMinHeight(), child.getPreferredHeight(), child.getMaxHeight());
    }
}
