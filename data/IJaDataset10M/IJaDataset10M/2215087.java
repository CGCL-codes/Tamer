package gov.nasa.gsfc.visbard.model;

import gov.nasa.gsfc.visbard.model.ArrowManager;
import gov.nasa.gsfc.visbard.model.PickEvent;
import gov.nasa.gsfc.visbard.model.PickListener;
import gov.nasa.gsfc.visbard.repository.Repository;
import gov.nasa.gsfc.visbard.repository.category.Category;
import gov.nasa.gsfc.visbard.repository.category.CategoryListener;
import gov.nasa.gsfc.visbard.repository.category.PseudoCategory;
import gov.nasa.gsfc.visbard.repository.category.PseudoCategoryListener;
import gov.nasa.gsfc.visbard.util.Range;
import gov.nasa.gsfc.visbard.util.VectorUtils;
import gov.nasa.gsfc.visbard.util.VisbardException;
import gov.nasa.gsfc.visbard.vis.GlyphFactory;
import gov.nasa.gsfc.visbard.vis.OrbitFactory;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Vector;
import java.awt.Color;

/**
 * The dataset is an extetion of the dataset properties container. It
 * contains a personal set of dataset properties.
 */
public class DefaultDatasetMhd implements Dataset, PseudoCategoryListener, CategoryListener, PropertyContainerListener, RangeModelListener, ModeManagerListener {

    static org.apache.log4j.Category sLogger = org.apache.log4j.Category.getInstance(DefaultDataset.class.getName());

    private static final Color MISSING_COLOR_PROPERTY = new Color(0.5f, 0.5f, 0.5f);

    private static final double[] MISSING_LOCATION_PROPERTY = new double[] { 0, 0, 0 };

    private static final int MISSING_STYLE_PROPERTY = Glyph.STYLE_SPHERE;

    private static final float MISSING_SCALE_PROPERTY = 0;

    private static final float MISSING_WIDTH_PROPERTY = 1;

    private static final float[] MISSING_VECTOR_PROPERTY = new float[] { 0, 0, 0 };

    private static final float MISSING_SIZE_PROPERTY = 0;

    private static final float MISSING_ORB_DECIMATION_PROPERTY = 0f;

    private PropertyContainer fDefaultContainer = null;

    private PropertyContainer fContainer = null;

    private Repository fRep = null;

    private RangeModel fRangeModel = null;

    private HashMap fCatListen = new HashMap();

    private Range fCurRangeRow = new Range(0d, 0d);

    private Range fCurRangeTime = new Range(0d, 0d);

    private HashMap fRowToGlyph = new HashMap();

    private HashMap fGlyphToRow = new HashMap();

    private double fDelta = Double.MAX_VALUE;

    private int fNumGlyphs = 0;

    private LinkedList fFreeGlyphs = new LinkedList();

    private Universe fUniverse = null;

    private String fName = "N/A";

    private Vector fListeners = new Vector();

    private int fSelectedRow = -1;

    private Color fStaticColor = MISSING_COLOR_PROPERTY;

    public DefaultDatasetMhd(Repository rep, String name) {
        fName = name;
        fRep = rep;
    }

    /**
     * Change the static glyph color for this dataset. (The color to render
     * glyphs in case the static color property is set to true)
     */
    public void setStaticColor(Color col) {
        fStaticColor = col;
        this.updateProperty(Property.sStaticColor);
    }

    /**
     * Returns the static glyph color for this dataset.
     */
    public Color getStaticColor() {
        return fStaticColor;
    }

    /**
     * Attach a dataset to the universe.
     */
    public synchronized void attach(PropertyContainer container, RangeModel model, Universe uni) {
        this.detach();
        fContainer = new DefaultPropertyContainer(true);
        fRangeModel = model;
        fDefaultContainer = container;
        fUniverse = uni;
        this.setNumGlyphs(((Integer) getBasePropVal(Property.sNumGlyphs)).intValue());
        if (fDefaultContainer.allowsNulls()) {
            sLogger.warn("Default property container allows nulls! " + "May cause inconsistencies.");
        }
        Property[] props = fDefaultContainer.getAllProperties();
        for (int i = 0; i < props.length; i++) {
            this.updateProperty(props[i]);
        }
        this.setRange(fRangeModel.getSelectedRange());
        fDefaultContainer.addListener(this);
        fRangeModel.addListener(this);
        fContainer.addListener(this);
        VisbardMain.getModeManager().addListener(this);
    }

    /**
     * Returns the default property container of this dataset.
     */
    public synchronized PropertyContainer getDefaultPropertyContainer() {
        return fDefaultContainer;
    }

    /**
     * Detatch stop visualising this dataset.
     */
    public synchronized void detach() {
        if (fRangeModel != null) {
            fRangeModel.removeListener(this);
            fRangeModel = null;
        }
        if (fDefaultContainer != null) {
            fDefaultContainer.removeListener(this);
            for (Iterator i = fCatListen.keySet().iterator(); i.hasNext(); ) {
                Property prop = (Property) i.next();
                if (prop.getValueType().equals(Property.VAL_TYPE_CATEGORY)) {
                    this.removeListeners((Category) fCatListen.get(prop));
                }
            }
            fDefaultContainer = null;
        }
        if (fContainer != null) {
            fContainer.removeListener(this);
            fContainer.dispose();
            fContainer = null;
        }
        if (fUniverse != null) {
            ArrayList allGlyphs = new ArrayList();
            allGlyphs.addAll(fRowToGlyph.values());
            allGlyphs.addAll(fFreeGlyphs);
            for (Iterator i = allGlyphs.iterator(); i.hasNext(); ) {
                fUniverse.detach((Glyph) i.next());
            }
            fUniverse = null;
        }
        VisbardMain.getModeManager().removeListener(this);
    }

    /**
     * Returns the row address of the glyph. Returns -1 if glyph does not
     * belong to this dataset.
     */
    public synchronized int findGlyphRow(Glyph g) {
        Integer row = (Integer) fGlyphToRow.get(g);
        if (row != null) {
            return row.intValue();
        }
        return -1;
    }

    public synchronized Glyph findMiddleVisibleGlyph() {
        int currow = -1;
        int row = (int) fCurRangeRow.fStart + (int) Math.floor(fCurRangeRow.getExtent() / 2);
        if (fCurRangeRow.getExtent() == 0) return null;
        for (int i = row; i < fCurRangeRow.fStart + fCurRangeRow.getExtent(); i++) {
            Glyph g = (Glyph) fRowToGlyph.get(new Integer(i));
            if (g != null) return g;
        }
        return null;
    }

    /**
     * Chagnes the currently visualized range to the specified time range.
     */
    private void setRange(Range rng) {
        Range oldRangeRow = new Range(fCurRangeRow);
        Range oldRangeTime = new Range(fCurRangeTime);
        fCurRangeTime = rng;
        if (VisbardMain.getModeManager().isMode3D()) {
            if (rng.isInverted()) {
                this.setRangeRow(new Range(0d, 0d), false);
            } else {
                Category time = (Category) getBasePropVal(Property.sTime);
            }
            this.plotOrbit();
        }
    }

    /**
     * Returns the number of currently displayed glyphs
     */
    public int getNumGlyphsDisplayed() {
        return fRowToGlyph.keySet().size();
    }

    /**
     * Returns the currently displayed row range
     */
    public Range getRowRange() {
        return fCurRangeRow;
    }

    /**
     * Changes the currently visualized range.
     */
    private void setRangeRow(Range rng, boolean forgive) {
        Glyph glyph = null;
        double mapped, decPrcnt, newDelta, numGlyphs;
        int i, repsize;
        double newStartF, oldStartF, oldRowFrac, newRowFrac;
        int oldRow, newRow;
        int oldEnd, oldStart, oldExtent, newEnd, newStart, newExtent;
        LinkedList tempglyph = new LinkedList();
        float percent;
        repsize = fRep.getNumRows();
        newStart = (int) rng.fStart;
        newEnd = (int) rng.fEnd;
        newExtent = (int) rng.getExtent();
        oldStart = (int) fCurRangeRow.fStart;
        oldEnd = (int) fCurRangeRow.fEnd;
        oldExtent = (int) fCurRangeRow.getExtent();
        numGlyphs = (double) Math.min(fNumGlyphs, newExtent);
        Float dp = (Float) this.getBasePropVal(Property.sDecPercent);
        decPrcnt = (dp == null) ? 1 : dp.floatValue();
        mapped = (numGlyphs * decPrcnt);
        if (forgive == false) {
            newDelta = (mapped <= 0) ? Double.MAX_VALUE : newExtent / mapped;
        } else {
            newDelta = fDelta;
        }
        newStartF = newDelta;
        while (newStartF < newStart) newStartF += newDelta;
        oldStartF = fDelta;
        while (oldStartF < oldStart) oldStartF += fDelta;
        for (newRowFrac = newStartF, oldRowFrac = oldStartF; (int) Math.floor(oldRowFrac) < oldEnd; oldRowFrac += fDelta) {
            oldRow = (int) Math.floor(oldRowFrac);
            while ((oldRow > (int) Math.floor(newRowFrac)) && ((int) Math.floor(newRowFrac + newDelta) < newEnd)) {
                newRowFrac += newDelta;
            }
            if (oldRow != (int) Math.floor(newRowFrac)) {
                Object obj = fRowToGlyph.remove(new Integer(oldRow));
                fGlyphToRow.remove(obj);
                if (obj != null) tempglyph.add(obj); else sLogger.warn("Error deallocating glyph : " + oldRow);
            }
        }
        for (newRowFrac = newStartF; (int) Math.floor(newRowFrac) < newEnd; newRowFrac += newDelta) {
            newRow = (int) Math.floor(newRowFrac);
            if (!fRowToGlyph.containsKey(new Integer(newRow))) {
                if (!tempglyph.isEmpty()) {
                    glyph = (Glyph) tempglyph.removeLast();
                    if (fUniverse != null) {
                        this.loadGlyphMap(glyph, newRow);
                    }
                    fRowToGlyph.put(new Integer(newRow), glyph);
                    fGlyphToRow.put(glyph, new Integer(newRow));
                } else {
                    if (!fFreeGlyphs.isEmpty()) {
                        glyph = (Glyph) fFreeGlyphs.removeLast();
                        if (fUniverse != null) {
                            this.loadGlyphStatic(glyph);
                            this.loadGlyphMap(glyph, newRow);
                            glyph.setVisible(true);
                        }
                        fRowToGlyph.put(new Integer(newRow), glyph);
                        fGlyphToRow.put(glyph, new Integer(newRow));
                    } else {
                        sLogger.warn("Unable to load glyph: " + newRow + ".");
                    }
                }
            } else {
            }
        }
        while (!tempglyph.isEmpty()) {
            glyph = (Glyph) tempglyph.removeLast();
            glyph.setVisible(false);
            fFreeGlyphs.add(glyph);
        }
        fDelta = newDelta;
        fCurRangeRow = rng;
        this.fireEvent(DatasetListener.SELECTED_ROWRANGE_CHANGED);
    }

    /**
     * Allocates/deallocates the glyphs and resets the display.
     */
    private void setNumGlyphs(int num) {
        Glyph g;
        int i, totalToRemove, tot;
        if (num == fNumGlyphs) return;
        if (num > fNumGlyphs) {
            for (i = fNumGlyphs; i < num; i++) {
                g = GlyphFactory.getInstance().createGlyph(GlyphFactory.TYPE_SIMPLE);
                g.setVisible(false);
                if (fUniverse != null) {
                    fUniverse.attach(g);
                    this.loadGlyphStatic(g);
                }
                fFreeGlyphs.add(g);
            }
            fNumGlyphs = num;
            this.setRangeRow(fCurRangeRow, false);
        } else {
            Iterator it = fRowToGlyph.values().iterator();
            while (it.hasNext()) {
                g = (Glyph) it.next();
                g.setVisible(false);
                fFreeGlyphs.add(g);
            }
            fRowToGlyph.clear();
            fGlyphToRow.clear();
            Range cur = fCurRangeRow;
            fCurRangeRow = new Range(0d, 0d);
            fDelta = Double.MAX_VALUE;
            totalToRemove = fNumGlyphs - num;
            fNumGlyphs = num;
            this.setRangeRow(cur, false);
            for (i = 0; i < totalToRemove; i++) {
                g = (Glyph) fFreeGlyphs.remove(0);
                if (fUniverse != null) fUniverse.detach(g);
            }
        }
        System.gc();
    }

    /**
     * Obtain a normalized scalar from repository
     * for the specified row.
     */
    private float getNormalizedScalar(Category cat, int row) {
        float rv = Float.NaN;
        Range rng = cat.getRange();
        return rv;
    }

    /**
     * Obtain a normalized vector from repository for
     * the specified row.
     */
    private float[] getNormalizedVector(Category cat, int row) {
        float rv[] = null;
        return rv;
    }

    /**
     * Load the glyph with attributes that change from row to row.
     */
    private void loadGlyphMap(Glyph g, int row) {
        if (row == fSelectedRow) {
            g.setSelected(true);
        } else {
            g.setSelected(false);
        }
        Category cat = (Category) this.getBasePropVal(Property.sLocation);
        cat = (Category) getBasePropVal(Property.sMSize);
        if ((cat != null) && (fRep.isComputable(cat))) g.setMoldSize(this.getNormalizedScalar(cat, row)); else g.setMoldSize(MISSING_SIZE_PROPERTY);
        Boolean bol = (Boolean) getBasePropVal(Property.sStaticColor);
        if (bol.booleanValue() == false) {
            cat = (Category) getBasePropVal(Property.sMColor);
            if ((cat != null) && (fRep.isComputable(cat))) g.setMoldColor(cat.getColorPalette().getColor(this.getNormalizedScalar(cat, row))); else g.setMoldColor(MISSING_COLOR_PROPERTY);
        } else {
            g.setMoldColor(fStaticColor);
        }
        for (int i = 0; i < fUniverse.getArrowManager().getNumArrows(); i++) {
            cat = (Category) getBasePropVal(new Property(Property.A_VECTOR_MAP, i));
            if ((cat != null) && (fRep.isComputable(cat))) g.setArrowVector(i, this.getNormalizedVector(cat, row)); else g.setArrowVector(i, MISSING_VECTOR_PROPERTY);
            cat = (Category) getBasePropVal(new Property(Property.A_COLOR_MAP, i));
            if ((cat != null) && (fRep.isComputable(cat))) g.setArrowColor(i, cat.getColorPalette().getColor(this.getNormalizedScalar(cat, row))); else g.setArrowColor(i, MISSING_COLOR_PROPERTY);
        }
    }

    /**
     * Set the row of the selected glyph. Use -1 to unselect glyph.
     */
    private void setSelectedRow(int row) {
        if (row == fSelectedRow) return;
        Glyph g = (Glyph) fRowToGlyph.get(new Integer(fSelectedRow));
        if (g != null) {
            g.setSelected(false);
        }
        if (row != -1) {
            g = (Glyph) fRowToGlyph.get(new Integer(row));
            if (g != null) {
                g.setSelected(true);
            }
        }
        fSelectedRow = row;
        fireEvent(DatasetListener.SELECTED_ROW);
    }

    /**
     * Returns the currently selected row.
     */
    public int getSelectedRow() {
        return fSelectedRow;
    }

    /**
     * Load glyph with attributes that do not change from row to row.
     */
    private void loadGlyphStatic(Glyph g) {
        Integer style = (Integer) this.getBasePropVal(Property.sMStyle);
        if (style != null) g.setMoldStyle(style.intValue()); else g.setMoldStyle(MISSING_STYLE_PROPERTY);
        Float scalar = (Float) this.getBasePropVal(Property.sMScale);
        if (scalar != null) g.setMoldScale(scalar.floatValue()); else g.setMoldScale(MISSING_SCALE_PROPERTY);
        for (int i = 0; i < fUniverse.getArrowManager().getNumArrows(); i++) {
            scalar = (Float) this.getBasePropVal(new Property(Property.A_SCALE, i));
            if (scalar != null) g.setArrowScale(i, scalar.floatValue()); else g.setArrowScale(i, MISSING_SCALE_PROPERTY);
            scalar = (Float) this.getBasePropVal(new Property(Property.A_WIDTH, i));
            if (scalar != null) g.setArrowWidth(i, scalar.floatValue()); else g.setArrowWidth(i, MISSING_WIDTH_PROPERTY);
        }
    }

    /**
     * Returns the property container of the dataset.
     */
    public synchronized PropertyContainer getPropertyContainer() {
        return fContainer;
    }

    /**
     * Returns the name of the dataset.
     */
    public synchronized String getName() {
        return fName;
    }

    public String toString() {
        return fName;
    }

    public synchronized Repository getRepository() {
        return fRep;
    }

    private void addListeners(Category cat) {
        sLogger.debug("Now listening to " + cat + ".");
        cat.addListener(this);
        if (cat instanceof PseudoCategory) {
            ((PseudoCategory) cat).addListener((PseudoCategoryListener) this);
        }
    }

    private void removeListeners(Category cat) {
        sLogger.debug("No longer listening to " + cat + ".");
        cat.removeListener(this);
        if (cat instanceof PseudoCategory) {
            ((PseudoCategory) cat).removeListener((PseudoCategoryListener) this);
        }
    }

    /**
     * Updates listeners for the property.
     */
    private void updateListeners(Property p, Object newValue) {
        Object oldValue = fCatListen.get(p);
        if (oldValue != null) {
            if ((newValue != null) && (oldValue.equals(newValue))) return;
            fCatListen.remove(p);
            if (!fCatListen.containsValue(oldValue)) {
                this.removeListeners((Category) oldValue);
            }
        }
        if ((newValue != null) && (newValue instanceof Category)) {
            if (!fCatListen.containsValue(newValue)) {
                this.addListeners((Category) newValue);
            }
            fCatListen.put(p, newValue);
        }
    }

    /**
     * Property has changed.
     */
    private void updateProperty(Property p) {
        int aIntVal = 0, row;
        float aFloatVal = Float.NaN;
        Integer rowInt;
        Category cat = null;
        Glyph g;
        boolean static_color = false;
        Category colorCat = null;
        boolean colorRead = false;
        Object value = this.getBasePropVal(p);
        this.updateListeners(p, value);
        if (!VisbardMain.getModeManager().isMode3D()) return;
        switch(p.type()) {
            case Property.TIME_MAP:
                this.setRange(fCurRangeTime);
                return;
            case Property.DEC_PERCENT:
                this.setRange(fCurRangeTime);
                return;
            case Property.NUM_GLYPHS:
                if (value != null) this.setNumGlyphs(((Integer) value).intValue()); else this.setNumGlyphs(0);
                return;
            case Property.M_STYLE:
                if (value != null) aIntVal = ((Integer) value).intValue(); else aIntVal = Glyph.STYLE_SPHERE;
                break;
            case Property.STATIC_COLOR:
                if (value != null) static_color = ((Boolean) value).booleanValue(); else static_color = false;
                colorCat = (Category) this.getBasePropVal(Property.sMColor);
                colorRead = ((colorCat != null) && (fRep.isComputable(colorCat)));
                break;
            case Property.M_SCALE:
            case Property.A_WIDTH:
            case Property.A_SCALE:
                if (value != null) aFloatVal = ((Float) value).floatValue(); else aFloatVal = Float.NaN;
                break;
            case Property.M_COLOR_MAP:
                static_color = ((Boolean) this.getBasePropVal(Property.sStaticColor)).booleanValue();
                cat = (Category) value;
                break;
            case Property.M_SIZE_MAP:
            case Property.A_VECTOR_MAP:
            case Property.A_COLOR_MAP:
            case Property.LOC_MAP:
                cat = (Category) value;
                break;
            case Property.PLOT_ORBIT:
                return;
            case Property.ORB_COLOR_MAP:
                return;
            case Property.ORB_THICKNESS:
                return;
            case Property.VERTEX_DEC_PERCENT:
                float decVal;
                if (value != null) decVal = ((Float) value).floatValue(); else decVal = MISSING_ORB_DECIMATION_PROPERTY;
                return;
            default:
                sLogger.error("Unknown property!");
                break;
        }
        if (fUniverse == null) return;
        boolean read = ((cat != null) && (fRep.isComputable(cat)));
        Iterator rows = fRowToGlyph.keySet().iterator();
        while (rows.hasNext()) {
            rowInt = (Integer) rows.next();
            row = rowInt.intValue();
            g = (Glyph) fRowToGlyph.get(rowInt);
            switch(p.type()) {
                case Property.M_STYLE:
                    g.setMoldStyle(aIntVal);
                    break;
                case Property.M_SCALE:
                    g.setMoldScale(aFloatVal);
                    break;
                case Property.A_WIDTH:
                    g.setArrowWidth(p.arrowIdx(), aFloatVal);
                    break;
                case Property.A_SCALE:
                    g.setArrowScale(p.arrowIdx(), aFloatVal);
                    break;
                case Property.M_SIZE_MAP:
                    if (read) g.setMoldSize(this.getNormalizedScalar(cat, row)); else g.setMoldSize(MISSING_SIZE_PROPERTY);
                    break;
                case Property.STATIC_COLOR:
                    if (static_color == true) {
                        g.setMoldColor(fStaticColor);
                    } else {
                        if (colorRead) g.setMoldColor(colorCat.getColorPalette().getColor(this.getNormalizedScalar(colorCat, row))); else g.setMoldColor(MISSING_COLOR_PROPERTY);
                    }
                    break;
                case Property.M_COLOR_MAP:
                    if (static_color == false) {
                        if (read) g.setMoldColor(cat.getColorPalette().getColor(this.getNormalizedScalar(cat, row))); else g.setMoldColor(MISSING_COLOR_PROPERTY);
                    }
                    break;
                case Property.A_VECTOR_MAP:
                    if (read) g.setArrowVector(p.arrowIdx(), this.getNormalizedVector(cat, row)); else g.setArrowVector(p.arrowIdx(), MISSING_VECTOR_PROPERTY);
                    break;
                case Property.A_COLOR_MAP:
                    if (read) {
                        g.setArrowColor(p.arrowIdx(), cat.getColorPalette().getColor(this.getNormalizedScalar(cat, row)));
                    } else g.setArrowColor(p.arrowIdx(), MISSING_COLOR_PROPERTY);
                    break;
                case Property.LOC_MAP:
                    g.setLocation(MISSING_LOCATION_PROPERTY);
                    break;
                default:
                    sLogger.error("Unknown property!");
                    break;
            }
        }
    }

    private void plotOrbit() {
    }

    private boolean isVertexPairValid(double p0[], double p1[]) {
        return (!(Double.isNaN(p0[0]) || Double.isNaN(p0[1]) || Double.isNaN(p0[2]) || Double.isNaN(p1[0]) || Double.isNaN(p1[1]) || Double.isNaN(p1[2]) || (p0[0] == Float.MAX_VALUE) || (p0[1] == Float.MAX_VALUE) || (p0[2] == Float.MAX_VALUE) || (p1[0] == Float.MAX_VALUE) || (p1[1] == Float.MAX_VALUE) || (p1[2] == Float.MAX_VALUE)));
    }

    private void updateOrbitColor() {
    }

    /**
     * This method either returns our own property value, or (if we dont have
     * it) returns one from the default property container.
     */
    public synchronized Object getBasePropVal(Property prop) {
        if ((fContainer != null) && (fContainer.hasProperty(prop) == true)) return fContainer.getPropertyValue(prop); else {
            if (fDefaultContainer == null) return null; else return fDefaultContainer.getPropertyValue(prop);
        }
    }

    /**
     * Send an event to all the listeners.
     */
    private void fireEvent(int eventID) {
        Vector myVec = fListeners;
        int length = myVec.size();
        for (int i = 0; i < length; i++) {
            DatasetListener listener = (DatasetListener) myVec.get(i);
            listener.datasetChanged(this, eventID);
        }
    }

    /**
     * Registers a listener.
     */
    public synchronized void addListener(DatasetListener listener) {
        Vector myVec = (Vector) fListeners.clone();
        if (!myVec.contains(listener)) {
            myVec.add(listener);
            fListeners = myVec;
        }
    }

    /**
     * Unregisters a listener.
     */
    public synchronized void removeListener(DatasetListener listener) {
        Vector myVec = (Vector) fListeners.clone();
        myVec.remove(listener);
        fListeners = myVec;
    }

    /**
     * Indicates that the range controller changed.
     */
    public synchronized void rangeModelChanged(RangeModel source, int eventID) {
        if ((eventID == RangeModelListener.SELECTED_RANGE_CHANGED) || (eventID == RangeModelListener.ALL_CHANGED)) {
            this.setRange(source.getSelectedRange());
        }
    }

    /**
     * Indicates that the VALUE for a particular visual property has changed.
     * This call comes from the default property container when it changes
     * the default. We ignore the default if we have our own setting.
     */
    public synchronized void propertyChanged(PropertyContainer source, Property prop) {
        if (source == fContainer) {
            this.updateProperty(prop);
        } else {
            if (fContainer.hasProperty(prop) == false) this.updateProperty(prop);
        }
    }

    /**
     * An event that indicates that this pseudo category has changed how it
     * computes its values.
     */
    public synchronized void pseudoCategoryChanged(PseudoCategory source) {
        for (Iterator i = fCatListen.keySet().iterator(); i.hasNext(); ) {
            Property prop = (Property) i.next();
            Category cat = (Category) fCatListen.get(prop);
            if (cat instanceof PseudoCategory) {
                if (source.equals((PseudoCategory) cat)) {
                    this.updateProperty(prop);
                    this.plotOrbit();
                }
            }
        }
    }

    /**
     * An event indicating something about the category has changed.
     */
    public synchronized void categoryChanged(Category source, int eventID) {
        for (Iterator i = fCatListen.keySet().iterator(); i.hasNext(); ) {
            Property prop = (Property) i.next();
            Category cat = (Category) fCatListen.get(prop);
            if (source.equals((Category) cat)) {
                if ((eventID == CategoryListener.RANGE_CHANGED) && prop.equals(Property.sTime)) {
                    sLogger.debug("Ignored range change for " + cat + " because it is mapped to time.");
                } else {
                    if ((eventID == CategoryListener.PALETTE_CHANGED) && (!prop.isColorMap())) {
                        sLogger.debug("Ignored palette change for " + cat + " because it is not mapped to a color.");
                    } else {
                        this.updateProperty(prop);
                    }
                }
            }
        }
    }

    /**
     * Indicates that an arrow has been added. (to the end of the arrow list)
     */
    public void arrowAdded(ArrowManager source) {
    }

    /**
     * Indicates that an arrow at index idx has been removed
     */
    public void arrowRemoved(ArrowManager source, int idx) {
        for (int j = idx; j <= source.getNumArrows(); j++) {
            Property[] props = new Property[fCatListen.keySet().size()];
            fCatListen.keySet().toArray(props);
            for (int i = 0; i < props.length; i++) {
                if (props[i].arrowIdx() == j) {
                    Object val = fCatListen.remove(props[i]);
                    if ((j == idx) && (!fCatListen.containsValue(val))) {
                        this.removeListeners((Category) val);
                    }
                }
            }
            for (int i = 0; i < props.length; i++) {
                if (props[i].arrowIdx() == j + 1) {
                    Property newProp = new Property(props[i].type(), j);
                    fCatListen.put(newProp, fCatListen.get(props[i]));
                }
            }
        }
    }

    /**
     * Glyph has been picked, check if it needs to be selected.
     */
    public void picked(PickEvent event) {
        if (event.isGlyphSelect() == true) {
            if (event.getDataset() != this) {
                this.setSelectedRow(-1);
                return;
            }
            this.setSelectedRow(event.getRow());
        } else if (event.isDeselectEvent()) this.setSelectedRow(-1);
    }

    /**
     * Indicates a switch
     */
    public void modeChanged(ModeManager source) {
        if (source.isMode3D()) {
            sLogger.debug("Refreshing 3D view");
            Iterator it = fRowToGlyph.values().iterator();
            while (it.hasNext()) {
                Glyph g = (Glyph) it.next();
                g.setVisible(false);
                fFreeGlyphs.add(g);
            }
            fRowToGlyph.clear();
            fGlyphToRow.clear();
            fCurRangeRow = new Range(0d, 0d);
            this.setRange(fCurRangeTime);
        }
    }
}
