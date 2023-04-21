package scpn.model.all_projects_singleparticle;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author aquintana <aquintana@cnb.csic.es>
 */
@Embeddable
public class ViewWrappersToHostsPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "wrapper_name", nullable = false)
    private String wrapperName;

    @Basic(optional = false)
    @Column(name = "host_name", nullable = false)
    private String hostName;

    public ViewWrappersToHostsPK() {
    }

    public ViewWrappersToHostsPK(String wrapperName, String hostName) {
        this.wrapperName = wrapperName;
        this.hostName = hostName;
    }

    public String getwrapperName() {
        return wrapperName;
    }

    public void setwrapperName(String wrapperName) {
        this.wrapperName = wrapperName;
    }

    public String gethostName() {
        return hostName;
    }

    public void sethostName(String hostName) {
        this.hostName = hostName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ViewWrappersToHostsPK other = (ViewWrappersToHostsPK) obj;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + (this.wrapperName != null ? this.wrapperName.hashCode() : 0);
        hash = 83 * hash + (this.hostName != null ? this.hostName.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "i2pc.scipion.db.model.all_projects_singleparticle.ViewWrappersToHostPK[wrapperName=" + wrapperName + ", hostName=" + hostName + "]";
    }
}
