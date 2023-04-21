package org.jcvi.vics.model.metadata;

import com.google.gwt.user.client.rpc.IsSerializable;
import org.jcvi.vics.model.download.DataFile;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: tsafford
 * Date: Apr 13, 2006
 * Time: 2:49:28 PM
 */
public class Sample implements IsSerializable, Serializable, Comparable {

    private Long sampleId;

    private String sampleAcc = "";

    private String sampleName = "";

    private String[] comments;

    private Double filterMin;

    private Double filterMax;

    private String intellectualPropertyNotice;

    private String title;

    private Set<BioMaterial> bioMaterials;

    private Set<Library> libraries;

    private Set<DataFile> dataFiles;

    public Sample() {
    }

    public Long getSampleId() {
        return sampleId;
    }

    public void setSampleId(Long sampleId) {
        this.sampleId = sampleId;
    }

    public String[] getComments() {
        return comments;
    }

    public void setComments(String[] comments) {
        this.comments = comments;
    }

    public String getSampleAcc() {
        return sampleAcc;
    }

    public void setSampleAcc(String sampleAcc) {
        this.sampleAcc = sampleAcc;
    }

    public String getSampleName() {
        return sampleName;
    }

    public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
    }

    public Double getFilterMax() {
        return filterMax;
    }

    public void setFilterMax(Double filterMax) {
        this.filterMax = filterMax;
    }

    public Double getFilterMin() {
        return filterMin;
    }

    public void setFilterMin(Double filterMin) {
        this.filterMin = filterMin;
    }

    public Set<BioMaterial> getBioMaterials() {
        return bioMaterials;
    }

    public void setBioMaterials(Set<BioMaterial> bioMaterials) {
        this.bioMaterials = bioMaterials;
    }

    public Set<Library> getLibraries() {
        return libraries;
    }

    public void setLibraries(Set<Library> libraries) {
        this.libraries = libraries;
    }

    public Set<DataFile> getDataFiles() {
        return dataFiles;
    }

    public void setDataFiles(Set<DataFile> dataFiles) {
        this.dataFiles = dataFiles;
    }

    public String getIntellectualPropertyNotice() {
        return intellectualPropertyNotice;
    }

    public void setIntellectualPropertyNotice(String intellectualPropertyNotice) {
        this.intellectualPropertyNotice = intellectualPropertyNotice;
    }

    public int compareTo(Object o) {
        return this.sampleName.compareTo(((Sample) o).getSampleName());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
