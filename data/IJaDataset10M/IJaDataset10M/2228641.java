package org.opencms.db.jpa.persistence;

import javax.persistence.*;

@Entity
@Table(name = "cms_offline_properties", uniqueConstraints = @UniqueConstraint(columnNames = { "propertydef_id", "property_mapping_id" }))
public class CmsDAOOfflineProperties implements I_CmsDAOProperties {

    @Id
    @Column(name = "property_id", length = 36)
    private String m_propertyId;

    @Basic
    @Column(name = "property_mapping_id", nullable = false, length = 36)
    private String m_propertyMappingId;

    @Basic
    @Column(name = "property_mapping_type")
    private int m_propertyMappingType;

    @Basic
    @Column(name = "property_value", nullable = false, length = 2048)
    private String m_propertyValue;

    @Basic
    @Column(name = "propertydef_id", nullable = false, length = 36)
    private String m_propertyDefId;

    public CmsDAOOfflineProperties() {
    }

    public CmsDAOOfflineProperties(String propertyId) {
        this.m_propertyId = propertyId;
    }

    public String getPropertyId() {
        return m_propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.m_propertyId = propertyId;
    }

    public String getPropertyMappingId() {
        return m_propertyMappingId;
    }

    public void setPropertyMappingId(String propertyMappingId) {
        this.m_propertyMappingId = propertyMappingId;
    }

    public int getPropertyMappingType() {
        return m_propertyMappingType;
    }

    public void setPropertyMappingType(int propertyMappingType) {
        this.m_propertyMappingType = propertyMappingType;
    }

    public String getPropertyValue() {
        return m_propertyValue;
    }

    public void setPropertyValue(String propertyValue) {
        this.m_propertyValue = propertyValue;
    }

    public String getPropertyDefId() {
        return m_propertyDefId;
    }

    public void setPropertyDefId(String propertydefId) {
        this.m_propertyDefId = propertydefId;
    }
}
