package edu.unibi.agbi.biodwh.entity.intact;

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
@Entity(name = "intact_parti_feature_alias")
@Table(name = "intact_parti_feature_alias")
public class IntactPartiFeatureAlias implements java.io.Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -8679847170802978517L;

    private Integer aliasId;

    private IntactPartiFeature intactPartiFeature;

    private String alias;

    private String type;

    private String typeAc;

    public IntactPartiFeatureAlias() {
    }

    public IntactPartiFeatureAlias(IntactPartiFeature intactPartiFeature) {
        this.intactPartiFeature = intactPartiFeature;
    }

    public IntactPartiFeatureAlias(IntactPartiFeature intactPartiFeature, String alias, String type, String typeAc) {
        this.intactPartiFeature = intactPartiFeature;
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
    @JoinColumn(name = "feature_id", referencedColumnName = "feature_id", nullable = false)
    public IntactPartiFeature getIntactPartiFeature() {
        return this.intactPartiFeature;
    }

    public void setIntactPartiFeature(IntactPartiFeature intactPartiFeature) {
        this.intactPartiFeature = intactPartiFeature;
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
