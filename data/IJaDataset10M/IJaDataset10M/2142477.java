package tufts.vue;

import tufts.vue.gui.TextRow;
import tufts.vue.ui.ResourceIcon;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import javax.swing.*;
import javax.swing.border.*;
import com.lowagie.text.pdf.PdfGraphics2D;
import edu.tufts.vue.preferences.PreferencesManager;
import java.util.Iterator;
import edu.tufts.vue.preferences.PreferencesManager;
import edu.tufts.vue.preferences.VuePrefEvent;
import edu.tufts.vue.preferences.VuePrefListener;
import edu.tufts.vue.preferences.implementations.BooleanPreference;
import edu.tufts.vue.preferences.implementations.ShowIconsPreference;
import edu.tufts.vue.preferences.interfaces.VuePreference;

/**
 * Icon's for displaying on LWComponents.
 *
 * Various icons can be displayed and stacked vertically or horizontally.
 * The icon region displays a tool-tip on rollover and may handle double-click.
 *
 * @version $Revision: 1.71 $ / $Date: 2007/11/22 07:28:50 $ / $Author: sfraize $
 *
 */
public abstract class LWIcon extends Rectangle2D.Float implements VueConstants {

    private static final org.apache.log4j.Logger Log = org.apache.log4j.Logger.getLogger(LWIcon.class);

    private static final float DefaultScale = 0.045f;

    private static final Color TextColor = VueResources.getColor("node.icon.color.foreground");

    private static final Color FillColor = VueResources.getColor("node.icon.color.fill");

    public static final Font FONT_ICON = VueResources.getFont("node.icon.font");

    private static final BooleanPreference oneClickLaunchResPref = BooleanPreference.create(edu.tufts.vue.preferences.PreferenceConstants.INTERACTIONS_CATEGORY, "oneClickResLaunch", VueResources.getString("preference.resourcelaunching.title"), VueResources.getString("preference.resourcelaunching.description"), Boolean.TRUE, true);

    protected LWComponent mLWC;

    protected final Color mColor;

    protected float mMinWidth;

    protected float mMinHeight;

    public static final ShowIconsPreference IconPref = new ShowIconsPreference();

    static {
        IconPref.addVuePrefListener(new VuePrefListener() {

            public void preferenceChanged(VuePrefEvent prefEvent) {
                Log.debug("update icon prefs: " + prefEvent);
                for (tufts.vue.LWMap map : VUE.getAllMaps()) {
                    map.notify(LWKey.Repaint);
                }
            }
        });
    }

    private LWIcon(LWComponent lwc, Color c) {
        super.width = 22;
        super.height = 12;
        mLWC = lwc;
        mColor = c;
    }

    private LWIcon(LWComponent lwc) {
        this(lwc, TextColor);
    }

    public void setLocation(float x, float y) {
        super.x = x;
        super.y = y;
    }

    public void setSize(float w, float h) {
        super.width = w;
        super.height = h;
    }

    /**
     * do we contain coords x,y?
     * Coords may be component local or map local or
     * whataver -- depends on what was handed to us
     * via @see setLocation
     */
    public boolean contains(float x, float y) {
        if (isShowing() && super.width > 0 && super.height > 0) {
            return x >= super.x + 1 && y >= super.y + 1 && x <= (super.x + super.width - 1) && y <= (super.y + super.height - 1);
        }
        return false;
    }

    public void setMinimumSize(float w, float h) {
        mMinWidth = w;
        mMinHeight = h;
        if (super.width < w) super.width = w;
        if (super.height < h) super.height = h;
    }

    void draw(DrawContext dc) {
        if (DEBUG.BOXES) {
            dc.g.setColor(Color.red);
            dc.g.setStroke(STROKE_SIXTEENTH);
            dc.g.draw(this);
        }
    }

    void layout() {
    }

    abstract boolean isShowing();

    abstract void doSingleClickAction();

    abstract void doDoubleClickAction();

    public abstract JComponent getToolTipComponent();

    private static final Cursor RESOURCE_CURSOR = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);

    private static tufts.vue.Resource RolloverResource;

    public static boolean hasRolloverResource(Cursor cursor) {
        return RolloverResource != null && cursor == RESOURCE_CURSOR;
    }

    public static void clearRolloverResource() {
        RolloverResource = null;
        final MapViewer viewer = VUE.getActiveViewer();
        if (viewer != null && viewer.getCursor() == RESOURCE_CURSOR) {
            viewer.setTopCursor(VueToolbarController.getActiveTool().getCursor());
        }
    }

    public static void displayRolloverResource() {
        if (RolloverResource != null) {
            if (oneClickLaunchResPref.isTrue()) RolloverResource.displayContent();
            clearRolloverResource();
        }
    }

    public static class Block extends Rectangle2D.Float {

        public static final boolean VERTICAL = true;

        public static final boolean HORIZONTAL = false;

        private LWComponent mLWC;

        private LWIcon[] mIcons = new LWIcon[7];

        private final boolean mNoShrink;

        private boolean mVertical;

        private final float mIconWidth;

        private final float mIconHeight;

        public Block(LWComponent lwc, int iconWidth, int iconHeight, Color c, boolean vertical) {
            if (c == null) c = TextColor;
            mNoShrink = (lwc instanceof LWImage);
            mIconWidth = iconWidth;
            mIconHeight = iconHeight;
            setOrientation(vertical);
            mIcons[0] = new LWIcon.Resource(lwc, c);
            mIcons[1] = new LWIcon.Notes(lwc, c);
            mIcons[2] = new LWIcon.Pathway(lwc, c);
            mIcons[3] = new LWIcon.MetaData(lwc, c);
            mIcons[4] = new LWIcon.Hierarchy(lwc, c);
            mIcons[5] = new LWIcon.MergeSourceMetaData(lwc, c);
            mIcons[6] = new LWIcon.OntologicalMetaData(lwc, c);
            for (int i = 0; i < mIcons.length; i++) {
                mIcons[i].setSize(iconWidth, iconHeight);
                mIcons[i].setMinimumSize(iconWidth, iconHeight);
            }
            this.mLWC = lwc;
            IconPref.addVuePrefListener(new VuePrefListener() {

                public void preferenceChanged(VuePrefEvent prefEvent) {
                    mLWC.layout();
                }
            });
        }

        public float getIconWidth() {
            return mIconWidth;
        }

        public float getIconHeight() {
            return mIconHeight;
        }

        public void setOrientation(boolean vertical) {
            mVertical = vertical;
            if (vertical) super.width = mIconWidth; else super.height = mIconHeight;
        }

        /**
         * do we contain coords x,y?
         * Coords may be component local or map local or
         * whataver -- depends on what was handed to us
         * via @see setLocation
         */
        public boolean contains(float x, float y) {
            if (isShowing() && super.width > 0 && super.height > 0) {
                return x >= super.x && y >= super.y && x <= (super.x + super.width) && y <= (super.y + super.height);
            }
            return false;
        }

        public String toString() {
            return "LWIcon.Block[" + super.x + "," + super.y + " " + super.width + "x" + super.height + " " + mLWC + "]";
        }

        boolean isShowing() {
            return super.width > 0 && super.height > 0;
        }

        void setLocation(float x, float y) {
            super.x = x;
            super.y = y;
            layout();
        }

        /** Layout whatever is currently relevant to show, computing
         * width & height -- does NOT change location of the block itself
         */
        void layout() {
            if (mVertical) {
                super.height = 0;
                float iconY = super.y;
                for (int i = 0; i < mIcons.length; i++) {
                    LWIcon icon = mIcons[i];
                    if (icon.isShowing()) {
                        icon.layout();
                        icon.setLocation(x, iconY);
                        iconY += icon.height + 3;
                        super.height += icon.height + 3;
                    }
                }
            } else {
                super.width = 0;
                float iconX = super.x;
                for (int i = 0; i < mIcons.length; i++) {
                    LWIcon icon = mIcons[i];
                    if (icon.isShowing()) {
                        icon.layout();
                        icon.setLocation(iconX, y);
                        iconX += icon.width + 3;
                        super.width += icon.width + 3;
                    }
                }
            }
        }

        void draw(DrawContext dc) {
            if (mNoShrink && dc.zoom < 1) dc.setAbsoluteDrawing(true);
            for (int i = 0; i < mIcons.length; i++) {
                if (mIcons[i].isShowing()) mIcons[i].draw(dc);
            }
            if (mNoShrink && dc.zoom < 1) dc.setAbsoluteDrawing(false);
        }

        void checkAndHandleMouseOver(MapMouseEvent e) {
            final Point2D.Float localPoint = e.getLocalPoint(mLWC);
            float cx = localPoint.x;
            float cy = localPoint.y;
            RolloverResource = null;
            JComponent tipComponent = null;
            LWIcon tipIcon = null;
            for (LWIcon icon : mIcons) {
                if (!icon.isShowing()) continue;
                if (mNoShrink) {
                    double zoom = e.getViewer().getZoomFactor();
                    if (zoom < 1) {
                        cx *= zoom;
                        cy *= zoom;
                    }
                }
                if (icon instanceof LWIcon.Resource) {
                    if (icon.contains(cx, cy)) {
                        e.getViewer().setTopCursor(RESOURCE_CURSOR);
                        RolloverResource = icon.mLWC.getResource();
                    } else {
                        e.getViewer().setTopCursor(VueToolbarController.getActiveTool().getCursor());
                    }
                }
                if (icon.contains(cx, cy)) {
                    tipIcon = icon;
                    break;
                } else {
                    e.getViewer().setTopCursor(VueToolbarController.getActiveTool().getCursor());
                }
            }
            if (tipIcon != null) {
                tipComponent = tipIcon.getToolTipComponent();
                final Rectangle2D tipRegion = mLWC.transformZeroToMapRect((Rectangle2D.Float) tipIcon.getBounds2D());
                final Rectangle2D avoidRegion;
                if (mLWC instanceof LWLink) {
                    if (mLWC.hasLabel()) {
                        avoidRegion = new Rectangle2D.Float();
                        avoidRegion.setRect(mLWC.labelBox.getBoxBounds());
                        avoidRegion.add(this);
                    } else {
                        avoidRegion = this;
                    }
                    mLWC.transformZeroToMapRect((Rectangle2D.Float) avoidRegion);
                } else {
                    avoidRegion = mLWC.transformZeroToMapRect(mLWC.getZeroBounds());
                }
                e.getViewer().activateRolloverToolTip(e, tipComponent, avoidRegion, tipRegion);
            }
        }

        boolean handleSingleClick(MapMouseEvent e) {
            boolean handled = false;
            final Point2D.Float localPoint = e.getLocalPoint(mLWC);
            for (int i = 0; i < mIcons.length; i++) {
                LWIcon icon = mIcons[i];
                if (icon.isShowing() && icon.contains(localPoint.x, localPoint.y)) {
                    icon.doSingleClickAction();
                    handled = true;
                    break;
                }
            }
            return handled;
        }

        boolean handleDoubleClick(MapMouseEvent e) {
            boolean handled = false;
            final Point2D.Float localPoint = e.getLocalPoint(mLWC);
            for (int i = 0; i < mIcons.length; i++) {
                LWIcon icon = mIcons[i];
                if (icon.isShowing() && icon.contains(localPoint.x, localPoint.y)) {
                    icon.doDoubleClickAction();
                    handled = true;
                    break;
                }
            }
            return handled;
        }
    }

    /**
     * AALabel: A JLabel that forces anti-aliasing -- use this if
     * you want a tool-tip to be anti-aliased on the PC,
     * because there's no way to set it otherwise.
     * (This is redundant on the Mac which does it automatically)
     */
    class AALabel extends JLabel {

        AALabel(String s) {
            super(s);
        }

        ;

        public void paintComponent(Graphics g) {
            ((Graphics2D) g).setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            super.paintComponent(g);
        }
    }

    static class Resource extends LWIcon {

        private static final String NoResource = VueUtil.isMacPlatform() ? "---" : "__";

        private TextRow mTextRow;

        private String extension;

        private final Rectangle2D.Float boxBounds = new Rectangle2D.Float();

        Resource(LWComponent lwc) {
            super(lwc);
        }

        Resource(LWComponent lwc, Color c) {
            super(lwc, c);
            layout();
        }

        boolean isShowing() {
            if (IconPref.getResourceIconValue()) return mLWC.hasResource(); else return false;
        }

        void doDoubleClickAction() {
            if (oneClickLaunchResPref.isFalse()) {
                Log.debug("DOUBLE-CLICK " + getClass());
                mLWC.getResource().displayContent();
            }
        }

        void doSingleClickAction() {
            if (oneClickLaunchResPref.isTrue()) {
                Log.debug("SINGLE CLICK " + getClass() + " " + mLWC);
                mLWC.getResource().displayContent();
            }
        }

        static final String gap = "&nbsp;";

        static final String indent = "";

        private JLabel ttResource;

        public JComponent getToolTipComponent() {
            if (ttResource == null) {
                ttResource = new AALabel("");
                ttResource.setFont(FONT_MEDIUM_UNICODE);
            }
            final tufts.vue.Resource r = mLWC.getResource();
            final boolean hasTitle = (r.getTitle() != null && !r.getTitle().equals(r.getLocationName()));
            final String prettyResource = r.getLocationName();
            ttResource.setIcon(r.getTinyIcon());
            ttResource.setVerticalTextPosition(SwingConstants.TOP);
            ttResource.setText("<html>" + (hasTitle ? (indent + r.getTitle() + "&nbsp;<br>") : "") + indent + prettyResource);
            return ttResource;
        }

        void layout() {
            extension = null;
            internalLayout();
        }

        private static final Color BoxFill = FillColor;

        void internalLayout() {
            if (extension == null) {
                if (mLWC.hasResource()) extension = mLWC.getResource().getDataType();
                if (extension == null || extension.length() < 1) {
                    extension = NoResource;
                } else if (extension.length() > 3) {
                    extension = extension.substring(0, 3);
                }
                mTextRow = TextRow.instance(extension.toLowerCase(), FONT_ICON);
            }
            boxBounds.setRect(this);
            if (mLWC instanceof LWNode) {
                final float insetW = 2;
                final float insetH = 1;
                boxBounds.x += insetW;
                boxBounds.y += insetH;
                boxBounds.width -= insetW * 2;
                boxBounds.height -= insetH * 2;
            }
        }

        void draw(DrawContext dc) {
            if (false && DEBUG.Enabled) {
                Icon icon = mLWC.getResource().getContentIcon();
                if (icon instanceof ResourceIcon) {
                    ((ResourceIcon) icon).setSize((int) Math.round(getWidth() - 4), (int) Math.round(getHeight()));
                    icon.paintIcon(null, dc.g, (int) Math.round(getX() + 2), (int) Math.round(getY() + 2));
                    super.draw(dc);
                    return;
                }
            }
            if (true || extension == null) internalLayout();
            super.draw(dc);
            if (dc.isIndicated(mLWC)) {
                dc.g.setColor(Color.white);
                dc.g.fill(boxBounds);
                dc.g.setColor(Color.gray);
                dc.g.setStroke(STROKE_HALF);
                dc.g.draw(boxBounds);
                return;
            }
            if (!dc.isInteractive()) {
                dc.g.setRenderingHint(PdfGraphics2D.HyperLinkKey.KEY_INSTANCE, mLWC.getResource().getSpec());
                BufferedImage bi = new BufferedImage((int) getWidth(), (int) getHeight(), BufferedImage.TYPE_INT_ARGB);
                Graphics g = bi.createGraphics();
                if (g instanceof Graphics2D) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setBackground(new Color(0, 0, 0, 0));
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN, 0.0f));
                    g2d.clearRect(0, 0, (int) getWidth(), (int) getHeight());
                }
                dc.g.drawImage(bi, (BufferedImageOp) null, (int) getX(), (int) getY());
                dc.g.setRenderingHint(PdfGraphics2D.HyperLinkKey.KEY_INSTANCE, PdfGraphics2D.HyperLinkKey.VALUE_HYPERLINKKEY_OFF);
            }
            final Color fill = mLWC.getRenderFillColor(dc);
            dc.g.setColor(BoxFill);
            dc.g.fill(boxBounds);
            dc.g.setColor(fill == null ? Color.gray : fill.darker());
            dc.g.setStroke(STROKE_HALF);
            dc.g.draw(boxBounds);
            dc.g.setColor(mColor);
            dc.g.setFont(FONT_ICON);
            double x = getX();
            double y = getY();
            dc.g.translate(x, y);
            final TextRow row = mTextRow;
            if (super.width < row.width) super.width = row.width;
            final float xoff = (super.width - row.width) / 2;
            final float yoff = (super.height - row.height) / 2;
            row.draw(dc, xoff, yoff);
            dc.g.translate(-x, -y);
        }
    }

    static class Notes extends LWIcon {

        private static final float MaxX = 155;

        private static final float MaxY = 212;

        private static final float scale = DefaultScale;

        private static final AffineTransform t = AffineTransform.getScaleInstance(scale, scale);

        private static final Stroke stroke = new BasicStroke(0.2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);

        static float iconWidth = MaxX * scale;

        static float iconHeight = MaxY * scale;

        private static final GeneralPath pencil_body = new GeneralPath();

        private static final GeneralPath pencil_point = new GeneralPath();

        private static final GeneralPath pencil_tip = new GeneralPath();

        static {
            pencil_body.moveTo(0, 31);
            pencil_body.lineTo(55, 0);
            pencil_body.lineTo(150, 155);
            pencil_body.lineTo(98, 187);
            pencil_body.closePath();
            pencil_body.transform(t);
            pencil_point.moveTo(98, 187);
            pencil_point.lineTo(150, 155);
            pencil_point.lineTo(150, 212);
            pencil_point.closePath();
            pencil_point.transform(t);
            pencil_tip.moveTo(132, 203);
            pencil_tip.lineTo(150, 192);
            pencil_tip.lineTo(150, 212);
            pencil_tip.closePath();
            pencil_tip.transform(t);
        }

        Notes(LWComponent lwc, Color c) {
            super(lwc, c);
        }

        Notes(LWComponent lwc) {
            super(lwc);
        }

        boolean isShowing() {
            if (IconPref.getNotesIconValue()) return mLWC.hasNotes(); else return false;
        }

        void doSingleClickAction() {
        }

        void doDoubleClickAction() {
            tufts.vue.gui.GUI.makeVisibleOnScreen(this, NotePanel.class);
        }

        private JComponent ttNotes;

        private String ttLastNotes;

        public JComponent getToolTipComponent() {
            if (ttNotes == null || !ttLastNotes.equals(mLWC.getNotes())) {
                ttLastNotes = mLWC.getNotes();
                int size = ttLastNotes.length();
                if (size > 50 || ttLastNotes.indexOf('\n') >= 0) {
                    JTextArea ta = new JTextArea(ttLastNotes, 1, 30);
                    ta.setFont(FONT_SMALL);
                    ta.setLineWrap(true);
                    ta.setWrapStyleWord(true);
                    ta.setSize(ta.getPreferredSize());
                    ttNotes = ta;
                } else {
                    ttNotes = new JLabel(ttLastNotes);
                    ttNotes.setFont(FONT_SMALL);
                }
            }
            return ttNotes;
        }

        public void draw(DrawContext dc) {
            super.draw(dc);
            double x = getX();
            double y = getY();
            dc.g.translate(x, y);
            double x2 = (getWidth() - iconWidth) / 2;
            double y2 = (getHeight() - iconHeight) / 2;
            dc.g.translate(x2, y2);
            x += x2;
            y += y2;
            dc.g.setColor(mColor);
            dc.g.fill(pencil_body);
            dc.g.setStroke(stroke);
            dc.g.setColor(Color.white);
            dc.g.fill(pencil_point);
            dc.g.setColor(mColor);
            dc.g.draw(pencil_point);
            dc.g.fill(pencil_tip);
            dc.g.translate(-x, -y);
        }
    }

    static class Pathway extends LWIcon {

        private static final float MaxX = 224;

        private static final float MaxY = 145;

        private static final double scale = DefaultScale;

        private static final double scaleInv = 1 / scale;

        private static final Stroke stroke = new BasicStroke((float) (0.5 / scale));

        static float iconWidth = (float) (MaxX * scale);

        static float iconHeight = (float) (MaxY * scale);

        private static final Line2D line1 = new Line2D.Float(39, 123, 92, 46);

        private static final Line2D line2 = new Line2D.Float(101, 43, 153, 114);

        private static final Line2D line3 = new Line2D.Float(163, 114, 224, 39);

        private static final Ellipse2D dot1 = new Ellipse2D.Float(0, 95, 62, 62);

        private static final Ellipse2D dot2 = new Ellipse2D.Float(65, 0, 62, 62);

        private static final Ellipse2D dot3 = new Ellipse2D.Float(127, 90, 62, 62);

        Pathway(LWComponent lwc, Color c) {
            super(lwc, c);
        }

        Pathway(LWComponent lwc) {
            super(lwc);
        }

        boolean isShowing() {
            if (IconPref.getPathwayIconValue()) return mLWC.inPathway(); else return false;
        }

        void doDoubleClickAction() {
            tufts.vue.gui.GUI.makeVisibleOnScreen(this, PathwayPanel.class);
        }

        private JComponent ttPathway;

        private String ttPathwayHtml;

        public JComponent getToolTipComponent() {
            String html = "<html>";
            Iterator i = mLWC.getPathways().iterator();
            int n = 0;
            while (i.hasNext()) {
                LWPathway p = (LWPathway) i.next();
                if (n++ > 0) html += "<br>";
                html += "&nbsp;In path: <b>" + p.getLabel() + "</b>&nbsp;";
            }
            if (ttPathwayHtml == null || !ttPathwayHtml.equals(html)) {
                ttPathway = new AALabel(html);
                ttPathway.setFont(FONT_MEDIUM);
                ttPathwayHtml = html;
            }
            return ttPathway;
        }

        void doSingleClickAction() {
        }

        void draw(DrawContext dc) {
            super.draw(dc);
            double x = getX();
            double y = getY();
            dc.g.translate(x, y);
            double x2 = (getWidth() - iconWidth) / 2;
            double y2 = (getHeight() - iconHeight) / 2;
            dc.g.translate(x2, y2);
            x += x2;
            y += y2;
            dc.g.scale(scale, scale);
            dc.g.setColor(mColor);
            dc.g.fill(dot1);
            dc.g.fill(dot2);
            dc.g.fill(dot3);
            dc.g.setStroke(stroke);
            dc.g.draw(line1);
            dc.g.draw(line2);
            dc.g.draw(line3);
            dc.g.scale(scaleInv, scaleInv);
            dc.g.translate(-x, -y);
        }
    }

    static class MetaData extends LWIcon {

        private static final int w = 16;

        private static final float MaxX = 221;

        private static final float MaxY = 114 + w;

        private static final double scale = DefaultScale;

        private static final double scaleInv = 1 / scale;

        private static final AffineTransform t = AffineTransform.getScaleInstance(scale, scale);

        private static float iconWidth = (float) (MaxX * scale);

        private static float iconHeight = (float) (MaxY * scale);

        private static final GeneralPath ul = new GeneralPath();

        private static final GeneralPath ll = new GeneralPath();

        private static final GeneralPath ur = new GeneralPath();

        private static final GeneralPath lr = new GeneralPath();

        static {
            ul.moveTo(0, 58);
            ul.lineTo(96, 0);
            ul.lineTo(96, w);
            ul.lineTo(0, 58 + w);
            ul.closePath();
            ul.transform(t);
            ll.moveTo(0, 58);
            ll.lineTo(96, 114);
            ll.lineTo(96, 114 + w);
            ll.lineTo(0, 58 + w);
            ll.closePath();
            ll.transform(t);
            ur.moveTo(125, 0);
            ur.lineTo(221, 58);
            ur.lineTo(221, 58 + w);
            ur.lineTo(125, w);
            ur.closePath();
            ur.transform(t);
            lr.moveTo(221, 58);
            lr.lineTo(125, 114);
            lr.lineTo(125, 114 + w);
            lr.lineTo(221, 58 + w);
            lr.closePath();
            lr.transform(t);
        }

        MetaData(LWComponent lwc, Color c) {
            super(lwc, c);
        }

        MetaData(LWComponent lwc) {
            super(lwc);
        }

        boolean isShowing() {
            if (IconPref.getMetaDataIconValue()) return mLWC.hasMetaData(); else return false;
        }

        void doDoubleClickAction() {
            System.out.println(this + " Meta-Data Action?");
        }

        void doSingleClickAction() {
        }

        private JComponent ttMetaData;

        private String ttMetaDataHtml;

        public JComponent getToolTipComponent() {
            String html = "<html>";
            html += mLWC.getMetaDataAsHTML();
            if (ttMetaDataHtml == null || !ttMetaDataHtml.equals(html)) {
                ttMetaData = new AALabel(html);
                ttMetaData.setFont(FONT_MEDIUM);
                ttMetaDataHtml = html;
            }
            return ttMetaData;
        }

        void draw(DrawContext dc) {
            super.draw(dc);
            double x = getX() + (getWidth() - iconWidth) / 2;
            double y = getY() + (getHeight() - iconHeight) / 2;
            dc.g.translate(x, y);
            dc.g.setColor(mColor);
            dc.g.fill(ul);
            dc.g.fill(ur);
            dc.g.fill(ll);
            dc.g.fill(lr);
            dc.g.translate(-x, -y);
        }
    }

    static class MergeSourceMetaData extends LWIcon {

        private static final int w = 16;

        private static final float MaxX = 221;

        private static final float MaxY = 114 + w;

        private static final double scale = DefaultScale;

        private static final double scaleInv = 1 / scale;

        private static final AffineTransform t = AffineTransform.getScaleInstance(scale, scale);

        private static float iconWidth = (float) (MaxX * scale);

        private static float iconHeight = (float) (MaxY * scale);

        private static final Ellipse2D oval = new Ellipse2D.Float(0, 0, 3, 3);

        MergeSourceMetaData(LWComponent lwc, Color c) {
            super(lwc, c);
        }

        MergeSourceMetaData(LWComponent lwc) {
            super(lwc);
        }

        boolean isShowing() {
            if (IconPref.getMetaDataIconValue()) return mLWC.hasMetaData(edu.tufts.vue.metadata.VueMetadataElement.OTHER); else return false;
        }

        void doDoubleClickAction() {
            System.out.println(this + " Merge Source Meta-Data Action?");
        }

        void doSingleClickAction() {
        }

        private JComponent ttMetaData;

        private String ttMetaDataHtml;

        public JComponent getToolTipComponent() {
            String html = "<html>";
            html += mLWC.getMetaDataAsHTML(edu.tufts.vue.metadata.VueMetadataElement.OTHER);
            if (ttMetaDataHtml == null || !ttMetaDataHtml.equals(html)) {
                ttMetaData = new AALabel(html);
                ttMetaData.setFont(FONT_MEDIUM);
                ttMetaDataHtml = html;
            }
            return ttMetaData;
        }

        void draw(DrawContext dc) {
            super.draw(dc);
            double x = getX() + (getWidth() - iconWidth) / 2;
            double y = getY() + (getHeight() - iconHeight) / 2;
            dc.g.translate(x, y);
            dc.g.setColor(mColor);
            dc.g.setStroke(STROKE_HALF);
            dc.g.draw(oval);
            dc.g.translate(-x, -y);
        }
    }

    static class OntologicalMetaData extends LWIcon {

        private static final int w = 16;

        private static final float MaxX = 221;

        private static final float MaxY = 114 + w;

        private static final double scale = DefaultScale;

        private static final double scaleInv = 1 / scale;

        private static final AffineTransform t = AffineTransform.getScaleInstance(scale, scale);

        private static float iconWidth = (float) (MaxX * scale);

        private static float iconHeight = (float) (MaxY * scale);

        private static final GeneralPath one = new GeneralPath();

        private static final GeneralPath two = new GeneralPath();

        private static final GeneralPath three = new GeneralPath();

        private static final GeneralPath four = new GeneralPath();

        private static final GeneralPath five = new GeneralPath();

        static {
            one.moveTo(73, 96);
            one.lineTo(0, 81);
            one.lineTo(11, 46);
            one.lineTo(79, 77);
            one.lineTo(74, 84);
            one.closePath();
            one.transform(t);
            two.moveTo(83, 73);
            two.lineTo(76, 0);
            two.lineTo(111, 0);
            two.lineTo(104, 75);
            two.lineTo(94, 72);
            two.closePath();
            two.transform(t);
            three.moveTo(107, 77);
            three.lineTo(176, 47);
            three.lineTo(186, 80);
            three.lineTo(113, 96);
            three.lineTo(112, 86);
            three.closePath();
            three.transform(t);
            four.moveTo(113, 100);
            four.lineTo(162, 156);
            four.lineTo(133, 177);
            four.lineTo(95, 112);
            four.lineTo(106, 109);
            four.closePath();
            four.transform(t);
            five.moveTo(91, 112);
            five.lineTo(53, 177);
            five.lineTo(24, 156);
            five.lineTo(74, 100);
            five.lineTo(81, 109);
            five.closePath();
            five.transform(t);
        }

        OntologicalMetaData(LWComponent lwc, Color c) {
            super(lwc, c);
        }

        OntologicalMetaData(LWComponent lwc) {
            super(lwc);
        }

        boolean isShowing() {
            if (IconPref.getMetaDataIconValue()) return mLWC.hasMetaData(edu.tufts.vue.metadata.VueMetadataElement.ONTO_TYPE); else return false;
        }

        void doDoubleClickAction() {
            System.out.println(this + " Ontological Meta-Data Action?");
        }

        void doSingleClickAction() {
        }

        private JComponent ttMetaData;

        private String ttMetaDataHtml;

        public JComponent getToolTipComponent() {
            String html = "<html>";
            html += mLWC.getMetaDataAsHTML(edu.tufts.vue.metadata.VueMetadataElement.ONTO_TYPE);
            if (ttMetaDataHtml == null || !ttMetaDataHtml.equals(html)) {
                ttMetaData = new AALabel(html);
                ttMetaData.setFont(FONT_MEDIUM);
                ttMetaDataHtml = html;
            }
            return ttMetaData;
        }

        void draw(DrawContext dc) {
            super.draw(dc);
            double x = getX() + (getWidth() - iconWidth) / 2;
            double y = getY() + (getHeight() - iconHeight) / 2;
            dc.g.translate(x, y);
            dc.g.setColor(mColor);
            dc.g.fill(one);
            dc.g.fill(two);
            dc.g.fill(three);
            dc.g.fill(four);
            dc.g.fill(five);
            dc.g.translate(-x, -y);
        }
    }

    static class Hierarchy extends LWIcon {

        private static final float MaxX = 220;

        private static final float MaxY = 155;

        private static final double scale = DefaultScale;

        private static final double scaleInv = 1 / scale;

        private static final Stroke stroke = STROKE_TWO;

        static float iconWidth = (float) (MaxX * scale);

        static float iconHeight = (float) (MaxY * scale);

        private static final Line2D line1 = new Line2D.Float(101, 16, 141, 16);

        private static final Line2D line2 = new Line2D.Float(70, 76, 141, 76);

        private static final Line2D line3 = new Line2D.Float(101, 136, 141, 136);

        private static final Line2D line4 = new Line2D.Float(101, 16, 101, 136);

        private static final Rectangle2D box = new Rectangle2D.Float(0, 51, 56, 56);

        private static final Rectangle2D rect1 = new Rectangle2D.Float(150, 0, 70, 33);

        private static final Rectangle2D rect2 = new Rectangle2D.Float(150, 63, 70, 33);

        private static final Rectangle2D rect3 = new Rectangle2D.Float(150, 122, 70, 33);

        Hierarchy(LWComponent lwc, Color c) {
            super(lwc, c);
        }

        Hierarchy(LWComponent lwc) {
            super(lwc);
        }

        boolean isShowing() {
            if (IconPref.getHierarchyIconValue()) {
                if (mLWC.numChildren() == 1) {
                    LWComponent child0 = mLWC.getChild(0);
                    if (child0.isTextNode() || LWNode.isImageNode(mLWC)) return false; else return true;
                } else if (mLWC.hasChildren()) return true;
            }
            return false;
        }

        void doDoubleClickAction() {
        }

        void doSingleClickAction() {
        }

        private JLabel ttTree;

        private String ttTreeHtml;

        public JComponent getToolTipComponent() {
            if ((mLWC instanceof LWContainer) == false) return new JLabel(VueResources.getString("jlabel.nochild"));
            String html = "<html>" + getChildHtml(mLWC, 1);
            if (html.endsWith("<br>")) html = html.substring(0, html.length() - 4);
            if (ttTreeHtml == null || !ttTreeHtml.equals(html)) {
                ttTree = new AALabel(html);
                ttTree.setFont(FONT_MEDIUM);
                ttTreeHtml = html;
            }
            return ttTree;
        }

        private static final String Indent = "&nbsp;&nbsp;&nbsp;&nbsp;";

        private static final String RightMargin = Indent;

        private String getChildHtml(LWComponent c, int indent) {
            String label = null;
            if (indent == 1) label = "&nbsp;<b>" + c.getDisplayLabel() + "</b>"; else label = c.getDisplayLabel();
            String html = label + RightMargin + "<br>";
            if (!(c instanceof LWContainer)) return html;
            for (LWComponent child : c.getChildren()) {
                if (child.isTextNode()) continue;
                for (int x = 0; x < indent; x++) html += Indent;
                if (indent % 2 == 0) html += "- "; else html += "+ ";
                html += getChildHtml(child, indent + 1);
            }
            return html;
        }

        void draw(DrawContext dc) {
            super.draw(dc);
            double x = getX() + (getWidth() - iconWidth) / 2;
            double y = getY() + (getHeight() - iconHeight) / 2;
            dc.g.translate(x, y);
            dc.g.scale(scale, scale);
            dc.g.setColor(mColor);
            dc.g.fill(box);
            dc.g.fill(rect1);
            dc.g.fill(rect2);
            dc.g.fill(rect3);
            dc.g.setStroke(stroke);
            dc.g.draw(line1);
            dc.g.draw(line2);
            dc.g.draw(line3);
            dc.g.draw(line4);
            dc.g.scale(scaleInv, scaleInv);
            dc.g.translate(-x, -y);
        }
    }

    private static Font MinisculeFont = new Font("SansSerif", Font.PLAIN, 1);
}
