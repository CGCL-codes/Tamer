package org.slasoi.orcmockup.poc;

import org.slasoi.gslam.core.builder.PlanningOptimizationBuilder;
import org.slasoi.gslam.core.poc.PlanningOptimization;
import org.slasoi.orcmockup.poc.impl.PlanningOptimizationImpl;

public class PlanningOptimizationServices implements PlanningOptimizationBuilder {

    /**
     * This method exposes a PlanningOptimizationServices, so SkeletonSLAMBean (located in skl-main bundle) is able to
     * create in runtime a PlanningOptimization component and push it into the SLAManagerContext
     */
    public PlanningOptimization create() {
        return new PlanningOptimizationImpl();
    }
}
