package org.mcisb.massspectrometry.pride.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

/**
 * Structure allowing the use of controlled or uncontrolled vocabulary
 * 
 * <p>Java class for paramType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="paramType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice maxOccurs="unbounded" minOccurs="0">
 *         &lt;element name="cvParam" type="{}cvParamType"/>
 *         &lt;element name="userParam" type="{}userParamType"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "paramType", propOrder = { "cvParamOrUserParam" })
@XmlSeeAlso({ org.mcisb.massspectrometry.pride.model.SpectrumSettingsType.AcqSpecification.Acquisition.class, org.mcisb.massspectrometry.pride.model.SpectrumSettingsType.SpectrumInstrument.class, FragmentIonType.class, DescriptionType.class })
public class ParamType {

    @XmlElements({ @XmlElement(name = "userParam", type = UserParamType.class), @XmlElement(name = "cvParam", type = CvParamType.class) })
    protected List<Object> cvParamOrUserParam;

    /**
     * Gets the value of the cvParamOrUserParam property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cvParamOrUserParam property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCvParamOrUserParam().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UserParamType }
     * {@link CvParamType }
     * 
     * 
     */
    public List<Object> getCvParamOrUserParam() {
        if (cvParamOrUserParam == null) {
            cvParamOrUserParam = new ArrayList<Object>();
        }
        return this.cvParamOrUserParam;
    }
}
