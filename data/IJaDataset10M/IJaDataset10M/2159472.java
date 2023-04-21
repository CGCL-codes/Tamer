package edu.unibi.agbi.biodwh.entity.go;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name = "go_gene_product_prop")
@Table(name = "go_gene_product_prop")
public class GeneProductProperty implements java.io.Serializable {

    private static final long serialVersionUID = -8835537117792146203L;

    private GeneProductPropertyId id;

    private GeneProduct geneProduct;

    public GeneProductProperty() {
    }

    public GeneProductProperty(GeneProductPropertyId id, GeneProduct geneProduct) {
        this.id = id;
        this.geneProduct = geneProduct;
    }

    @EmbeddedId
    @AttributeOverrides({ @AttributeOverride(name = "geneProductId", column = @Column(name = "gene_product_id", nullable = false)), @AttributeOverride(name = "propertyKey", column = @Column(name = "property_key", nullable = false, length = 64)), @AttributeOverride(name = "propertyVal", column = @Column(name = "property_val")) })
    public GeneProductPropertyId getId() {
        return this.id;
    }

    public void setId(GeneProductPropertyId id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gene_product_id", nullable = false, insertable = false, updatable = false)
    public GeneProduct getGeneProduct() {
        return this.geneProduct;
    }

    public void setGeneProduct(GeneProduct geneProduct) {
        this.geneProduct = geneProduct;
    }
}
