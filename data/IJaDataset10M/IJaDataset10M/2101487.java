package de.sepp.aigaebeditormodule.jaxb.gaeb.schema;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Informationen zum LV
 * 
 * <p>Java class for tgBoQInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tgBoQInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Name" type="{http://www.gaeb.de/GAEB_DA_XML/200407}tgNormalizedString20"/>
 *         &lt;element name="LblBoQ" type="{http://www.gaeb.de/GAEB_DA_XML/200407}tgNormalizedString60"/>
 *         &lt;element name="CPVCode" type="{http://www.gaeb.de/GAEB_DA_XML/200407}tgCPVCode" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;sequence minOccurs="0">
 *           &lt;element name="CONo" type="{http://www.gaeb.de/GAEB_DA_XML/200407}tgCONo"/>
 *           &lt;element name="COStatus" type="{http://www.gaeb.de/GAEB_DA_XML/200407}tgCOStatus"/>
 *         &lt;/sequence>
 *         &lt;element name="Date" type="{http://www.gaeb.de/GAEB_DA_XML/200407}tgDate" minOccurs="0"/>
 *         &lt;element name="OutlCompl">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.gaeb.de/GAEB_DA_XML/200407}tgNormalizedString">
 *               &lt;enumeration value="AllTxt"/>
 *               &lt;enumeration value="OutTxt"/>
 *               &lt;enumeration value="DetailTxt"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="BoQBkdn" type="{http://www.gaeb.de/GAEB_DA_XML/200407}tgBoQBkdn" maxOccurs="7"/>
 *         &lt;sequence minOccurs="0">
 *           &lt;element name="NoUPComps">
 *             &lt;simpleType>
 *               &lt;restriction base="{http://www.gaeb.de/GAEB_DA_XML/200407}tgNormalizedString">
 *                 &lt;enumeration value="0"/>
 *                 &lt;enumeration value="2"/>
 *                 &lt;enumeration value="3"/>
 *                 &lt;enumeration value="4"/>
 *                 &lt;enumeration value="5"/>
 *                 &lt;enumeration value="6"/>
 *               &lt;/restriction>
 *             &lt;/simpleType>
 *           &lt;/element>
 *           &lt;element name="LblUPComp1" type="{http://www.gaeb.de/GAEB_DA_XML/200407}tgNormalizedString20" minOccurs="0"/>
 *           &lt;element name="LblUPComp2" type="{http://www.gaeb.de/GAEB_DA_XML/200407}tgNormalizedString20" minOccurs="0"/>
 *           &lt;element name="LblUPComp3" type="{http://www.gaeb.de/GAEB_DA_XML/200407}tgNormalizedString20" minOccurs="0"/>
 *           &lt;element name="LblUPComp4" type="{http://www.gaeb.de/GAEB_DA_XML/200407}tgNormalizedString20" minOccurs="0"/>
 *           &lt;element name="LblUPComp5" type="{http://www.gaeb.de/GAEB_DA_XML/200407}tgNormalizedString20" minOccurs="0"/>
 *           &lt;element name="LblUPComp6" type="{http://www.gaeb.de/GAEB_DA_XML/200407}tgNormalizedString20" minOccurs="0"/>
 *         &lt;/sequence>
 *         &lt;element name="LblTime" type="{http://www.gaeb.de/GAEB_DA_XML/200407}tgNormalizedString20" minOccurs="0"/>
 *         &lt;element name="CtlgAssign" type="{http://www.gaeb.de/GAEB_DA_XML/200407}tgCtlgAssign" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Totals" type="{http://www.gaeb.de/GAEB_DA_XML/200407}tgTotals" minOccurs="0"/>
 *         &lt;element name="Ctlg" type="{http://www.gaeb.de/GAEB_DA_XML/200407}tgCtlg" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tgBoQInfo", propOrder = { "name", "lblBoQ", "cpvCode", "coNo", "coStatus", "date", "outlCompl", "boQBkdn", "noUPComps", "lblUPComp1", "lblUPComp2", "lblUPComp3", "lblUPComp4", "lblUPComp5", "lblUPComp6", "lblTime", "ctlgAssign", "totals", "ctlg" })
public class TgBoQInfo {

    @XmlElement(name = "Name", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String name;

    @XmlElement(name = "LblBoQ", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String lblBoQ;

    @XmlElement(name = "CPVCode")
    protected List<TgCPVCode> cpvCode;

    @XmlElement(name = "CONo")
    protected Integer coNo;

    @XmlElement(name = "COStatus")
    protected TgCOStatus coStatus;

    @XmlElement(name = "Date")
    protected XMLGregorianCalendar date;

    @XmlElement(name = "OutlCompl", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String outlCompl;

    @XmlElement(name = "BoQBkdn", required = true)
    protected List<TgBoQBkdn> boQBkdn;

    @XmlElement(name = "NoUPComps")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String noUPComps;

    @XmlElement(name = "LblUPComp1")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String lblUPComp1;

    @XmlElement(name = "LblUPComp2")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String lblUPComp2;

    @XmlElement(name = "LblUPComp3")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String lblUPComp3;

    @XmlElement(name = "LblUPComp4")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String lblUPComp4;

    @XmlElement(name = "LblUPComp5")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String lblUPComp5;

    @XmlElement(name = "LblUPComp6")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String lblUPComp6;

    @XmlElement(name = "LblTime")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String lblTime;

    @XmlElement(name = "CtlgAssign")
    protected List<TgCtlgAssign> ctlgAssign;

    @XmlElement(name = "Totals")
    protected TgTotals totals;

    @XmlElement(name = "Ctlg")
    protected List<TgCtlg> ctlg;

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
     * Gets the value of the lblBoQ property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLblBoQ() {
        return lblBoQ;
    }

    /**
     * Sets the value of the lblBoQ property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLblBoQ(String value) {
        this.lblBoQ = value;
    }

    /**
     * Gets the value of the cpvCode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cpvCode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCPVCode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TgCPVCode }
     * 
     * 
     */
    public List<TgCPVCode> getCPVCode() {
        if (cpvCode == null) {
            cpvCode = new ArrayList<TgCPVCode>();
        }
        return this.cpvCode;
    }

    /**
     * Gets the value of the coNo property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getCONo() {
        return coNo;
    }

    /**
     * Sets the value of the coNo property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setCONo(Integer value) {
        this.coNo = value;
    }

    /**
     * Gets the value of the coStatus property.
     * 
     * @return
     *     possible object is
     *     {@link TgCOStatus }
     *     
     */
    public TgCOStatus getCOStatus() {
        return coStatus;
    }

    /**
     * Sets the value of the coStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link TgCOStatus }
     *     
     */
    public void setCOStatus(TgCOStatus value) {
        this.coStatus = value;
    }

    /**
     * Gets the value of the date property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDate() {
        return date;
    }

    /**
     * Sets the value of the date property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDate(XMLGregorianCalendar value) {
        this.date = value;
    }

    /**
     * Gets the value of the outlCompl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOutlCompl() {
        return outlCompl;
    }

    /**
     * Sets the value of the outlCompl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOutlCompl(String value) {
        this.outlCompl = value;
    }

    /**
     * Gets the value of the boQBkdn property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the boQBkdn property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBoQBkdn().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TgBoQBkdn }
     * 
     * 
     */
    public List<TgBoQBkdn> getBoQBkdn() {
        if (boQBkdn == null) {
            boQBkdn = new ArrayList<TgBoQBkdn>();
        }
        return this.boQBkdn;
    }

    /**
     * Gets the value of the noUPComps property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNoUPComps() {
        return noUPComps;
    }

    /**
     * Sets the value of the noUPComps property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNoUPComps(String value) {
        this.noUPComps = value;
    }

    /**
     * Gets the value of the lblUPComp1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLblUPComp1() {
        return lblUPComp1;
    }

    /**
     * Sets the value of the lblUPComp1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLblUPComp1(String value) {
        this.lblUPComp1 = value;
    }

    /**
     * Gets the value of the lblUPComp2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLblUPComp2() {
        return lblUPComp2;
    }

    /**
     * Sets the value of the lblUPComp2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLblUPComp2(String value) {
        this.lblUPComp2 = value;
    }

    /**
     * Gets the value of the lblUPComp3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLblUPComp3() {
        return lblUPComp3;
    }

    /**
     * Sets the value of the lblUPComp3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLblUPComp3(String value) {
        this.lblUPComp3 = value;
    }

    /**
     * Gets the value of the lblUPComp4 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLblUPComp4() {
        return lblUPComp4;
    }

    /**
     * Sets the value of the lblUPComp4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLblUPComp4(String value) {
        this.lblUPComp4 = value;
    }

    /**
     * Gets the value of the lblUPComp5 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLblUPComp5() {
        return lblUPComp5;
    }

    /**
     * Sets the value of the lblUPComp5 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLblUPComp5(String value) {
        this.lblUPComp5 = value;
    }

    /**
     * Gets the value of the lblUPComp6 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLblUPComp6() {
        return lblUPComp6;
    }

    /**
     * Sets the value of the lblUPComp6 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLblUPComp6(String value) {
        this.lblUPComp6 = value;
    }

    /**
     * Gets the value of the lblTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLblTime() {
        return lblTime;
    }

    /**
     * Sets the value of the lblTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLblTime(String value) {
        this.lblTime = value;
    }

    /**
     * Gets the value of the ctlgAssign property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ctlgAssign property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCtlgAssign().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TgCtlgAssign }
     * 
     * 
     */
    public List<TgCtlgAssign> getCtlgAssign() {
        if (ctlgAssign == null) {
            ctlgAssign = new ArrayList<TgCtlgAssign>();
        }
        return this.ctlgAssign;
    }

    /**
     * Gets the value of the totals property.
     * 
     * @return
     *     possible object is
     *     {@link TgTotals }
     *     
     */
    public TgTotals getTotals() {
        return totals;
    }

    /**
     * Sets the value of the totals property.
     * 
     * @param value
     *     allowed object is
     *     {@link TgTotals }
     *     
     */
    public void setTotals(TgTotals value) {
        this.totals = value;
    }

    /**
     * Gets the value of the ctlg property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ctlg property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCtlg().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TgCtlg }
     * 
     * 
     */
    public List<TgCtlg> getCtlg() {
        if (ctlg == null) {
            ctlg = new ArrayList<TgCtlg>();
        }
        return this.ctlg;
    }
}
