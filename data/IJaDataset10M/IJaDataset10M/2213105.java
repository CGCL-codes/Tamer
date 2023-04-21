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

@Entity(name = "uniprot_ord_loc_names")
@Table(name = "uniprot_ord_loc_names")
public class UniprotOrdLocNames implements java.io.Serializable {

    private static final long serialVersionUID = 5907879818019883461L;

    private UniprotOrdLocNamesId id;

    private Uniprot uniprot;

    public UniprotOrdLocNames() {
    }

    public UniprotOrdLocNames(UniprotOrdLocNamesId id, Uniprot uniprot) {
        this.id = id;
        this.uniprot = uniprot;
    }

    @EmbeddedId
    @AttributeOverrides({ @AttributeOverride(name = "ordLocNames", column = @Column(name = "ord_loc_names", nullable = false, length = 64)), @AttributeOverride(name = "uniprotId", column = @Column(name = "uniprot_id", nullable = false, length = 16)) })
    public UniprotOrdLocNamesId getId() {
        return this.id;
    }

    public void setId(UniprotOrdLocNamesId id) {
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
