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
@Entity(name = "mint_exp_alias")
@Table(name = "mint_exp_alias")
public class MintExpAlias implements java.io.Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -3180148142354137980L;

    private Integer aliasId;

    private MintExp mintExp;

    private String alias;

    private String type;

    private String typeAc;

    public MintExpAlias() {
    }

    public MintExpAlias(Integer aliasId) {
        this.aliasId = aliasId;
    }

    public MintExpAlias(MintExp mintExp, String alias, String type, String typeAc) {
        this.mintExp = mintExp;
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
    @JoinColumn(name = "experiment_xml_internal_id", referencedColumnName = "xml_internal_id", nullable = false)
    public MintExp getMintExp() {
        return this.mintExp;
    }

    public void setMintExp(MintExp mintExp) {
        this.mintExp = mintExp;
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
