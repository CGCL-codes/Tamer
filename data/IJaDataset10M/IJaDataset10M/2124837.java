package edu.unibi.agbi.dawismd.entities.biodwh.transpath.gene;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

/**
 * @author Benjamin Kormeier
 * @version 1.0 24.04.2008
 */
@Entity(name = "tp_gene_synonyms")
@Table(name = "tp_gene_synonyms")
@IdClass(GeneSynonymId.class)
public class GeneSynonyms {

    @Id
    private Gene geneId = new Gene();

    @Id
    private String synonym = new String();

    public GeneSynonyms() {
    }

    /**
	 * @param geneId
	 * @param synonym
	 */
    public GeneSynonyms(Gene geneId, String synonym) {
        super();
        this.geneId = geneId;
        this.synonym = synonym;
    }

    /**
	 * @return the geneId
	 */
    public Gene getGeneId() {
        return geneId;
    }

    /**
	 * @param geneId the geneId to set
	 */
    public void setGeneId(Gene geneId) {
        this.geneId = geneId;
    }

    /**
	 * @return the synonym
	 */
    public String getSynonym() {
        return synonym;
    }

    /**
	 * @param synonym the synonym to set
	 */
    public void setSynonym(String synonym) {
        this.synonym = synonym;
    }
}
