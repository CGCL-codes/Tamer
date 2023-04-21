package edu.uis.csc478.spring09.threeoxen.requestprocessing;

import java.util.HashMap;

/**
 * User: Chris Logan
 * Date: Apr 14, 2009
 * Time: 11:57:57 AM
 */
public class RequestProcessorFactory {

    public static final String USER_MGMT = "edu.uis.csc478.spring09.threeoxen.requestprocessing.impl.UserManagementRequestProcessorImpl";

    public static final String LOGIN = "edu.uis.csc478.spring09.threeoxen.requestprocessing.impl.LoginRequestProcessorImpl";

    public static final String STORE_MGMT = "edu.uis.csc478.spring09.threeoxen.requestprocessing.impl.StoreManagementRequestProcessorImpl";

    public static final String PANTRY_MGMT = "edu.uis.csc478.spring09.threeoxen.requestprocessing.impl.PantryManagementRequestProcessorImpl";

    public HashMap processorCache = new HashMap();

    private static RequestProcessorFactory instance = new RequestProcessorFactory();

    private RequestProcessorFactory() {
    }

    public static RequestProcessorFactory getInstance() {
        return instance;
    }

    public RequestProcessor getRequestProcessor(String processorName) throws InvalidRequestProcessorException {
        RequestProcessor processor = (RequestProcessor) processorCache.get(processorName);
        if (processor == null) {
            try {
                processor = (RequestProcessor) Class.forName(processorName).newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                throw new InvalidRequestProcessorException("RequestProcessor " + processorName + " could not be created.", e);
            }
        }
        this.processorCache.put(processorName, processor);
        return processor;
    }
}
