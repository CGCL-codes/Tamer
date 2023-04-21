package edu.unibi.agbi.biodwh.entity.enzyme;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name = "enzyme_uniprot")
@Table(name = "enzyme_uniprot")
public class EnzymeUniprot implements java.io.Serializable {

    private static final long serialVersionUID = 783904305819236302L;

    private EnzymeUniprotId id;

    private EnzymeEnzyme enzymeEnzyme;

    public EnzymeUniprot() {
    }

    public EnzymeUniprot(EnzymeUniprotId id, EnzymeEnzyme enzymeEnzyme) {
        this.id = id;
        this.enzymeEnzyme = enzymeEnzyme;
    }

    @EmbeddedId
    @AttributeOverrides({ @AttributeOverride(name = "ecNumber", column = @Column(name = "ec_number", nullable = false)), @AttributeOverride(name = "primaryAccession", column = @Column(name = "primary_accession", nullable = false)), @AttributeOverride(name = "entryName", column = @Column(name = "entry_name", nullable = false)) })
    public EnzymeUniprotId getId() {
        return this.id;
    }

    public void setId(EnzymeUniprotId id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ec_number", nullable = false, insertable = false, updatable = false)
    public EnzymeEnzyme getEnzymeEnzyme() {
        return this.enzymeEnzyme;
    }

    public void setEnzymeEnzyme(EnzymeEnzyme enzymeEnzyme) {
        this.enzymeEnzyme = enzymeEnzyme;
    }
}
