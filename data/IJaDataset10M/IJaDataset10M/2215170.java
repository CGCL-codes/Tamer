package woko.tracker.facets.admin;

import net.sf.woko.facets.edit.EditPropertyValue;
import net.sf.woko.util.Util;
import net.sf.woko.usermgt.WokoRole;
import net.sourceforge.jfacets.annotations.FacetKey;
import woko.tracker.model.UserManager;
import woko.tracker.model.TrackerUser;
import java.util.List;

@FacetKey(name = "editPropertyValue_role", profileId = UserManager.ROLE_TRACKER_ADMIN, targetObjectType = TrackerUser.class)
public class EditPropValUserRole extends EditPropertyValue {

    public EditPropValUserRole() {
        propertySpecific = true;
    }

    public String getFragmentPath() {
        super.getFragmentPath();
        return "/WEB-INF/tracker/admin/prop-user-role-edit.jsp";
    }

    public List<WokoRole> getRoles() {
        String hql = "from WokoRole as r where r.name = 'ROLE_TRACKER_REPORTER' or r.name = 'ROLE_TRACKER_ADMIN'";
        List res = getSession().createQuery(hql).list();
        return res;
    }
}
