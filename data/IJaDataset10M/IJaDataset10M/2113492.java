package org.openwms.core.domain.search;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * Encapsulates a collection of {@link Tag}s and is mapped to the XML type
 * "tags" in the ui-actions-schema.xsd.
 * <p>
 * <a href="http://www.openwms.org/schema/ui-actions-schema.xsd">http://www.
 * openwms.org/schema/ui-actions-schema.xsd</a>
 * </p>
 * 
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 * @version $Revision: $
 * @since 0.2
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tags", propOrder = { "tag" })
public class Tags {

    /**
     * A List of <code>Tag</code>s.
     */
    protected List<Tag> tag;

    /**
     * Gets the value of the tag property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the tag property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getTag().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list {@link Tag }
     * 
     * @return The List of Tags wrapped by this instance
     */
    public List<Tag> getTag() {
        if (tag == null) {
            tag = new ArrayList<Tag>();
        }
        return this.tag;
    }
}
