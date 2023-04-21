package org.qedeq.kernel.bo.service.heuristic;

import org.qedeq.base.io.Parameters;
import org.qedeq.kernel.bo.common.PluginExecutor;
import org.qedeq.kernel.bo.logic.model.SixDynamicModel;
import org.qedeq.kernel.bo.module.KernelQedeqBo;
import org.qedeq.kernel.bo.module.PluginBo;

/**
 * Test if QEDEQ module formulas are valid within a model.
 *
 * @author  Michael Meyling
 */
public class HeuristicCheckerPlugin implements PluginBo {

    /** This class. */
    public static final Class CLASS = HeuristicCheckerPlugin.class;

    /**
     * Constructor.
     */
    public HeuristicCheckerPlugin() {
    }

    public String getPluginId() {
        return CLASS.getName();
    }

    public String getPluginActionName() {
        return "Heuristic tester";
    }

    public String getPluginDescription() {
        return "checks mathematical correctness by interpreting within a model";
    }

    public PluginExecutor createExecutor(final KernelQedeqBo qedeq, final Parameters parameters) {
        return new HeuristicCheckerExecutor(this, qedeq, parameters);
    }

    public void setDefaultValuesForEmptyPluginParameters(final Parameters parameters) {
        parameters.setDefault("model", SixDynamicModel.class.getName());
    }
}
