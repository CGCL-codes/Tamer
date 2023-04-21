package edu.unibi.agbi.biodwh.entity.uniprot;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name = "uniprot_orf_names")
@Table(name = "uniprot_orf_names")
public class UniprotOrfNames implements java.io.Serializable {

    private static final long serialVersionUID = 2737068923913838405L;

    private UniprotOrfNamesId id;

    private Uniprot uniprot;

    public UniprotOrfNames() {
    }

    public UniprotOrfNames(UniprotOrfNamesId id, Uniprot uniprot) {
        this.id = id;
        this.uniprot = uniprot;
    }

    @EmbeddedId
    @AttributeOverrides({ @AttributeOverride(name = "orfNames", column = @Column(name = "orf_names", nullable = false, length = 64)), @AttributeOverride(name = "uniprotId", column = @Column(name = "uniprot_id", nullable = false, length = 16)) })
    public UniprotOrfNamesId getId() {
        return this.id;
    }

    public void setId(UniprotOrfNamesId id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uniprot_id", nullable = false, insertable = false, updatable = false)
    public Uniprot getUniprot() {
        return this.uniprot;
    }

    public void setUniprot(Uniprot uniprot) {
        this.uniprot = uniprot;
    }
}
