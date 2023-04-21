package org.saosis.core.models.features;

import java.util.ArrayList;
import org.saosis.core.models.DatabaseCrossReference;
import org.saosis.core.models.Feature;
import org.saosis.core.models.FeatureInference;

/**
 * A model for the "misc_feature" feature. See
 * http://www.ebi.ac.uk/embl/WebFeat/index.html for details.
 * 
 * @author Daniel Allen Prust (danprust@yahoo.com)
 * 
 */
public class MiscellaneousFeature extends Feature {

    private ArrayList<String> allele = new ArrayList<String>();

    private int citation;

    private ArrayList<DatabaseCrossReference> databaseCrossReferences = new ArrayList<DatabaseCrossReference>();

    private String experiment = "";

    private String function = "";

    private String gene = "";

    private ArrayList<String> geneSynonyms = new ArrayList<String>();

    private FeatureInference inference = new FeatureInference();

    private String label = "";

    private String locusTag = "";

    private String map = "";

    private String notes = "";

    private String number = "";

    private ArrayList<String> oldLocusTags = new ArrayList<String>();

    private String phenotype = "";

    private ArrayList<String> products = new ArrayList<String>();

    private boolean pseudo;

    private String standardName = "";

    public MiscellaneousFeature() {
        super();
    }

    /**
	 * allele
	 */
    public ArrayList<String> getAllele() {
        return this.allele;
    }

    /**
	 * citation
	 */
    public int getCitation() {
        return this.citation;
    }

    /**
	 * db_xref
	 */
    public ArrayList<DatabaseCrossReference> getDatabaseCrossReferences() {
        return this.databaseCrossReferences;
    }

    /**
	 * experiment
	 */
    public String getExperiment() {
        return this.experiment;
    }

    /**
	 * function
	 */
    public String getFunction() {
        return this.function;
    }

    /**
	 * gene
	 */
    public String getGene() {
        return this.gene;
    }

    /**
	 * gene_synonym
	 */
    public ArrayList<String> getGeneSynonyms() {
        return this.geneSynonyms;
    }

    /**
	 * inference
	 */
    public FeatureInference getInference() {
        return this.inference;
    }

    /**
	 * label
	 */
    public String getLabel() {
        return this.label;
    }

    /**
	 * locus_tag
	 */
    public String getLocusTag() {
        return this.locusTag;
    }

    /**
	 * map
	 */
    public String getMap() {
        return this.map;
    }

    /**
	 * note
	 */
    public String getNotes() {
        return this.notes;
    }

    /**
	 * number
	 */
    public String getNumber() {
        return this.number;
    }

    /**
	 * old_locus_tag
	 */
    public ArrayList<String> getOldLocusTags() {
        return this.oldLocusTags;
    }

    /**
	 * phenotype
	 */
    public String getPhenotype() {
        return this.phenotype;
    }

    /**
	 * product
	 */
    public ArrayList<String> getProducts() {
        return this.products;
    }

    /**
	 * pseudo
	 */
    public boolean getPseudo() {
        return this.pseudo;
    }

    /**
	 * standard_name
	 */
    public String getStandardName() {
        return this.standardName;
    }

    /**
	 * citation
	 */
    public void setCitation(int citation) {
        this.citation = citation;
    }

    /**
	 * experiment
	 */
    public void setExperiment(String experiment) {
        this.experiment = experiment == null ? "" : experiment.trim();
    }

    /**
	 * function
	 */
    public void setFunction(String function) {
        this.function = function == null ? "" : function.trim();
    }

    /**
	 * gene
	 */
    public void setGene(String gene) {
        this.gene = gene == null ? "" : gene.trim();
    }

    /**
	 * inference
	 */
    public void setInference(FeatureInference inference) {
        this.inference = inference == null ? new FeatureInference() : inference;
    }

    /**
	 * label
	 */
    public void setLabel(String label) {
        this.label = label == null ? "" : label.trim();
    }

    /**
	 * locus_tag
	 */
    public void setLocusTag(String locusTag) {
        this.locusTag = locusTag == null ? "" : locusTag.trim();
    }

    /**
	 * map
	 */
    public void setMap(String map) {
        this.map = map == null ? "" : map.trim();
    }

    /**
	 * note
	 */
    public void setNotes(String notes) {
        this.notes = notes == null ? "" : notes.trim();
    }

    /**
	 * number
	 */
    public void setNumber(String number) {
        this.number = number == null ? "" : number.trim();
    }

    /**
	 * phenotype
	 */
    public void setPhenotype(String phenotype) {
        this.phenotype = phenotype == null ? "" : phenotype.trim();
    }

    /**
	 * pseudo
	 */
    public void setPseudo(boolean pseudo) {
        this.pseudo = pseudo;
    }

    /**
	 * standard_name
	 */
    public void setStandardName(String standardName) {
        this.standardName = standardName == null ? "" : standardName.trim();
    }
}
