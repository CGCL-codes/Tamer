package org.nodevision.portal.hibernate.om;

import java.sql.Blob;

public class NvUserportlets implements java.io.Serializable {

    private org.nodevision.portal.hibernate.om.NvUserportletsId id;

    private Blob PortletsList;

    /** default constructor */
    public NvUserportlets() {
    }

    /** constructor with id */
    public NvUserportlets(org.nodevision.portal.hibernate.om.NvUserportletsId id) {
        this.id = id;
    }

    /**
     */
    public org.nodevision.portal.hibernate.om.NvUserportletsId getId() {
        return this.id;
    }

    public void setId(org.nodevision.portal.hibernate.om.NvUserportletsId id) {
        this.id = id;
    }

    /**
     */
    public Blob getPortletsList() {
        return this.PortletsList;
    }

    public void setPortletsList(Blob PortletsList) {
        this.PortletsList = PortletsList;
    }
}
