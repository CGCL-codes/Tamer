package com.fddtool.ui.view.feature;

import java.util.List;
import com.fddtool.exception.DuplicateNameException;
import com.fddtool.pd.common.AppEntryType;
import com.fddtool.pd.fddproject.Feature;
import com.fddtool.pd.fddproject.Project;
import com.fddtool.resource.MessageKey;
import com.fddtool.ui.faces.FacesUtils;
import com.fddtool.ui.faces.bean.ManagedBeans;
import com.fddtool.ui.faces.bean.RefreshableManagedBean;
import com.fddtool.ui.view.navigation.NavigationBean;
import com.fddtool.ui.view.navigation.NavigationResults;
import com.fddtool.util.J2EETransaction;
import com.fddtool.util.Utils;

/**
 * This is a JSF-managed bean that backs up a view which allows to insert new,
 * unassigned features into a project.
 * 
 * @author Serguei Khramtchenko
 */
public class InsertProjectFeaturesBean extends RefreshableManagedBean {

    /**
     * Initial serial ID
     */
    private static final long serialVersionUID = 6463427901145213335L;

    /**
     * Comma- or CRLF- separated list of project features.
     */
    private String features;

    /**
     * Returns the title for the view.
     * 
     * @return String containing view title.
     */
    public String getViewTitle() {
        Project p = obtainLatestProject();
        if (p != null) {
            return messageProvider.getMessage(MessageKey.CAPTION_INSERT_PROJECT_FEATURES, new Object[] { p.getName() });
        } else {
            return processViewTitleForUnknowObject(AppEntryType.PROJECT);
        }
    }

    /**
     * Creates a new project by saving user's input in the persistent storage.
     * JSF framework calls this method when a user clicks "Save" button on
     * "Insert Project" view.
     * 
     * @return String describing the result of the operation. The JSF framework
     *         uses this value to figure out the next view to display.
     * 
     * @see com.fddtool.ui.view.NavigationResults#SUCCESS SUCCESS
     * @see com.fddtool.ui.view.NavigationResults#RETRY RETRY
     */
    public String save() {
        Project project = obtainLatestProject();
        if (project == null) {
            NavigationBean.processObjectNotFound();
            return NavigationResults.NONE;
        }
        J2EETransaction tr = null;
        try {
            tr = new J2EETransaction();
            tr.begin();
            List<String> features = Utils.parseDelimitedList(getFeatures(), Utils.CRLF);
            int i = 0;
            int successCount = 0;
            while (i < features.size()) {
                String featureName = features.get(i);
                if (!Utils.isEmpty(featureName)) {
                    Feature f = new Feature(null, featureName, null, project, null, null, null, false);
                    try {
                        f.save();
                        features.remove(i);
                        successCount++;
                    } catch (DuplicateNameException ex) {
                        i++;
                        FacesUtils.addErrorMessage(ex.getMessage());
                    }
                }
            }
            tr.commit();
            String msg = messageProvider.getMessage(MessageKey.INFO_INSERTED_N_FEATURES, new String[] { "" + successCount });
            FacesUtils.addInfoMessage(msg);
            setFeatures(Utils.createDelimitedList(features, "\n"));
            ManagedBeans.refresh();
            if (features.size() == 0) {
                return NavigationResults.SUCCESS;
            } else {
                return NavigationResults.RETRY;
            }
        } catch (Exception ex) {
            handleException(ex, tr);
        }
        return NavigationResults.RETRY;
    }

    /**
     * Returns CRLF-separated list of feature names that need to be inserted
     * into a project.
     * 
     * @return String containing a list of features.
     */
    public String getFeatures() {
        return features;
    }

    /**
     * Sets the CRLF-separated list of feature names that need to be inserted
     * into a project. JSF framework calls this method in response to user's
     * input.
     * 
     * @param features
     *            String containing a list of feature names.
     */
    public void setFeatures(String features) {
        this.features = features;
    }
}
