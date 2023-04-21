package freemind.view.mindmapview;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import javax.swing.JComponent;
import freemind.modes.MindMapNode;

public abstract class NodeViewLayoutAdapter implements NodeViewLayout {

    protected final int LISTENER_VIEW_WIDTH = 10;

    protected Point location = new Point();

    private static Dimension minDimension;

    private NodeView view;

    private MindMapNode model;

    private int childCount;

    private JComponent content;

    private int vGap;

    private int spaceAround;

    public void addLayoutComponent(String arg0, Component arg1) {
    }

    public void removeLayoutComponent(Component arg0) {
    }

    public Dimension minimumLayoutSize(Container arg0) {
        if (minDimension == null) minDimension = new Dimension(0, 0);
        return minDimension;
    }

    public Dimension preferredLayoutSize(Container c) {
        if (!c.isValid()) {
            c.validate();
        }
        return c.getSize();
    }

    public void layoutContainer(Container c) {
        setUp(c);
        layout();
        shutDown();
    }

    protected abstract void layout();

    private void setUp(Container c) {
        final NodeView localView = (NodeView) c;
        localView.syncronizeAttributeView();
        final int localChildCount = localView.getComponentCount() - 1;
        for (int i = 0; i < localChildCount; i++) {
            localView.getComponent(i).validate();
        }
        this.view = localView;
        this.model = localView.getModel();
        this.childCount = localChildCount;
        this.content = localView.getContent();
        if (getModel().isVisible()) {
            this.vGap = getView().getVGap();
        } else {
            this.vGap = getView().getVisibleParentView().getVGap();
        }
        spaceAround = view.getMap().getZoomed(NodeView.SPACE_AROUND);
    }

    private void shutDown() {
        this.view = null;
        this.model = null;
        this.content = null;
        this.childCount = 0;
        this.vGap = 0;
        this.spaceAround = 0;
    }

    /**
     * @return Returns the view.
     */
    protected NodeView getView() {
        return view;
    }

    /**
     * @return Returns the model.
     */
    protected MindMapNode getModel() {
        return model;
    }

    /**
     * @return Returns the childCount.
     */
    protected int getChildCount() {
        return childCount;
    }

    /**
     * @return Returns the content.
     */
    protected JComponent getContent() {
        return content;
    }

    protected int getChildContentHeight(boolean isLeft) {
        final int childCount = getChildCount();
        if (childCount == 0) {
            return 0;
        }
        int height = 0;
        int count = 0;
        for (int i = 0; i < childCount; i++) {
            final NodeView child = (NodeView) getView().getComponent(i);
            if (child.isLeft() == isLeft) {
                final int additionalCloudHeigth = child.getAdditionalCloudHeigth();
                final int contentHeight = child.getContent().getHeight();
                height += contentHeight + additionalCloudHeigth;
                count++;
            }
        }
        return height + vGap * (count - 1);
    }

    protected int getChildVerticalShift(boolean isLeft) {
        if (getChildCount() == 0) return 0;
        int shift = 0;
        for (int i = 0; i < getChildCount(); i++) {
            final NodeView child = (NodeView) getView().getComponent(i);
            if (child.isLeft() == isLeft) {
                final int childShift = child.getShift();
                if (childShift < 0 || i == 0) shift += childShift;
                shift -= (child.getContent().getY() - getSpaceAround());
            }
        }
        return shift - getSpaceAround();
    }

    protected int getChildHorizontalShift() {
        if (getChildCount() == 0) return 0;
        int shift = 0;
        for (int i = 0; i < getChildCount(); i++) {
            NodeView child = (NodeView) getView().getComponent(i);
            int shiftCandidate;
            if (child.isLeft()) {
                shiftCandidate = -child.getContent().getX() - child.getContent().getWidth();
                if (child.isContentVisible()) {
                    shiftCandidate -= child.getHGap() + child.getAdditionalCloudHeigth() / 2;
                }
            } else {
                shiftCandidate = -child.getContent().getX();
                if (child.isContentVisible()) {
                    shiftCandidate += child.getHGap();
                }
            }
            shift = Math.min(shift, shiftCandidate);
        }
        return shift;
    }

    protected void placeRightChildren(int childVerticalShift) {
        final int baseX = getContent().getX() + getContent().getWidth();
        int y = getContent().getY() + childVerticalShift;
        int right = baseX + getSpaceAround();
        ;
        NodeView child = null;
        for (int i = 0; i < getChildCount(); i++) {
            final NodeView component = (NodeView) getView().getComponent(i);
            if (component.isLeft()) {
                continue;
            }
            child = component;
            final int additionalCloudHeigth = child.getAdditionalCloudHeigth() / 2;
            y += additionalCloudHeigth;
            int shiftY = child.getShift();
            final int childHGap = child.getContent().isVisible() ? child.getHGap() : 0;
            int x = baseX + childHGap - child.getContent().getX();
            if (shiftY < 0) {
                child.setLocation(x, y);
                y -= shiftY;
            } else {
                y += shiftY;
                child.setLocation(x, y);
            }
            y += child.getHeight() - 2 * getSpaceAround() + getVGap() + additionalCloudHeigth;
            right = Math.max(right, x + child.getWidth() + additionalCloudHeigth);
        }
        final int bottom = getContent().getY() + getContent().getHeight() + getSpaceAround();
        if (child != null) {
            getView().setSize(right, Math.max(bottom, child.getY() + child.getHeight() + child.getAdditionalCloudHeigth() / 2));
        } else {
            getView().setSize(right, bottom);
        }
    }

    protected void placeLeftChildren(int childVerticalShift) {
        final int baseX = getContent().getX();
        int y = getContent().getY() + childVerticalShift;
        int right = baseX + getContent().getWidth() + getSpaceAround();
        NodeView child = null;
        for (int i = 0; i < getChildCount(); i++) {
            final NodeView component = (NodeView) getView().getComponent(i);
            if (!component.isLeft()) {
                continue;
            }
            child = component;
            final int additionalCloudHeigth = child.getAdditionalCloudHeigth() / 2;
            y += additionalCloudHeigth;
            int shiftY = child.getShift();
            final int childHGap = child.getContent().isVisible() ? child.getHGap() : 0;
            int x = baseX - childHGap - child.getContent().getX() - child.getContent().getWidth();
            if (shiftY < 0) {
                child.setLocation(x, y);
                y -= shiftY;
            } else {
                y += shiftY;
                child.setLocation(x, y);
            }
            y += child.getHeight() - 2 * getSpaceAround() + getVGap() + additionalCloudHeigth;
            right = Math.max(right, x + child.getWidth());
        }
        final int bottom = getContent().getY() + getContent().getHeight() + getSpaceAround();
        if (child != null) {
            getView().setSize(right, Math.max(bottom, child.getY() + child.getHeight() + child.getAdditionalCloudHeigth() / 2));
        } else {
            getView().setSize(right, bottom);
        }
    }

    /**
     * @return Returns the vGap.
     */
    int getVGap() {
        return vGap;
    }

    /**
     * @return Returns the spaceAround.
     */
    int getSpaceAround() {
        return spaceAround;
    }
}
