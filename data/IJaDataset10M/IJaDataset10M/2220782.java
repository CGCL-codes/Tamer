package de.juwimm.cms.authorization.model;

import org.apache.log4j.Logger;
import de.juwimm.cms.util.SequenceHelper;

/**
 * @see de.juwimm.cms.authorization.model.GroupHbm
 */
public class GroupHbmDaoImpl extends de.juwimm.cms.authorization.model.GroupHbmDaoBase {

    private static Logger log = Logger.getLogger(GroupHbmDaoImpl.class);

    @Override
    public GroupHbm create(GroupHbm groupHbm) {
        if (groupHbm.getGroupId() == null || groupHbm.getGroupId().intValue() == 0) {
            try {
                Integer id = SequenceHelper.getSequenceSession().getNextSequenceNumber("group.group_id");
                groupHbm.setGroupId(id);
            } catch (Exception e) {
                log.error("Error creating primary key: " + e.getMessage(), e);
            }
        }
        return super.create(groupHbm);
    }

    @SuppressWarnings("unchecked")
    public java.util.Collection findAll(final int transform, final java.lang.Integer siteId) {
        return this.findAll(transform, "from de.juwimm.cms.authorization.model.GroupHbm as g where g.site.siteId = ?", siteId);
    }

    @SuppressWarnings("unchecked")
    public java.util.Collection findByUserAndSite(final int transform, final java.lang.String userId, final java.lang.Integer siteId) {
        return this.findByUserAndSite(transform, "select g from de.juwimm.cms.authorization.model.GroupHbm g inner join g.users u where u.userId = ? and g.site.siteId = ?", userId, siteId);
    }

    @SuppressWarnings("unchecked")
    public java.util.Collection findByRoleAndSite(final int transform, final java.lang.String roleId, final java.lang.Integer siteId) {
        return this.findByRoleAndSite(transform, "SELECT g FROM de.juwimm.cms.authorization.model.GroupHbm g inner join g.roles r where r.roleId = ? and g.site.siteId = ?", roleId, siteId);
    }
}
