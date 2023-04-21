package tufts.vue;

import tufts.Util;
import static tufts.Util.*;
import java.util.*;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.AffineTransform;

public class LWGroup extends LWContainer {

    private static final boolean FancyGroups = true;

    private boolean isForSelection = false;

    public LWGroup() {
        if (!FancyGroups) disablePropertyTypes(KeyType.STYLE);
        disableProperty(LWKey.Font);
        disableProperty(LWKey.FontName);
        disableProperty(LWKey.FontSize);
        disableProperty(LWKey.FontStyle);
        disableProperty(LWKey.TextColor);
    }

    @Override
    public boolean supportsChildren() {
        return FancyGroups;
    }

    @Override
    public boolean supportsUserResize() {
        return false;
    }

    @Override
    public boolean supportsUserLabel() {
        return false;
    }

    @Override
    public boolean isOrphan() {
        return isForSelection || super.isOrphan();
    }

    /**
     * For the viewer selection code -- we're mainly interested
     * in the ability of a group to move all of it's children
     * with it.
     */
    void useSelection(LWSelection selection) {
        if (!isForSelection) Util.printStackTrace("NOT FOR SELECTION!: " + this);
        Rectangle2D bounds = selection.getBounds();
        if (bounds != null) {
            super.setSize((float) bounds.getWidth(), (float) bounds.getHeight());
            super.setLocation((float) bounds.getX(), (float) bounds.getY());
        } else {
            System.err.println("null bounds in LWGroup.useSelection");
        }
        super.mChildren = selection;
    }

    /**
     * Create a new LWGroup, reparenting all the LWComponents
     * in the selection to the new group.
     */
    static LWGroup create(LWSelection selection) {
        final LWGroup group = new LWGroup();
        group.mChildren = new ArrayList(selection.size());
        group.createFromNonLinks(selection);
        if (DEBUG.CONTAINMENT) System.out.println("CREATED: " + group);
        return group;
    }

    /**
     * Establish a newly created group from the memebrs of the given selection.
     * Will initially ignore any links (for a variety of reasons) and leave
     * it up each link cleanup task to decide if it should add itself to the group.
     * This also makes sure to maintain the relative z-order of the imported nodes.
     */
    private void createFromNonLinks(Collection<LWComponent> selection) {
        final Collection<LWComponent> reparenting;
        if (true) {
            reparenting = new HashSet<LWComponent>() {

                @Override
                public boolean add(LWComponent c) {
                    if (c instanceof LWLink) {
                        return false;
                    } else return super.add(c);
                }
            };
            reparenting.addAll(selection);
            setShapeFromContents(reparenting);
        } else {
            reparenting = new HashSet();
            final Collection<LWComponent> allUniqueDescendents = new HashSet();
            for (LWComponent c : selection) {
                reparenting.add(c);
                allUniqueDescendents.add(c);
                c.getAllDescendents(ChildKind.PROPER, allUniqueDescendents);
            }
            final HashSet uniqueLinks = new HashSet();
            for (LWComponent c : allUniqueDescendents) {
                if (DEBUG.PARENTING) out("ALL UNIQUE " + c);
                for (LWLink l : c.getLinks()) {
                    boolean bothEndsInPlay = !uniqueLinks.add(l);
                    if (DEBUG.PARENTING) out("SEEING LINK " + l + " IN-PLAY=" + bothEndsInPlay);
                    if (bothEndsInPlay && !(c.getParent() instanceof LWGroup)) {
                        if (DEBUG.PARENTING) out("GRABBING " + l + " (both ends in group)");
                        reparenting.add(l);
                    }
                }
            }
            setShapeFromContents(reparenting);
        }
        addChildren(sort(reparenting, LWContainer.ReverseOrder));
        normalize(false);
    }

    public static LWGroup createTemporary(java.util.ArrayList selection) {
        LWGroup group = new LWGroup();
        group.isForSelection = true;
        if (DEBUG.Enabled) group.setLabel("<=SELECTION=>");
        group.mChildren = (java.util.ArrayList) selection.clone();
        group.setShapeFromChildren();
        if (DEBUG.CONTAINMENT) System.out.println("LWGroup.createTemporary " + group);
        return group;
    }

    private void setShapeFromContents(Iterable<LWComponent> contents) {
        final Rectangle2D.Float bounds = LWMap.getLocalBorderBounds(contents);
        super.setSize(bounds.width, bounds.height);
        super.setLocation(bounds.x, bounds.y);
    }

    private void setShapeFromChildren() {
        setShapeFromContents(getChildren());
    }

    protected void normalize() {
        normalize(true);
    }

    /**
     * Normalize the group.
     *
     * The process of normalization is to expand/contract the group to fit the new
     * bounds of our contents if they've moved/resized, and then update the local
     * coordinates of our members to reflect their offset with the new group bounds, if
     * the upper left hand corner of the group has changed (it's 0,0 position in it's
     * parent has changed).  The point is to update the local child locations relative
     * to any new group position such that there is no net change to their absolute
     * position on the map.  So: if a group member has simply moved within the group
     * without moving beyond any current edge of the group, there is nothing to do.  If
     * a group member has moved below or to the right if our current bounds, the group
     * simply needs to increase it's size.  Howver, if a member has moved above or to
     * the left of our current bounds, the group needs to change it's location (as well
     * as size), then "normalize" all the children: translate them down and to the right
     * by the exact amount the group has moved up and to the left.
     *
     * @param reshape - if true, allow reshaping of the group.  This is the standard
     * case, except during group creation, where in order to bootstrap ourseleves,
     * the group separately estalishes an initial bounds, and then updates the child
     * locations.
     *
     */
    private void normalize(boolean reshape) {
        if (DEBUG.WORK) {
            System.out.println();
            out("NORMALIZING" + (reshape ? "" : " W/OUT RESHAPE FOR INIT"));
        }
        final Rectangle2D.Float curBounds = getLocalBounds();
        final Rectangle2D.Float preBounds = getPreNormalBounds();
        final float dx = preBounds.x;
        final float dy = preBounds.y;
        if (DEBUG.WORK) {
            final float dw = preBounds.width - curBounds.width;
            final float dh = preBounds.height - curBounds.height;
            out("curBounds: " + Util.out(curBounds));
            out("preBounds: " + Util.out(preBounds) + String.format("; dx=%+.2f dy=%+.2f dw=%+.1f dh=%+.1f", dx, dy, dw, dh));
        }
        if (dx != 0.0f || dy != 0.0f) {
            if (reshape) super.setLocation(getX() + dx, getY() + dy, this, false);
            if (DEBUG.WORK) out("normalizing relative children: dx=" + dx + " dy=" + dy);
            for (LWComponent c : getChildren()) {
                c.translate(-dx, -dy);
                if (c instanceof LWLink) {
                    ((LWLink) c).layout();
                }
            }
        }
        if (DEBUG.WORK) {
            out(String.format("curShape %f,%f %fx%f", getX(), getY(), super.width, super.height));
            out(String.format("newShape %f,%f %fx%f", dx, dy, preBounds.width, preBounds.height));
        }
        if (reshape) super.setSize(preBounds.width, preBounds.height);
        if (DEBUG.WORK) out("NORMALIZED");
    }

    /** @return the bounds of of contents prior to normalization: these bounds 
     * are likely to be different than our current bounds: the process of normalization
     * ensures that the group bounds eventually match these bounds.  The bounds
     * are in the parent-local coordinate space (the groups).  So, for instance, if
     * the upper left most member of a group moves up 10 pixels, the pre-normalized
     * upper left hand corner bounds of the contents will be at 0,-10, relative to
     * the current group (the parent), and the pre-normal height will be 10 pixels
     * greater than the current height of the group.
     */
    private Rectangle2D.Float getPreNormalBounds() {
        if (numChildren() < 2) return LWMap.EmptyBounds; else return LWMap.getLocalBorderBounds(getChildren());
    }

    @Override
    protected void removeChildrenFromModel() {
    }

    private class DisperseOrNormalize implements Runnable {

        final Object srcMsg;

        DisperseOrNormalize(Object srcMsg) {
            this.srcMsg = srcMsg;
        }

        public void run() {
            if (isDeleted()) return;
            if (numChildren() < 2) {
                if (DEBUG.PARENTING || DEBUG.CONTAINMENT) out("AUTO-DISPERSING on child count " + numChildren());
                disperse();
            } else {
                normalize();
            }
        }

        public String toString() {
            return getClass().getName() + "@" + Integer.toHexString(hashCode()) + "[" + srcMsg + "]";
        }
    }

    private void requestCleanup(Object srcMsg) {
        final UndoManager um = getUndoManager();
        if (um != null && !um.isUndoing() && !um.hasLastTask(this)) {
            if (DEBUG.CONTAINMENT) out("addLastTask/DisperseOrNormalize; on " + srcMsg);
            um.addLastTask(this, new DisperseOrNormalize(srcMsg));
        }
    }

    @Override
    protected void removeChildren(Iterable<LWComponent> iterable, Object context) {
        super.removeChildren(iterable, context);
        requestCleanup("removeChildren");
    }

    @Override
    public void addChildren(Collection<? extends LWComponent> iterable, Object context) {
        super.addChildren(iterable, context);
        requestCleanup("addChildren");
    }

    /**
     * Remove all children and re-parent to this group's parent,
     * then remove this now empty group object from the parent.
     */
    public void disperse() {
        if (DEBUG.PARENTING || DEBUG.CONTAINMENT) System.out.println("DISPERSING: " + this);
        if (hasChildren()) {
            final LWContainer newParent = getParentOfType(LWContainer.class);
            final List tmpChildren = new ArrayList(mChildren);
            newParent.addChildren(tmpChildren);
        }
        getParent().deleteChildPermanently(this);
    }

    @Override
    public void setMapLocation(double x, double y) {
        if (isForSelection) {
            final double dx = x - getX();
            final double dy = y - getY();
            translateSelection(dx, dy);
            super.setLocation((float) x, (float) y);
        } else super.setMapLocation(x, y);
    }

    private void translateSelection(double dx, double dy) {
        for (LWComponent c : getChildren()) {
            if (c.isSelected() && c.isAncestorSelected()) continue; else translateOnMap(c, dx, dy);
        }
    }

    /** translate across the map in absolute map coordinates -- special use by LWGroup */
    private static void translateOnMap(LWComponent c, double dx, double dy) {
        final double scale = c.getParent().getMapScale();
        if (scale != 1.0) {
            dx /= scale;
            dy /= scale;
        }
        c.translate((float) dx, (float) dy);
    }

    @Override
    public void setLocation(float x, float y) {
        if (isForSelection) Util.printStackTrace("setLocation on selection group " + x + "," + y + " " + this); else super.setLocation(x, y);
    }

    private boolean linksAreTranslatingWithUs = false;

    @Override
    protected void notifyMapLocationChanged(LWComponent src, double mdx, double mdy) {
        if (!isForSelection) {
            try {
                linksAreTranslatingWithUs = true;
                super.notifyMapLocationChanged(src, mdx, mdy);
            } finally {
                linksAreTranslatingWithUs = false;
            }
        }
    }

    @Override
    void broadcastChildEvent(LWCEvent e) {
        if (mXMLRestoreUnderway) {
            return;
        }
        updateConnectedLinks(null);
        super.broadcastChildEvent(e);
        if (e.hasOldValue() && !linksAreTranslatingWithUs) {
            requestCleanup(e);
        }
    }

    @Override
    public void notifyHierarchyChanged() {
        requestCleanup("hierarchyChanged");
        super.notifyHierarchyChanged();
    }

    /** Overridden in to handle special selection LWGroup: if is asked to draw itself into another context (e.g., on an image),
     * it won't bother to transform locally -- just draw the children as they are.
     */
    @Override
    public void draw(DrawContext dc) {
        if (isForSelection) drawChildren(dc); else super.draw(dc);
    }

    private final Color getPathwayColor() {
        final LWPathway exclusive = getExclusiveVisiblePathway();
        if (exclusive != null) return exclusive.getColor(); else if (inPathway(VUE.getActivePathway()) && VUE.getActivePathway().isDrawn()) return VUE.getActivePathway().getColor(); else return null;
    }

    private static final boolean TrackPathwayColor = false;

    @Override
    public Color getRenderFillColor(DrawContext dc) {
        if (TrackPathwayColor && dc != null && dc.drawPathways() && isTransparent()) {
            return getPathwayColor();
        }
        return super.getRenderFillColor(dc);
    }

    @Override
    protected void drawImpl(DrawContext dc) {
        if (DEBUG.CONTAINMENT && !isForSelection) {
            java.awt.Shape shape = getZeroShape();
            dc.g.setColor(new java.awt.Color(64, 64, 64, 64));
            dc.g.fill(shape);
            dc.g.setColor(java.awt.Color.blue);
            dc.setAbsoluteStroke(1.0);
            dc.g.draw(shape);
        }
        final Color fill;
        if (TrackPathwayColor) {
            if (dc.drawPathways() && dc.focal != this && isTransparent()) {
                final Color c = getPathwayColor();
                if (c != null) fill = c;
            } else fill = getFillColor();
        } else {
            fill = getFillColor();
        }
        if (fill != null && fill.getAlpha() != 0) {
            dc.g.setColor(fill);
            dc.g.fill(getZeroShape());
        }
        if (getStrokeWidth() > 0) {
            dc.g.setStroke(this.stroke);
            dc.g.setColor(getStrokeColor());
            dc.g.draw(getZeroShape());
        }
        drawChildren(dc);
        if (dc.isInteractive()) {
            if (isSelected() && dc.focal != this) {
                final Shape shape = getZeroShape();
                if (DEBUG.CONTAINMENT) out("drawing selection bounds shape " + shape);
                dc.g.setColor(COLOR_HIGHLIGHT);
                dc.g.fill(shape);
            } else if (isZoomedFocus() && !hasDecoratedFeatures()) {
                dc.setAbsoluteStroke(1);
                dc.g.setColor(COLOR_SELECTION);
                dc.g.draw(getZeroShape());
            }
        }
    }

    /** @return 1 */
    @Override
    public int getPickLevel() {
        return 1;
    }

    @Override
    protected LWComponent pickChild(PickContext pc, LWComponent c) {
        if (pc.pickDepth > 0) return c; else if (c.isPathwayOwned() && c instanceof LWSlide) return c; else return this;
    }

    /** @return false unless decordated: groups contain no points themselves --
     * only a point over a child is "contained" by the group. If decorated,
     * the standard impl applies of containing any point in the bounding box.
     */
    @Override
    protected boolean containsImpl(final float x, final float y, PickContext pc) {
        if ((pc.isZoomRollover && pc.pickDepth < 1) || hasDecoratedFeatures() || pc.root == this) {
            return super.containsImpl(x, y, pc);
        } else return false;
    }

    @Override
    protected boolean intersectsImpl(final Rectangle2D rect) {
        if (isForSelection) return true;
        if (hasDecoratedFeatures()) {
            return super.intersectsImpl(rect);
        } else {
            for (LWComponent c : getChildren()) if (c.intersects(rect)) return true;
            return false;
        }
    }

    private boolean hasDecoratedFeatures() {
        return getStrokeWidth() > 0 || !isTransparent();
    }
}
