package cu.edu.cujae.biowh.parser.protein.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * This Class is 
 * @author rvera
 * @version 1.0
 * @since Aug 11, 2011
 */
@Embeddable
public class ProteinBioCycPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "Protein_WID")
    private long proteinWID;

    @Basic(optional = false)
    @Column(name = "Id")
    private String id;

    public ProteinBioCycPK() {
    }

    public ProteinBioCycPK(long proteinWID, String id) {
        this.proteinWID = proteinWID;
        this.id = id;
    }

    public long getProteinWID() {
        return proteinWID;
    }

    public void setProteinWID(long proteinWID) {
        this.proteinWID = proteinWID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) proteinWID;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ProteinBioCycPK)) {
            return false;
        }
        ProteinBioCycPK other = (ProteinBioCycPK) object;
        if (this.proteinWID != other.proteinWID) {
            return false;
        }
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cu.edu.cujae.biowh.parser.protein.entities.ProteinBioCycPK[ proteinWID=" + proteinWID + ", id=" + id + " ]";
    }
}
