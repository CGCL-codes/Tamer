package eu.vph.predict.vre.in_silico.business.application.chaste.chaste_parameters.jaxb.v1_0;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for ionic_models_type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ionic_models_type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Default" type="{}ionic_models_available_type"/>
 *         &lt;element name="Region" type="{}ionic_model_region_type" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ionic_models_type", propOrder = { "_default", "region" })
public class IonicModelsType {

    @XmlElement(name = "Default", required = true)
    protected IonicModelsAvailableType _default;

    @XmlElement(name = "Region")
    protected List<IonicModelRegionType> region;

    /**
     * Gets the value of the default property.
     * 
     * @return
     *     possible object is
     *     {@link IonicModelsAvailableType }
     *     
     */
    public IonicModelsAvailableType getDefault() {
        return _default;
    }

    /**
     * Sets the value of the default property.
     * 
     * @param value
     *     allowed object is
     *     {@link IonicModelsAvailableType }
     *     
     */
    public void setDefault(IonicModelsAvailableType value) {
        this._default = value;
    }

    /**
     * Gets the value of the region property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the region property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRegion().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IonicModelRegionType }
     * 
     * 
     */
    public List<IonicModelRegionType> getRegion() {
        if (region == null) {
            region = new ArrayList<IonicModelRegionType>();
        }
        return this.region;
    }
}
