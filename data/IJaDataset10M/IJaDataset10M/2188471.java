package org.vikamine.app.rcp.control;

import org.vikamine.app.rcp.Activator;
import org.vikamine.kernel.statistics.StatisticComponent;

/**
 * The Class StatisticComponentPreferences.
 */
public class StatisticComponentPreferences {

    /** The Constant DATA_ANALYSIS. */
    public static final StatisticComponentPreferences DATA_ANALYSIS = new StatisticComponentPreferences("org.vikamine.dataAnalysis");

    /** The Constant CURRENT_SG. */
    public static final StatisticComponentPreferences CURRENT_SG = new StatisticComponentPreferences("org.vikamine.currentSG");

    /** The id. */
    private final String id;

    /**
     * Instantiates a new StatisticComponentPreferences.
     * 
     * @param id
     *            the id
     */
    public StatisticComponentPreferences(String id) {
        super();
        this.id = id;
    }

    /**
     * Gets the id.
     * 
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Checks if it is enabled.
     * 
     * @param statisticComponent
     *            the statisticComponent
     * @return true, if is enabled
     */
    public boolean isEnabled(StatisticComponent statisticComponent) {
        return Activator.getDefault().getPreferenceStore().getBoolean(getIdStringForStatComponent(statisticComponent));
    }

    /**
     * Gets the id string for stat component.
     * 
     * @param statComponent
     *            the stat component
     * @return the id string for stat component
     */
    public String getIdStringForStatComponent(StatisticComponent statisticComponent) {
        return id + "." + statisticComponent.getDescription();
    }
}
