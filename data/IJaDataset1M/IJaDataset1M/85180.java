package ca.ubc.icapture.genapha.forms;

import ca.ubc.icapture.genapha.beans.MakePathwayGeneComparison;
import icapture.beans.DB.Gene;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.ValidatorForm;

/**
 * Form bean for Struts File Upload.
 */
public class MakePathwayForm extends ValidatorForm {

    private ArrayList<Gene> availableGenes;

    private String[] selectedGenes;

    private String genes;

    private FormFile file;

    private ArrayList<MakePathwayGeneComparison> geneList;

    private String nodeText;

    private String edgeText;

    private String asthmaJS;

    private String atopyJS;

    private String ahrJS;

    private String atopicAsthmaJS;

    private String submitType;

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        setSubmitType("");
    }

    @Override
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = super.validate(mapping, request);
        if (getSubmitType().equalsIgnoreCase("Next")) {
            if (selectedGenes == null && (genes == null || genes.isEmpty()) && file.getFileSize() <= 0) {
                errors.add("makePathway", new ActionMessage("makePathway.error.noGeneOrFile"));
            }
        } else if (getSubmitType().equalsIgnoreCase("Display Pathway")) {
            boolean isValid = false;
            for (MakePathwayGeneComparison mpgc : geneList) {
                if (mpgc.getGene() != null) {
                    isValid = true;
                    break;
                }
            }
            if (!isValid) {
                errors.add("geneList", new ActionMessage("makePathway.error.noValidGene"));
            }
        }
        return errors;
    }

    /**
     * @return the availableGenes
     */
    public ArrayList<Gene> getAvailableGenes() {
        return availableGenes;
    }

    /**
     * @param availableGenes the availableGenes to set
     */
    public void setAvailableGenes(ArrayList<Gene> availableGenes) {
        this.availableGenes = availableGenes;
    }

    /**
     * @return the selectedGenes
     */
    public String[] getSelectedGenes() {
        return selectedGenes;
    }

    /**
     * @param selectedGenes the selectedGenes to set
     */
    public void setSelectedGenes(String[] selectedGenes) {
        this.selectedGenes = selectedGenes;
    }

    /**
     * @return the genes
     */
    public String getGenes() {
        return genes;
    }

    /**
     * @param genes the genes to set
     */
    public void setGenes(String genes) {
        this.genes = genes;
    }

    /**
     * @return the file
     */
    public FormFile getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(FormFile file) {
        this.file = file;
    }

    /**
     * @return the geneList
     */
    public ArrayList<MakePathwayGeneComparison> getGeneList() {
        return geneList;
    }

    /**
     * @param geneList the geneList to set
     */
    public void setGeneList(ArrayList<MakePathwayGeneComparison> geneList) {
        this.geneList = geneList;
    }

    /**
     * @return the nodeText
     */
    public String getNodeText() {
        return nodeText;
    }

    /**
     * @param nodeText the nodeText to set
     */
    public void setNodeText(String nodeText) {
        this.nodeText = nodeText;
    }

    /**
     * @return the edgeText
     */
    public String getEdgeText() {
        return edgeText;
    }

    /**
     * @param edgeText the edgeText to set
     */
    public void setEdgeText(String edgeText) {
        this.edgeText = edgeText;
    }

    /**
     * @return the asthmaJS
     */
    public String getAsthmaJS() {
        return asthmaJS;
    }

    /**
     * @param asthmaJS the asthmaJS to set
     */
    public void setAsthmaJS(String asthmaJS) {
        this.asthmaJS = asthmaJS;
    }

    /**
     * @return the atopyJS
     */
    public String getAtopyJS() {
        return atopyJS;
    }

    /**
     * @param atopyJS the atopyJS to set
     */
    public void setAtopyJS(String atopyJS) {
        this.atopyJS = atopyJS;
    }

    /**
     * @return the ahrJS
     */
    public String getAhrJS() {
        return ahrJS;
    }

    /**
     * @param ahrJS the ahrJS to set
     */
    public void setAhrJS(String ahrJS) {
        this.ahrJS = ahrJS;
    }

    /**
     * @return the atopicAsthmaJS
     */
    public String getAtopicAsthmaJS() {
        return atopicAsthmaJS;
    }

    /**
     * @param atopicAsthmaJS the atopicAsthmaJS to set
     */
    public void setAtopicAsthmaJS(String atopicAsthmaJS) {
        this.atopicAsthmaJS = atopicAsthmaJS;
    }

    /**
     * @return the submitType
     */
    public String getSubmitType() {
        return submitType;
    }

    /**
     * @param submitType the submitType to set
     */
    public void setSubmitType(String submitType) {
        this.submitType = submitType;
    }
}
