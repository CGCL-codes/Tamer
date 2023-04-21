package net.jsrb.runtime.impl.service;

import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import javax.management.*;
import net.jsrb.*;
import net.jsrb.management.ServerRuntimeMBean;
import net.jsrb.metadata.ServiceMetadata;
import net.jsrb.runtime.*;
import net.jsrb.runtime.gateway.*;
import net.jsrb.runtime.impl.server.ManagedServer;
import net.jsrb.runtime.server.*;
import net.jsrb.runtime.server.Server.TYPE;
import net.jsrb.runtime.service.*;
import net.jsrb.util.StringUtil;
import net.jsrb.util.log4j.Logger;

/**
 * Impl of ServiceAccess <br>
 * It maintains a queue for each server process, and dispatch service call
 * equally. <br>
 * The algorithm of service call dispatch is based on the round robin.
 *  
 */
public class ServiceAccessImpl implements ServiceAccess, ServerListener, GatewayListener, GatewayDataListener, Dumpable {

    private static final Logger TRC_LOGGER = Logger.getLogger(ServiceAccessImpl.class);

    /**
     * Service Name to Service List Map. <br>
     * key--> Service Name <br>
     * value List of ServerQueue that has the Service
     */
    ConcurrentHashMap<String, ServiceList> serviceMap = new ConcurrentHashMap<String, ServiceList>();

    ConcurrentHashMap<Integer, GatewayServerQueue> gatewayServerQueueMap = new ConcurrentHashMap<Integer, GatewayServerQueue>();

    ConcurrentHashMap<Integer, ManagedServerQueue> serverQueues = new ConcurrentHashMap<Integer, ManagedServerQueue>();

    int maxservers;

    public ServiceAccessImpl() {
    }

    public void setMaxservers(int maxservers) {
        this.maxservers = maxservers;
    }

    public void init() {
    }

    public boolean serviceExists(String service) {
        ServiceList serviceList = serviceMap.get(service);
        if (serviceList != null) {
            return true;
        }
        return false;
    }

    public void submitRequest(ServiceRequest req) throws JsrbException {
        final String METHOD = "submitRequest(ServiceRequest req)";
        if (TRC_LOGGER.isDebugMinEnabled()) {
            TRC_LOGGER.debugMin(METHOD + "|entry,req=" + req);
        }
        if (req == null) {
            throw new IllegalArgumentException("request must not be null");
        }
        ServerQueue queue = null;
        String service = req.getService();
        ServiceList serviceList = serviceMap.get(service);
        if (serviceList != null) {
            queue = serviceList.lookupServer(req.getExcludeServers());
        }
        if (queue == null) {
            throw new JsrbException(JsrbCodes.CODE_SVC_NOIMPL);
        }
        queue.enqueue(req);
        if (TRC_LOGGER.isDebugMinEnabled()) {
            TRC_LOGGER.debugMin(METHOD + "|exit");
        }
    }

    public void submitRequest(ServiceRequest req, int sid) throws JsrbException {
        final String METHOD = "submitRequest(ServiceRequest req,int sid)";
        if (TRC_LOGGER.isDebugMinEnabled()) {
            TRC_LOGGER.debugMin(METHOD + "|entry,sid=" + sid + ",req=" + req);
        }
        ServerQueue queue = null;
        String service = req.getService();
        ServiceList serviceList = serviceMap.get(service);
        if (serviceList != null) {
            queue = serviceList.getServer(sid);
        }
        if (queue == null) {
            throw new JsrbException(JsrbCodes.CODE_SVC_NOIMPL);
        }
        queue.enqueue(req);
        if (TRC_LOGGER.isDebugMinEnabled()) {
            TRC_LOGGER.debugMin(METHOD + "|exit");
        }
    }

    public boolean cancelRequest(ServiceRequest req) {
        final String METHOD = "cancelRequest(ServiceRequest req)";
        if (TRC_LOGGER.isDebugMinEnabled()) {
            TRC_LOGGER.debugMin(METHOD + "|entry,req=" + req);
        }
        boolean result = false;
        if (req.serverQueue == null) {
            result = false;
        } else {
            ServerQueue queue = (ServerQueue) req.serverQueue;
            result = queue.cancel(req);
        }
        if (TRC_LOGGER.isDebugMinEnabled()) {
            TRC_LOGGER.debugMin(METHOD + "|exit,result=" + result);
        }
        return result;
    }

    public void visitServices(ServiceVisitor visitor) {
        final String METHOD = "visitServices(ServiceVisitor visitor) ";
        if (TRC_LOGGER.isDebugMinEnabled()) {
            TRC_LOGGER.debugMin(METHOD + "|entry,visitor=" + visitor);
        }
        for (Iterator<ManagedServerQueue> it = serverQueues.values().iterator(); it.hasNext(); ) {
            ManagedServerQueue queue = it.next();
            if (queue == null) {
                continue;
            }
            if (queue.getStatus() != ServerQueue.STATUS.WORKING) {
                continue;
            }
            visitor.visitServerQueue(queue.getSid(), queue.getServer().getMetadata().getName(), queue.getServiceNames(), queue.getServiceCallCounts(), queue.getQueueDepth());
        }
        for (Iterator<GatewayServerQueue> it = gatewayServerQueueMap.values().iterator(); it.hasNext(); ) {
            GatewayServerQueue queue = it.next();
            visitor.visitServerQueue(queue.getSid(), "@" + queue.getSession().getJsrbId(), queue.getServiceNames(), queue.getServiceCallCounts(), queue.getQueueDepth());
        }
        if (TRC_LOGGER.isDebugMinEnabled()) {
            TRC_LOGGER.debugMin(METHOD + "|exit");
        }
    }

    /**
     * Remove GatewayServerQueue from ServiceList
     */
    public void onGatewaySessionClosed(GatewaySession session) {
        final String METHOD = "onGatewaySessionClosed(GatewaySession session)";
        if (TRC_LOGGER.isDebugMinEnabled()) {
            TRC_LOGGER.debugMin(METHOD + "|entry,session=" + session);
        }
        GatewayServerQueue serverQueue = gatewayServerQueueMap.remove(session.getId());
        if (serverQueue != null) {
            serverQueue.setStatus(ServerQueue.STATUS.STOPPED);
            String[] services = serverQueue.getServiceNames();
            for (int i = 0; i < services.length; i++) {
                String service = services[i];
                ServiceList serviceList = getServiceList(service);
                serviceList.removeServer(serverQueue);
            }
            unregisterServerQueueMBean(serverQueue);
        } else {
            TRC_LOGGER.warn(METHOD + "|Sesssion has no runtime queue.");
        }
        if (TRC_LOGGER.isDebugMinEnabled()) {
            TRC_LOGGER.debugMin(METHOD + "|exit");
        }
    }

    /**
     * Register GatewaySeverQueue to ServiceList
     */
    public void onGatewaySessionImportService(GatewaySession session, String[] remoteServices) {
        final String METHOD = "onGatewaySessionImportService(GatewaySession session, String[] remoteServices)";
        if (TRC_LOGGER.isDebugMinEnabled()) {
            TRC_LOGGER.debugMin(METHOD + "|entry,session=" + session + ",remoteServices=" + Arrays.asList(remoteServices));
        }
        if (remoteServices != null && remoteServices.length > 0) {
            GatewayServerQueue serverQueue = new GatewayServerQueue(session, remoteServices);
            gatewayServerQueueMap.put(session.getId(), serverQueue);
            serverQueue.setStatus(ServerQueue.STATUS.WORKING);
            for (int i = 0; i < remoteServices.length; i++) {
                ServiceList serviceList = getServiceList(remoteServices[i]);
                serviceList.appendServer(serverQueue);
            }
            registerServerQueueMBean(serverQueue);
        }
        if (TRC_LOGGER.isDebugMinEnabled()) {
            TRC_LOGGER.debugMin(METHOD + "|exit");
        }
    }

    public void onGatewaySessionReady(GatewaySession session) {
        final String METHOD = "onGatewaySessionReady(GatewaySession session)";
        if (TRC_LOGGER.isDebugMinEnabled()) {
            TRC_LOGGER.debugMin(METHOD + "|entry,session=" + session);
        }
        if (TRC_LOGGER.isDebugMinEnabled()) {
            TRC_LOGGER.debugMin(METHOD + "|exit");
        }
    }

    public Object onGatewayRequestData(GatewaySession session, int opType, Object data) {
        final String METHOD = "onGatewayRequestData(GatewaySession session, int opType, Object data)";
        if (TRC_LOGGER.isDebugMinEnabled()) {
            TRC_LOGGER.debugMin(METHOD + "|entry,session=" + session + ",opType=0X" + Integer.toHexString(opType) + ".data=" + data);
        }
        Object result = null;
        GatewayServerQueue queue = gatewayServerQueueMap.get(session.getId());
        if (queue != null) {
            result = queue.onGatewayRequestData(session, opType, data);
        } else {
            TRC_LOGGER.warn(METHOD + "| Get unmatched gateway request data " + data + " for session " + session);
        }
        if (TRC_LOGGER.isDebugMinEnabled()) {
            TRC_LOGGER.debugMin(METHOD + "|exit,result=" + result);
        }
        return result;
    }

    /**
     * Change ServerQueue status to WORKING.
     * <BR>Append running server's all services to ServiceList map
     */
    public void onServerRunning(Server server) {
        final String METHOD = "onServerRunning(Server svr)";
        if (TRC_LOGGER.isDebugMinEnabled()) {
            TRC_LOGGER.debugMin(METHOD + "|entry,svr=" + server);
        }
        int sid = server.getSid();
        ManagedServerQueue serverQueue = serverQueues.get(sid);
        if (serverQueue == null) {
            serverQueue = new ManagedServerQueue((ManagedServer) server);
            serverQueues.put(sid, serverQueue);
        } else {
            serverQueue.updateServer(server);
        }
        ServerQueue.STATUS lastStatus = serverQueue.getStatus();
        serverQueue.setStatus(ServerQueue.STATUS.WORKING);
        if (!serverQueue.queue.isEmpty()) {
            serverQueue.sendQueueRequest();
        }
        for (ServiceMetadata svcmeta : server.getMetadata().services()) {
            ServiceList svclist = getServiceList(svcmeta.name());
            svclist.appendServer(serverQueue);
            if (TRC_LOGGER.isDebugMinEnabled()) {
                TRC_LOGGER.debugMin(METHOD + "|append service " + svcmeta.name() + " for server sid=" + server.getSid());
            }
        }
        if (lastStatus == ServerQueue.STATUS.STOPPED) {
            registerServerQueueMBean(serverQueue);
        }
        if (TRC_LOGGER.isDebugMinEnabled()) {
            TRC_LOGGER.debugMin(METHOD + "|exit");
        }
    }

    /**
     * <BR>For un-restartable server: remove all services from servicemap, unregister serverqueue mbean
     * <BR>For restartable server: suspend the service.
     */
    public void onServerStopped(Server server) {
        final String METHOD = "onServerStopped(Server svr)";
        if (TRC_LOGGER.isDebugMinEnabled()) {
            TRC_LOGGER.debugMin(METHOD + "|entry,svr=" + server);
        }
        int sid = server.getSid();
        ServerQueue serverQueue = serverQueues.get(sid);
        if (serverQueue == null) {
            TRC_LOGGER.warn(METHOD + "|server sid=" + sid + " stopped , but its not registered in ServiceAccess");
        } else {
            serverQueue.setStatus(ServerQueue.STATUS.SUSPEND);
            for (ServiceMetadata svcmeta : server.getMetadata().services()) {
                ServiceList svclist = getServiceList(svcmeta.name());
                svclist.removeServer(serverQueue);
                if (TRC_LOGGER.isDebugMinEnabled()) {
                    TRC_LOGGER.debugMin(METHOD + "|remove service " + svcmeta.name() + " for server sid=" + server.getSid());
                }
            }
            if (!server.isRestartable()) {
                serverQueue.setStatus(ServerQueue.STATUS.STOPPED);
                serverQueue.failAllRequests(JsrbCodes.CODE_SVR_SVRSTOPPED);
                unregisterServerQueueMBean(serverQueue);
            } else {
                serverQueue.setStatus(ServerQueue.STATUS.SUSPEND);
                serverQueue.failPendingRequest(JsrbCodes.CODE_SVR_SVRSTOPPED);
            }
        }
        if (TRC_LOGGER.isDebugMinEnabled()) {
            TRC_LOGGER.debugMin(METHOD + "|exit");
        }
    }

    public void onServerRestartFailed(Server server) {
        final String METHOD = "onServerRestartFailed(Server server)";
        if (TRC_LOGGER.isDebugMinEnabled()) {
            TRC_LOGGER.debugMin(METHOD + "|entry,svr=" + server);
        }
        int sid = server.getSid();
        ServerQueue serverQueue = serverQueues.get(sid);
        if (serverQueue == null) {
            TRC_LOGGER.warn(METHOD + "|server sid=" + sid + " restart failed , but its not registered in ServiceAccess");
        } else {
            serverQueue.setStatus(ServerQueue.STATUS.STOPPED);
            serverQueue.failAllRequests(JsrbCodes.CODE_SVR_SVRSTOPPED);
            unregisterServerQueueMBean(serverQueue);
        }
        if (TRC_LOGGER.isDebugMinEnabled()) {
            TRC_LOGGER.debugMin(METHOD + "|exit");
        }
    }

    private static final String TEMPLATE_MANAGEDSERVERQUEUE = "net.jsrb:server=@{jsrb.id},category=ServerQueue,type=ManagedServerQueue,sid=@{sid}";

    private static final String TEMPLATE_GATEWAYSERVERQUEUE = "net.jsrb:server=@{jsrb.id},category=ServerQueue,type=GatewayServerQueue,sid=@{sid}";

    private String getServerQueueObjectName(ServerQueue serverQueue) {
        final String METHOD = "getServerQueueObjectName(ServerQueue serverQueue)";
        if (TRC_LOGGER.isDebugMidEnabled()) {
            TRC_LOGGER.debugMid(METHOD + "|entry,serverQueue=" + serverQueue);
        }
        TYPE type = serverQueue.getServerType();
        String templateString = null;
        if (type == Server.TYPE.MANAGED) {
            templateString = TEMPLATE_MANAGEDSERVERQUEUE;
        } else if (type == Server.TYPE.REMOTE) {
            templateString = TEMPLATE_GATEWAYSERVERQUEUE;
        }
        Properties props = new Properties();
        props.put("jsrb.id", GlobalVars.getInstance().getAppMeta().getId());
        props.put("sid", "" + serverQueue.getSid());
        String result = StringUtil.substituteString(templateString, props);
        if (TRC_LOGGER.isDebugMidEnabled()) {
            TRC_LOGGER.debugMin(METHOD + "|exit,result=" + result);
        }
        return result;
    }

    private void registerServerQueueMBean(ServerQueue serverQueue) {
        final String METHOD = "registerServerQueueMBean(ServerQueue serverQueue)";
        if (TRC_LOGGER.isDebugMidEnabled()) {
            TRC_LOGGER.debugMid(METHOD + "|entry,serverQueue=" + serverQueue);
        }
        MBeanServer mbeanServer = GlobalVars.getInstance().getMBeanServer();
        ObjectName name;
        try {
            String nameStr = getServerQueueObjectName(serverQueue);
            name = ObjectName.getInstance(nameStr);
            mbeanServer.registerMBean(serverQueue, name);
        } catch (Exception e) {
            TRC_LOGGER.error(METHOD + "|register ServerRuntimeMBean failed.", e);
        }
        if (TRC_LOGGER.isDebugMidEnabled()) {
            TRC_LOGGER.debugMin(METHOD + "|exit");
        }
    }

    private void unregisterServerQueueMBean(ServerQueue serverQueue) {
        final String METHOD = "unregisterServerQueueMBean(ServerQueue serverQueue)";
        if (TRC_LOGGER.isDebugMidEnabled()) {
            TRC_LOGGER.debugMid(METHOD + "|entry,serverQueue=" + serverQueue);
        }
        MBeanServer mbeanServer = GlobalVars.getInstance().getMBeanServer();
        ObjectName name;
        try {
            String nameStr = getServerQueueObjectName(serverQueue);
            name = ObjectName.getInstance(nameStr);
            mbeanServer.unregisterMBean(name);
        } catch (Exception e) {
            TRC_LOGGER.error(METHOD + "|unregister ServerRuntimeMBean failed.", e);
        }
        if (TRC_LOGGER.isDebugMidEnabled()) {
            TRC_LOGGER.debugMin(METHOD + "|exit");
        }
    }

    private ServiceList getServiceList(String svcname) {
        ServiceList svclist = serviceMap.get(svcname);
        if (svclist == null) {
            svclist = new ServiceList(svcname);
            serviceMap.put(svcname, svclist);
        }
        return svclist;
    }

    public void dump(PrintWriter dumpWriter, Level dumpLevel) {
        dumpWriter.println("====================== Server Queues Dump Begin ======================");
        if (dumpLevel == Level.Summary) {
            dumpSummary(dumpWriter);
        } else if (dumpLevel == Level.Detail) {
            dumpDetail(dumpWriter);
        }
        dumpWriter.println("====================== Server Queues Dump End ========================");
    }

    private void dumpSummary(PrintWriter dumpWriter) {
        Set<String> nameSet = new TreeSet<String>(serviceMap.keySet());
        for (Iterator<String> it = nameSet.iterator(); it.hasNext(); ) {
            String serviceName = it.next();
            dumpWriter.println(serviceName + " counts: " + serviceMap.get(serviceName).getServerSize());
        }
    }

    private void dumpDetail(PrintWriter dumpWriter) {
        Map<Integer, ServerQueue> serverQueueMap = new HashMap<Integer, ServerQueue>();
        for (Iterator<ServiceList> it = serviceMap.values().iterator(); it.hasNext(); ) {
            ServerQueue[] servers = it.next().getAllServers();
            if (servers != null && servers.length > 0) {
                for (int i = 0; i < servers.length; i++) {
                    ServerQueue serverQueue = servers[i];
                    serverQueueMap.put(serverQueue.getSid(), serverQueue);
                }
            }
        }
        Set<Integer> nameSet = new TreeSet<Integer>(serverQueueMap.keySet());
        for (Iterator<Integer> it = nameSet.iterator(); it.hasNext(); ) {
            Integer serviceId = it.next();
            ServerQueue serverQueue = serverQueueMap.get(serviceId);
            StringBuilder builder = new StringBuilder(512);
            builder.append("sid=" + serverQueue.getSid());
            builder.append(", status=" + serverQueue.getStatus());
            builder.append(", type=" + serverQueue.getServerType());
            builder.append(", qdepth=" + serverQueue.getQueueDepth());
            builder.append(", svcnames=" + Arrays.asList(serverQueue.getServiceNames()));
            if (serverQueue instanceof ServerRuntimeMBean) {
                ServerRuntimeMBean runtimeMBean = (ServerRuntimeMBean) serverQueue;
                int[] counts = runtimeMBean.getServiceCallCounts();
                Integer[] c = new Integer[counts.length];
                for (int i = 0; i < c.length; i++) {
                    c[i] = counts[i];
                }
                builder.append(", svccounts=" + Arrays.asList(c));
                builder.append(", readytime=" + runtimeMBean.getStartedTime());
            }
            dumpWriter.println(builder.toString());
        }
    }
}
