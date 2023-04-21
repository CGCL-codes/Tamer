package org.mobicents.slee.resource.diameter.sh.events.avp.userdata;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import net.java.slee.resource.diameter.sh.events.avp.userdata.ApplicationServer;
import net.java.slee.resource.diameter.sh.events.avp.userdata.Extension;
import net.java.slee.resource.diameter.sh.events.avp.userdata.InitialFilterCriteria;
import net.java.slee.resource.diameter.sh.events.avp.userdata.Trigger;

/**
 * <p>Java class for tInitialFilterCriteria complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tInitialFilterCriteria">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Priority" type="{}tPriority"/>
 *         &lt;element name="TriggerPoint" type="{}tTrigger" minOccurs="0"/>
 *         &lt;element name="ApplicationServer" type="{}tApplicationServer"/>
 *         &lt;element name="Extension" type="{}tExtension" minOccurs="0"/>
 *         &lt;any processContents='lax' namespace='##other' maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tInitialFilterCriteria", propOrder = { "priority", "triggerPoint", "applicationServer", "extension", "any" })
public class TInitialFilterCriteria implements InitialFilterCriteria {

    @XmlElement(name = "Priority")
    protected int priority;

    @XmlElement(name = "TriggerPoint")
    protected TTrigger triggerPoint;

    @XmlElement(name = "ApplicationServer", required = true)
    protected TApplicationServer applicationServer;

    @XmlElement(name = "Extension")
    protected TExtension extension;

    @XmlAnyElement(lax = true)
    protected List<Object> any;

    public int getPriority() {
        return priority;
    }

    public void setPriority(int value) {
        this.priority = value;
    }

    public Trigger getTriggerPoint() {
        return triggerPoint;
    }

    public void setTriggerPoint(Trigger value) {
        this.triggerPoint = (TTrigger) value;
    }

    public ApplicationServer getApplicationServer() {
        return applicationServer;
    }

    public void setApplicationServer(ApplicationServer value) {
        this.applicationServer = (TApplicationServer) value;
    }

    public Extension getExtension() {
        return extension;
    }

    public void setExtension(Extension value) {
        this.extension = (TExtension) value;
    }

    public List<Object> getAny() {
        if (any == null) {
            any = new ArrayList<Object>();
        }
        return this.any;
    }
}
