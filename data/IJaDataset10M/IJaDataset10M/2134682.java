package edu.unibi.agbi.dawismd.entities.biodwh.go;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity(name = "go_species")
@Table(name = "go_species")
public class Species implements java.io.Serializable {

    private static final long serialVersionUID = 1297853261430862094L;

    private Integer id;

    private Integer ncbiTaxaId;

    private String commonName;

    private String lineageString;

    private String genus;

    private String species;

    private Integer parentId;

    private Integer leftValue;

    private Integer rightValue;

    private String taxonomicRank;

    private Set<GeneProductCount> geneProductCounts = new HashSet<GeneProductCount>(0);

    private Set<GeneProduct> geneProducts = new HashSet<GeneProduct>(0);

    private Set<Homolset> homolsets = new HashSet<Homolset>(0);

    private Set<AssociationSpeciesQualifier> associationSpeciesQualifiers = new HashSet<AssociationSpeciesQualifier>(0);

    public Species() {
    }

    public Species(Integer ncbiTaxaId, String commonName, String lineageString, String genus, String species, Integer parentId, Integer leftValue, Integer rightValue, String taxonomicRank, Set<GeneProductCount> geneProductCounts, Set<GeneProduct> geneProducts, Set<Homolset> homolsets, Set<AssociationSpeciesQualifier> associationSpeciesQualifiers) {
        this.ncbiTaxaId = ncbiTaxaId;
        this.commonName = commonName;
        this.lineageString = lineageString;
        this.genus = genus;
        this.species = species;
        this.parentId = parentId;
        this.leftValue = leftValue;
        this.rightValue = rightValue;
        this.taxonomicRank = taxonomicRank;
        this.geneProductCounts = geneProductCounts;
        this.geneProducts = geneProducts;
        this.homolsets = homolsets;
        this.associationSpeciesQualifiers = associationSpeciesQualifiers;
    }

    @Id
    @Column(name = "id", nullable = false)
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "ncbi_taxa_id")
    public Integer getNcbiTaxaId() {
        return this.ncbiTaxaId;
    }

    public void setNcbiTaxaId(Integer ncbiTaxaId) {
        this.ncbiTaxaId = ncbiTaxaId;
    }

    @Column(name = "common_name", length = 64)
    public String getCommonName() {
        return this.commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    @Column(name = "lineage_string")
    public String getLineageString() {
        return this.lineageString;
    }

    public void setLineageString(String lineageString) {
        this.lineageString = lineageString;
    }

    @Column(name = "genus", length = 64)
    public String getGenus() {
        return this.genus;
    }

    public void setGenus(String genus) {
        this.genus = genus;
    }

    @Column(name = "species", length = 128)
    public String getSpecies() {
        return this.species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    @Column(name = "parent_id")
    public Integer getParentId() {
        return this.parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    @Column(name = "left_value")
    public Integer getLeftValue() {
        return this.leftValue;
    }

    public void setLeftValue(Integer leftValue) {
        this.leftValue = leftValue;
    }

    @Column(name = "right_value")
    public Integer getRightValue() {
        return this.rightValue;
    }

    public void setRightValue(Integer rightValue) {
        this.rightValue = rightValue;
    }

    @Column(name = "taxonomic_rank")
    public String getTaxonomicRank() {
        return this.taxonomicRank;
    }

    public void setTaxonomicRank(String taxonomicRank) {
        this.taxonomicRank = taxonomicRank;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "species")
    public Set<GeneProductCount> getGeneProductCounts() {
        return this.geneProductCounts;
    }

    public void setGeneProductCounts(Set<GeneProductCount> geneProductCounts) {
        this.geneProductCounts = geneProductCounts;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "species")
    public Set<GeneProduct> getGeneProducts() {
        return this.geneProducts;
    }

    public void setGeneProducts(Set<GeneProduct> geneProducts) {
        this.geneProducts = geneProducts;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "species")
    public Set<Homolset> getHomolsets() {
        return this.homolsets;
    }

    public void setHomolsets(Set<Homolset> homolsets) {
        this.homolsets = homolsets;
    }

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "species")
    public Set<AssociationSpeciesQualifier> getAssociationSpeciesQualifiers() {
        return this.associationSpeciesQualifiers;
    }

    public void setAssociationSpeciesQualifiers(Set<AssociationSpeciesQualifier> associationSpeciesQualifiers) {
        this.associationSpeciesQualifiers = associationSpeciesQualifiers;
    }
}
