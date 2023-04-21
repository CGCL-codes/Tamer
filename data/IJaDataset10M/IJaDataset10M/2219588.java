package org.opennms.netmgt.protocols.xmp.collector;

import java.io.File;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.krupczak.Xmp.SocketOpts;
import org.krupczak.Xmp.Xmp;
import org.krupczak.Xmp.XmpMessage;
import org.krupczak.Xmp.XmpSession;
import org.krupczak.Xmp.XmpVar;
import org.opennms.core.utils.ParameterMap;
import org.opennms.core.utils.ThreadCategory;
import org.opennms.netmgt.collectd.CollectionAgent;
import org.opennms.netmgt.collectd.ServiceCollector;
import org.opennms.netmgt.config.collector.AttributeGroup;
import org.opennms.netmgt.config.collector.AttributeGroupType;
import org.opennms.netmgt.config.collector.CollectionSet;
import org.opennms.netmgt.config.xmpConfig.XmpConfig;
import org.opennms.netmgt.config.xmpDataCollection.Group;
import org.opennms.netmgt.config.xmpDataCollection.MibObj;
import org.opennms.netmgt.config.xmpDataCollection.XmpCollection;
import org.opennms.netmgt.model.RrdRepository;
import org.opennms.netmgt.model.events.EventProxy;
import org.opennms.netmgt.protocols.xmp.config.XmpAgentConfig;
import org.opennms.netmgt.protocols.xmp.config.XmpConfigFactory;
import org.opennms.netmgt.protocols.xmp.config.XmpPeerFactory;

public class XmpCollector implements ServiceCollector {

    static final String SERVICE_NAME = "XMP";

    int xmpPort;

    int timeout;

    int retries;

    Set<CollectionAgent> setOfNodes;

    SocketOpts sockopts;

    String authenUser;

    /**
     * <p>Constructor for XmpCollector.</p>
     */
    public XmpCollector() {
        log().debug("XmpCollector created");
        setOfNodes = new HashSet<CollectionAgent>();
        xmpPort = Xmp.XMP_PORT;
        sockopts = new SocketOpts();
        authenUser = new String("xmpUser");
        timeout = 3000;
        return;
    }

    private ThreadCategory log() {
        return ThreadCategory.getInstance(getClass());
    }

    private boolean handleScalarQuery(String groupName, XmpCollectionSet collectionSet, long oldUptime, XmpSession session, XmpCollectionResource scalarResource, XmpVar[] queryVars) {
        XmpMessage reply;
        AttributeGroupType agt;
        AttributeGroup ag;
        long newUptime;
        int i;
        XmpVar[] vars;
        XmpCollectionAttribute aVar;
        XmpCollectionAttributeType attribType;
        reply = session.queryVars(queryVars);
        if (reply == null) {
            log().warn("collect: query to " + collectionSet.getCollectionAgent() + " failed, " + Xmp.errorStatusToString(session.getErrorStatus()));
            return false;
        }
        agt = new AttributeGroupType(groupName, "ignore");
        ag = new AttributeGroup(scalarResource, agt);
        vars = reply.getMIBVars();
        newUptime = 0;
        for (i = 0; i < vars.length; i++) {
            if (vars[i].getMibName().equals("core") && vars[i].getObjName().equals("sysUpTime")) {
                newUptime = vars[i].getValueLong();
            }
            attribType = new XmpCollectionAttributeType(vars[i], agt);
            aVar = new XmpCollectionAttribute(scalarResource, attribType, vars[i].getObjName(), vars[i]);
            ag.addAttribute(aVar);
        }
        if (newUptime > oldUptime) {
            collectionSet.ignorePersistFalse();
        }
        if (newUptime > 0) {
            collectionSet.getCollectionAgent().setSavedSysUpTime(newUptime);
        }
        scalarResource.addAttributeGroup(ag);
        return true;
    }

    private boolean handleTableQuery(String groupName, String resourceType, XmpCollectionSet collectionSet, String[] tableInfo, XmpSession session, XmpVar[] queryVars) {
        int numColumns, numRows;
        XmpMessage reply;
        int i, j;
        XmpVar[] vars;
        String targetInstance;
        numColumns = queryVars.length;
        targetInstance = tableInfo[2];
        if ((tableInfo[2] == null) || (tableInfo[2].length() == 0)) {
            tableInfo[2] = new String("*");
            targetInstance = null;
        }
        log().debug("sending table query " + tableInfo[0] + "," + tableInfo[1] + "," + tableInfo[2] + " target: " + targetInstance);
        reply = session.queryTableVars(tableInfo, 0, queryVars);
        if (reply == null) {
            log().warn("collect: query to " + collectionSet.getCollectionAgent() + " failed, " + Xmp.errorStatusToString(session.getErrorStatus()));
            return false;
        }
        vars = reply.getMIBVars();
        numRows = vars.length / numColumns;
        log().info("query returned valid table data for " + groupName + " numRows=" + numRows + " numColumns=" + numColumns);
        for (i = 0; i < numRows; i++) {
            XmpCollectionResource rowResource;
            AttributeGroup ag;
            AttributeGroupType agt;
            String rowInstance;
            rowInstance = vars[i * numColumns].getKey();
            if (targetInstance != null) rowResource = new XmpCollectionResource(collectionSet.getCollectionAgent(), resourceType, tableInfo[1], targetInstance); else rowResource = new XmpCollectionResource(collectionSet.getCollectionAgent(), resourceType, tableInfo[1], rowInstance);
            agt = new AttributeGroupType(groupName, "all");
            ag = new AttributeGroup(rowResource, agt);
            log().debug("queryTable instance=" + rowInstance);
            for (j = 0; j < numColumns; j++) {
                XmpCollectionAttributeType attribType = new XmpCollectionAttributeType(vars[i * numColumns + j], agt);
                XmpCollectionAttribute aVar = new XmpCollectionAttribute(rowResource, attribType, vars[i * numColumns + j].getObjName(), vars[i * numColumns + j]);
                ag.addAttribute(aVar);
            }
            rowResource.addAttributeGroup(ag);
            collectionSet.addResource(rowResource);
            log().info("query table data adding row resource " + rowResource);
        }
        return true;
    }

    /**
     * {@inheritDoc}
     *
     * initialize our XmpCollector with global parameters *
     */
    public void initialize(Map<String, String> parameters) {
        log().debug("initialize(params) called");
        try {
            XmpCollectionFactory.init();
        } catch (Throwable e) {
            log().error("initialize: XmpCollectionFactory failed to initialize");
            throw new UndeclaredThrowableException(e);
        }
        try {
            XmpPeerFactory.init();
        } catch (Throwable e) {
            log().error("initialize: XmpPeerFactory failed to initialize");
            throw new UndeclaredThrowableException(e);
        }
        try {
            XmpConfigFactory.init();
        } catch (Throwable e) {
            log().error("initialize: config factory failed to initialize");
            throw new UndeclaredThrowableException(e);
        }
        File f = new File(XmpCollectionFactory.getInstance().getRrdPath());
        if (!f.isDirectory()) {
            if (!f.mkdirs()) {
                throw new RuntimeException("Unable to create RRD file " + "repository.  Path doesn't already exist and could not make directory: " + XmpCollectionFactory.getInstance().getRrdPath());
            }
        }
        XmpConfig protoConfig = XmpConfigFactory.getInstance().getXmpConfig();
        if (protoConfig.hasPort()) xmpPort = protoConfig.getPort();
        if (protoConfig.hasTimeout()) timeout = protoConfig.getTimeout();
        if (protoConfig.getAuthenUser() != null) authenUser = protoConfig.getAuthenUser();
        log().debug("initialize: authenUser '" + authenUser + "' port " + xmpPort);
        log().debug("initialize: keystore found? " + sockopts.getKeystoreFound());
        return;
    }

    /**
     * {@inheritDoc}
     *
     * initialize the querying of a particular agent/interface with
     * parameters specific to this agent/interface *
     */
    public void initialize(CollectionAgent agent, Map<String, Object> parameters) {
        log().debug("initialize agent/params called for " + agent);
        setOfNodes.add(agent);
        return;
    }

    /**
     * Release/stop all querying of agents/interfaces and release
     *       state associated with them *
     */
    public void release() {
        log().info("release()");
        setOfNodes = new HashSet<CollectionAgent>();
        return;
    }

    /**
     * {@inheritDoc}
     *
     * Release/stop querying a particular agent *
     */
    public void release(CollectionAgent agent) {
        log().info("release agent called for " + agent);
        setOfNodes.remove(agent);
        return;
    }

    /**
     * who am I and what am I ? *
     *
     * @return a {@link java.lang.String} object.
     */
    public String serviceName() {
        return SERVICE_NAME;
    }

    /**
     * {@inheritDoc}
     *
     * Collect data, via XMP, from a particular agent EventProxy is
     *       used to send opennms events into the system in case a
     *       collection fails or if a system is back working again after a
     *       failure (suceed event).  But otherwise, no events sent if
     *       collection succeeds.  Collect is called once per agent per
     *       collection cycle.  Parameters are a map of String Key/String
     *       Value passed in.  Keys come from collectd config
     */
    public CollectionSet collect(CollectionAgent agent, EventProxy eproxy, Map<String, Object> parameters) {
        XmpCollectionSet collectionSet;
        XmpSession session;
        long oldUptime;
        int i;
        XmpCollection collection;
        XmpCollectionResource scalarResource;
        log().debug("collect agent " + agent);
        oldUptime = 0;
        XmpAgentConfig peerConfig = XmpPeerFactory.getInstance().getAgentConfig(agent.getInetAddress());
        authenUser = peerConfig.getAuthenUser();
        timeout = (int) peerConfig.getTimeout();
        retries = peerConfig.getRetry();
        xmpPort = peerConfig.getPort();
        if (parameters.get("authenUser") != null) authenUser = ParameterMap.getKeyedString(parameters, "authenUser", null);
        if (parameters.get("timeout") != null) {
            timeout = ParameterMap.getKeyedInteger(parameters, "timeout", 3000);
        }
        if (parameters.get("retry") != null) {
            retries = ParameterMap.getKeyedInteger(parameters, "retries", 0);
        }
        parameters.get("collection");
        if (parameters.get("port") != null) {
            xmpPort = Integer.valueOf((String) parameters.get("port"));
        }
        String collectionName = ParameterMap.getKeyedString(parameters, "collection", null);
        if (collectionName == null) {
            log().warn("collect found no collectionName for " + agent);
            return null;
        }
        log().debug("XmpCollector: collect " + collectionName + " from " + agent);
        collectionSet = new XmpCollectionSet(agent);
        collectionSet.setCollectionTimestamp(new Date());
        collectionSet.setStatusFailed();
        collectionSet.ignorePersistTrue();
        scalarResource = new XmpCollectionResource(agent, null, "node", null);
        collectionSet.addResource(scalarResource);
        collection = XmpCollectionFactory.getInstance().getXmpCollection(collectionName);
        if (collection == null) {
            log().warn("collect found no matching collection for " + agent);
            return collectionSet;
        }
        oldUptime = agent.getSavedSysUpTime();
        log().debug("collect: attempting to open XMP session with " + agent.getInetAddress() + ":" + xmpPort + "," + authenUser);
        sockopts.setConnectTimeout(timeout);
        session = new XmpSession(sockopts, agent.getInetAddress(), xmpPort, authenUser);
        if (session.isClosed()) {
            log().warn("collect unable to open XMP session with " + agent);
            return collectionSet;
        }
        log().debug("collect: successfully opened XMP session with" + agent);
        for (Group group : collection.getGroups().getGroup()) {
            String groupName = group.getName();
            MibObj[] mibObjects = group.getMibObj();
            XmpVar[] vars = new XmpVar[mibObjects.length];
            log().debug("collecting XMP group " + groupName + " with " + mibObjects.length + " mib objects");
            for (i = 0; i < mibObjects.length; i++) {
                vars[i] = new XmpVar(mibObjects[i].getMib(), mibObjects[i].getVar(), mibObjects[i].getInstance(), "", Xmp.SYNTAX_NULLSYNTAX);
            }
            if ((mibObjects[0].getTable() != null) && (mibObjects[0].getTable().length() != 0)) {
                String[] tableInfo = new String[3];
                tableInfo[0] = mibObjects[0].getMib();
                tableInfo[1] = mibObjects[0].getTable();
                tableInfo[2] = mibObjects[0].getInstance();
                if (handleTableQuery(group.getName(), group.getResourceType(), collectionSet, tableInfo, session, vars) == false) {
                    session.closeSession();
                    return collectionSet;
                }
            } else {
                if (handleScalarQuery(group.getName(), collectionSet, oldUptime, session, scalarResource, vars) == false) {
                    session.closeSession();
                    return collectionSet;
                }
            }
        }
        session.closeSession();
        collectionSet.setStatus(ServiceCollector.COLLECTION_SUCCEEDED);
        log().debug("XMP collect finished for " + collectionName + ", uptime for " + agent + " is " + agent.getSavedSysUpTime());
        return collectionSet;
    }

    /** {@inheritDoc} */
    public RrdRepository getRrdRepository(String collectionName) {
        log().debug("XMP getRrdRepository called for " + collectionName);
        return XmpCollectionFactory.getInstance().getRrdRepository(collectionName);
    }
}
