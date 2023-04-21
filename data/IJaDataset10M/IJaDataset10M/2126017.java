package org.opencms.db.jpa.persistence;

import java.io.*;
import javax.persistence.*;

@Entity
@Table(name = "cms_offline_accesscontrol")
@IdClass(CmsDAOOfflineAccessControl.CmsDAOOfflineAccessControlPK.class)
public class CmsDAOOfflineAccessControl implements I_CmsDAOAccessControl {

    @Basic
    @Column(name = "access_allowed")
    private int m_accessAllowed;

    @Basic
    @Column(name = "access_denied")
    private int m_accessDenied;

    @Basic
    @Column(name = "access_flags")
    private int m_accessFlags;

    @Id
    @Column(name = "principal_id", length = 36)
    private String m_principalId;

    @Id
    @Column(name = "resource_id", length = 36)
    private String m_resourceId;

    public CmsDAOOfflineAccessControl() {
    }

    public CmsDAOOfflineAccessControl(String principalId, String resourceId) {
        this.m_principalId = principalId;
        this.m_resourceId = resourceId;
    }

    public int getAccessAllowed() {
        return m_accessAllowed;
    }

    public void setAccessAllowed(int accessAllowed) {
        this.m_accessAllowed = accessAllowed;
    }

    public int getAccessDenied() {
        return m_accessDenied;
    }

    public void setAccessDenied(int accessDenied) {
        this.m_accessDenied = accessDenied;
    }

    public int getAccessFlags() {
        return m_accessFlags;
    }

    public void setAccessFlags(int accessFlags) {
        this.m_accessFlags = accessFlags;
    }

    public String getPrincipalId() {
        return m_principalId;
    }

    public void setPrincipalId(String principalId) {
        this.m_principalId = principalId;
    }

    public String getResourceId() {
        return m_resourceId;
    }

    public void setResourceId(String resourceId) {
        this.m_resourceId = resourceId;
    }

    public static class CmsDAOOfflineAccessControlPK implements Serializable {

        static {
            try {
                Class.forName("org.opencms.db.jpa.persistence.CmsDAOOfflineAccessControl");
            } catch (Exception e) {
            }
        }

        public String m_principalId;

        public String m_resourceId;

        public CmsDAOOfflineAccessControlPK() {
        }

        public CmsDAOOfflineAccessControlPK(String str) {
            fromString(str);
        }

        public String getPrincipalId() {
            return m_principalId;
        }

        public void setPrincipalId(String principalId) {
            this.m_principalId = principalId;
        }

        public String getResourceId() {
            return m_resourceId;
        }

        public void setResourceId(String resourceId) {
            this.m_resourceId = resourceId;
        }

        public String toString() {
            return m_principalId + "::" + m_resourceId;
        }

        public int hashCode() {
            int rs = 17;
            rs = rs * 37 + ((m_principalId == null) ? 0 : m_principalId.hashCode());
            rs = rs * 37 + ((m_resourceId == null) ? 0 : m_resourceId.hashCode());
            return rs;
        }

        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || obj.getClass() != getClass()) return false;
            CmsDAOOfflineAccessControlPK other = (CmsDAOOfflineAccessControlPK) obj;
            return ((m_principalId == null && other.m_principalId == null) || (m_principalId != null && m_principalId.equals(other.m_principalId))) && ((m_resourceId == null && other.m_resourceId == null) || (m_resourceId != null && m_resourceId.equals(other.m_resourceId)));
        }

        private void fromString(String str) {
            Tokenizer toke = new Tokenizer(str);
            str = toke.nextToken();
            if ("null".equals(str)) m_principalId = null; else m_principalId = str;
            str = toke.nextToken();
            if ("null".equals(str)) m_resourceId = null; else m_resourceId = str;
        }

        protected static class Tokenizer {

            private final String str;

            private int last;

            public Tokenizer(String str) {
                this.str = str;
            }

            public String nextToken() {
                int next = str.indexOf("::", last);
                String part;
                if (next == -1) {
                    part = str.substring(last);
                    last = str.length();
                } else {
                    part = str.substring(last, next);
                    last = next + 2;
                }
                return part;
            }
        }
    }
}
