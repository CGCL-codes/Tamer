package cu.edu.cujae.biowh.parser.gene.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * This Class is 
 * @author rvera
 * @version 1.0
 * @since Jul 27, 2011
 */
@Embeddable
public class Gene2MIMPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "GeneInfo_WID")
    private long geneInfoWID;

    @Basic(optional = false)
    @Column(name = "MIM")
    private long mim;

    public Gene2MIMPK() {
    }

    public Gene2MIMPK(long geneInfoWID, long mim) {
        this.geneInfoWID = geneInfoWID;
        this.mim = mim;
    }

    public long getGeneInfoWID() {
        return geneInfoWID;
    }

    public void setGeneInfoWID(long geneInfoWID) {
        this.geneInfoWID = geneInfoWID;
    }

    public long getMim() {
        return mim;
    }

    public void setMim(long mim) {
        this.mim = mim;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) geneInfoWID;
        hash += (int) mim;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Gene2MIMPK)) {
            return false;
        }
        Gene2MIMPK other = (Gene2MIMPK) object;
        if (this.geneInfoWID != other.geneInfoWID) {
            return false;
        }
        if (this.mim != other.mim) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cu.edu.cujae.biowh.parser.gene.entities.Gene2MIMPK[ geneInfoWID=" + geneInfoWID + ", mim=" + mim + " ]";
    }
}
