package com.hp.hpl.jena.query.darq.engine.optimizer;

import java.util.List;
import com.hp.hpl.jena.query.darq.core.ServiceGroup;

public interface PlanOptimizer {

    public static final double PLAN_UNFEASIBLE_SELECTIVITY = -154;

    public static final double PLAN_UNFEASIBLE_RESULTS = Double.MAX_VALUE;

    public List<ServiceGroup> getCheapestPlan(List<ServiceGroup> servicegroups) throws PlanUnfeasibleException;

    public double getCosts();
}
