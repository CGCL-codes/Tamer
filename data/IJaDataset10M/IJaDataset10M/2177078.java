package com.corratech.opensuite.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.security.auth.DestroyFailedException;
import javax.security.auth.Destroyable;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.acegisecurity.userdetails.UserDetails;
import org.apache.log4j.Logger;
import org.hibernate.engine.SessionFactoryImplementor;
import com.corratech.opensuite.acegi.api.OpensuiteUserDetailsService;
import com.corratech.opensuite.api.businesscomponent.BusinessComponent;
import com.corratech.opensuite.api.businesscomponent.BusinessComponentManagement;
import com.corratech.opensuite.api.businesscomponent.Endpoint;
import com.corratech.opensuite.api.businesscomponent.BusinessComponentManagement.EndpointType;
import com.corratech.opensuite.persist.hibernate.impl.GlobalSessionManager;
import com.corratech.opensuite.services.OpensuiteServiceFacade;
import com.opensuite.bind.base.UserCredentials;
import com.opensuite.bind.endpoint.EndpointProperty;
import com.opensuite.bind.services.endpointmanagement.GetEndpointPropertiesGuestRequest;
import com.opensuite.bind.services.endpointmanagement.GetEndpointPropertiesRequest;
import com.opensuite.bind.services.endpointmanagement.GetEndpointPropertiesResponse;
import com.opensuite.bind.services.endpointmanagement.SetEndpointPropertiesGuestRequest;
import com.opensuite.bind.services.endpointmanagement.SetEndpointPropertiesGuestResponse;

public class CoreComponentUtil implements Destroyable {

    private static Logger log = Logger.getLogger(CoreComponentUtil.class);

    protected static OpensuiteServiceFacade facade;

    protected static final String CORE_SERVICE_USER = "core_service";

    private static CoreComponentUtil instance;

    public static CoreComponentUtil getInstance() {
        if (instance == null) {
            instance = new CoreComponentUtil();
        }
        return instance;
    }

    private CoreComponentUtil() {
        facade = OpensuiteServiceFacade.getInstance();
        login();
    }

    public GetEndpointPropertiesResponse getEndpointPropertiesGuest(GetEndpointPropertiesGuestRequest request) {
        facade.setThreadSessionManager(new GlobalSessionManager((SessionFactoryImplementor) facade.getFactory().getBean("openSuiteSessionFactory")));
        OpensuiteUserDetailsService userDetailsService = (OpensuiteUserDetailsService) facade.getFactory().getBean("userDetailsService");
        UserDetails coreServiceUser = userDetailsService.loadUserByUsername(CORE_SERVICE_USER);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(coreServiceUser, CORE_SERVICE_USER, coreServiceUser.getAuthorities()));
        GetEndpointPropertiesRequest endpointPropertiesRequest = new GetEndpointPropertiesRequest();
        endpointPropertiesRequest.setBcName(request.getBcName());
        endpointPropertiesRequest.setUserLogin(coreServiceUser.getUsername());
        endpointPropertiesRequest.setUserPassword(coreServiceUser.getPassword());
        return getEndpointProps(endpointPropertiesRequest);
    }

    public SetEndpointPropertiesGuestResponse setEndpointPropertiesGuest(SetEndpointPropertiesGuestRequest request) {
        SetEndpointPropertiesGuestResponse response = new SetEndpointPropertiesGuestResponse();
        response.setSuccess(false);
        BusinessComponentManagement bcManagement = facade.getBCManagementService();
        BusinessComponent bc = bcManagement.getBusinessComponent(request.getBcName());
        if (bc != null) {
            Endpoint endpoint = bc.getEndpoint();
            if (endpoint == null) {
                endpoint = bcManagement.createEndpoint(request.getBcName() + "_endpoint", EndpointType.JBI.getId());
            }
            Map<String, String> properties = new HashMap<String, String>();
            for (EndpointProperty property : request.getEndpointProperties()) {
                properties.put(property.getKey(), property.getValue());
            }
            bcManagement.setEndpointProperties(endpoint.getEndpointId(), properties);
            response.setSuccess(true);
            log.info("Endpoint properties were successfully setted for business component named '" + request.getBcName() + "'");
        } else {
            log.warn("There is no business component named '" + request.getBcName() + "'");
        }
        return response;
    }

    public GetEndpointPropertiesResponse getEndpointProperties(GetEndpointPropertiesRequest request) {
        return getEndpointProps(request);
    }

    private GetEndpointPropertiesResponse getEndpointProps(GetEndpointPropertiesRequest request) {
        GetEndpointPropertiesResponse response = new GetEndpointPropertiesResponse();
        BusinessComponent bc = facade.getBCManagementService().getBusinessComponent(request.getBcName());
        if (bc != null) {
            List<com.corratech.opensuite.api.businesscomponent.EndpointProperty> endpointProperties;
            endpointProperties = facade.getBCManagementService().getEndpointProperties(bc.getEndpoint());
            for (com.corratech.opensuite.api.businesscomponent.EndpointProperty endpointProperty : endpointProperties) {
                com.corratech.opensuite.persist.businesscomponent.EndpointProperty persistProperty = (com.corratech.opensuite.persist.businesscomponent.EndpointProperty) endpointProperty;
                EndpointProperty property = new EndpointProperty();
                property.setKey(persistProperty.getProperty().getPropertyName());
                property.setValue(persistProperty.getPropertyValue());
                response.getEndpointProperties().add(property);
            }
        }
        return response;
    }

    protected boolean login() {
        facade.setThreadSessionManager(new GlobalSessionManager((SessionFactoryImplementor) facade.getFactory().getBean("openSuiteSessionFactory")));
        OpensuiteUserDetailsService userDetailsService = (OpensuiteUserDetailsService) facade.getFactory().getBean("userDetailsService");
        UserDetails coreServiceUser = userDetailsService.loadUserByUsername(CORE_SERVICE_USER);
        if (coreServiceUser != null) {
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(coreServiceUser, CORE_SERVICE_USER, coreServiceUser.getAuthorities()));
            return true;
        } else {
            return false;
        }
    }

    private UserDetails retrieveUserDetails(UserCredentials userCredentials) {
        UserDetails user = null;
        facade.setThreadSessionManager(new GlobalSessionManager((SessionFactoryImplementor) facade.getFactory().getBean("openSuiteSessionFactory")));
        OpensuiteUserDetailsService userDetailsService = (OpensuiteUserDetailsService) facade.getFactory().getBean("userDetailsService");
        UserDetails coreServiceUser = userDetailsService.loadUserByUsername(CORE_SERVICE_USER);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(coreServiceUser, CORE_SERVICE_USER, coreServiceUser.getAuthorities()));
        BusinessComponentManagement bcManagement = facade.getBCManagementService();
        BusinessComponent businessComponent = bcManagement.getBusinessComponent(userCredentials.getBCName());
        com.corratech.opensuite.api.businesscomponent.UserCredentials uc = bcManagement.getUserCredentials(businessComponent, userCredentials.getUserLogin(), userCredentials.getUserPassword());
        user = userDetailsService.loadOpensuiteUser(uc);
        return user;
    }

    protected void logout() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    public void destroy() throws DestroyFailedException {
        logout();
    }

    public boolean isDestroyed() {
        return false;
    }
}
