package edu.unibi.agbi.dawismd.entities.biodwh.go;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Table;

@Entity(name = "go_dbxref")
@Table(appliesTo = "go_dbxref", indexes = { @Index(name = "dblinksIdx", columnNames = { "xref_dbname", "xref_key" }) })
public class Dbxref implements java.io.Serializable {

    private static final long serialVersionUID = 1830396482261492026L;

    private Integer id;

    private String xrefDbname;

    private String xrefKey;

    private String xrefKeytype;

    private String xrefDesc;

    private Set<GeneProduct> geneProducts = new HashSet<GeneProduct>(0);

    private Set<Evidence> evidences = new HashSet<Evidence>(0);

    private Set<TermDefinition> termDefinitions = new HashSet<TermDefinition>(0);

    private Set<TermDbxref> termDbxrefs = new HashSet<TermDbxref>(0);

    private Set<Homolset> homolsets = new HashSet<Homolset>(0);

    private Set<Seq> seqs = new HashSet<Seq>(0);

    private Set<Evidence> evidences_1 = new HashSet<Evidence>(0);

    public Dbxref() {
    }

    public Dbxref(String xrefDbname, String xrefKey) {
        this.xrefDbname = xrefDbname;
        this.xrefKey = xrefKey;
    }

    public Dbxref(String xrefDbname, String xrefKey, String xrefKeytype, String xrefDesc, Set<GeneProduct> geneProducts, Set<Evidence> evidences, Set<TermDefinition> termDefinitions, Set<TermDbxref> termDbxrefs, Set<Homolset> homolsets, Set<Seq> seqs, Set<Evidence> evidences_1) {
        this.xrefDbname = xrefDbname;
        this.xrefKey = xrefKey;
        this.xrefKeytype = xrefKeytype;
        this.xrefDesc = xrefDesc;
        this.geneProducts = geneProducts;
        this.evidences = evidences;
        this.termDefinitions = termDefinitions;
        this.termDbxrefs = termDbxrefs;
        this.homolsets = homolsets;
        this.seqs = seqs;
        this.evidences_1 = evidences_1;
    }

    @Id
    @Column(name = "id", nullable = false)
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "xref_dbname", nullable = false, length = 32)
    public String getXrefDbname() {
        return this.xrefDbname;
    }

    public void setXrefDbname(String xrefDbname) {
        this.xrefDbname = xrefDbname;
    }

    @Column(name = "xref_key", nullable = false, length = 128)
    public String getXrefKey() {
        return this.xrefKey;
    }

    public void setXrefKey(String xrefKey) {
        this.xrefKey = xrefKey;
    }

    @Column(name = "xref_keytype", length = 32)
    public String getXrefKeytype() {
        return this.xrefKeytype;
    }

    public void setXrefKeytype(String xrefKeytype) {
        this.xrefKeytype = xrefKeytype;
    }

    @Column(name = "xref_desc")
    public String getXrefDesc() {
        return this.xrefDesc;
    }

    public void setXrefDesc(String xrefDesc) {
        this.xrefDesc = xrefDesc;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "dbxref")
    public Set<GeneProduct> getGeneProducts() {
        return this.geneProducts;
    }

    public void setGeneProducts(Set<GeneProduct> geneProducts) {
        this.geneProducts = geneProducts;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "dbxref")
    public Set<Evidence> getEvidences() {
        return this.evidences;
    }

    public void setEvidences(Set<Evidence> evidences) {
        this.evidences = evidences;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "dbxref")
    public Set<TermDefinition> getTermDefinitions() {
        return this.termDefinitions;
    }

    public void setTermDefinitions(Set<TermDefinition> termDefinitions) {
        this.termDefinitions = termDefinitions;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "dbxref")
    public Set<TermDbxref> getTermDbxrefs() {
        return this.termDbxrefs;
    }

    public void setTermDbxrefs(Set<TermDbxref> termDbxrefs) {
        this.termDbxrefs = termDbxrefs;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "dbxref")
    public Set<Homolset> getHomolsets() {
        return this.homolsets;
    }

    public void setHomolsets(Set<Homolset> homolsets) {
        this.homolsets = homolsets;
    }

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "go_seq_dbxref", uniqueConstraints = @UniqueConstraint(columnNames = { "seq_id", "dbxref_id" }), joinColumns = { @JoinColumn(name = "dbxref_id", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "seq_id", nullable = false, updatable = false) })
    public Set<Seq> getSeqs() {
        return this.seqs;
    }

    public void setSeqs(Set<Seq> seqs) {
        this.seqs = seqs;
    }

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "go_evidence_dbxref", joinColumns = { @JoinColumn(name = "dbxref_id", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "evidence_id", nullable = false, updatable = false) })
    public Set<Evidence> getEvidences_1() {
        return this.evidences_1;
    }

    public void setEvidences_1(Set<Evidence> evidences_1) {
        this.evidences_1 = evidences_1;
    }
}
