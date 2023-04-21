package com.iver.cit.gvsig.fmap.layers;

import java.awt.Dimension;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Vector;
import org.gvsig.remoteClient.wms.WMSStyle;
import com.iver.andami.messages.NotificationManager;

/**
 * Class defining the node of the layer tree of a common WMS service.
 * @author jaume
 *
 */
public class WMSLayerNode {

    private String _name;

    private String _title;

    private Vector srs;

    private boolean queryable;

    private boolean transparency;

    private String lAbstract;

    private String latLonBox;

    private int selectedStyleIndex = 0;

    private ArrayList styles = new ArrayList();

    private ArrayList dimensions;

    private ArrayList keywords;

    private ArrayList children = new ArrayList();

    private WMSLayerNode _parent;

    private Dimension fixedSize;

    /**
     * <p>min scale for the layer to be visible</p>
     */
    private double scaleMin = -1;

    /**
     * <p>max scale for the layer to be visible</p>
     */
    private double scaleMax = -1;

    /**
     * @return Returns the name.
     */
    public String getName() {
        return _name;
    }

    /**
     * @return
     */
    public ArrayList getChildren() {
        return children;
    }

    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this._name = name;
    }

    /**
     * @return Returns the namedStyles.
     */
    public ArrayList getStyles() {
        return styles;
    }

    public ArrayList getKeywords() {
        return keywords;
    }

    /**
     * @return Returns the queryable.
     */
    public boolean isQueryable() {
        return queryable;
    }

    /**
     * @param queryable The queryable to set.
     */
    public void setQueryable(boolean queryable) {
        this.queryable = queryable;
    }

    /**
     * @return Returns the srs.
     */
    public Vector getAllSrs() {
        if ((srs.size() == 0) && _parent != null) return _parent.getAllSrs();
        return srs;
    }

    /**
     * @param srs The srs to set.
     */
    public void setSrs(Vector srs) {
        this.srs = srs;
    }

    /**
     * @return Returns the title.
     */
    public String getTitle() {
        return _title;
    }

    /**
     * @param title The title to set.
     */
    public void setTitle(String title) {
        this._title = title.trim();
    }

    /**
     * @return Returns the transparency.
     */
    public boolean isTransparent() {
        return transparency;
    }

    /**
     * @param transparency The transparency to set.
     */
    public void setTransparency(boolean transparency) {
        this.transparency = transparency;
    }

    /**
     * Sets the list of sons of this layer.
     * @param children
     */
    public void setChildren(ArrayList children) {
        this.children = children;
    }

    /**
     * returns the layer whose this is son of.
     * @return
     */
    public WMSLayerNode getParent() {
        return _parent;
    }

    /**
     * @param parentNode
     */
    public void setParent(WMSLayerNode parentNode) {
        this._parent = parentNode;
    }

    public ArrayList getDimensions() {
        return dimensions;
    }

    /**
     * Gets the layer abstract.
     *
     * @return Returns the abstract.
     */
    public String getAbstract() {
        return lAbstract;
    }

    /**
     * Sets the layer abstract.
     *
     * @param abstract The abstract to set.
     */
    public void setAbstract(String _abstract) {
        lAbstract = _abstract;
    }

    /**
     * <p>gets the maximum scale for this layer</p>
     * @return
     */
    public double getScaleMax() {
        return scaleMax;
    }

    /**
     * <p>gets the minimum scale for this layer</p>
     * @return
     */
    public double getScaleMin() {
        return scaleMin;
    }

    /**
     * <p>sets the minimum scale for this layer to be visible.</p>
     *
     * @param scale
     */
    public void setScaleMin(double scale) {
        scaleMin = scale;
    }

    /**
     * <p>sets the maximum scale for this layer to be visible</p>
     * @param scale
     */
    public void setScaleMax(double scale) {
        scaleMax = scale;
    }

    /**
     * @param name
     * @param units
     * @param unitSymbol
     * @param dimensionExpression
     */
    public void addDimension(String name, String units, String unitSymbol, String dimExpression) {
        if (dimensions == null) dimensions = new ArrayList();
        if (name.equalsIgnoreCase("time")) {
            try {
                dimensions.add(new TimeDimension(units, unitSymbol, dimExpression));
            } catch (IllegalArgumentException e) {
                dimensions.add(new DefaultDimension(name.toUpperCase(), units, unitSymbol, dimExpression));
            }
        } else if (name.equalsIgnoreCase("sequence")) {
            return;
        } else {
            dimensions.add(new DefaultDimension(name.toUpperCase(), units, unitSymbol, dimExpression));
        }
    }

    /**
     * Sets the Latitude-Longitude box text to be shown in an user interface layer descriptor.
     * @param latLonBox
     */
    public void setLatLonBox(String _latLonBox) {
        latLonBox = _latLonBox;
    }

    /**
     * Returns the Latitude-Longitude box text to be shown in an user interface layer descriptor.
     * @return
     */
    public String getLatLonBox() {
        return latLonBox;
    }

    /**
     * When a server cannot renderize images but just server them in constant size and
     * BBox, the layer must have this value set in order to correctly work.
     *
     * @param fixedWidth - the constant value for the image width
     * @param fixedHeight - the constant value for the image height
     */
    public void setFixedSize(int fixedWidth, int fixedHeight) {
        fixedSize = new Dimension(fixedWidth, fixedHeight);
    }

    /**
     * Returns the size of this layer (which is constant-sized)
     * @return
     */
    public Dimension getFixedSize() {
        return fixedSize;
    }

    /**
	 * Tells whether the layer is constant-sized or not.
	 * @return boolean
	 */
    public boolean isSizeFixed() {
        return fixedSize != null && fixedSize.getWidth() > 0 && fixedSize.getHeight() > 0;
    }

    /**
     *
     * @param _name
     * @param _title
     * @param _abstract
     */
    public void addStyle(WMSStyle style) {
        if (style.getName().equalsIgnoreCase("default")) selectedStyleIndex = styles.size();
        if (styles == null) styles = new ArrayList();
        styles.add(new FMapWMSStyle(style, this));
    }

    /**
     * Returns the style marked as selected or null if none.
     * @return FMapWMSStyle
     */
    public FMapWMSStyle getSelectedStyle() {
        if (styles == null || selectedStyleIndex > styles.size() - 1 || selectedStyleIndex == -1) return null;
        return (FMapWMSStyle) styles.get(selectedStyleIndex);
    }

    /**
     * Marks the style of this layer given by the index as selected
     * @param inex of the style
     */
    public void setSelectedStyleByIndex(int index) {
        selectedStyleIndex = index;
    }

    /**
     * Marks the style of this layer given by its name as selected. If
     * this layer has no style with this name, then the layer is marked
     * as none selected.
     * @param style name
     */
    public void setSelectedStyleByName(String styName) {
        if (styName == null || styName.equals("")) setSelectedStyleByIndex(-1);
        for (int i = 0; i < styles.size(); i++) {
            FMapWMSStyle sty = (FMapWMSStyle) styles.get(i);
            if (sty.name.equals(styName)) {
                setSelectedStyleByIndex(i);
                return;
            }
        }
        setSelectedStyleByIndex(-1);
    }

    public void addKeyword(String keyword) {
        if (keywords == null) keywords = new ArrayList();
        keywords.add(keyword);
    }

    public String toString() {
        String str;
        if (getName() == null) str = getTitle(); else str = "[" + getName() + "] " + getTitle();
        return str;
    }

    /**
     * Creates a new instance of WMSLayerNode containing a copy of this,
     * but with no children and parent set.
     */
    public Object clone() {
        WMSLayerNode clone = new WMSLayerNode();
        clone._name = this._name;
        clone.queryable = this.queryable;
        clone.srs = this.srs;
        clone._title = this._title;
        clone.transparency = this.transparency;
        clone.styles = new ArrayList();
        clone.lAbstract = this.lAbstract;
        clone.latLonBox = this.latLonBox;
        clone.selectedStyleIndex = this.selectedStyleIndex;
        if (keywords != null) {
            clone.keywords = new ArrayList(keywords.size());
            for (int i = 0; i < keywords.size(); i++) {
                clone.keywords.add((String) keywords.get(i));
            }
        }
        if (styles != null) for (int i = 0; i < styles.size(); i++) {
            FMapWMSStyle sty = (FMapWMSStyle) ((FMapWMSStyle) this.styles.get(i)).clone();
            sty.parent = this;
            clone.styles.add(sty);
        }
        if (dimensions != null) for (int i = 0; i < dimensions.size(); i++) {
            clone.dimensions = new ArrayList();
            clone.dimensions.add((IFMapWMSDimension) this.dimensions.get(i));
        }
        return clone;
    }

    /**
     * Just a C-struct-like class.
     * @author jaume
     *
     */
    public class FMapWMSStyle {

        public String name;

        public String title;

        public String styleAbstract;

        public String format;

        public String type;

        public String href;

        public WMSLayerNode parent;

        public int legendHeight;

        public int legendWidth;

        /**
         * Creates a new instance of FMapWMSStyle
         * @param name
         * @param title
         * @param styleAbstract
         * @param parent
         */
        public FMapWMSStyle(WMSStyle style, WMSLayerNode parent) {
            this.name = style.getName();
            this.title = style.getTitle();
            this.styleAbstract = style.getAbstract();
            this.legendWidth = style.getLegendURLWidth();
            this.legendHeight = style.getLegendURLHeight();
            this.format = style.getLegendURLFormat();
            this.href = style.getLegendURLOnlineResourceHRef();
            this.type = style.getLegendURLOnlineResourceType();
            this.parent = parent;
        }

        public FMapWMSStyle() {
        }

        public String toString() {
            return name;
        }

        public Object clone() {
            FMapWMSStyle clone = new FMapWMSStyle();
            Field[] fields = FMapWMSStyle.class.getFields();
            for (int i = 0; i < fields.length; i++) {
                try {
                    Class clazz = getClass();
                    String fieldName = fields[i].getName();
                    if (fields[i].getType().equals(Integer.class)) {
                        clazz.getField(fieldName).setInt(clone, clazz.getField(fieldName).getInt(this));
                    } else if (fields[i].getType().equals(Double.class)) {
                        clazz.getField(fieldName).setDouble(clone, clazz.getField(fieldName).getDouble(this));
                    } else {
                        clazz.getField(fieldName).set(clone, clazz.getField(fieldName).get(this));
                    }
                } catch (NullPointerException e) {
                    NotificationManager.addWarning("Cloning " + "'" + fields[i].getName() + "' field is null" + "(FMapWMSStyle: " + name + ")", e);
                } catch (Exception e) {
                    NotificationManager.addWarning("Reflect error when cloning " + "'" + fields[i].getName() + "' field " + "(FMapWMSStyle)", e);
                }
            }
            return clone;
        }
    }
}
