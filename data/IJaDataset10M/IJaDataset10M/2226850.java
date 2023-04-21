package edu.ucdavis.genomics.metabolomics.binbase.bdi.types.experiment.sample.bin;

import java.util.Collection;
import edu.ucdavis.genomics.metabolomics.binbase.bdi.types.Queryable;
import edu.ucdavis.genomics.metabolomics.util.type.converter.BooleanConverter;

/**
 * @swt
 * @hibernate.class table = "STANDARD" dynamic-insert = "true" dynamic-update =
 *                  "true"
 * @author wohlgemuth
 * 
 */
public class Standard implements Queryable {

    /**
	 * Comment for <code>serialVersionUID</code>
	 */
    private static final long serialVersionUID = 2L;

    /**
	 * DOCUMENT ME!
	 */
    private Integer id;

    /**
	 * DOCUMENT ME!
	 */
    private Double maximalDistanceRatio;

    /**
	 * DOCUMENT ME!
	 */
    private Double maximalQualifierRatio;

    /**
	 * DOCUMENT ME!
	 */
    private Double minimalApexSn;

    /**
	 * DOCUMENT ME!
	 */
    private Double minimalDistanceRatio;

    /**
	 * DOCUMENT ME!
	 */
    private Double minimalQualifierRatio;

    /**
	 * DOCUMENT ME!
	 */
    private Double minimalSimilarity;

    /**
	 * DOCUMENT ME!
	 */
    private Integer qualifier;

    /**
	 * DOCUMENT ME!
	 */
    private Double qualifierMaxRatio;

    /**
	 * DOCUMENT ME!
	 */
    private String requiredString;

    /**
	 * history of this standard
	 */
    private Collection history;

    private Bin bin;

    /**
	 * @hibernate.id column = "`bin_id`" generator-class = "assigned"
	 * @see edu.ucdavis.genomics.metabolomics.binbase.bdi.types.experiment.sample.spectra.Spectra#getId()
	 * 
	 * @uml.property name="id"
	 */
    public Integer getId() {
        return id;
    }

    /**
	 * @swt.variable visible="true" name="Maximal Distance Ratio"
	 *               searchable="true"
	 * @swt.modify canModify="true"
	 * @hibernate.property column = "`max_distance_ratio`" update = "true"
	 *                     insert = "true" not-null = "true"
	 * @see edu.ucdavis.genomics.metabolomics.binbase.bdi.types.experiment.sample.bin.Standard#getMaximalDistanceRatio()
	 */
    public Double getMaximalDistanceRatio() {
        return maximalDistanceRatio;
    }

    /**
	 * @swt.variable visible="true" name="Maximal Qualifier Ratio"
	 *               searchable="true"
	 * @swt.modify canModify="true"
	 * @hibernate.property column = "`max_ratio`" update = "true" insert =
	 *                     "true" not-null = "true"
	 * @see edu.ucdavis.genomics.metabolomics.binbase.bdi.types.experiment.sample.bin.Standard#getMinimalQualifierRatio()
	 */
    public Double getMaximalQualifierRatio() {
        return maximalQualifierRatio;
    }

    /**
	 * @swt.variable visible="true" name="Minimal Apex Sn" searchable="true"
	 * @swt.modify canModify="true"
	 * @hibernate.property column = "`min_apex_sn`" update = "true" insert =
	 *                     "true" not-null = "true"
	 * @see edu.ucdavis.genomics.metabolomics.binbase.bdi.types.experiment.sample.bin.Standard#getMinimalApexSn()
	 */
    public Double getMinimalApexSn() {
        return minimalApexSn;
    }

    /**
	 * @swt.variable visible="true" name="Minimal Distance Ratio"
	 *               searchable="true"
	 * @swt.modify canModify="true"
	 * @hibernate.property column = "`min_distance_ratio`" update = "true"
	 *                     insert = "true" not-null = "true"
	 * @see edu.ucdavis.genomics.metabolomics.binbase.bdi.types.experiment.sample.bin.Standard#getMinimalDistanceRatio()
	 */
    public Double getMinimalDistanceRatio() {
        return minimalDistanceRatio;
    }

    /**
	 * @swt.variable visible="true" name="Minimal Qualifier Ratio"
	 *               searchable="true"
	 * @swt.modify canModify="true"
	 * @hibernate.property column = "`min_ratio`" update = "true" insert =
	 *                     "true" not-null = "true"
	 * @see edu.ucdavis.genomics.metabolomics.binbase.bdi.types.experiment.sample.bin.Standard#getMinimalQualifierRatio()
	 */
    public Double getMinimalQualifierRatio() {
        return minimalQualifierRatio;
    }

    /**
	 * @swt.variable visible="true" name="Minimal Similarity" searchable="true"
	 * @swt.modify canModify="true"
	 * @hibernate.property column = "`min_similarity`" update = "true" insert =
	 *                     "true" not-null = "true"
	 * @see edu.ucdavis.genomics.metabolomics.binbase.bdi.types.experiment.sample.bin.Standard#getMinimalSimilarity()
	 */
    public Double getMinimalSimilarity() {
        return minimalSimilarity;
    }

    /**
	 * @swt.variable visible="true" name="Qualifier" searchable="true"
	 * @swt.modify canModify="true"
	 * @hibernate.property column = "`qualifier`" update = "true" insert =
	 *                     "true" not-null = "true"
	 * @see edu.ucdavis.genomics.metabolomics.binbase.bdi.types.experiment.sample.spectra.Spectra#getRetentionIndex()
	 * 
	 * @uml.property name="quantifier"
	 */
    public Integer getQualifier() {
        return qualifier;
    }

    /**
	 * @hibernate.property column = "`required`" update = "true" insert = "true"
	 *                     not-null = "true"
	 * @see edu.ucdavis.genomics.metabolomics.binbase.bdi.types.experiment.sample.bin.Bin#getExport()
	 * 
	 * @uml.property name="exportString"
	 */
    public String getRequiredString() {
        return requiredString;
    }

    /**
	 * @swt.variable visible="true" name="Required" searchable="true"
	 * @swt.modify canModify="true"
	 * @see edu.ucdavis.genomics.metabolomics.binbase.bdi.types.experiment.sample.bin.Standard#isRequired()
	 */
    public Boolean isRequiered() {
        return new Boolean(BooleanConverter.StringtoBoolean(getRequiredString()));
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @param id
	 *            DOCUMENT ME!
	 */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @param maximalDistanceRatio
	 *            DOCUMENT ME!
	 */
    public void setMaximalDistanceRatio(Double maximalDistanceRatio) {
        this.maximalDistanceRatio = maximalDistanceRatio;
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @param maximalQualifierRatio
	 *            DOCUMENT ME!
	 */
    public void setMaximalQualifierRatio(Double maximalQualifierRatio) {
        this.maximalQualifierRatio = maximalQualifierRatio;
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @param minimalApexSn
	 *            DOCUMENT ME!
	 */
    public void setMinimalApexSn(Double minimalApexSn) {
        this.minimalApexSn = minimalApexSn;
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @param minimalDistanceRatio
	 *            DOCUMENT ME!
	 */
    public void setMinimalDistanceRatio(Double minimalDistanceRatio) {
        this.minimalDistanceRatio = minimalDistanceRatio;
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @param minimalQualifierRatio
	 *            DOCUMENT ME!
	 */
    public void setMinimalQualifierRatio(Double minimalQualifierRatio) {
        this.minimalQualifierRatio = minimalQualifierRatio;
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @param minimalSimilarity
	 *            DOCUMENT ME!
	 */
    public void setMinimalSimilarity(Double minimalSimilarity) {
        this.minimalSimilarity = minimalSimilarity;
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @param qualifier
	 *            DOCUMENT ME!
	 */
    public void setQualifier(Integer qualifier) {
        this.qualifier = qualifier;
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @param qualifierMaxRatio
	 *            DOCUMENT ME!
	 */
    public void setQualifierMaxRatio(Double qualifierMaxRatio) {
        this.qualifierMaxRatio = qualifierMaxRatio;
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @param b
	 *            DOCUMENT ME!
	 */
    public void setRequiered(Boolean b) {
        setRequiredString(BooleanConverter.booleanToString(b.booleanValue()));
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @param requiredString
	 *            DOCUMENT ME!
	 */
    public void setRequiredString(String requiredString) {
        this.requiredString = requiredString;
    }

    /**
	 * @hibernate.one-to-one cascade = "none"
	 * @author wohlgemuth
	 * @version May 24, 2006
	 * @return
	 */
    public Bin getBin() {
        return bin;
    }

    public void setBin(Bin bin) {
        this.bin = bin;
    }

    /**
	 * @hibernate.set lazy="true" cascade = "save-update" inverse = "true"
	 * @hibernate.collection-one-to-many class =
	 *                                   "edu.ucdavis.genomics.metabolomics.binbase.bdi.types.experiment.sample.bin.StandardHistory"
	 * @hibernate.collection-key column = "`bin_id`"
	 * @return
	 */
    public Collection getHistory() {
        return history;
    }

    public void setHistory(Collection history) {
        this.history = history;
    }
}
