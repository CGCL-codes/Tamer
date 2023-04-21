package net.sf.portletunit.service;

import java.util.HashMap;
import java.util.Iterator;
import javax.servlet.ServletConfig;
import org.apache.pluto.om.common.ObjectID;
import org.apache.pluto.om.entity.PortletApplicationEntity;
import org.apache.pluto.om.entity.PortletApplicationEntityList;
import org.apache.pluto.om.entity.PortletEntity;
import org.apache.pluto.om.portlet.PortletApplicationDefinition;
import org.apache.pluto.om.portlet.PortletApplicationDefinitionList;
import org.apache.pluto.portalImpl.services.portletdefinitionregistry.PortletDefinitionRegistry;
import org.apache.pluto.portalImpl.services.portletentityregistry.PortletEntityRegistryService;
import org.apache.pluto.portalImpl.util.Properties;

/**
 * 
 
 * 
 **/
public class PortletEntityRegistryServiceDynamicImpl extends PortletEntityRegistryService {

    private HashMap entityMap = new HashMap();

    private static int nextPortlet = 1;

    private PortletUnitPortletApplicationEntityListImpl appList = new PortletUnitPortletApplicationEntityListImpl();

    public void init(ServletConfig servletConfig, Properties properties) throws Exception {
        PortletApplicationDefinitionList portletAppList = PortletDefinitionRegistry.getPortletApplicationDefinitionList();
        for (Iterator iter = portletAppList.iterator(); iter.hasNext(); ) {
            PortletApplicationDefinition portletApp = (PortletApplicationDefinition) iter.next();
            PortletApplicationEntity appEntity = new PortletUnitPortletApplicationEntityImpl(this, portletApp);
            appList.add(appEntity);
        }
    }

    /**
     * Returns the portlet application instance with the given id.
     * 
     * @return the portlet application instance
     */
    public PortletApplicationEntityList getPortletApplicationEntityList() {
        return appList;
    }

    public PortletEntity getPortletEntity(ObjectID id) {
        PortletEntity ent = (PortletEntity) entityMap.get(id.toString());
        return ent;
    }

    public void store() throws java.io.IOException {
    }

    public void load() throws java.io.IOException {
    }

    public void refresh(PortletEntity portletEntity) {
        entityMap.put("1." + nextPortlet++, portletEntity);
    }
}
