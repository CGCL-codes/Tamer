package de.schwarzrot.dvd.theme.domain;

import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import de.schwarzrot.app.support.ApplicationServiceProvider;
import de.schwarzrot.data.ChildEntity;
import de.schwarzrot.data.meta.SortInfo;
import de.schwarzrot.data.support.AbstractEntity;
import de.schwarzrot.dvd.theme.Theme;
import de.schwarzrot.dvd.theme.domain.data.MenueElementCategory;
import de.schwarzrot.dvd.theme.domain.data.MenueElementType;
import de.schwarzrot.dvd.theme.domain.data.MenuePageType;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.ImageObserver;
import org.springframework.context.MessageSource;

/**
 * base class for menue elements of a DVD menue template. This {@code Entity} is also a virtual one,
 * but opposed to {@code ThemeBase}, it's type range is not open. The final class of {@code
 * ThemeElement}s is bound to the {@code MenueElementType}. To support overloading of {@code
 * ThemeElement}s, the variantTypeMap is setup on {@code Theme} activation - {@code MenueDefinition}
 * cares for that.
 * <p>
 * Each {@code Theme} has the variantTypeMap for the {@code ThemeElement}s, so on {@code Theme}
 * activation the variantTypeMap from {@code Theme} is set as variantTypeMap of {@code ThemeElement}
 * s.
 * <p>
 * Like {@code ThemeBase} this class is also an abstract class, which cannot be really abstract. So
 * the most vital function {@code drawItem} just throws an exception.
 * <p>
 * Each access to a skinned property uses {@code getSkin}, which asks the parent ({@code Theme}) for
 * the current active {@code Skin}.
 * <p>
 * Painting of {@code ThemeElement}s works that way, that this class offers independant space to
 * draw into. That space will be moved and rotated as specified by the theme property values. Each
 * child draws it's content at 0/0.
 * <p>
 * This class is the "bean", which means, that derived classes most commonly change painting or just
 * the type of the ThemeElement. {@code type} should be considered final - this value will be set by
 * constructors only. As the anonymous class creation needs an constructor without args, we can't
 * make it really final.
 * 
 * @author <a href="mailto:rmantey@users.sourceforge.net">Reinhard Mantey</a>
 * 
 * @param <T>
 *            type of parent {@code Theme}
 */
public class ThemeElement<T extends Theme> extends AbstractEntity implements ChildEntity<T>, Shape, ImageObserver {

    public enum AdJust {

        LEFT, CENTER, RIGHT
    }

    public static final String FLD_POS_X = "posX";

    public static final String FLD_POS_Y = "posY";

    public static final String FLD_WIDTH = "width";

    public static final String FLD_HEIGHT = "height";

    public static final String FLD_ADJUST = "adjust";

    public static final String FLD_DEGREES = "degrees";

    public static final String FLD_IMAGE_SCALE = "imageScale";

    private static final String PERSISTENT_NAME = "pageelem";

    private static final String VARIANT_COLNAME = "type";

    private static final long serialVersionUID = 713L;

    private static MessageSource msgSource;

    private static Map<Object, String> variantTypeMap;

    private T parent;

    private MenuePageType pageType;

    private MenueElementCategory category;

    private MenueElementType type;

    private String text;

    private Rectangle geometry;

    private int weight;

    private int posX;

    private int posY;

    private int width;

    private int height;

    private double angle;

    private File imageName;

    private double imageScale;

    private AdJust adjust;

    public ThemeElement() {
        init();
    }

    protected ThemeElement(T theme, MenuePageType pageType, MenueElementType type, MenueElementCategory cat, Rectangle r) {
        init();
        this.type = type;
        this.pageType = pageType;
        this.category = cat;
        setParent(theme);
        setGeometry(r);
    }

    public ThemeElement(ThemeElement<T> other) {
        parent = other.parent;
        pageType = other.pageType;
        category = other.category;
        type = other.type;
        text = new String(other.text);
        geometry = (Rectangle) getGeometry().clone();
        weight = other.weight;
        posX = other.posX;
        posY = other.posY;
        width = other.width;
        height = other.height;
        angle = other.angle;
        if (imageName != null) imageName = new File(other.imageName.getAbsolutePath());
        imageScale = other.imageScale;
        adjust = other.adjust;
        setDirty(true);
    }

    public void setup(AssemblingDefaultsProvider adp) {
        switch(type) {
            case TITLE_ITEM:
                setText(adp.getDefTitle());
                break;
            case HEADER_ITEM:
                setText(adp.getDefHeader());
                break;
            case ITEM:
                setText(adp.getDefItemText());
                break;
            case DESCRIPTION:
                setText(adp.getDefDescription());
                break;
            case JOB_IMAGE:
                setText(adp.getDefJobImage().getAbsolutePath());
                break;
            case REC_IMAGE:
                setText(adp.getDefRecImage().getAbsolutePath());
                break;
        }
    }

    public boolean contains(double x, double y) {
        return getGeometry().contains(x, y);
    }

    public boolean contains(double x, double y, double w, double h) {
        return getGeometry().contains(x, y, w, h);
    }

    public boolean contains(Point2D p) {
        return getGeometry().contains(p);
    }

    public boolean contains(Rectangle2D r) {
        return getGeometry().contains(r);
    }

    public final AdJust getAdjust() {
        return adjust;
    }

    public final double getAngle() {
        return angle;
    }

    public Rectangle getBounds() {
        return getGeometry().getBounds();
    }

    public Rectangle2D getBounds2D() {
        return getGeometry().getBounds2D();
    }

    public MenueElementCategory getCategory() {
        return category;
    }

    @Override
    public List<SortInfo> getDefaultOrder() {
        List<SortInfo> order = new ArrayList<SortInfo>();
        order.add(new SortInfo(PARENT_ATTR_NAME));
        order.add(new SortInfo("pageType"));
        order.add(new SortInfo("weight"));
        return order;
    }

    public final int getDegrees() {
        return (int) Math.toDegrees(angle);
    }

    public Rectangle getGeometry() {
        if (geometry == null || geometry.x != getPosX() || geometry.y != getPosY() || geometry.width != getWidth() || geometry.height != getHeight()) geometry = new Rectangle(getPosX(), getPosY(), getWidth(), getHeight());
        return geometry;
    }

    public final int getHeight() {
        return height;
    }

    public final File getImageName() {
        return imageName;
    }

    public final double getImageScale() {
        return imageScale;
    }

    @Override
    public Map<String, String> getMappings() {
        Map<String, String> mappings = super.getMappings();
        mappings.put(PARENT_ATTR_NAME, "themeid");
        mappings.put("type", "elemtype");
        mappings.put("imageName", "image");
        return mappings;
    }

    @Override
    public Map<String, Integer> getSkipList() {
        Map<String, Integer> skipList = super.getSkipList();
        skipList.put("text", 1);
        skipList.put("font", 1);
        skipList.put("active", 1);
        skipList.put("geometry", 1);
        skipList.put("degrees", 1);
        skipList.put("selected", 1);
        skipList.put("category", 1);
        skipList.put("orderVisible", 1);
        skipList.put("image", 1);
        return skipList;
    }

    public final MenuePageType getPageType() {
        return pageType;
    }

    public final T getParent() {
        return parent;
    }

    public Class<T> getParentType() {
        return null;
    }

    public PathIterator getPathIterator(AffineTransform at) {
        return getGeometry().getPathIterator(at);
    }

    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return getGeometry().getPathIterator(at, flatness);
    }

    @Override
    public final String getPersistenceName() {
        return PERSISTENT_NAME;
    }

    public final int getPosX() {
        return posX;
    }

    public final int getPosY() {
        return posY;
    }

    public final String getText() {
        return text;
    }

    public final MenueElementType getType() {
        return type;
    }

    @Override
    public List<String> getUniqColumnNames() {
        List<String> rv = new ArrayList<String>();
        rv.add(PARENT_ATTR_NAME);
        rv.add("pageType");
        rv.add("weight");
        return rv;
    }

    @Override
    public final String getVariantColumnName() {
        return VARIANT_COLNAME;
    }

    @Override
    public final Map<Object, String> getVariantTypeMap() {
        return variantTypeMap;
    }

    public final int getWeight() {
        return weight;
    }

    public final int getWidth() {
        return width;
    }

    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
        return false;
    }

    public boolean intersects(double x, double y, double w, double h) {
        return getGeometry().intersects(x, y, w, h);
    }

    public boolean intersects(Rectangle2D r) {
        return getGeometry().intersects(r);
    }

    @Override
    public final boolean isVirtual() {
        return true;
    }

    public final void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        performDrawing(g2);
    }

    public void refresh() {
    }

    @Override
    public final void setDirty(boolean dirty) {
        super.setDirty(dirty);
        if (parent != null && dirty) parent.setDirty(true);
    }

    public final void setAdjust(AdJust adjust) {
        AdJust ov = this.adjust;
        this.adjust = adjust;
        if (ov != null) {
            if (adjust == null || !ov.equals(adjust)) setDirty(true);
        } else if (ov != adjust) setDirty(true);
        PropertyChangeEvent pce = new PropertyChangeEvent(this, "adjust", ov, adjust);
        this.firePropertyChange(pce);
    }

    public final void setAngle(double angle) {
        Double ov = this.angle;
        this.angle = angle;
        if (!ov.equals(angle)) setDirty(true);
        PropertyChangeEvent pce = new PropertyChangeEvent(this, "angle", ov, angle);
        this.firePropertyChange(pce);
    }

    public void setCategory(MenueElementCategory category) {
        MenueElementCategory ov = this.category;
        this.category = category;
        if (ov != null) {
            if (category == null || !ov.equals(category)) setDirty(true);
        } else if (ov != category) setDirty(true);
        PropertyChangeEvent pce = new PropertyChangeEvent(this, "category", ov, category);
        this.firePropertyChange(pce);
    }

    public final void setDegrees(int deg) {
        Double ov = this.angle;
        angle = Math.toRadians(deg);
        if (!ov.equals(angle)) setDirty(true);
        PropertyChangeEvent pce = new PropertyChangeEvent(this, "angle", ov, angle);
        this.firePropertyChange(pce);
    }

    public void setGeometry(Rectangle r) {
        this.geometry = r;
        setPosX(r.x);
        setPosY(r.y);
        setWidth(r.width);
        setHeight(r.height);
    }

    public final void setHeight(int height) {
        Integer ov = this.height;
        this.height = height;
        geometry.height = height;
        if (!ov.equals(height)) setDirty(true);
        PropertyChangeEvent pce = new PropertyChangeEvent(this, "height", ov, height);
        this.firePropertyChange(pce);
    }

    public final void setImageName(File imageName) {
        File ov = this.imageName;
        this.imageName = imageName;
        if (ov != null) {
            if (imageName == null || ov.compareTo(imageName) != 0) setDirty(true);
        } else if (ov != imageName) setDirty(true);
        PropertyChangeEvent pce = new PropertyChangeEvent(this, "imageName", ov, imageName);
        this.firePropertyChange(pce);
    }

    public final void setImageScale(double imageScale) {
        Double ov = this.imageScale;
        this.imageScale = imageScale;
        if (!ov.equals(imageScale)) setDirty(true);
        PropertyChangeEvent pce = new PropertyChangeEvent(this, "imageScale", ov, imageScale);
        this.firePropertyChange(pce);
    }

    public final void setPageType(MenuePageType pageType) {
        this.pageType = pageType;
    }

    public final void setParent(T parent) {
        this.parent = parent;
    }

    public final void setPosX(int posX) {
        Integer ov = this.posX;
        this.posX = posX;
        geometry.x = posX;
        if (!ov.equals(posX)) setDirty(true);
        PropertyChangeEvent pce = new PropertyChangeEvent(this, "posX", ov, posX);
        this.firePropertyChange(pce);
    }

    public final void setPosY(int posY) {
        Integer ov = this.posY;
        this.posY = posY;
        geometry.y = posY;
        if (!ov.equals(posY)) setDirty(true);
        PropertyChangeEvent pce = new PropertyChangeEvent(this, "posY", ov, posY);
        this.firePropertyChange(pce);
    }

    public final void setText(String text) {
        if (msgSource == null) msgSource = (MessageSource) ApplicationServiceProvider.getService(MessageSource.class);
        if (msgSource != null) this.text = msgSource.getMessage(text, null, text, null); else this.text = text;
    }

    public final void setType(MenueElementType type) {
        this.type = type;
    }

    public final void setWeight(int weight) {
        Integer ov = this.weight;
        this.weight = weight;
        if (!ov.equals(weight)) setDirty(true);
        PropertyChangeEvent pce = new PropertyChangeEvent(this, "weight", ov, weight);
        this.firePropertyChange(pce);
    }

    public final void setWidth(int width) {
        Integer ov = this.width;
        this.width = width;
        geometry.width = width;
        if (!ov.equals(width)) setDirty(true);
        PropertyChangeEvent pce = new PropertyChangeEvent(this, "width", ov, width);
        this.firePropertyChange(pce);
    }

    protected void drawItem(Graphics2D g2) {
        throw new UnsupportedOperationException("need to implement drawItem() for class " + getClass().getName());
    }

    protected final ElementSkin getSkin() {
        if (parent != null && parent.getSkin() != null) return parent.getSkin().getSkin4Element(pageType, getCategory());
        return null;
    }

    protected void init() {
        pageType = MenuePageType.MAIN_MENUE;
        category = MenueElementCategory.ITEM;
        type = MenueElementType.ITEM;
        geometry = new Rectangle();
        parent = null;
        text = "";
        imageName = null;
        adjust = AdJust.LEFT;
        imageScale = 1.0;
    }

    protected final void performDrawing(Graphics2D g2) {
        g2.translate(getPosX(), getPosY());
        g2.rotate(getAngle());
        drawItem(g2);
        g2.rotate(-getAngle());
        g2.translate(-getPosX(), -getPosY());
    }

    public static void setVariantTypeMap(Map<Object, String> variantTypeMap) {
        ThemeElement.variantTypeMap = variantTypeMap;
    }
}
