package uk.icat3.jaxb.gen;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>Java class for Datafile complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Datafile">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{}str255"/>
 *         &lt;element name="location" type="{}str4000"/>
 *         &lt;element name="description" type="{}str4000" minOccurs="0"/>
 *         &lt;element name="datafile_version" type="{}str255" minOccurs="0"/>
 *         &lt;element name="datafile_version_comment" type="{}str255" minOccurs="0"/>
 *         &lt;element name="datafile_format" type="{}str255" minOccurs="0"/>
 *         &lt;element name="datafile_format_version" type="{}str255" minOccurs="0"/>
 *         &lt;element name="datafile_create_time" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="datafile_modify_time" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="file_size" type="{http://www.w3.org/2001/XMLSchema}integer" minOccurs="0"/>
 *         &lt;element name="command" type="{}str4000" minOccurs="0"/>
 *         &lt;element name="checksum" type="{}str255" minOccurs="0"/>
 *         &lt;element name="signature" type="{}str4000" minOccurs="0"/>
 *         &lt;element name="parameter" type="{}Parameter" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Datafile", propOrder = { "name", "location", "description", "datafileVersion", "datafileVersionComment", "datafileFormat", "datafileFormatVersion", "datafileCreateTime", "datafileModifyTime", "fileSize", "command", "checksum", "signature", "parameter" })
public class Datafile {

    @XmlElement(required = true)
    protected String name;

    @XmlElement(required = true)
    protected String location;

    protected String description;

    @XmlElement(name = "datafile_version")
    protected String datafileVersion;

    @XmlElement(name = "datafile_version_comment")
    protected String datafileVersionComment;

    @XmlElement(name = "datafile_format")
    protected String datafileFormat;

    @XmlElement(name = "datafile_format_version")
    protected String datafileFormatVersion;

    @XmlElement(name = "datafile_create_time", required = true)
    protected XMLGregorianCalendar datafileCreateTime;

    @XmlElement(name = "datafile_modify_time", required = true)
    protected XMLGregorianCalendar datafileModifyTime;

    @XmlElement(name = "file_size")
    protected BigInteger fileSize;

    protected String command;

    protected String checksum;

    protected String signature;

    protected List<Parameter> parameter;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the location property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the value of the location property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocation(String value) {
        this.location = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the datafileVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatafileVersion() {
        return datafileVersion;
    }

    /**
     * Sets the value of the datafileVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatafileVersion(String value) {
        this.datafileVersion = value;
    }

    /**
     * Gets the value of the datafileVersionComment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatafileVersionComment() {
        return datafileVersionComment;
    }

    /**
     * Sets the value of the datafileVersionComment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatafileVersionComment(String value) {
        this.datafileVersionComment = value;
    }

    /**
     * Gets the value of the datafileFormat property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatafileFormat() {
        return datafileFormat;
    }

    /**
     * Sets the value of the datafileFormat property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatafileFormat(String value) {
        this.datafileFormat = value;
    }

    /**
     * Gets the value of the datafileFormatVersion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDatafileFormatVersion() {
        return datafileFormatVersion;
    }

    /**
     * Sets the value of the datafileFormatVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDatafileFormatVersion(String value) {
        this.datafileFormatVersion = value;
    }

    /**
     * Gets the value of the datafileCreateTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDatafileCreateTime() {
        return datafileCreateTime;
    }

    /**
     * Sets the value of the datafileCreateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDatafileCreateTime(XMLGregorianCalendar value) {
        this.datafileCreateTime = value;
    }

    /**
     * Gets the value of the datafileModifyTime property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDatafileModifyTime() {
        return datafileModifyTime;
    }

    /**
     * Sets the value of the datafileModifyTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDatafileModifyTime(XMLGregorianCalendar value) {
        this.datafileModifyTime = value;
    }

    /**
     * Gets the value of the fileSize property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getFileSize() {
        return fileSize;
    }

    /**
     * Sets the value of the fileSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setFileSize(BigInteger value) {
        this.fileSize = value;
    }

    /**
     * Gets the value of the command property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCommand() {
        return command;
    }

    /**
     * Sets the value of the command property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCommand(String value) {
        this.command = value;
    }

    /**
     * Gets the value of the checksum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChecksum() {
        return checksum;
    }

    /**
     * Sets the value of the checksum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChecksum(String value) {
        this.checksum = value;
    }

    /**
     * Gets the value of the signature property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSignature() {
        return signature;
    }

    /**
     * Sets the value of the signature property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSignature(String value) {
        this.signature = value;
    }

    /**
     * Gets the value of the parameter property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the parameter property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getParameter().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Parameter }
     * 
     * 
     */
    public List<Parameter> getParameter() {
        if (parameter == null) {
            parameter = new ArrayList<Parameter>();
        }
        return this.parameter;
    }
}
