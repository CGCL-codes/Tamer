package com.mu.rai.services;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for target complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="target">
 *   &lt;complexContent>
 *     &lt;extension base="{http://services.rai.mu.com}namedModelObject">
 *       &lt;sequence>
 *         &lt;element name="analyzer_mode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="max_boot_time" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="min_boot_time" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="restart_delay" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="restart_fail_behavior" type="{http://services.rai.mu.com}restartFailBehavior" minOccurs="0"/>
 *         &lt;choice minOccurs="0">
 *           &lt;element name="kernel_target_control" type="{http://services.rai.mu.com}kernelTargetControl"/>
 *           &lt;element name="process_target_control" type="{http://services.rai.mu.com}processTargetControl"/>
 *           &lt;element name="service_target_control" type="{http://services.rai.mu.com}serviceTargetControl"/>
 *         &lt;/choice>
 *         &lt;element name="target_in" type="{http://services.rai.mu.com}reference"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "target", propOrder = { "analyzerMode", "maxBootTime", "minBootTime", "restartDelay", "restartFailBehavior", "kernelTargetControl", "processTargetControl", "serviceTargetControl", "targetIn" })
@XmlSeeAlso({ PassthroughTarget.class, ServerTarget.class })
public abstract class Target extends NamedModelObject {

    @XmlElement(name = "analyzer_mode", required = true)
    protected String analyzerMode;

    @XmlElement(name = "max_boot_time")
    protected long maxBootTime;

    @XmlElement(name = "min_boot_time")
    protected long minBootTime;

    @XmlElement(name = "restart_delay")
    protected int restartDelay;

    @XmlElement(name = "restart_fail_behavior")
    protected RestartFailBehavior restartFailBehavior;

    @XmlElement(name = "kernel_target_control")
    protected KernelTargetControl kernelTargetControl;

    @XmlElement(name = "process_target_control")
    protected ProcessTargetControl processTargetControl;

    @XmlElement(name = "service_target_control")
    protected ServiceTargetControl serviceTargetControl;

    @XmlElement(name = "target_in", required = true)
    protected Reference targetIn;

    /**
     * Gets the value of the analyzerMode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnalyzerMode() {
        return analyzerMode;
    }

    /**
     * Sets the value of the analyzerMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnalyzerMode(String value) {
        this.analyzerMode = value;
    }

    /**
     * Gets the value of the maxBootTime property.
     * 
     */
    public long getMaxBootTime() {
        return maxBootTime;
    }

    /**
     * Sets the value of the maxBootTime property.
     * 
     */
    public void setMaxBootTime(long value) {
        this.maxBootTime = value;
    }

    /**
     * Gets the value of the minBootTime property.
     * 
     */
    public long getMinBootTime() {
        return minBootTime;
    }

    /**
     * Sets the value of the minBootTime property.
     * 
     */
    public void setMinBootTime(long value) {
        this.minBootTime = value;
    }

    /**
     * Gets the value of the restartDelay property.
     * 
     */
    public int getRestartDelay() {
        return restartDelay;
    }

    /**
     * Sets the value of the restartDelay property.
     * 
     */
    public void setRestartDelay(int value) {
        this.restartDelay = value;
    }

    /**
     * Gets the value of the restartFailBehavior property.
     * 
     * @return
     *     possible object is
     *     {@link RestartFailBehavior }
     *     
     */
    public RestartFailBehavior getRestartFailBehavior() {
        return restartFailBehavior;
    }

    /**
     * Sets the value of the restartFailBehavior property.
     * 
     * @param value
     *     allowed object is
     *     {@link RestartFailBehavior }
     *     
     */
    public void setRestartFailBehavior(RestartFailBehavior value) {
        this.restartFailBehavior = value;
    }

    /**
     * Gets the value of the kernelTargetControl property.
     * 
     * @return
     *     possible object is
     *     {@link KernelTargetControl }
     *     
     */
    public KernelTargetControl getKernelTargetControl() {
        return kernelTargetControl;
    }

    /**
     * Sets the value of the kernelTargetControl property.
     * 
     * @param value
     *     allowed object is
     *     {@link KernelTargetControl }
     *     
     */
    public void setKernelTargetControl(KernelTargetControl value) {
        this.kernelTargetControl = value;
    }

    /**
     * Gets the value of the processTargetControl property.
     * 
     * @return
     *     possible object is
     *     {@link ProcessTargetControl }
     *     
     */
    public ProcessTargetControl getProcessTargetControl() {
        return processTargetControl;
    }

    /**
     * Sets the value of the processTargetControl property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProcessTargetControl }
     *     
     */
    public void setProcessTargetControl(ProcessTargetControl value) {
        this.processTargetControl = value;
    }

    /**
     * Gets the value of the serviceTargetControl property.
     * 
     * @return
     *     possible object is
     *     {@link ServiceTargetControl }
     *     
     */
    public ServiceTargetControl getServiceTargetControl() {
        return serviceTargetControl;
    }

    /**
     * Sets the value of the serviceTargetControl property.
     * 
     * @param value
     *     allowed object is
     *     {@link ServiceTargetControl }
     *     
     */
    public void setServiceTargetControl(ServiceTargetControl value) {
        this.serviceTargetControl = value;
    }

    /**
     * Gets the value of the targetIn property.
     * 
     * @return
     *     possible object is
     *     {@link Reference }
     *     
     */
    public Reference getTargetIn() {
        return targetIn;
    }

    /**
     * Sets the value of the targetIn property.
     * 
     * @param value
     *     allowed object is
     *     {@link Reference }
     *     
     */
    public void setTargetIn(Reference value) {
        this.targetIn = value;
    }
}
