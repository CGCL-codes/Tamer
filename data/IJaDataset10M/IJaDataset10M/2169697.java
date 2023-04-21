package com.fddtool.ui.view.subjectarea;

import java.util.List;
import javax.faces.model.SelectItem;
import com.fddtool.pd.common.AppEntryType;
import com.fddtool.pd.fddproject.ProjectAspect;
import com.fddtool.pd.fddproject.SubjectArea;
import com.fddtool.resource.MessageKey;
import com.fddtool.ui.faces.FacesUtils;
import com.fddtool.ui.faces.bean.ManagedBeans;
import com.fddtool.ui.faces.bean.RefreshableManagedBean;
import com.fddtool.ui.util.GuiUtil;
import com.fddtool.ui.view.FddpmaView;
import com.fddtool.ui.view.navigation.NavigationBean;
import com.fddtool.ui.view.navigation.NavigationResults;
import com.fddtool.ui.view.tree.TreeProviderBean;
import com.fddtool.util.J2EETransaction;
import com.fddtool.util.Utils;

/**
 * This is a JSF - managed bean that supports the view where user deletes
 * subject areas. It handles direct delete, or delete with reassignment of
 * enclosed activities to another subject area.
 * 
 * @author Serguei Khramtchenko
 */
public class DeleteSubjectAreaBean extends RefreshableManagedBean {

    /**
     * Initial serialization ID
     */
    private static final long serialVersionUID = 1563733709256684336L;

    /**
     * An option that indicates that user wants to delete subject area and all
     * of its content. This value is hard-coded in corresponding JSP.
     */
    public static final String OPTION_DELETE_ALL = "deleteAll";

    /**
     * An option that indicates that user wants to delete subject area but first
     * reassign all the activities to another subject area. This value is
     * hard-coded in corresponding JSP.
     */
    public static final String OPTION_REASSIGN_AND_DELETE = "reassignAndDelete";

    /**
     * The identifier of area where the activities have to be reassigned.
     */
    private transient String assignAreaId;

    /**
     * The list of subject areas available for reassignment.
     */
    private transient List<SelectItem> availableSubjectAreas;

    /**
     * The deletion option selected by user. Initial value is to reassign
     * activities first, then delete.
     */
    private String deleteOption = OPTION_REASSIGN_AND_DELETE;

    /**
     * {@inheritDoc}
     */
    public void refresh() {
        assignAreaId = null;
        availableSubjectAreas = null;
        deleteOption = OPTION_REASSIGN_AND_DELETE;
    }

    /**
     * Returns the title for a view.
     * 
     * @return String containing title for "Delete subject Area" view.
     */
    public String getViewTitle() {
        SubjectArea sa = obtainLatestSubjectArea();
        if (sa != null) {
            return messageProvider.getMessage(MessageKey.CAPTION_DELETE_SUBJECT_AREA, new String[] { sa.getName() });
        } else {
            return processViewTitleForUnknowObject(AppEntryType.SUBJECT_AREA);
        }
    }

    /**
     * Returns identifier of the area where subject activities of deleted area
     * have to be assigned to.
     * 
     * @return String with identifier of subject area, or empty string if user
     *         has not yet selected the destination area.
     */
    public String getAssignAreaId() {
        return assignAreaId;
    }

    /**
     * Sets identifier of the area where subject activities of deleted area have
     * to be assigned to. JSF framework calls this method when user submits
     * "Delete subject Area" view.
     * 
     * @param assignAreaId
     *            String with identifier of subject area selected by user.
     */
    public void setAssignAreaId(String assignAreaId) {
        this.assignAreaId = assignAreaId;
    }

    /**
     * Returns a list of available subject areas that the activities from
     * deleted area may be assigned to.
     * 
     * @return non-null List of <code>SelectItem</code> objects.
     */
    public List<SelectItem> getAvailableSubjectAreas() {
        if (availableSubjectAreas == null) {
            SubjectArea area = TreeProviderBean.findInstance().findSelectedSubjectArea();
            if (area != null) {
                ProjectAspect ps = area.getProjectAspect();
                String prompt = messageProvider.getMessage(MessageKey.LBL_PROMPT);
                List<SubjectArea> list = ps.listSubjectAreas();
                list.remove(area);
                List<SelectItem> result = GuiUtil.convertListToSelectItems(list, null, prompt);
                availableSubjectAreas = result;
            }
        }
        return availableSubjectAreas;
    }

    /**
     * Validates user input. If user has decided to move activities to another
     * subject area, this method will check that the destination subject area is
     * selected.
     * 
     * @return boolean value of <code>true</code> if the user input is valid,
     *         and <code>false</code> if it is not. In this case this method
     *         posts error messages to the appropriate components.
     */
    private boolean validate() {
        if (getDeleteOption().equals(OPTION_REASSIGN_AND_DELETE) && Utils.isEmpty(getAssignAreaId())) {
            String msg = messageProvider.getMessage(MessageKey.ERROR_VALUE_IS_REQUIRED);
            FacesUtils.addErrorMessage("assignTo", msg);
            return false;
        }
        return true;
    }

    /**
     * Performs the delete operation according to the options selected by user.
     * 
     * @return String that reports the result of the operation. JSF framework
     *         uses this result to decide the view to forward to.
     * 
     * @see com.fddtool.ui.view.NavigationResults#SUCCESS
     * @see com.fddtool.ui.view.NavigationResults#RETRY
     */
    public String deleteAction() {
        if (validate()) {
            SubjectArea area = TreeProviderBean.findInstance().findSelectedSubjectArea();
            ProjectAspect aspect = TreeProviderBean.findInstance().findSelectedProjectAspect();
            if (area == null || aspect == null) {
                NavigationBean.processObjectNotFound();
                return NavigationResults.NONE;
            }
            J2EETransaction tr = null;
            try {
                tr = new J2EETransaction();
                tr.begin();
                if (getDeleteOption().equals(OPTION_REASSIGN_AND_DELETE)) {
                    SubjectArea dest = SubjectArea.findById(getAssignAreaId());
                    area.moveActivitiesTo(dest);
                }
                area.delete();
                tr.commit();
                TreeProviderBean.findInstance().nodeChildrenChanged(aspect, aspect);
                ManagedBeans.refresh();
                NavigationBean.redirectRequest(FddpmaView.VIEW_PROJECT_ASPECT_WORKPLACE);
                return NavigationResults.NONE;
            } catch (Exception ex) {
                handleException(ex, tr);
            }
        }
        return NavigationResults.RETRY;
    }

    /**
     * Returns the selected delete option. The delete option defines if the
     * activities should also be deleted, or they should first be reassigned to
     * another subject area.
     * 
     * @return String that contains the currently selected delete option. The
     *         default option is to reassign activities to another area.
     * 
     * @see #OPTION_REASSIGN_AND_DELETE
     */
    public String getDeleteOption() {
        return deleteOption;
    }

    /**
     * Sets the delete option that defines if the activities should also be
     * deleted, or they should first be reassigned to another subject area.
     * 
     * @param deleteOption
     *            String that contains the currently selected delete option.
     * 
     * @see #OPTION_REASSIGN_AND_DELETE
     * @see #OPTION_DELETE_ALL
     */
    public void setDeleteOption(String deleteOption) {
        this.deleteOption = deleteOption;
    }
}
