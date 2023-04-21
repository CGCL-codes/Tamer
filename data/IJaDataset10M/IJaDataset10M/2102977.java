package org.zeroexchange.web.flow.management.step.resource;

import java.util.Date;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.zeroexchange.event.EventsDispatcher;
import org.zeroexchange.event.contract.ContractDataSaved;
import org.zeroexchange.exception.BusinessLogicException;
import org.zeroexchange.flow.FlowDataManager;
import org.zeroexchange.model.resource.Resource;
import org.zeroexchange.model.resource.participant.Consumption;
import org.zeroexchange.model.resource.participant.ResourceTender;
import org.zeroexchange.model.resource.participant.Supply;
import org.zeroexchange.model.user.User;
import org.zeroexchange.resource.write.ResourceWriter;
import org.zeroexchange.user.read.UserReader;
import org.zeroexchange.web.flow.management.ContractInfo;
import org.zeroexchange.web.flow.management.step.AbstractContractFlowStep;

/**
 * @author black
 *
 */
public abstract class AbstractResourceStep extends AbstractContractFlowStep implements ResourceManager {

    public static final String PARAM_RESOURCE = "resource";

    @Autowired
    private ResourceWriter resourceWriter;

    @Autowired
    private UserReader userReader;

    @Autowired
    private EventsDispatcher eventsDispatcher;

    private Resource resource;

    private UsageType usageType;

    /**
     * {@inheritDoc}
     */
    @Override
    public void initStep(ContractInfo flowData, Map<String, ?> actionData) {
        super.initStep(flowData, actionData);
        this.resource = (Resource) actionData.get(PARAM_RESOURCE);
        super.initStep(flowData, actionData);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FlowDataManager<ContractInfo> getFlowDataManager() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Resource getResource() {
        return resource;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object finishStep() {
        Resource savingResource = getResource();
        if (usageType == null && savingResource.getId() == null) {
            throw new BusinessLogicException("Resource usage type is null!");
        }
        ResourceTender resourceTender = usageType == UsageType.NEED ? new Consumption() : new Supply();
        resourceTender.setAcceptDate(new Date());
        resourceTender.setResource(savingResource);
        User flowUser = userReader.getUser(getData().getUserId());
        resourceTender.setUser(flowUser);
        if (usageType == UsageType.NEED) {
            savingResource.getConsumptions().add((Consumption) resourceTender);
        } else {
            savingResource.getSupplies().add((Supply) resourceTender);
        }
        Resource savedResource = resourceWriter.save(savingResource);
        eventsDispatcher.publishEvent(new ContractDataSaved(getData().getContract()));
        return savedResource;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUsageType(UsageType usageType) {
        this.usageType = usageType;
    }
}
