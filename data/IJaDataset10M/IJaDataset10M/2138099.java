package de.enough.polish.ui.containerviews;

import de.enough.polish.android.lcdui.Graphics;
import de.enough.polish.ui.ClippingRegion;
import de.enough.polish.ui.Container;
import de.enough.polish.ui.ContainerView;
import de.enough.polish.ui.IconItem;
import de.enough.polish.ui.Item;
import de.enough.polish.ui.Screen;
import de.enough.polish.ui.Style;

/**
 * <p>Shows  the available items of a Container in a horizontal list.</p>
 * <p>Apply this view by specifying "view-type: horizontal;" in your polish.css file.</p>
 *
 * <p>Copyright Enough Software 2007 - 2009</p>
 * @author Robert Virkus, j2mepolish@enough.de
 */
public class HorizontalContainerView extends ContainerView {

    protected int targetXOffset;

    private boolean allowRoundTrip;

    private boolean isExpandRightLayout;

    private boolean isClippingRequired;

    private boolean isPointerPressedHandled;

    private int pointerPressedX;

    private int completeContentWidth;

    /**
	 * Creates a new view
	 */
    public HorizontalContainerView() {
        super();
        this.allowsAutoTraversal = false;
        this.isHorizontal = true;
        this.isVertical = false;
    }

    public void animate(long currentTime, ClippingRegion repaintRegion) {
        super.animate(currentTime, repaintRegion);
        int target = this.targetXOffset;
        int current = this.xOffset;
        if (target != current) {
            int diff = Math.abs(target - current);
            int delta = diff / 3;
            if (delta < 2) {
                delta = 2;
            }
            if (target < current) {
                current -= delta;
                if (current < target) {
                    current = target;
                }
            } else {
                current += delta;
                if (current > target) {
                    current = target;
                }
            }
            this.xOffset = current;
            addFullRepaintRegion(this.parentItem, repaintRegion);
        }
    }

    protected void initContent(Item parentItm, int firstLineWidth, int availWidth, int availHeight) {
        Container parent = (Container) parentItm;
        int selectedItemIndex = parent.getFocusedIndex();
        int maxHeight = 0;
        int completeWidth = 0;
        Item[] items = parent.getItems();
        for (int i = 0; i < items.length; i++) {
            Item item = items[i];
            int itemHeight = item.getItemHeight(availWidth, availWidth, availHeight);
            int itemWidth = item.itemWidth;
            if (itemHeight > maxHeight) {
                maxHeight = itemHeight;
            }
            boolean isLast = i == items.length - 1;
            if (isLast && item.isLayoutRight() && (completeWidth + item.itemWidth < availWidth)) {
                completeWidth = availWidth - item.itemWidth;
            }
            int startX = completeWidth;
            item.relativeX = completeWidth;
            item.relativeY = 0;
            completeWidth += itemWidth + (isLast ? 0 : this.paddingHorizontal);
            if (i == selectedItemIndex) {
                if (startX + this.xOffset < 0) {
                    this.targetXOffset = -startX;
                } else if (completeWidth + this.targetXOffset > availWidth) {
                    this.targetXOffset = availWidth - completeWidth;
                }
                this.focusedItem = item;
            }
            if (item.appearanceMode != Item.PLAIN) {
                this.appearanceMode = Item.INTERACTIVE;
            }
        }
        this.contentHeight = maxHeight;
        if (completeWidth > availWidth) {
            this.isClippingRequired = true;
            this.contentWidth = availWidth;
        } else {
            this.isClippingRequired = false;
            this.contentWidth = completeWidth;
        }
        this.completeContentWidth = completeWidth;
        if (((parent.getLayout() & Item.LAYOUT_RIGHT) == Item.LAYOUT_RIGHT) && ((parent.getLayout() & Item.LAYOUT_EXPAND) == Item.LAYOUT_EXPAND)) {
            this.isExpandRightLayout = true;
        } else {
            this.isExpandRightLayout = false;
        }
    }

    public Style focusItem(int focIndex, Item item, int direction, Style focStyle) {
        if (this.isClippingRequired && item != null) {
            if (this.targetXOffset + item.relativeX < 0) {
                this.targetXOffset = -item.relativeX;
            } else if (this.targetXOffset + item.relativeX + item.itemWidth > this.contentWidth) {
                this.targetXOffset = this.contentWidth - item.relativeX - item.itemWidth;
            }
        }
        return super.focusItem(focIndex, item, direction, focStyle);
    }

    protected void setStyle(Style style) {
        super.setStyle(style);
    }

    protected void paintContent(Container container, Item[] myItems, int x, int y, int leftBorder, int rightBorder, int clipX, int clipY, int clipWidth, int clipHeight, Graphics g) {
        if (this.isExpandRightLayout) {
            x = rightBorder - this.contentWidth;
        }
        if (this.isClippingRequired) {
            g.clipRect(x, y, this.contentWidth + 1, this.contentHeight + 1);
        }
        x += this.xOffset;
        super.paintContent(container, myItems, x, y, leftBorder, rightBorder, clipX, clipY, clipWidth, clipHeight, g);
        if (this.isClippingRequired) {
            g.setClip(clipX, clipY, clipWidth, clipHeight);
        }
    }

    public boolean handlePointerDragged(int x, int y) {
        if (this.isPointerPressedHandled && this.isClippingRequired) {
            int offset = this.targetXOffset + (x - this.pointerPressedX);
            if (offset + this.completeContentWidth < this.contentWidth) {
                offset = this.contentWidth - this.completeContentWidth;
            } else if (offset > 0) {
                offset = 0;
            }
            this.xOffset = offset;
            this.targetXOffset = offset;
            this.pointerPressedX = x;
            return true;
        }
        return super.handlePointerDragged(x, y);
    }

    public boolean handlePointerPressed(int x, int y) {
        if (this.isClippingRequired && this.parentContainer.isInItemArea(x, y)) {
            this.isPointerPressedHandled = true;
            this.pointerPressedX = x;
        }
        return super.handlePointerPressed(x, y);
    }
}
