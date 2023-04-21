package org.personalsmartspace.recommendersystem.api.proxy;

import java.util.List;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.felix.scr.annotations.Services;
import org.osgi.framework.Constants;
import org.osgi.service.cm.ConfigurationListener;
import org.osgi.service.cm.ManagedService;
import org.personalsmartspace.onm.api.pss3p.ICallbackListener;
import org.personalsmartspace.onm.api.pss3p.ONMException;
import org.personalsmartspace.onm.api.pss3p.ServiceMessage;
import org.personalsmartspace.onm.api.pss3p.XMLConverter;
import org.personalsmartspace.recommendersystem.api.platform.IQueryRecommender;
import org.personalsmartspace.sre.api.pss3p.IDigitalPersonalIdentifier;

/**
 * This class is a deployable proxy for the IQueryRecommender interface.
*/
@Component(name = "Query Recommender Proxy", label = "Query Recommender Proxy", immediate = true)
@Services(value = { @Service(value = IQueryRecommender.class), @Service(value = ManagedService.class), @Service(value = ConfigurationListener.class) })
public class QueryRecommenderProxy extends AbstractConfigurableRecommenderProxy implements IQueryRecommender {

    /**
     * The service PID property for ConfigAdmin purposes
     */
    @Property(value = QueryRecommenderProxy.SERVICE_PID_NAME_VALUE)
    static final String SERVICE_PID_NAME = Constants.SERVICE_PID;

    static final String SERVICE_PID_NAME_VALUE = "Query_Recommender";

    /**
     * The name of the remote method
     */
    protected final String REMOTE_METHOD_NAME = "recommend";

    /**
     * Are the invocations asynchronous?
     */
    protected final boolean IS_ASYNCHRONOUS = true;

    @Override
    public void recommend(IDigitalPersonalIdentifier userID, List<String> items, int howMany, ICallbackListener callbackFunction) {
        ServiceMessage message = new ServiceMessage(this.srcSrvId, this.targetSrvId, this.serverEntityId, this.isExtraPssService, this.REMOTE_METHOD_NAME, this.IS_ASYNCHRONOUS, XMLConverter.objectsToXml(new Object[] { userID, items, callbackFunction }), new String[] { userID.getClass().getCanonicalName(), items.getClass().getCanonicalName(), callbackFunction.getClass().getCanonicalName() });
        try {
            this.msgQService.addServiceMessage(message, callbackFunction);
        } catch (ONMException e) {
            e.printStackTrace();
            logger.error(e.getLocalizedMessage());
        }
    }

    @Override
    protected String getPidValue() {
        return QueryRecommenderProxy.SERVICE_PID_NAME_VALUE;
    }
}
