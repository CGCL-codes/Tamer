package shu.cms.colorformat.cxf;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.*;

/**
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "ICC-ProfileLink")
public class ICCProfileLink {

    @XmlAttribute(name = "UniqueID")
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String uniqueID;

    /**
   * Gets the value of the uniqueID property.
   *
   * @return
   *     possible object is
   *     {@link String }
   *
   */
    public String getUniqueID() {
        return uniqueID;
    }

    /**
   * Sets the value of the uniqueID property.
   *
   * @param value
   *     allowed object is
   *     {@link String }
   *
   */
    public void setUniqueID(String value) {
        this.uniqueID = value;
    }
}
