package edu.unibi.agbi.dawismd.entities.biodwh.transfac.gene;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.hibernate.annotations.Index;
import edu.unibi.agbi.dawismd.entities.biodwh.transfac.Organism;
import edu.unibi.agbi.dawismd.entities.biodwh.transfac.Reference;

/**
 * @author Benjamin Kormeier
 * @version 1.0 24.04.2008
 */
@Entity(name = "tf_gene")
@Table(name = "tf_gene")
public class Gene {

    @Id
    @Column(name = "gene_id", length = 7)
    private String geneId = new String();

    @Column(name = "short_gene_term", length = 25)
    private String shortGeneTerm = new String();

    @Column(name = "explicit_name", length = 260)
    @Index(name = "explicitNameIdx")
    private String explicit_name = new String();

    @JoinColumn(name = "organism_Id")
    @OneToOne
    private Organism organismId = new Organism();

    @Column(name = "chromosomal_location", length = 50)
    private String chromosomalLocation = new String();

    @Column(name = "host_gene_id", length = 7)
    private String hostGeneId = new String();

    @Column(name = "intronic_gene_id", length = 7)
    private String intronicGeneId = new String();

    @Column(name = "promotor_classification", length = 15)
    private String promotorClassification = new String();

    @JoinColumn(name = "reference_id")
    @ManyToMany
    @JoinTable(name = "tf_gene2reference", joinColumns = { @JoinColumn(name = "gene_id") }, inverseJoinColumns = { @JoinColumn(name = "reference_id") })
    private Set<Reference> referenceId = new HashSet<Reference>();

    public Gene() {
    }

    /**
	 * @return the geneId
	 */
    public String getGeneId() {
        return geneId;
    }

    /**
	 * @param geneId the geneId to set
	 */
    public void setGeneId(String geneId) {
        this.geneId = geneId;
    }

    /**
	 * @return the shortGeneTerm
	 */
    public String getShortGeneTerm() {
        return shortGeneTerm;
    }

    /**
	 * @param shortGeneTerm the shortGeneTerm to set
	 */
    public void setShortGeneTerm(String shortGeneTerm) {
        this.shortGeneTerm = shortGeneTerm;
    }

    /**
	 * @return the organismId
	 */
    public Organism getOrganismId() {
        return organismId;
    }

    /**
	 * @param organismId the organismId to set
	 */
    public void setOrganismId(Organism organismId) {
        this.organismId = organismId;
    }

    /**
	 * @return the chromosomalLocation
	 */
    public String getChromosomalLocation() {
        return chromosomalLocation;
    }

    /**
	 * @param chromosomalLocation the chromosomalLocation to set
	 */
    public void setChromosomalLocation(String chromosomalLocation) {
        this.chromosomalLocation = chromosomalLocation;
    }

    /**
	 * @return the hostGeneId
	 */
    public String getHostGeneId() {
        return hostGeneId;
    }

    /**
	 * @param hostGeneId the hostGeneId to set
	 */
    public void setHostGeneId(String hostGeneId) {
        this.hostGeneId = hostGeneId;
    }

    /**
	 * @return the intronicGeneId
	 */
    public String getIntronicGeneId() {
        return intronicGeneId;
    }

    /**
	 * @param intronicGeneId the intronicGeneId to set
	 */
    public void setIntronicGeneId(String intronicGeneId) {
        this.intronicGeneId = intronicGeneId;
    }

    /**
	 * @return the promotorClassification
	 */
    public String getPromotorClassification() {
        return promotorClassification;
    }

    /**
	 * @param promotorClassification the promotorClassification to set
	 */
    public void setPromotorClassification(String promotorClassification) {
        this.promotorClassification = promotorClassification;
    }

    /**
	 * @return the explicit_name
	 */
    public String getExplicit_name() {
        return explicit_name;
    }

    /**
	 * @param explicit_name the explicit_name to set
	 */
    public void setExplicit_name(String explicit_name) {
        this.explicit_name = explicit_name;
    }

    /**
	 * @return the referenceId
	 */
    public Set<Reference> getReferenceId() {
        return referenceId;
    }

    /**
	 * @param referenceId the referenceId to set
	 */
    public void setReferenceId(Set<Reference> referenceId) {
        this.referenceId = referenceId;
    }

    /** 
	 * @see java.lang.Object#hashCode()
	 */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((geneId == null) ? 0 : geneId.hashCode());
        return result;
    }

    /** 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof Gene)) return false;
        final Gene other = (Gene) obj;
        if (geneId == null) {
            if (other.geneId != null) return false;
        } else if (!geneId.equals(other.geneId)) return false;
        return true;
    }
}
