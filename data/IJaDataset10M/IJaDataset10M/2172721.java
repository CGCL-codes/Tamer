package com.global360.sketchpadbpmn.graphic;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import com.global360.sketchpadbpmn.SketchpadDefaults;
import com.global360.sketchpadbpmn.documents.idmanager.BpmnId;
import com.global360.sketchpadbpmn.graphic.layout.PoolLayoutManager;
import com.global360.sketchpadbpmn.handle.AbstractHandle;
import com.global360.sketchpadbpmn.handle.BarHandle;
import com.global360.sketchpadbpmn.i18n.Messages;
import com.global360.sketchpadbpmn.utility.ColorMap;
import com.global360.sketchpadbpmn.utility.ExtendedGraphics2D;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class BPMNLaneGraphic extends BPMNSwimContainer implements Cloneable {

    private static final long serialVersionUID = 1L;

    private ArrayList<String> categories = new ArrayList<String>();

    public BPMNLaneGraphic(ContainerGraphic parent, BpmnId id) {
        super(parent, id);
        initialize((BPMNSwimContainer) parent);
    }

    @SuppressWarnings("unchecked")
    public BPMNLaneGraphic(BPMNLaneGraphic source) throws CloneNotSupportedException {
        super(source);
        this.categories = (ArrayList<String>) source.categories.clone();
        updateShapes();
    }

    public BPMNLaneGraphic clone() throws CloneNotSupportedException {
        return new BPMNLaneGraphic(this);
    }

    private void initialize(BPMNSwimContainer parent) {
        setName(getIdString());
        setBounds(parent.getInternalBounds());
        setHeight(SketchpadDefaults.getMinimumLaneSize());
        setOrientation(parent.getOrientation());
        this.setHighlightBackground(!getParentPool().isTopLevelPool());
    }

    public boolean equals(Object other) {
        if (other == this) return true;
        if ((other != null) && (other instanceof BPMNLaneGraphic)) {
            return this.equals((BPMNLaneGraphic) other);
        }
        return false;
    }

    public boolean equals(BPMNLaneGraphic other) {
        if (other == this) return true;
        if (other == null) return false;
        if (!super.equals(other)) return false;
        if (!this.categories.equals(other.categories)) return false;
        return true;
    }

    protected void updateShapes() {
        super.updateShapes();
    }

    public void updateName() {
    }

    public String toString() {
        String result = this.getName();
        result = this.getDumpString();
        return result;
    }

    public BPMNPoolGraphic getParentPool() {
        return (BPMNPoolGraphic) getParent();
    }

    public void setNameFont(Font font) {
        super.setNameFont(font);
        getLayout().arrangeLanes();
    }

    public void setOrientation(BPMNPoolOrientation orientation) {
        this.orientation = orientation;
    }

    @Override
    public boolean isBlackBox() {
        if (getParentPool() != null) return getParentPool().isBlackBox();
        return false;
    }

    public void displace(double deltaX, double deltaY) {
        log.debug("BPMNLaneGraphic.displace: laneId=" + this.getId() + " deltaX=" + deltaX + " deltaY=" + deltaY);
        double newX = this.getX() + deltaX;
        double newY = this.getY() + deltaY;
        this.setLocation(newX, newY);
        Iterator<SketchGraphic> i = getChildren().iterator();
        while (i.hasNext()) {
            SketchGraphic graphic = (SketchGraphic) i.next();
            newX = graphic.getX() + deltaX;
            newY = graphic.getY() + deltaY;
            graphic.setLocation(newX, newY);
        }
        if (true) return;
    }

    public void displaceContents(double deltaX, double deltaY) {
        log.debug("BPMNLaneGraphic.displaceContents: laneId=" + this.getId() + " deltaX=" + deltaX + " deltaY=" + deltaY);
        double newX = this.getX() + deltaX;
        double newY = this.getY() + deltaY;
        Iterator<SketchGraphic> i = getChildren().iterator();
        while (i.hasNext()) {
            SketchGraphic graphic = (SketchGraphic) i.next();
            newX = graphic.getX() + deltaX;
            newY = graphic.getY() + deltaY;
            graphic.setLocation(newX, newY);
        }
        if (true) return;
    }

    public ArrayList<AbstractHandle> makeHandles() {
        ArrayList<AbstractHandle> handles = new ArrayList<AbstractHandle>();
        if (this.getParentPool().getLastLane() != this) {
            double extra = this.getLayout().getBarHandleSize();
            BarHandle barHandle = null;
            if (this.getOrientation().equals(BPMNPoolOrientation.HORIZONTAL)) {
                barHandle = new BarHandle(AbstractHandle.HANDLE_S, this, this.getS());
                barHandle.setBounds(this.getX() + extra, this.getY() + this.getHeight() - extra, this.getWidth() - (2 * extra), (2 * extra) + 1);
                barHandle.setBounds(this.getX(), this.getY() + this.getHeight() - extra, this.getWidth() - (1.5 * extra), (1 * extra) + 1);
            } else {
                barHandle = new BarHandle(AbstractHandle.HANDLE_E, this, this.getE());
                barHandle.setBounds(this.getX() + this.getWidth() - extra, this.getY(), (1 * extra) + 1, this.getHeight() - (1.5 * extra));
            }
            handles.add(barHandle);
        }
        return handles;
    }

    /**
     * getGraphicTypeName
     *
     * @return String
     */
    public String getGraphicTypeName() {
        return Messages.getString("BPMNLaneGraphic.8");
    }

    /**
     * setCategories
     *
     * @param categories ArrayList
     */
    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    /**
     * getCategories
     *
     * @return ArrayList
     */
    public ArrayList<String> getCategories() {
        return this.categories;
    }

    public void addChild(SketchGraphic child) {
        if (child instanceof BPMNPoolGraphic) {
        } else {
            super.addChild(child);
            child.setLaneId(this.getId());
        }
    }

    public boolean isEmpty() {
        return this.getChildren().size() == 0;
    }

    public boolean isLabelVisible() {
        return getParent().getChildCount() > 1;
    }

    @Override
    public Color getCurrentLabelColor(boolean isForPrinting, ColorMap map) {
        if ("ID-30".equals(this.getIdString())) {
            log.debug("");
        }
        Color result = null;
        if (this.isSelected) {
            result = Palette.PALETTE_BUTTON_FILL;
        } else if (this.isUnderCursor()) {
            result = Palette.SWIM_CONTAINER_LABEL_UNDER_CURSOR_COLOR;
        } else {
            result = this.getCurrentFillColor(isForPrinting, map);
        }
        return result;
    }

    @Override
    public void drawLines(ExtendedGraphics2D g) {
        if ((getParent().getChildCount() > 1) && !isFirstChild()) {
            g.setPaint(getCurrentLineColor(g.isPrinting(), g.getColorMap()));
            g.setStroke(this.getStroke());
            Rectangle2D.Double bounds = getBounds();
            Line2D divider;
            if (isHorizontal()) {
                divider = new Line2D.Double(bounds.x, bounds.y, bounds.x + bounds.width - 2, bounds.y);
            } else {
                divider = new Line2D.Double(bounds.x, bounds.y, bounds.x, bounds.y + bounds.height - 2);
            }
            g.draw(divider);
        }
    }

    @Override
    public void drawFills(ExtendedGraphics2D g) {
        Rectangle2D.Double r;
        Color fillcolor = this.getCurrentFillColor(g.isPrinting(), g.getColorMap());
        if (fillcolor != null) {
            g.setPaint(fillcolor);
            r = this.internalBounds;
            r = new Rectangle2D.Double();
            r.x = this.internalBounds.x;
            r.y = this.internalBounds.y;
            if (isHorizontal()) {
                r.width = this.internalBounds.width;
                r.height = this.internalBounds.height + 1;
            } else {
                r.width = this.internalBounds.width + 1;
                r.height = this.internalBounds.height;
            }
            g.fill(r);
        }
        if (isLabelVisible()) {
            g.setPaint(getCurrentLabelColor(g.isPrinting(), g.getColorMap()));
            r = new Rectangle2D.Double();
            r.x = getLabelBounds().x;
            r.y = getLabelBounds().y;
            if (isHorizontal()) {
                r.width = getLabelBounds().width;
                r.height = getLabelBounds().height + 1;
            } else {
                r.width = getLabelBounds().width + 1;
                r.height = getLabelBounds().height;
            }
            g.fill(r);
        }
    }

    @Override
    public void drawSelf(ExtendedGraphics2D g) {
    }

    @Override
    public Color getCurrentFillColor(boolean isForPrinting, ColorMap map) {
        Color result = null;
        if (!isForPrinting && this.isHighlighted() && highlightBackground()) result = Palette.PA_SELECTED_BACKGROUND; else if (!isForPrinting && this.isBlackBox()) result = Palette.DEFAULT_FILL_COLOR; else {
            result = this.getFillColorRaw();
            if (!isForPrinting && result == null) {
                result = this.getParentPool().getFillColorRaw();
                if (result == null) result = getDefaultFillColor(false);
            }
        }
        if (map != null) {
            result = map.mapFillColor(this.getIdString(), result);
        }
        return result;
    }

    @Override
    public Rectangle2D.Double getMinimumBounds() {
        Rectangle2D.Double result = super.getMinimumBounds();
        double finalX = result.getX();
        double finalY = result.getY();
        double finalWidth = result.getWidth();
        double finalHeight = result.getHeight();
        for (SketchGraphic graphic : this.getChildren()) {
            if (graphic.getLeft() < finalX) finalX = graphic.getLeft();
            if (graphic.getTop() < finalY) finalY = graphic.getTop();
            double x = graphic.getRight() - this.getX();
            double y = graphic.getBottom() - this.getY();
            if (x > finalWidth) finalWidth = x;
            if (y > finalHeight) finalHeight = y;
        }
        double xExtra = (this.labelBounds.getWidth());
        double yExtra = (this.labelBounds.getHeight());
        if (this.isHorizontal()) yExtra = 0; else xExtra = 0;
        finalX -= xExtra;
        finalY -= yExtra;
        finalWidth += xExtra;
        finalHeight += yExtra;
        result.setRect(finalX, finalY, finalWidth, finalHeight);
        return result;
    }

    @Override
    protected PoolLayoutManager getLayout() {
        return this.getParentPool().getLayout();
    }
}
