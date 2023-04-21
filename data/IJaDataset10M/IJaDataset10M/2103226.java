package org.gwtplanning.client.test;

import java.util.Date;
import org.gwtplanning.client.model.Category;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * DOC stephane class global comment. Detailled comment
 */
public interface DataService extends RemoteService {

    public static final String SERVICE_URI = "/data";

    public Category[] getData(Date startDate, Date endDate);

    /**
     * DOC stephane DataService class global comment. Detailled comment
     */
    public static class Util {

        public static DataServiceAsync getInstance() {
            DataServiceAsync instance = (DataServiceAsync) GWT.create(DataService.class);
            ServiceDefTarget target = (ServiceDefTarget) instance;
            target.setServiceEntryPoint(GWT.getModuleBaseURL() + SERVICE_URI);
            return instance;
        }
    }
}
