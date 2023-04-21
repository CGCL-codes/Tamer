package org.matsim.config.groups;

import java.util.LinkedList;
import java.util.List;
import org.matsim.config.Module;

/**
 * @author dgrether
 *
 */
public class WithindayConfigGroup extends Module {

    public static final String GROUP_NAME = "withinday";

    private static final String WITHINDAYITERATION = "withindayIteration";

    private static final String TRAFFICMANAGEMENTCONFIG = "trafficManagementConfiguration";

    private static final String REPLANNINGINTERVAL = "replanningInterval";

    private static final String AGENTVISIBILITYRANGE = "agentVisibilityRange";

    private static final String CONTENTTHRESHOLD = "contentThreshold";

    private int contentmentThreshold;

    private int agentVisibilityRange;

    private double replanningInterval;

    private String trafficManagementConfiguration;

    private List<Integer> withindayIterations;

    public WithindayConfigGroup(final String name) {
        super(name);
        this.withindayIterations = new LinkedList<Integer>();
    }

    @Override
    public String getValue(final String key) {
        throw new UnsupportedOperationException("This method is only implemented if compatibility with old code is needed, which is not the case for withinday replanning");
    }

    @Override
    public void addParam(final String key, final String value) {
        if (WITHINDAYITERATION.equals(key)) {
            this.withindayIterations.add(Integer.parseInt(value));
        } else if (TRAFFICMANAGEMENTCONFIG.equals(key)) {
            this.trafficManagementConfiguration = value.replace("\\", "/");
        } else if (REPLANNINGINTERVAL.equals(key)) {
            this.replanningInterval = Double.parseDouble(value);
        } else if (AGENTVISIBILITYRANGE.equals(key)) {
            this.agentVisibilityRange = Integer.parseInt(value);
        } else if (CONTENTTHRESHOLD.equals(key)) {
            this.contentmentThreshold = Integer.parseInt(value);
        } else {
            throw new IllegalArgumentException("The key : " + key + " is not supported by this config group");
        }
    }

    public List<Integer> getWithindayIterations() {
        return this.withindayIterations;
    }

    public String getTrafficManagementConfiguration() {
        return this.trafficManagementConfiguration;
    }

    /**
	 * The replanning interval specifies at which time the 
	 * agents are triggered to consider their contentment and
	 * eventually replan 
	 * @return A double according to the settings of the SimulationTimer
	 */
    public double getReplanningInterval() {
        return this.replanningInterval;
    }

    /**
	 * 
	 * @return The number of links an Agent is able to see
	 */
    public int getAgentVisibilityRange() {
        return this.agentVisibilityRange;
    }

    public double getContentmentThreshold() {
        return this.contentmentThreshold;
    }
}
