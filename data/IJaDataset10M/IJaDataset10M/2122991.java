package gov.nist.mel.emergency.cap;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author Guillaume Radde <guillaume.radde@nist.gov>
 */
@XmlType(propOrder = { "resourceDesc", "mimeType", "size", "uri", "derefUri", "digest" })
@Entity
@Table(name = "resource")
@NamedQueries({ @NamedQuery(name = "Resource.findByResourceId", query = "SELECT r FROM Resource r WHERE r.resourceId = :resourceId"), @NamedQuery(name = "Resource.findByResourceDesc", query = "SELECT r FROM Resource r WHERE r.resourceDesc = :resourceDesc"), @NamedQuery(name = "Resource.findByMimeType", query = "SELECT r FROM Resource r WHERE r.mimeType = :mimeType"), @NamedQuery(name = "Resource.findBySize", query = "SELECT r FROM Resource r WHERE r.size = :size"), @NamedQuery(name = "Resource.findByUri", query = "SELECT r FROM Resource r WHERE r.uri = :uri"), @NamedQuery(name = "Resource.findByDerefUri", query = "SELECT r FROM Resource r WHERE r.derefUri = :derefUri"), @NamedQuery(name = "Resource.findByDigest", query = "SELECT r FROM Resource r WHERE r.digest = :digest") })
public class Resource implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resource_id", nullable = false)
    private Integer resourceId;

    @Column(name = "resource_desc", nullable = false)
    private String resourceDesc;

    @Column(name = "mime_type")
    private String mimeType;

    @Column(name = "size")
    private Integer size;

    @Column(name = "uri")
    private String uri;

    @Column(name = "deref_uri")
    private String derefUri;

    @Column(name = "digest")
    private String digest;

    @JoinColumn(name = "info_id", referencedColumnName = "info_id")
    @ManyToOne
    private Info infoId;

    public Resource() {
    }

    public Resource(Integer resourceId) {
        this.resourceId = resourceId;
    }

    public Resource(Integer resourceId, String resourceDesc) {
        this.resourceId = resourceId;
        this.resourceDesc = resourceDesc;
    }

    @XmlTransient
    public Integer getResourceId() {
        return resourceId;
    }

    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

    @XmlElement(namespace = "urn:oasis:names:tc:emergency:cap:1.1")
    public String getResourceDesc() {
        return resourceDesc;
    }

    public void setResourceDesc(String resourceDesc) {
        this.resourceDesc = resourceDesc;
    }

    @XmlElement(namespace = "urn:oasis:names:tc:emergency:cap:1.1")
    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    @XmlElement(namespace = "urn:oasis:names:tc:emergency:cap:1.1")
    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @XmlElement(namespace = "urn:oasis:names:tc:emergency:cap:1.1")
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @XmlElement(namespace = "urn:oasis:names:tc:emergency:cap:1.1")
    public String getDerefUri() {
        return derefUri;
    }

    public void setDerefUri(String derefUri) {
        this.derefUri = derefUri;
    }

    @XmlElement(namespace = "urn:oasis:names:tc:emergency:cap:1.1")
    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    @XmlTransient
    public Info getInfoId() {
        return infoId;
    }

    public void setInfoId(Info infoId) {
        this.infoId = infoId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (resourceId != null ? resourceId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Resource)) {
            return false;
        }
        Resource other = (Resource) object;
        if ((this.resourceId == null && other.resourceId != null) || (this.resourceId != null && !this.resourceId.equals(other.resourceId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "exercisecontrolsystem.entities.Resource[resourceId=" + resourceId + "]";
    }
}
