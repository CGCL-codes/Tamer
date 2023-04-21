package uk.org.ogsadai.dqp.lqp.optimiser.project.strategies;

import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.dqp.lqp.exceptions.LQPException;
import uk.org.ogsadai.dqp.lqp.operators.ProjectOperator;
import uk.org.ogsadai.dqp.lqp.optimiser.project.ProjectPullUpStrategy;

/**
 * Pull PROJECT pass unary operator provided that no derived attributes are
 * used.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class ProjectPullPastUnaryNonDerivedStrategy implements ProjectPullUpStrategy {

    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2008";

    /** Logger. */
    private static final DAILogger LOG = DAILogger.getLogger(ProjectPullPastUnaryNonDerivedStrategy.class);

    /**
     * {@inheritDoc}
     */
    public ProjectOperator pullUp(ProjectOperator operator) {
        ProjectOperator passOperator = null;
        if (ProjectStrategyUtils.parentUsesDerivedAttributes(operator)) {
            return null;
        }
        try {
            passOperator = ProjectStrategyUtils.pullPastNoChange(operator);
        } catch (LQPException e) {
            LOG.debug(e.getMessage());
            passOperator = null;
        }
        return passOperator;
    }
}
