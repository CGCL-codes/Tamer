package com.wgo.bpot.server.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import org.apache.log4j.Logger;
import com.wgo.bpot.common.transport.exception.InvalidUserSessionException;
import com.wgo.bpot.common.transport.exception.RemoteServiceException;
import com.wgo.bpot.common.transport.servicefacade.BpotServices;
import com.wgo.bpot.common.transport.servicefacade.RemoteServices;
import com.wgo.bpot.domain.common.Service;
import com.wgo.bpot.domain.common.User;
import com.wgo.bpot.domain.common.UserCredential;
import com.wgo.bpot.domain.common.UserRole;
import com.wgo.bpot.domain.common.UserSession;
import com.wgo.bpot.domain.common.impl.UserImpl;
import com.wgo.bpot.domain.common.impl.UserRoleImpl;
import com.wgo.bpot.domain.common.impl.UserSessionImpl;
import com.wgo.bpot.wiring.ServiceAccessor;
import com.wgo.bpot.wiring.WiredService;
import com.wgo.bpot.wiring.WiringEngine;

/**
 * @version $Id: RemoteServicesImpl.java 540 2008-01-09 19:42:31Z petter.eide $
 */
public class RemoteServicesImpl implements RemoteServices, WiredService {

    private WiringEngine<? extends ServiceAccessor> wiring;

    private ServiceAccessor serviceAccessor;

    private static final transient Logger log = Logger.getLogger(RemoteServicesImpl.class);

    @SuppressWarnings("unchecked")
    public RemoteServicesImpl() {
    }

    public void setWiring(WiringEngine<? extends ServiceAccessor> wiring) {
        this.wiring = wiring;
        this.serviceAccessor = wiring.getServiceAccessor();
    }

    public void destroy() {
    }

    public void init(WiringEngine<? extends ServiceAccessor> wiringEngine) {
        setWiring(wiringEngine);
        if (null != serviceAccessor.getInitializationService()) {
            if (serviceAccessor.getInitializationService().isInitializationNeeded()) {
                log.info("Initializing services for the first time.");
                try {
                    serviceAccessor.getInitializationService().initialize();
                } catch (RuntimeException e) {
                    log.error("Error while trying to initialize services", e);
                }
            }
        } else {
            log.error("InitializationService not available.");
        }
    }

    public ServiceAccessor getServiceAccessor() {
        return serviceAccessor;
    }

    public WiringEngine<? extends ServiceAccessor> getWiring() {
        return wiring;
    }

    public UserSession authenticateUser(UserCredential userCredential) {
        User user = findUser(userCredential);
        boolean isUserNameOk = ((null != user) && (null != user.getUserName()));
        boolean isPasswordOk = isUserNameOk && (user.getPasswordHashCode() == userCredential.getPasswordHashCode());
        log.info("User authentication result:    UserName:" + isUserNameOk + ".  Password:" + isPasswordOk + ".");
        UserSession userSession = null;
        if (isUserNameOk && isPasswordOk) {
            userSession = new UserSessionImpl(user);
        }
        log.debug("User authentication result:    UserName:" + isUserNameOk + ".  Password:" + isPasswordOk);
        return userSession;
    }

    protected User findUser(UserCredential userCredential) {
        UserImpl user = null;
        if ((null != userCredential) && (null != userCredential.getUserName())) {
            try {
                log.info("Looking for user: " + userCredential.getUserName());
                user = (UserImpl) serviceAccessor.getPersistService().find(User.class, "userName", userCredential.getUserName(), 1);
                if (null != user) {
                    log.info("Found user: " + user.getUserName() + " with userrole: " + user.getUserRole());
                }
            } catch (Exception e) {
                log.error("Error finding user " + userCredential, e);
                user = null;
                if ((null != serviceAccessor) && (null != serviceAccessor.getPersistService())) {
                    serviceAccessor.getPersistService().rollbackTransaction();
                }
            }
        }
        if (null == user && ((null != userCredential) && ("admin".equals(userCredential.getUserName())))) {
            log.info("Creating default admin user");
            user = new UserImpl();
            user.setUserName("admin");
            user.setFirstName("admin");
            user.setPassword("admin");
            user.setUserRole(new UserRoleImpl("admin"));
        } else {
            setUserRole(user);
        }
        return user;
    }

    private void setUserRole(UserImpl user) {
        if (null != user) {
            boolean isOk = false;
            log.info("Checking for user role: " + user.getUserRoleName());
            for (UserRole userRole : serviceAccessor.getUserRoleRegistry().getUserRoles()) {
                if (userRole.getName().equals(user.getUserName())) {
                    log.info("...against: " + userRole.getName());
                    user.setUserRole(userRole);
                    isOk = true;
                }
            }
            if (!isOk) {
                log.error("Unable to find user role for " + user);
            }
        }
    }

    public Object invokeService(UserSession userSession, Service service, Object[] args) throws InvalidUserSessionException {
        Object result = null;
        if ((null == userSession) || userSession.isExpired()) {
            log.debug("User session is invalid!");
            throw new InvalidUserSessionException(userSession);
        }
        log.debug("User session is valid and has not expired. It will expire in " + ((userSession.getExpirationTime().getTime() - userSession.getCreateTime().getTime()) / 1000 / 60) + " minutes.");
        try {
            log.debug("Attempting to invoke service " + service.getName());
            result = invokeServiceMethod(userSession, service, args);
            log.debug(service.getName() + " invoked successfully!");
        } catch (RuntimeException e) {
            throw e;
        } catch (InvocationTargetException e) {
            Throwable t = e.getTargetException() == null ? e : e.getTargetException();
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            }
            throw new RemoteServiceException(t);
        } catch (Exception e) {
            throw new RemoteServiceException(e);
        }
        return result;
    }

    @Deprecated
    private void handleFailure(String name, Throwable e) {
        log.error(name + " invokation failed! Rollback will execute now.", e);
        if ((null != serviceAccessor) && (null != serviceAccessor.getPersistService()) && serviceAccessor.getPersistService().isTransactionActive()) {
            serviceAccessor.getPersistService().rollbackTransaction();
        }
        if (e instanceof RuntimeException) {
            throw (RuntimeException) e;
        }
        throw new RemoteServiceException(e);
    }

    @Deprecated
    private void commitTransaction() {
        if ((null != serviceAccessor) && (null != serviceAccessor.getPersistService())) {
            if (serviceAccessor.getPersistService().isTransactionActive()) {
                serviceAccessor.getPersistService().commitTransaction();
            } else {
                log.warn("I was asked to commit a transaction that was not active. This should normally not happen!");
            }
        }
    }

    @Deprecated
    private void startTransaction() {
        if ((null != serviceAccessor) && (null != serviceAccessor.getPersistService())) {
            if (serviceAccessor.getPersistService().isTransactionActive()) {
                log.warn("A previously started transaction was not committed. This should normally not happen!");
                commitTransaction();
            }
            serviceAccessor.getPersistService().startTransaction();
        }
    }

    private Object invokeServiceMethod(UserSession userSession, Service service, Object[] args) throws Exception {
        for (Method method : serviceAccessor.getServicesFactory().getServicesApi().getMethods()) {
            if (method.getName().equals(service.getName())) {
                if ((null == args && (method.getParameterTypes().length == 0)) || ((method.getParameterTypes().length == args.length))) {
                    BpotServices services = null;
                    if ((null != serviceAccessor) && null != serviceAccessor.getServicesFactory()) {
                        services = serviceAccessor.getServicesFactory().createServices(userSession, service);
                    } else {
                        services = createBpotServices(userSession);
                    }
                    Object result = method.invoke(services, args);
                    return result;
                }
            }
        }
        throw new RemoteServiceException("The requested service does not exist. Service name: " + service.getName());
    }

    private BpotServices createBpotServices(UserSession userSession) {
        BpotServicesImpl s = new BpotServicesImpl();
        s.setUserSession(userSession);
        s.setServiceAccessor(serviceAccessor);
        ModifyingOperationsState modifyingOperationsState = new ModifyingOperationsState(new Date(), 1000, serviceAccessor.getPersistService().getDomainModelConvention());
        s.setModifyingOperationsState(modifyingOperationsState);
        return s;
    }
}
