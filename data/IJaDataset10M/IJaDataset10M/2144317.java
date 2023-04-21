package edu.unibi.agbi.dawismd.entities.biodwh.go;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author Benjamin Kormeier
 * @version 1.0 21.04.2009
 */
@Embeddable
public class EvidenceDbxrefId implements Serializable {

    private static final long serialVersionUID = -46668194727358133L;

    @JoinColumn(name = "evidence_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Evidence evidenceId = new Evidence();

    @JoinColumn(name = "dbxref_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Dbxref dbxrefId = new Dbxref();

    public EvidenceDbxrefId() {
    }

    /**
	 * @return the evidenceId
	 */
    public Evidence getEvidenceId() {
        return evidenceId;
    }

    /**
	 * @param evidenceId the evidenceId to set
	 */
    public void setEvidenceId(Evidence evidenceId) {
        this.evidenceId = evidenceId;
    }

    /**
	 * @return the dbxrefId
	 */
    public Dbxref getDbxrefId() {
        return dbxrefId;
    }

    /**
	 * @param dbxrefId the dbxrefId to set
	 */
    public void setDbxrefId(Dbxref dbxrefId) {
        this.dbxrefId = dbxrefId;
    }

    /** 
	 * @see java.lang.Object#hashCode()
	 */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((dbxrefId == null) ? 0 : dbxrefId.hashCode());
        result = prime * result + ((evidenceId == null) ? 0 : evidenceId.hashCode());
        return result;
    }

    /** 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        EvidenceDbxrefId other = (EvidenceDbxrefId) obj;
        if (dbxrefId == null) {
            if (other.dbxrefId != null) return false;
        } else if (!dbxrefId.equals(other.dbxrefId)) return false;
        if (evidenceId == null) {
            if (other.evidenceId != null) return false;
        } else if (!evidenceId.equals(other.evidenceId)) return false;
        return true;
    }
}
