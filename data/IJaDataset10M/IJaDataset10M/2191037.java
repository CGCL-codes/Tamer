package gov.nih.nlm.ncbi.soap.eutils.efetch_pubmed;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for HistoryType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="HistoryType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PubMedPubDate" type="{http://www.ncbi.nlm.nih.gov/soap/eutils/efetch_pubmed}PubMedPubDateType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HistoryType", propOrder = { "pubMedPubDate" })
public class HistoryType {

    @XmlElement(name = "PubMedPubDate", required = true)
    protected List<PubMedPubDateType> pubMedPubDate;

    /**
     * Gets the value of the pubMedPubDate property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the pubMedPubDate property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPubMedPubDate().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PubMedPubDateType }
     * 
     * 
     */
    public List<PubMedPubDateType> getPubMedPubDate() {
        if (pubMedPubDate == null) {
            pubMedPubDate = new ArrayList<PubMedPubDateType>();
        }
        return this.pubMedPubDate;
    }
}
