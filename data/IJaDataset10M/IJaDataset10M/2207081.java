package tufts.vue;

import tufts.Util;
import static tufts.Util.fmt;
import tufts.vue.shape.RectangularPoly2D;
import edu.tufts.vue.preferences.PreferencesManager;
import edu.tufts.vue.preferences.VuePrefEvent;
import edu.tufts.vue.preferences.VuePrefListener;
import edu.tufts.vue.preferences.implementations.ShowIconsPreference;
import edu.tufts.vue.preferences.interfaces.VuePreference;
import java.util.Iterator;
import java.util.Collection;
import java.util.List;
import java.awt.*;
import java.awt.geom.*;
import javax.swing.ImageIcon;

public class LWNode extends LWContainer {

    protected static final org.apache.log4j.Logger Log = org.apache.log4j.Logger.getLogger(LWNode.class);

    public static final Object TYPE_TEXT = "textNode";

    static final boolean WrapText = false;

    public static final Font DEFAULT_NODE_FONT = VueResources.getFont("node.font");

    public static final Color DEFAULT_NODE_FILL = VueResources.getColor("node.fillColor");

    public static final int DEFAULT_NODE_STROKE_WIDTH = VueResources.getInt("node.strokeWidth");

    public static final Color DEFAULT_NODE_STROKE_COLOR = VueResources.getColor("node.strokeColor");

    public static final Font DEFAULT_TEXT_FONT = VueResources.getFont("text.font");

    /** how much smaller children are than their immediately enclosing parent (is cumulative) */
    static final float ChildScale = VueResources.getInt("node.child.scale", 75) / 100f;

    /** 0 based with current local width/height */
    protected RectangularShape mShape;

    protected boolean isAutoSized = true;

    private transient float mBoxedLayoutChildY;

    private transient boolean isRectShape = true;

    private transient Line2D.Float mIconDivider = new Line2D.Float();

    private transient Point2D.Float mLabelPos = new Point2D.Float();

    private transient Point2D.Float mChildPos = new Point2D.Float();

    private transient Size mMinSize;

    private transient boolean inLayout = false;

    private transient boolean isCenterLayout = false;

    private java.awt.Dimension textSize = null;

    private final LWIcon.Block mIconBlock = new LWIcon.Block(this, IconWidth, IconHeight, null, LWIcon.Block.VERTICAL);

    private void initNode() {
        enableProperty(KEY_Alignment);
    }

    LWNode(String label, float x, float y, RectangularShape shape) {
        initNode();
        super.label = label;
        setFillColor(DEFAULT_NODE_FILL);
        if (shape == null) setShape(tufts.vue.shape.RoundRect2D.class); else if (shape != null) setShapeInstance(shape);
        setStrokeWidth(DEFAULT_NODE_STROKE_WIDTH);
        setStrokeColor(DEFAULT_NODE_STROKE_COLOR);
        setLocation(x, y);
        this.width = NEEDS_DEFAULT;
        this.height = NEEDS_DEFAULT;
        setFont(DEFAULT_NODE_FONT);
        setLabel(label);
    }

    public LWNode(String label) {
        this(label, 0, 0);
    }

    LWNode(String label, RectangularShape shape) {
        this(label, 0, 0, shape);
    }

    LWNode(String label, float x, float y) {
        this(label, x, y, null);
    }

    LWNode(String label, Resource resource) {
        this(label, 0, 0);
        setResource(resource);
    }

    public static final Key KEY_Shape = new Key<LWNode, Class<? extends RectangularShape>>("node.shape", "shape") {

        @Override
        public boolean setValueFromCSS(LWNode c, String cssKey, String cssValue) {
            RectangularShape shape = NodeTool.getTool().getNamedShape(cssValue);
            if (shape == null) {
                return false;
            } else {
                setValue(c, shape.getClass());
                System.err.println("applied shape: " + this + "=" + getValue(c));
                return true;
            }
        }

        @Override
        public void setValue(LWNode c, Class<? extends RectangularShape> shapeClass) {
            c.setShape(shapeClass);
        }

        @Override
        public Class<? extends RectangularShape> getValue(LWNode c) {
            try {
                return c.mShape.getClass();
            } catch (NullPointerException e) {
                return null;
            }
        }

        /**
         * This is overridden to allow for equivalence tests against an instance value
         * RectangularShape, as opposed to just types of Class<? extends
         * RectangularShape>.
         *
         * @param other
         * If this is an instance of RectangularShape, we compare
         * our getValue() against it's Class object, not it's instance.
         */
        @Override
        boolean valueEquals(LWNode c, Object other) {
            final Class<? extends RectangularShape> value = getValue(c);
            final Class<? extends RectangularShape> otherValue;
            if (other instanceof RectangularShape) {
                otherValue = ((RectangularShape) other).getClass();
            } else if (other instanceof Class) {
                otherValue = (Class) other;
            } else if (other != null) {
                if (DEBUG.Enabled) Log.warn(this + "; valueEquals against unexpected type: " + Util.tags(other));
                return false;
            } else otherValue = null;
            return value == otherValue || (otherValue != null && otherValue.equals(value));
        }
    };

    /**
     * @param shapeClass -- a class object this is a subclass of RectangularShape
     */
    public void setShape(Class<? extends RectangularShape> shapeClass) {
        if (mShape != null && IsSameShape(mShape.getClass(), shapeClass)) return;
        try {
            setShapeInstance(shapeClass.newInstance());
        } catch (Throwable t) {
            tufts.Util.printStackTrace(t);
        }
    }

    /**
     * @param shape a new instance of a shape for us to use: should be a clone and not an original
     */
    protected void setShapeInstance(RectangularShape shape) {
        if (DEBUG.CASTOR) System.out.println("SETSHAPE " + shape.getClass() + " in " + this + " " + shape);
        if (IsSameShape(mShape, shape)) return;
        final Object old = mShape;
        isRectShape = (shape instanceof Rectangle2D || shape instanceof RoundRectangle2D);
        mShape = shape;
        mShape.setFrame(0, 0, getWidth(), getHeight());
        layout(LWKey.Shape);
        notify(LWKey.Shape, new Undoable(old) {

            void undo() {
                setShapeInstance((RectangularShape) old);
            }
        });
    }

    public void setXMLshape(RectangularShape shape) {
        setShapeInstance(shape);
    }

    public RectangularShape getXMLshape() {
        return mShape;
    }

    @Override
    public RectangularShape getZeroShape() {
        return mShape;
    }

    @Override
    protected Point2D getZeroSouthEastCorner() {
        if (isRectShape) return super.getZeroSouthEastCorner();
        final float[] corner = VueUtil.computeIntersection(getWidth() / 2, getHeight() / 2, getWidth(), getHeight(), mShape, null);
        return new Point2D.Float(corner[0], corner[1]);
    }

    /**
     * This fixed value depends on the arc width/height specifications in our RoundRect2D, which
     * are currently 20,20.
     * If that ever changes, this will need to be recomputed (see commented out code in
     * in getZeroNorthWestCorner) and updated.
     * @see tufts.vue.shape.RoundRect2D
     */
    private static final Point2D RoundRectCorner = new Point2D.Double(2.928932, 2.928932);

    @Override
    protected Point2D getZeroNorthWestCorner() {
        if (mShape instanceof Rectangle2D) {
            return super.getZeroNorthWestCorner();
        } else if (mShape instanceof RoundRectangle2D) {
            return RoundRectCorner;
        } else {
            final float[] corner = VueUtil.computeIntersection(getWidth() / 2, getHeight() / 2, 0, 0, mShape, null);
            return new Point2D.Float(corner[0], corner[1]);
        }
    }

    /** Duplicate this node.
     * @return the new node -- will have the same style (visible properties) of the old node */
    @Override
    public LWNode duplicate(CopyContext cc) {
        LWNode newNode = (LWNode) super.duplicate(cc);
        if (DEBUG.STYLE) out("re-adjusting size during duplicate to set shape size");
        newNode.setSize(super.getWidth(), super.getHeight());
        return newNode;
    }

    @Override
    public boolean supportsUserLabel() {
        return true;
    }

    @Override
    public boolean supportsUserResize() {
        if (isTextNode()) return !isAutoSized(); else return true;
    }

    /** @return false if this is a text node */
    @Override
    public boolean supportsChildren() {
        if (hasFlag(Flag.SLIDE_STYLE) && hasResource() && getResource().isImage()) {
            return true;
        } else return !isTextNode();
    }

    /** @return true -- a node is always considered to have content */
    @Override
    public boolean hasContent() {
        return true;
    }

    @Override
    public int getFocalMargin() {
        return -10;
    }

    @Override
    public boolean isManagingChildLocations() {
        return true;
    }

    /**
     * This is consulted during LAYOUT, which can effect the size of the node.
     * So if anything happens that changes what this returns, the node has
     * to be laid out again.  (E.g., if we turn them all of with a pref,
     * all nodes need to be re-laid out / resized
     */
    protected boolean iconShowing() {
        return !hasFlag(Flag.INTERNAL) && mIconBlock.isShowing();
    }

    private boolean textBoxHit(float cx, float cy) {
        final float lx = relativeLabelX() - IconPadRight;
        final float ly = relativeLabelY() - PadTop;
        final Size size = getTextSize();
        final float h = size.height + PadTop;
        final float w = size.width + IconPadRight;
        return cx >= lx && cy >= ly && cx <= lx + w && cy <= ly + h;
    }

    @Override
    public void mouseOver(MapMouseEvent e) {
        if (iconShowing()) mIconBlock.checkAndHandleMouseOver(e);
    }

    @Override
    public boolean handleDoubleClick(MapMouseEvent e) {
        if (this instanceof LWPortal) return super.handleDoubleClick(e);
        final Point2D.Float localPoint = e.getLocalPoint(this);
        final float cx = localPoint.x;
        final float cy = localPoint.y;
        if (textBoxHit(cx, cy)) {
            e.getViewer().activateLabelEdit(this);
        } else {
            if (!mIconBlock.handleDoubleClick(e)) {
                if (hasResource()) {
                    getResource().displayContent();
                }
            }
        }
        return true;
    }

    @Override
    public boolean handleSingleClick(MapMouseEvent e) {
        final Point2D.Float localPoint = e.getLocalPoint(this);
        final float cx = localPoint.x;
        final float cy = localPoint.y;
        if (!textBoxHit(cx, cy)) {
            return mIconBlock.handleSingleClick(e);
        }
        return false;
    }

    public static boolean isImageNode(LWComponent c) {
        if (c instanceof LWNode) {
            final LWNode node = (LWNode) c;
            final LWComponent childZero = node.getChild(0);
            return childZero instanceof LWImage && childZero.hasResource() && childZero.getResource().equals(node.getResource());
        } else return false;
    }

    public LWImage getImage() {
        if (isImageNode(this)) return (LWImage) getChild(0); else return null;
    }

    @Override
    public Object getTypeToken() {
        return isTextNode() ? TYPE_TEXT : super.getTypeToken();
    }

    /**
     * if asText is true, make this a text node, and isTextNode should return true.
     * If asText is false, do the minimum to this node such that isTextNode will
     * no longer return true.
     */
    public void setAsTextNode(boolean asText) {
        if (asText) {
            setShape(java.awt.geom.Rectangle2D.Float.class);
            disableProperty(LWKey.Shape);
            setFillColor(COLOR_TRANSPARENT);
            setFont(DEFAULT_TEXT_FONT);
        } else {
            enableProperty(LWKey.Shape);
            setFillColor(DEFAULT_NODE_FILL);
        }
        if (asText) setAutoSized(true);
    }

    public static boolean isTextNode(LWComponent c) {
        if (c instanceof LWNode) return ((LWNode) c).isTextNode(); else return false;
    }

    @Override
    public boolean isTextNode() {
        return isLikelyTextNode() && mShape instanceof Rectangle2D;
    }

    @Override
    public boolean isLikelyTextNode() {
        return getClass() == LWNode.class && isTranslucent() && !hasChildren() && !inPathway();
    }

    @Override
    public boolean isExternalResourceLinkForPresentations() {
        return hasResource() && !hasChildren() && !iconShowing() && getStyle() != null;
    }

    /** If true, compute node size from label & children */
    @Override
    public boolean isAutoSized() {
        if (WrapText) return false; else return isAutoSized;
    }

    /**
     * For explicitly restoring the autoSized bit to true.
     *
     * The autoSize bit is only *cleared* via automatic means: when the
     * node's size is explicitly set to something bigger than that
     * size it would have if it took on it's automatic size.
     *
     * Clearing the autoSize bit on a node manually would have no
     * effect, because as soon as it was next laid out, it would
     * notice it has it's minimum size, and would automatically
     * set the bit.
     */
    @Override
    public void setAutoSized(boolean makeAutoSized) {
        if (WrapText) return;
        if (isAutoSized == makeAutoSized) return;
        if (DEBUG.LAYOUT) out("*** setAutoSized " + makeAutoSized);
        Object old = null;
        if (makeAutoSized) old = new Point2D.Float(this.width, this.height);
        isAutoSized = makeAutoSized;
        if (isAutoSized && !inLayout) layout();
        if (makeAutoSized) notify("node.autosized", new Undoable(old) {

            void undo() {
                Point2D.Float p = (Point2D.Float) old;
                setSize(p.x, p.y);
            }
        });
    }

    /**
     * For triggering automatic shifts in the auto-size bit based on a call
     * to setSize or as a result of a layout
     */
    private void setAutomaticAutoSized(boolean tv) {
        if (isOrphan()) return;
        if (isAutoSized == tv) return;
        if (DEBUG.LAYOUT) out("*** setAutomaticAutoSized " + tv);
        isAutoSized = tv;
    }

    private static boolean IsSameShape(Class<? extends RectangularShape> c1, Class<? extends RectangularShape> c2) {
        if (c1 == null || c2 == null) return false;
        if (c1 == c2) {
            if (java.awt.geom.RoundRectangle2D.class.isAssignableFrom(c1)) return false; else return true;
        } else return false;
    }

    private static boolean IsSameShape(Shape s1, Shape s2) {
        if (s1 == null || s2 == null) return false;
        if (s1.getClass() == s2.getClass()) {
            if (s1 instanceof java.awt.geom.RoundRectangle2D) {
                RoundRectangle2D rr1 = (RoundRectangle2D) s1;
                RoundRectangle2D rr2 = (RoundRectangle2D) s2;
                return rr1.getArcWidth() == rr2.getArcWidth() && rr1.getArcHeight() == rr2.getArcHeight();
            } else return true;
        } else return false;
    }

    @Override
    protected boolean intersectsImpl(final Rectangle2D mapRect) {
        if (isRectShape) {
            return super.intersectsImpl(mapRect);
        } else {
            if (super.intersectsImpl(mapRect) == false) {
                return false;
            } else {
                return getZeroShape().intersects(transformMapToZeroRect(mapRect));
            }
        }
    }

    @Override
    protected boolean containsImpl(float x, float y, PickContext pc) {
        if (isRectShape) {
            return super.containsImpl(x, y, pc);
        } else if (super.containsImpl(x, y, pc)) {
            return mShape.contains(x, y);
        } else return false;
    }

    @Override
    public void XML_completed(Object context) {
        super.XML_completed(context);
        if (hasChildren()) {
            if (DEBUG.WORK || DEBUG.XML || DEBUG.LAYOUT) Log.debug("XML_completed: scaling down LWNode children in: " + this);
            for (LWComponent c : getChildren()) {
                if (isScaledChildType(c)) c.setScale(ChildScale);
            }
            if (hasResource() && getChild(0) instanceof LWImage) {
                final LWImage image = (LWImage) getChild(0);
                final Resource IR = image.getResource();
                final Resource r = getResource();
                if (r != null && IR != null && r != IR && r.equals(IR)) {
                    takeResource(IR);
                }
            }
        }
    }

    @Override
    protected void addChildImpl(LWComponent c, Object context) {
        if (isScaledChildType(c)) c.setScale(LWNode.ChildScale);
        super.addChildImpl(c, context);
    }

    @Override
    public void addChildren(java.util.Collection<? extends LWComponent> children, Object context) {
        if (!mXMLRestoreUnderway && !hasResource() && !hasChildren() && children.size() == 1) {
            final LWComponent first = Util.getFirst(children);
            if (first instanceof LWImage) {
                takeResource(first.getResource());
            }
        }
        super.addChildren(children, context);
    }

    @Override
    protected List<LWComponent> sortForIncomingZOrder(Collection<? extends LWComponent> toAdd) {
        return java.util.Arrays.asList(sort(toAdd, YSorter));
    }

    @Override
    public void setResource(final Resource r) {
        super.setResource(r);
        if (r == null || mXMLRestoreUnderway) return;
        LWImage newImageIcon = null;
        boolean rebuildImageIcon = true;
        if (getChild(0) instanceof LWImage) {
            final LWImage image0 = (LWImage) getChild(0);
            if (DEBUG.IMAGE) out("checking for resource sync to image @child(0): " + image0);
            if (r.isImage()) {
                if (image0.isNodeIcon() && !r.equals(image0.getResource())) {
                    deleteChildPermanently(image0);
                    newImageIcon = LWImage.createNodeIcon(image0, r);
                }
            } else {
                deleteChildPermanently(image0);
            }
        } else if (r.isImage()) {
            newImageIcon = LWImage.createNodeIcon(r);
        }
        if (newImageIcon != null) {
            addChild(newImageIcon);
            sendToBack(newImageIcon);
        }
    }

    private void loadAssetToVueMetadata(Osid2AssetResource r) throws org.osid.repository.RepositoryException {
        org.osid.repository.Asset asset = r.getAsset();
        if (asset == null) {
            Log.warn(this + "; can't load asset meta-data: Resource has no asset: " + r);
            return;
        }
        org.osid.repository.RecordIterator recordIterator = asset.getRecords();
        while (recordIterator.hasNextRecord()) {
            org.osid.repository.Record record = recordIterator.nextRecord();
            org.osid.repository.PartIterator partIterator = record.getParts();
            String recordDesc = null;
            while (partIterator.hasNextPart()) {
                org.osid.repository.Part part = partIterator.nextPart();
                org.osid.repository.PartStructure partStructure = part.getPartStructure();
                if ((part != null) && (partStructure != null)) {
                    org.osid.shared.Type partStructureType = partStructure.getType();
                    final String description = partStructure.getDescription();
                    java.io.Serializable value = part.getValue();
                    String key;
                    if (description != null && description.trim().length() > 0) {
                        key = description;
                    } else {
                        key = partStructureType.getKeyword();
                    }
                    if (!key.startsWith(VueResources.getString("metadata.dublincore.url"))) continue;
                    if (key == null) {
                        Log.warn(this + " Asset Part [" + part + "] has null key.");
                        continue;
                    }
                    if (value == null) {
                        Log.warn(this + " Asset Part [" + key + "] has null value.");
                        continue;
                    }
                    if (value instanceof String) {
                        String s = ((String) value).trim();
                        if (s.length() <= 0) continue;
                        if (s.startsWith("<p>") && s.endsWith("</p>")) {
                            String body = s.substring(3, s.length() - 4);
                            if (body.trim().length() == 0) {
                                if (DEBUG.DR) value = "[empty <p></p> ignored]"; else continue;
                            }
                        }
                        edu.tufts.vue.metadata.VueMetadataElement vueMDE = new edu.tufts.vue.metadata.VueMetadataElement();
                        vueMDE.setKey(key);
                        vueMDE.setValue(value.toString());
                        vueMDE.setType(edu.tufts.vue.metadata.VueMetadataElement.CATEGORY);
                        getMetadataList().addElement(vueMDE);
                    }
                }
            }
        }
    }

    static boolean isScaledChildType(LWComponent c) {
        return c instanceof LWNode || c instanceof LWSlide;
    }

    @Override
    protected void removeChildImpl(LWComponent c) {
        c.setScale(1.0);
        super.removeChildImpl(c);
    }

    @Override
    protected final void setSizeImpl(float w, float h, boolean internal) {
        if (DEBUG.LAYOUT) out("*** setSize         " + w + "x" + h);
        if (isAutoSized() && (w > this.width || h > this.height)) setAutomaticAutoSized(false);
        layoutNode(LWKey.Size, new Size(getWidth(), getHeight()), new Size(w, h));
    }

    private void setSizeNoLayout(float w, float h) {
        if (DEBUG.LAYOUT) out("*** setSizeNoLayout " + w + "x" + h);
        super.setSizeImpl(w, h, false);
        mShape.setFrame(0, 0, getWidth(), getHeight());
    }

    public Size getMinimumSize() {
        return mMinSize;
    }

    @Override
    public void setLocation(float x, float y) {
        super.setLocation(x, y);
        layoutChildren();
    }

    @Override
    protected void layoutImpl(Object triggerKey) {
        layoutNode(triggerKey, new Size(getWidth(), getHeight()), null);
    }

    /**
     * @param triggerKey - the property change that triggered this layout
     * @param curSize - the current size of the node
     * @param request - the requested new size of the node
     */
    private void layoutNode(Object triggerKey, Size curSize, Size request) {
        if (inLayout) {
            if (DEBUG.Enabled) {
                if (DEBUG.LAYOUT) new Throwable("ALREADY IN LAYOUT " + this).printStackTrace(); else Log.warn("already in layout: " + this);
            }
            return;
        }
        inLayout = true;
        if (DEBUG.LAYOUT) {
            String msg = "*** layoutNode, trigger=" + triggerKey + " cur=" + curSize + " request=" + request + " isAutoSized=" + isAutoSized();
            if (DEBUG.META) Util.printClassTrace("tufts.vue.LW", msg + " " + this); else out(msg);
        }
        mIconBlock.layout();
        if (DEBUG.LAYOUT && labelBox != null) {
            final int prefHeight = labelBox.getPreferredSize().height;
            final int realHeight = labelBox.getHeight();
            if (prefHeight != realHeight) {
                Log.debug("prefHeight != height in " + this + "\n\tprefHeight=" + prefHeight + "\n\trealHeight=" + realHeight);
            }
        }
        if (triggerKey == Flag.COLLAPSED) {
            final boolean collapsed = isCollapsed();
            for (LWComponent c : getChildren()) {
                c.setHidden(HideCause.COLLAPSED, collapsed);
            }
        }
        final Size min;
        isCenterLayout = !isRectShape;
        if (isCenterLayout) {
            if (request == null) request = curSize;
            min = layoutCentered(request);
        } else {
            min = layoutBoxed(request, curSize, triggerKey);
            if (request == null) request = curSize;
        }
        mMinSize = new Size(min);
        if (DEBUG.LAYOUT) out("*** layout computed minimum=" + min);
        if (request.height <= min.height && request.width <= min.width) setAutomaticAutoSized(true);
        final float newWidth;
        final float newHeight;
        if (isAutoSized()) {
            newWidth = min.width;
            newHeight = min.height;
        } else {
            if (request.width > min.width) newWidth = request.width; else newWidth = min.width;
            if (request.height > min.height) newHeight = request.height; else newHeight = min.height;
        }
        setSizeNoLayout(newWidth, newHeight);
        if (isCenterLayout == false) {
            layoutBoxed_label();
            mIconDivider.setLine(IconMargin, MarginLinePadY, IconMargin, newHeight - MarginLinePadY);
        }
        if (labelBox != null) labelBox.setBoxLocation(relativeLabelX(), relativeLabelY());
        if (isLaidOut()) {
            this.parent.layout();
        }
        inLayout = false;
    }

    /** @return the current size of the label object, providing a margin of error
     * on the width given sometime java bugs in computing the accurate length of a
     * a string in a variable width font. */
    protected Size getTextSize() {
        if (WrapText) {
            Size s = new Size(getLabelBox().getSize());
            return s;
        } else {
            Size s = new Size(getLabelBox().getPreferredSize());
            Size ps = new Size(getLabelBox().getSize());
            if (ps.height < s.height) s.height = ps.height;
            s.width *= TextWidthFudgeFactor;
            s.width += 3;
            return s;
        }
    }

    private int getTextWidth() {
        if (WrapText) return labelBox.getWidth(); else return Math.round(getTextSize().width);
    }

    /**
     * Layout the contents of the node centered, and return the min size of the node.
     * @return the minimum rectangular size of node shape required to to contain all
     * the visual node contents
     */
    private Size layoutCentered(Size request) {
        NodeContent content = getLaidOutNodeContent();
        Size minSize = new Size(content);
        Size node = new Size(content);
        if (!isAutoSized()) {
            node.fit(request);
        }
        RectangularShape nodeShape = (RectangularShape) mShape.clone();
        nodeShape.setFrame(0, 0, content.width, content.height);
        if ((hasLabel() || hasChildren()) && growShapeUntilContainsContent(nodeShape, content)) {
            minSize.fit(nodeShape);
            node.fit(minSize);
        }
        nodeShape.setFrame(0, 0, node.width, node.height);
        layoutContentInShape(nodeShape, content);
        if (DEBUG.LAYOUT) out("*** content placed at " + content + " in " + nodeShape);
        content.layoutTargets();
        return minSize;
    }

    /**
     * Brute force increase the size of the given arbitrary shape until it's borders fully
     * contain the given rectangle when it is centered in the shape.  Algorithm starts
     * with size of content for shape (which would work it it was rectangular) then increases
     * width & height incrememntally by %10 until content is contained, then backs off 1 pixel
     * at a time to tighten the fit.
     *
     * @param shape - the shape to grow: expected be zero based (x=y=0)
     * @param content - the rectangle to ensure we can contain (x/y is ignored: it's x/y value at end will be centered)
     * @return true if the shape was grown
     */
    private boolean growShapeUntilContainsContent(RectangularShape shape, NodeContent content) {
        final int MaxTries = 1000;
        final float increment;
        if (content.width > content.height) increment = content.width * 0.1f; else increment = content.height * 0.1f;
        final float xinc = increment;
        final float yinc = increment;
        int tries = 0;
        while (!shape.contains(content) && tries < MaxTries) {
            shape.setFrame(0, 0, shape.getWidth() + xinc, shape.getHeight() + yinc);
            layoutContentInShape(shape, content);
            tries++;
        }
        if (tries > 0) {
            final float shrink = 1f;
            if (DEBUG.LAYOUT) System.out.println("Contents of " + shape + "  rought  fit  to " + content + " in " + tries + " tries");
            do {
                shape.setFrame(0, 0, shape.getWidth() - shrink, shape.getHeight() - shrink);
                layoutContentInShape(shape, content);
                tries++;
            } while (content.fitsInside(shape) && tries < MaxTries);
            shape.setFrame(0, 0, shape.getWidth() + shrink, shape.getHeight() + shrink);
        }
        if (tries >= MaxTries) {
            Log.error("Contents of " + shape + " failed to contain " + content + " after " + tries + " tries.");
        } else if (tries > 0) {
            if (DEBUG.LAYOUT) System.out.println("Contents of " + shape + " grown to contain " + content + " in " + tries + " tries");
        } else if (DEBUG.LAYOUT) System.out.println("Contents of " + shape + " already contains " + content);
        if (DEBUG.LAYOUT) out("*** content minput at " + content + " in " + shape);
        return tries > 0;
    }

    /**
     * Layout the given content rectangle in the given shape.  The default is to center
     * the content rectangle in the shape, however, if the shape in an instance
     * of tufts.vue.shape.RectangularPoly2D, it will call getContentGravity() to
     * determine layout gravity (CENTER, NORTH, EAST, etc).
     *
     * @param shape - the shape to layout the content in
     * @param content - the region to layout in the shape: x/y values will be set
     *
     * @see tufts.vue.shape.RectangularPoly2D, 
     */
    private void layoutContentInShape(RectangularShape shape, NodeContent content) {
        final float shapeWidth = (float) shape.getWidth();
        final float shapeHeight = (float) shape.getHeight();
        final float margin = 0.5f;
        boolean content_laid_out = false;
        if (shape instanceof RectangularPoly2D) {
            int gravity = ((RectangularPoly2D) shape).getContentGravity();
            content_laid_out = true;
            if (gravity == RectangularPoly2D.CENTER) {
                content.x = (shapeWidth - content.width) / 2;
                content.y = (shapeHeight - content.height) / 2;
            } else if (gravity == RectangularPoly2D.WEST) {
                content.x = margin;
                content.y = (float) (shapeHeight - content.height) / 2;
            } else if (gravity == RectangularPoly2D.EAST) {
                content.x = (shapeWidth - content.width) - margin;
                content.y = (float) Math.floor((shapeHeight - content.height) / 2);
            } else if (gravity == RectangularPoly2D.NORTH) {
                content.x = (shapeWidth - content.width) / 2;
                content.y = margin;
            } else if (gravity == RectangularPoly2D.SOUTH) {
                content.x = (shapeWidth - content.width) / 2;
                content.y = (shapeHeight - content.height) - margin;
            } else {
                Log.error(new Error("Unsupported content gravity " + gravity + " on shape " + shape + "; defaulting to CENTER"));
                content_laid_out = false;
            }
        }
        if (!content_laid_out) {
            content.x = (shapeWidth - content.width) / 2;
            content.y = (shapeHeight - content.height) / 2;
        }
    }

    /**
     * Provide a center-layout frame work for all the node content.
     * Constructing this object creates the layout.  It get's sizes
     * for all the potential regions in the node (icon block, label,
     * children) and lays out those regions relative to each other,
     * contained in a single rectangle.  Then that containging
     * rectangle can be used to quickly compute the the size of a
     * non-rectangular node shape required to enclose it completely.
     * Layout of the actual underlying targets doesn't happen until
     * layoutTargets() is called.
     */
    private class NodeContent extends Rectangle2D.Float {

        private Rectangle2D.Float rIcons;

        private Rectangle2D.Float rLabel = new Rectangle2D.Float();

        private Rectangle2D.Float rChildren;

        /**
         * Initial position is 0,0.  Regions are all normalized to offsets from 0,0.
         * Construct node content layout object: layout the regions for
         * icons, label & children.  Does NOT do the final layout (setting
         * LWNode member variables, laying out the child nodes, etc, until
         * layoutTargts() is called).
         */
        NodeContent() {
            if (hasLabel()) {
                Size text = getTextSize();
                rLabel.width = text.width;
                rLabel.height = text.height;
                rLabel.x = ChildPadX;
                this.width = ChildPadX + text.width;
                this.height = text.height;
            }
            if (iconShowing()) {
                rIcons = new Rectangle2D.Float(0, 0, mIconBlock.width, mIconBlock.height);
                this.width += mIconBlock.width;
                this.width += ChildPadX;
                rLabel.x += mIconBlock.width;
            }
            if (hasChildren() && !isCollapsed()) {
                Size children = layoutChildren(new Size(), 0f, true);
                float childx = rLabel.x;
                float childy = rLabel.height + ChildPadY;
                rChildren = new Rectangle2D.Float(childx, childy, children.width, children.height);
                this.height = rLabel.height + ChildPadY + children.height;
                fitWidth(rLabel.x + children.width);
            }
            if (rIcons != null) {
                fitHeight(mIconBlock.height);
                if (mIconBlock.height < height) {
                    rIcons.y = (height - rIcons.height) / 2;
                } else if (height > rLabel.height && !hasChildren()) {
                    rLabel.y = (height - rLabel.height) / 2;
                }
            }
        }

        /** do the center-layout for the actual targets (LWNode state) of our regions */
        void layoutTargets() {
            if (DEBUG.LAYOUT) out("*** laying out targets");
            mLabelPos.setLocation(x + rLabel.x, y + rLabel.y);
            if (rIcons != null) {
                mIconBlock.setLocation(x + rIcons.x, y + rIcons.y);
                mIconDivider.setLine(mIconBlock.x + mIconBlock.width, this.y, mIconBlock.x + mIconBlock.width, this.y + this.height);
            }
            if (rChildren != null) {
                mChildPos.setLocation(x + rChildren.x, y + rChildren.y);
                layoutChildren();
            }
        }

        /** @return true if all of the individual content items, as currently positioned, fit
            inside the given shape.  Note that this may return true even while outer dimensions
            of the NodeContent do NOT fit inside the shape: it's okay to clip corners of
            the NodeContent box as long as the individual components still fit: the NodeContent
            box is used for <i>centering</i> the content in the bounding box of the shape,
            and for the initial rough estimate of an enclosing shape.
        */
        private Rectangle2D.Float checker = new Rectangle2D.Float();

        boolean fitsInside(RectangularShape shape) {
            boolean fit = true;
            copyTranslate(rLabel, checker, x, y);
            fit &= shape.contains(checker);
            if (rIcons != null) {
                copyTranslate(rIcons, checker, x, y);
                fit &= shape.contains(checker);
            }
            if (rChildren != null) {
                copyTranslate(rChildren, checker, x, y);
                fit &= shape.contains(checker);
            }
            return fit;
        }

        private void copyTranslate(Rectangle2D.Float src, Rectangle2D.Float dest, float xoff, float yoff) {
            dest.width = src.width;
            dest.height = src.height;
            dest.x = src.x + xoff;
            dest.y = src.y + yoff;
        }

        private void fitWidth(float w) {
            if (width < w) width = w;
        }

        private void fitHeight(float h) {
            if (height < h) height = h;
        }

        public String toString() {
            return "NodeContent[" + VueUtil.out(this) + "]";
        }
    }

    /** 
     * @return internal node content already laid out
     */
    private NodeContent _lastNodeContent;

    /** get a center-layout framework */
    private NodeContent getLaidOutNodeContent() {
        return _lastNodeContent = new NodeContent();
    }

    @Override
    public void setToNaturalSize() {
        Size m = this.getMinimumSize();
        setSize(m.width, m.height);
    }

    private Size layoutBoxed(Size request, Size oldSize, Object triggerKey) {
        final Size min;
        if (WrapText) min = layoutBoxed_floating_text(request, oldSize, triggerKey); else min = layoutBoxed_vanilla(request);
        return min;
    }

    /** @return new minimum size of node */
    private Size layoutBoxed_vanilla(final Size request) {
        final Size min = new Size();
        final Size text = getTextSize();
        min.width = text.width;
        min.height = EdgePadY + text.height + EdgePadY;
        if (!iconShowing()) {
            min.width += LabelPadLeft;
        } else {
            float dividerY = EdgePadY + text.height;
            double stubX = LabelPositionXWhenIconShowing + text.width;
            double stubHeight = DividerStubAscent;
            min.width = (float) stubX + IconPadLeft;
        }
        if (hasChildren() && !isCollapsed()) {
            if (DEBUG.LAYOUT) out("*** textSize b4 layoutBoxed_children: " + text);
            layoutBoxed_children(min, text);
        }
        if (iconShowing()) layoutBoxed_icon(request, min, text);
        return min;
    }

    /** set mLabelPos */
    private void layoutBoxed_label() {
        Size text = getTextSize();
        if (hasChildren()) {
            mLabelPos.y = EdgePadY;
        } else {
            mLabelPos.y = (this.height - text.height) / 2;
        }
        if (iconShowing()) {
            mLabelPos.x = -100;
        } else {
            if (WrapText) mLabelPos.x = (this.width - text.width) / 2 + 1; else mLabelPos.x = 200;
        }
    }

    /** will CHANGE min.width and min.height */
    private void layoutBoxed_children(Size min, Size labelText) {
        if (DEBUG.LAYOUT) out("*** layoutBoxed_children; min=" + min + " text=" + labelText);
        mBoxedLayoutChildY = EdgePadY + labelText.height;
        float minWidth;
        if (false && isPresentationContext()) {
            minWidth = Math.max(labelText.width, getWidth() - 20);
        } else minWidth = 0;
        final Size children = layoutChildren(new Size(), minWidth, false);
        final float childSpan = childOffsetX() + children.width + ChildPadX;
        if (min.width < childSpan) min.width = childSpan;
        min.height += children.height;
        min.height += ChildOffsetY + ChildrenPadBottom;
    }

    protected float getMaxChildSpan() {
        java.util.Iterator i = getChildIterator();
        float maxWidth = 0;
        while (i.hasNext()) {
            LWComponent c = (LWComponent) i.next();
            float w = c.getLocalBorderWidth();
            if (w > maxWidth) maxWidth = w;
        }
        return childOffsetX() + maxWidth + ChildPadX;
    }

    /** will CHANGE min */
    private void layoutBoxed_icon(Size request, Size min, Size text) {
        if (DEBUG.LAYOUT) out("*** layoutBoxed_icon");
        float iconWidth = IconWidth;
        float iconHeight = IconHeight;
        float iconX = IconPadLeft;
        float iconPillarX = iconX;
        float iconPillarY = IconPillarPadY;
        float totalIconHeight = (float) mIconBlock.getHeight();
        float iconPillarHeight = totalIconHeight + IconPillarPadY * 2;
        if (min.height < iconPillarHeight) {
            min.height += iconPillarHeight - min.height;
        } else if (isRectShape) {
            float centerY = (min.height - totalIconHeight) / 2;
            if (centerY > IconPillarPadY + IconPillarFudgeY) centerY = IconPillarPadY + IconPillarFudgeY;
            iconPillarY = centerY;
        }
        if (!isRectShape) {
            float height;
            if (isAutoSized()) height = min.height; else height = Math.max(min.height, request.height);
            iconPillarY = height / 2 - totalIconHeight / 2;
        }
        mIconBlock.setLocation(iconPillarX, iconPillarY);
    }

    private Size layoutBoxed_floating_text(Size request, Size curSize, Object triggerKey) {
        if (DEBUG.LAYOUT) out("*** layoutBoxed_floating_text, req=" + request + " cur=" + curSize + " trigger=" + triggerKey);
        final Size min = new Size();
        getLabelBox();
        if (iconShowing()) min.width = LabelPositionXWhenIconShowing; else min.width = LabelPadLeft;
        min.width += LabelPadRight;
        min.height = EdgePadY + EdgePadY;
        final float textPadWidth = min.width;
        final float textPadHeight = min.height;
        final Size newTextSize;
        final boolean resizeRequest;
        if (request == null) {
            resizeRequest = false;
            request = curSize;
        } else resizeRequest = true;
        if (hasChildren()) request.fitWidth(getMaxChildSpan());
        if (curSize.width == NEEDS_DEFAULT) {
            if (DEBUG.WORK) out("SETTING DEFAULT - UNITIALIZED WIDTH");
            newTextSize = new Size(labelBox.getPreferredSize());
        } else if (textSize == null) {
            if (DEBUG.WORK) out("SETTING DEFAULT - NO TEXT SIZE");
            newTextSize = new Size(labelBox.getPreferredSize());
        } else {
            newTextSize = new Size(textSize);
            if (resizeRequest) {
                newTextSize.width = request.width - textPadWidth;
                newTextSize.height = request.height - textPadHeight;
                newTextSize.fitWidth(labelBox.getMaxWordWidth());
            } else {
                if (true) {
                    boolean keepPreferredWidth = false;
                    boolean keepMaxWordWidth = false;
                    final int curWidth = labelBox.getWidth();
                    if (curWidth == labelBox.getPreferredSize().width) keepPreferredWidth = true; else if (curWidth == labelBox.getMaxWordWidth()) keepMaxWordWidth = true;
                    newTextSize.width = labelBox.getMaxWordWidth();
                } else {
                    newTextSize.width = labelBox.getWidth();
                    newTextSize.fitWidth(labelBox.getMaxWordWidth());
                }
                newTextSize.height = labelBox.getHeight();
            }
        }
        labelBox.setSizeFlexHeight(newTextSize);
        newTextSize.height = labelBox.getHeight();
        this.textSize = newTextSize.dim();
        min.height += newTextSize.height;
        min.width += newTextSize.width;
        if (hasChildren()) {
            layoutBoxed_children(min, newTextSize);
        }
        if (iconShowing()) layoutBoxed_icon(request, min, newTextSize);
        return min;
    }

    /**
     * Need to be able to do this seperately from layout -- this
     * get's called everytime a node's location is changed so
     * that's it's children will follow along with it.
     *
     * Children are laid out relative to the parent, but given
     * absolute map coordinates.  Note that because if this, anytime
     * we're computing a location for a child, we have to factor in
     * the current scale factor of the parent.
     */
    void layoutChildren() {
        layoutChildren(null, 0f, false);
    }

    private Size layoutChildren(Size result) {
        return layoutChildren(0f, 0f, 0f, result);
    }

    private Size layoutChildren(Size result, float minWidth, boolean sizeOnly) {
        if (DEBUG.LAYOUT) out("*** layoutChildren; sizeOnly=" + sizeOnly);
        if (!hasChildren()) return Size.None;
        float baseX = 0;
        float baseY = 0;
        if (!sizeOnly) {
            baseX = childOffsetX();
            baseY = childOffsetY();
        }
        return layoutChildren(baseX, baseY, minWidth, result);
    }

    private void layoutChildren(float baseX, float baseY) {
        layoutChildren(baseX, baseY, 0f, null);
    }

    private Size layoutChildren(float baseX, float baseY, float minWidth, Size result) {
        if (DEBUG.LAYOUT) out("*** layoutChildren at " + baseX + "," + baseY);
        if (DEBUG.LAYOUT && DEBUG.META) Util.printClassTrace("tufts.vue.LW", "*** layoutChildren");
        if (hasFlag(Flag.SLIDE_STYLE) && mAlignment.get() != Alignment.LEFT && isImageNode(this)) layoutChildrenColumnAligned(baseX, baseY, result); else layoutChildrenSingleColumn(baseX, baseY, result);
        return result;
    }

    protected void layoutChildrenColumnAligned(float baseX, float baseY, Size result) {
        float maxWidth = 0;
        for (LWComponent c : getChildren()) {
            if (c instanceof LWLink) continue;
            float w = c.getLocalBorderWidth();
            if (w > maxWidth) maxWidth = w;
        }
        float maxLayoutWidth = Math.max(maxWidth, getWidth() - baseX * 2);
        float y = baseY;
        boolean first = true;
        for (LWComponent c : getChildren()) {
            if (c instanceof LWLink) continue;
            if (first) first = false; else y += ChildVerticalGap * getScale();
            if (mAlignment.get() == Alignment.RIGHT) c.setLocation(baseX + maxLayoutWidth - c.getLocalWidth(), y); else if (mAlignment.get() == Alignment.CENTER) c.setLocation(baseX + (maxLayoutWidth - c.getLocalWidth()) / 2, y); else c.setLocation(baseX, y);
            y += c.getLocalHeight();
        }
        if (result != null) {
            result.width = maxWidth;
            result.height = (y - baseY);
        }
    }

    protected void layoutChildrenSingleColumn(float baseX, float baseY, Size result) {
        float y = baseY;
        float maxWidth = 0;
        boolean first = true;
        for (LWComponent c : getChildren()) {
            if (c instanceof LWLink) continue;
            if (c.isHidden()) continue;
            if (first) first = false; else y += ChildVerticalGap * getScale();
            c.setLocation(baseX, y);
            y += c.getLocalHeight();
            if (result != null) {
                float w = c.getLocalBorderWidth();
                if (w > maxWidth) maxWidth = w;
            }
        }
        if (result != null) {
            result.width = maxWidth;
            result.height = (y - baseY);
        }
    }

    class Column extends java.util.ArrayList<LWComponent> {

        float width;

        float height;

        Column(float minWidth) {
            width = minWidth;
        }

        void layout(float baseX, float baseY, boolean center) {
            float y = baseY;
            Iterator i = iterator();
            while (i.hasNext()) {
                LWComponent c = (LWComponent) i.next();
                if (center) c.setLocation(baseX + (width - c.getLocalBorderWidth()) / 2, y); else c.setLocation(baseX, y);
                y += c.getHeight();
                y += ChildVerticalGap * getScale();
            }
            height = y - baseY;
        }

        void addChild(LWComponent c) {
            super.add(c);
            float w = c.getLocalBorderWidth();
            if (w > width) width = w;
        }
    }

    protected void layoutChildrenGrid(float baseX, float baseY, Size result, int nColumn, float minWidth) {
        float y = baseY;
        float totalWidth = 0;
        float maxHeight = 0;
        Column[] cols = new Column[nColumn];
        java.util.Iterator i = getChildIterator();
        int curCol = 0;
        while (i.hasNext()) {
            LWComponent c = (LWComponent) i.next();
            if (cols[curCol] == null) cols[curCol] = new Column(minWidth);
            cols[curCol].addChild(c);
            if (++curCol >= nColumn) curCol = 0;
        }
        float colX = baseX;
        float colY = baseY;
        for (int x = 0; x < cols.length; x++) {
            Column col = cols[x];
            if (col == null) break;
            col.layout(colX, colY, nColumn == 1);
            colX += col.width + ChildHorizontalGap;
            totalWidth += col.width + ChildHorizontalGap;
            if (col.height > maxHeight) maxHeight = col.height;
        }
        totalWidth -= ChildHorizontalGap;
        if (result != null) {
            result.width = totalWidth;
            result.height = maxHeight;
        }
    }

    @Override
    public Color getRenderFillColor(DrawContext dc) {
        if (DEBUG.LAYOUT) if (!isAutoSized()) return Color.green;
        if (dc == null || dc.focal == this) return super.getRenderFillColor(dc);
        Color fillColor = super.getFillColor();
        if (getParent() instanceof LWNode) {
            if (fillColor != null) {
                Color parentFill = getParent().getRenderFillColor(dc);
                if (parentFill != null && !parentFill.equals(Color.black) && parentFill.getAlpha() != 0 && fillColor.equals(parentFill)) {
                    fillColor = VueUtil.darkerColor(fillColor);
                }
            }
        }
        return fillColor;
    }

    @Override
    protected void drawImpl(DrawContext dc) {
        if (!isFiltered()) {
            drawNode(dc);
        }
        if (hasChildren()) {
            drawChildren(dc);
        }
        if (isSelected() && dc.isInteractive() && dc.focal != this) drawSelection(dc);
    }

    /** Draw without rendering any textual glyphs, possibly without children, possibly as a rectanlge only */
    private void drawNodeWithReducedLOD(final DrawContext dc, final float renderScale) {
        boolean hasVisibleFill = true;
        if (isSelected()) {
            dc.g.setColor(COLOR_SELECTION);
        } else {
            final Color renderFill = getRenderFillColor(dc);
            if (isTransparent() || renderFill.equals(getParent().getRenderFillColor(dc))) hasVisibleFill = false; else dc.g.setColor(renderFill);
        }
        if (this.height * renderScale > 5) {
            if (hasVisibleFill) dc.g.fill(getZeroShape()); else drawLODTextLine(dc);
            if (hasChildren()) drawChildren(dc);
        } else {
            if (hasVisibleFill) {
                if (mShape.getClass() == Rectangle2D.Float.class) dc.g.fill(mShape); else dc.g.fillRect(0, 0, (int) getWidth(), (int) getHeight());
            } else drawLODTextLine(dc);
        }
    }

    private void drawLODTextLine(final DrawContext dc) {
        final int hh = (int) ((getHeight() / 2f) + 0.5f);
        dc.g.setStroke(STROKE_SEVEN);
        dc.g.setColor(mTextColor.get());
        dc.g.drawLine(0, hh, getLabelBox().getWidth(), hh);
    }

    @Override
    public boolean isCollapsed() {
        if (COLLAPSE_IS_GLOBAL) return isGlobalCollapsed; else return super.isCollapsed();
    }

    @Override
    public void setCollapsed(boolean collapsed) {
        if (COLLAPSE_IS_GLOBAL) throw new Error("collapse is set to global impl");
        if (hasFlag(Flag.COLLAPSED) != collapsed) {
            setFlag(Flag.COLLAPSED, collapsed);
            layout(KEY_Collapsed);
            notify(KEY_Collapsed, !collapsed);
        }
    }

    @Override
    protected void drawChildren(DrawContext dc) {
        if (isCollapsed()) {
            if (COLLAPSE_IS_GLOBAL == false) {
                dc.g.setStroke(STROKE_ONE);
                dc.g.setColor(getRenderFillColor(dc));
                final int bottom = (int) (getHeight() + getStrokeWidth() / 2f + 2.5f);
                dc.g.drawLine(1, bottom, (int) (getWidth() - 0.5f), bottom);
            }
            return;
        } else super.drawChildren(dc);
    }

    private void drawSelection(DrawContext dc) {
        dc.g.setColor(COLOR_HIGHLIGHT);
        if (dc.zoom < 1) dc.setAbsoluteStroke(SelectionStrokeWidth); else dc.g.setStroke(new BasicStroke(getStrokeWidth() + SelectionStrokeWidth));
        dc.g.draw(mShape);
    }

    private void drawNode(DrawContext dc) {
        if (dc.isLODEnabled()) {
            final float renderScale = (float) dc.getAbsoluteScale();
            final float renderFont = mFontSize.get() * renderScale;
            final boolean canSkipLabel = renderFont < 5;
            final boolean canSkipIcon;
            if (iconShowing()) canSkipIcon = LWIcon.FONT_ICON.getSize() * renderScale < 5; else canSkipIcon = true;
            if (canSkipLabel && canSkipIcon) {
                drawNodeWithReducedLOD(dc, renderScale);
                return;
            }
        }
        drawFullNode(dc);
    }

    /**  DRAW COMPLETE (with full detail) */
    private void drawFullNode(DrawContext dc) {
        if (false && (dc.isPresenting() || isPresentationContext())) {
            ;
        } else {
            Color fillColor = getRenderFillColor(dc);
            if (fillColor != null && fillColor.getAlpha() != 0) {
                dc.g.setColor(fillColor);
                dc.g.fill(mShape);
            }
        }
        if (getStrokeWidth() > 0) {
            dc.g.setColor(getStrokeColor());
            dc.g.setStroke(this.stroke);
            dc.g.draw(mShape);
        }
        if (DEBUG.BOXES) {
            dc.setAbsoluteStroke(0.5);
            if (false && _lastNodeContent != null && !isRectShape) {
                dc.g.setColor(Color.darkGray);
                dc.g.draw(_lastNodeContent);
            } else {
                dc.g.setColor(Color.blue);
                dc.g.draw(mShape);
            }
        }
        try {
            drawNodeDecorations(dc);
        } catch (Throwable t) {
            Log.error("decoration failed: " + this + " in + " + dc + "; " + t);
            Util.printStackTrace(t);
        }
        if (hasLabel() && this.labelBox != null && this.labelBox.getParent() == null) {
            drawLabel(dc);
        }
    }

    protected void drawLabel(DrawContext dc) {
        float lx = relativeLabelX();
        float ly = relativeLabelY();
        dc.g.translate(lx, ly);
        this.labelBox.draw(dc);
        dc.g.translate(-lx, -ly);
    }

    private void drawNodeDecorations(DrawContext dc) {
        final Graphics2D g = dc.g;
        if (iconShowing()) {
            mIconBlock.draw(dc);
            if (hasLabel()) {
                g.setColor(getContrastStrokeColor(dc));
                g.setStroke(STROKE_ONE);
                g.draw(mIconDivider);
            }
        }
    }

    @Override
    public void initTextBoxLocation(TextBox textBox) {
        textBox.setBoxLocation(relativeLabelX(), relativeLabelY());
    }

    protected float relativeLabelX() {
        if (isCenterLayout) {
            return mLabelPos.x;
        } else if (iconShowing()) {
            return LabelPositionXWhenIconShowing;
        } else {
            if (WrapText) {
                return mLabelPos.x;
            } else {
                if (mAlignment.get() == Alignment.LEFT && hasFlag(Flag.SLIDE_STYLE)) {
                    return ChildPadX;
                } else if (mAlignment.get() == Alignment.RIGHT) {
                    return (this.width - getTextSize().width) - 1;
                } else {
                    final float offset = (this.width - getTextSize().width) / 2;
                    return offset + 1;
                }
            }
        }
    }

    protected float relativeLabelY() {
        if (isCenterLayout) {
            return mLabelPos.y;
        } else if (hasChildren()) {
            return EdgePadY;
        } else {
            if (false && WrapText) return mLabelPos.y; else {
                return (this.height - getTextSize().height) / 2;
            }
        }
    }

    private float childOffsetX() {
        if (isCenterLayout) {
            return mChildPos.x;
        }
        return iconShowing() ? ChildOffsetX : ChildPadX;
    }

    private float childOffsetY() {
        if (isCenterLayout) {
            return mChildPos.y;
        }
        float baseY;
        if (iconShowing()) {
            baseY = mBoxedLayoutChildY;
            if (DEBUG.LAYOUT) out("*** childOffsetY starting with precomputed " + baseY + " to produce " + (baseY + ChildOffsetY));
        } else {
            final TextBox labelBox = getLabelBox();
            int labelHeight = labelBox == null ? 12 : labelBox.getHeight();
            baseY = relativeLabelY() + labelHeight;
        }
        baseY += ChildOffsetY;
        return baseY;
    }

    private static final int EdgePadY = 4;

    private static final int PadTop = EdgePadY;

    private static final int IconGutterWidth = 26;

    private static final int IconPadLeft = 2;

    private static final int IconPadRight = 0;

    private static final int IconWidth = IconGutterWidth - IconPadLeft;

    private static final int IconHeight = VueResources.getInt("node.icon.height", 14);

    private static final int IconMargin = IconPadLeft + IconWidth + IconPadRight;

    /** this is the descent of the closed icon down below the divider line */
    private static final float IconDescent = IconHeight / 3f;

    /** this is the rise of the closed icon above the divider line */
    private static final float IconAscent = IconHeight - IconDescent;

    private static final int IconPadBottom = (int) IconAscent;

    private static final int IconMinY = IconPadLeft;

    private static final int LabelPadLeft = 8;

    private static final int LabelPadRight = 8;

    private static final int LabelPadX = LabelPadLeft;

    private static final int LabelPadY = EdgePadY;

    private static final int LabelPositionXWhenIconShowing = IconMargin + LabelPadLeft;

    private static final int ChildOffsetX = IconMargin + LabelPadLeft;

    private static final int ChildOffsetY = 4;

    private static final int ChildPadY = ChildOffsetY;

    private static final int ChildPadX = 5;

    private static final int ChildVerticalGap = 3;

    private static final int ChildHorizontalGap = 3;

    private static final int ChildrenPadBottom = ChildPadX - ChildVerticalGap;

    private static final float DividerStubAscent = IconDescent;

    private static final float TextWidthFudgeFactor = 1;

    private static final int MarginLinePadY = 5;

    private static final int IconPillarPadY = MarginLinePadY;

    private static final int IconPillarFudgeY = 4;

    /** for castor restore, internal default's and duplicate use only
     * Note special case: this creates a node with autoSized set to false -- this is probably for backward compat with old save files */
    public LWNode() {
        initNode();
        isRectShape = true;
        isAutoSized = false;
        mShape = new java.awt.geom.Rectangle2D.Float();
    }

    /**
     * construct an absolutely minimal node, completely uninitialized (including label, font, size, etc) except for having a rectangular shape
     * Useful for constructing a node that's immediatley going to be styled.
     */
    public static LWNode createRaw() {
        LWNode n = new LWNode();
        n.isAutoSized = true;
        return n;
    }
}
