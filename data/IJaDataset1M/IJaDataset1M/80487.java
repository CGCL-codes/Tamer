package cu.edu.cujae.biowh.parser.kegg.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * This class is the KEGG Enzyme DBLink PK entity
 * @author rvera
 * @version 1.0
 * @since Nov 17, 2011
 */
@Embeddable
public class KEGGEnzymeDBLinkPK implements Serializable {

    @Basic(optional = false)
    @Column(name = "KEGGEnzyme_WID")
    private long kEGGEnzymeWID;

    @Basic(optional = false)
    @Column(name = "DB")
    private String db;

    @Basic(optional = false)
    @Column(name = "Id")
    private String id;

    public KEGGEnzymeDBLinkPK() {
    }

    public KEGGEnzymeDBLinkPK(long kEGGEnzymeWID, String db, String id) {
        this.kEGGEnzymeWID = kEGGEnzymeWID;
        this.db = db;
        this.id = id;
    }

    public long getKEGGEnzymeWID() {
        return kEGGEnzymeWID;
    }

    public void setKEGGEnzymeWID(long kEGGEnzymeWID) {
        this.kEGGEnzymeWID = kEGGEnzymeWID;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
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
        hash += (int) kEGGEnzymeWID;
        hash += (db != null ? db.hashCode() : 0);
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof KEGGEnzymeDBLinkPK)) {
            return false;
        }
        KEGGEnzymeDBLinkPK other = (KEGGEnzymeDBLinkPK) object;
        if (this.kEGGEnzymeWID != other.kEGGEnzymeWID) {
            return false;
        }
        if ((this.db == null && other.db != null) || (this.db != null && !this.db.equals(other.db))) {
            return false;
        }
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    public String toString() {
        return "KEGGEnzymeDBLinkPK{" + "kEGGEnzymeWID=" + kEGGEnzymeWID + ", db=" + db + ", id=" + id + '}';
    }
}
