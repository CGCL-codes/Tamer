package org.jazzteam.bpe.web.service.client;

import java.util.HashMap;
import java.util.Map;
import javax.xml.ws.Service;
import org.jazzteam.bpe.logic.Engine;
import org.jazzteam.bpe.model.variables.Variable;
import org.jazzteam.bpe.model.variables.interfaces.IVariable;
import org.jazzteam.bpe.web.service.client.services.test.Test;
import org.jazzteam.bpe.web.service.client.services.test.TestService;

/**
 * Multitheaded client for other web-services.
 * 
 * @author skars
 * @version $Rev: $
 */
public class ServiceClient {

    /** Available services map. */
    private static Map<WebService, Service> servicesMap = new HashMap<WebService, Service>();

    /** Business process engine for starting process with response from service. */
    private Engine engine;

    static {
        servicesMap.put(WebService.TEST, new TestService());
    }

    /**
	 * Constructs service client.
	 */
    public ServiceClient() {
    }

    /**
	 * Sends request to service.
	 * 
	 * @param serviceAddr
	 *            Service address.
	 * @param procId
	 *            Identifier of process that send request.
	 * @param reqParam
	 *            Service request parameter.
	 * @version 1
	 */
    @SuppressWarnings("rawtypes")
    public void sendRequest(final WebService service, final long procId, final Variable reqParam) {
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                Service s = null;
                s = servicesMap.get(service);
                if (s != null) {
                    if (WebService.TEST.equals(service)) {
                        Test proxyTestService = ((TestService) s).getTestPort();
                        proxyTestService.changeData(procId, ((IVariable) reqParam).getValue());
                    }
                }
            }
        });
        t.start();
    }

    /**
	 * Gets business process engine.
	 * 
	 * @return Returns process engine.
	 */
    public Engine getEngine() {
        return engine;
    }

    /**
	 * Sets business process engine.
	 * 
	 * @param engine
	 *            The engine to set.
	 */
    public void setEngine(Engine engine) {
        this.engine = engine;
    }
}
