package edu.unibi.agbi.dawismd.entities.biodwh.transpath.pathway;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author Benjamin Kormeier
 * @version 1.0 14.05.2008
 */
@Embeddable
public class PathwayMoleculesInvolvedId implements Serializable {

    private static final long serialVersionUID = 8972715569351811271L;

    @ManyToOne
    @JoinColumn(name = "pathway_id")
    private Pathway pathwayId = new Pathway();

    @Column(name = "molecule_id")
    private String moleculeId = new String();

    /**
	 * @return the pathwayId
	 */
    public Pathway getPathwayId() {
        return pathwayId;
    }

    /**
	 * @param pathwayId the pathwayId to set
	 */
    public void setPathwayId(Pathway pathwayId) {
        this.pathwayId = pathwayId;
    }

    /**
	 * @return the moleculenId
	 */
    public String getMoleculeId() {
        return moleculeId;
    }

    /**
	 * @param moleculenId the moleculenId to set
	 */
    public void setMoleculeId(String moleculeId) {
        this.moleculeId = moleculeId;
    }

    /** 
	 * @see java.lang.Object#hashCode()
	 */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((moleculeId == null) ? 0 : moleculeId.hashCode());
        result = prime * result + ((pathwayId == null) ? 0 : pathwayId.hashCode());
        return result;
    }

    /** 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof PathwayMoleculesInvolvedId)) return false;
        final PathwayMoleculesInvolvedId other = (PathwayMoleculesInvolvedId) obj;
        if (moleculeId == null) {
            if (other.moleculeId != null) return false;
        } else if (!moleculeId.equals(other.moleculeId)) return false;
        if (pathwayId == null) {
            if (other.pathwayId != null) return false;
        } else if (!pathwayId.equals(other.pathwayId)) return false;
        return true;
    }
}
