package epcglobal.epcis_masterdata.xsd._1;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for VocabularyListType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VocabularyListType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Vocabulary" type="{urn:epcglobal:epcis-masterdata:xsd:1}VocabularyType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VocabularyListType", propOrder = { "vocabularies" })
public class VocabularyListType {

    @XmlElement(name = "Vocabulary")
    protected List<VocabularyType> vocabularies;

    /**
     * Gets the value of the vocabularies property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the vocabularies property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getVocabularies().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link VocabularyType }
     * 
     * 
     */
    public List<VocabularyType> getVocabularies() {
        if (vocabularies == null) {
            vocabularies = new ArrayList<VocabularyType>();
        }
        return this.vocabularies;
    }
}
