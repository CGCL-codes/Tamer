package edu.unibi.agbi.biodwh.entity.transpath.reaction;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author Benjamin Kormeier
 * @version 1.0 20.05.2008
 */
@Embeddable
public class ReactionMoleculeId implements Serializable {

    private static final long serialVersionUID = 2499533014356601717L;

    @ManyToOne
    @JoinColumn(name = "reaction_id")
    private Reaction reactionId = new Reaction();

    @Column(name = "molecule_id")
    private String moleculeId = new String();

    /**
	 * @return the reactionId
	 */
    public Reaction getReactionId() {
        return reactionId;
    }

    /**
	 * @param reactionId the reactionId to set
	 */
    public void setReactionId(Reaction reactionId) {
        this.reactionId = reactionId;
    }

    /**
	 * @return the moleculeId
	 */
    public String getMoleculeId() {
        return moleculeId;
    }

    /**
	 * @param moleculeId the moleculeId to set
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
        result = prime * result + ((reactionId == null) ? 0 : reactionId.hashCode());
        return result;
    }

    /** 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof ReactionMoleculeId)) return false;
        final ReactionMoleculeId other = (ReactionMoleculeId) obj;
        if (moleculeId == null) {
            if (other.moleculeId != null) return false;
        } else if (!moleculeId.equals(other.moleculeId)) return false;
        if (reactionId == null) {
            if (other.reactionId != null) return false;
        } else if (!reactionId.equals(other.reactionId)) return false;
        return true;
    }
}
