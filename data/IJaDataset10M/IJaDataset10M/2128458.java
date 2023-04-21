package uk.org.ogsadai.dqp.lqp.optimiser.project.pushdown.strategies;

import java.util.LinkedList;
import java.util.List;
import uk.org.ogsadai.dqp.lqp.operators.ProjectOperator;
import uk.org.ogsadai.dqp.lqp.optimiser.project.pushdown.ProjectPushDownStrategy;

/**
 * No operation strategy - returns null, push down not allowed.
 *
 * @author The OGSA-DAI Project Team
 */
public class ProjectPushDownNoOpStrategy implements ProjectPushDownStrategy {

    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2009.";

    /**
     * {@inheritDoc}
     */
    public List<ProjectOperator> pushDown(ProjectOperator operator) {
        return new LinkedList<ProjectOperator>();
    }
}
