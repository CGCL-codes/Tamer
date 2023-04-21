package org.eaasyst.eaa.security.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import org.eaasyst.eaa.Constants;
import org.eaasyst.eaa.data.DataAccessBean;
import org.eaasyst.eaa.data.DataAccessBeanFactory;
import org.eaasyst.eaa.data.DataConnector;
import org.eaasyst.eaa.data.impl.ResourceAuthorizationDabFactory;
import org.eaasyst.eaa.security.Authorizer;
import org.eaasyst.eaa.security.User;
import org.eaasyst.eaa.security.UserProfileManager;
import org.eaasyst.eaa.syst.EaasyStreet;
import org.eaasyst.eaa.syst.data.persistent.ResourceAuthorization;
import org.eaasyst.eaa.syst.data.persistent.ResourceCollection;
import org.eaasyst.eaa.syst.data.persistent.ResourceGroupMember;

/**
 * <p>Provides authorization based on group affiliations.</p>
 *
 * @version 2.6
 * @author Jeff Chilton
 */
public class ResourceGroupAuthorizer implements Authorizer {

    /**
	 * <p>This method determines if an application user is authorized to access
	 * the specified resource.</p>
	 *
	 * @param type the type of resource identified by the <code>resource</code>
	 * parameter
	 * @param resource the name or identifier of the requested resource
	 * @param req the <code>HttpServeltRequest</code> object
	 * @return true, if the application user is authorized
	 * @since Eaasy Street 2.4
	 */
    public boolean isAuthorized(String type, String resource, HttpServletRequest req) {
        boolean authorized = true;
        Map authorizationData = getAuthorizationData(req);
        Map typeData = (Map) authorizationData.get(type);
        if (typeData != null && type != null && resource != null) {
            String resourceName = resource;
            if (Constants.RESOURCE_TYPE_APPLICATION.equalsIgnoreCase(type)) {
                if (resourceName.indexOf("&") != -1) {
                    resourceName = resourceName.substring(0, resourceName.indexOf("&"));
                }
                if (resourceName.indexOf(Constants.DOT) == -1) {
                    resourceName = EaasyStreet.getProperty(Constants.CFG_DEFAULT_PACKAGE) + Constants.DOT + resource;
                }
            }
            List protectedResources = (List) typeData.get("Protected Resources");
            if (protectedResources.contains(resourceName)) {
                authorized = checkAuthorization(resourceName, typeData);
            }
        }
        return authorized;
    }

    /**
	 * <p>This method determines if an application user is authorized to access
	 * the specified resource.</p>
	 *
	 * @param resource the name or identifier of the requested resource
	 * @param typeData a Map of the resource groups for this type of resource
	 * @return true, if the application user is authorized
	 * @since Eaasy Street 2.4
	 */
    private boolean checkAuthorization(String resource, Map typeData) {
        boolean authorized = false;
        User user = UserProfileManager.getUserProfile();
        if (user.getUserGroups() != null) {
            Iterator i = user.getUserGroups().iterator();
            while (i.hasNext() && !authorized) {
                String userGroupId = (String) i.next();
                List authorizedResources = (List) typeData.get(userGroupId);
                if (authorizedResources != null) {
                    if (authorizedResources.contains(resource)) {
                        authorized = true;
                    }
                }
            }
        }
        return authorized;
    }

    /**
	 * <p>This method returns the data used to determine authorization.</p>
	 *
	 * @param req the <code>HttpServeltRequest</code> object
	 * @return the data used to determine authorization
	 * @since Eaasy Street 2.4
	 */
    private Map getAuthorizationData(HttpServletRequest req) {
        Map authorizationData = (Map) EaasyStreet.getContextAttribute("org.eaasyst.eaa.security.impl.ResourceGroupAuthorizer.authorizationData");
        if (authorizationData == null) {
            authorizationData = loadAuthorizationData(req);
            EaasyStreet.setContextAttribute("org.eaasyst.eaa.security.impl.ResourceGroupAuthorizer.authorizationData", authorizationData);
        }
        return authorizationData;
    }

    /**
	 * <p>This method loads the data used to determine authorization.</p>
	 *
	 * @param req the <code>HttpServeltRequest</code> object
	 * @return the data used to determine authorization
	 * @since Eaasy Street 2.4
	 */
    private Map loadAuthorizationData(HttpServletRequest req) {
        Map authorizationData = new HashMap();
        Map mappedByType = new HashMap();
        List resourceAuthorizations = getResourceAuthorizations();
        Iterator i = resourceAuthorizations.iterator();
        while (i.hasNext()) {
            ResourceAuthorization ra = (ResourceAuthorization) i.next();
            String userGroupId = ra.getUserGroupId();
            if (ra.getResourceGroup().getMemberCt() > 0) {
                Iterator k = ra.getResourceGroup().getMembers().iterator();
                while (k.hasNext()) {
                    ResourceGroupMember rgm = (ResourceGroupMember) k.next();
                    if (rgm.getResourceCollection().getElementCt() > 0) {
                        processResourceCollection(userGroupId, rgm.getResourceCollection(), mappedByType);
                    }
                }
            }
        }
        i = mappedByType.keySet().iterator();
        while (i.hasNext()) {
            String type = (String) i.next();
            Map typeMap = (Map) mappedByType.get(type);
            Map newMap = new HashMap();
            authorizationData.put(type, newMap);
            Iterator j = typeMap.keySet().iterator();
            while (j.hasNext()) {
                String userGroupId = (String) j.next();
                Map mappedResources = (Map) typeMap.get(userGroupId);
                newMap.put(userGroupId, new ArrayList(mappedResources.keySet()));
            }
        }
        return authorizationData;
    }

    /**
	 * <p>This method processes a single ResourceCollection.</p>
	 *
	 * @param userGroupId the id of the current user group
	 * @param rc the ResourceCollection
	 * @param mappedByType the resource map
	 * @since Eaasy Street 2.4
	 */
    private void processResourceCollection(String userGroupId, ResourceCollection rc, Map mappedByType) {
        String type = rc.getType();
        Map typeMap = (Map) mappedByType.get(type);
        if (typeMap == null) {
            typeMap = new HashMap();
            typeMap.put("Protected Resources", new TreeMap());
            mappedByType.put(type, typeMap);
        }
        Map protectedResources = (Map) typeMap.get("Protected Resources");
        Map authorizedResources = (Map) typeMap.get(userGroupId);
        if (authorizedResources == null) {
            authorizedResources = new TreeMap();
            typeMap.put(userGroupId, authorizedResources);
        }
        Iterator i = rc.getElement().iterator();
        while (i.hasNext()) {
            String resourceName = (String) i.next();
            protectedResources.put(resourceName, resourceName);
            authorizedResources.put(resourceName, resourceName);
        }
    }

    /**
	 * <p>This method returns all resource authorizations on file.</p>
	 *
	 * @return all resource authorizations on file
	 * @since Eaasy Street 2.5.1
	 */
    private List getResourceAuthorizations() {
        List resourceAuthorizations = new ArrayList();
        DataAccessBeanFactory factory = new ResourceAuthorizationDabFactory();
        DataAccessBean dataAccessBean = factory.getAccessBean("list");
        DataConnector dc = new DataConnector(dataAccessBean);
        Map parameters = new HashMap();
        parameters.put("useFindCommand", "true");
        dc.setParameters(parameters);
        dc.setCommand(DataConnector.READ_COMMAND);
        dc.execute();
        if (dc.getResponseCode() == 0) {
            resourceAuthorizations = (List) dc.getExecutionResults();
        } else {
            if (dc.getResponseCode() != 1) {
                EaasyStreet.logError("Error accessing resource authorizations in ResourceGroupAuthorizer; response code: " + dc.getResponseCode() + ", response: " + dc.getResponseString());
            }
        }
        return resourceAuthorizations;
    }
}
