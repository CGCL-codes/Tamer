package net.sourceforge.solexatools.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class ExperimentLibraryDesign implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private Integer experimentLibraryDesignId;

    private String name;

    private String description;

    private String constructionProtocol;

    private String strategy;

    private String source;

    private String selection;

    private String layout;

    private String pairedOrientation;

    private String nominalLength;

    private String nominalSdev;

    public ExperimentLibraryDesign() {
        super();
    }

    public int compareTo(ExperimentLibraryDesign that) {
        if (that == null) return -1;
        if (that.getName() == this.getName()) return 0;
        if (that.getName() == null) return -1;
        return (that.getName().compareTo(this.getName()));
    }

    public String toString() {
        return new ToStringBuilder(this).append("experimentLibraryDesignId", getExperimentLibraryDesignId()).append("name", getName()).toString();
    }

    public boolean equals(Object other) {
        if ((this == other)) return true;
        if (!(other instanceof ExperimentLibraryDesign)) return false;
        ExperimentLibraryDesign castOther = (ExperimentLibraryDesign) other;
        return new EqualsBuilder().append(this.getName(), castOther.getName()).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(getName()).toHashCode();
    }

    public Integer getExperimentLibraryDesignId() {
        return experimentLibraryDesignId;
    }

    public void setExperimentLibraryDesignId(Integer experimentLibraryDesignId) {
        this.experimentLibraryDesignId = experimentLibraryDesignId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getConstructionProtocol() {
        return constructionProtocol;
    }

    public void setConstructionProtocol(String constructionProtocol) {
        this.constructionProtocol = constructionProtocol;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSelection() {
        return selection;
    }

    public void setSelection(String selection) {
        this.selection = selection;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public String getPairedOrientation() {
        return pairedOrientation;
    }

    public void setPairedOrientation(String pairedOrientation) {
        this.pairedOrientation = pairedOrientation;
    }

    public String getNominalLength() {
        return nominalLength;
    }

    public void setNominalLength(String nominalLength) {
        this.nominalLength = nominalLength;
    }

    public String getNominalSdev() {
        return nominalSdev;
    }

    public void setNominalSdev(String nominalSdev) {
        this.nominalSdev = nominalSdev;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
}
