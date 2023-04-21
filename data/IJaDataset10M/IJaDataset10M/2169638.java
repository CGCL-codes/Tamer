package com.astrium.faceo.client.rpc.programming.sps2;

import com.astrium.faceo.client.bean.programming.sps2.describeResultAccess.DescribeResultAccessRequestBean;
import com.astrium.faceo.client.bean.programming.sps2.describeResultAccess.DescribeResultAccessResponseBean;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * 
 * The DescribeResultAccess operation allows SPS clients to retrieve 
 * information how to access data that was produced by a specific task, 
 * or how to retrieve data for a given sensor that is tasked by this SPS in general.
 * The response may point to a SOS, WMS, WFS any other OGC Web Service 
 * that provides data any data file or folder on a ftp server any data file 
 * or file container that is accessible over the Internet Clients provide the ID 
 * of either a sensor or task to identify what information they are interested in. 
 * 
 * @author ASTRIUM
 *
 */
@RemoteServiceRelativePath("getDescribeResultAccessController.srv")
public interface DescribeResultAccessService extends RemoteService {

    /**
	 * 
	 * @author ASTRIUM
	 *
	 */
    public static class Util {

        /**
		 * 
		 * @return GWT.create(DescribeResultAccessService.class)
		 */
        public static DescribeResultAccessServiceAsync getInstance() {
            return GWT.create(DescribeResultAccessService.class);
        }
    }

    /**
     * This method returns the result for a 'DescribeResultAccess' SPS operation for one sensor
     * 
     * @param _operation (String)							: SPS 2.0 'DescribeResultAccess' operation
     * @param _parameters (DescribeResultAccessRequestBean)	: 'DescribeResultAccess' parameters for one sensor
     * @param _webAccessMode (boolean)						: true for using web services, false else (for using xml files)
     * 
     * @return DescribeTaskingResponseBean : 'DescribeResultAccess' response in HTML format
     */
    public DescribeResultAccessResponseBean getDescribeResultAccessResult(String _operation, DescribeResultAccessRequestBean _parameters, boolean _webAccessMode);
}
