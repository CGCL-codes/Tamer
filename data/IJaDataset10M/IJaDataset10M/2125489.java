package au.com.thinkingrock.xsd.tr.view.actions.screens;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for SortColumns complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="SortColumns">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *         &lt;element ref="{http://thinkingrock.com.au/xsd/tr/view/actions/screens}sortColumn"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SortColumns", propOrder = { "sortColumn" })
public class SortColumns {

    protected List<SortColumn> sortColumn;

    /**
     * Gets the value of the sortColumn property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sortColumn property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSortColumn().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SortColumn }
     *
     *
     */
    public List<SortColumn> getSortColumns() {
        if (sortColumn == null) {
            sortColumn = new ArrayList<SortColumn>();
        }
        return this.sortColumn;
    }
}