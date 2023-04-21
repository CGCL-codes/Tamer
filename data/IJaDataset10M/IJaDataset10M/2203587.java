package cu.edu.cujae.biowh.parser.kegg.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * This class is the KEGG Pathway Relation SubType PK
 * @author rvera
 * @version 1.0
 * @since Nov 29, 2011
 */
@Embeddable
public class KEGGPathwayRelationSubTypePK implements Serializable {

    @Basic(optional = false)
    @Column(name = "KEGGPathwayRelation_WID")
    private long kEGGPathwayRelationWID;

    @Basic(optional = false)
    @Column(name = "Name")
    private String name;

    @Basic(optional = false)
    @Column(name = "Value")
    private String value;

    public KEGGPathwayRelationSubTypePK() {
    }

    public KEGGPathwayRelationSubTypePK(long kEGGPathwayRelationWID, String name, String value) {
        this.kEGGPathwayRelationWID = kEGGPathwayRelationWID;
        this.name = name;
        this.value = value;
    }

    public long getKEGGPathwayRelationWID() {
        return kEGGPathwayRelationWID;
    }

    public void setKEGGPathwayRelationWID(long kEGGPathwayRelationWID) {
        this.kEGGPathwayRelationWID = kEGGPathwayRelationWID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) kEGGPathwayRelationWID;
        hash += (name != null ? name.hashCode() : 0);
        hash += (value != null ? value.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof KEGGPathwayRelationSubTypePK)) {
            return false;
        }
        KEGGPathwayRelationSubTypePK other = (KEGGPathwayRelationSubTypePK) object;
        if (this.kEGGPathwayRelationWID != other.kEGGPathwayRelationWID) {
            return false;
        }
        if ((this.name == null && other.name != null) || (this.name != null && !this.name.equals(other.name))) {
            return false;
        }
        if ((this.value == null && other.value != null) || (this.value != null && !this.value.equals(other.value))) {
            return false;
        }
        return true;
    }

    public String toString() {
        return "KEGGPathwayRelationSubTypePK{" + "kEGGPathwayRelationWID=" + kEGGPathwayRelationWID + ", name=" + name + ", value=" + value + '}';
    }
}
