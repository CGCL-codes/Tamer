package org.jcvi.glk;

import java.io.Serializable;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.Type;

/**
 * <code>Assembly</code> is a lightweight representation of a
 * genomic Contig.  This representation only contains metadata of
 * the conig as well as the consensus.  <strong>This Object Has No
 * Knowledge of its Underlying Sequences</strong>.
 *
 *
 * @author jsitz
 * @author dkatzel
 */
@Entity
@Table(name = "assembly")
public class Assembly implements Serializable {

    /** The Serial Version UID */
    private static final long serialVersionUID = -543415796375385019L;

    /**
     * The Contig id.
     */
    private int id;

    /**
     * The BAC id that this contig belongs.
     */
    private Integer bacId;

    /**
     * The number of sequences that this contig has.
     */
    private int numberOfSequences;

    /**
     * The ungapped consensus data for this contig.
     */
    private String ungappedConsensus;

    /**
     * the gapped consensus data for this contig.
     */
    private String gappedConsensus;

    /**
     * Is this a circular contig?
     */
    private boolean circular;

    /**
     * The common name for this contig (ex: flu segment name)
     */
    private String commonName;

    /**
     * the EUID for this contig from a Celera Assembler job.
     */
    private EUID celeraAssemblerContigId;

    /**
     * the Edit info for the last time this contig was modified.
     */
    private Edit lastModified;

    /**
     * the info about when this contig was first uploaded.
     */
    private Edit uploadInfo;

    /**
     * A comment on this contig.
     */
    private String comment;

    private int avgCoverage;

    public Assembly() {
        super();
    }

    /**
     * Retrieves the Id of this contig.
     *
     * @return A long
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "asmbl_id", unique = true, nullable = false, insertable = true, updatable = false)
    public int getId() {
        return id;
    }

    /**
     * Sets the Id of this contig.
     * @param id An int
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retrieves the BAC id of this contig.
     *
     * @return An int
     */
    @Column(name = "bac_id", nullable = true, insertable = true, updatable = true)
    public Integer getBacId() {
        return bacId;
    }

    /**
     * Sets the BAC id of this contig.
     * @param bacId An {@link Integer}.
     */
    public void setBacId(Integer bacId) {
        this.bacId = bacId;
    }

    /**
     * Retrieves the number of sequences that belong to
     * this contig.
     *
     * @return An int
     */
    @Column(name = "seq#", nullable = false, updatable = true, insertable = true)
    public int getNumberOfSequences() {
        return numberOfSequences;
    }

    /**
     * Sets the number of sequences that belong to
     * this contig.
     * @param numberOfSequences A int
     */
    public void setNumberOfSequences(int numberOfSequences) {
        this.numberOfSequences = numberOfSequences;
    }

    /**
     * Retrieves the ungapped consensus of this contig.
     *
     * @return A {@link String}
     */
    @Column(name = "sequence", nullable = false, updatable = true, insertable = true)
    public String getUngappedConsensus() {
        return ungappedConsensus;
    }

    /**
     * Sets the ungapped consensus of this contig.
     * @param ungappedConsensus A String
     */
    public void setUngappedConsensus(String ungappedConsensus) {
        this.ungappedConsensus = ungappedConsensus;
    }

    /**
     * Retrieves the gapped consensus of this contig.
     *
     * @return A {@link String}
     */
    @Column(name = "lsequence", nullable = false, updatable = true, insertable = true)
    public String getGappedConsensus() {
        return gappedConsensus;
    }

    /**
     * Sets the gapped consensus of this contig.
     * @param gappedConsensus A String
     */
    public void setGappedConsensus(String gappedConsensus) {
        this.gappedConsensus = gappedConsensus;
    }

    /**
     * is this Contig circular?
     *
     * @return <code>true</code> if this contig is circular;
     * <code>false</code> otherwise.
     */
    @Column(name = "is_circular", nullable = false, updatable = true, insertable = true)
    public boolean isCircular() {
        return circular;
    }

    /**
     * Sets if this contig is circular.
     * @param circular A boolean
     */
    public void setCircular(boolean circular) {
        this.circular = circular;
    }

    @Override
    public String toString() {
        return String.format("%d-%s", this.getId(), this.getCommonName());
    }

    /**
     * Retrieves the common name for this contig.
     *
     * @return A {@link String}
     */
    @Column(name = "com_name", insertable = true, updatable = true, nullable = false)
    public String getCommonName() {
        return commonName;
    }

    /**
     * Sets the common name for this contig.
     * @param commonName A String
     */
    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    /**
     * Retrieves the EUID that references this contig from a
     * Celera Assembler job.
     *
     * @return A {@link EUID}
     */
    @Type(type = "org.jcvi.glk.hibernate.EUIDUserType")
    @Column(name = "ca_contig_id")
    public EUID getCeleraAssemblerContigId() {
        return celeraAssemblerContigId;
    }

    /**
     * Sets the EUID that references this contig from a
     * Celera Assembler job.
     * @param celeraAssemblerContigId A EUID
     */
    public void setCeleraAssemblerContigId(EUID celeraAssemblerContigId) {
        this.celeraAssemblerContigId = celeraAssemblerContigId;
    }

    /**
     * Retrieves the {@link Edit} containing the
     * info for the last time this contig was modified.
     *
     * @return A {@link Edit}
     */
    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "date", column = @Column(name = "mod_date")), @AttributeOverride(name = "userName", column = @Column(name = "mod_pn")) })
    public Edit getLastModified() {
        return lastModified;
    }

    /**
     * Sets the {@link Edit} containing the
     * info for the last time this contig was modified.
     * @param lastModified A Edit
     */
    public void setLastModified(Edit lastModified) {
        this.lastModified = lastModified;
    }

    /**
     * Retrieves the Edit containing the info
     * for when this contig was uploaded.
     *
     * @return A {@link Edit}
     */
    @Embedded
    public Edit getUploadInfo() {
        return uploadInfo;
    }

    /**
     * Sets the Edit containing the info
     * for when this contig was uploaded.
     * @param uploadInfo A Edit
     */
    public void setUploadInfo(Edit uploadInfo) {
        this.uploadInfo = uploadInfo;
    }

    /**
     * Retrieves the comment for this Contig.
     *
     * @return A {@link String}
     */
    @Column(name = "comment")
    public String getComment() {
        return comment;
    }

    /**
     * Sets the comment for this Contig.
     * @param comment A String
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    @Column(name = "redundancy", nullable = false, insertable = false, updatable = false)
    public int getAvgCoverage() {
        return avgCoverage;
    }

    public void setAvgCoverage(int avgCoverage) {
        this.avgCoverage = avgCoverage;
    }
}
