package edu.unibi.agbi.dawismd.entities.biodwh.go;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name = "go_gene_product_count")
@Table(name = "go_gene_product_count")
public class GeneProductCount implements java.io.Serializable {

    private static final long serialVersionUID = -561871324980120280L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "term_id", nullable = false)
    private Term term;

    @Column(name = "code", length = 8)
    private String code;

    @Column(name = "speciesdbname", length = 55)
    private String speciesdbname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "species_id", insertable = false, updatable = false)
    private Species species;

    @Column(name = "product_count", nullable = false)
    private int productCount;

    public GeneProductCount() {
    }

    /**
	 * @return the id
	 */
    public Integer getId() {
        return id;
    }

    /**
	 * @param id the id to set
	 */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
	 * @return the code
	 */
    public String getCode() {
        return code;
    }

    /**
	 * @param code the code to set
	 */
    public void setCode(String code) {
        this.code = code;
    }

    /**
	 * @return the speciesdbname
	 */
    public String getSpeciesdbname() {
        return speciesdbname;
    }

    /**
	 * @param speciesdbname the speciesdbname to set
	 */
    public void setSpeciesdbname(String speciesdbname) {
        this.speciesdbname = speciesdbname;
    }

    /**
	 * @return the productCount
	 */
    public int getProductCount() {
        return productCount;
    }

    /**
	 * @param productCount the productCount to set
	 */
    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    public Term getTerm() {
        return this.term;
    }

    public void setTerm(Term term) {
        this.term = term;
    }

    public Species getSpecies() {
        return this.species;
    }

    public void setSpecies(Species species) {
        this.species = species;
    }
}
