package remote.proxies;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>Java class for taskInstance complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="taskInstance">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="running" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="runningSince" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="taskSchedule" type="{http://services.remote/}taskSchedule" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "taskInstance", propOrder = { "id", "running", "runningSince", "taskSchedule" })
public class TaskInstance {

    protected Long id;

    protected boolean running;

    protected XMLGregorianCalendar runningSince;

    protected TaskSchedule taskSchedule;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setId(Long value) {
        this.id = value;
    }

    /**
     * Gets the value of the running property.
     * 
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Sets the value of the running property.
     * 
     */
    public void setRunning(boolean value) {
        this.running = value;
    }

    /**
     * Gets the value of the runningSince property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getRunningSince() {
        return runningSince;
    }

    /**
     * Sets the value of the runningSince property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setRunningSince(XMLGregorianCalendar value) {
        this.runningSince = value;
    }

    /**
     * Gets the value of the taskSchedule property.
     * 
     * @return
     *     possible object is
     *     {@link TaskSchedule }
     *     
     */
    public TaskSchedule getTaskSchedule() {
        return taskSchedule;
    }

    /**
     * Sets the value of the taskSchedule property.
     * 
     * @param value
     *     allowed object is
     *     {@link TaskSchedule }
     *     
     */
    public void setTaskSchedule(TaskSchedule value) {
        this.taskSchedule = value;
    }
}
