package org.ministone.portal.domain;

/**
 * 
 *@author Sun Wenju
 *@since 0.1
 */
public abstract class PortalConfig implements java.io.Serializable {

    /**
     * The serial version UID of this class. Needed for serialization.
     */
    private static final long serialVersionUID = -6375396473643771776L;

    private java.lang.String portalName;

    /**
     * 
     */
    public java.lang.String getPortalName() {
        return this.portalName;
    }

    public void setPortalName(java.lang.String portalName) {
        this.portalName = portalName;
    }

    private java.lang.String remark;

    /**
     * 
     */
    public java.lang.String getRemark() {
        return this.remark;
    }

    public void setRemark(java.lang.String remark) {
        this.remark = remark;
    }

    private java.lang.String id;

    /**
     * 
     */
    public java.lang.String getId() {
        return this.id;
    }

    public void setId(java.lang.String id) {
        this.id = id;
    }

    private java.util.Collection pageConfigs = new java.util.HashSet();

    /**
     * 
     */
    public java.util.Collection getPageConfigs() {
        return this.pageConfigs;
    }

    public void setPageConfigs(java.util.Collection pageConfigs) {
        this.pageConfigs = pageConfigs;
    }

    private org.ministone.portal.domain.PageConfig defaultPage;

    /**
     * 
     */
    public org.ministone.portal.domain.PageConfig getDefaultPage() {
        return this.defaultPage;
    }

    public void setDefaultPage(org.ministone.portal.domain.PageConfig defaultPage) {
        this.defaultPage = defaultPage;
    }

    /**
     * Returns <code>true</code> if the argument is an PortalConfig instance and all identifiers for this entity
     * equal the identifiers of the argument entity. Returns <code>false</code> otherwise.
     */
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof PortalConfig)) {
            return false;
        }
        final PortalConfig that = (PortalConfig) object;
        if (this.id == null || that.getId() == null || !this.id.equals(that.getId())) {
            return false;
        }
        return true;
    }

    /**
     * Returns a hash code based on this entity's identifiers.
     */
    public int hashCode() {
        int hashCode = 0;
        hashCode = 29 * hashCode + (id == null ? 0 : id.hashCode());
        return hashCode;
    }

    /**
     * Constructs new instances of {@link org.ministone.portal.domain.PortalConfig}.
     */
    public static final class Factory {

        /**
         * Constructs a new instance of {@link org.ministone.portal.domain.PortalConfig}.
         */
        public static org.ministone.portal.domain.PortalConfig newInstance() {
            return new org.ministone.portal.domain.PortalConfigImpl();
        }

        /**
         * Constructs a new instance of {@link org.ministone.portal.domain.PortalConfig}, taking all required and/or
         * read-only properties as arguments.
         */
        public static org.ministone.portal.domain.PortalConfig newInstance(java.lang.String portalName) {
            final org.ministone.portal.domain.PortalConfig entity = new org.ministone.portal.domain.PortalConfigImpl();
            entity.setPortalName(portalName);
            return entity;
        }

        /**
         * Constructs a new instance of {@link org.ministone.portal.domain.PortalConfig}, taking all possible properties
         * (except the identifier(s))as arguments.
         */
        public static org.ministone.portal.domain.PortalConfig newInstance(java.lang.String portalName, java.lang.String remark, java.util.Collection pageConfigs, org.ministone.portal.domain.PageConfig defaultPage) {
            final org.ministone.portal.domain.PortalConfig entity = new org.ministone.portal.domain.PortalConfigImpl();
            entity.setPortalName(portalName);
            entity.setRemark(remark);
            entity.setPageConfigs(pageConfigs);
            entity.setDefaultPage(defaultPage);
            return entity;
        }
    }
}
