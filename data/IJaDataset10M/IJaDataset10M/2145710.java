package net.sf.portletunit.service;

import org.apache.pluto.om.entity.PortletEntityList;
import java.util.Iterator;
import org.apache.pluto.om.entity.PortletEntity;
import org.apache.pluto.om.common.ObjectID;
import java.util.HashMap;

/**
 * i
 
 * 
 **/
public class PortletUnitPortletEntityListImpl implements PortletEntityList {

    HashMap map = new HashMap();

    public Iterator iterator() {
        return map.values().iterator();
    }

    public PortletEntity get(ObjectID id) {
        return (PortletEntity) map.get(id);
    }

    public void add(PortletEntity entity) {
        map.put(entity.getId(), entity);
    }
}
