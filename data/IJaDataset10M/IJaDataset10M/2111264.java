package net.entropysoft.dashboard.plugin.dashboard.model;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.gef.rulers.RulerProvider;

/**
 * Model object representing a ruler.
 * 
 */
public class DashboardRuler extends ModelObject {

    public static final String PROPERTY_CHILDREN = "children changed";

    public static final String PROPERTY_UNIT = "units changed";

    private int unit;

    private boolean horizontal;

    private List<DashboardGuide> guides = new ArrayList<DashboardGuide>();

    public DashboardRuler(boolean isHorizontal) {
        this(isHorizontal, RulerProvider.UNIT_PIXELS);
    }

    public DashboardRuler(boolean isHorizontal, int unit) {
        horizontal = isHorizontal;
        setUnit(unit);
    }

    /**
	 * add a guide to this ruler
	 * 
	 * @param guide
	 */
    public void addGuide(DashboardGuide guide) {
        if (!guides.contains(guide)) {
            guide.setHorizontal(!isHorizontal());
            guides.add(guide);
            firePropertyChange(PROPERTY_CHILDREN, null, guide);
        }
    }

    public List<DashboardGuide> getGuides() {
        return guides;
    }

    public int getUnit() {
        return unit;
    }

    public boolean isHidden() {
        return false;
    }

    public boolean isHorizontal() {
        return horizontal;
    }

    public void removeGuide(DashboardGuide guide) {
        if (guides.remove(guide)) {
            firePropertyChange(PROPERTY_CHILDREN, null, guide);
        }
    }

    public void setHidden(boolean isHidden) {
    }

    /**
	 * set the unit to use for the ruler
	 * 
	 * @param newUnit
	 *            one of {@link RulerProvider#UNIT_INCHES},
	 *            {@link RulerProvider#UNIT_CENTIMETERS},
	 *            {@link RulerProvider#UNIT_PIXELS}.
	 */
    public void setUnit(int newUnit) {
        if (unit != newUnit) {
            int oldUnit = unit;
            unit = newUnit;
            firePropertyChange(PROPERTY_UNIT, new Integer(oldUnit), new Integer(newUnit));
        }
    }
}
