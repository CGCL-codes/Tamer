package edu.unibi.agbi.biodwh.entity.go;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class GeneProductSeqId implements java.io.Serializable {

    private static final long serialVersionUID = 2574489997442813236L;

    private int geneProductId;

    private int seqId;

    private Integer isPrimarySeq;

    public GeneProductSeqId() {
    }

    public GeneProductSeqId(int geneProductId, int seqId) {
        this.geneProductId = geneProductId;
        this.seqId = seqId;
    }

    public GeneProductSeqId(int geneProductId, int seqId, Integer isPrimarySeq) {
        this.geneProductId = geneProductId;
        this.seqId = seqId;
        this.isPrimarySeq = isPrimarySeq;
    }

    @Column(name = "gene_product_id", nullable = false)
    public int getGeneProductId() {
        return this.geneProductId;
    }

    public void setGeneProductId(int geneProductId) {
        this.geneProductId = geneProductId;
    }

    @Column(name = "seq_id", nullable = false)
    public int getSeqId() {
        return this.seqId;
    }

    public void setSeqId(int seqId) {
        this.seqId = seqId;
    }

    @Column(name = "is_primary_seq")
    public Integer getIsPrimarySeq() {
        return this.isPrimarySeq;
    }

    public void setIsPrimarySeq(Integer isPrimarySeq) {
        this.isPrimarySeq = isPrimarySeq;
    }

    public boolean equals(Object other) {
        if ((this == other)) return true;
        if ((other == null)) return false;
        if (!(other instanceof GeneProductSeqId)) return false;
        GeneProductSeqId castOther = (GeneProductSeqId) other;
        return (this.getGeneProductId() == castOther.getGeneProductId()) && (this.getSeqId() == castOther.getSeqId()) && ((this.getIsPrimarySeq() == castOther.getIsPrimarySeq()) || (this.getIsPrimarySeq() != null && castOther.getIsPrimarySeq() != null && this.getIsPrimarySeq().equals(castOther.getIsPrimarySeq())));
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + this.getGeneProductId();
        result = 37 * result + this.getSeqId();
        result = 37 * result + (getIsPrimarySeq() == null ? 0 : this.getIsPrimarySeq().hashCode());
        return result;
    }
}
