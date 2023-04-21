package pl.swmud.ns.swaedit.highlighter;

import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for highlighter complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="highlighter">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence maxOccurs="unbounded">
 *         &lt;element name="words" type="{http://swmud.pl/ns/swaedit/highlighter}words"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "highlighter", propOrder = { "words" })
public class Highlighter {

    @XmlElement(required = true)
    protected List<Words> words = new LinkedList<Words>();

    /**
     * Gets the value of the words property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the words property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getWords().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Words }
     * 
     * 
     */
    public List<Words> getWords() {
        if (words == null) {
            words = new LinkedList<Words>();
        }
        return this.words;
    }
}
