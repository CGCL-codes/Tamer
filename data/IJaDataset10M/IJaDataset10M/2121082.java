package edu.unibi.agbi.biodwh.entity.epd;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name = "epd_alternative_promoter")
@Table(name = "epd_alternative_promoter")
public class EpdAlternativePromoter implements java.io.Serializable {

    private static final long serialVersionUID = -661938458275479328L;

    private EpdAlternativePromoterId id;

    private EpdIdentification epdIdentification;

    public EpdAlternativePromoter() {
    }

    public EpdAlternativePromoter(EpdAlternativePromoterId id, EpdIdentification epdIdentification) {
        this.id = id;
        this.epdIdentification = epdIdentification;
    }

    @EmbeddedId
    @AttributeOverrides({ @AttributeOverride(name = "entryName", column = @Column(name = "entry_name", nullable = false)), @AttributeOverride(name = "alternativePromoter", column = @Column(name = "alternative_promoter", nullable = false)) })
    public EpdAlternativePromoterId getId() {
        return this.id;
    }

    public void setId(EpdAlternativePromoterId id) {
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
