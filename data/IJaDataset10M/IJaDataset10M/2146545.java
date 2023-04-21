package com.astrium.faceo.client.bean.programming.sps2.validate;

import java.io.Serializable;
import java.util.ArrayList;

/** * <B>FACEO HMAFO</B> <BR> *  * <P> * This class is a informations container for the 'Validate' parameters *   * Several acquisition attempts are sometimes necessary to obtain a satisfying result  * (case of optical satellites on zones with cloudy tendency for example).  * The Validate operation can be used by the customer to indicate to the server  * that an acquisition is satisfactory and thus to stop collecting new images for this area. *  * </P> * </P> *  * @author  GR * @version 1.0, le 10/12/2010 */
public class ValidateRequestBean implements Serializable {

    /**	 * 	 */
    private static final long serialVersionUID = 7385188865588652920L;

    /** sensor urn identifier */
    private String sensorUrn = null;

    /** task identifier */
    private String taskId = null;

    /** task user */
    private String user = null;

    /** task name */
    private String name = null;

    /** List of segments identifiers */
    private ArrayList<String> segmentsId = new ArrayList<String>();

    /**	 * Default Constructor. The Default Constructor's explicit declaration	 * is required for a serializable class. (GWT)	*/
    public ValidateRequestBean() {
    }

    /** 	 * getter on taskId	 * 	 * @return String : task identifier	*/
    public String getTaskId() {
        return (this.taskId != null) ? this.taskId : "";
    }

    /** 	 * getter on user	 * 	 * @return String : user name	*/
    public String getUser() {
        return (this.user != null) ? this.user : "";
    }

    /** 	 * getter on name	 * 	 * @return String : task name	*/
    public String getName() {
        return (this.name != null) ? this.name : "";
    }

    /** 	 * getter on segmentsId	 * 	 * @return List : segments identifiers	*/
    public ArrayList<String> getSegmentsId() {
        return this.segmentsId;
    }

    /** 	 * getter on sensorUrn	 * 	 * @return String : sensor urn identifier	*/
    public String getSensorUrn() {
        return (this.sensorUrn != null) ? this.sensorUrn : "";
    }

    /** 	 * setter on taskId	 * 	 * @param _taskId (String) : task identifier value	*/
    public void setTaskId(String _taskId) {
        this.taskId = _taskId;
    }

    /** 	 * setter on user	 * 	 * @param _user (String) : task user value	*/
    public void setUser(String _user) {
        this.user = _user;
    }

    /** 	 * setter on name	 * 	 * @param _name (String) : task name value	*/
    public void setName(String _name) {
        this.name = _name;
    }

    /** 	 * setter on segmentsId	 * 	 * @param _segmentsId (List) : segments identifiers values	*/
    public void setSegmentsId(ArrayList<String> _segmentsId) {
        this.segmentsId = _segmentsId;
    }

    /** 	 * setter on sensorUrn	 * 	 * @param _sensorUrn (String) : Get Status sensor urn identifier value	*/
    public void setSensorUrn(String _sensorUrn) {
        this.sensorUrn = _sensorUrn;
    }
}
