package com.faceye.components.portal.dao.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import com.faceye.components.portal.dao.iface.IPortletDao;
import com.faceye.components.portal.dao.model.Portlet;
import com.faceye.components.portal.dao.model.PortletSubscribe;
import com.faceye.core.componentsupport.dao.controller.DomainDao;
import com.faceye.core.util.helper.PaginationSupport;

public class PortletDao extends DomainDao implements IPortletDao {

    public PaginationSupport getPortletsByPortalColumnId(Serializable portalColumnId) {
        List result = new ArrayList();
        String hql = "from " + PortletSubscribe.class.getName() + " p where p.portalColumn.id=:portalColumnId order by p.order asc";
        List portletSubscribes = this.getAllByHQL(hql, "portalColumnId", portalColumnId);
        if (null != portletSubscribes) {
            Iterator it = portletSubscribes.iterator();
            while (it.hasNext()) {
                PortletSubscribe item = (PortletSubscribe) it.next();
                Portlet portlet = item.getProtlet();
                result.add(portlet);
            }
        }
        return new PaginationSupport(result);
    }

    public void saveOrUpdate(Portlet portlet) {
        super.saveOrUpdateObject(portlet);
    }

    public PaginationSupport getPortlets(int startIndex) {
        String hql = "from " + Portlet.class.getName() + " p order by p.id desc";
        return this.getPageByHQL(hql, startIndex);
    }

    public PaginationSupport getAllPortletsByPortalId(Serializable portalId) {
        String hql = "from " + PortletSubscribe.class.getName() + " p where p.portalColumn.portal.id=:portalId order by p.createTime desc";
        List portletSubscribes = this.getAllByHQL(hql, "portalId", portalId);
        List result = new ArrayList();
        if (CollectionUtils.isNotEmpty(portletSubscribes)) {
            for (int i = 0; i < portletSubscribes.size(); i++) {
                PortletSubscribe item = (PortletSubscribe) portletSubscribes.get(i);
                Portlet portlet = item.getProtlet();
                result.add(portlet);
            }
        }
        return new PaginationSupport(result);
    }

    public PaginationSupport getPortletSubscribeByPortalColumnId(Serializable portalColumnId) {
        String hql = "from " + PortletSubscribe.class.getName() + " p where p.portalColumn.id=:portalColumnId order by p.order asc";
        List portletSubscribes = this.getAllByHQL(hql, "portalColumnId", portalColumnId);
        return new PaginationSupport(portletSubscribes);
    }

    public PortletSubscribe getPortletSubscribeByPortletAndPortleColumn(Serializable portalColumnId, Serializable portletId) {
        String hql = "from " + PortletSubscribe.class.getName() + " p where p.portalColumn.id=:portalColumnId and p.protlet.id=:portletId";
        List items = this.getAllByHQL(hql, new String[] { "portalColumnId", "portletId" }, new Object[] { portalColumnId, portletId });
        if (CollectionUtils.isNotEmpty(items)) {
            return (PortletSubscribe) items.get(0);
        }
        return null;
    }

    public PortletSubscribe getPortletSubscribeByPortalAndPortalColumnAndPortlet(Serializable portletId, Serializable portalColumnId, Serializable portalId) {
        String hql = "from " + PortletSubscribe.class.getName() + " p where p.portalColumn.portal.id=:portalId  and p.protlet.id=:portletId";
        List items = this.getAllByHQL(hql, new String[] { "portalId", "portletId" }, new Object[] { portalId, portletId });
        if (CollectionUtils.isNotEmpty(items)) {
            return (PortletSubscribe) items.get(0);
        }
        return null;
    }

    public int getNextPortletSubscribeOrder(Serializable portalColumnId) {
        int o = 0;
        String hql = "select max(p.order) from " + PortletSubscribe.class.getName() + " p where p.portalColumn.id=:portalColumnId";
        List items = this.getAllByHQL(hql, "portalColumnId", portalColumnId);
        if (null != items && items.size() > 0) {
            if (items.get(0) != null) {
                o = (Integer) items.get(o) + 1;
            }
        }
        return o;
    }

    public List getAllPortletSubscribeByUserId(Serializable userId, Serializable portalId) {
        String hql = "from " + PortletSubscribe.class.getName() + " p where p.portalColumn.portal.portalContainer.user.id=:userId and p.portalColumn.portal.id=:portalId";
        List items = this.getAllByHQL(hql, new String[] { "userId", "portalId" }, new Object[] { userId, portalId });
        return items;
    }
}
