package edu.ucdavis.genomics.metabolomics.binbase.bdi.types.experiment.sample.bin;

import java.sql.Timestamp;
import java.util.Date;
import edu.ucdavis.genomics.metabolomics.binbase.bdi.types.Queryable;
import edu.ucdavis.genomics.metabolomics.util.type.converter.BooleanConverter;

/**
 * @swt
 * @hibernate.class table = "STANDARD_HIST" dynamic-insert = "true"
 *                  dynamic-update = "true"
 * @author wohlgemuth
 * 
 */
public class StandardHistory implements Queryable {

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

    private Standard standard;

    private Timestamp timeStamp;

    /**
	 * who changed it
	 */
    private String changedBy;

    /**
	 * @hibernate.id column = "`id`" generator-class = "assigned"
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
	 * @swt.modify canModify="false"
	 * @hibernate.property column = "`max_distance_ratio`" update = "true"
	 *                     insert = "true" not-null = "false"
	 * @see edu.ucdavis.genomics.metabolomics.binbase.bdi.types.experiment.sample.bin.StandardHistory#getMaximalDistanceRatio()
	 */
    public Double getMaximalDistanceRatio() {
        return maximalDistanceRatio;
    }

    /**
	 * @swt.variable visible="true" name="Maximal Qualifier Ratio"
	 *               searchable="true"
	 * @swt.modify canModify="false"
	 * @hibernate.property column = "`max_ratio`" update = "true" insert =
	 *                     "true" not-null = "false"
	 * @see edu.ucdavis.genomics.metabolomics.binbase.bdi.types.experiment.sample.bin.StandardHistory#getMinimalQualifierRatio()
	 */
    public Double getMaximalQualifierRatio() {
        return maximalQualifierRatio;
    }

    /**
	 * @swt.variable visible="true" name="Minimal Apex Sn" searchable="true"
	 * @swt.modify canModify="false"
	 * @hibernate.property column = "`min_apex_sn`" update = "true" insert =
	 *                     "true" not-null = "false"
	 * @see edu.ucdavis.genomics.metabolomics.binbase.bdi.types.experiment.sample.bin.StandardHistory#getMinimalApexSn()
	 */
    public Double getMinimalApexSn() {
        return minimalApexSn;
    }

    /**
	 * @swt.variable visible="true" name="Minimal Distance Ratio"
	 *               searchable="true"
	 * @swt.modify canModify="false"
	 * @hibernate.property column = "`min_distance_ratio`" update = "true"
	 *                     insert = "true" not-null = "false"
	 * @see edu.ucdavis.genomics.metabolomics.binbase.bdi.types.experiment.sample.bin.StandardHistory#getMinimalDistanceRatio()
	 */
    public Double getMinimalDistanceRatio() {
        return minimalDistanceRatio;
    }

    /**
	 * @swt.variable visible="true" name="Minimal Qualifier Ratio"
	 *               searchable="true"
	 * @swt.modify canModify="false"
	 * @hibernate.property column = "`min_ratio`" update = "true" insert =
	 *                     "true" not-null = "false"
	 * @see edu.ucdavis.genomics.metabolomics.binbase.bdi.types.experiment.sample.bin.StandardHistory#getMinimalQualifierRatio()
	 */
    public Double getMinimalQualifierRatio() {
        return minimalQualifierRatio;
    }

    /**
	 * @swt.variable visible="true" name="Minimal Similarity" searchable="true"
	 * @swt.modify canModify="false"
	 * @hibernate.property column = "`min_similarity`" update = "true" insert =
	 *                     "true" not-null = "false"
	 * @see edu.ucdavis.genomics.metabolomics.binbase.bdi.types.experiment.sample.bin.StandardHistory#getMinimalSimilarity()
	 */
    public Double getMinimalSimilarity() {
        return minimalSimilarity;
    }

    /**
	 * @swt.variable visible="true" name="Qualifier" searchable="true"
	 * @swt.modify canModify="false"
	 * @hibernate.property column = "`qualifier`" update = "true" insert =
	 *                     "true" not-null = "false"
	 * @see edu.ucdavis.genomics.metabolomics.binbase.bdi.types.experiment.sample.spectra.Spectra#getRetentionIndex()
	 * 
	 * @uml.property name="quantifier"
	 */
    public Integer getQualifier() {
        return qualifier;
    }

    /**
	 * @swt.variable visible="true" name="Qualifier Max Ratio" searchable="true"
	 * @swt.modify canModify="false"
	 * @return
	 */
    public Double getQualifierMaxRatio() {
        return this.qualifierMaxRatio;
    }

    /**
	 * @hibernate.property column = "`required`" update = "true" insert = "true"
	 *                     not-null = "false"
	 * @see edu.ucdavis.genomics.metabolomics.binbase.bdi.types.experiment.sample.bin.Bin#getExport()
	 * 
	 * @uml.property name="exportString"
	 */
    public String getRequiredString() {
        return requiredString;
    }

    /**
	 * @swt.variable visible="true" name="Required" searchable="true"
	 * @swt.modify canModify="false"
	 * @see edu.ucdavis.genomics.metabolomics.binbase.bdi.types.experiment.sample.bin.StandardHistory#isRequired()
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
	 * @hibernate.many-to-one column = "`bin_id`" class =
	 *                        "edu.ucdavis.genomics.metabolomics.binbase.bdi.types.experiment.sample.bin.Standard"
	 *                        update = "true" insert = "true"
	 * @author wohlgemuth
	 * @version May 24, 2006
	 * @return
	 */
    public Standard getStandard() {
        return standard;
    }

    public void setStandard(Standard standard) {
        this.standard = standard;
    }

    /**
	 * @swt.variable visible="false" name="Date" searchable="true"
	 * @swt.modify canModify="false"
	 * @hibernate.property column = "`changed_at`" update = "true" insert =
	 *                     "true" not-null = "false"
	 * @return
	 */
    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    /**
	 * @swt.variable visible="true" name="Date" searchable="true"
	 * @swt.modify canModify="false"
	 * @return
	 */
    public Date getDate() {
        return new Date(getTimeStamp().getTime());
    }

    /**
	 * @swt.variable visible="true" name="User" searchable="true"
	 * @swt.modify canModify="false"
	 * 
	 * @hibernate.property column = "`changed_by`" update = "true" insert =
	 *                     "true" not-null = "false"
	 * 
	 * @return
	 */
    public String getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }
}
