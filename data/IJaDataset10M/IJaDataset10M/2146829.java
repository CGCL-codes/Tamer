package org.omnisys.metrics;

/**
 * 
 *
 * @hibernate.class
 *     table="metric"
 *
 */
public class Metric {

    private Long id;

    /**
  *   @hibernate.id
  *     generator-class="native"
  *     column="id"
  *     type="long"
  *   @hibernate.generator-param name="sequence" value="METRIC_SEQ"
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

    private String result;

    /**
    *   @hibernate.property
    *
    *   @hibernate.column
    *   name="result"
    *   not-null="true"
    *   length="10000"
    */
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    private long timeran;

    /**
    *   @hibernate.property
    *     column="timeran"
    *     type="long"
    *   @hibernate.column
    *     name="timeran"
    */
    public long getTimeran() {
        return timeran;
    }

    public void setTimeran(long timeran) {
        this.timeran = timeran;
    }

    private org.omnisys.metrics.MetricType metricType;

    /**
    * @hibernate.many-to-one
    *     column="metrictype"
    *     class="org.omnisys.metrics.MetricType"
    *     not-null="true"
    *     outer-join="auto"
    */
    public org.omnisys.metrics.MetricType getMetricType() {
        return this.metricType;
    }

    public void setMetricType(org.omnisys.metrics.MetricType metricType) {
        this.metricType = metricType;
    }

    private org.omnisys.devices.Device device;

    /**
    * @hibernate.many-to-one
    *     column="device"
    *     class="org.omnisys.devices.Device"
    *     outer-join="auto"
    */
    public org.omnisys.devices.Device getDevice() {
        return this.device;
    }

    public void setDevice(org.omnisys.devices.Device device) {
        this.device = device;
    }

    private org.omnisys.services.Service service;

    /**
    * @hibernate.many-to-one
    *     column="service"
    *     class="org.omnisys.services.Service"
    *     outer-join="auto"
    */
    public org.omnisys.services.Service getService() {
        return this.service;
    }

    public void setService(org.omnisys.services.Service service) {
        this.service = service;
    }

    private org.omnisys.devices.Component component;

    /**
    * @hibernate.many-to-one
    *     column="component"
    *     class="org.omnisys.devices.Component"
    *     outer-join="auto"
    */
    public org.omnisys.devices.Component getComponent() {
        return this.component;
    }

    public void setComponent(org.omnisys.devices.Component component) {
        this.component = component;
    }
}
