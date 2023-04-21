package com.astrium.faceo.client.rpc.programming.sps2;

import java.util.Map;
import com.astrium.faceo.client.bean.programming.sps2.TaskBean;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 * @author ASTRIUM
 *
 */
public interface GetTasksListServiceAsync {

    /**
     * This method returns a list with the SPS tasks of one user 
     * with the 'GetTasksList' operation
     * 
     * @param _operation (String)		: operation of the SPS service ('GetTasksList')
     * @param _idUser (String)			: id of the user
     * @param _webAccessMode (String)	: true for using web services, false else (for using kml files)
     * 
     * @param _callback (Map<String, TaskBean>) : HashMap of tasks
     */
    public void getTasksList(String _operation, String _idUser, boolean _webAccessMode, AsyncCallback<Map<String, TaskBean>> _callback);
}
