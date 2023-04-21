package com.astrium.faceo.client.rpc.programming.sps2;

import com.astrium.faceo.client.bean.programming.sps2.describeSensor.DescribeSensorRequestBean;
import com.astrium.faceo.client.bean.programming.sps2.describeSensor.DescribeSensorResponseBean;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * 
 * The DescribeSensor operation allows a client to request a detailed description 
 * of a sensor. The request can be targeted at a description that was valid 
 * at a certain point in or during a certain period of time in the past. 
 * 
 * @author ASTRIUM
 *
 */
@RemoteServiceRelativePath("getDescribeSensorController.srv")
public interface DescribeSensorService extends RemoteService {

    /**
	 * 
	 * @author ASTRIUM
	 *
	 */
    public static class Util {

        /**
		 * 
		 * @return GWT.create(DescribeSensorService.class)
		 */
        public static DescribeSensorServiceAsync getInstance() {
            return GWT.create(DescribeSensorService.class);
        }
    }

    /**
     * This method returns the result for a 'DescribeSensor' SPS operation for one sensor
     * 
     * @param _operation (String)						: SPS 2.0 'DescribeSensor' operation
     * @param _parameters (DescribeSensorRequestBean)	: 'DesccribeTasking' parameters for one sensor
     * @param _webAccessMode (boolean)					: true for using web services, false else (for using xml files)
     * 
     * @return DescribeSensorResponseBean : 'DescribeSensor' response in HTML format
     */
    public DescribeSensorResponseBean getDescribeSensorResult(String _operation, DescribeSensorRequestBean _parameters, boolean _webAccessMode);
}
