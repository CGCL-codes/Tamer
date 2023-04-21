package uk.org.ogsadai.dqp.lqp.optimiser.project.pushdown.strategies;

import java.util.LinkedList;
import java.util.List;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.operators.ProjectOperator;
import uk.org.ogsadai.dqp.lqp.operators.UnionOperator;

/**
 * Strategy to push a PROJECT operator past a UNION operator.  We can only 
 * push past a UNION ALL operator otherwise the PROJECT will alter the duplicate
 * detection.
 *
 * @author The OGSA-DAI Project Team
 */
public class ProjectPushDownPastUnionStrategy extends ProjectPushDownCopyPastStrategy {

    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2009.";

    /** Logger. */
    private static final DAILogger LOG = DAILogger.getLogger(ProjectPushDownPastUnionStrategy.class);

    /**
     * {@inheritDoc}
     */
    public List<ProjectOperator> pushDown(ProjectOperator projectOperator) {
        Operator child = projectOperator.getChild(0);
        if (child instanceof UnionOperator) {
            UnionOperator union = (UnionOperator) child;
            if (LOG.isDebugEnabled()) {
                LOG.debug("Trying to push PROJECT down past UNION.  Bag Op = " + union.isBagOperator());
            }
            if (union.isBagOperator()) {
                return super.pushDown(projectOperator);
            } else {
                return new LinkedList<ProjectOperator>();
            }
        } else {
            return super.pushDown(projectOperator);
        }
    }
}
