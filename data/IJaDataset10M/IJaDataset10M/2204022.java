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
@Entity(name = "mint_parti_exp_prep_alias")
@Table(name = "mint_parti_exp_prep_alias")
public class MintPartiExpPrepAlias implements java.io.Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -7802053078127962148L;

    private Integer aliasId;

    private MintPartiExpPrep mintPartiExpPrep;

    private String alias;

    private String type;

    private String typeAc;

    public MintPartiExpPrepAlias() {
    }

    public MintPartiExpPrepAlias(MintPartiExpPrep mintPartiExpPrep) {
        this.mintPartiExpPrep = mintPartiExpPrep;
    }

    public MintPartiExpPrepAlias(MintPartiExpPrep mintPartiExpPrep, String alias, String type, String typeAc) {
        this.mintPartiExpPrep = mintPartiExpPrep;
        this.alias = alias;
        this.type = type;
        this.typeAc = typeAc;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "alias_id", nullable = false)
    public Integer getAliasId() {
        return this.aliasId;
    }

    public void setAliasId(Integer aliasId) {
        this.aliasId = aliasId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exp_preparation_id", nullable = false)
    public MintPartiExpPrep getMintPartiExpPrep() {
        return this.mintPartiExpPrep;
    }

    public void setMintPartiExpPrep(MintPartiExpPrep mintPartiExpPrep) {
        this.mintPartiExpPrep = mintPartiExpPrep;
    }

    @Column(name = "alias")
    public String getAlias() {
        return this.alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Column(name = "type")
    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "type_ac")
    public String getTypeAc() {
        return this.typeAc;
    }

    public void setTypeAc(String typeAc) {
        this.typeAc = typeAc;
    }
}
