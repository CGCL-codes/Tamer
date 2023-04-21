package edu.unibi.agbi.biodwh.entity.mint;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Jan_Fuessmann
 * @version 1.0_2010
 */
@Entity(name = "mint_parti_feature_exp_ref")
@Table(name = "mint_parti_feature_exp_ref")
public class MintPartiFeatureExpRef implements java.io.Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1497192211898677397L;

    private Integer id;

    private MintPartiFeature mintPartiFeature;

    private MintExp mintExp;

    public MintPartiFeatureExpRef() {
    }

    public MintPartiFeatureExpRef(MintPartiFeature mintPartiFeature, MintExp mintExp) {
        this.mintPartiFeature = mintPartiFeature;
        this.mintExp = mintExp;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feature_id", referencedColumnName = "feature_id", nullable = false)
    public MintPartiFeature getMintPartiFeature() {
        return this.mintPartiFeature;
    }

    public void setMintPartiFeature(MintPartiFeature mintPartiFeature) {
        this.mintPartiFeature = mintPartiFeature;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "experiment_xml_internal_id", referencedColumnName = "xml_internal_id", nullable = false)
    public MintExp getMintExp() {
        return this.mintExp;
    }

    public void setMintExp(MintExp mintExp) {
        this.mintExp = mintExp;
    }
}
