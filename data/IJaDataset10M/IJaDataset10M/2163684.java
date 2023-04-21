package org.eledge.pages;

import static org.eledge.Eledge.dbcommit;
import java.util.List;
import metadata.FormatDescription;
import metadata.FormatIdentification;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.eledge.domain.ReportAssignment;
import org.eledge.domain.permissions.ManageReportPermission;
import org.eledge.domain.permissions.PermissionRequired;

/**
 * @author robertz
 * 
 */
public abstract class ReportProperties extends EledgeSecureBasePage implements PermissionRequired {

    private IPropertySelectionModel contentTypesModel;

    public abstract ReportAssignment getAssignment();

    public String getPermissionName() {
        return ManageReportPermission.NAME;
    }

    public void update(IRequestCycle cycle) {
        getAssignment().updatePreferredTypesString();
        dbcommit(cycle);
    }

    public IPropertySelectionModel getContentTypesModel() {
        if (contentTypesModel == null) {
            contentTypesModel = new ContentTypesModel();
        }
        return contentTypesModel;
    }

    public String getPgtitle() {
        return format("pgtitle", getAssignment().getTitle());
    }

    public Boolean getAllowMultipleTries() {
        return getAssignment().getParameters().getAllowMultipleTries();
    }

    public void setAllowMultipleTries(Boolean b) {
        getAssignment().getParameters().setAllowMultipleTries(b);
    }

    public Boolean getEnforceDeadlines() {
        return getAssignment().getParameters().getEnforceDeadlines();
    }

    public void setEnforceDeadlines(Boolean b) {
        getAssignment().getParameters().setEnforceDeadlines(b);
    }

    public List<?> getPreferredContentTypes() {
        return getAssignment().getPreferredContentTypes();
    }

    public void setPreferredContentTypes(List<String> l) {
        getAssignment().setPreferredContentTypes(l);
    }

    public boolean getEnforceContentType() {
        return getAssignment().getEnforceContentType();
    }

    public void setEnforceContentType(boolean b) {
        getAssignment().setEnforceContentType(b);
    }

    public int getMaxLength() {
        return getAssignment().getMaxLength();
    }

    public void setMaxLength(int i) {
        getAssignment().setMaxLength(i);
    }
}

class ContentTypesModel implements IPropertySelectionModel {

    List<?> descriptions;

    public ContentTypesModel() {
        descriptions = FormatIdentification.getKnownDescriptions();
    }

    public int getOptionCount() {
        return descriptions.size();
    }

    public Object getOption(int index) {
        return ((FormatDescription) descriptions.get(index)).getShortName();
    }

    public String getLabel(int index) {
        return ((FormatDescription) descriptions.get(index)).getLongName();
    }

    public String getValue(int index) {
        return Integer.toString(index);
    }

    public Object translateValue(String value) {
        return ((FormatDescription) descriptions.get(Integer.parseInt(value))).getShortName();
    }
}
