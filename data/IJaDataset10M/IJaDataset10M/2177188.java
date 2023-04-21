package org.wsmoss.core;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.collections.map.StaticBucketMap;
import org.wsmoss.core.monitor.WSMStatusMonitor;
import org.wsmoss.core.storage.StorageService;
import org.wsmoss.core.timeout.TimeoutManager;
import org.wsmoss.util.BusinessEventLogger;
import org.wsmoss.util.Configuration;
import org.wsmoss.web.ManageSessionRequest;
import org.wsmoss.web.ManageSessionRequestWrapper;

/**
 * The Class WorkSessionManagerBean.
 */
public class WorkSessionManagerBean implements WorkSessionManager {

    /** The business event logger. */
    private BusinessEventLogger businessEventLogger = new BusinessEventLogger();

    /** The configuration. */
    private Configuration configuration;

    /**
	 * Sets the configuration.
	 * 
	 * @param configuration the new configuration
	 */
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    /** The storage service. */
    private StorageService storageService;

    /**
	 * Sets the storage service.
	 * 
	 * @param storageService the new storage service
	 */
    public void setStorageService(StorageService storageService) {
        this.storageService = storageService;
    }

    /** The timeout manager. */
    private TimeoutManager timeoutManager;

    /**
	 * Sets the timeout manager.
	 * 
	 * @param timeoutManager the new timeout manager
	 */
    public void setTimeoutManager(TimeoutManager timeoutManager) {
        this.timeoutManager = timeoutManager;
    }

    /** The manage session request. */
    private ManageSessionRequest manageSessionRequest;

    /**
	 * Sets the manage session request.
	 * 
	 * @param manageSessionRequest the new manage session request
	 */
    public void setManageSessionRequest(ManageSessionRequest manageSessionRequest) {
        this.manageSessionRequest = manageSessionRequest;
    }

    /** The work session clients. */
    @SuppressWarnings("unchecked")
    private Map<String, WorkSessionClient> workSessionClients = new StaticBucketMap();

    /**
	 * Sets the work session clients map buckets.
	 * 
	 * @param numBuckets the new work session clients map buckets
	 */
    @SuppressWarnings("unchecked")
    public void setWorkSessionClientsMapBuckets(int numBuckets) {
        workSessionClients = new StaticBucketMap(numBuckets);
        System.out.println(" WorkSessionManager reset NUMBER of WORKSESSION_CLIENT_MapBuckets to: " + numBuckets);
    }

    /** The status monitor. */
    private WSMStatusMonitor statusMonitor;

    /**
	 * The Class Monitor.
	 */
    private class Monitor implements WSMStatusMonitor {

        public int getWorkSessionClientsCount() {
            return workSessionClients.size();
        }

        public String[] getWorkSessionClientsInfo() {
            String[] returnValue = new String[workSessionClients.size()];
            int index = 0;
            Iterator<String> it = workSessionClients.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                WorkSessionClient workSessionClient = workSessionClients.get(key);
                returnValue[index++] = "key=" + key + ",value=" + makeInfoString(workSessionClient);
            }
            return returnValue;
        }

        public String getWorkSessionClientInfo(String httpSessionId) {
            WorkSessionClient workSessionClient = getWorkSessionClient(httpSessionId);
            if (workSessionClient != null) {
                return makeInfoString(workSessionClient);
            }
            return null;
        }

        public boolean existsWorkSessionClient(String httpSessionId) {
            return (getWorkSessionClientInfo(httpSessionId) != null);
        }

        /**
		 * Make info string.
		 * 
		 * @param workSessionClient the work session client
		 * 
		 * @return the string
		 */
        private String makeInfoString(WorkSessionClient workSessionClient) {
            if (workSessionClient == null) {
                return "{null}";
            }
            StringBuffer sb = new StringBuffer("");
            sb.append("{" + workSessionClient + ":httpSession=" + workSessionClient.getHttpSession() + ",");
            if (workSessionClient.getHttpSession() != null) {
                sb.append("httpSession.id=" + workSessionClient.getHttpSession().getId() + ",");
            }
            sb.append("httpSessionIds=" + workSessionClient.getHttpSessionIds() + ",");
            sb.append("attributeNames=" + workSessionClient.getAttributeNames() + "}");
            return sb.toString();
        }
    }

    public WSMStatusMonitor getStatusMonitor() {
        return statusMonitor = (statusMonitor == null ? new Monitor() : statusMonitor);
    }

    /**
	 * Checks if is valid http session.
	 * 
	 * @param httpSession the http session
	 * 
	 * @return true, if is valid http session
	 */
    private boolean isValidHttpSession(HttpSession httpSession) {
        if (httpSession != null) {
            try {
                httpSession.isNew();
                return true;
            } catch (IllegalStateException ex) {
            }
        }
        return false;
    }

    /**
	 * Gets the client http session id.
	 * 
	 * @param request the request
	 * 
	 * @return the client http session id
	 */
    private String getClientHttpSessionId(HttpServletRequest request) {
        String returnValue = null;
        String wsmSessionCookie = null;
        String requestedSessionId = request.getRequestedSessionId();
        if (requestedSessionId != null && !requestedSessionId.equals("")) {
            returnValue = requestedSessionId;
        }
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                if (cookie.getName().equalsIgnoreCase(configuration.getWsmSessionIdCookieName())) {
                    if (cookie.getValue() != null && !"".equals(cookie.getValue())) {
                        wsmSessionCookie = cookie.getValue();
                        returnValue = cookie.getValue();
                        break;
                    }
                }
            }
        }
        businessEventLogger.traceBusinessEvent("getClientHttpSessionId", "clientHttpSession (" + returnValue + ")", "requestedSessionId (" + requestedSessionId + ") wsmSessionCookie (" + wsmSessionCookie + ")");
        return returnValue;
    }

    /** The request pack. */
    private static ThreadLocal<RequestPack> requestPack = new ThreadLocal<RequestPack>();

    public RequestPack validateSession(RequestPack pack) {
        if (configuration.isDisabled()) return pack;
        requestPack.set(pack);
        HttpServletRequest request = (HttpServletRequest) pack.getRequest();
        String clientHttpSessionId = getClientHttpSessionId(request);
        if (clientHttpSessionId == null) {
            businessEventLogger.traceBusinessEvent("clientHttpSessionIdNotFound (SKIP REQUEST)", "clientHttpSessionId (" + clientHttpSessionId + ")");
            return pack;
        }
        WorkSessionClient workSessionClient = getLazyWorkSessionClient(clientHttpSessionId);
        businessEventLogger.traceBusinessEvent("getLazyWorkSessionClient", "clientHttpSessionId (" + clientHttpSessionId + ")", "WorkSessionClient (" + workSessionClient + ") - httpSessionIds (" + workSessionClient.getHttpSessionIds() + ") workSessionStatus (" + workSessionClient.getWorkSessionStatus() + ") httpSession (" + workSessionClient.getHttpSession() + ") isValidHttpSession (" + isValidHttpSession(workSessionClient.getHttpSession()) + ")");
        businessEventLogger.traceInfo("before entering synchronized block !");
        synchronized (workSessionClient) {
            businessEventLogger.traceInfo("entering synchronized block !");
            WorkSession workSession = getWorkSession(clientHttpSessionId, workSessionClient);
            if (workSession == null) {
                businessEventLogger.traceBusinessEvent("WorkSessionNotFound (SKIP REQUEST)", "clientHttpSessionId (" + clientHttpSessionId + ")");
                return pack;
            }
            if (workSession.isExpired()) {
                businessEventLogger.traceBusinessEvent("WorkSessionExpired", "clientHttpSessionId (" + clientHttpSessionId + ")", "WorkSession (" + workSession + ") - id (" + workSession.getId() + ")", "lastUpdate (" + workSession.getLastUpdate() + ") timeout (" + workSession.getTimeout() + ") timeoutTime (" + new Date(workSession.getCalculatedTimeout()) + ")");
                storageService.destroyWorkSession(workSession.getHttpSessionId());
                deleteWorkSessionClient(workSessionClient);
                return pack;
            }
            businessEventLogger.traceBusinessEvent("WorkSessionNotExpired", "clientHttpSessionId (" + clientHttpSessionId + ")", "WorkSession (" + workSession + ") - id (" + workSession.getId() + ")", "lastUpdate (" + workSession.getLastUpdate() + ") timeout (" + workSession.getTimeout() + ") timeoutTime (" + new Date(workSession.getCalculatedTimeout()) + ")");
            ManageSessionRequest requestWrapper = manageSessionRequest == null ? new ManageSessionRequestWrapper(request) : manageSessionRequest;
            if (workSession.isUnloaded()) {
                workSession = storageService.reloadWorkSessionFromPersistent(workSession);
            }
            HttpSession currentHttpSession = workSessionClient.getHttpSession();
            if (!isValidHttpSession(currentHttpSession)) {
                currentHttpSession = request.getSession(true);
                String currentHttpSessionId = currentHttpSession.getId();
                businessEventLogger.traceBusinessEvent("HttpSessionToBeRigenerated", "clientHttpSessionId (" + clientHttpSessionId + ")", "currentHttpSession (" + currentHttpSession + ") currentHttpSessionId (" + currentHttpSessionId + ")", "WorkSession (" + workSession + ") id (" + workSession.getId() + ")");
                workSessionClient.setHttpSession(currentHttpSession);
                workSessionClients.put(currentHttpSessionId, workSessionClient);
                workSessionClient.addHttpSessionId(currentHttpSessionId);
                Date lastUpdate = storageService.setWorkSessionHttpSessionId(workSession.getHttpSessionId(), currentHttpSessionId);
                workSession.setLastUpdate(lastUpdate);
                populateHttpSession(currentHttpSession, workSession);
            } else {
                Date lastUpdate = storageService.setWorkSessionLastUpdate(clientHttpSessionId, new Date());
                workSession.setLastUpdate(lastUpdate);
                businessEventLogger.traceBusinessEvent("WorkSessionValidated", "clientHttpSessionId (" + clientHttpSessionId + ")", "WorkSession (" + workSession + ") id (" + workSession.getId() + ") new lastUpdate (" + workSession.getLastUpdate() + ")");
            }
            requestWrapper.setWorkSessionClient(workSessionClient);
            this.manageResponse(requestPack.get());
            pack = new RequestPack(requestWrapper, pack.getResponse());
            return pack;
        }
    }

    /**
	 * Manage response.
	 * 
	 * @param pack the pack
	 */
    private void manageResponse(RequestPack pack) {
        HttpServletRequest request = pack.getRequest();
        businessEventLogger.traceInfo("Manage Response - looking for http session...");
        if (request.getSession(false) != null) {
            String httpClientSessionId = request.getSession(false).getId();
            businessEventLogger.traceInfo("Manage Response - http session id: " + httpClientSessionId);
            businessEventLogger.traceInfo("Manage Response - looking for work session...");
            WorkSession workSession = storageService.getWorkSession(httpClientSessionId);
            businessEventLogger.traceInfo("Manage Response - work session (" + workSession + ")");
            if (workSession != null) {
                Cookie wsmSessionCookie = new Cookie(configuration.getWsmSessionIdCookieName(), workSession.getHttpSessionId());
                businessEventLogger.traceInfo("Manage Response - is too late? " + pack.getResponse().isCommitted());
                pack.getResponse().addCookie(wsmSessionCookie);
                businessEventLogger.traceInfo("Manage Response - added wsm cookie: name (" + wsmSessionCookie.getName() + ") domain (" + wsmSessionCookie.getDomain() + ") path (" + wsmSessionCookie.getPath() + ") value (" + wsmSessionCookie.getValue() + ")");
            }
        } else {
            businessEventLogger.traceInfo("manageResponse - looking for http session...NOT FOUND!");
        }
    }

    public void purgeExpiredWorkSessions() {
        if (configuration.isDisabled()) return;
        Set<WorkSession> expWorkSessions = storageService.getExpiredWorkSessions();
        businessEventLogger.traceBusinessEvent("PurgeExpiredWorkSessions", "found (" + expWorkSessions.size() + ") WorkSessions to purge");
        for (WorkSession workSession : expWorkSessions) {
            storageService.destroyWorkSession(workSession.getHttpSessionId());
            deleteWorkSessionClient(getWorkSessionClient(workSession.getHttpSessionId()));
        }
    }

    public void onSessionCreated(HttpSession httpSession) {
        if (configuration.isDisabled()) return;
    }

    public void onSessionDestroyed(HttpSession httpSession) {
        if (configuration.isDisabled()) return;
        if (storageService.getWorkSession(httpSession.getId()) == null) {
            WorkSessionClient workSessionClient = getWorkSessionClient(httpSession.getId());
            deleteWorkSessionClient(workSessionClient);
        }
        storageService.unloadWorkSessionInCache(httpSession.getId());
    }

    public void onAttributeAdded(HttpSession httpSession, String name, Object value) {
        if (configuration.isDisabled()) return;
        if (!configuration.isManagedAttribute(name)) return;
        String httpSessionId = httpSession.getId();
        WorkSessionClient workSessionClient = getLazyWorkSessionClient(httpSession);
        workSessionClient.onAttributeAdded(name);
        WorkSession workSession;
        boolean isWorkSessionLazyCreated = false;
        if (configuration.isLazyActivationAttribute(name)) {
            workSession = safeCreateWorkSession(httpSession);
            isWorkSessionLazyCreated = true;
        } else {
            workSession = getWorkSession(httpSessionId, workSessionClient);
            if (workSession == null) return;
        }
        if (workSession.isReadonly()) {
            return;
        }
        if (isWorkSessionLazyCreated) {
            populateWorkSession(workSession, httpSession);
        } else {
            storageService.addAttribute(httpSessionId, name, value);
        }
        timeoutManager.manageTimeout(name, TimeoutManager.AttributeOperation.added, workSession);
    }

    public void onAttributeRemoved(HttpSession httpSession, String name) {
        if (configuration.isDisabled()) return;
        if (!configuration.isManagedAttribute(name)) return;
        String httpSessionId = httpSession.getId();
        WorkSessionClient workSessionClient = getLazyWorkSessionClient(httpSession);
        workSessionClient.onAttributeRemoved(name);
        WorkSession workSession = getWorkSession(httpSessionId, workSessionClient);
        if (workSession == null || workSession.isReadonly()) {
            return;
        }
        storageService.removeAttribute(httpSessionId, name);
        timeoutManager.manageTimeout(name, TimeoutManager.AttributeOperation.removed, workSession);
    }

    public void onAttributeReplaced(HttpSession httpSession, String name, Object value) {
        if (configuration.isDisabled()) return;
        if (!configuration.isManagedAttribute(name)) return;
        String httpSessionId = httpSession.getId();
        WorkSession workSession = getWorkSession(httpSessionId, getWorkSessionClient(httpSessionId));
        if (workSession == null || workSession.isReadonly()) {
            return;
        }
        storageService.replaceAttribute(httpSessionId, name, value);
        timeoutManager.manageTimeout(name, TimeoutManager.AttributeOperation.replaced, workSession);
    }

    public void createWorkSession(HttpSession httpSession) {
        safeCreateWorkSession(httpSession);
    }

    private WorkSession safeCreateWorkSession(HttpSession httpSession) {
        String httpSessionId = httpSession.getId();
        WorkSessionClient workSessionClient = getLazyWorkSessionClient(httpSession);
        WorkSession workSession;
        synchronized (workSessionClient) {
            workSession = getWorkSession(httpSessionId, workSessionClient);
            if (workSession == null) {
                workSession = storageService.createWorkSession(httpSessionId);
                businessEventLogger.traceInfo("CreateWorkSession - setted workSessionStatus to (EXISTS) of workSessionClient (" + workSessionClient + ")");
                workSessionClient.setHttpSession(httpSession);
                workSessionClient.setWorkSessionStatus(WorkSessionClient.WorkSessionStatus.EXISTS);
                this.manageResponse(requestPack.get());
                businessEventLogger.traceBusinessEvent("CreateWorkSession - UpdatedWorkSessionClient", "HttpSession (" + httpSession + ") id (" + httpSession.getId() + ")", "WorkSessionClient (" + workSessionClient + ") - httpSessionIds (" + workSessionClient.getHttpSessionIds() + ") workSessionStatus (" + workSessionClient.getWorkSessionStatus() + ") httpSession (" + workSessionClient.getHttpSession() + ") isValidHttpSession (" + isValidHttpSession(workSessionClient.getHttpSession()) + ")");
            }
        }
        return workSession;
    }

    public void destroyWorkSession(HttpSession httpSession) {
        businessEventLogger.traceBusinessEvent("DestroyWorkSession", "WorkSessions related to JSESSIONID=" + httpSession.getId() + " is  to purge");
        String httpSessionId = httpSession.getId();
        WorkSessionClient workSessionClient = getWorkSessionClient(httpSessionId);
        WorkSession workSession = getWorkSession(httpSessionId, workSessionClient);
        storageService.destroyWorkSession(workSession.getHttpSessionId());
        deleteWorkSessionClient(getWorkSessionClient(workSession.getHttpSessionId()));
    }

    /**
	 * Populate http session.
	 * 
	 * @param httpSession the http session
	 * @param workSession the work session
	 */
    protected void populateHttpSession(HttpSession httpSession, WorkSession workSession) {
        synchronized (workSession) {
            workSession.setReadonly(true);
            businessEventLogger.traceBusinessEvent("WorkSessionSetReadonly", "HttpSession (" + httpSession + ") id (" + httpSession.getId() + ")", "WorkSession (" + workSession + ") - id (" + workSession.getId() + ") httpSessionId (" + workSession.getHttpSessionId() + ") prevHttpSessionId (" + workSession.getPrevHttpSessionId() + ") readonly (" + workSession.isReadonly() + ")");
            Map<String, Object> attributes = workSession.getAttributes();
            businessEventLogger.traceInfo("attributes (" + attributes + ")");
            for (String name : attributes.keySet()) {
                Object value = attributes.get(name);
                businessEventLogger.traceInfo("setting attribute with name (" + name + ") and value (" + value + ") in http session (" + httpSession.getId() + ") from workSession (" + workSession.getId() + ")...");
                httpSession.setAttribute(name, value);
                businessEventLogger.traceInfo("setted attribute with name (" + name + ") and value (" + value + ") in http session (" + httpSession.getId() + ") from workSession (" + workSession.getId() + ")");
                timeoutManager.manageTimeout(name, TimeoutManager.AttributeOperation.added, workSession);
            }
            workSession.setReadonly(false);
            businessEventLogger.traceBusinessEvent("WorkSessionSetReadonly", "HttpSession (" + httpSession + ") id (" + httpSession.getId() + ")", "WorkSession (" + workSession + ") - id (" + workSession.getId() + ") httpSessionId (" + workSession.getHttpSessionId() + ") prevHttpSessionId (" + workSession.getPrevHttpSessionId() + ") readonly (" + workSession.isReadonly() + ")");
            businessEventLogger.traceBusinessEvent("HttpSessionRigenerated", "HttpSession (" + httpSession + ") id (" + httpSession.getId() + ")", "WorkSession (" + workSession + ") - id (" + workSession.getId() + ") httpSessionId (" + workSession.getHttpSessionId() + ") prevHttpSessionId (" + workSession.getPrevHttpSessionId() + ") readonly (" + workSession.isReadonly() + ")");
        }
    }

    /**
	 * Populate work session.
	 * 
	 * @param workSession the work session
	 * @param httpSession the http session
	 * 
	 * @return the work session
	 */
    protected WorkSession populateWorkSession(WorkSession workSession, HttpSession httpSession) {
        String httpSessionId = httpSession.getId();
        WorkSessionClient workSessionClient = getWorkSessionClient(httpSessionId);
        synchronized (workSessionClient) {
            for (String attributeName : workSessionClient.getAttributeNames()) {
                if (configuration.isManagedAttribute(attributeName) && !(workSession.getAttributes().containsKey(attributeName)) && httpSession.getAttribute(attributeName) != null) {
                    storageService.addAttribute(httpSessionId, attributeName, httpSession.getAttribute(attributeName));
                }
            }
        }
        return workSession;
    }

    /**
	 * Delete work session client.
	 * 
	 * @param workSessionClient the work session client
	 */
    protected void deleteWorkSessionClient(WorkSessionClient workSessionClient) {
        if (workSessionClient != null) {
            for (String httpSessionId : workSessionClient.getHttpSessionIds()) {
                workSessionClients.remove(httpSessionId);
            }
            workSessionClient.delete();
        }
    }

    /**
	 * Gets the work session client.
	 * 
	 * @param httpSessionId the http session id
	 * 
	 * @return the work session client
	 */
    protected WorkSessionClient getWorkSessionClient(String httpSessionId) {
        return (WorkSessionClient) workSessionClients.get(httpSessionId);
    }

    /**
	 * Gets the lazy work session client.
	 * 
	 * @param clientHttpSessionId the client http session id
	 * 
	 * @return the lazy work session client
	 */
    protected WorkSessionClient getLazyWorkSessionClient(String clientHttpSessionId) {
        WorkSessionClient workSessionClient = getWorkSessionClient(clientHttpSessionId);
        if (workSessionClient == null) {
            businessEventLogger.traceBusinessEvent("WorkSessionClientNotFound", "clientHttpSessionId (" + clientHttpSessionId + ")");
            synchronized (workSessionClients) {
                workSessionClient = getWorkSessionClient(clientHttpSessionId);
                if (workSessionClient == null) {
                    workSessionClient = new WorkSessionClient(null);
                    workSessionClients.put(clientHttpSessionId, workSessionClient);
                    workSessionClient.addHttpSessionId(clientHttpSessionId);
                }
            }
        }
        return workSessionClient;
    }

    /**
	 * Gets the lazy work session client.
	 * 
	 * @param httpSession the http session
	 * 
	 * @return the lazy work session client
	 */
    protected WorkSessionClient getLazyWorkSessionClient(HttpSession httpSession) {
        WorkSessionClient workSessionClient;
        String httpSessionId = httpSession.getId();
        workSessionClient = getWorkSessionClient(httpSessionId);
        if (workSessionClient == null) {
            synchronized (workSessionClients) {
                workSessionClient = getWorkSessionClient(httpSessionId);
                if (workSessionClient == null) {
                    workSessionClient = new WorkSessionClient(httpSession);
                    workSessionClients.put(httpSessionId, workSessionClient);
                    workSessionClient.addHttpSessionId(httpSessionId);
                }
            }
        }
        return workSessionClient;
    }

    /**
	 * Gets the work session.
	 * 
	 * @param clientHttpSessionId the client http session id
	 * @param workSessionClient the work session client
	 * 
	 * @return the work session
	 */
    protected WorkSession getWorkSession(String clientHttpSessionId, WorkSessionClient workSessionClient) {
        WorkSession returnValue = null;
        if (workSessionClient.getWorkSessionStatus() != WorkSessionClient.WorkSessionStatus.NOT_EXISTS) {
            returnValue = storageService.getWorkSession(clientHttpSessionId);
            if (workSessionClient.getWorkSessionStatus() == WorkSessionClient.WorkSessionStatus.UNCHECKED) {
                if (returnValue == null) {
                    workSessionClient.setWorkSessionStatus(WorkSessionClient.WorkSessionStatus.NOT_EXISTS);
                } else {
                    workSessionClient.setWorkSessionStatus(WorkSessionClient.WorkSessionStatus.EXISTS);
                }
                businessEventLogger.traceInfo("setted workSessionStatus (" + workSessionClient.getWorkSessionStatus() + ") of workSessionClient (" + workSessionClient + ")");
            }
        }
        return returnValue;
    }

    /**
	 * Gets the business event logger.
	 * 
	 * @return the business event logger
	 */
    public BusinessEventLogger getBusinessEventLogger() {
        return businessEventLogger;
    }

    /**
	 * Sets the business event logger.
	 * 
	 * @param businessEventLogger the new business event logger
	 */
    public void setBusinessEventLogger(BusinessEventLogger businessEventLogger) {
        this.businessEventLogger = businessEventLogger;
    }
}
