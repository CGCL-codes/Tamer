package oracle.toplink.essentials.internal.ejb.cmp3.xml.accessors;

import java.util.List;
import oracle.toplink.essentials.internal.ejb.cmp3.metadata.accessors.objects.MetadataAccessibleObject;
import oracle.toplink.essentials.internal.ejb.cmp3.metadata.accessors.OneToOneAccessor;
import oracle.toplink.essentials.internal.ejb.cmp3.metadata.columns.MetadataJoinColumns;
import oracle.toplink.essentials.internal.ejb.cmp3.metadata.columns.MetadataPrimaryKeyJoinColumns;
import oracle.toplink.essentials.internal.ejb.cmp3.xml.XMLHelper;
import oracle.toplink.essentials.internal.ejb.cmp3.xml.columns.XMLJoinColumns;
import oracle.toplink.essentials.internal.ejb.cmp3.xml.columns.XMLPrimaryKeyJoinColumns;
import org.w3c.dom.Node;

/**
 * An extended one to one relationship accessor.
 * 
 * @author Guy Pelletier
 * @since TopLink EJB 3.0 Reference Implementation
 */
public class XMLOneToOneAccessor extends OneToOneAccessor {

    private Node m_node;

    private XMLHelper m_helper;

    /**
     * INTERNAL:
     */
    public XMLOneToOneAccessor(MetadataAccessibleObject accessibleObject, Node node, XMLClassAccessor classAccessor) {
        super(accessibleObject, classAccessor);
        m_node = node;
        m_helper = classAccessor.getHelper();
    }

    /**
     * INTERNAL: (Override from OneToOneAccessor)
     */
    public List<String> getCascadeTypes() {
        return m_helper.getCascadeTypes(m_node);
    }

    /**
     * INTERNAL: (Override from OneToOneAccessor)
     */
    public String getFetchType() {
        return m_helper.getFetchTypeDefaultEAGER(m_node);
    }

    /**
     * INTERNAL: (Override from RelationshipAccessor)
     */
    protected MetadataJoinColumns getJoinColumns() {
        if (m_helper.nodeHasJoinColumns(m_node)) {
            return new XMLJoinColumns(m_node, m_helper);
        } else {
            return super.getJoinColumns();
        }
    }

    /**
     * INTERNAL: (Override from OneToOneAccessor)
     */
    public String getMappedBy() {
        return m_helper.getMappedBy(m_node);
    }

    /**
     * INTERNAL: (Override from MetadataAccessor)
     */
    protected MetadataPrimaryKeyJoinColumns getPrimaryKeyJoinColumns(String sourceTableName, String targetTableName) {
        if (m_helper.nodeHasPrimaryKeyJoinColumns(m_node)) {
            return new XMLPrimaryKeyJoinColumns(m_node, m_helper, sourceTableName, targetTableName);
        } else {
            return super.getPrimaryKeyJoinColumns(sourceTableName, targetTableName);
        }
    }

    /**
     * INTERNAL: (Override from OneToOneAccessor)
     */
    public Class getTargetEntity() {
        return m_helper.getTargetEntity(m_node);
    }

    /**
     * INTERNAL: (Override from RelationshipAccessor)
     * 
     * Return true is this one-to-one has primary key join columns.
     */
    public boolean hasPrimaryKeyJoinColumns() {
        if (m_helper.nodeHasPrimaryKeyJoinColumns(m_node)) {
            return true;
        } else {
            return super.hasPrimaryKeyJoinColumns();
        }
    }

    /**
     * INTERNAL: (Override from OneToOneAccessor)
     */
    public boolean isOptional() {
        return m_helper.isOptional(m_node);
    }
}
