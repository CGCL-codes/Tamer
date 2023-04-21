package edu.unibi.agbi.dawismd.entities.biodwh.epd;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name = "epd_taxonomy")
@Table(name = "epd_taxonomy")
public class EpdTaxonomy implements java.io.Serializable {

    private static final long serialVersionUID = -6604324999219690189L;

    private EpdTaxonomyId id;

    private EpdIdentification epdIdentification;

    public EpdTaxonomy() {
    }

    public EpdTaxonomy(EpdTaxonomyId id, EpdIdentification epdIdentification) {
        this.id = id;
        this.epdIdentification = epdIdentification;
    }

    @EmbeddedId
    @AttributeOverrides({ @AttributeOverride(name = "entryName", column = @Column(name = "entry_name", nullable = false)), @AttributeOverride(name = "classification", column = @Column(name = "classification", nullable = false)) })
    public EpdTaxonomyId getId() {
        return this.id;
    }

    public void setId(EpdTaxonomyId id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entry_name", nullable = false, insertable = false, updatable = false)
    public EpdIdentification getEpdIdentification() {
        return this.epdIdentification;
    }

    public void setEpdIdentification(EpdIdentification epdIdentification) {
        this.epdIdentification = epdIdentification;
    }
}
