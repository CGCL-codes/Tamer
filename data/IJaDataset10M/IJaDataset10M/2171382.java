package org.opencms.db.jpa.persistence;

import javax.persistence.*;

@Entity
@Table(name = "cms_history_propertydef")
public class CmsDAOHistoryPropertyDef {

    @Id
    @Column(name = "propertydef_id", length = 36)
    private String m_propertyDefId;

    @Basic
    @Column(name = "propertydef_name", nullable = false, length = 128)
    private String m_propertyDefName;

    @Basic
    @Column(name = "propertydef_type")
    private int m_propertyDefType;

    public CmsDAOHistoryPropertyDef() {
    }

    public CmsDAOHistoryPropertyDef(String propertydefId) {
        this.m_propertyDefId = propertydefId;
    }

    public String getPropertyDefId() {
        return m_propertyDefId;
    }

    public void setPropertyDefId(String propertydefId) {
        this.m_propertyDefId = propertydefId;
    }

    public String getPropertyDefName() {
        return m_propertyDefName;
    }

    public void setPropertyDefName(String propertydefName) {
        this.m_propertyDefName = propertydefName;
    }

    public int getPropertyDefType() {
        return m_propertyDefType;
    }

    public void setPropertyDefType(int propertydefType) {
        this.m_propertyDefType = propertydefType;
    }
}
