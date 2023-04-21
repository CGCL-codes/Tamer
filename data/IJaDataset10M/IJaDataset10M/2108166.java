package edu.unibi.agbi.dawismd.entities.biodwh.transfac.factor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * @author Benjamin Kormeier
 * @version 1.0 18.04.2008
 */
@Entity(name = "tf_factor_synonyms")
@Table(name = "tf_factor_synonyms")
@IdClass(FactorSynonymId.class)
public class FactorSynonyms {

    @Id
    private Factor factor = new Factor();

    @Id
    private String factorSynonym = new String();

    public FactorSynonyms() {
    }

    /**
	 * @param factor
	 * @param factorSynonym
	 */
    public FactorSynonyms(Factor factor, String factorSynonym) {
        this.factor = factor;
        this.factorSynonym = factorSynonym;
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
	 * @return the factorSynonym
	 */
    public String getFactorSynonym() {
        return factorSynonym;
    }

    /**
	 * @param factorSynonym the factorSynonym to set
	 */
    public void setFactorSynonym(String factorSynonym) {
        this.factorSynonym = factorSynonym;
    }
}
