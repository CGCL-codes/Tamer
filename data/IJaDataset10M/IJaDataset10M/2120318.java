package jhomenet.commons.persistence.xml;

import java.util.List;
import jhomenet.commons.persistence.ResponsivePersistenceFacade;
import jhomenet.commons.responsive.condition.Condition;
import jhomenet.commons.responsive.plan.Plan;
import jhomenet.commons.responsive.response.Response;
import jhomenet.commons.responsive.trigger.*;

/**
 * TODO: Class description.
 * <p>
 * Id: $Id: $
 *
 * @author Dave Irwin (jhomenet at gmail dot com)
 */
public class ResponsivePersistenceLayerXml implements ResponsivePersistenceFacade {

    /**
	 * 
	 */
    public ResponsivePersistenceLayerXml() {
    }

    /**
	 * @see jhomenet.commons.persistence.ResponsivePersistenceFacade#removeCondition(jhomenet.commons.responsive.condition.Condition)
	 */
    public void removeCondition(Condition condition) {
    }

    /**
	 * @see jhomenet.commons.persistence.ResponsivePersistenceFacade#removePlan(jhomenet.commons.responsive.plan.Plan)
	 */
    public void removePlan(Plan plan) {
    }

    /**
	 * @see jhomenet.commons.persistence.ResponsivePersistenceFacade#removeResponse(jhomenet.commons.responsive.response.Response)
	 */
    public void removeResponse(Response response) {
    }

    /**
	 * @see jhomenet.commons.persistence.ResponsivePersistenceFacade#retrieveAllConditions()
	 */
    public List<Condition> retrieveAllConditions() {
        return null;
    }

    /**
	 * @see jhomenet.commons.persistence.ResponsivePersistenceFacade#retrieveAllPlans()
	 */
    public List<Plan> retrieveAllPlans() {
        return null;
    }

    /**
	 * @see jhomenet.commons.persistence.ResponsivePersistenceFacade#retrieveAllResponses()
	 */
    public List<Response> retrieveAllResponses() {
        return null;
    }

    /**
	 * @see jhomenet.commons.persistence.ResponsivePersistenceFacade#storeCondition(jhomenet.commons.responsive.condition.Condition)
	 */
    public Condition storeCondition(Condition condition) {
        return null;
    }

    /**
	 * @see jhomenet.commons.persistence.ResponsivePersistenceFacade#storePlan(jhomenet.commons.responsive.plan.Plan)
	 */
    public Plan storePlan(Plan plan) {
        return null;
    }

    /**
	 * @see jhomenet.commons.persistence.ResponsivePersistenceFacade#storeResponse(jhomenet.commons.responsive.response.Response)
	 */
    public Response storeResponse(Response response) {
        return null;
    }

    /**
	 * @see jhomenet.commons.persistence.ResponsivePersistenceFacade#removeTrigger(org.quartz.Trigger)
	 */
    public void removeTrigger(TriggerWrapper trigger) {
    }

    /**
	 * @see jhomenet.commons.persistence.ResponsivePersistenceFacade#retrieveAllTriggers()
	 */
    public List<TriggerWrapper> retrieveAllTriggers() {
        return null;
    }

    /**
	 * @see jhomenet.commons.persistence.ResponsivePersistenceFacade#storeTrigger(org.quartz.Trigger)
	 */
    public TriggerWrapper storeTrigger(TriggerWrapper trigger) {
        return null;
    }
}
