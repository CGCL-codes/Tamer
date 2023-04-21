package woko.tracker.facets.reporter;

import net.sf.woko.facets.edit.DeleteObject;
import net.sourceforge.jfacets.IInstanceFacet;
import net.sourceforge.jfacets.annotations.FacetKey;
import net.sourceforge.jfacets.annotations.FacetKeyList;
import woko.tracker.model.*;

@FacetKeyList(keys = { @FacetKey(name = "delete", profileId = UserManager.ROLE_TRACKER_REPORTER, targetObjectType = Comment.class), @FacetKey(name = "delete", profileId = UserManager.ROLE_TRACKER_REPORTER, targetObjectType = Attachment.class) })
public class DeleteTrackerObjectReporter extends DeleteObject implements IInstanceFacet {

    /**
     * Only allow to update my own comments/attachments !
     */
    public boolean matchesTargetObject(Object o) {
        return ((TrackerObject) o).isOwnedBy((TrackerUser) getWokoUser());
    }
}
