package org.commonlibrary.lcms.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents a link that always points to the latest version of the content of the object it references
 * <p/>
 * @author diegomunguia
 *         Date: Dec 11, 2008
 *         Time: 2:52:31 PM
 *         <p/>
 * @see Link
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "dynamicLink")
@Entity
@Table(name = "clv2_dynamic_links")
public class DynamicLink extends Link {

    /**
     * the referenced (bookmarked) object
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private Linkable linkedObject;

    public DynamicLink() {
        this.linkType = LinkType.DYNAMIC;
    }

    public Linkable getLinkedObject() {
        return linkedObject;
    }

    public void setLinkedObject(Linkable linkedObject) {
        this.linkedObject = linkedObject;
    }

    @Override
    public boolean onEquals(Object o) {
        boolean preResult = super.onEquals(o);
        if (preResult) {
            if (o instanceof DynamicLink) {
                final DynamicLink dl = (DynamicLink) o;
                return linkedObject == null ? dl.getLinkedObject() == null : linkedObject.equals(dl.getLinkedObject());
            }
        }
        return false;
    }

    @Override
    public int onHashCode(int result) {
        result = super.onHashCode(result);
        result = 29 * result + (linkedObject != null ? linkedObject.hashCode() : 0);
        return result;
    }

    @Override
    protected StringBuilder toStringBuilder() {
        StringBuilder sb = super.toStringBuilder();
        sb.append(", linkedObject = ").append(linkedObject);
        return sb;
    }
}
