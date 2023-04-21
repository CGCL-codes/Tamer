package com.idna.batchid.model.database;

import java.io.Serializable;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.StringTokenizer;
import org.apache.commons.lang.StringUtils;

/**
 * An encapsulation for records from the bidService table.  Instances
 * of this class are typically constructed from a <tt>ServiceDao</tt> instance.
 * @author Guy Rish
 * @see com.idna.batchid.dao.ServiceDao
 */
public class Service implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer serviceId = null;

    private String description = null;

    private String taskName = null;

    private String url = null;

    private String operationName = null;

    private String inputXslt = null;

    private String responseColumns = null;

    private List<String> responseColumnsList = null;

    private String responseXslt = null;

    private Map<String, String> _parameters = null;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setServiceId(Integer id) {
        this.serviceId = id;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    /**
	 * @param taskname CheckID, ProveID etc.
	 */
    public void setTaskName(String taskname) {
        this.taskName = taskname;
    }

    public String getTaskName() {
        return this.taskName;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    /**
	 * @param operationName 'search' or what
	 */
    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setXslt(String xslt) {
        inputXslt = xslt;
    }

    public String getXslt() {
        return inputXslt;
    }

    public void setResponseColumns(String responseColumns) {
        this.responseColumns = responseColumns;
        if (responseColumns != null) {
            StringUtils.deleteWhitespace(responseColumns);
            responseColumnsList = new ArrayList<String>();
            StringTokenizer st = new StringTokenizer(responseColumns, ",");
            while (st.hasMoreTokens()) {
                responseColumnsList.add(st.nextToken());
            }
        }
    }

    public String getResponseColumns() {
        return responseColumns;
    }

    /**
	 * Set the runtime response columns based on what returned from the service.
	 * 
	 * @param columnList
	 */
    public void setResponseColumnList(List<String> columnList) {
        responseColumnsList = columnList;
    }

    public List<String> getResponseColumnsList() {
        return responseColumnsList;
    }

    public void setResponseXslt(String xslt) {
        responseXslt = xslt;
    }

    public String getResponseXslt() {
        return responseXslt;
    }

    /**
	 * This method is typically used only during creation as it establishes,
	 * for a particular instance, a set of parameters drawn from the bidServiceParameter
	 * table.  These parameters are used when the WebService represented by the object
	 * is called as fixed values that are incorporated into the <tt>ProductRequest</tt>.
	 * @param params a map of <tt>String</tt>s.
	 */
    public void setParameters(Map<String, String> params) {
        _parameters = params;
    }

    /**
	 * Retrieve the parameters used to when creating a ProductRequest with this
	 * <tt>Service</tt> instance.
	 * @return a map of <tt>String</tt>s.
	 */
    public Map<String, String> getParameters() {
        return _parameters;
    }

    /**
	 * Paying homage to the grish map philosophy, we call this method getUnique'KEY'
	 * This key will be can be used to uniquely identify the service
	 * @return
	 */
    public String getUniqueKey() {
        return new Integer(serviceId).toString() + ":" + description;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Service {");
        buffer.append(String.format("[serviceid:%s]", this.getServiceId()));
        buffer.append(String.format(",[Response columns:%s]", this.getResponseColumnsList()));
        buffer.append(String.format(",[taskname:%s]", this.getTaskName()));
        buffer.append(String.format(",[description:%s]", this.getDescription()));
        buffer.append(String.format(",[url:%s]", this.getUrl()));
        buffer.append(String.format(",[operation name:%s]", this.getOperationName()));
        buffer.append(String.format(",parameters {%s}", this.getParameters().toString()));
        String entryXSLT = this.getXslt();
        buffer.append(String.format(",[xslt:%s]", this.helperToString(entryXSLT)));
        buffer.append(String.format(",[Response xslt:%s]", this.helperToString(this.getResponseXslt())));
        buffer.append("}");
        return buffer.toString();
    }

    private String helperToString(String checkIsValid) {
        if (checkIsValid != null) {
            return "Valid";
        } else return null;
    }
}
