package org.iptc.nar.core.datatype;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * The Link Component offers a generic mechanism for linking items within NAR
 * framework as well as creating links from Items to other Web resources.
 * </p>
 * <p>
 * Links have several usages:
 * <ul>
 * <li><b>Navigation links</b> allow for navigation from an Item to another
 * related item or a Web resource.</li>
 * <li><b>Derivation links</b> allow for the expression of a parent/child
 * relationship. An example is a cropped picture, derived from an original RAW
 * picture.</li>
 * <li><b>Attachement links</b> allow for the attachment of an item to the
 * current one. An example is the attachment of a picture to a textual story.</li>
 * <li><b>Instance Links</b> allow for a dynamic association of Items
 * according to their relationship with a given resource, such a Web page or
 * specific Item. Such link can be used for example to represent a daily top
 * news package, by its association with a home page of a web site</li>
 * <li><b>Composition links</b> are used in a Package Item to aggregate the
 * Items included in the package.</li>
 * </ul>
 * </p>
 */
public class LinkType {

    private Long m_identity;

    /**
	 * the Item Identifier of the target Item, or the locator of the target Web
	 * resource.
	 */
    private URI m_targetIdentifier;

    /**
	 * The provider’s identifier of the target resource
	 */
    private String m_residref;

    /**
	 * @deprecated An IANA MIME type. From NAR 1.2, the use of this attribute is
	 *             deprecated; contentype should be used instead.
	 */
    private String m_targetType;

    /**
	 * The IANA (Internet Assigned Numbers Authority) MIME type of the target
	 * resource.
	 */
    private String m_contentType;

    /**
	 * The identifier of the relationship between the current item and the
	 * target resource. The precise processing model of this property is still
	 * to be defined, as is the associated IPTC controlled vocabulary.
	 */
    private QCodeType m_relationshipIndicator;

    /**
	 * the size
	 */
    private Integer m_targetSize;

    private List<LabelType> m_titles = new LinkedList<LabelType>();

    public Long getIdentity() {
        return m_identity;
    }

    public void setIdentity(Long identity) {
        m_identity = identity;
    }

    public URI getTargetIdentifier() {
        return m_targetIdentifier;
    }

    public void setTargetIdentifier(URI href) {
        this.m_targetIdentifier = href;
    }

    public String getResidref() {
        return m_residref;
    }

    public void setResidref(String residref) {
        m_residref = residref;
    }

    /**
	 * @deprecated use getContentType instead
	 * @return
	 */
    public String getTargetType() {
        return m_targetType;
    }

    /**
	 * @deprecated use setContentType instead
	 * @param hrefType
	 */
    public void setTargetType(String hrefType) {
        this.m_targetType = hrefType;
    }

    public String getContentType() {
        return m_contentType;
    }

    public void setContentType(String contentType) {
        m_contentType = contentType;
    }

    public List<LabelType> getTitles() {
        return m_titles;
    }

    public void setTitles(List<LabelType> ltitle) {
        this.m_titles = ltitle;
    }

    public void addTitle(LabelType title) {
        m_titles.add(title);
    }

    public void removeTitle(LabelType title) {
        m_titles.remove(title);
    }

    public QCodeType getRelationshipIndicator() {
        return m_relationshipIndicator;
    }

    public void setRelationshipIndicator(QCodeType relationshipIndicator) {
        m_relationshipIndicator = relationshipIndicator;
    }

    /**
	 * 
	 * @return
	 */
    public Integer getTargetSize() {
        return m_targetSize;
    }

    public void setTargetSize(Integer size) {
        m_targetSize = size;
    }
}
