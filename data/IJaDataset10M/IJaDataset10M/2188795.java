package org.jmove.zui;

import edu.umd.cs.piccolo.PNode;
import edu.umd.cs.piccolo.nodes.PPath;
import edu.umd.cs.piccolo.nodes.PText;
import edu.umd.cs.piccolo.util.PBounds;
import org.jmove.core.Thing;
import org.jmove.java.model.Item;
import org.jmove.java.model.Type;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ItemNode extends PPath {

    protected static Font DEFAULT_TITLE_FONT = new Font("Helvetica", Font.PLAIN, 14);

    protected Thing myItem;

    protected PNode myControlNode;

    PText myTitleNode;

    PBounds myCachedChildBounds = new PBounds();

    PBounds myComparisonBounds = new PBounds();

    private boolean isSelected;

    public ItemNode() {
        super();
        myControlNode = new PNode();
        myControlNode.setPickable(false);
        myTitleNode = new PText("foo");
        myTitleNode.setFont(DEFAULT_TITLE_FONT);
        myTitleNode.setPickable(false);
        myTitleNode.setConstrainHeightToTextHeight(true);
        myTitleNode.setConstrainWidthToTextWidth(true);
        Rectangle2D.Float rect = new Rectangle2D.Float(0, 0, 20, 20);
        setPathTo(rect);
        setPaint(new Color(255, 255, 200, 64));
        setSelected(false);
        addChild(myControlNode);
        addChild(myTitleNode);
    }

    public ItemNode(Thing item) {
        this();
        setItem(item);
    }

    public void setItem(Thing item) {
        myItem = item;
        if (item != null) {
            String name = getTitle();
            myTitleNode.setText(name);
            if (item instanceof Type) {
                setPaint(new Color(255, 200, 200, 64));
            }
            addAttribute("tooltip", name);
            myTitleNode.addAttribute("tooltip", name);
        }
        recalculateLayout();
    }

    protected String getTitle() {
        return (myItem instanceof Item) ? ((Item) myItem).name() : myItem.id();
    }

    public Thing getItem() {
        return myItem;
    }

    public List getItemChilds() {
        List childs = new ArrayList();
        for (int i = 0; i < getChildrenCount(); i++) {
            PNode node = getChild(i);
            if (node instanceof ItemNode) {
                childs.add(node);
            }
        }
        return childs;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean select) {
        isSelected = select;
        if (isSelected) {
            setStroke(new BasicStroke(2.0f));
            setStrokePaint(Color.BLACK);
        } else {
            setStroke(new BasicStroke(0.0f));
            setStrokePaint(Color.GRAY);
        }
    }

    /**
	 * TODO: should integrate with piccolo layout (invalidation) mechanism
	 */
    public void recalculateLayout() {
        layoutChildren();
        PBounds bounds = getUnionOfChildrenBounds(null);
        setBounds(getX(), getY(), bounds.getMaxX() + 4, bounds.getMaxY() + 4);
    }

    protected void internalUpdateBounds(double x, double y, double width, double height) {
        PBounds bounds = getUnionOfChildrenBounds(null);
        setBounds(x, y, bounds.getMaxX() + 4, bounds.getMaxY() + 4);
        super.internalUpdateBounds(x, y, bounds.getMaxX() + 4, bounds.getMaxY() + 4);
    }

    protected void layoutChildren() {
        float yOffset = 2.0f;
        float xOffset = 2.0f;
        double maxHeight = 0.0;
        List childs = new ArrayList();
        for (Iterator iterator = getChildrenIterator(); iterator.hasNext(); ) {
            PNode node = (PNode) iterator.next();
            if (node.getVisible()) childs.add(node);
        }
        int count = childs.size();
        int columns = (count > 0) ? (int) Math.sqrt((double) count) : 1;
        for (int i = 0; i < count; i++) {
            PNode child = (PNode) childs.get(i);
            if (child == myControlNode) {
                child.setOffset(xOffset, yOffset);
                if (child.getFullBoundsReference().getWidth() > 0.0f) {
                    xOffset += child.getFullBoundsReference().getWidth() + 2.0f;
                }
            } else if (child == myTitleNode) {
                child.setOffset(xOffset, yOffset);
                yOffset += child.getFullBoundsReference().getHeight() + 2.0f;
                xOffset = 10.0f;
            } else {
                child.setScale(1.0);
                child.setOffset(xOffset, yOffset);
                double height = child.getFullBoundsReference().getHeight();
                maxHeight = (height > maxHeight) ? height : maxHeight;
                if (i % columns != 1) {
                    xOffset += child.getFullBoundsReference().getWidth() + 2.0f;
                } else {
                    yOffset += maxHeight + 2.0f;
                    maxHeight = 0.0;
                    xOffset = 10.0f;
                }
            }
        }
    }

    protected void _layoutChildren() {
        float yOffset = 2.0f;
        for (int i = 0; i < getChildrenCount(); i++) {
            PNode child = getChild(i);
            if (child != myTitleNode) {
                child.setScale(0.8);
                child.setOffset(10.0f, yOffset);
            } else {
                child.setOffset(2.0f, yOffset);
            }
            yOffset += child.getFullBoundsReference().getHeight() + 2.0f;
        }
    }

    /**
	 * This is a crucial step.  We have to override this method to invalidate the paint each time the bounds are changed so
	 * we repaint the correct region
	 */
    public boolean validateFullBounds() {
        myComparisonBounds = getUnionOfChildrenBounds(myComparisonBounds);
        if (!myCachedChildBounds.equals(myComparisonBounds)) {
            setPaintInvalid(true);
        }
        return super.validateFullBounds();
    }
}
