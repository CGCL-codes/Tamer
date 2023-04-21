package com.astrium.faceo.client.bean.programming.sps2.confirm;

import java.io.Serializable;

/** * <B>FACEO HMAFO</B> <BR> *  * <P> * This class is a informations container for the 'Confirm' parameters *   * The Confirm operation allows SPS clients to confirm a previously reserved task.  * If accepted i;e; if task has'nt expired yet, the task shall transit from state reserved * to inExecution, otherwise the SPS shall reject the request *  * </P> * </P> *  * @author  GR * @version 1.0, le 24/08/2010 */
public class ConfirmRequestBean implements Serializable {

    /**	 * 	 */
    private static final long serialVersionUID = -8811539059920934267L;

    /** sensor urn identifier */
    private String sensorUrn = null;

    /** task identifier */
    private String taskId = null;

    /**	 * Default Constructor. The Default Constructor's explicit declaration	 * is required for a serializable class. (GWT)	*/
    public ConfirmRequestBean() {
    }

    /** 	 * getter on taskId	 * 	 * @return String : task identifier	*/
    public String getTaskId() {
        return (this.taskId != null) ? this.taskId : "";
    }

    /** 	 * getter on sensorUrn	 * 	 * @return String : sensor urn	*/
    public String getSensorUrn() {
        return (this.sensorUrn != null) ? this.sensorUrn : "";
    }

    /** 	 * setter on taskId	 * 	 * @param _taskId (String) : task identifier value	*/
    public void setTaskId(String _taskId) {
        this.taskId = _taskId;
    }

    /** 	 * setter on sensorUrn	 * 	 * @param _sensorUrn (String) : sensor urn value	*/
    public void setSensorUrn(String _sensorUrn) {
        this.sensorUrn = _sensorUrn;
    }
}
