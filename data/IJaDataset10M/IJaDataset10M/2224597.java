package edu.unibi.agbi.biodwh.entity.transfac.factor;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import edu.unibi.agbi.biodwh.entity.transfac.ExternalDatabaseLinks;

/**
 * @author Benjamin Kormeier
 * @version 1.0 24.04.2008
 */
@Embeddable
public class FactorExternalDatabaseLinksId implements Serializable {

    private static final long serialVersionUID = -5804277805139375850L;

    @ManyToOne
    @JoinColumn(name = "factor_id")
    private Factor factor = new Factor();

    @ManyToOne
    @JoinColumn(name = "id")
    private ExternalDatabaseLinks link = new ExternalDatabaseLinks();

    public FactorExternalDatabaseLinksId() {
    }

    /**
	 * @return the factor
	 */
    public Factor getFactor() {
        return factor;
    }

    /**
	 * @param factor the factor to set
	 */
    public void setFactor(Factor factor) {
        this.factor = factor;
    }

    /**
	 * @return the link
	 */
    public ExternalDatabaseLinks getLink() {
        return link;
    }

    /**
	 * @param link the link to set
	 */
    public void setLink(ExternalDatabaseLinks link) {
        this.link = link;
    }

    /** 
	 * @see java.lang.Object#hashCode()
	 */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((factor == null) ? 0 : factor.hashCode());
        result = prime * result + ((link == null) ? 0 : link.hashCode());
        return result;
    }

    /** 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof FactorExternalDatabaseLinksId)) return false;
        final FactorExternalDatabaseLinksId other = (FactorExternalDatabaseLinksId) obj;
        if (factor == null) {
            if (other.factor != null) return false;
        } else if (!factor.equals(other.factor)) return false;
        if (link == null) {
            if (other.link != null) return false;
        } else if (!link.equals(other.link)) return false;
        return true;
    }
}
