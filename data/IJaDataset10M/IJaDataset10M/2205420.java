package woko.tracker.facets.admin;

import net.sf.woko.facets.edit.SaveObject;
import net.sourceforge.jfacets.IInstanceFacet;
import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.stripes.validation.ValidationErrors;
import woko.tracker.model.TrackerObject;
import woko.tracker.model.TrackerUser;
import woko.tracker.model.UserManager;
import woko.tracker.facets.reporter.SaveTrackerObjectReporter;
import java.util.Date;

@FacetKey(name = "save", profileId = UserManager.ROLE_TRACKER_ADMIN, targetObjectType = TrackerObject.class)
public class SaveTrackerObjectAdmin extends SaveTrackerObjectReporter {

    /**
     * Allow to update all items !
     */
    @Override
    public boolean matchesTargetObject(Object o) {
        return true;
    }
}
