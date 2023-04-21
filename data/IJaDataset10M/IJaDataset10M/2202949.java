package in.raster.mayam.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.dcm4che.data.Dataset;
import org.dcm4che.dict.Tags;

/**
 *
 * @author  BabuHussain
 * @version 0.5
 *
 */
public class Series implements Serializable {

    private String SeriesInstanceUID;

    private String StudyInstanceUID;

    private String SeriesNumber;

    private String Modality;

    private String institutionName;

    private List<Instance> imageList;

    private String seriesDesc;

    private String bodyPartExamined;

    private int seriesRelatedInstance;

    private boolean multiframe;

    private String instanceUID;

    public Series() {
        SeriesInstanceUID = "";
        StudyInstanceUID = "";
        Modality = "";
        SeriesNumber = "";
        seriesDesc = "";
        multiframe = false;
        instanceUID = "";
        imageList = new ArrayList<Instance>();
    }

    public Series(Dataset dataset) {
        this.SeriesInstanceUID = dataset.getString(Tags.SeriesInstanceUID);
        this.StudyInstanceUID = dataset.getString(Tags.StudyInstanceUID);
        this.SeriesNumber = dataset.getString(Tags.SeriesNumber) != null ? dataset.getString(Tags.SeriesNumber) : "";
        this.Modality = dataset.getString(Tags.Modality);
        this.bodyPartExamined = dataset.getString(Tags.BodyPartExamined);
        this.seriesDesc = dataset.getString(Tags.SeriesDescription) != null ? dataset.getString(Tags.SeriesDescription) : "";
        this.institutionName = dataset.getString(Tags.InstitutionName) != null ? dataset.getString(Tags.InstitutionName) : "";
    }

    /**
     * @return the SeriesInstanceUID
     */
    public String getSeriesInstanceUID() {
        return SeriesInstanceUID;
    }

    /**
     * @param SeriesInstanceUID the SeriesInstanceUID to set
     */
    public void setSeriesInstanceUID(String SeriesInstanceUID) {
        this.SeriesInstanceUID = SeriesInstanceUID;
    }

    /**
     * @return the Modality
     */
    public String getModality() {
        return Modality;
    }

    /**
     * @param Modality the Modality to set
     */
    public void setModality(String Modality) {
        this.Modality = Modality;
    }

    /**
     * 
     * @param image
     */
    public void addImage(Instance image) {
        this.imageList.add(image);
    }

    public String getSeriesNumber() {
        return SeriesNumber;
    }

    /**
     * 
     * @param seriesNumber
     */
    public void setSeriesNumber(String seriesNumber) {
        SeriesNumber = seriesNumber;
    }

    /**
     * @return the imageList
     */
    public List<Instance> getImageList() {
        return imageList;
    }

    /**
     * @param imageList the imageList to set
     */
    public void setImageList(List<Instance> imageList) {
        this.imageList = imageList;
    }

    public String getStudyInstanceUID() {
        return StudyInstanceUID;
    }

    public void setStudyInstanceUID(String StudyInstanceUID) {
        this.StudyInstanceUID = StudyInstanceUID;
    }

    public String getSeriesDesc() {
        return seriesDesc;
    }

    public void setSeriesDesc(String seriesDesc) {
        this.seriesDesc = seriesDesc;
    }

    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    public int getSeriesRelatedInstance() {
        return seriesRelatedInstance;
    }

    public void setSeriesRelatedInstance(int seriesRelatedInstance) {
        this.seriesRelatedInstance = seriesRelatedInstance;
    }

    public String getBodyPartExamined() {
        return bodyPartExamined;
    }

    public void setBodyPartExamined(String bodyPartExamined) {
        this.bodyPartExamined = bodyPartExamined;
    }

    public String getInstanceUID() {
        return instanceUID;
    }

    public void setInstanceUID(String instanceUID) {
        this.instanceUID = instanceUID;
    }

    public boolean isMultiframe() {
        return multiframe;
    }

    public void setMultiframe(boolean multiframe) {
        this.multiframe = multiframe;
    }
}
