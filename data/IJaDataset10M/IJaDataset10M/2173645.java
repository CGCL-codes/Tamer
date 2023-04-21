package com.astrium.faceo.client.rpc.programming.sps2;

import java.util.Map;
import com.astrium.faceo.client.bean.programming.sps2.TaskBean;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * 
 * @author ASTRIUM
 *
 */
@RemoteServiceRelativePath("getUpdatedTasksListController.srv")
public interface GetUpdatedTasksListService extends RemoteService {

    /**
	 * 
	 * @author ASTRIUM
	 *
	 */
    public static class Util {

        /**
		 * 
		 * @return GetUpdatedTasksListServiceAsync
		 */
        public static GetUpdatedTasksListServiceAsync getInstance() {
            return GWT.create(GetUpdatedTasksListService.class);
        }
    }

    /**
     * This method returns a list with the SPS tasks of one user 
     * with the 'GetUpdatedTasksList' operation
     * 
     * @param _operation (String)	: operation of the SPS service ('GetUpdatedTasksList')
     * @param _idUser (String)		: id of the user
     * 
     * @return Map<String, TaskBean> : HashMap of tasks
     */
    public Map<String, TaskBean> getUpdatedTasksList(String _operation, String _idUser);
}
