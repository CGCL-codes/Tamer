package jaxb.server;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for ipConf complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ipConf">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="nTriesBan" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="banTimeM" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="adminMail" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ipConf", propOrder = {  })
public class IpConf {

    protected int nTriesBan;

    protected Integer banTimeM;

    protected Boolean adminMail;

    /**
     * Gets the value of the nTriesBan property.
     * 
     */
    public int getNTriesBan() {
        return nTriesBan;
    }

    /**
     * Sets the value of the nTriesBan property.
     * 
     */
    public void setNTriesBan(int value) {
        this.nTriesBan = value;
    }

    /**
     * Gets the value of the banTimeM property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getBanTimeM() {
        return banTimeM;
    }

    /**
     * Sets the value of the banTimeM property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setBanTimeM(Integer value) {
        this.banTimeM = value;
    }

    /**
     * Gets the value of the adminMail property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isAdminMail() {
        return adminMail;
    }

    /**
     * Sets the value of the adminMail property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAdminMail(Boolean value) {
        this.adminMail = value;
    }
}
