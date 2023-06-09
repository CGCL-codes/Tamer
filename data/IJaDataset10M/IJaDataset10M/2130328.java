package edu.unibi.agbi.biodwh.entity.kegg.ligand.compound;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * KeggCompoundName generated by hbm2java
 */
@Entity(name = "kegg_compound_name")
@Table(name = "kegg_compound_name")
public class KeggCompoundName implements java.io.Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -247877367185106598L;

    private KeggCompoundNameId id;

    private KeggCompound keggCompound;

    public KeggCompoundName() {
    }

    public KeggCompoundName(KeggCompoundNameId id, KeggCompound keggCompound) {
        this.id = id;
        this.keggCompound = keggCompound;
    }

    @EmbeddedId
    @AttributeOverrides({ @AttributeOverride(name = "entry", column = @Column(name = "entry", nullable = false)), @AttributeOverride(name = "name", column = @Column(name = "name", nullable = false)) })
    public KeggCompoundNameId getId() {
        return this.id;
    }

    public void setId(KeggCompoundNameId id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entry", nullable = false, insertable = false, updatable = false)
    public KeggCompound getKeggCompound() {
        return this.keggCompound;
    }

    public void setKeggCompound(KeggCompound keggCompound) {
        this.keggCompound = keggCompound;
    }
}
