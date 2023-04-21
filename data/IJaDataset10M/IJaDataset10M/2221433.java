package com.astrium.faceo.client.rpc.programming.sps2;

import com.astrium.faceo.client.bean.programming.sps2.TaskResultBean;
import com.astrium.faceo.client.bean.programming.sps2.request.TaskingParametersBean;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 * @author ASTRIUM
 *
 */
public interface GetTaskResultServiceAsync {

    /**
     * This method returns the detail of a SPS task of one user in database
     * an with the 'GetStatus' operation
     * 
     * @param _operation (String)		: operation of the FACEO service ('GetTaskResult')
     * @param _idUser (String)			: id User
     * @param _idTask (String)			: id of the task
     * @param _taskingParameters (TaskingParametersBean)	: tasking parameters for one sensor
     * @param _webAccessMode (boolean)	: true for using web services, false else (for using xml files)
     * 
     * @param _callback (TaskResultBean) : a task
     */
    public void getTaskResult(String _operation, String _idUser, String _idTask, TaskingParametersBean _taskingParameters, boolean _webAccessMode, AsyncCallback<TaskResultBean> _callback);
}
