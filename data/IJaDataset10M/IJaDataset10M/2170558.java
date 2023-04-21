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
public class ProteinIsoformIdPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "ProteinCommentIsoform_WID")
    private long proteinCommentIsoformWID;

    @Basic(optional = false)
    @Column(name = "Id")
    private String id;

    public ProteinIsoformIdPK() {
    }

    public ProteinIsoformIdPK(long proteinCommentIsoformWID, String id) {
        this.proteinCommentIsoformWID = proteinCommentIsoformWID;
        this.id = id;
    }

    public long getProteinCommentIsoformWID() {
        return proteinCommentIsoformWID;
    }

    public void setProteinCommentIsoformWID(long proteinCommentIsoformWID) {
        this.proteinCommentIsoformWID = proteinCommentIsoformWID;
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
        hash += (int) proteinCommentIsoformWID;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ProteinIsoformIdPK)) {
            return false;
        }
        ProteinIsoformIdPK other = (ProteinIsoformIdPK) object;
        if (this.proteinCommentIsoformWID != other.proteinCommentIsoformWID) {
            return false;
        }
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cu.edu.cujae.biowh.parser.protein.entities.ProteinIsoformIdPK[ proteinCommentIsoformWID=" + proteinCommentIsoformWID + ", id=" + id + " ]";
    }
}
