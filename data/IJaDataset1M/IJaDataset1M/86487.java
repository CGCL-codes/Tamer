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
public class PathwayLevelId implements Serializable {

    private static final long serialVersionUID = -9150216173877037051L;

    @ManyToOne
    @JoinColumn(name = "pathway_id")
    private Pathway pathwayId = new Pathway();

    @Column(name = "super_id")
    private String superOrdinatedId = new String();

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
	 * @return the superOrdinatedId
	 */
    public String getSuperOrdinatedId() {
        return superOrdinatedId;
    }

    /**
	 * @param superOrdinatedId the superOrdinatedId to set
	 */
    public void setSuperOrdinatedId(String superOrdinatedId) {
        this.superOrdinatedId = superOrdinatedId;
    }

    /** 
	 * @see java.lang.Object#hashCode()
	 */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((pathwayId == null) ? 0 : pathwayId.hashCode());
        result = prime * result + ((superOrdinatedId == null) ? 0 : superOrdinatedId.hashCode());
        return result;
    }

    /** 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof PathwayLevelId)) return false;
        final PathwayLevelId other = (PathwayLevelId) obj;
        if (pathwayId == null) {
            if (other.pathwayId != null) return false;
        } else if (!pathwayId.equals(other.pathwayId)) return false;
        if (superOrdinatedId == null) {
            if (other.superOrdinatedId != null) return false;
        } else if (!superOrdinatedId.equals(other.superOrdinatedId)) return false;
        return true;
    }
}
