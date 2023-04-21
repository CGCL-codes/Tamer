package com.wgo.bpot.wiring;

import java.util.ArrayList;
import java.util.Collection;
import com.wgo.bpot.common.transport.servicefacade.BpotServices;
import com.wgo.bpot.common.transport.servicefacade.RemoteServices;
import com.wgo.bpot.domain.common.Service;
import com.wgo.bpot.domain.common.ServicePrivilege;
import com.wgo.bpot.domain.common.UserRole;
import com.wgo.bpot.domain.common.UserSession;
import com.wgo.bpot.domain.common.impl.UserRoleImpl;
import com.wgo.bpot.server.configuration.UserRoleRegistry;
import com.wgo.bpot.server.persist.PersistService;
import com.wgo.bpot.server.persist.trigger.TriggerRegistry;
import com.wgo.bpot.server.service.BpotServicesImpl;
import com.wgo.bpot.wiring.common.DomainModelConvention;

/**
 * @version $Id: ServiceAccessorImpl.java 540 2008-01-09 19:42:31Z petter.eide $
 * @param <T> The root concept type.
 * @param <PS> The implementation of {@link PersistService} extending classes implements. 
 */
public class ServiceAccessorImpl<T, PS extends PersistService<T>> implements ServiceAccessor<T, PS> {

    private PS persistService;

    private TriggerRegistry triggerRegistry;

    private DomainModelConvention<T> domainModelConvention;

    private InitializationService initializationService;

    private RemoteServices remoteServices;

    private ServicesFactory servicesFactory = new DefaultServicesFactory();

    private UserRoleRegistry userRoleRegistry = new DefaultUserRoleRegistry();

    public TriggerRegistry getTriggerRegistry() {
        return triggerRegistry;
    }

    public void setTriggerRegistry(TriggerRegistry triggerRegistry) {
        this.triggerRegistry = triggerRegistry;
    }

    public PS getPersistService() {
        return persistService;
    }

    public void setPersistService(PS persistService) {
        this.persistService = persistService;
    }

    public ServicesFactory getServicesFactory() {
        return servicesFactory;
    }

    public void setServicesFactory(ServicesFactory servicesFactory) {
        this.servicesFactory = servicesFactory;
    }

    public DomainModelConvention<T> getDomainModelConvention() {
        return domainModelConvention;
    }

    public void setDomainModelConvention(DomainModelConvention<T> domainModelConvention) {
        this.domainModelConvention = domainModelConvention;
    }

    public InitializationService getInitializationService() {
        return initializationService;
    }

    public void setInitializationService(InitializationService initializationService) {
        this.initializationService = initializationService;
    }

    public RemoteServices getRemoteServices() {
        return remoteServices;
    }

    public void setRemoteServices(RemoteServices remoteServices) {
        this.remoteServices = remoteServices;
    }

    public UserRoleRegistry getUserRoleRegistry() {
        return userRoleRegistry;
    }

    public void setUserRoleRegistry(UserRoleRegistry userRoleRegistry) {
        this.userRoleRegistry = userRoleRegistry;
    }

    private class DefaultServicesFactory implements ServicesFactory {

        public BpotServices createServices(UserSession userSession, Service service) {
            return new BpotServicesImpl();
        }

        public Class<? extends BpotServices> getServicesApi() {
            return BpotServices.class;
        }
    }

    private class DefaultUserRoleRegistry implements UserRoleRegistry {

        public Collection<UserRole> getUserRoles() {
            Collection<UserRole> result = new ArrayList<UserRole>();
            result.add(new AdminUserRole());
            return result;
        }
    }

    private class AdminUserRole extends UserRoleImpl {

        @Override
        public String getName() {
            return "admin";
        }

        @Override
        public boolean isAllowed(ServicePrivilege... servicePrivileges) {
            return true;
        }
    }
}
