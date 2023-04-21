package uk.ac.ebi.intact.plugins.dbupdate.experiments;

import uk.ac.ebi.intact.util.cdb.UpdateExperimentAnnotationsFromPudmed;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO comment this!
 *
 * @author Bruno Aranda (baranda@ebi.ac.uk)
 * @version $Id: UpdateSingleExperimentReport.java 10012 2007-10-16 15:57:58Z baranda $
 */
public class UpdateSingleExperimentReport {

    private UpdateExperimentAnnotationsFromPudmed.UpdateReport updateReport;

    private String experimentAc;

    private String experimentLabel;

    private boolean invalid;

    private String invalidMessage;

    private List<String> warningMessages;

    private UpdatedValue shortLabelValue;

    private UpdatedValue fullNameValue;

    public UpdateSingleExperimentReport(String experimentAc, String experimentLabel) {
        this.experimentAc = experimentAc;
        this.experimentLabel = experimentLabel;
    }

    public UpdateSingleExperimentReport(UpdateExperimentAnnotationsFromPudmed.UpdateReport updateReport) {
        this.updateReport = updateReport;
    }

    public UpdateExperimentAnnotationsFromPudmed.UpdateReport getUpdateReport() {
        return updateReport;
    }

    public void setUpdateReport(UpdateExperimentAnnotationsFromPudmed.UpdateReport updateReport) {
        this.updateReport = updateReport;
    }

    public boolean isUpdated() {
        return isAuthorEmailUpdated() || isAuthorListUpdated() || isContactUpdated() || isFullNameUpdated() || isJournalUpdated() || isShortLabelUpdated() || isYearUpdated();
    }

    public String getExperimentAc() {
        return experimentAc;
    }

    public String getExperimentLabel() {
        return experimentLabel;
    }

    public boolean isInvalid() {
        return invalid;
    }

    public String getInvalidMessage() {
        return invalidMessage;
    }

    public void setInvalidMessage(String invalidMessage) {
        this.invalid = true;
        this.invalidMessage = invalidMessage;
    }

    public boolean isShortLabelUpdated() {
        return shortLabelValue != null;
    }

    public boolean isFullNameUpdated() {
        return fullNameValue != null;
    }

    public boolean isAuthorListUpdated() {
        if (updateReport == null) return false;
        return updateReport.isAuthorListUpdated();
    }

    public boolean isContactUpdated() {
        if (updateReport == null) return false;
        return updateReport.isContactUpdated();
    }

    public boolean isYearUpdated() {
        if (updateReport == null) return false;
        return updateReport.isYearUpdated();
    }

    public boolean isJournalUpdated() {
        if (updateReport == null) return false;
        return updateReport.isJournalUpdated();
    }

    public boolean isAuthorEmailUpdated() {
        if (updateReport == null) return false;
        return updateReport.isAuthorEmailUpdated();
    }

    public List<String> getWarningMessages() {
        return warningMessages;
    }

    public void setWarningMessages(List<String> warningMessages) {
        this.warningMessages = warningMessages;
    }

    public boolean addWarningMessage(String o) {
        if (warningMessages == null) {
            warningMessages = new ArrayList<String>();
        }
        return warningMessages.add(o);
    }

    public UpdatedValue getShortLabelValue() {
        return shortLabelValue;
    }

    public void setShortLabelValue(UpdatedValue shortLabelValue) {
        this.shortLabelValue = shortLabelValue;
    }

    public UpdatedValue getFullNameValue() {
        return fullNameValue;
    }

    public void setFullNameValue(UpdatedValue fullNameValue) {
        this.fullNameValue = fullNameValue;
    }

    public UpdatedValue getAuthorListValue() {
        return convertValue(updateReport.getAuthorListValue());
    }

    public UpdatedValue getContactListValue() {
        return convertValue(updateReport.getContactListValue());
    }

    public UpdatedValue getYearListValue() {
        return convertValue(updateReport.getYearListValue());
    }

    public UpdatedValue getJournalListValue() {
        return convertValue(updateReport.getJournalListValue());
    }

    public UpdatedValue getAuthorEmailValue() {
        return convertValue(updateReport.getAuthorEmailValue());
    }

    private UpdatedValue convertValue(UpdateExperimentAnnotationsFromPudmed.UpdatedValue ueaValue) {
        return new UpdatedValue(ueaValue.getOldValue(), ueaValue.getNewValue());
    }
}
