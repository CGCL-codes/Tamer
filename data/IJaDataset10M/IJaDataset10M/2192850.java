package jhomenet.commons.responsive.exec;

import java.util.List;
import org.apache.log4j.Logger;
import jhomenet.commons.GeneralApplicationContext;
import jhomenet.commons.responsive.ResponsiveException;
import jhomenet.commons.responsive.plan.Plan;
import jhomenet.commons.responsive.plan.ResponseType;
import jhomenet.commons.responsive.response.Response;

/**
 * TODO: Class description.
 *
 * @author Dave Irwin (jhomenet at gmail dot com)
 */
public abstract class AbstractResponsiveEvaluator implements ResponsiveEvaluator {

    /**
	 * Define a logging mechanism.
	 */
    private static final Logger logger = Logger.getLogger(AbstractResponsiveEvaluator.class.getName());

    /**
	 * 
	 */
    public AbstractResponsiveEvaluator() {
        super();
    }

    /**
	 * @see jhomenet.commons.responsive.exec.ResponsiveEvaluator#execute(jhomenet.commons.responsive.plan.Plan, jhomenet.commons.GeneralApplicationContext)
	 */
    public void execute(Plan plan, GeneralApplicationContext serverContext) {
        if (plan.isActive()) {
            logger.debug("Executing plan [" + plan.getPlanName() + "]");
            try {
                if (evaluateExpression(plan.getExpression(), serverContext)) {
                    plan.setLatestExpressionResult(Boolean.TRUE);
                    executeResponses(plan, plan.getResponses(ResponseType.ONTRUE));
                } else {
                    plan.setLatestExpressionResult(Boolean.FALSE);
                    executeResponses(plan, plan.getResponses(ResponseType.ONFALSE));
                }
            } catch (ResponsiveException re) {
                logger.error("Error while executing plan: " + re.getMessage(), re);
            }
        } else {
            logger.debug("Plan [" + plan.getPlanName() + "] not executed -> not active");
        }
    }

    /**
	 * 
	 * @param responseList
	 * @throws ResponsiveException
	 */
    private void executeResponses(Plan plan, List<Response> responseList) throws ResponsiveException {
        for (Response r : responseList) r.execute(plan);
    }
}
