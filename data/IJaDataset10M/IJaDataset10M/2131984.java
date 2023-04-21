package org.xith3d.ui.hud.widgets;

import java.awt.Font;
import org.openmali.vecmath2.Colorf;
import org.openmali.vecmath2.Point3f;
import org.openmali.vecmath2.Tuple2f;
import org.openmali.vecmath2.Tuple3f;
import org.xith3d.scenegraph.GroupNode;
import org.xith3d.scenegraph.Node;
import org.xith3d.scenegraph.PolygonAttributes;
import org.xith3d.scenegraph.Texture;
import org.xith3d.scenegraph.Transform3D;
import org.xith3d.scenegraph.TransformGroup;
import org.xith3d.scenegraph.utils.ShapeUtils;
import org.xith3d.ui.hud.HUD;
import org.xith3d.ui.hud.base.BackgroundSettableWidget;
import org.xith3d.ui.hud.base.PaddingSettable;
import org.xith3d.ui.hud.base.RectangularWidget;
import org.xith3d.ui.hud.base.WidgetContainer;
import org.xith3d.ui.hud.geometries.NumberGeom;
import org.xith3d.ui.text2d.TextAlignment;

/**
 * A NumberLabel displays floating-point-numbers, that may change very dynamically.
 * 
 * @see Label
 * @see DynamicLabel
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public class NumberLabel extends RectangularWidget implements BackgroundSettableWidget, PaddingSettable {

    /**
     * This class is used to describe a (set of) Label Widget(s).
     * You can pass it to the Label constructor.
     * Modifications on the used instance after creating the Label Widget
     * won't have any effect.
     * 
     * @author Marvin Froehlich (aka Qudus)
     */
    public static class Description extends Label.Description {

        /**
         * @return a clone of this instance
         */
        @Override
        public Description clone() {
            return (new Description(this));
        }

        /**
         * Clon-Constructor
         * 
         * @param desc the original to clone
         */
        public Description(Description desc) {
            super(desc);
        }

        /**
         * Creates a new Label.Description.
         * 
         * @param font the Font to be used for the text
         * @param color the color to be used
         * @param alignment the horizontal and vertical alignment
         * @param bgTex the background texture
         */
        public Description(Font font, Colorf color, TextAlignment alignment, Texture bgTex) {
            super(font, color, alignment, bgTex);
        }

        /**
         * Creates a new Label.Description.
         * 
         * @param font the Font to be used for the text
         * @param color the color to be used
         * @param alignment the horizontal and vertical alignment
         */
        public Description(Font font, Colorf color, TextAlignment alignment) {
            this(font, color, alignment, (Texture) null);
        }

        /**
         * Creates a new Label.Description.
         * 
         * @param alignment the horizontal and vertical alignment
         */
        public Description(TextAlignment alignment) {
            this(null, null, alignment);
        }

        /**
         * Creates a new Label.Description.
         * 
         * @param color the color to be used
         * @param alignment the horizontal and vertical alignment
         */
        public Description(Colorf color, TextAlignment alignment) {
            this(null, color, alignment);
        }

        /**
         * Creates a new Label.Description.
         * 
         * @param font the Font to be used for the text
         * @param color the color to be used
         */
        public Description(Font font, Colorf color) {
            this(font, color, TextAlignment.TOP_LEFT);
        }

        /**
         * Creates a new Label.Description.
         * 
         * @param color the color to be used
         */
        public Description(Colorf color) {
            this(color, TextAlignment.TOP_LEFT);
        }

        /**
         * Creates a new Label.Description.
         * 
         * @param font the Font to be used for the text
         */
        public Description(Font font) {
            this(font, null, TextAlignment.TOP_LEFT);
        }

        /**
         * Creates a new Label.Description.
         * 
         * @param font the Font to be used for the text
         * @param alignment the horizontal and vertical alignment
         */
        public Description(Font font, TextAlignment alignment) {
            this(font, null, alignment);
        }

        /**
         * Creates a new Label.Description.
         */
        public Description() {
            this((Font) null, (Colorf) null, TextAlignment.TOP_LEFT);
        }
    }

    protected boolean autoSize = false;

    private TransformGroup contentGroup;

    private Tuple3f groupLTVW0 = new Point3f();

    private Tuple3f groupLTVW = new Point3f();

    private Image backgroundImage;

    private final TransformGroup scale;

    private NumberGeom numberGeom;

    private TextAlignment alignment;

    private float value;

    private Font font;

    private Colorf fontColor;

    private float paddingBottom = 0f;

    private float paddingRight = 0f;

    private float paddingTop = 0f;

    private float paddingLeft = 0f;

    /**
     * @return the TransformGroup to put the Text2DNode or whatever in
     */
    public GroupNode getContentGroup() {
        return (contentGroup);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTransparency(float transparency) {
        super.setTransparency(transparency);
        if (numberGeom != null) ShapeUtils.setShapesTransparency(numberGeom, transparency, true);
    }

    /**
     * {@inheritDoc}
     */
    public float getTransparency() {
        if (numberGeom != null) return (ShapeUtils.getShapesTransparency(numberGeom)); else return (0.0f);
    }

    /**
     * {@inheritDoc}
     */
    public void setBackground(Colorf color) {
        if (color == null) {
            if (backgroundImage != null) backgroundImage.setVisible(false);
            return;
        }
        if (backgroundImage == null) {
            backgroundImage = new Image(this.getWidth(), getHeight(), 0, color);
            if (isInitialized() && !getWidgetAssembler().contains(backgroundImage)) getWidgetAssembler().addWidget(backgroundImage);
        } else {
            backgroundImage.setColor(color);
        }
        backgroundImage.setVisible(true);
    }

    /**
     * {@inheritDoc}
     */
    public void setBackground(String texture) {
        if (texture == null) {
            if (backgroundImage != null) backgroundImage.setVisible(false);
            return;
        }
        if (backgroundImage == null) {
            backgroundImage = new Image(getWidth(), getHeight(), 0, texture);
            if (isInitialized() && !getWidgetAssembler().contains(backgroundImage)) getWidgetAssembler().addWidget(backgroundImage);
        } else {
            backgroundImage.setTexture(texture);
        }
        backgroundImage.setVisible(true);
    }

    /**
     * {@inheritDoc}
     */
    public void setBackground(Texture texture) {
        if (texture == null) {
            if (backgroundImage != null) backgroundImage.setVisible(false);
            return;
        }
        if (backgroundImage == null) {
            backgroundImage = new Image(getWidth(), getHeight(), 0, texture);
            if (isInitialized() && !getWidgetAssembler().contains(backgroundImage)) getWidgetAssembler().addWidget(backgroundImage);
        } else {
            backgroundImage.setTexture(texture);
        }
        backgroundImage.setVisible(true);
    }

    /**
     * {@inheritDoc}
     */
    public Texture getBackground() {
        if (backgroundImage == null) return (null);
        return (backgroundImage.getTexture());
    }

    /**
     * {@inheritDoc}
     */
    public Colorf getBackgroundColor() {
        if (backgroundImage != null) {
            return (backgroundImage.getColor());
        } else {
            return (null);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void setBackgroundTileSize(float tileWidth, float tileHeight) {
        backgroundImage.setTileSize(tileWidth, tileHeight);
    }

    /**
     * {@inheritDoc}
     */
    public void setBackgroundTileSize(Tuple2f tileSize) {
        backgroundImage.setTileSize(tileSize);
    }

    /**
     * {@inheritDoc}
     */
    public Tuple2f getBackgroundTileSize() {
        return (backgroundImage.getTileSize());
    }

    /**
     * {@inheritDoc}
     */
    public float getBackgroundTileWidth() {
        return (backgroundImage.getTileWidth());
    }

    /**
     * {@inheritDoc}
     */
    public float getBackgroundTileHeight() {
        return (backgroundImage.getTileHeight());
    }

    /**
     * {@inheritDoc}
     */
    public boolean setPadding(float paddingBottom, float paddingRight, float paddingTop, float paddingLeft) {
        if ((this.paddingBottom == paddingBottom) && (this.paddingRight == paddingRight) && (this.paddingTop == paddingTop) && (this.paddingLeft == paddingLeft)) {
            return (false);
        }
        this.paddingBottom = paddingBottom;
        this.paddingRight = paddingRight;
        this.paddingTop = paddingTop;
        this.paddingLeft = paddingLeft;
        if (isInitialized()) updateTranslation();
        return (true);
    }

    /**
     * {@inheritDoc}
     */
    public final boolean setPadding(float padding) {
        return (setPadding(padding, padding, padding, padding));
    }

    /**
     * {@inheritDoc}
     */
    public float getPaddingBottom() {
        return (paddingBottom);
    }

    /**
     * {@inheritDoc}
     */
    public float getPaddingRight() {
        return (paddingRight);
    }

    /**
     * {@inheritDoc}
     */
    public float getPaddingTop() {
        return (paddingTop);
    }

    /**
     * {@inheritDoc}
     */
    public float getPaddingLeft() {
        return (paddingLeft);
    }

    /**
     * Enables or disables auto-sizing.<br>
     * If enabled, the Label's size will always be the minimal size to wrap
     * the whole text content.
     * 
     * @param enabled
     */
    public void setAutoSizeEnabled(boolean enabled) {
        this.autoSize = enabled;
    }

    /**
     * @return if auto-sizing is enabled.<br>
     * If enabled, the Label's size will always be the minimal size to wrap
     * the whole text content.
     */
    public boolean isAutoSizeEnabled() {
        return (autoSize);
    }

    /**
     * @return the untilized NumberGeom to render the number
     */
    public NumberGeom getNumberGeom() {
        return (numberGeom);
    }

    protected void updateNumber() {
        if ((numberGeom != null) && (getContainer() != null)) {
            numberGeom.setValue(value);
        }
        if (isInitialized() && autoSize) {
            setMinimalSize();
        }
        updateTranslation();
    }

    public void setValue(float value) {
        this.value = value;
        updateNumber();
    }

    public float getValue() {
        return (value);
    }

    public void setNumFractions(int numFractions) {
        this.numberGeom.setNumFractions(numFractions);
    }

    public final int getNumFractions() {
        return (numberGeom.getNumFractions());
    }

    /**
     * Sets the horizontal and vertical alignment of the text
     */
    public void setAlignment(TextAlignment alignment) {
        this.alignment = alignment;
        updateNumber();
    }

    /**
     * @return the horizontal and vertical alignment of the text
     */
    public TextAlignment getAlignment() {
        return (alignment);
    }

    /**
     * {@inheritDoc}
     */
    public Colorf getFontColor() {
        return (fontColor);
    }

    /**
     * {@inheritDoc}
     */
    public void setFontColor(Colorf color) {
        this.fontColor = color;
        updateNumber();
    }

    /**
     * {@inheritDoc}
     */
    public Font getFont() {
        return (font);
    }

    /**
     * {@inheritDoc}
     */
    public void setFont(Font font) {
        this.font = font;
        updateNumber();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean setSize(float width, float height, boolean forced) {
        final boolean result = super.setSize(width, height, forced);
        if (!result && !forced) {
            return (false);
        }
        if (backgroundImage != null) {
            backgroundImage.setSize(width, height);
        }
        updateTranslation();
        return (result);
    }

    /**
     * Calculates the minimum Size needed to contain the whole caption.
     */
    public Tuple2f getMinimalSize(Tuple2f buffer) {
        getContainer().getSizeSG2HUD(numberGeom.getWidth(), numberGeom.getHeight(), buffer);
        return (buffer);
    }

    /**
     * Resizes this Label to the minimum Size needed to contain the whole caption.
     */
    public void setMinimalSize() {
        if (getHUD() == null) throw (new IllegalStateException("You cannot call this method, if the Widget is not added to the HUD."));
        final Tuple2f newSize = Tuple2f.fromPool();
        getMinimalSize(newSize);
        setSize(newSize);
        Tuple2f.toPool(newSize);
    }

    protected void fixTranslation(Tuple2f offset) {
        if (numberGeom != null) {
            final Tuple2f size2 = Tuple2f.fromPool();
            getContainer().getSizeHUD2SG(getWidth(), getHeight(), size2);
            if (getAlignment().isHCenterAligned()) offset.addX((size2.getX() / 2.0f) - (numberGeom.getWidth() / 2.0f)); else if (getAlignment().isRightAligned()) offset.addX(size2.getX() - (numberGeom.getWidth() * scale.getTransform().getScale()));
            if (getAlignment().isVCenterAligned()) offset.subY((size2.getY() / 2.0f) - (numberGeom.getHeight() / 2.0f)); else if (getAlignment().isBottomAligned()) offset.subY(size2.getY() - numberGeom.getHeight());
            Tuple2f.toPool(size2);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void updateTranslation() {
        super.updateTranslation();
        final HUD hud = getHUD();
        if ((hud != null) && (hud.getSGNode().getRoot() != null)) {
            Transform3D t3d = contentGroup.getTransform();
            final Tuple2f trans3D = Tuple2f.fromPool();
            float corrLeft = 0f;
            float corrTop = 0f;
            if (getAlignment().isLeftAligned()) corrLeft = getPaddingLeft(); else if (getAlignment().isRightAligned()) corrLeft = -getPaddingRight();
            if (getAlignment().isTopAligned()) corrTop = getPaddingTop(); else if (getAlignment().isBottomAligned()) corrTop = -getPaddingBottom();
            getContainer().getSizeHUD2SG(corrLeft, -corrTop, trans3D);
            fixTranslation(trans3D);
            {
                contentGroup.getWorldTransform().getTranslation(groupLTVW0);
                groupLTVW.set(groupLTVW0);
                groupLTVW.addX(trans3D.getX());
                groupLTVW.addY(trans3D.getY());
                final float pxW = hud.getSGPixelWidth();
                final float pxH = hud.getSGPixelHeight();
                groupLTVW.setX(Math.round(groupLTVW.getX() / pxW));
                groupLTVW.setY(Math.round(groupLTVW.getY() / pxH));
                groupLTVW.mulX(pxW);
                groupLTVW.mulY(pxH);
                trans3D.setX((groupLTVW.getX() - groupLTVW0.getX()));
                trans3D.setY((groupLTVW.getY() - groupLTVW0.getY()));
            }
            t3d.setTranslation(trans3D.getX(), trans3D.getY(), Z_INDEX_UNIT_ASSEMBLER * 1f);
            Tuple2f.toPool(trans3D);
            contentGroup.setTransform(t3d);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void init() {
        scale.getTransform().setScale(1000.0f / getHUD().getWidth());
        if (autoSize) {
            setMinimalSize();
        }
        updateTranslation();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setContainer(WidgetContainer container) {
        super.setContainer(container);
        if ((backgroundImage != null) && !getWidgetAssembler().contains(backgroundImage)) {
            getWidgetAssembler().addWidget(backgroundImage);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return (this.getClass().getSimpleName() + "( name = \"" + this.getName() + "\", value = \"" + this.getValue() + "\" )");
    }

    /**
     * Creates a new Label with the given width, height and z-index.
     * 
     * @param width the new width of this Widget
     * @param height the new height of this Widget
     * @param zIndex the z-index of this Widget
     * @param value the value to display in this NumberWidget
     * @param decimalChar the character to use as decimal delimiter
     * @param maxLength the maximum number of digits (inclusive sign, decimal-delim and fractions)
     * @param labelDesc a LabelDescription instance holding information about font, color, alignment and background-texture
     */
    public NumberLabel(float width, float height, int zIndex, float value, char decimalChar, int maxLength, Description labelDesc) {
        super(width, height, zIndex);
        this.autoSize = ((width <= 0f) || (height <= 0f));
        super.setSize(Math.max(width, 0f), Math.max(height, 0f));
        Node.pushGlobalIgnoreBounds(true);
        this.contentGroup = new TransformGroup();
        Node.popGlobalIgnoreBounds();
        this.transformGroup.addChild(contentGroup);
        if (labelDesc.getBackgroundTexture() != null) backgroundImage = new Image(getWidth(), getHeight(), 0, labelDesc.getBackgroundTexture()); else if (labelDesc.getBackgroundColor() != null) backgroundImage = new Image(getWidth(), getHeight(), 0, labelDesc.getBackgroundColor()); else this.backgroundImage = null;
        this.alignment = labelDesc.getAlignment();
        if (labelDesc.getFont() == null) this.font = HUD.getTheme().getFont(); else this.font = labelDesc.getFont();
        if (labelDesc.getFontColor() == null) this.fontColor = HUD.getTheme().getFontColor(); else this.fontColor = labelDesc.getFontColor();
        Node.pushGlobalIgnoreBounds(true);
        final int numFractions = (decimalChar == '\0') ? 0 : 2;
        this.numberGeom = new NumberGeom(getFont(), getFontColor(), maxLength, decimalChar, numFractions, getValue());
        numberGeom.getAppearance().getPolygonAttributes(true).setCullFace(PolygonAttributes.CULL_BACK);
        Node.popGlobalIgnoreBounds();
        this.scale = new TransformGroup();
        scale.addChild(numberGeom);
        getContentGroup().addChild(scale);
        this.setFocussable(false);
    }

    /**
     * Creates a new Label with the given width, height and z-index.
     * 
     * @param width the new width of this Widget
     * @param height the new height of this Widget
     * @param value the value to display in this NumberWidget
     * @param decimalChar the character to use as decimal delimiter
     * @param maxLength the maximum number of digits (inclusive sign, decimal-delim and fractions)
     * @param labelDesc a LabelDescription instance holding information about font, color, alignment and background-texture
     */
    public NumberLabel(float width, float height, float value, char decimalChar, int maxLength, Description labelDesc) {
        this(width, height, 0, value, decimalChar, maxLength, labelDesc);
    }

    /**
     * Creates a new Label with the given width, height and z-index.
     * 
     * @param size the new size of this Widget
     * @param value the value to display in this NumberWidget
     * @param decimalChar the character to use as decimal delimiter
     * @param maxLength the maximum number of digits (inclusive sign, decimal-delim and fractions)
     * @param labelDesc a LabelDescription instance holding information about font, color, alignment and background-texture
     */
    public NumberLabel(Tuple2f size, float value, char decimalChar, int maxLength, Description labelDesc) {
        this(size.getX(), size.getY(), 0, value, decimalChar, maxLength, labelDesc);
    }

    /**
     * Creates a new Label with the given width, height and z-index.
     * 
     * @param size the new size of this Widget
     * @param zIndex the z-index of this Widget
     * @param value the value to display in this NumberWidget
     * @param decimalChar the character to use as decimal delimiter
     * @param maxLength the maximum number of digits (inclusive sign, decimal-delim and fractions)
     * @param labelDesc a LabelDescription instance holding information about font, color, alignment and background-texture
     */
    public NumberLabel(Tuple2f size, int zIndex, float value, char decimalChar, int maxLength, Description labelDesc) {
        this(size.getX(), size.getY(), 0, value, decimalChar, maxLength, labelDesc);
    }

    /**
     * Creates a new Label with the given width, height and z-index.
     * 
     * @param width the new width of this Widget
     * @param height the new height of this Widget
     * @param zIndex the z-index of this Widget
     * @param value the value to display in this NumberWidget
     * @param decimalChar the character to use as decimal delimiter
     * @param maxLength the maximum number of digits (inclusive sign, decimal-delim and fractions)
     * @param font the Font to be used for the text
     * @param color the color to be used
     * @param alignment the horizontal and vertical alignment
     */
    public NumberLabel(float width, float height, int zIndex, float value, char decimalChar, int maxLength, Font font, Colorf color, TextAlignment alignment) {
        super(width, height, zIndex);
        this.autoSize = ((width <= 0f) || (height <= 0f));
        super.setSize(Math.max(width, 0f), Math.max(height, 0f));
        Node.pushGlobalIgnoreBounds(true);
        this.contentGroup = new TransformGroup();
        Node.popGlobalIgnoreBounds();
        this.transformGroup.addChild(contentGroup);
        this.backgroundImage = null;
        this.alignment = alignment;
        if (font == null) this.font = HUD.getTheme().getFont(); else this.font = font;
        if (color == null) this.fontColor = HUD.getTheme().getFontColor(); else this.fontColor = color;
        Node.pushGlobalIgnoreBounds(true);
        final int numFractions = (decimalChar == '\0') ? 0 : 2;
        this.numberGeom = new NumberGeom(getFont(), getFontColor(), maxLength, decimalChar, numFractions, getValue());
        Node.popGlobalIgnoreBounds();
        this.scale = new TransformGroup();
        scale.addChild(numberGeom);
        getContentGroup().addChild(scale);
        this.setValue(value);
        this.setFocussable(false);
    }

    /**
     * Creates a new Label with the given width, height and z-index.
     * 
     * @param size the new size of this Widget
     * @param zIndex the z-index of this Widget
     * @param value the value to display in this NumberWidget
     * @param decimalChar the character to use as decimal delimiter
     * @param maxLength the maximum number of digits (inclusive sign, decimal-delim and fractions)
     * @param font the Font to be used for the text
     * @param color the color to be used
     * @param alignment the horizontal and vertical alignment
     */
    public NumberLabel(Tuple2f size, int zIndex, float value, char decimalChar, int maxLength, Font font, Colorf color, TextAlignment alignment) {
        this(size.getX(), size.getY(), zIndex, value, decimalChar, maxLength, font, color, alignment);
    }

    /**
     * Creates a new Label with the given width and height and a z-index of 0.
     * 
     * @param size the new size of this Widget
     * @param zIndex the z-index of this Widget
     * @param value the value to display in this NumberWidget
     * @param decimalChar the character to use as decimal delimiter
     * @param maxLength the maximum number of digits (inclusive sign, decimal-delim and fractions)
     * @param alignment the horizontal and vertical alignment
     */
    public NumberLabel(Tuple2f size, int zIndex, float value, char decimalChar, int maxLength, TextAlignment alignment) {
        this(size.getX(), size.getY(), zIndex, value, decimalChar, maxLength, null, null, alignment);
    }

    /**
     * Creates a new Label with the given width and height and a z-index of 0.
     * 
     * @param width the new width of this Widget
     * @param height the new height of this Widget
     * @param zIndex the z-index of this Widget
     * @param value the value to display in this NumberWidget
     * @param decimalChar the character to use as decimal delimiter
     * @param maxLength the maximum number of digits (inclusive sign, decimal-delim and fractions)
     * @param alignment the horizontal and vertical alignment
     */
    public NumberLabel(float width, float height, int zIndex, float value, char decimalChar, int maxLength, TextAlignment alignment) {
        this(width, height, zIndex, value, decimalChar, maxLength, null, null, alignment);
    }

    /**
     * Creates a new Label with the given width and height and a z-index of 0.
     * 
     * @param size the new size of this Widget
     * @param value the value to display in this NumberWidget
     * @param decimalChar the character to use as decimal delimiter
     * @param maxLength the maximum number of digits (inclusive sign, decimal-delim and fractions)
     * @param alignment the horizontal and vertical alignment
     */
    public NumberLabel(Tuple2f size, float value, char decimalChar, int maxLength, TextAlignment alignment) {
        this(size.getX(), size.getY(), 0, value, decimalChar, maxLength, null, null, alignment);
    }

    /**
     * Creates a new Label with the given width and height and a z-index of 0.
     * 
     * @param width the new width of this Widget
     * @param height the new height of this Widget
     * @param value the value to display in this NumberWidget
     * @param decimalChar the character to use as decimal delimiter
     * @param maxLength the maximum number of digits (inclusive sign, decimal-delim and fractions)
     * @param alignment the horizontal and vertical alignment
     */
    public NumberLabel(float width, float height, float value, char decimalChar, int maxLength, TextAlignment alignment) {
        this(width, height, 0, value, decimalChar, maxLength, null, null, alignment);
    }

    /**
     * Creates a new Label with the given width and height and a z-index of 0.
     * 
     * @param size the new size of this Widget
     * @param zIndex the z-index of this Widget
     * @param value the value to display in this NumberWidget
     * @param decimalChar the character to use as decimal delimiter
     * @param maxLength the maximum number of digits (inclusive sign, decimal-delim and fractions)
     * @param color the color to be used
     * @param alignment the horizontal and vertical alignment
     */
    public NumberLabel(Tuple2f size, int zIndex, float value, char decimalChar, int maxLength, Colorf color, TextAlignment alignment) {
        this(size.getX(), size.getY(), zIndex, value, decimalChar, maxLength, null, color, alignment);
    }

    /**
     * Creates a new Label with the given width and height and a z-index of 0.
     * 
     * @param width the new width of this Widget
     * @param height the new height of this Widget
     * @param zIndex the z-index of this Widget
     * @param value the value to display in this NumberWidget
     * @param decimalChar the character to use as decimal delimiter
     * @param maxLength the maximum number of digits (inclusive sign, decimal-delim and fractions)
     * @param color the color to be used
     * @param alignment the horizontal and vertical alignment
     */
    public NumberLabel(float width, float height, int zIndex, float value, char decimalChar, int maxLength, Colorf color, TextAlignment alignment) {
        this(width, height, zIndex, value, decimalChar, maxLength, null, color, alignment);
    }

    /**
     * Creates a new Label with the given width and height and a z-index of 0.
     * 
     * @param size the new size of this Widget
     * @param value the value to display in this NumberWidget
     * @param decimalChar the character to use as decimal delimiter
     * @param maxLength the maximum number of digits (inclusive sign, decimal-delim and fractions)
     * @param color the color to be used
     * @param alignment the horizontal and vertical alignment
     */
    public NumberLabel(Tuple2f size, float value, char decimalChar, int maxLength, Colorf color, TextAlignment alignment) {
        this(size.getX(), size.getY(), 0, value, decimalChar, maxLength, null, color, alignment);
    }

    /**
     * Creates a new Label with the given width and height and a z-index of 0.
     * 
     * @param width the new width of this Widget
     * @param height the new height of this Widget
     * @param value the value to display in this NumberWidget
     * @param decimalChar the character to use as decimal delimiter
     * @param maxLength the maximum number of digits (inclusive sign, decimal-delim and fractions)
     * @param color the color to be used
     * @param alignment the horizontal and vertical alignment
     */
    public NumberLabel(float width, float height, float value, char decimalChar, int maxLength, Colorf color, TextAlignment alignment) {
        this(width, height, 0, value, decimalChar, maxLength, null, color, alignment);
    }

    /**
     * Creates a new Label with the given width and height and a z-index of 0.
     * 
     * @param size the new size of this Widget
     * @param value the value to display in this NumberWidget
     * @param decimalChar the character to use as decimal delimiter
     * @param maxLength the maximum number of digits (inclusive sign, decimal-delim and fractions)
     * @param font the Font to be used for the text
     * @param color the color to be used
     * @param alignment the horizontal and vertical alignment
     */
    public NumberLabel(Tuple2f size, float value, char decimalChar, int maxLength, Font font, Colorf color, TextAlignment alignment) {
        this(size.getX(), size.getY(), 0, value, decimalChar, maxLength, font, color, alignment);
    }

    /**
     * Creates a new Label with the given width and height and a z-index of 0.
     * 
     * @param width the new width of this Widget
     * @param height the new height of this Widget
     * @param value the value to display in this NumberWidget
     * @param decimalChar the character to use as decimal delimiter
     * @param maxLength the maximum number of digits (inclusive sign, decimal-delim and fractions)
     * @param font the Font to be used for the text
     * @param color the color to be used
     * @param alignment the horizontal and vertical alignment
     */
    public NumberLabel(float width, float height, float value, char decimalChar, int maxLength, Font font, Colorf color, TextAlignment alignment) {
        this(width, height, 0, value, decimalChar, maxLength, font, color, alignment);
    }

    /**
     * Creates a new Label with the given width, height and z-index.
     * 
     * @param size the new size of this Widget
     * @param zIndex the z-index of this Widget
     * @param value the value to display in this NumberWidget
     * @param decimalChar the character to use as decimal delimiter
     * @param maxLength the maximum number of digits (inclusive sign, decimal-delim and fractions)
     * @param font the Font to be used for the text
     * @param color the color to be used
     */
    public NumberLabel(Tuple2f size, int zIndex, float value, char decimalChar, int maxLength, Font font, Colorf color) {
        this(size.getX(), size.getY(), zIndex, value, decimalChar, maxLength, font, color, TextAlignment.TOP_LEFT);
    }

    /**
     * Creates a new Label with the given width, height and z-index.
     * 
     * @param width the new width of this Widget
     * @param height the new height of this Widget
     * @param zIndex the z-index of this Widget
     * @param value the value to display in this NumberWidget
     * @param decimalChar the character to use as decimal delimiter
     * @param maxLength the maximum number of digits (inclusive sign, decimal-delim and fractions)
     * @param font the Font to be used for the text
     * @param color the color to be used
     */
    public NumberLabel(float width, float height, int zIndex, float value, char decimalChar, int maxLength, Font font, Colorf color) {
        this(width, height, zIndex, value, decimalChar, maxLength, font, color, TextAlignment.TOP_LEFT);
    }

    /**
     * Creates a new Label with the given width and height and a z-index of 0.
     * 
     * @param size the new size of this Widget
     * @param value the value to display in this NumberWidget
     * @param decimalChar the character to use as decimal delimiter
     * @param maxLength the maximum number of digits (inclusive sign, decimal-delim and fractions)
     * @param font the Font to be used for the text
     * @param color the color to be used
     */
    public NumberLabel(Tuple2f size, float value, char decimalChar, int maxLength, Font font, Colorf color) {
        this(size.getX(), size.getY(), 0, value, decimalChar, maxLength, font, color);
    }

    /**
     * Creates a new Label with the given width and height and a z-index of 0.
     * 
     * @param width the new width of this Widget
     * @param height the new height of this Widget
     * @param value the value to display in this NumberWidget
     * @param decimalChar the character to use as decimal delimiter
     * @param maxLength the maximum number of digits (inclusive sign, decimal-delim and fractions)
     * @param font the Font to be used for the text
     * @param color the color to be used
     */
    public NumberLabel(float width, float height, float value, char decimalChar, int maxLength, Font font, Colorf color) {
        this(width, height, 0, value, decimalChar, maxLength, font, color);
    }

    /**
     * Creates a new Label with the given width, height and z-index.
     * 
     * @param size the new size of this Widget
     * @param zIndex the z-index of this Widget
     * @param value the value to display in this NumberWidget
     * @param decimalChar the character to use as decimal delimiter
     * @param maxLength the maximum number of digits (inclusive sign, decimal-delim and fractions)
     * @param color the color to be used
     */
    public NumberLabel(Tuple2f size, int zIndex, float value, char decimalChar, int maxLength, Colorf color) {
        this(size.getX(), size.getY(), zIndex, value, decimalChar, maxLength, null, color, TextAlignment.TOP_LEFT);
    }

    /**
     * Creates a new Label with the given width, height and z-index.
     * 
     * @param width the new width of this Widget
     * @param height the new height of this Widget
     * @param zIndex the z-index of this Widget
     * @param value the value to display in this NumberWidget
     * @param decimalChar the character to use as decimal delimiter
     * @param maxLength the maximum number of digits (inclusive sign, decimal-delim and fractions)
     * @param color the color to be used
     */
    public NumberLabel(float width, float height, int zIndex, float value, char decimalChar, int maxLength, Colorf color) {
        this(width, height, zIndex, value, decimalChar, maxLength, null, color, TextAlignment.TOP_LEFT);
    }

    /**
     * Creates a new Label with the given width and height and a z-index of 0.
     * 
     * @param size the new size of this Widget
     * @param value the value to display in this NumberWidget
     * @param decimalChar the character to use as decimal delimiter
     * @param maxLength the maximum number of digits (inclusive sign, decimal-delim and fractions)
     * @param color the color to be used
     */
    public NumberLabel(Tuple2f size, float value, char decimalChar, int maxLength, Colorf color) {
        this(size.getX(), size.getY(), 0, value, decimalChar, maxLength, null, color);
    }

    /**
     * Creates a new Label with the given width and height and a z-index of 0.
     * 
     * @param width the new width of this Widget
     * @param height the new height of this Widget
     * @param value the value to display in this NumberWidget
     * @param decimalChar the character to use as decimal delimiter
     * @param maxLength the maximum number of digits (inclusive sign, decimal-delim and fractions)
     * @param color the color to be used
     */
    public NumberLabel(float width, float height, float value, char decimalChar, int maxLength, Colorf color) {
        this(width, height, 0, value, decimalChar, maxLength, null, color);
    }

    /**
     * Creates a new Label with the given width and height and a z-index of 0.
     * 
     * @param size the new size of this Widget
     * @param value the value to display in this NumberWidget
     * @param decimalChar the character to use as decimal delimiter
     * @param maxLength the maximum number of digits (inclusive sign, decimal-delim and fractions)
     * @param font the Font to be used for the text
     */
    public NumberLabel(Tuple2f size, float value, char decimalChar, int maxLength, Font font) {
        this(size.getX(), size.getY(), 0, value, decimalChar, maxLength, font, null);
    }

    /**
     * Creates a new Label with the given width and height and a z-index of 0.
     * 
     * @param width the new width of this Widget
     * @param height the new height of this Widget
     * @param value the value to display in this NumberWidget
     * @param decimalChar the character to use as decimal delimiter
     * @param maxLength the maximum number of digits (inclusive sign, decimal-delim and fractions)
     * @param font the Font to be used for the text
     */
    public NumberLabel(float width, float height, float value, char decimalChar, int maxLength, Font font) {
        this(width, height, 0, value, decimalChar, maxLength, font, null);
    }

    /**
     * Creates a new Label with the given width and height and a z-index of 0.
     * 
     * @param size the new size of this Widget
     * @param value the value to display in this NumberWidget
     * @param decimalChar the character to use as decimal delimiter
     * @param maxLength the maximum number of digits (inclusive sign, decimal-delim and fractions)
     * @param font the Font to be used for the text
     * @param alignment the horizontal and vertical alignment
     */
    public NumberLabel(Tuple2f size, float value, char decimalChar, int maxLength, Font font, TextAlignment alignment) {
        this(size.getX(), size.getY(), 0, value, decimalChar, maxLength, font, null, alignment);
    }

    /**
     * Creates a new Label with the given width and height and a z-index of 0.
     * 
     * @param width the new width of this Widget
     * @param height the new height of this Widget
     * @param value the value to display in this NumberWidget
     * @param decimalChar the character to use as decimal delimiter
     * @param maxLength the maximum number of digits (inclusive sign, decimal-delim and fractions)
     * @param font the Font to be used for the text
     * @param alignment the horizontal and vertical alignment
     */
    public NumberLabel(float width, float height, float value, char decimalChar, int maxLength, Font font, TextAlignment alignment) {
        this(width, height, 0, value, decimalChar, maxLength, font, null, alignment);
    }

    /**
     * Creates a new Label with the given width and height and no text initially.
     * 
     * @param size the new size of this Widget
     * @param value the value to display in this NumberWidget
     * @param decimalChar the character to use as decimal delimiter
     * @param maxLength the maximum number of digits (inclusive sign, decimal-delim and fractions)
     */
    public NumberLabel(Tuple2f size, float value, char decimalChar, int maxLength) {
        this(size.getX(), size.getY(), 0, value, decimalChar, maxLength, (Font) null, (Colorf) null);
    }

    /**
     * Creates a new Label with the given width and height and no text initially.
     * 
     * @param width the new width of this Widget
     * @param height the new height of this Widget
     * @param zIndex the z-index of this Widget
     * @param value the value to display in this NumberWidget
     * @param decimalChar the character to use as decimal delimiter
     * @param maxLength the maximum number of digits (inclusive sign, decimal-delim and fractions)
     */
    public NumberLabel(float width, float height, int zIndex, float value, char decimalChar, int maxLength) {
        this(width, height, zIndex, value, decimalChar, maxLength, (Font) null, (Colorf) null);
    }

    /**
     * Creates a new Label with the given width and height and no text initially.
     * 
     * @param width the new width of this Widget
     * @param height the new height of this Widget
     * @param value the value to display in this NumberWidget
     * @param decimalChar the character to use as decimal delimiter
     * @param maxLength the maximum number of digits (inclusive sign, decimal-delim and fractions)
     */
    public NumberLabel(float width, float height, float value, char decimalChar, int maxLength) {
        this(width, height, 0, value, decimalChar, maxLength);
    }
}
