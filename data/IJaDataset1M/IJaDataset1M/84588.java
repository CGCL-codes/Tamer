package org.qedeq.kernel.bo.service.logic;

import java.util.Map;
import org.qedeq.kernel.bo.common.PluginExecutor;
import org.qedeq.kernel.bo.module.KernelQedeqBo;
import org.qedeq.kernel.bo.module.PluginBo;

/**
 * Checks if all formulas of a QEDEQ module are well formed.
 *
 * @author  Michael Meyling
 */
public final class QedeqBoFormalLogicCheckerPlugin implements PluginBo {

    /** This class. */
    private static final Class CLASS = QedeqBoFormalLogicCheckerPlugin.class;

    public String getPluginId() {
        return CLASS.getName();
    }

    public String getPluginName() {
        return "Verifier";
    }

    public String getPluginDescription() {
        return "checks mathematical correctness";
    }

    public PluginExecutor createExecutor(final KernelQedeqBo qedeq, final Map parameters) {
        return new QedeqBoFormalLogicCheckerExecutor(this, qedeq, parameters);
    }
}
