package scpn.model.all_projects_singleparticle;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author aquintana <aquintana@cnb.csic.es>
 */
@Entity
@Table(name = "wrappers_hosts", catalog = "scipion", schema = "all_projects_singleparticle")
@NamedQueries({ @NamedQuery(name = "WrappersHosts.findAll", query = "SELECT w FROM WrappersHosts w"), @NamedQuery(name = "WrappersHosts.findByHostId", query = "SELECT w FROM WrappersHosts w WHERE w.wrappersHostsPK.hostId = :hostId"), @NamedQuery(name = "WrappersHosts.findByWrapperId", query = "SELECT w FROM WrappersHosts w WHERE w.wrappersHostsPK.wrapperId = :wrapperId") })
public class WrappersHosts implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    protected WrappersHostsPK wrappersHostsPK;

    @JoinColumn(name = "wrapper_id", referencedColumnName = "wrapper_id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Wrappers wrappers;

    public WrappersHosts() {
    }

    public WrappersHosts(WrappersHostsPK wrappersHostsPK) {
        this.wrappersHostsPK = wrappersHostsPK;
    }

    public WrappersHosts(int hostId, int wrapperId) {
        this.wrappersHostsPK = new WrappersHostsPK(hostId, wrapperId);
    }

    public WrappersHostsPK getWrappersHostsPK() {
        return wrappersHostsPK;
    }

    public void setWrappersHostsPK(WrappersHostsPK wrappersHostsPK) {
        this.wrappersHostsPK = wrappersHostsPK;
    }

    public Wrappers getWrappers() {
        return wrappers;
    }

    public void setWrappers(Wrappers wrappers) {
        this.wrappers = wrappers;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (wrappersHostsPK != null ? wrappersHostsPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof WrappersHosts)) {
            return false;
        }
        WrappersHosts other = (WrappersHosts) object;
        if ((this.wrappersHostsPK == null && other.wrappersHostsPK != null) || (this.wrappersHostsPK != null && !this.wrappersHostsPK.equals(other.wrappersHostsPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "scpn.model.all_projects_singleparticle.WrappersHosts[wrappersHostsPK=" + wrappersHostsPK + "]";
    }
}
