package com.fddtool.ui.view.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.fddtool.pd.common.AppEntryType;
import com.fddtool.pd.fddproject.Activity;
import com.fddtool.pd.fddproject.Feature;
import com.fddtool.pd.fddproject.FeatureProgress;
import com.fddtool.pd.fddproject.MilestoneDescription;
import com.fddtool.resource.MessageKey;
import com.fddtool.ui.faces.bean.RefreshableManagedBean;
import com.fddtool.ui.view.feature.UIFeature;
import com.fddtool.ui.view.navigation.NavigationBean;
import com.fddtool.ui.view.navigation.NavigationResults;
import com.fddtool.ui.view.tree.TreeProviderBean;
import com.fddtool.util.UserLocalizer;
import com.fddtool.util.Utils;

/**
 * This is a JSF-managed bean that supports the view where the user can see
 * summary of the activity.
 * 
 * @author Serguei Khramtchenko
 */
public class SummaryActivityBean extends RefreshableManagedBean {

    /**
	 * Initial serialization ID
	 */
    private static final long serialVersionUID = 5975068458798625850L;

    /**
	 * List of features within activity.
	 */
    private transient List<UIFeature> features;

    /**
	 * The identifier of a feature that user clicks on.
	 */
    private Long selectedFeatuteId;

    public void refresh() {
        super.refresh();
        features = null;
    }

    /**
	 * Indicates if current activity is read-only for current user. The activity
	 * will be rad-only if user does not have permissions to modify activities
	 * in current project.
	 * 
	 * @return boolean value of <code>true</code> if activity is read-only and
	 *         <code>false</code> if it could be modified/deleted.
	 */
    public boolean isActivityReadOnly() {
        return false;
    }

    /**
	 * Indicates if features of current activities are read-only for current
	 * user. The features will be rad-only if user does not have permissions to
	 * modify features in current project.
	 * 
	 * @return boolean value of <code>true</code> if features are read-only
	 *         and <code>false</code> if they could be inserted/deleted.
	 */
    public boolean isFeatureReadOnly() {
        return false;
    }

    /**
	 * Returns the title for a view.
	 * 
	 * @return String containing title for "Business Area Workplace" view.
	 */
    public String getViewTitle() {
        Activity a = obtainLatestActivity();
        if (a != null) {
            return messageProvider.getMessage(MessageKey.CAPTION_ACTIVITY, new String[] { a.getName() });
        } else {
            return processViewTitleForUnknowObject(AppEntryType.ACTIVITY);
        }
    }

    /**
	 * Returns a list of features that belong to currently displayed activity.
	 * 
	 * @return List of <code>UIFeature</code> elements.
	 */
    public List<UIFeature> getFeatures() {
        Activity a = obtainLatestActivity();
        if (a != null && features == null) {
            List<FeatureProgress> fps = a.listFeatureProgress();
            Map<Feature, FeatureProgress> map = new HashMap<Feature, FeatureProgress>();
            Iterator<FeatureProgress> iter = fps.iterator();
            while (iter.hasNext()) {
                FeatureProgress fp = iter.next();
                map.put(fp.getFeature(), fp);
            }
            List<UIFeature> result = new ArrayList<UIFeature>();
            Iterator<Feature> iter1 = a.listFeatures().iterator();
            while (iter1.hasNext()) {
                Feature f = iter1.next();
                FeatureProgress fp = map.get(f);
                result.add(new UIFeature(f, fp));
            }
            features = result;
        }
        if (features != null) {
            return features;
        } else {
            return Collections.emptyList();
        }
    }

    /**
	 * Returns the identifier of the feature that user has selected in the view.
	 * 
	 * @return String with feature id.
	 */
    public Long getSelectedFeatuteId() {
        return selectedFeatuteId;
    }

    /**
	 * Sets the identifier of the feature that user has selected in the view.
	 * JSF framework calls this method when user selects a feature in the list.
	 * 
	 * @return String with feature id.
	 */
    public void setSelectedFeatuteId(Long selectedFeatuteId) {
        this.selectedFeatuteId = selectedFeatuteId;
    }

    /**
	 * Forwards to a feature summary view after a user has selected a feature in
	 * this view.
	 * 
	 * @return String that is <code>null</code>, as JSF framework need not
	 *         take navigation decision, this method handles navigation itself.
	 */
    public String selectFeature() {
        Activity a = obtainLatestActivity();
        Feature f = Feature.findById("" + getSelectedFeatuteId());
        if (f != null && a != null) {
            TreeProviderBean.findInstance().nodeChildrenChanged(a, f);
            NavigationBean.getInstance().processNavigationAfterTreeChanged(f, null);
        } else {
            NavigationBean.processObjectNotFound();
        }
        return NavigationResults.NONE;
    }

    /**
	 * Returns the name of the owner for the activity.
	 * 
	 * @return String with owner's name, or <code>null</code> if the activity
	 *         does not yet have an owner.
	 */
    public String getOwner() {
        Activity a = obtainLatestActivity();
        if (a == null || a.getOwner() == null) {
            return messageProvider.getMessage(MessageKey.LBL_NOT_ASSIGNED);
        } else {
            return a.getOwner().getDisplayFullName();
        }
    }

    /**
	 * Returns the completion percent for the activity.
	 * 
	 * @return String with completion percent.
	 */
    public String getPercentComplete() {
        Activity a = obtainLatestActivity();
        if (a != null) {
            return "" + a.getPercentComplete() + "%";
        } else {
            return "0%";
        }
    }

    /**
	 * Returns the date when the activity is supposed to be complete.
	 * 
	 * @return String with date (month and year) of planned completion.
	 */
    public String getExpectedCompletion() {
        Activity a = obtainLatestActivity();
        if (a != null) {
            String result = UserLocalizer.formatMonthYearDate(a.getTargetDate());
            if (Utils.isEmpty(result)) {
                result = messageProvider.getMessage(MessageKey.LBL_NOT_ASSIGNED);
            }
            return result;
        } else {
            return null;
        }
    }

    /**
     * Returns a list of milestones for features included into this activity.
     * 
     * @return List of <code>MilestoneDescription</code> objects.
     */
    public List<MilestoneDescription> getMilestones() {
        Activity a = obtainLatestActivity();
        return a == null ? null : a.determineMilestoneSet().listMilestones();
    }
}
