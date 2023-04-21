package edu.unibi.agbi.biodwh.entity.transfac.site;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * @author Benjamin Kormeier
 * @version 1.0 28.04.2008
 */
@Embeddable
public class SiteSequencesId implements Serializable {

    private static final long serialVersionUID = -3042965002341029600L;

    @ManyToOne
    @JoinColumn(name = "site_id")
    private Site siteId = new Site();

    @Column(name = "site_sequence")
    private String sequence = new String();

    /**
	 * @return the siteId
	 */
    public Site getSiteId() {
        return siteId;
    }

    /**
	 * @param siteId the siteId to set
	 */
    public void setSiteId(Site siteId) {
        this.siteId = siteId;
    }

    /**
	 * @return the sequence
	 */
    public String getSequence() {
        return sequence;
    }

    /**
	 * @param sequence the sequence to set
	 */
    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    /** 
	 * @see java.lang.Object#hashCode()
	 */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((sequence == null) ? 0 : sequence.hashCode());
        result = prime * result + ((siteId == null) ? 0 : siteId.hashCode());
        return result;
    }

    /** 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof SiteSequencesId)) return false;
        final SiteSequencesId other = (SiteSequencesId) obj;
        if (sequence == null) {
            if (other.sequence != null) return false;
        } else if (!sequence.equals(other.sequence)) return false;
        if (siteId == null) {
            if (other.siteId != null) return false;
        } else if (!siteId.equals(other.siteId)) return false;
        return true;
    }
}
