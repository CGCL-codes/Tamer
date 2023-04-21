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
@Entity(name = "intact_source_alias")
@Table(name = "intact_source_alias")
public class IntactSourceAlias implements java.io.Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -3375232561902234954L;

    private Integer aliasId;

    private IntactSource intactSource;

    private String alias;

    private String type;

    private String typeAc;

    public IntactSourceAlias() {
    }

    public IntactSourceAlias(IntactSource intactSource) {
        this.intactSource = intactSource;
    }

    public IntactSourceAlias(IntactSource intactSource, String alias, String type, String typeAc) {
        this.intactSource = intactSource;
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
    @JoinColumn(name = "entry_id", nullable = false)
    public IntactSource getIntactSource() {
        return this.intactSource;
    }

    public void setIntactSource(IntactSource intactSource) {
        this.intactSource = intactSource;
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
