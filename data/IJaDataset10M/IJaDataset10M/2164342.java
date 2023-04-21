package org.omnisys.metrics;

/**
 * 
 *
 * @hibernate.class
 *     table="metrictype"
 *
 */
public class MetricType {

    private String indicator;

    /**
     *   @hibernate.property
     *     column="indicator"
     *     type="java.lang.String"
     *   @hibernate.column
     *     name="indicator"
     *     not-null="true"
     */
    public String getIndicator() {
        return indicator;
    }

    public void setIndicator(String indicator) {
        this.indicator = indicator;
    }

    private String name;

    /**
     *   @hibernate.property
     *     column="name"
     *     type="java.lang.String"
     *   @hibernate.column
     *     name="name"
     *     not-null="true"
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private long id;

    /**
  *   @hibernate.id
  *     generator-class="native"
  *     column="id"
  *     type="long"
  *   @hibernate.generator-param name="sequence" value="METRICTYPE_SEQ"
  *   @hibernate.column
  *     name="id"
  *     not-null="true"
  */
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private String arg1;

    /**
    *   @hibernate.property
    *     column="arg1"
    *     type="java.lang.String"
    *   @hibernate.column
    *     name="arg1"
    */
    public String getArg1() {
        return arg1;
    }

    public void setArg1(String arg1) {
        this.arg1 = arg1;
    }

    private String arg2;

    /**
    *   @hibernate.property
    *     column="arg2"
    *     type="java.lang.String"
    *   @hibernate.column
    *     name="arg2"
    */
    public String getArg2() {
        return arg2;
    }

    public void setArg2(String arg2) {
        this.arg2 = arg2;
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

    private int checkInterval;

    /**
    *   @hibernate.property
    *     column="checkInterval"
    *     type="int"
    *   @hibernate.column
    *     name="checkInterval"
    *     not-null="true"
    */
    public int getCheckInterval() {
        return checkInterval;
    }

    public void setCheckInterval(int checkInterval) {
        this.checkInterval = checkInterval;
    }

    private org.omnisys.devices.Protocol protocol;

    /**
    * @hibernate.many-to-one
    *     column="protocol"
    *     class="org.omnisys.devices.Protocol"
    *     not-null="true"
    *     outer-join="auto"
    */
    public org.omnisys.devices.Protocol getProtocol() {
        return this.protocol;
    }

    public void setProtocol(org.omnisys.devices.Protocol protocol) {
        this.protocol = protocol;
    }

    private String type;

    /**
    *   @hibernate.property
    */
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private int dashboard;

    /**
    *   @hibernate.property
    *     column="dashboard"
    *     type="int"
    *   @hibernate.column
    *     name="dashboard"
    */
    public int getDashboard() {
        return dashboard;
    }

    public void setDashboard(int dashboard) {
        this.dashboard = dashboard;
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

    private long nextrun;

    /**
    *   @hibernate.property
    *     column="nextrun"
    *     type="long"
    *   @hibernate.column
    *     name="nextrun"
    */
    public long getNextrun() {
        return nextrun;
    }

    public void setNextrun(long nextrun) {
        this.nextrun = nextrun;
    }
}
