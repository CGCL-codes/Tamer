package com.faceye.components.portal.dao.controller;

import java.io.Serializable;
import java.util.List;
import com.faceye.components.portal.dao.iface.IPortalColumnDao;
import com.faceye.components.portal.dao.model.PortalColumn;
import com.faceye.components.portal.dao.model.PortalColumnTemplate;
import com.faceye.core.componentsupport.dao.controller.DomainDao;
import com.faceye.core.util.helper.PaginationSupport;

public class PortalColumnDao extends DomainDao implements IPortalColumnDao {

    public PaginationSupport getPortalColumnByPortalId(Serializable portalId) {
        String hql = "from " + PortalColumn.class.getName() + " p where p.portal.id=:portalId order by p.createTime asc";
        List items = this.getAllByHQL(hql, "portalId", portalId);
        return new PaginationSupport(items);
    }

    public void saveOrUpdate(PortalColumn portalColumn) {
        super.saveOrUpdateObject(portalColumn);
    }

    public PaginationSupport getAllPortalColumnTemplates() {
        String hql = "from " + PortalColumnTemplate.class.getName() + " p order by p.id asc";
        List items = this.getAllByHQL(hql);
        PaginationSupport ps = new PaginationSupport(items);
        return ps;
    }

    public PortalColumnTemplate getSystemDefaultPortalColumnTemplate() {
        Object o = this.getObject(PortalColumnTemplate.class, "marker", "default");
        if (null != o) {
            if (o instanceof PortalColumnTemplate) {
                return (PortalColumnTemplate) o;
            } else {
                return (PortalColumnTemplate) ((List) o).get(0);
            }
        }
        return null;
    }
}
