package org.omnisys.system;

/**
 * 
 *
 * @hibernate.class
 *     table="action"
 *     lazy="false"
 *
 */
public class Action {

    private Long id;

    /**
  *   @hibernate.id
  *     generator-class="native"
  *     column="id"
  *     type="java.lang.Long"
  *   @hibernate.generator-param name="sequence" value="ACTION_SEQ"
  *   @hibernate.column
  *     name="id"
  *     not-null="true"
  */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String description;

    /**
    *   @hibernate.property
    *     column="description"
    *     type="java.lang.String"
    *   @hibernate.column
    *     name="description"
    *     not-null="true"
    */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String alertType;

    /**
    *   @hibernate.property
    *     column="alerttype"
    *     type="java.lang.String"
    *   @hibernate.column
    *     name="alerttype"
    *     not-null="true"
    */
    public String getAlertType() {
        return alertType;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    private org.omnisys.system.ActionType actionType;

    /**
    * @hibernate.many-to-one
    *     column="actiontype"
    *     class="org.omnisys.system.ActionType"
    *     not-null="true"
    *     outer-join="auto"
    */
    public org.omnisys.system.ActionType getActionType() {
        return this.actionType;
    }

    public void setActionType(org.omnisys.system.ActionType actionType) {
        this.actionType = actionType;
    }

    private boolean selectDevicesAction;

    /**
    *   @hibernate.property
    *     column="selectdevicesaction"
    *     type="boolean"
    *   @hibernate.column
    *     name="selectdevicesaction"
    */
    public boolean getSelectDevicesAction() {
        return selectDevicesAction;
    }

    public void setSelectDevicesAction(boolean selectDevicesAction) {
        this.selectDevicesAction = selectDevicesAction;
    }

    private boolean selectServicesAction;

    /**
    *   @hibernate.property
    *     column="selectServicesAction"
    *     type="boolean"
    *   @hibernate.column
    *     name="selectServicesAction"
    */
    public boolean getSelectServicesAction() {
        return selectServicesAction;
    }

    public void setSelectServicesAction(boolean selectServicesAction) {
        this.selectServicesAction = selectServicesAction;
    }

    private org.omnisys.metrics.MetricType metricType;

    /**
    * @hibernate.many-to-one
    *     column="metrictype"
    *     class="org.omnisys.metrics.MetricType"
    *     not-null="false"
    *     outer-join="auto"
    */
    public org.omnisys.metrics.MetricType getMetricType() {
        return this.metricType;
    }

    public void setMetricType(org.omnisys.metrics.MetricType metricType) {
        this.metricType = metricType;
    }

    private String operator;

    /**
    *   @hibernate.property
    *     column="operator"
    *     type="java.lang.String"
    *   @hibernate.column
    *     name="operator"
    *     not-null="false"
    */
    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    private String comparisonValue;

    /**
    *   @hibernate.property
    *     column="comparisonValue"
    *     type="java.lang.String"
    *   @hibernate.column
    *     name="comparisonValue"
    *     not-null="false"
    */
    public String getComparisonValue() {
        return comparisonValue;
    }

    public void setComparisonValue(String comparisonValue) {
        this.comparisonValue = comparisonValue;
    }

    private String comparisonValueType;

    /**
    *   @hibernate.property
    *     column="comparisonValueType"
    *     type="java.lang.String"
    *   @hibernate.column
    *     name="comparisonValueType"
    *     not-null="false"
    */
    public String getComparisonValueType() {
        return comparisonValueType;
    }

    public void setComparisonValueType(String comparisonValueType) {
        this.comparisonValueType = comparisonValueType;
    }

    private java.lang.Long frequency;

    /**
    *   @hibernate.property
    *     column="frequency"
    *     type="java.lang.Long"
    *   @hibernate.column
    *     name="frequency"
    *     not-null="true"
    */
    public java.lang.Long getFrequency() {
        return frequency;
    }

    public void setFrequency(java.lang.Long frequency) {
        this.frequency = frequency;
    }

    private long timeToCheckNext;

    /**
    *   @hibernate.property
    *     column="timetochecknext"
    *     type="long"
    *   @hibernate.column
    *     name="timetochecknext"
    */
    public long getTimeToCheckNext() {
        return timeToCheckNext;
    }

    public void setTimeToCheckNext(long timeToCheckNext) {
        this.timeToCheckNext = timeToCheckNext;
    }

    private String emailAddresses;

    /**
    *   @hibernate.property
    *     column="emailAddresses"
    *     type="java.lang.String"
    *   @hibernate.column
    *     name="emailAddresses"
    */
    public String getEmailAddresses() {
        return emailAddresses;
    }

    public void setEmailAddresses(String emailAddresses) {
        this.emailAddresses = emailAddresses;
    }

    private org.omnisys.system.Script script;

    /**
    * @hibernate.many-to-one
    *     column="script"
    *     class="org.omnisys.system.Script"
    *     not-null="false"
    *     outer-join="auto"
    */
    public org.omnisys.system.Script getScript() {
        return this.script;
    }

    public void setScript(org.omnisys.system.Script script) {
        this.script = script;
    }

    private int status;

    /**
    *   @hibernate.property
    *     column="status"
    *     type="int"
    *   @hibernate.column
    *     name="status"
    *     not-null="true"
    */
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    private org.omnisys.system.ActionTarget actionTarget;

    /**
    * @hibernate.many-to-one
    *     column="actiontarget"
    *     class="org.omnisys.system.ActionTarget"
    *     not-null="false"
    *     outer-join="auto"
    */
    public org.omnisys.system.ActionTarget getActionTarget() {
        return this.actionTarget;
    }

    public void setActionTarget(org.omnisys.system.ActionTarget actionTarget) {
        this.actionTarget = actionTarget;
    }

    private org.omnisys.devices.Platform platform;

    /**
    * @hibernate.many-to-one
    *     column="platform"
    *     class="org.omnisys.devices.Platform"
    *     not-null="false"
    *     outer-join="auto"
    */
    public org.omnisys.devices.Platform getPlatform() {
        return this.platform;
    }

    public void setPlatform(org.omnisys.devices.Platform platform) {
        this.platform = platform;
    }

    private org.omnisys.services.ServiceType serviceType;

    /**
    * @hibernate.many-to-one
    *     column="servicetype"
    *     class="org.omnisys.services.ServiceType"
    *     not-null="false"
    *     outer-join="auto"
    */
    public org.omnisys.services.ServiceType getServiceType() {
        return this.serviceType;
    }

    public void setServiceType(org.omnisys.services.ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    private long timesToRepeat;

    /**
    *   @hibernate.property
    *     column="timestorepeat"
    *     type="java.lang.Long"
    *   @hibernate.column
    *     name="timestorepeat"
    *     not-null="true"
    */
    public long getTimesToRepeat() {
        return timesToRepeat;
    }

    public void setTimesToRepeat(long timesToRepeat) {
        this.timesToRepeat = timesToRepeat;
    }
}
